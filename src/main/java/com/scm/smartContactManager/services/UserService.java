package com.scm.smartContactManager.services;

import java.util.List;
import java.util.Optional;

import com.scm.smartContactManager.models.UserModel;

public interface UserService {

    UserModel saveUser(UserModel user);

    Optional<UserModel> getUserById(String userId);

    Optional<UserModel> updateUser(UserModel user);

    void deleteUser(String userId);

    boolean isUserExist(String userId);

    boolean isUserExistByEmail(String email);

    List<UserModel> getAllUsers();

    UserModel getUserByEmail(String email);

}
