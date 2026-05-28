package com.auctioneer.domain.entities;

import com.auctioneer.converters.StringListConverter;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

    @Column(name = "roles")
    @Convert(converter = StringListConverter.class)
    private List<String> roles;

    @Email
    @Column(name = "email", nullable = false, length = 100, unique = true)
    private String email;

    @Column(name = "username", nullable = false, length = 50, unique = true)
    private String username;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @Column(name = "stripe_customer_id", unique = true)
    private String stripeCustomerId;

    @Builder.Default
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal balance = BigDecimal.ZERO;

    @Builder.Default
    @Column(name = "credits", nullable = false, precision = 10, scale = 2)
    private BigDecimal credits = BigDecimal.ZERO;
}
