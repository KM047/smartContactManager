package com.scm.smartContactManager.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.scm.smartContactManager.constants.AppConstants;
import com.scm.smartContactManager.helper.ResourceNotFoundException;
import com.scm.smartContactManager.helper.UserHelper;
import com.scm.smartContactManager.models.UserModel;
import com.scm.smartContactManager.repositories.UserRepo;
import com.scm.smartContactManager.services.EmailService;
import com.scm.smartContactManager.services.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private EmailService emailService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    @Override
    public UserModel saveUser(UserModel user) {

        // User id generated

        String userId = UUID.randomUUID().toString();
        user.setUserId(userId);

        user.setPassword(encoder.encode(user.getPassword()));

        user.setRoleList(List.of(AppConstants.ROLE_USER));

        String emailToken = UUID.randomUUID().toString();

        String emailBody = UserHelper.getLinkForEmailVerification(emailToken, user);

        user.setEmailVerifyToken(emailToken);

        emailService.sendEmail(user.getEmail(), "Verify you email.", emailBody);

        return userRepo.save(user);
    }

    @Override
    public Optional<UserModel> getUserById(String userId) {
        return userRepo.findById(userId);
    }

    @Override
    public Optional<UserModel> updateUser(UserModel user) {

        UserModel availableUser = null;

        if (user.getUserId() != null) {

            availableUser = userRepo.findById(user.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        } else {

            availableUser = userRepo.findByEmail(user.getEmail());

        }

        if (availableUser == null) {
            throw new ResourceNotFoundException("User not found");
        }

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
            e.printStackTrace();
        }
    }

    @Override
    public boolean isUserExist(String userId) {
        UserModel user = userRepo.findById(userId).orElse(null);

        return user != null ? true : false;
    }

    @Override
    public boolean isUserExistByEmail(String email) {

        UserModel availableUser = userRepo.findByEmail(email);
        return availableUser != null ? true : false;

    }

    @Override
    public List<UserModel> getAllUsers() {
        return userRepo.findAll();
    }

    @Override
    public UserModel getUserByEmail(String email) {

        UserModel userByEmail = userRepo.findByEmail(email);

        if (userByEmail == null) {
            return null;
        }

        return userByEmail;
    }

    @Override
    public UserModel verifyEmailByToken(String emailToken) {
        return userRepo.findByEmailVerifyToken(emailToken);
    }

}
