package com.fedorov.dao;

import com.fedorov.model.Contact;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ContactDAOImplTest {

    private ContactDAO contactDAO;

    @BeforeAll
    public void setUpStorage() {
        contactDAO = new ContactDAOImpl();
        contactDAO.addContact(new Contact("Best Friend", "89214857784"));
        contactDAO.addContact(new Contact("Brother", "89944716633"));
    }

    @Test
    public void getAllContacts_listOfAllContactsExpected() {
        List<Contact> expectedContacts = Arrays.asList(
                new Contact(1,"Best Friend", "89214857784")
        );

        List<Contact> actualContacts = contactDAO.getAllContacts();

        assertEquals(actualContacts, expectedContacts);
    }

    @Test
    public void getContactById_singleContactIsExpected() {
        Contact expectedContact = new Contact(1,"Best Friend", "89214857784");
        int contactId = expectedContact.getId();

        Contact actualContact = contactDAO.getContactById(contactId);

        assertEquals(actualContact, expectedContact);
    }

    @Test
    public void deleteContact_contactExpectedToBeDeleted() {
        boolean isDeleted;

        int contactId = 2;

        isDeleted = contactDAO.deleteContact(contactId);

        assertTrue(isDeleted);
    }

    @Test
    public void updateContact_contactExpectedToBeUpdated() {
        boolean isUpdated;
        Contact contactToUpdate = new Contact(2, "Brother", "89944716633");
        int contactId = contactToUpdate.getId();

        isUpdated = contactDAO.updateContact(contactId, contactToUpdate);

        assertTrue(isUpdated);
    }
}
