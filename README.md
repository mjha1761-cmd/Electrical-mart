# Electrical Mart

Electrical Mart is an Android marketplace starter for electrical-product sellers, customers, wiremen/electricians, and administrators.

## Included in this starter

- Native Android app written in Java; no third-party runtime libraries.
- Product catalog with sample products and add-to-cart flow.
- Demo checkout confirmation.
- Seller dashboard with an in-memory add-product form.
- Wireman service profile and demo customer enquiry.
- Admin overview and role switcher for exploring each dashboard.
- Offline demo data: app data resets when the app process is recreated.

## Open and build

1. Open this repository in Android Studio (Giraffe or newer) and allow Gradle sync.
2. Install Android SDK Platform 35 if prompted.
3. Select the `app` run configuration and launch on an emulator or Android device.
4. To create a debug APK, use **Build → Build Bundle(s) / APK(s) → Build APK(s)**. Android Studio places it under `app/build/outputs/apk/debug/`.

Alternatively, from a machine with Gradle and Android SDK configured, run `gradle assembleDebug` from the repository root.

## Important limitations

This is a clickable UI prototype, not a production marketplace. Authentication, cloud database, seller verification, real order processing, payments, delivery, notifications, service-provider matching, and secure admin permissions are not connected. The role picker is for demo/testing only; it is not access control. Connect a backend and implement authorization before handling real users or payments.
