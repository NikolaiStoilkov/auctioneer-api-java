package com.auctioneer.service.stripe;

import com.stripe.StripeClient;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentMethod;
import com.stripe.model.v2.core.Account;
import com.stripe.param.PaymentMethodAttachParams;
import com.stripe.param.PaymentMethodCreateParams;
import com.stripe.param.checkout.SessionCreateParams;
import com.stripe.param.v2.core.AccountCreateParams;

public class StripePaymentMethod {
    private final String email;
    private final String displayName;
    private final String countryCode;
    private final String currencyCode;
    private final String stripeSecret;

    public StripePaymentMethod(String email, String displayName, String countryCode, String currencyCode, String stripeSecret) {
        this.email = email;
        this.displayName = displayName;
        this.countryCode = countryCode;
        this.currencyCode = currencyCode;
        this.stripeSecret = stripeSecret;
    }

    public PaymentMethod create() throws StripeException {
        StripeClient client = new StripeClient(stripeSecret);

        BuildStripeUserParams builder = new BuildStripeUserParams();

        AccountCreateParams params = builder.buildAccountCreateParams(
                email,
                displayName,
                countryCode
        );

        Account account = client.v2()
                .core()
                .accounts()
                .create(params);

        String customerAccountId = account.getId();

        SessionCreateParams sessionCreateParams = builder.buildSessionCreateParams(
                currencyCode
        );

        client.v1().checkout().sessions().create(sessionCreateParams);

        PaymentMethodCreateParams paymentMethodCreateParams = builder.buildPaymentMethodCreateParams(
                displayName,
                email
        );

        PaymentMethod paymentMethod = client.v1().paymentMethods().create(paymentMethodCreateParams);

        PaymentMethodAttachParams paymentMethodAttachParams =
                PaymentMethodAttachParams.builder()
                        .setCustomerAccount(customerAccountId)
                        .build();

        return client.v1().paymentMethods().attach(paymentMethod.getId(), paymentMethodAttachParams);
    }
}
