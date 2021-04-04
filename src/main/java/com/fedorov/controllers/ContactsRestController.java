package com.fedorov.controllers;

import com.fedorov.model.Contact;
import com.fedorov.service.ContactsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/v1/contacts")
public class ContactsRestController {

    private ContactsService contactsService;

    @Autowired
    public ContactsRestController(ContactsService contactsService) {
        this.contactsService = contactsService;
    }

    @GetMapping()
    public ResponseEntity<List<Contact>> getAllContacts() {
        List<Contact> contacts = contactsService.getAllContacts();

        return contacts != null && !contacts.isEmpty()
                ? new ResponseEntity<>(contacts, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping()
    public ResponseEntity<?> addContact(@RequestBody Contact contact) {
        contactsService.addContact(contact);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<Contact> getContactById(@PathVariable int id) {
        Contact contact = contactsService.getContactById(id);

        return contact != null
                ? new ResponseEntity<>(contact, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("{id}")
    public ResponseEntity<?> updateContact(@PathVariable int id, @RequestBody Contact contact) {
        boolean isUpdated = contactsService.updateContact(id, contact);

        return isUpdated
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteContact(@PathVariable int id) {
        boolean isDeleted = contactsService.deleteContact(id);

        return isDeleted
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @GetMapping("q={phoneNumber}")
    public ResponseEntity<List<Contact>> getContactsByPhoneNumber(@PathVariable String phoneNumber) {
        Pattern pattern = Pattern.compile("^(7|8)\\d{10}$");
        Matcher matcher = pattern.matcher(phoneNumber);
        if (!matcher.find()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        List<Contact> contacts = contactsService.getContactByPhoneNumber(phoneNumber);

        return contacts != null && !contacts.isEmpty()
                ? new ResponseEntity<>(contacts, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
