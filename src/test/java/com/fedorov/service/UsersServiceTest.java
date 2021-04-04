package com.fedorov.service;

import com.fedorov.dao.ContactDAOImpl;
import com.fedorov.dao.UserDAOImpl;
import com.fedorov.model.Contact;
import com.fedorov.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UsersServiceTest {

    @Mock
    private UserDAOImpl userDAO;
    @Mock
    private ContactDAOImpl contactDAO;

    @InjectMocks
    private UsersService usersService;

    @Test
    public void getAllUsers_listOfAllUsersExpected() {
        List<User> expectedUsers = Arrays.asList(
                new User(1, "Egor", "Fedorov"),
                new User(2, "Ivan", "Ivanov")
        );

        Mockito.when(userDAO.getAllUsers()).thenReturn(expectedUsers);
        List<User> actualUsers = usersService.getAllUsers();

        assertEquals(actualUsers, expectedUsers);
        Mockito.verify(userDAO).getAllUsers();
    }

    @Test
    public void getUserById_singleUserIsExpected() {
        User expectedUser = new User(4, "Bob", "Wayne");
        int userId = expectedUser.getId();

        Mockito.when(userDAO.getUserById(userId)).thenReturn(expectedUser);
        User actualUser = usersService.getUserById(userId);

        assertEquals(actualUser, expectedUser);
        Mockito.verify(userDAO).getUserById(userId);
    }

    @Test
    public void addUser_newUserExpectedToBeAdded() {
        User userToSave = new User( "Henry", "Dale");

        Mockito.when(userDAO.addUser(userToSave)).thenReturn(userToSave);
        usersService.addUser(userToSave);
        Mockito.verify(userDAO).addUser(userToSave);
    }

    @Test
    public void updateUser_userExpectedToBeUpdated() {
        boolean isUpdated;
        User userToUpdate = new User(1, "Egor", "Fedorov");
        int userId = userToUpdate.getId();

        Mockito.when(userDAO.updateUser(userId, userToUpdate)).thenReturn(true);

        isUpdated = usersService.updateUser(userId, userToUpdate);

        assertTrue(isUpdated);

        Mockito.verify(userDAO).updateUser(userId, userToUpdate);
    }

    @Test
    public void deleteUser_userExpectedToBeDeleted() {
        boolean isDeleted;
        User userToDelete = new User(5, "Tod", "Howard");
        int userId = userToDelete.getId();

        Mockito.when(userDAO.deleteUser(userId)).thenReturn(true);

        isDeleted = usersService.deleteUser(userId);

        assertTrue(isDeleted);

        Mockito.verify(userDAO).deleteUser(userId);
    }

    @Test
    public void addContactToPhoneBook_newContactExpectedToBeAddedToUsersPhoneBook() {
        User user = new User(1, "Egor", "Fedorov");
        Contact contact = new Contact(1, "Best Friend", "89539012992");
        int userId = user.getId();

        Mockito.when(userDAO.getUserById(userId)).thenReturn(user);
        Mockito.when(contactDAO.addContact(contact)).thenReturn(contact);
        usersService.addContactToPhoneBook(userId, contact);

        Mockito.verify(userDAO).getUserById(userId);
        Mockito.verify(contactDAO).addContact(contact);
    }

    @Test
    public void deleteContactFromPhoneBook_contactExpectedToBeDeletedFromPhoneBook() {
        boolean isDeleted;
        User user = new User(1, "Egor", "Fedorov");
        int userId = user.getId();
        Contact contact = new Contact(1, "Best Friend", "89539012992");
        int contactId = contact.getId();
        user.addContactToPhoneBook(contact);

        Mockito.when(userDAO.getUserById(userId)).thenReturn(user);
        Mockito.when(contactDAO.getAllContacts()).thenReturn(Collections.singletonList(contact));
        Mockito.when(contactDAO.deleteContact(contactId)).thenReturn(true);

        isDeleted = usersService.deleteContactFromPhoneBook(userId, contactId);

        assertTrue(isDeleted);

        Mockito.verify(userDAO).getUserById(userId);
        Mockito.verify(contactDAO).getAllContacts();
        Mockito.verify(contactDAO).deleteContact(contactId);
    }

    @Test
    public void getUserByName_userExpectedToBeFoundBySearchedName() {
        String searchedName = "Egor";
        User expectedReturnedUser = new User(1, "Egor", "Fedorov");

        Mockito.when(userDAO.getAllUsers()).thenReturn(Collections.singletonList(expectedReturnedUser));
        User actualReturnedUser = usersService.getUserByName(searchedName).get(0);

        assertEquals(actualReturnedUser, expectedReturnedUser);

        Mockito.verify(userDAO).getAllUsers();
    }

    @Test
    public void getUserByName_userNotExpectedToBeFound() {
        String searchedName = "Abba";
        User returnedUser = new User(1, "Egor", "Fedorov");

        Mockito.when(userDAO.getAllUsers()).thenReturn(Collections.singletonList(returnedUser));
        List<User> actualReturnedUsers = usersService.getUserByName(searchedName);

        assertNotEquals(actualReturnedUsers, null);

        Mockito.verify(userDAO).getAllUsers();
    }

    @Test
    public void getUsersContactsByPhoneNumber_usersContactsWithSearchedNumberExpected() {
        String searchedPhone = "89519012190";

        User user = new User(1, "Egor", "Fedorov");
        int userId = user.getId();
        Contact firstContact = new Contact(1, "Best Friend", "89519012190");
        Contact secondContact = new Contact(2,"Hate this guy", "89538001741");

        user.addContactToPhoneBook(firstContact);
        user.addContactToPhoneBook(secondContact);

        Mockito.when(userDAO.getUserById(userId)).thenReturn(user);
        List<Contact> expectedContacts = Collections.singletonList(firstContact);
        List<Contact> foundContacts = usersService.getUsersContactsByPhoneNumber(userId, searchedPhone);

        assertEquals(expectedContacts, foundContacts);

        Mockito.verify(userDAO).getUserById(userId);
    }
}
