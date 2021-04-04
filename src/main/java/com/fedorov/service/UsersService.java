package com.fedorov.service;

import com.fedorov.dao.ContactDAOImpl;
import com.fedorov.dao.UserDAOImpl;
import com.fedorov.model.Contact;
import com.fedorov.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsersService {

    private UserDAOImpl userDAOImpl;
    private ContactDAOImpl contactDAOImpl;

    @Autowired
    public UsersService(UserDAOImpl userDAOImpl, ContactDAOImpl contactDAOImpl) {
        this.userDAOImpl = userDAOImpl;
        this.contactDAOImpl = contactDAOImpl;
    }

    public List<User> getAllUsers() {
        return userDAOImpl.getAllUsers();
    }

    public User getUserById(int id) {
        return userDAOImpl.getUserById(id);
    }

    public User addUser(User user) {
       return userDAOImpl.addUser(user);
    }

    public boolean updateUser(int id, User user) {
        return userDAOImpl.updateUser(id, user);
    }

    public boolean deleteUser(int id) {
        return userDAOImpl.deleteUser(id);
    }

    public void addContactToPhoneBook(int userId, Contact contact) {
        User user = userDAOImpl.getUserById(userId);

        Contact contactToAdd = contactDAOImpl.addContact(contact);
        user.addContactToPhoneBook(contactToAdd);
    }

    public boolean deleteContactFromPhoneBook(int userId, int contactId) {
        boolean isDeleted = false;
        User user = userDAOImpl.getUserById(userId);
        if (user == null) {
            return isDeleted;
        }

        List<Contact> contacts = contactDAOImpl.getAllContacts();
        Contact contactToRemove = contacts.stream().filter(storedContact -> storedContact.getId() == contactId).findAny().orElse(null);
        if (contactToRemove == null) {
            return isDeleted;
        }

        user.deleteContactFromPhoneBook(contactToRemove);
        contactDAOImpl.deleteContact(contactId);
        isDeleted = true;
        return isDeleted;
    }

    public List<User> getUserByName(String searchString) {
        String searchStringLowCase = searchString.toLowerCase();

        List<User> users = userDAOImpl.getAllUsers();
        List<User> foundUsers = users.stream().filter(storedUser -> {
            String storedUserName = storedUser.getFullName();
            storedUserName = storedUserName.toLowerCase();

            return storedUserName.contains(searchStringLowCase);
        }).collect(Collectors.toList());

        return foundUsers;
    }

    public List<Contact> getUsersContactsByPhoneNumber(int userId, String phoneNumber) {
        User user = userDAOImpl.getUserById(userId);
        List<Contact> usersContacts = user.getPhoneBook();

        List<Contact> foundContacts = usersContacts.stream().filter(storedContact -> storedContact.getPhoneNumber().equals(phoneNumber)).collect(Collectors.toList());
        return foundContacts;
    }
}
