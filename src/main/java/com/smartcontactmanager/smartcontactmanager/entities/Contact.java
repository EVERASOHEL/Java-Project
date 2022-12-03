package com.smartcontactmanager.smartcontactmanager.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "CONTACT")
public class Contact {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int cid;
    private String name;
    private String secondname;
    private String phone;
    private String work;
    private String email;
    private String image;
    @Column(length = 5000)
    private String description;
    
    @ManyToOne
    @JsonIgnore
    private User user;

    @Transient
    private MultipartFile image1;
    
    public MultipartFile getImage1() {
        return image1;
    }
    
    public Contact() {

    }
    
    public Contact(int cid, String name, String secondname, String work, String email, String phone, String image,
            String description) {
        this.cid = cid;
        this.name = name;
        this.secondname = secondname;
        this.work = work;
        this.email = email;
        this.phone = phone;
        this.image = image;
        this.description = description;
    }
    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSecondname() {
        return secondname;
    }

    public void setSecondname(String secondname) {
        this.secondname = secondname;
    }

    public String getWork() {
        return work;
    }

    public void setWork(String work) {
        this.work = work;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Contact [cid=" + cid + ", description=" + description + ", email=" + email + ", image=" + image
                + ", name=" + name + ", phone=" + phone + ", secondname=" + secondname + ", user=" + user + ", work="
                + work + "]";
    }

    public void setImage1(MultipartFile image1) {
        this.image1 = image1;
    }

}
