package com.scm.smartContactManager.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.scm.smartContactManager.models.Contact;
import com.scm.smartContactManager.models.UserModel;

@Repository
public interface ContactRepo extends JpaRepository<Contact, String> {

    Page<Contact> findByUser(UserModel user, Pageable pageable);

    @Query("SELECT c FROM Contact c WHERE c.user.id = :userId")
    List<Contact> findByUserId(@Param("userId") String userId);

    Page<Contact> findByUserAndNameContaining(UserModel user, String namekeyword, Pageable pageable);

    Page<Contact> findByUserAndEmailContaining(UserModel user, String emailkeyword, Pageable pageable);

    Page<Contact> findByUserAndPhoneNumberContaining(UserModel user, String phonekeyword, Pageable pageable);
}
