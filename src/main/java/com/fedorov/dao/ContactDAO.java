package com.fedorov.dao;

import com.fedorov.model.Contact;

import java.util.List;

public interface ContactDAO {

    Contact addContact(Contact contact);

    Contact getContactById(int id);

    List<Contact> getAllContacts();

    boolean updateContact(int id, Contact contact);

    boolean deleteContact(int id);
}
