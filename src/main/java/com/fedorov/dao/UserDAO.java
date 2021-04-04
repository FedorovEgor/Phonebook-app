package com.fedorov.dao;

import com.fedorov.model.User;

import java.util.List;

public interface UserDAO {

    User addUser(User user);

    User getUserById(int id);

    List<User> getAllUsers();

    boolean updateUser(int id, User user);

    boolean deleteUser(int id);
}
