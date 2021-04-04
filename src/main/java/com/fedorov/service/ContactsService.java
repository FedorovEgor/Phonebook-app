package com.fedorov.service;

import com.fedorov.dao.ContactDAOImpl;
import com.fedorov.model.Contact;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContactsService {

    private ContactDAOImpl contactDAOImpl;

    @Autowired
    public ContactsService(ContactDAOImpl contactDAOImpl) {
        this.contactDAOImpl = contactDAOImpl;
    }

    public Contact addContact(Contact contact) {
       return contactDAOImpl.addContact(contact);
    }

    public Contact getContactById(int id) {
        return contactDAOImpl.getContactById(id);
    }

    public List<Contact> getAllContacts() {
        return contactDAOImpl.getAllContacts();
    }

    public boolean updateContact(int id, Contact contact) {
        return contactDAOImpl.updateContact(id, contact);
    }

    public boolean deleteContact(int id) {
        return contactDAOImpl.deleteContact(id);
    }

    public List<Contact> getContactByPhoneNumber(String phoneNumber) {
        List<Contact> contacts = contactDAOImpl.getAllContacts();

        List<Contact> foundContacts = contacts.stream().filter(storedContact -> {
            return storedContact.getPhoneNumber().equals(phoneNumber);
        }).collect(Collectors.toList());

        return foundContacts;
    }
}
