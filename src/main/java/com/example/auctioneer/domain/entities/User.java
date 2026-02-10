package com.example.auctioneer.domain.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(
        name = "USERS"
)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Column(name = "middle_name", length = 50)
    private String middleName;

    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @Column(name = "ucn", nullable = false, length = 20, unique = true)
    private String ucn; // ЕГН

    @Column(name = "country", length = 50)
    private String country;

    @Column(name = "city", length = 50)
    private String city;

    @Column(name = "street", length = 100)
    private String street;

    @Column(name = "street_number", length = 20)
    private String streetNumber;

    @Column(name = "postal_code", length = 20)
    private String postalCode;

    @Column(name = "phone_number", length = 30)
    private String phoneNumber;

    @Column(name = "role", nullable = false, length = 100)
    private String role;

    @Email
    @Column(name = "email", nullable = false, length = 100, unique = true)
    private String email;

    @Column(name = "username", nullable = false, length = 50, unique = true)
    private String username;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;
}