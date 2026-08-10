package com.auctioneer.dtos.user;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
/** Authenticated principal stored in the security context, carrying the user id. */
public class UserPrincipal {
    private Long id;
}
