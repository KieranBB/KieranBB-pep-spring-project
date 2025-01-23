package com.example.service;

import com.example.repository.MessageRepository;
import com.example.entity.Message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class MessageService {

    private MessageRepository messageRepository;
    private AccountService accountService;

    @Autowired
    public MessageService(MessageRepository messageRepository, AccountService accountService) {
        this.messageRepository = messageRepository;
        this.accountService = accountService;
    }

    @Transactional
    public Message createMessage(Message newMssg) {
        String mssgTxt = newMssg.getMessageText();
        int pstBy = newMssg.getPostedBy();
        boolean nonBlank = (mssgTxt.strip().length() > 0);
        boolean lenGood = (mssgTxt.length() <= 255);
        boolean usrReal = accountService.userExists(pstBy);
        if (nonBlank && lenGood && usrReal) {
            Message buffer = new Message(pstBy, mssgTxt, newMssg.getTimePostedEpoch());
            return messageRepository.save(buffer);
        } else return null;
    }


    public List<Message> getMessages() {
        return messageRepository.findAll();
    }


    public Message getMessageById(int id) {
        return messageRepository.findById(id).orElse(null);
    }

    @Transactional
    public int deleteMessageById(int id) {
        boolean mssgExists = messageRepository.existsById(id);
        if (mssgExists) {
            messageRepository.deleteById(id);
            return 1;
        }
        return 0;
    }

    @Transactional
    public int updateMessageById(int id, Message newMssg) {
        Optional<Message> mssgO = messageRepository.findById(id);
        String newTxt = newMssg.getMessageText();
        boolean newNonBlank = (newTxt.strip().length() > 0);
        boolean newLenGood = (newTxt.length() <= 255);

        if (mssgO.isPresent() && newNonBlank && newLenGood) {
            Message mssg = mssgO.get();
            mssg.setMessageText(newTxt);
            messageRepository.save(mssg);
            return 1;
        }
        return 0;
    }



    public List<Message> getMessagesByUser(int acctId) {
        return messageRepository.findByPostedBy(acctId);
    }



}
