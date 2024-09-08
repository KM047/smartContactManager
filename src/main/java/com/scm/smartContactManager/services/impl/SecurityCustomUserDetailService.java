package com.scm.smartContactManager.services.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.scm.smartContactManager.models.UserModel;
import com.scm.smartContactManager.repositories.UserRepo;
import com.scm.smartContactManager.security.UserPrincipal;

@Service
public class SecurityCustomUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepo repo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        UserModel availableUser = repo.findByEmail(email);

        if (availableUser == null) {

            throw new UsernameNotFoundException("User not found with email " + email);

        }

        return new UserPrincipal(availableUser);

    }

}
