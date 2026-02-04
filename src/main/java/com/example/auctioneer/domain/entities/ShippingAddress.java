package com.example.auctioneer.domain.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(
        name = "SHIPPING_ADDRESSES",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames =  "phone_number"
                )
        }
)
@Setter
@Getter
public class ShippingAddress {
    // Getters and Setters
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Column(name = "middle_name", length = 50)
    private String middleName;

    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @Column(name = "phone_number", nullable = false, length = 20, unique = true)
    private String phoneNumber;

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

    // Required no-args constructor for JPA
    public ShippingAddress() {
    }

}


/**
 * First Name
 * Middle name
 * Last Name
 * Phone number
 * Country
 * City
 * Street
 * Street number
 * Postal code
 */