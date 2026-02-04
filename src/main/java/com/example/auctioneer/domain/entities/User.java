package com.example.auctioneer.domain.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
@Table(
        name = "USERS",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "email"),
                @UniqueConstraint(columnNames = "pin")
        }
)
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 50)
    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Size(max = 50)
    @Column(name = "middle_name", length = 50)
    private String middleName;

    @NotBlank
    @Size(max = 50)
    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @NotBlank
    @Size(max = 20)
    @Column(name = "pin", nullable = false, length = 20, unique = true)
    private String pin; // ЕГН

    @Size(max = 50)
    @Column(name = "country", length = 50)
    private String country;

    @Size(max = 50)
    @Column(name = "city", length = 50)
    private String city;

    @Size(max = 100)
    @Column(name = "street", length = 100)
    private String street;

    @Size(max = 20)
    @Column(name = "street_number", length = 20)
    private String streetNumber;

    @Size(max = 20)
    @Column(name = "postal_code", length = 20)
    private String postalCode;

    @Size(max = 30)
    @Column(name = "phone_number", length = 30)
    private String phoneNumber;

    @NotBlank
    @Email
    @Size(max = 100)
    @Column(name = "email", nullable = false, length = 100, unique = true)
    private String email;

    public User() {
    }

}
