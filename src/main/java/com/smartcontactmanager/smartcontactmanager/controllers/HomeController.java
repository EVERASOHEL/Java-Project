package com.smartcontactmanager.smartcontactmanager.controllers;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import com.smartcontactmanager.smartcontactmanager.dao.UserRepository;
import com.smartcontactmanager.smartcontactmanager.entities.User;
import com.smartcontactmanager.smartcontactmanager.helper.Message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository repository;

    @RequestMapping("/")
    public String Home(Model model) {
        model.addAttribute("title", "Home - contact Manager");
        return "home";
    }

    @RequestMapping("/about")
    public String about(Model model) {
        model.addAttribute("title", "about - smart contact manager");
        return "about";
    }

    @RequestMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("title", "Register - smart contact manager");
        model.addAttribute("user", new User());
        return "signup";
    }

    // this handler for register user
    @RequestMapping(value = "/do_register", method = RequestMethod.POST)
    public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult result,
            @RequestParam(value = "agreement", defaultValue = "false") boolean agreement, HttpSession session,
            Model model) {

        try {

            user.setRole("ROLE_USER");
            user.setEnabled(true);
            user.setImageurl("default.png");
            user.setPassword(passwordEncoder.encode(user.getPassword()));

            if (!agreement) {
                System.out.println("you have not agreed term and condition");
                throw new Exception("you have not agreed term and condition");
            }

            if (result.hasErrors()) {
                System.out.println("ERROR : " + result.toString());
                model.addAttribute("user", user);
                return "signup";
            }
            
            User result1 = this.repository.save(user);

            model.addAttribute("user", user);

            session.setAttribute("message", new Message("Successfully Registered !!", "alert-success"));

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("user", user);
            session.setAttribute("message", new Message("Somthing went wrong!!" + e.getMessage(), "alert-danger"));
        }

        return "signup";
    }
    //handler for custom login
    @GetMapping("/signin")
    public String customelogin(Model model){
        model.addAttribute("title", "loginpage");
        return "login";
    }

}
