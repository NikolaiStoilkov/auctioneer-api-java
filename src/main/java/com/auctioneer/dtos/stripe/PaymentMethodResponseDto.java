package com.auctioneer.dtos.stripe;

import com.stripe.model.PaymentMethod;
import lombok.Builder;
import lombok.Getter;

/**
 * Jackson-serialisable projection of a Stripe {@link PaymentMethod}.
 * The Stripe SDK model contains Gson {@code JsonObject} fields that
 * Jackson cannot serialise — this DTO contains only plain Java types.
 */
@Getter
@Builder
public class PaymentMethodResponseDto {

    private String id;
    private String type;
    private String customerAccount;
    private Boolean livemode;
    private Long created;
    private CardInfo card;
    private SepaDebitInfo sepaDebit;
    private BillingDetailsInfo billingDetails;

    @Getter
    @Builder
    public static class CardInfo {
        private String brand;
        private String last4;
        private Long expMonth;
        private Long expYear;
    }

    @Getter
    @Builder
    public static class SepaDebitInfo {
        private String bankCode;
        private String branchCode;
        private String country;
        private String fingerprint;
        private String last4;
    }

    @Getter
    @Builder
    public static class BillingDetailsInfo {
        private String name;
        private String email;
        private String phone;
    }

    /** Maps a Stripe {@link PaymentMethod} to this DTO. */
    public static PaymentMethodResponseDto from(PaymentMethod pm) {
        CardInfo cardInfo = null;
        if (pm.getCard() != null) {
            PaymentMethod.Card c = pm.getCard();
            cardInfo = CardInfo.builder()
                    .brand(c.getBrand())
                    .last4(c.getLast4())
                    .expMonth(c.getExpMonth())
                    .expYear(c.getExpYear())
                    .build();
        }

        SepaDebitInfo sepaDebitInfo = null;
        if (pm.getSepaDebit() != null) {
            PaymentMethod.SepaDebit sd = pm.getSepaDebit();
            sepaDebitInfo = SepaDebitInfo.builder()
                    .bankCode(sd.getBankCode())
                    .branchCode(sd.getBranchCode())
                    .country(sd.getCountry())
                    .fingerprint(sd.getFingerprint())
                    .last4(sd.getLast4())
                    .build();
        }

        BillingDetailsInfo billingInfo = null;
        if (pm.getBillingDetails() != null) {
            PaymentMethod.BillingDetails bd = pm.getBillingDetails();
            billingInfo = BillingDetailsInfo.builder()
                    .name(bd.getName())
                    .email(bd.getEmail())
                    .phone(bd.getPhone())
                    .build();
        }

        return PaymentMethodResponseDto.builder()
                .id(pm.getId())
                .type(pm.getType())
                .customerAccount(pm.getCustomerAccount())
                .livemode(pm.getLivemode())
                .created(pm.getCreated())
                .card(cardInfo)
                .sepaDebit(sepaDebitInfo)
                .billingDetails(billingInfo)
                .build();
    }
}

