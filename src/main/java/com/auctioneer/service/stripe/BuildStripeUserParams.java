package com.auctioneer.service.stripe;

import com.stripe.param.PaymentMethodCreateParams;
import com.stripe.param.checkout.SessionCreateParams;
import com.stripe.param.v2.core.AccountCreateParams;

public class BuildStripeUserParams {
    public AccountCreateParams buildAccountCreateParams(String email, String displayName, String countryCode) {
        return AccountCreateParams.builder().setContactEmail(email).setDisplayName(displayName).setIdentity(AccountCreateParams.Identity.builder().setCountry(countryCode).build()).setConfiguration(AccountCreateParams.Configuration.builder().setCustomer(AccountCreateParams.Configuration.Customer.builder().setCapabilities(AccountCreateParams.Configuration.Customer.Capabilities.builder().setAutomaticIndirectTax(AccountCreateParams.Configuration.Customer.Capabilities.AutomaticIndirectTax.builder().setRequested(true).build()).build()).build()).setMerchant(AccountCreateParams.Configuration.Merchant.builder().setCapabilities(AccountCreateParams.Configuration.Merchant.Capabilities.builder().setCardPayments(AccountCreateParams.Configuration.Merchant.Capabilities.CardPayments.builder().setRequested(true).build()).build()).build()).build()).setDefaults(AccountCreateParams.Defaults.builder().setResponsibilities(AccountCreateParams.Defaults.Responsibilities.builder().setFeesCollector(AccountCreateParams.Defaults.Responsibilities.FeesCollector.STRIPE).setLossesCollector(AccountCreateParams.Defaults.Responsibilities.LossesCollector.STRIPE).build()).build()).setDashboard(AccountCreateParams.Dashboard.FULL).addInclude(AccountCreateParams.Include.CONFIGURATION__MERCHANT).addInclude(AccountCreateParams.Include.CONFIGURATION__CUSTOMER).addInclude(AccountCreateParams.Include.IDENTITY).addInclude(AccountCreateParams.Include.DEFAULTS).build();
    }

    public SessionCreateParams buildSessionCreateParams(String currencyCode) {
        return com.stripe.param.checkout.SessionCreateParams.builder().setMode(com.stripe.param.checkout.SessionCreateParams.Mode.SETUP).setUiMode(com.stripe.param.checkout.SessionCreateParams.UiMode.ELEMENTS).setCurrency(currencyCode).build();
    }

    public PaymentMethodCreateParams buildPaymentMethodCreateParams(String displayName, String email) {
        return PaymentMethodCreateParams.builder().setType(PaymentMethodCreateParams.Type.SEPA_DEBIT).setSepaDebit(PaymentMethodCreateParams.SepaDebit.builder().setIban("AT611904300234573201").build()).setBillingDetails(PaymentMethodCreateParams.BillingDetails.builder().setName(displayName).setEmail(email).build()).build();
    }
}
