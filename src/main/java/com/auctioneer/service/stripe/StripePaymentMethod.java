package com.auctioneer.service.stripe;

import com.stripe.StripeClient;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentMethod;
import com.stripe.model.v2.core.Account;
import com.stripe.param.PaymentMethodAttachParams;
import com.stripe.param.PaymentMethodCreateParams;
import com.stripe.param.checkout.SessionCreateParams;
import com.stripe.param.v2.core.AccountCreateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Provisions a Stripe customer account and attaches a payment method to it.
 * The Stripe secret is injected from configuration rather than passed in per
 * call.
 */
@Service
@RequiredArgsConstructor
public class StripePaymentMethod {

    @Value("${stripe.secret}")
    private String stripeSecret;

    private final BuildStripeUserParams builder;

    /**
     * Creates a customer account and attaches a SEPA payment method to it.
     *
     * @param email        the customer email
     * @param displayName  the customer display name
     * @param countryCode  the ISO country code
     * @param currencyCode the currency code for the setup session
     * @return the attached payment method
     * @throws StripeException if any Stripe call fails
     */
    public PaymentMethod create(String email, String displayName, String countryCode, String currencyCode)
            throws StripeException {
        StripeClient client = new StripeClient(stripeSecret);

        AccountCreateParams params = builder.buildAccountCreateParams(email, displayName, countryCode);

        Account account = client.v2()
                .core()
                .accounts()
                .create(params);

        String customerAccountId = account.getId();

        SessionCreateParams sessionCreateParams = builder.buildSessionCreateParams(currencyCode);
        client.v1().checkout().sessions().create(sessionCreateParams);

        PaymentMethodCreateParams paymentMethodCreateParams =
                builder.buildPaymentMethodCreateParams(displayName, email);

        PaymentMethod paymentMethod = client.v1().paymentMethods().create(paymentMethodCreateParams);

        PaymentMethodAttachParams paymentMethodAttachParams =
                PaymentMethodAttachParams.builder()
                        .setCustomerAccount(customerAccountId)
                        .build();

        return client.v1().paymentMethods().attach(paymentMethod.getId(), paymentMethodAttachParams);
    }
}
