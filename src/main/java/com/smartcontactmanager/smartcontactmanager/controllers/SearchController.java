package com.smartcontactmanager.smartcontactmanager.controllers;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.smartcontactmanager.smartcontactmanager.dao.Contact_Repository;
import com.smartcontactmanager.smartcontactmanager.dao.UserRepository;
import com.smartcontactmanager.smartcontactmanager.entities.Contact;
import com.smartcontactmanager.smartcontactmanager.entities.User;

@RestController
public class SearchController {
    
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private Contact_Repository contact_Repository;

    // search handler
    @GetMapping("/search/{query}")
    public ResponseEntity<?> search(@PathVariable("query") String query,Principal principal){
        System.out.println(query);

        User user=this.userRepository.getUserByUserName(principal.getName());

        List<Contact> contact = this.contact_Repository.findByNameContainingAndUser(query, user);

        return ResponseEntity.ok(contact);
    }

}
