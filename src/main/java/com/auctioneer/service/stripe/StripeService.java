package com.auctioneer.service.stripe;

import com.auctioneer.domain.entities.User;
import com.auctioneer.dtos.stripe.PaymentMethodResponseDto;
import com.auctioneer.repository.user.UserRepository;
import com.auctioneer.service.discordNotifications.DiscordService;
import com.stripe.StripeClient;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.PaymentIntent;
import com.stripe.model.PaymentMethod;
import com.stripe.model.SetupIntent;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.PaymentMethodListParams;
import com.stripe.param.SetupIntentCreateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class StripeService {
    @Value("${stripe.secret}")
    private String stripeSecret;

    @Value("${stripe.publishable:}")
    private String stripePublishable;

    @Value("${stripe.default.country:US}")
    private String defaultCountryCode;

    private final UserRepository userRepository;
    private final DiscordService discordService;

    public String getPublishableKey() {
        return stripePublishable;
    }

    public String createSetupIntent(Long userId) throws StripeException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found: " + userId));

        StripeClient client = new StripeClient(stripeSecret);

        String customerId = user.getStripeCustomerId();
        if (customerId == null || customerId.isBlank()) {
            CustomerCreateParams customerParams = CustomerCreateParams.builder()
                    .setEmail(user.getEmail())
                    .setName(user.getFirstName() + " " + user.getLastName())
                    .build();

            Customer customer = client.v1().customers().create(customerParams);

            customerId = customer.getId();

            user.setStripeCustomerId(customerId);

            userRepository.save(user);
        }

        SetupIntentCreateParams params = SetupIntentCreateParams.builder()
                .setCustomer(customerId)
                .addPaymentMethodType("card")
                .build();

        SetupIntent intent = client.v1().setupIntents().create(params);

        discordService.sendStripeNotification("🔧 Setup intent created for user " + userId);
        return intent.getClientSecret();
    }

    public String createPaymentIntent(Long userId, BigDecimal amount) throws StripeException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found: " + userId));

        StripeClient client = new StripeClient(stripeSecret);

        String customerId = user.getStripeCustomerId();
        if (customerId == null || customerId.isBlank()) {
            CustomerCreateParams customerParams = CustomerCreateParams.builder()
                    .setEmail(user.getEmail())
                    .setName(user.getFirstName() + " " + user.getLastName())
                    .build();

            Customer customer = client.v1().customers().create(customerParams);

            customerId = customer.getId();

            user.setStripeCustomerId(customerId);

            userRepository.save(user);
        }

        long amountInCents = amount.multiply(BigDecimal.valueOf(100)).longValue();

        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(amountInCents)
                .setCurrency("eur")
                .setCustomer(customerId)
                .addPaymentMethodType("card")
                .build();

        PaymentIntent intent = client.v1().paymentIntents().create(params);

        discordService.sendStripeNotification("💳 Payment intent created for user " + userId + ": **" + amount + "**");
        return intent.getClientSecret();
    }

    public List<PaymentMethodResponseDto> listSavedCards(Long userId) throws StripeException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found: " + userId));

        String customerId = user.getStripeCustomerId();
        if (customerId == null || customerId.isBlank()) {
            return List.of();
        }

        StripeClient client = new StripeClient(stripeSecret);
        PaymentMethodListParams params = PaymentMethodListParams.builder()
                .setCustomer(customerId)
                .setType(PaymentMethodListParams.Type.CARD)
                .build();

        return client.v1().paymentMethods().list(params).getData().stream()
                .map(PaymentMethodResponseDto::from)
                .toList();
    }

    public PaymentMethod createCustomerAccount(Long userId) throws StripeException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found: " + userId));

        String email       = user.getEmail();
        String displayName = user.getFirstName() + " " + user.getLastName();
        String countryCode = resolveCountryCode(user.getCountry());
        String currencyCode = "eur";

        StripePaymentMethod paymentMethod = new StripePaymentMethod(
                email,
                displayName,
                countryCode,
                currencyCode,
                stripeSecret
        );

        return paymentMethod.create();
    }

    private String resolveCountryCode(String country) {
        if (country == null || country.isBlank()) {
            return defaultCountryCode;
        }

        if (country.trim().length() == 2) {
            return country.trim().toUpperCase();
        }

        String normalized = country.trim().toLowerCase();
        String code = COUNTRY_NAME_TO_CODE.get(normalized);
        return code != null ? code : defaultCountryCode;
    }

    private static final Map<String, String> COUNTRY_NAME_TO_CODE = Map.ofEntries(
            Map.entry("united states", "US"), Map.entry("usa", "US"),
            Map.entry("united kingdom", "GB"), Map.entry("uk", "GB"),
            Map.entry("germany", "DE"), Map.entry("france", "FR"),
            Map.entry("spain", "ES"), Map.entry("italy", "IT"),
            Map.entry("netherlands", "NL"), Map.entry("belgium", "BE"),
            Map.entry("austria", "AT"), Map.entry("switzerland", "CH"),
            Map.entry("sweden", "SE"), Map.entry("norway", "NO"),
            Map.entry("denmark", "DK"), Map.entry("finland", "FI"),
            Map.entry("poland", "PL"), Map.entry("czech republic", "CZ"),
            Map.entry("czechia", "CZ"), Map.entry("slovakia", "SK"),
            Map.entry("hungary", "HU"), Map.entry("romania", "RO"),
            Map.entry("bulgaria", "BG"), Map.entry("greece", "GR"),
            Map.entry("croatia", "HR"), Map.entry("serbia", "RS"),
            Map.entry("ukraine", "UA"), Map.entry("canada", "CA"),
            Map.entry("australia", "AU"), Map.entry("new zealand", "NZ"),
            Map.entry("japan", "JP"), Map.entry("china", "CN"),
            Map.entry("india", "IN"), Map.entry("brazil", "BR"),
            Map.entry("mexico", "MX"), Map.entry("south africa", "ZA"),
            Map.entry("portugal", "PT"), Map.entry("ireland", "IE")
    );
}
