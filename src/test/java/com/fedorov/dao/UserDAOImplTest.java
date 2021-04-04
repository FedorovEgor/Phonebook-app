package com.fedorov.dao;

import com.fedorov.model.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserDAOImplTest {

    private UserDAO userDAO;

    @BeforeAll
    public void setUpStorage() {
        userDAO = new UserDAOImpl();
        userDAO.addUser(new User( "Egor", "Fedorov"));
        userDAO.addUser(new User( "George", "Lake"));
    }

    @Test
    public void getAllUsers_listOfAllUsersExpected() {
        List<User> expectedUsers = Arrays.asList(
                new User(1, "Egor", "Fedorov"),
                new User(2, "George", "Lake")
        );

        List<User> actualUsers = userDAO.getAllUsers();

        assertEquals(actualUsers, expectedUsers);
    }

    @Test
    public void getUserById_singleUserExpected() {
        User expectedUser = new User(2, "George", "Lake");
        int userId = expectedUser.getId();

        User actualUser = userDAO.getUserById(userId);

        assertEquals(actualUser, expectedUser);
    }

    @Test
    public void updateUser_userExpectedToBeUpdated() {
        boolean isUpdated;

        User userToUpdate = new User(2, "Jack", "Kruger");
        int userId = userToUpdate.getId();

        isUpdated = userDAO.updateUser(userId, userToUpdate);

        assertTrue(isUpdated);
    }

    @Test
    public void deleteUser_userExpectedToBeDeleted() {
        boolean isDeleted;

        int userId = 1;

        isDeleted = userDAO.deleteUser(userId);

        assertTrue(isDeleted);
    }
}
