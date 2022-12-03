package com.smartcontactmanager.smartcontactmanager.controllers;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.smartcontactmanager.smartcontactmanager.dao.Contact_Repository;
import com.smartcontactmanager.smartcontactmanager.dao.UserRepository;
import com.smartcontactmanager.smartcontactmanager.entities.Contact;
import com.smartcontactmanager.smartcontactmanager.entities.User;
import com.smartcontactmanager.smartcontactmanager.helper.Message;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Contact_Repository contact_Repository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    // dashbord home
    @RequestMapping("/index")
    public String dashboard(Model model, Principal principal) {
        String username = principal.getName();
        // System.out.println("USERNAME : " + username);
        User user = userRepository.getUserByUserName(username);
        // System.out.println("USER " + user);
        model.addAttribute("user", user);
        // get the user using username
        return "normal/user_dashboard";
    }

    // method add form handler
    @ModelAttribute
    public void addCommonData(Model model, Principal principal) {
        String username = principal.getName();
        System.out.println("USERNAME : " + username);
        User user = userRepository.getUserByUserName(username);
        System.out.println("USER " + user);
        model.addAttribute("user", user);
        // get the user using username
        // return "normal/user_dashboard";
    }

    // open add form controller
    @GetMapping("/add-contact")
    public String openAddContactForm(Model model) {
        model.addAttribute("title", "Add Contact");
        model.addAttribute("contact", new Contact());
        return "normal/add_contact_form";

    }

    // processing add contact form
    @PostMapping("/process-contact")
    public String processContact(@ModelAttribute Contact contact,
            Principal principal, HttpSession session,Model model) {

        try {

            String name = principal.getName();
            User user = this.userRepository.getUserByUserName(name);

            // processing and uploading file

            // if the file is empty then try our
            if (contact.getImage1().isEmpty()) {
                // System.out.println("File is empty..");
                // contact.setImage("contact.jpg");
                // file the file to folder and update the name to contact
            } else {
                contact.setImage(contact.getImage1().getOriginalFilename());
                // File savefile = new ClassPathResource("D:\\smartcontact_images").getFile();
                File filepath = new File("D:\\smartcontact_images");
                // File savefile = new ClassPathResource("static/image").getFile();
                Path path = Paths.get(filepath + File.separator + contact.getImage1().getOriginalFilename());
                
                Files.copy(contact.getImage1().getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                // System.out.println("Image is uploabded...");
            }
            
            contact.setUser(user);

            user.getContacts().add(contact);
            
            this.userRepository.save(user);
            
            // System.out.println("added to database");

            // System.out.println("Data : " + contact);

            session.setAttribute("message", new Message("your contact is added !! And more..", "success"));
            model.addAttribute("content", "your contact is added !! And more..");
            model.addAttribute("type", "success");

        } catch (Exception e) {
            System.out.println("ERROR : " + e.getMessage());
            session.setAttribute("message", new Message("somthing went worng please try again", "danger"));
            model.addAttribute("content", "your contact is added !! And more..");
            model.addAttribute("type", "danger");
        }
        return "normal/add_contact_form";
    }

    // show contact handler
    // per page = 5[n]
    // current page = 0 [page]
    @GetMapping("/show-contacts/{pages}")
    public String showContacts(@PathVariable("pages") Integer page, Model m, Principal principal) {

        String username = principal.getName();
        User user = this.userRepository.getUserByUserName(username);

        Pageable pageable = PageRequest.of(page, 3);
        
        Page<Contact> contact = this.contact_Repository.findContactByUser(user.getId(), pageable);

        // for (Contact contact2 : contact) {
        //     System.out.println("image name : "+contact2.getImage());
        // }

        m.addAttribute("contacts", contact);
        
        System.out.println();
        m.addAttribute("currentpage", page);

        m.addAttribute("totalPages", contact.getTotalPages());

        return "normal/show_contact";
    }

    // showing particular contact
    @RequestMapping("/{cid}/contact")
    public String showcontactdetails(@PathVariable("cid") Integer cid,Model model,Principal principal){
        
        Optional<Contact> contactOptional = this.contact_Repository.findById(cid);
        Contact contact=contactOptional.get();

        String username=principal.getName();

        User user=this.userRepository.getUserByUserName(username);

        if(user.getId()==contact.getUser().getId()){
            model.addAttribute("contact", contact);
            model.addAttribute("title", contact.getName());
        }

        return "normal/contact_details";
    }

    // delete contact handler
    @GetMapping("/delete/{cid}")
    public String deletecontact(@PathVariable("cid") Integer cid,Model model,HttpSession session){
        
        // Optional<Contact> contactoptional=this.contact_Repository.findById(cid);
        // Contact contact = contactoptional.get();

        // this.contact_Repository.delete(contact);

        Contact contact = this.contact_Repository.findById(cid).get();
        contact.setUser(null);

        // remove image


        this.contact_Repository.delete(contact);

        session.setAttribute("message", new Message("Contact deleted successfully...","sucess"));

        return "redirect:/user/show-contacts/0";
    }

    // open update form handler
    @PostMapping("/update-contact/{cid}")
    public String updateform(@PathVariable("cid") Integer cid, Model model){

        model.addAttribute("title", "Update Contact");
        Contact contact = this.contact_Repository.findById(cid).get();
        model.addAttribute("contact", contact);

        return "normal/update_form";
    }

    // update contact handler
    @RequestMapping(value = "/process-update",method = RequestMethod.POST)
    public String update_handler(@ModelAttribute Contact contact,Model model,HttpSession session,Principal principal){

        try {
            
            Contact oldcontact = this.contact_Repository.findById(contact.getCid()).get();

            if(!contact.getImage1().isEmpty()){
                // file work rewrite
                // delete oldpicture

                File deletefile = new File("D:\\smartcontact_images");
                File file=new File(deletefile, oldcontact.getImage());
                file.delete();

                // update picture
                File filepath = new File("D:\\smartcontact_images");
                
                Path path = Paths.get(filepath + File.separator + contact.getImage1().getOriginalFilename());
                
                Files.copy(contact.getImage1().getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

                contact.setImage(contact.getImage1().getOriginalFilename());

            }else{
                contact.setImage(oldcontact.getImage());
            }

            User user=this.userRepository.getUserByUserName(principal.getName());
            contact.setUser(user);
            this.contact_Repository.save(contact);
            
            session.setAttribute("message", new Message("Your contact is updated...", "success"));

        } catch (Exception e) {
            e.printStackTrace();
        }

        // System.out.println("contact name : "+contact.getName());
        return "redirect:/user/"+contact.getCid()+"/contact";
    }

    // profile handler
    @GetMapping("/profile")
    public String your_profile(Model model){
        model.addAttribute("title", "Profile Page");
        return "normal/profile";
    }

    // open setting handler
    @GetMapping("/settings")
    public String openSetting(){
        return "normal/settings";
    }

    // change password handler
    @PostMapping("/change-password")
    public String changepassword(@RequestParam("oldpassword") String oldpassword,@RequestParam("newpassword") String newpassword,Principal principal,HttpSession session){
        
        String username=principal.getName();
        User currentuser = this.userRepository.getUserByUserName(username);
        System.out.println("current password : "+currentuser.getPassword());

        if(this.bCryptPasswordEncoder.matches(oldpassword, currentuser.getPassword())){
            
            System.out.println("hello");

            // change the password
            currentuser.setPassword(this.bCryptPasswordEncoder.encode(newpassword));
            this.userRepository.save(currentuser);

            session.setAttribute("message", new Message("Your Password is successfully change", "success"));

        }else{
            System.out.println("password is not change");
            // error
            session.setAttribute("message", new Message("Please Enter currect old password !!", "danger"));

            return "redirect:/user/settings";
        }

        return "redirect:/user/index";
    }

    @RequestMapping("/emp_form")
    public String emp_form(){
        return "empform";
    }


    @PostMapping("/do_registers")
    public String regi(@RequestParam("name") String name,@RequestParam("email") String email){
        System.out.println("Name os :"+name);
        System.out.println("email : "+email);
        return "";
    }
    
}
