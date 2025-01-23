package com.example.controller;

import com.example.service.*;
import com.example.entity.*;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.transaction.Transactional;
import java.util.List;


/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */

@RestController
public class SocialMediaController {
    private AccountService accountService;
    private MessageService messageService;

    @Autowired
    public SocialMediaController(AccountService accountService, MessageService messageService) {
        this.accountService = accountService;
        this.messageService = messageService;
    }
    

    @PostMapping("/register")
    @Transactional
    public ResponseEntity registerUser(@RequestBody Account acct) {
        if (!accountService.usernameExists(acct.getUsername())) {
            Account res = accountService.registerUser(acct);
            if (res != null) {
                return ResponseEntity.ok(res);
            } else return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Registration Unsuccessful");
        } else return ResponseEntity.status(HttpStatus.CONFLICT).body("Username Already Taken");
    }

    @PostMapping("/login")
    public ResponseEntity loginUser(@RequestBody Account acct) {
        Account res = accountService.loginUser(acct);
        if (res != null) {
            return ResponseEntity.ok(res);
        } else return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Username or Password Incorrect");
    }

    @PostMapping("/messages")
    public ResponseEntity createMessage(@RequestBody Message mssg) {
        Message res = messageService.createMessage(mssg);
        if (res != null) {
            return ResponseEntity.ok(res);
        } else return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Message Creation Unsuccessful");
    }

    @GetMapping("/messages")
    public List<Message> getMessages() {
        return messageService.getMessages();
    }


    @GetMapping("/messages/{messageId}")
    public Message getMessageById(@PathVariable("messageId") int id) {
        return messageService.getMessageById(id);
    }

    @DeleteMapping("messages/{messageId}")
    public ResponseEntity deleteMessageById(@PathVariable("messageId") int id) {
        int res = messageService.deleteMessageById(id);
        if (res == 1) {
            return ResponseEntity.ok(1);
        } else return ResponseEntity.ok().build();
    }

    @PatchMapping("messages/{messageId}")
    public ResponseEntity updateMessageById(@PathVariable("messageId") int id, @RequestBody Message newMssg) {
        int res = messageService.updateMessageById(id, newMssg);
        if (res == 1) {
            return ResponseEntity.ok(1);
        } else return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Message Update Unsuccessful");
    }

    @GetMapping("accounts/{accountId}/messages")
    public List<Message> getMessagesByUser(@PathVariable("accountId") int acctId) {
        return messageService.getMessagesByUser(acctId);
    }


}
