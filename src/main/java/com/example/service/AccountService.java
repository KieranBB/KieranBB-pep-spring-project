package com.example.service;

import com.example.entity.Account;
import com.example.exception.OperationFailedException;
import com.example.exception.UsernameConflictException;
import com.example.repository.AccountRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.security.sasl.AuthenticationException;

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


    public Account registerUser(Account newUser) throws UsernameConflictException, OperationFailedException{
        String uName = newUser.getUsername();
        if (usernameExists(uName)) throw new UsernameConflictException("UsernameAlreadyTaken");
        String pass = newUser.getPassword();
        boolean uNameGood = (uName.strip().length() > 0);
        boolean passGood = (pass.length() >= 4);
        if (uNameGood && passGood) {
            return accountRepository.save(new Account(uName,pass));
        } else throw new OperationFailedException("Registration Unsuccessful");
    }


    public Account loginUser(Account user) throws AuthenticationException{
        Account acct = accountRepository.findByUsername(user.getUsername());
        if (acct != null) {
            if (acct.getPassword().equals(user.getPassword())) return acct;
        }
        throw new AuthenticationException("Login Unsuccessful");
    }







}
