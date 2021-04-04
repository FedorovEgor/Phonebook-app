package com.fedorov.controllers;

import com.fedorov.model.Contact;
import com.fedorov.model.User;
import com.fedorov.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/v1/users")
public class UsersRestController {

    private UsersService usersService;

    @Autowired
    public UsersRestController(UsersService usersService) {
        this.usersService = usersService;
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = usersService.getAllUsers();

        return users != null && !users.isEmpty()
                ? new ResponseEntity<>(users, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("{id}")
    public ResponseEntity<User> getUserById(@PathVariable int id) {
        User user = usersService.getUserById(id);

        return user != null
                ? new ResponseEntity<>(user, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping()
    public ResponseEntity<?> addUser(@RequestBody User user) {
        usersService.addUser(user);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<?> updateUser(@PathVariable int id, @RequestBody User user) {
        boolean isUpdated = usersService.updateUser(id, user);

        return isUpdated
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteUser(@PathVariable int id) {
        boolean isDeleted = usersService.deleteUser(id);

        return isDeleted
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @PostMapping("{id}/contacts")
    public ResponseEntity<?> addContactToPhoneBook(@PathVariable int id, @RequestBody Contact contact) {
        if (usersService.getUserById(id) == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        usersService.addContactToPhoneBook(id, contact);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("{id}/contacts")
    public ResponseEntity<List<Contact>> showUsersContacts(@PathVariable int id) {
        User user = usersService.getUserById(id);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        List<Contact> contacts = user.getPhoneBook();

        return contacts != null && !contacts.isEmpty()
                ? new ResponseEntity<>(contacts, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("{userId}/contacts/{contactId}")
    public ResponseEntity<?> deleteContactFromPhoneBook(@PathVariable(name = "userId") int userId, @PathVariable(name = "contactId") int contactId) {
        boolean isDeleted = usersService.deleteContactFromPhoneBook(userId, contactId);

        return isDeleted
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @GetMapping("q={searchedString}")
    public ResponseEntity<List<User>> getUserByName(@PathVariable String searchedString) {
        List<User> users = usersService.getUserByName(searchedString);

        return users != null && !users.isEmpty()
                ? new ResponseEntity<>(users, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("{userId}/contacts/q={phoneNumber}")
    public ResponseEntity<List<Contact>> getUsersContactsByPhoneNumber(@PathVariable int userId, @PathVariable String phoneNumber) {
        Pattern pattern = Pattern.compile("^(7|8)\\d{10}$");
        Matcher matcher = pattern.matcher(phoneNumber);
        if (!matcher.find()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        List<Contact> contacts = usersService.getUsersContactsByPhoneNumber(userId, phoneNumber);

        return contacts != null && !contacts.isEmpty()
                ? new ResponseEntity<>(contacts, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
