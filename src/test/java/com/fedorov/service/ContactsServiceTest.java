package com.fedorov.service;

import com.fedorov.dao.ContactDAOImpl;
import com.fedorov.model.Contact;
import com.fedorov.model.User;
import net.bytebuddy.build.ToStringPlugin;
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
public class ContactsServiceTest {

    @Mock
    private ContactDAOImpl contactDAO;
    @InjectMocks
    private ContactsService contactsService;

    @Test
    public void getAllContacts_listOfAllContactsExpected() {
        List<Contact> expectedContacts = Arrays.asList(
                new Contact(1, "Best Friend", "89216782299"),
                new Contact(2, "Brother", "89321924477")
        );

        Mockito.when(contactDAO.getAllContacts()).thenReturn(expectedContacts);
        List<Contact> actualContacts = contactsService.getAllContacts();

        assertEquals(actualContacts, expectedContacts);
        Mockito.verify(contactDAO).getAllContacts();
    }

    @Test
    public void getContactById_singleContactIsExpected() {
        Contact expectedContact = new Contact(1, "Best Friend", "89216782299");
        int contactId = expectedContact.getId();

        Mockito.when(contactDAO.getContactById(contactId)).thenReturn(expectedContact);
        Contact actualContact = contactsService.getContactById(contactId);

        assertEquals(actualContact, expectedContact);
        Mockito.verify(contactDAO).getContactById(contactId);
    }

    @Test
    public void addContact_newContactIsExpectedToBeAdded() {
        Contact contactToSave = new Contact(1, "Bet Friend", "89221425677");

        Mockito.when(contactDAO.addContact(contactToSave)).thenReturn(contactToSave);
        contactsService.addContact(contactToSave);
        Mockito.verify(contactDAO).addContact(contactToSave);
    }

    @Test
    public void updateContact_contactExpectedToBeUpdated() {
        boolean isUpdated;
        Contact contactToUpdate = new Contact(3, "Brother", "89216782299");
        int contactId = contactToUpdate.getId();

        Mockito.when(contactDAO.updateContact(contactId,contactToUpdate)).thenReturn(true);
        isUpdated = contactsService.updateContact(contactId, contactToUpdate);

        assertTrue(isUpdated);

        Mockito.verify(contactDAO).updateContact(contactId, contactToUpdate);
    }

    @Test
    public void deleteContact_contactExpectedToBeDeleted() {
        boolean isDeleted;
        Contact contactToDelete = new Contact(1, "Friend", "89539127788");
        int contactId = contactToDelete.getId();

        Mockito.when(contactDAO.deleteContact(contactId)).thenReturn(true);
        isDeleted = contactsService.deleteContact(contactId);

        assertTrue(isDeleted);

        Mockito.verify(contactDAO).deleteContact(contactId);
    }

    @Test
    public void getContactByPhoneNumber_contactsWithSearchedNumberExpected() {
        String searchedPhone = "89519012190";

        Contact firstContact = new Contact(1, "Best Friend", "89519012190");
        Contact secondContact = new Contact(2,"Hate this guy", "89538001741");
        List<Contact> storedContacts = Arrays.asList(
                firstContact, secondContact
        );

        Mockito.when(contactDAO.getAllContacts()).thenReturn(storedContacts);
        List<Contact> expectedContacts = Collections.singletonList(firstContact);
        List<Contact> foundContacts = contactsService.getContactByPhoneNumber(searchedPhone);

        assertEquals(expectedContacts, foundContacts);

        Mockito.verify(contactDAO).getAllContacts();
    }
}
