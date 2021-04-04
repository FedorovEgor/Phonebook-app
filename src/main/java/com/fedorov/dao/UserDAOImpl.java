package com.fedorov.dao;

import com.fedorov.model.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserDAOImpl implements UserDAO {
    private static int USERS_COUNT;
    private List<User> users = new ArrayList<>();

    public List<User> getAllUsers() {
        return users;
    }

    public User getUserById(int id) {
       User userToRetrieve = users.stream().filter(user -> user.getId() == id).findAny().orElse(null);
       return userToRetrieve;
    }

    public User addUser(User user) {
        User userToSave = new User(++USERS_COUNT, user.getFirstName(), user.getLastName());
        users.add(userToSave);
        return userToSave;
    }

    public boolean updateUser(int id, User user) {
        boolean isUpdated = false;

        User userToUpdate = users.stream().filter(storedUser -> storedUser.getId() == id).findAny().orElse(null);

        if (userToUpdate != null) {
            userToUpdate.setFirstName(user.getFirstName());
            userToUpdate.setLastName(user.getLastName());
            isUpdated = true;
        }
        return isUpdated;
    }

    public boolean deleteUser(int id) {
        boolean isDeleted = false;

        User userToDelete = users.stream().filter(storedUser -> storedUser.getId() == id).findAny().orElse(null);
        if (userToDelete != null) {
            users.remove(userToDelete);
            isDeleted = true;
        }
        return isDeleted;
    }
}
