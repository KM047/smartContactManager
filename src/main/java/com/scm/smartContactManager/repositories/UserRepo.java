package com.scm.smartContactManager.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.scm.smartContactManager.models.UserModel;

@Repository
public interface UserRepo extends JpaRepository<UserModel, String> {

    UserModel findByEmail(String email);

    UserModel findByEmailVerifyToken(String emailToken);

}
