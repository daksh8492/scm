package com.scm.controller;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.scm.entities.Contact;
import com.scm.entities.User;
import com.scm.forms.ContactForm;
import com.scm.forms.ContactSearchForm;
import com.scm.helpers.AppConstants;
import com.scm.helpers.Helper;
import com.scm.helpers.Message;
import com.scm.helpers.MessageType;
import com.scm.service.ContactService;
import com.scm.service.ImageService;
import com.scm.service.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/user/contacts")
public class ContactController {

    @Autowired
    private ContactService contactService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private UserService userService;

    Logger logger = LoggerFactory.getLogger(ContactController.class);

    @GetMapping("/add")
    public String addContactView(Model model) {
        ContactForm contactForm = new ContactForm();
        model.addAttribute("contactForm", contactForm);
        return "user/add_contact";
    }

    @PostMapping("/add")
    public String saveContact(@Valid @ModelAttribute("contactForm") ContactForm contactForm, BindingResult result, Authentication authentication, HttpSession session) {

        if (result.hasErrors()) {
            session.setAttribute("message", Message.builder().content("Could not add the contact").type(MessageType.red).build());
            System.out.println("The results are" + result.toString());
            return "user/add_contact";
        }

        String username = Helper.getEmailOfLoggedInUsser(authentication);

        String filename = UUID.randomUUID().toString();

        String fileUrl = imageService.uploadImage(contactForm.getContactImage(), filename);

        Contact contact = new Contact();

        User user = userService.getUserByEmail(username);

        contact.setAddress(contactForm.getAddress());
        contact.setDescription(contactForm.getDescription());
        contact.setEmail(contactForm.getEmail());
        contact.setFavourite(contactForm.isFavourite());
        contact.setLinkedInLink(contactForm.getLinkedInLink());
        contact.setName(contactForm.getName());
        contact.setPhoneNumber(contactForm.getPhoneNumber());
        contact.setUser(user);
        contact.setWebsiteLink(contactForm.getWebsiteLink());
        contact.setPicture(fileUrl);
        contact.setCloudImageId(filename);

        contactService.save(contact);
        session.setAttribute("message", Message.builder().content("Contact added successfully").type(MessageType.green).build());

        return "redirect:/user/contacts/add";
    }

    @GetMapping()
    public String viewContact(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = AppConstants.PAGE_SIZE + "") int size,
            @RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
            @RequestParam(value = "direction", defaultValue = "asc") String direction,
            Model model, Authentication authentication) {
        String username = Helper.getEmailOfLoggedInUsser(authentication);
        User user = userService.getUserByEmail(username);

        Page<Contact> pageContact = contactService.getContactsByUser(user, page, size, sortBy, direction);

        model.addAttribute("pageContact", pageContact);
        model.addAttribute("pageSize", AppConstants.PAGE_SIZE);

        return "user/contacts";
    }

    @GetMapping("/search")
    public String searchHandler(
            @ModelAttribute ContactSearchForm contactSearchForm,
            @RequestParam(value = "size", defaultValue = AppConstants.PAGE_SIZE + "") int size,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
            @RequestParam(value = "direction", defaultValue = "asc") String sortDirection,
            Model model, Authentication authentication) {

        logger.info("field : {} , keyword : {}", contactSearchForm.getField(), contactSearchForm.getValue());

        var user = userService.getUserByEmail(Helper.getEmailOfLoggedInUsser(authentication));

        Page<Contact> pageContact;
        if (contactSearchForm.getField().equalsIgnoreCase("name")) {
            pageContact = contactService.searchByName(contactSearchForm.getValue(), page, size, sortBy, sortDirection, user);
        } else if (contactSearchForm.getField().equalsIgnoreCase("email")) {
            pageContact = contactService.searchByEmail(contactSearchForm.getValue(), page, size, sortBy, sortDirection, user);
        } else {
            pageContact = contactService.searchByPhoneNumber(contactSearchForm.getValue(), page, size, sortBy, sortDirection, user);
        }

        logger.info("This is the page content :::::::  " + pageContact);

        model.addAttribute("pageContact", pageContact);
        model.addAttribute("pageSize", AppConstants.PAGE_SIZE);
        model.addAttribute("contactSearchForm", contactSearchForm);

        return "user/search";
    }

    @GetMapping("/delete/{contactId}")
    public String deleteContact(@PathVariable String contactId) {
        contactService.delete(contactId);
        return "redirect:/user/contacts";
    }

    @GetMapping("/view/{id}")
    public String updateContactFormView(@PathVariable String id, Model model) {
        Contact contact = contactService.getById(id);
        ContactForm contactForm = new ContactForm();
        contactForm.setAddress(contact.getAddress());
        contactForm.setName(contact.getName());
        contactForm.setEmail(contact.getEmail());
        contactForm.setPhoneNumber(contact.getPhoneNumber());
        contactForm.setDescription(contact.getDescription());
        contactForm.setFavourite(contact.isFavourite());
        contactForm.setWebsiteLink(contact.getWebsiteLink());
        contactForm.setLinkedInLink(contact.getLinkedInLink());
        contactForm.setPicture(contact.getPicture());
        model.addAttribute("contactForm", contactForm);
        model.addAttribute("contactId", id);
        return "user/update_contact_view";
    }

    @PostMapping("/update/{contactId}")
    public String postMethodName(@PathVariable String contactId,@Valid @ModelAttribute ContactForm contactForm,BindingResult bindingResult, Model model) {

        if(bindingResult.hasErrors()){
            return "user/update_contact_view";
        }

        var con = new Contact();
        con.setId(contactId);
        con.setName(contactForm.getName());
        con.setEmail(contactForm.getEmail());
        con.setPhoneNumber(contactForm.getPhoneNumber());
        con.setAddress(contactForm.getAddress());
        con.setDescription(contactForm.getDescription());
        con.setFavourite(contactForm.isFavourite());
        con.setWebsiteLink(contactForm.getWebsiteLink());
        con.setLinkedInLink(contactForm.getLinkedInLink());

        if (contactForm.getContactImage() != null && !contactForm.getContactImage().isEmpty()) {
            String filename = UUID.randomUUID().toString();
            String imageUrl = imageService.uploadImage(contactForm.getContactImage(), filename);
            con.setCloudImageId(filename);
            con.setPicture(imageUrl);
        }

        contactService.update(con);
        model.addAttribute("message", Message.builder().content("Contact updated").type(MessageType.green).build());

        return "redirect:/user/contacts/view/" + contactId;
    }

}
