package com.scm.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.scm.entities.Contact;
import com.scm.entities.User;

public interface ContactService {

    Contact save(Contact contact);

    Contact update(Contact contact);

    List<Contact> getAll();

    Contact getById(String id);

    void delete(String id);

    Page<Contact> searchByName(String name,int page,int size,String sortBy,String SortDirection,User user);

    Page<Contact> searchByEmail(String email,int page,int size,String sortBy,String SortDirection,User user);

    Page<Contact> searchByPhoneNumber(String phoneNumber,int page,int size,String sortBy,String SortDirection,User user);

    List<Contact> getUserByUserId(String userId);

    Page<Contact> getContactsByUser(User user,int page,int size,String sortBy,String sortDirection);

}
