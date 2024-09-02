package com.scm.smartContactManager.services;

import com.scm.smartContactManager.models.Contact;
import com.scm.smartContactManager.models.UserModel;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ContactService {

    Contact saveContact(Contact contact);

    Contact updateContact(Contact contact);

    List<Contact> getAll();

    List<Contact> getAllUserContactsById(String userId);

    Contact getContactById(String id);

    void deleteContact(String contactId);

    // search contacts

    Page<Contact> searchByName(String nameKeyword, int size, int page, String sortBy, String order, UserModel user);

    Page<Contact> searchByEmail(String emailKeyword, int size, int page, String sortBy, String order, UserModel user);

    Page<Contact> searchByPhoneNumber(String phoneNumberKeyword, int size, int page, String sortBy, String order,
            UserModel user);

    Page<Contact> getByUser(UserModel user, int page, int size, String sortField, String sortDirection);

}
