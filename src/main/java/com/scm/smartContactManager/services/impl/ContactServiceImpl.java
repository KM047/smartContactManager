package com.scm.smartContactManager.services.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.scm.smartContactManager.helper.ResourceNotFoundException;
import com.scm.smartContactManager.models.Contact;
import com.scm.smartContactManager.models.UserModel;
import com.scm.smartContactManager.repositories.ContactRepo;
import com.scm.smartContactManager.services.ContactService;

@Service
public class ContactServiceImpl implements ContactService {

    @Autowired
    private ContactRepo contactRepo;

    @Override
    public Contact saveContact(Contact contact) {
        String contactId = UUID.randomUUID().toString();

        contact.setId(contactId);

        return contactRepo.save(contact);

    }

    @Override
    public Contact updateContact(Contact contact) {
        var availableContact = contactRepo.findById(contact.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Contact not found"));

        availableContact.setName(contact.getName());
        availableContact.setEmail(contact.getEmail());
        availableContact.setPhoneNumber(contact.getPhoneNumber());
        availableContact.setAddress(contact.getAddress());
        availableContact.setDescription(contact.getDescription());
        availableContact.setContactImage(contact.getContactImage());
        availableContact.setFavorite(contact.isFavorite());
        availableContact.setWebsiteLink(contact.getWebsiteLink());
        availableContact.setCloudinaryImagePublicId(contact.getCloudinaryImagePublicId());

        return contactRepo.save(availableContact);
    }

    @Override
    public List<Contact> getAll() {
        return contactRepo.findAll();
    }

    @Override
    public Contact getContactById(String id) {
        return contactRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Contact not found"));
    }

    @Override
    public void deleteContact(String contactId) {

        var contact = contactRepo.findById(contactId)
                .orElseThrow(() -> new ResourceNotFoundException("Contact not found"));
        contactRepo.delete(contact);
    }

    @Override
    public Page<Contact> searchByName(String nameKeyword, int size, int page, String sortBy, String order,
            UserModel user) {

        Sort sort = Sort.Direction.fromString(order).equals(Sort.Direction.ASC) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        var pageable = PageRequest.of(page, size, sort);

        return contactRepo.findByUserAndNameContaining(user, nameKeyword, pageable);

    }

    @Override
    public Page<Contact> searchByEmail(String emailKeyword, int size, int page, String sortBy, String order,
            UserModel user) {
        Sort sort = Sort.Direction.fromString(order).equals(Sort.Direction.ASC) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        var pageable = PageRequest.of(page, size, sort);

        return contactRepo.findByUserAndEmailContaining(user, emailKeyword, pageable);
    }

    @Override
    public Page<Contact> searchByPhoneNumber(String phoneNumberKeyword, int size, int page, String sortBy, String order,
            UserModel user) {
        Sort sort = Sort.Direction.fromString(order).equals(Sort.Direction.ASC) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        var pageable = PageRequest.of(page, size, sort);

        return contactRepo.findByUserAndPhoneNumberContaining(user, phoneNumberKeyword, pageable);
    }

    @Override
    public Page<Contact> getByUser(UserModel user, int page, int size, String sortField, String sortDirection) {

        Sort sort = sortDirection.equals("desc") ? Sort.by(sortField).descending() : Sort.by(sortField).ascending();

        var pageable = PageRequest.of(page, size, sort);

        return contactRepo.findByUser(user, pageable);

    }

    @Override
    public List<Contact> getAllUserContactsById(String userId) {
        return contactRepo.findByUserId(userId);
    }

}
