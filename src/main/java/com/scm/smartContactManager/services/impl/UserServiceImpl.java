package com.scm.smartContactManager.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scm.smartContactManager.helper.ResourceNotFoundException;
import com.scm.smartContactManager.models.UserModel;
import com.scm.smartContactManager.repositories.UserRepo;
import com.scm.smartContactManager.services.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public UserModel saveUser(UserModel user) {

        // User id generated

        String userId = UUID.randomUUID().toString();
        user.setUserId(userId);

        // TODO: Encode the password while saving

        return userRepo.save(user);
    }

    @Override
    public Optional<UserModel> getUserById(String userId) {
        return userRepo.findById(userId);
    }

    @Override
    public Optional<UserModel> updateUser(UserModel user) {
        UserModel availableUser = userRepo.findById(user.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // update user with available user

        availableUser.setName(user.getName());
        availableUser.setEmail(user.getEmail());
        availableUser.setPassword(user.getPassword());
        availableUser.setAbout(user.getAbout());
        availableUser.setAddress(user.getAddress());
        availableUser.setEmailVerified(user.isEmailVerified());
        availableUser.setPhoneVerified(user.isPhoneVerified());

        UserModel updatedUser = userRepo.save(availableUser);

        return Optional.ofNullable(updatedUser);

    }

    @Override
    public void deleteUser(String userId) {
        try {
            userRepo.deleteById(userId);
        } catch (Exception e) {
            System.out.println("User not found or deleted already");
        }
    }

    @Override
    public boolean isUserExist(String userId) {
        UserModel user = userRepo.findById(userId).orElse(null);

        return user != null ? true : false;
    }

    @Override
    public boolean isUserExistByEmail(String email) {

        UserModel availableUser = userRepo.findByEmail(email).orElse(null);
        return availableUser != null ? true : false;

    }

    @Override
    public List<UserModel> getAllUsers() {
        return userRepo.findAll();
    }

}
