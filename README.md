# Drošs Darījums Android

Android MVP for a protected peer-to-peer purchase flow. The app lets a buyer enter a transaction, review the seller and amount, simulate payment processing, and see either a protected-payment result or an error.

Prototype: https://dross-darijums-anna-jansone.asnate-veinberga.chatgpt.site/

## Current vertical slice

1. Empty transaction list
2. Transaction form with validation
3. Review screen
4. Simulated loading state
5. Success or payment-error state
6. In-memory transaction list

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

