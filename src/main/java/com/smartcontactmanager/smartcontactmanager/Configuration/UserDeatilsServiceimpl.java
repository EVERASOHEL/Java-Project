package com.smartcontactmanager.smartcontactmanager.Configuration;

import com.smartcontactmanager.smartcontactmanager.dao.UserRepository;
import com.smartcontactmanager.smartcontactmanager.entities.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserDeatilsServiceimpl implements UserDetailsService{

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
        // feching user from datbase

        User user=userRepository.getUserByUserName(username);

        if(user==null){
            throw new UsernameNotFoundException("could not found user !!");
        }

        CustomUserDetails customUserDetails=new CustomUserDetails(user);
        
        return customUserDetails;
    }
    
}
