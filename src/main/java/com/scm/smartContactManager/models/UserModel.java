package com.scm.smartContactManager.models;

import com.scm.smartContactManager.constants.AuthProvider;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

@Entity(name = "users")
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserModel {

    @Id
    private String userId;
    @Column(name = "user_name", nullable = false)
    private String name;
    @Column(unique = true, nullable = false)
    private String email;
    private String password;
    @Column(unique = true)
    private String phoneNumber;
    private String address;
    @Column(length = 1000)
    private String about;

    @Column(length = 1000)
    private String avatar;
    private String gender;
    private boolean active = false;
    private boolean emailVerified = false;
    private String emailVerifyToken;
    private boolean phoneVerified = false;

    // Add more fields as needed for user registration and login details
    // e.g. -> Google, GitHub, Facebook, etc...

    @Enumerated(value = EnumType.STRING)
    private AuthProvider provider = AuthProvider.SELF;
    private String providerId;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Contact> contacts = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roleList = new ArrayList<>();

}
