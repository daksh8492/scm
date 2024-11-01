package com.scm.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.scm.entities.Contact;
import com.scm.entities.User;
import com.scm.helpers.ResourceNotFoundException;
import com.scm.repositories.ContactRepo;
import com.scm.service.ContactService;

@Service
public class ContactServiceImpl implements ContactService {

    @Autowired
    private ContactRepo contactRepo;

    @Override
    public Contact save(Contact contact) {
        contact.setId(UUID.randomUUID().toString());
        return contactRepo.save(contact);
    }

    @Override
    public List<Contact> getAll() {
        return contactRepo.findAll();
    }

    @Override
    public Contact getById(String id) {
        return contactRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Contact not found with id " + id));
    }

    @Override
    public void delete(String id) {
        Contact contact = contactRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Contact not found with id " + id));
        contactRepo.delete(contact);
    }

    @Override
    public List<Contact> getUserByUserId(String userId) {
        return contactRepo.findByUserId(userId);
    }

    @Override
    public Page<Contact> getContactsByUser(User user, int page, int size, String sortBy, String direction) {
        Sort sort = direction.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        var pageable = PageRequest.of(page, size, sort);
        return contactRepo.findByUser(user, pageable);
    }

    // @Override
    // public Page<Contact> searchByName(String name, int page, int size, String sortBy, String SortDirection) {
    //     Sort sort = SortDirection.equals("desc")? Sort.by(Direction.DESC) : Sort.by(Direction.ASC);
    //     var pageable = PageRequest.of(page, size, sort);
    //     return contactRepo.findByNameContaining(name,pageable);
    // }
    // @Override
    // public Page<Contact> searchByEmail(String email, int page, int size, String sortBy, String SortDirection) {
    //     Sort sort = SortDirection.equals("desc")? Sort.by(Direction.DESC) : Sort.by(Direction.ASC);
    //     var pageable = PageRequest.of(page, size, sort);
    //     return contactRepo.findByEmailContaining(email,pageable);
    // }
    // @Override
    // public Page<Contact> searchByPhoneNumber(String phoneNumber, int page, int size, String sortBy,String SortDirection) {
    //     Sort sort = SortDirection.equals("desc")? Sort.by(Direction.DESC) : Sort.by(Direction.ASC);
    //     var pageable = PageRequest.of(page, size, sort);
    //     return contactRepo.findByPhoneNumberContaining(phoneNumber, pageable);
    // }
    @Override
    public Page<Contact> searchByName(String name, int page, int size, String sortBy, String sortDirection,User user) {
        Sort sort = sortDirection.equalsIgnoreCase("desc") ? Sort.by(Sort.Direction.DESC, sortBy) : Sort.by(Sort.Direction.ASC, sortBy);
        var pageable = PageRequest.of(page, size, sort);
        return contactRepo.findByUserAndNameContaining(user, name, pageable);
    }

    @Override
    public Page<Contact> searchByEmail(String email, int page, int size, String sortBy, String sortDirection,User user) {
        Sort sort = sortDirection.equalsIgnoreCase("desc") ? Sort.by(Sort.Direction.DESC, sortBy) : Sort.by(Sort.Direction.ASC, sortBy);
        var pageable = PageRequest.of(page, size, sort);
        return contactRepo.findByUserAndEmailContaining(user, email, pageable);
    }

    @Override
    public Page<Contact> searchByPhoneNumber(String phoneNumber, int page, int size, String sortBy, String sortDirection,User user) {
        Sort sort = sortDirection.equalsIgnoreCase("desc") ? Sort.by(Sort.Direction.DESC, sortBy) : Sort.by(Sort.Direction.ASC, sortBy);
        var pageable = PageRequest.of(page, size, sort);
        return contactRepo.findByUserAndPhoneNumberContaining(user, phoneNumber, pageable);
    }

    @Override
    public Contact update(Contact contact) {
        var contactOld = contactRepo.findById(contact.getId()).orElseThrow(() -> {
            return new ResourceNotFoundException("Contact not found");
        });
        contactOld.setName(contact.getName());
        contactOld.setEmail(contact.getEmail());
        contactOld.setPhoneNumber(contact.getPhoneNumber());
        contactOld.setAddress(contact.getAddress());
        contactOld.setDescription(contact.getDescription());
        contactOld.setPicture(contact.getPicture());
        contactOld.setFavourite(contact.isFavourite());
        contactOld.setWebsiteLink(contact.getWebsiteLink());
        contactOld.setLinkedInLink(contact.getLinkedInLink());
        contactOld.setCloudImageId(contact.getCloudImageId());
        return contactRepo.save(contactOld);
    }

}
