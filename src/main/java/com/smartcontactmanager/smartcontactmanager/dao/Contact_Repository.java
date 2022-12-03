package com.smartcontactmanager.smartcontactmanager.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.smartcontactmanager.smartcontactmanager.entities.Contact;
import com.smartcontactmanager.smartcontactmanager.entities.User;

public interface Contact_Repository extends JpaRepository<Contact,Integer>{
    
    // pegination 
    @Query("from Contact as c where c.user.id =:userid")
    // currentpage 
    // Contact per page - 5
    public Page<Contact> findContactByUser(@Param("userid") int userid,Pageable pageable);

    // search name
    public List<Contact> findByNameContainingAndUser(String namekeyword,User user);

}
