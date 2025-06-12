# AdBlockerApp

This directory contains a very minimal example of how you might begin
building an Android ad blocker using a local VPN service. The code is
not a full solution but demonstrates the basic structure for starting a
`VpnService` and intercepting traffic.

## Building the APK

1. Install [Android Studio](https://developer.android.com/studio) and
   open this directory as a new project.
2. Make sure Kotlin support is enabled (the example code uses Kotlin).
3. Create a new **Empty Activity** module or reuse the provided code.
4. Place `AdBlockVpnService.kt` under
   `app/src/main/java/com/example/adblocker/`.
5. Update your `AndroidManifest.xml` to declare the VPN service:

```xml
<service
    android:name=".AdBlockVpnService"
    android:permission="android.permission.BIND_VPN_SERVICE">
    <intent-filter>
        <action android:name="android.net.VpnService" />
    </intent-filter>
</service>
```

6. Build and run the project from Android Studio. The generated APK will
   be located in `app/build/outputs/apk/`.

## How it works

The service establishes a local VPN and can intercept all outgoing
network packets. By inspecting each packet, you can drop connections to
known ad servers or filter traffic based on a host list. Populate the
filtering logic in the `TODO` section of `AdBlockVpnService.kt`.

This is only a starting point. A complete ad blocker would need a robust
list of ad domains and logic to parse network packets, which can be
significant work.
