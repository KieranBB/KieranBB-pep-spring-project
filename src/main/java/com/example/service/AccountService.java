package com.example.service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    private AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }



    public Boolean userExists(int id) {
        return accountRepository.existsById(id);
    }

    public boolean usernameExists(String uName) {
        return accountRepository.existsByUsername(uName);
    }


    //Controller checks for account existing before reaching here
    public Account registerUser(Account newUser) {
        boolean uNameGood = (newUser.getUsername().strip().length() > 0);
        boolean passGood = (newUser.getPassword().length() >= 4);
        if (uNameGood && passGood) {
            Account buffer = new Account(newUser.getUsername(),newUser.getPassword());
            return accountRepository.save(buffer);
        } else return null;
    }


    public Account loginUser(Account user) {
        Account acct = accountRepository.findByUsername(user.getUsername());
        if (acct != null) {
            if (acct.getPassword().equals(user.getPassword())) return acct;
        }
        return null;
    }







}
