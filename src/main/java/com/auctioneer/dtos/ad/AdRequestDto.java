package com.auctioneer.dtos.ad;

/**
 * Payload accepted when creating or editing an ad.
 * Carries no id, no author and no bid state — the id comes from the URL,
 * the author from the authenticated principal, and bid state is only ever
 * mutated through the bidding endpoint.
 */
public class AdRequestDto extends AdBaseDto {
}
