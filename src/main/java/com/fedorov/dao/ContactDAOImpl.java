package com.fedorov.dao;

import com.fedorov.model.Contact;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ContactDAOImpl implements ContactDAO {
    private static int CONTACTS_COUNT;
    private List<Contact> contacts = new ArrayList<>();

    @Override
    public Contact addContact(Contact contact) {
        Contact contactToSave = new Contact(++CONTACTS_COUNT, contact.getContactName(), contact.getPhoneNumber());
        contacts.add(contactToSave);
        return contactToSave;
    }

    @Override
    public Contact getContactById(int id) {
        Contact contactToRetrieve = contacts.stream().filter(contact -> contact.getId() == id).findAny().orElse(null);
        return contactToRetrieve;
    }

    @Override
    public List<Contact> getAllContacts() {
        return contacts;
    }

    @Override
    public boolean updateContact(int id, Contact contact) {
        boolean isUpdated = false;

        Contact contactToUpdate = contacts.stream().filter(storedContact -> storedContact.getId() == id).findAny().orElse(null);

        if (contactToUpdate != null) {
            contactToUpdate.setContactName(contact.getContactName());
            contactToUpdate.setPhoneNumber(contact.getPhoneNumber());
            isUpdated = true;
        }
        return isUpdated;
    }

    @Override
    public boolean deleteContact(int id) {
        boolean isDeleted = false;

        Contact contactToDelete = contacts.stream().filter(storedContact -> storedContact.getId() == id).findAny().orElse(null);

        if (contactToDelete != null) {
            contacts.remove(contactToDelete);
            isDeleted = true;
        }
        return isDeleted;
    }
}
