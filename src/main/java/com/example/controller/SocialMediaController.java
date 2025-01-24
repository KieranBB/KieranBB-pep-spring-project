package com.example.controller;

import com.example.service.*;
import com.example.entity.*;
import com.example.exception.OperationFailedException;
import com.example.exception.UsernameConflictException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.security.sasl.AuthenticationException;

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
    public Account registerUser(@RequestBody Account acct) throws UsernameConflictException, OperationFailedException{
        return accountService.registerUser(acct);
    }

    @PostMapping("/login")
    public Account loginUser(@RequestBody Account acct) throws AuthenticationException{
        return accountService.loginUser(acct);
    }

    @PostMapping("/messages")
    public Message createMessage(@RequestBody Message mssg) throws OperationFailedException{
        return messageService.createMessage(mssg);
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
//
    @PatchMapping("messages/{messageId}")
    public int updateMessageById(@PathVariable("messageId") int id, @RequestBody Message newMssg) throws OperationFailedException{
        return messageService.updateMessageById(id, newMssg);
    }

    @GetMapping("accounts/{accountId}/messages")
    public List<Message> getMessagesByUser(@PathVariable("accountId") int acctId) {
        return messageService.getMessagesByUser(acctId);
    }


    
    @ExceptionHandler(UsernameConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleUsernameConflictException(UsernameConflictException ex) {
        return ex.getMessage();
    }
    
    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String handleAuthenticationException(AuthenticationException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(OperationFailedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleOperationFailedException(OperationFailedException ex) {
        return ex.getMessage();
    }


}
