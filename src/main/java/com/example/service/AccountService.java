package com.example.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.exception.*;
import com.example.entity.Account;
import com.example.repository.AccountRepository;

@Service
public class AccountService {

    @Autowired
    AccountRepository accountRepository;

    /*
     * The login will be successful if and only if the username and password
     * provided in the request body JSON match a real account existing on the
     * database. If successful, the response body should contain a JSON of the
     * account in the response body, including its accountId. The response status
     * should be 200 OK, which is the default.
     */
    public Account get(Account bodyAccount) {
        String username = bodyAccount.getUsername();
        String password = bodyAccount.getPassword();

        Account account = accountRepository.findByUsername(username).orElse(null);
        if (account == null || !account.getPassword().equals(password)) {
            throw new UnauthorizedAccountException("Error: Account password is incorrect");
        }

        return account;
    }

    /*
     * The registration will be successful if and only if the username is not
     * blank, the password is at least 4 characters long, and an Account with
     * that username does not already exist. If all these conditions are met,
     * the response body should contain a JSON of the Account, including its
     * accountId. The response status should be 200 OK, which is the default.
     * The new account should be persisted to the database. 
     */
    public Account create(Account bodyAccount) {
        String username = bodyAccount.getUsername();
        String password = bodyAccount.getPassword();

        if (accountExists(username)) {
            throw new DuplicationUsernameException("Error: Duplicate Username found!");
        }
        if (!isValidPassword(password) || !isValidUsername(username)){
            throw new InvalidAccountException("Error: Invalid account structure");
        }

        Account account = accountRepository.save(bodyAccount);
        return account;
    }

    public Account findByAccountId(Integer postedBy) {
        return accountRepository.findByAccountId(postedBy).orElse(null);
    }

    private boolean accountExists(String username) {
        return accountRepository.findByUsername(username).isPresent();
    }
    private boolean isValidUsername(String username) {
        return (username.length() > 0);
    }
    private boolean isValidPassword(String password) {
        return (password.length() >= 4);
    }

}
