package com.example.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

        Account account = isAccount(username);
        if (account == null) {
            return null;
        }
        if (account.getPassword() != password) {
            return null;
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

        if (isValidPassword(password) && isValidUsername(username)) {
            Account account = accountRepository.save(bodyAccount);
            if (account != null) {
                return account;
            }
        }
        return null;
    }
    private Account isAccount(String username) {
        Optional<Account> optionalAccount= accountRepository.findByUsername(username);
        return (optionalAccount.isPresent()) ? optionalAccount.get() : null;
    }
    private Boolean isValidUsername(String username) {
        // TODO: Add any extra username validation
        return (username.length() > 0);
    }

    private Boolean isValidPassword(String password) {
        // TODO: Add any extra password validation
        return (password.length() >= 4);

    }

    public Account findByAccountId(Integer postedBy) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByAccountId'");
    }
}
