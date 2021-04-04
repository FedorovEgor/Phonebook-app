package com.fedorov.rest;

import ch.qos.logback.core.encoder.EchoEncoder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fedorov.controllers.ContactsRestController;
import com.fedorov.model.Contact;
import com.fedorov.service.ContactsService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@WebMvcTest
@ContextConfiguration(classes = ContactsRestController.class)
public class ContactsRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ContactsService contactsService;

    @Test
    public void getAllContacts_listOfContactsIsExpected() throws Exception {
        List<Contact> expectedContacts = Arrays.asList(
                new Contact(1, "Best Friend", "89216782299"),
                new Contact(2, "Brother", "89321924477")
        );

        Mockito.when(contactsService.getAllContacts()).thenReturn(expectedContacts);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/contacts"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String actualJsonResponse = mvcResult.getResponse().getContentAsString();
        String expectedJsonResponse = objectMapper.writeValueAsString(expectedContacts);

        assertEquals(actualJsonResponse, expectedJsonResponse);
        Mockito.verify(contactsService).getAllContacts();
    }

    @Test
    public void getAllContacts_returnEmptyListNotFoundResponseExpected() throws Exception {
        List<Contact> expectedContacts = Collections.emptyList();

        Mockito.when(contactsService.getAllContacts()).thenReturn(expectedContacts);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/contacts"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        Mockito.verify(contactsService).getAllContacts();
    }

    @Test
    public void getAllContacts_returnNullNotFoundResponseIsExpected() throws Exception {
        Mockito.when(contactsService.getAllContacts()).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/contacts"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        Mockito.verify(contactsService).getAllContacts();
    }

    @Test
    public void getContactById_singleContactIsExpected() throws Exception {
        Contact expectedContact = new Contact(1, "Best Friend", "89216782299");
        int contactId = expectedContact.getId();

        Mockito.when(contactsService.getContactById(contactId)).thenReturn(expectedContact);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/contacts/{contactId}", contactId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String actualJsonResponse = mvcResult.getResponse().getContentAsString();
        String expectedJsonResponse = objectMapper.writeValueAsString(expectedContact);

        assertEquals(actualJsonResponse, expectedJsonResponse);

        Mockito.verify(contactsService).getContactById(contactId);
    }

    @Test
    public void getContactById_returnNullNotFoundResponseExpected() throws Exception {
        int contactId = 1;
        Mockito.when(contactsService.getContactById(contactId)).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/contacts/{contactId}", contactId))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        Mockito.verify(contactsService).getContactById(contactId);
    }

    @Test
    public void addContact_newContactIsExpectedToBeAddedAndCreatedResponse() throws Exception {
        Contact savedContact = new Contact(1, "Best Friend", "89216782299");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/contacts")
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(savedContact)))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void updateContact_contactExpectedToBeUpdated() throws Exception {
        Contact contactToUpdate = new Contact(1, "Best Friend", "89216782299");
        int contactId = contactToUpdate.getId();

        Mockito.when(contactsService.updateContact(contactId, contactToUpdate)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/contacts/{contactId}", contactId)
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(contactToUpdate)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(contactsService).updateContact(contactId, contactToUpdate);
    }

    @Test
    public void updateContact_notModifiedResponseExpected() throws Exception {
        Contact contactToUpdate = new Contact(1, "Best Friend", "89216782299");
        int contactId = 1;

        Mockito.when(contactsService.updateContact(contactId, contactToUpdate)).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/contacts/{contactId}", contactId)
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(contactToUpdate)))
                .andExpect(MockMvcResultMatchers.status().isNotModified());

        Mockito.verify(contactsService).updateContact(contactId, contactToUpdate);
    }

    @Test
    public void deleteContact_contactIsExpectedToBeDeleted() throws Exception {
        Contact contactToDelete = new Contact(1, "Best Friend", "89216782299");
        int contactId = contactToDelete.getId();

        Mockito.when(contactsService.deleteContact(contactId)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/contacts/{contactId}", contactId))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(contactsService).deleteContact(contactId);
    }

    @Test
    public void deleteContact_notModifiedResponseExpected() throws Exception {
        int contactId = 1;

        Mockito.when(contactsService.deleteContact(contactId)).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/contacts/{contactId}", contactId))
                .andExpect(MockMvcResultMatchers.status().isNotModified());

        Mockito.verify(contactsService).deleteContact(contactId);
    }

    @Test
    public void getContactsByPhoneNumber_listOfContactsIsExpected() throws Exception {
        Contact contact = new Contact(1, "Best Friend", "89216782299");
        List<Contact> foundContacts = Collections.singletonList(contact);

        String searchedPhoneNumber = "89216782299";

        Mockito.when(contactsService.getContactByPhoneNumber(searchedPhoneNumber)).thenReturn(foundContacts);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/contacts/q={phoneNumber}", searchedPhoneNumber))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String actualJsonResponse = mvcResult.getResponse().getContentAsString();
        String expectedJsonResponse = objectMapper.writeValueAsString(foundContacts);

        assertEquals(actualJsonResponse, expectedJsonResponse);

        Mockito.verify(contactsService).getContactByPhoneNumber(searchedPhoneNumber);
    }

    @Test
    public void getContactsByPhoneNumber_notAValidPhoneNumberPassedBadRequestIsExpected() throws Exception {
        String searchedPhoneNumber = "123123010299";

        Mockito.when(contactsService.getContactByPhoneNumber(searchedPhoneNumber)).thenCallRealMethod();

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/contacts/q={phoneNumber}", searchedPhoneNumber))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void getContactsByPhoneNumber_emptyContactListReturnedNotFoundResponseIsExpected() throws Exception {
        List<Contact> foundContacts = Collections.emptyList();
        String searchedPhoneNumber = "89131299102";

        Mockito.when(contactsService.getContactByPhoneNumber(searchedPhoneNumber)).thenReturn(foundContacts);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/contacts/q={phoneNumber}", searchedPhoneNumber))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        Mockito.verify(contactsService).getContactByPhoneNumber(searchedPhoneNumber);
    }

    @Test
    public void getContactsByPhoneNumber_nullReturnedNotFoundResponseIsExpected() throws Exception {
        String searchedPhoneNumber = "89131299102";

        Mockito.when(contactsService.getContactByPhoneNumber(searchedPhoneNumber)).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/contacts/q={phoneNumber}", searchedPhoneNumber))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        Mockito.verify(contactsService).getContactByPhoneNumber(searchedPhoneNumber);
    }
}
