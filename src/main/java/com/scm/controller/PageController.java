package com.scm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.scm.entities.User;
import com.scm.forms.UserForm;
import com.scm.helpers.Message;
import com.scm.helpers.MessageType;
import com.scm.service.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class PageController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String getIndex() {
        return "redirect:/home";
    }

    @GetMapping("/home")
    public String getHome() {
        return "home";
    }

    @GetMapping("/about")
    public String getAbout(){
        return "about";
    }

    @GetMapping("/services")
    public String getServices(){
        return "services";
    }


    @GetMapping("/contact")
    public String getContact(){
        return "contact";
    }

    @GetMapping("/login")
    public String getLogin(){
        return "login";
    }

    @GetMapping("/register")
    public String getRegister(Model model){
        UserForm userForm = new UserForm();
        model.addAttribute("userForm",userForm);
        return "register";
    }

    @PostMapping("/do-register")
    public String processRegister(@Valid @ModelAttribute UserForm userForm,BindingResult rBindingResult,HttpSession session){
        System.out.println("Processing register");
        System.out.println(userForm.toString());

        if(rBindingResult.hasErrors()){
            return "register";
        }

        User user = new User();
        user.setName(userForm.getName());
        user.setEmail(userForm.getEmail());
        user.setPassword(userForm.getPassword());
        user.setAbout(userForm.getAbout());
        user.setPhoneNumber(userForm.getPhoneNumber());
        // user.setEnabled(false);
        user.setProfilePic("https://www.google.com/url?sa=i&url=https%3A%2F%2Fwww.pinterest.com%2Fpin%2Ftiktok-default-profile-picture-sticker-sticker-for-sale-by-tgamez522--679902875018963865%2F&psig=AOvVaw0Ud7MYmyxZWBnqvHBeyJgJ&ust=1728716167941000&source=images&cd=vfe&opi=89978449&ved=0CBEQjRxqFwoTCNiv2NbfhYkDFQAAAAAdAAAAABAE");
        
        userService.saveUser(user);
        System.out.println("User Saved");

        Message message = Message.builder().content("Registration Successfull").type(MessageType.green).build();

        session.setAttribute("message",message);

        return "redirect:/register";
    }
    

}
