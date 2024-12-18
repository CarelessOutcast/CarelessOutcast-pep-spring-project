package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.exception.*;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.repository.MessageRepository;

@Service
public class MessageService {

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    AccountService accountService;

    /*
     * The creation of the message will be successful if and only if the messageText
     * is not blank, is not over 255 characters, and postedBy refers to a real,
     * existing user. 
     * 
    */
    public Message create(Message bodyMessage) {
        if (bodyMessage.getMessageText().isBlank()
                || bodyMessage == null
                || bodyMessage.getMessageText().length() > 255) {
            throw new InvalidMessageException("Error: Message is not valid!");
        }

        Account account = accountService.findByAccountId(bodyMessage.getPostedBy());
        if (account == null) { 
            throw new InvalidAccountException("Error: Poster needs to exist!");
         }

        return messageRepository.save(bodyMessage);
    }

    /**
     * The response body should contain a JSON representation of a list containing all
     * messages retrieved from the database. It is expected for the list to simply be
     * empty if there are no messages. The response status should always be 200, which
     * is the default.
     * 
     * @return
     */
    public List<Message> get() {
        return messageRepository.findAll();
    }

    /**
     * 
     * The response body should contain a JSON representation of the message
     * identified by the messageId. It is expected for the response body to simply
     * be empty if there is no such message. The response status should always be
     * 200, which is the default.
     * 
     * @return
     */
    public Message get(int messageId) {
        return messageRepository.findByMessageId(messageId).orElse(null);
    }

    /**
     * The request body should contain a new messageText values to replace the
     * message identified by messageId. The request body can not be guaranteed
     * to contain any other information. The update of a message should be
     * successful if and only if the message id already exists and the new
     * messageText is not blank and is not over 255 characters. 
     * 
     * @return
     */
    public int update(int messageId, Message bodyMessage) {
        Message existingMessage = messageRepository.findByMessageId(messageId).orElse(null);
        
        if (existingMessage == null) {
            throw new InvalidMessageException("Error; Message need to exist!");
        }
        
         // Check message
         String messageText = bodyMessage.getMessageText();
         if ((messageText.isBlank()) || (messageText.length() > 255)) {
             throw new InvalidMessageException("Error: Message structure incorrect!");
         }
         existingMessage.setMessageText(bodyMessage.getMessageText());

        messageRepository.save(existingMessage);
        return 1;
    }

    /**
     * The deletion of an existing message should remove an existing message
     * from the database. If the message existed, the response body should
     * contain the number of rows updated (1).  
     * 
     * @return
     */
    public int delete(int messageId) {
        if (!messageRepository.existsById(messageId)) { 
            throw new MessageNotFoundException("Error: Message not Found!");
        }
        messageRepository.deleteById(messageId);
        return 1;
    }

    /**
     * 
     * The response body should contain a JSON representation of a list containing all
     * messages posted by a particular user, which is retrieved from the database.
     * It is expected for the list to simply be empty if there are no messages. The
     * response status should always be 200, which is the default.
     * 
     * @return
     */
    public List<Message> getMessageByUserId(int accountId) {
        List<Message> messages = messageRepository.findAllByPostedBy(accountId);
        return messages;
    }
}
