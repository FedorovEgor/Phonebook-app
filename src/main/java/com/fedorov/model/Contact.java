package com.fedorov.model;

import java.util.Objects;

public class Contact {

    private int id;
    private String contactName;
    private String phoneNumber;

    public Contact() {
    }

    public Contact(String contactName, String phoneNumber) {
        this.contactName = contactName;
        this.phoneNumber = phoneNumber;
    }

    public Contact(int id, String contactName, String phoneNumber) {
        this.id = id;
        this.contactName = contactName;
        this.phoneNumber = phoneNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contact contact = (Contact) o;
        return id == contact.id &&
                Objects.equals(contactName, contact.contactName) &&
                Objects.equals(phoneNumber, contact.phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, contactName, phoneNumber);
    }

    @Override
    public String toString() {
        return "Contact{" +
                "id=" + id +
                ", contactName='" + contactName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
