package com.fedorov.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fedorov.controllers.UsersRestController;
import com.fedorov.model.Contact;
import com.fedorov.model.User;
import com.fedorov.service.UsersService;
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
@ContextConfiguration(classes = UsersRestController.class)
public class UsersRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UsersService usersService;

    @Test
    public void getAllUsers_allUsersExpected() throws Exception {
        List<User> expectedUsers = Arrays.asList(
                new User(1, "Andrew", "Flock"),
                new User(2, "Maxim", "Ivanov")
        );

        Mockito.when(usersService.getAllUsers()).thenReturn(expectedUsers);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String actualJsonResponse = mvcResult.getResponse().getContentAsString();
        String expectedJsonResponse = objectMapper.writeValueAsString(expectedUsers);

        assertEquals(actualJsonResponse, expectedJsonResponse);
        Mockito.verify(usersService).getAllUsers();
    }

    @Test
    public void getAllUsers_returnEmptyListNotFoundResponseExpected() throws Exception {
        List<User> expectedUsers = Collections.emptyList();

        Mockito.when(usersService.getAllUsers()).thenReturn(expectedUsers);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        Mockito.verify(usersService).getAllUsers();
    }

    @Test
    public void getAllUsers_returnNullNotFoundResponseExpected() throws Exception {
        Mockito.when(usersService.getAllUsers()).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        Mockito.verify(usersService).getAllUsers();
    }

    @Test
    public void getUserById_singleUserIsExpected() throws Exception {
        User expectedUser = new User(1, "Tod", "Howard");
        int userId = expectedUser.getId();

        Mockito.when(usersService.getUserById(userId)).thenReturn(expectedUser);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/{userId}", userId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String actualJsonResponse = mvcResult.getResponse().getContentAsString();
        String expectedJsonResponse = objectMapper.writeValueAsString(expectedUser);

        assertEquals(actualJsonResponse, expectedJsonResponse);
        Mockito.verify(usersService).getUserById(userId);
    }

    @Test
    public void getUserById_returnNullNotFoundResponseExpected() throws Exception {
        int userId = 1;
        Mockito.when(usersService.getUserById(1)).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/{userId}", userId))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        Mockito.verify(usersService).getUserById(userId);
    }

    @Test
    public void addUser_newUserIsExpectedToBeAddedAndCreatedResponse() throws Exception {
        User savedUser = new User(1, "Egor", "Fedorov");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users")
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(savedUser)))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void updateUser_userIsExpectedToBeUpdated() throws Exception {
        User userToUpdate = new User(1, "Mack", "Wade");
        int userId = userToUpdate.getId();

        Mockito.when(usersService.updateUser(userId, userToUpdate)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/users/{useId}", userId)
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(userToUpdate)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(usersService).updateUser(userId, userToUpdate);
    }

    @Test
    public void updateUser_notModifiedResponseExcepted() throws Exception {
        User userToUpdate = new User(1, "Mack", "Wade");
        int userId = userToUpdate.getId();

        Mockito.when(usersService.updateUser(userId, userToUpdate)).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/users/{userId}", userId)
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(userToUpdate)))
                .andExpect(MockMvcResultMatchers.status().isNotModified());

        Mockito.verify(usersService).updateUser(userId, userToUpdate);
    }

    @Test
    public void deleteUser_userExpectedToBeDeleted () throws Exception {
        User userToDelete = new User(1, "George", "Wilson");
        int userId = userToDelete.getId();

        Mockito.when(usersService.deleteUser(userId)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/users/{userId}", userId))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(usersService).deleteUser(userId);
    }

    @Test
    public void deleteUser_notModifiedResponseExpected() throws Exception {
        int userId = 1;
        Mockito.when(usersService.deleteUser(userId)).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/users/{userId}", userId))
                .andExpect(MockMvcResultMatchers.status().isNotModified());

        Mockito.verify(usersService).deleteUser(userId);
    }

    @Test
    public void addContactToPhoneBook_newContactInUsersPhoneBookIsExpected() throws Exception {
        User user = new User(1, "Egor", "Fedorov");
        int userId = user.getId();
        Contact contact = new Contact("Best Friend", "89519012190");

        Mockito.when(usersService.getUserById(userId)).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users/{userId}/contacts", userId)
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(contact)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(usersService).getUserById(userId);
    }

    @Test
    public void addContactToPhoneBook_nullUserReturnedBadRequestResponseIsExpected() throws Exception {
        int userId = 1;
        Contact contact = new Contact("Best Friend", "89519012190");

        Mockito.when(usersService.getUserById(userId)).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users/{userId}/contacts", userId)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(contact)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        Mockito.verify(usersService).getUserById(userId);
    }

    @Test
    public void showUsersContacts_listOfUsersContactsIsExpected() throws Exception {
        User user = new User(1, "Egor", "Fedorov");
        int userId = user.getId();
        List<Contact> expectedContacts = Arrays.asList(
                new Contact("Best Friend", "89519012190"),
                new Contact("Hate this guy", "89538001741")
        );

        Mockito.when(usersService.getUserById(userId)).thenReturn(user);
        user.setPhoneBook(expectedContacts);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/{userId}/contacts", userId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String actualJsonResponse = mvcResult.getResponse().getContentAsString();
        String expectedJsonResponse = objectMapper.writeValueAsString(expectedContacts);

        assertEquals(actualJsonResponse, expectedJsonResponse);
        Mockito.verify(usersService).getUserById(userId);
    }

    @Test
    public void showUsersContacts_nullUserReturnedAndBadRequestResponseIsExpected() throws Exception {
        int userId = 1;

        Mockito.when(usersService.getUserById(userId)).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/{userId}/contacts", userId))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        Mockito.verify(usersService).getUserById(userId);
    }

    @Test
    public void showUsersContacts_emptyContactsListReturnedAndNotFoundResponseIsExpected() throws Exception {
        User user = new User(1, "Egor", "Fedorov");
        int userId = user.getId();

        Mockito.when(usersService.getUserById(userId)).thenReturn(user);
        user.setPhoneBook(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/{userId}/contacts", userId))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        Mockito.verify(usersService).getUserById(userId);
    }

    @Test
    public void showUsersContacts_nullContactsListReturnedAndNotFoundResponseIsExpected() throws Exception {
        User user = new User(1, "Egor", "Fedorov");
        int userId = user.getId();

        Mockito.when(usersService.getUserById(userId)).thenReturn(user);
        user.setPhoneBook(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/{userId}/contacts", userId))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        Mockito.verify(usersService).getUserById(userId);
    }

    @Test
    public void deleteContactFromPhoneBook_contactIsExpectedToBeDeleted() throws Exception {
        User user = new User(1, "Egor", "Fedorov");
        int userId = user.getId();
        Contact contact = new Contact(1, "Best Friend", "89519012190");
        int contactId = contact.getId();

        Mockito.when(usersService.deleteContactFromPhoneBook(userId, contactId)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/users/{userId}/contacts/{contactId}", userId, contactId))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(usersService).deleteContactFromPhoneBook(userId, contactId);
    }

    @Test
    public void deleteContactFromPhoneBook_notModifiedResponseExpected() throws Exception {
        int userId = 1;
        int contactId = 1;

        Mockito.when(usersService.deleteContactFromPhoneBook(userId, contactId)).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/users/{userId}/contacts/{contactId}", userId, contactId))
                .andExpect(MockMvcResultMatchers.status().isNotModified());

        Mockito.verify(usersService).deleteContactFromPhoneBook(userId, contactId);
    }

    @Test
    public void getUserByName_userIsExpected() throws Exception {
        User user = new User(1, "John", "Kruger");
        List<User> usersFound = Collections.singletonList(user);
        String searchedString = "John";

        Mockito.when(usersService.getUserByName(searchedString)).thenReturn(usersFound);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/q={searchedString}", searchedString))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String actualJsonResponse = mvcResult.getResponse().getContentAsString();
        String expectedJsonResponse = objectMapper.writeValueAsString(usersFound);

        assertEquals(actualJsonResponse, expectedJsonResponse);

        Mockito.verify(usersService).getUserByName(searchedString);
    }

    @Test
    public void getUserByName_notFoundResponseIsExpected() throws Exception {
        String searchedString = "Abra";

        Mockito.when(usersService.getUserByName(searchedString)).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/q={searchedString}", searchedString))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        Mockito.verify(usersService).getUserByName(searchedString);
    }

    @Test
    public void getUsersContactsByPhoneNumber_listOfUserContactsIsExpected() throws Exception {
        User user = new User(1, "Egor", "Fedorov");
        int userId = user.getId();
        Contact contact = new Contact(1, "Nick", "89529471579");
        List<Contact> foundContacts = Collections.singletonList(contact);

        String searchedPhoneNumber = "89529471579";

        Mockito.when(usersService.getUsersContactsByPhoneNumber(1, searchedPhoneNumber)).thenReturn(foundContacts);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                    .get("/api/v1/users/{userId}/contacts/q={searchedPhone}", userId, searchedPhoneNumber))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String actualJsonResponse = mvcResult.getResponse().getContentAsString();
        String expectedJsonResponse = objectMapper.writeValueAsString(foundContacts);

        assertEquals(actualJsonResponse, expectedJsonResponse);

        Mockito.verify(usersService).getUsersContactsByPhoneNumber(userId, searchedPhoneNumber);
    }

    @Test
    public void getUsersContactsByPhoneNumber_notAValidPhoneNumberPassedBadRequestResponseIsExpected() throws Exception {
        int userId = 1;
        String searchedPhoneNumber = "asdasdad";

        Mockito.when(usersService.getUsersContactsByPhoneNumber(userId, searchedPhoneNumber)).thenCallRealMethod();

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/{userId}/contacts/q={searchedPhone}", userId, searchedPhoneNumber))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void getUsersContactsByPhoneNumber_emptyContactListReturnedNotFoundResponseIsExpected() throws Exception {
        int userId = 1;
        List<Contact> foundContacts = Collections.emptyList();
        String searchedPhoneNumber = "89524872281";

        Mockito.when(usersService.getUsersContactsByPhoneNumber(userId, searchedPhoneNumber)).thenReturn(foundContacts);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/{userId}/contacts/q={searchedPhone}", userId, searchedPhoneNumber))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        Mockito.verify(usersService).getUsersContactsByPhoneNumber(userId, searchedPhoneNumber);
    }

    @Test
    public void getUsersContactsByPhoneNumber_nullReturnedNotFoundResponseIsExpected() throws Exception {
        int userId = 1;
        String searchedPhoneNumber = "89524872281";

        Mockito.when(usersService.getUsersContactsByPhoneNumber(userId, searchedPhoneNumber)).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/{userId}/contacts/q={searchedPhone}", userId, searchedPhoneNumber))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        Mockito.verify(usersService).getUsersContactsByPhoneNumber(userId, searchedPhoneNumber);
    }
}
