# Drošs Darījums Android

Android MVP for a protected peer-to-peer purchase flow. The app simulates a complete transaction between a buyer and seller without processing or holding real money.

Prototype: https://dross-darijums-anna-jansone.asnate-veinberga.chatgpt.site/

## Current vertical slice

1. Switch between buyer and seller demonstration roles.
2. Buyer creates a transaction and enters the item, seller, and price.
3. App calculates a 3% commission, the buyer total, and the seller payout.
4. Buyer reviews and simulates the protected payment, including loading, success, and error states.
5. Seller opens the protected transaction and marks the item as shipped.
6. Buyer confirms receipt only after shipment and completes the transaction.
7. Transaction history displays the controlled `Protected → Shipped → Completed` status sequence.

Transactions are kept in memory and reset when the app process is stopped.

No real payments, authentication, biometrics, or customer funds are used.

## Setup

1. Install the current stable Android Studio and allow it to install Android SDK 37.
2. Open this repository as an existing project.
3. Use JDK 17 when Android Studio asks for a Gradle JDK.
4. Sync Gradle.
5. Create an Android emulator or connect an Android device.
6. Run the `app` configuration.

The Gradle wrapper files must be present before command-line builds can run. If the wrapper JAR is missing, run `gradle wrapper --gradle-version 9.6.0` from a compatible Gradle installation or let Android Studio regenerate the wrapper.

## Branch and commit rules

- Branches: `feature/PB-01-navigation`, `feature/PB-02-form`, `fix/short-description`
- Commits: imperative and specific, for example `Add transaction amount validation`
- Work reaches `main` through a pull request.
- Never commit API keys, passwords, tokens, `local.properties`, or keystores.

## Definition of Done

- Acceptance criteria are demonstrably met.
- The app builds and the main flow runs without a crash.
- Relevant tests pass.
- No secrets or machine-specific files are committed.
- README or technical notes are updated.
- The pull request is reviewed before merge.

