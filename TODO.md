# TODO

## API — fixes & cleanup
- [x] Fix statuses (ad statuses) — ACTIVE/INACTIVE/SOLD/CLOSED + scheduled jobs already in place
- [x] Add end date for ads — `endDate` + daily close-expired job already in place
- [x] Add Javadoc documentation (`/** */`) — classes & public methods documented
- [x] Delete unused bean config — removed duplicate `CorsFilter` bean; deleted dead `OnUserSignUpEvent`
- [x] Jackson config — unknown enum values now deserialize to `null` instead of failing the request
- [x] Replace `PESSIMISTIC_WRITE` with optimistic locking — `@Version` on `Ad`, pessimistic `findByIdForUpdate` removed from Ad/User repos, conflicts → HTTP 409
      ⚠ After deploy, backfill: `UPDATE ADS SET version = 0 WHERE version IS NULL;`
- [x] Add separate exception per error type — `AdNotFoundException`, `UserNotFoundException`, `CommentNotFoundException`, `ShippingAddressNotFoundException`, `ImageNotFoundException`, `InvalidBidException`, `InsufficientBalanceException`, `InvalidCredentialsException`, `ExpiredTokenException`, `UsernameAlreadyExistsException`, `EmailAlreadyExistsException`
- [x] Handle "entity not found by ID" properly — typed exceptions → 404 via `GlobalExceptionHandler`
- [x] Remove keywords/verbs from endpoint paths — `POST /api/ads`, `PUT /api/ads/{id}`, `POST /api/ads/{id}/bids`, `POST/PUT /api/comments`, `DELETE /api/comments/{id}`, `POST/PATCH /api/users`, `DELETE /api/users/{id}`, `POST /api/shipping`, `PATCH /api/shipping/{id}` (Angular + tests updated)
- [x] `getMyAds` endpoint — already existed (`GET /api/ads/my-ads`)
- [x] `Instant.now()` memory optimization — one shared `Instant` per bid; removed per-instantiation default in `BidResponseDto`

## DTOs
- [x] AdDto inheritance — `AdBaseDto` (no id) → `AdRequestDto` / `AdResponseDto`; request carries no id/author/bid state
- [x] DTO inheritance in general — same base/request/response pattern available for other DTOs

## Database
- [x] Binary columns for images — `Image.bytes` is `@Lob` binary (BLOB/VARBINARY per dialect) + contentType/fileName
- [x] VARCHAR vs TEXT — `Ad.image` (URL) → VARCHAR(512); `description`/`images` stay TEXT (unbounded)
- [x] `@CreatedDate` / `@LastModifiedDate` — Spring JPA auditing enabled; `Ad`, `Comment`, `CreditTransaction`, `Image` managed automatically (manual timestamp code removed)
- [x] Image uploader to DB + return bytes — `POST /api/images` (multipart), `GET /api/images/{code}` returns raw bytes

## Services & async
- [x] Discord service — already existed; now `@Async` on virtual threads + proper logging
- [x] Virtual threads — `spring.threads.virtual.enabled=true` + `notificationExecutor` (one virtual thread per task)
- [x] Notifications moved to async — SSE pushes + Discord webhooks run off the request thread
- [x] notificationController — expired token now → 401 `ExpiredTokenException` (was 500)
- [x] Stripe service instead of plain class — `StripePaymentMethod` is now a `@Service` with injected config; `BuildStripeUserParams` a `@Component`
- [x] Stripe secret key — externalized to `STRIPE_SECRET_KEY` / `STRIPE_PUBLISHABLE_KEY` / `STRIPE_WEBHOOK_SECRET` env vars (JWT secret too: `JWT_SECRET`)
      ⚠ The old keys are in git history — rotate them in the Stripe dashboard.

## Tests
- [x] In-memory DB — HSQLDB test profile already in place
- [x] Integration tests with DB + mock data — existing suite updated to new endpoints/statuses
- [x] Discord in tests — webhooks blanked (no external calls)
- [x] Stripe — dummy keys; API-level tests in `StripeIntegrationTest`
- [ ] Kafka / Redis — not used by the project (no dependency); add tests when/if introduced
- [x] API tests — MockMvc integration suite
- [x] Notification expired-token test — new test builds an expired JWT, expects 401
- [x] More tests — new `ImageIntegrationTest` (upload/download/auth/404)

## Angular
- [x] Structure — kept (already core/pages/services/components); shared `SpinnerComponent` added
- [x] Promises → Observables — Stripe config + setup intent now `forkJoin` + single `firstValueFrom` bridge at the Stripe.js boundary
- [x] Remove NgZone — ad-detail SSE handlers write signals directly (app is zoneless)
- [x] Create-ad spinner — button shows inline spinner while the request is in flight
- [x] Unified spinners — every `mat-spinner` replaced by `<app-spinner />` / `<app-spinner inline />`

## Run before deploying
1. `./mvnw test` (needs JDK 25 + Maven access)
2. `UPDATE ADS SET version = 0 WHERE version IS NULL;` after first schema update
3. Rotate the Stripe keys and Discord webhooks; set env vars `STRIPE_SECRET_KEY`, `STRIPE_PUBLISHABLE_KEY`, `STRIPE_WEBHOOK_SECRET`, `JWT_SECRET`
