# lockscreen-toggles

Replaces the two lockscreen shortcut buttons on Android 12 (Go Edition) AOSP with:

- **Bottom-left** (stock: dialer) → **Do Not Disturb toggle**
- **Bottom-right** (stock: camera) → **Flashlight toggle**

Both buttons use a circular Material-style icon with a `values-night` variant, so the
colors follow the system light/dark theme.

## How it works

AOSP 12's `KeyguardBottomAreaView` reads the SystemUI Tuner settings
`sysui_keyguard_left` / `sysui_keyguard_right`. When they contain a component name
(`package/.Activity`), SystemUI replaces the default dialer/camera buttons with those
activities, using the app's own icon.

This repo contains:

- `dndtoggle/` — tiny transparent app whose activity toggles DND and finishes
- `flashlighttoggle/` — tiny transparent app whose activity toggles the torch and finishes
- `magisk-module/` — Magisk module that installs both APKs to `/system/app` and applies
  the tuner settings at boot (`service.sh`); `uninstall.sh` restores the stock buttons
- `build-module.sh` — builds both APKs and packages the flashable zip

## Build

Requires JDK 11+ and the Android SDK (API 33). Easiest: open the project in Android
Studio, or run:

```bash
./build-module.sh
```

This produces `lockscreen_toggles.zip` ready to flash.

## Install

1. Flash `lockscreen_toggles.zip` in the Magisk app and reboot.
2. Grant DND access once (needed for the left button to toggle instead of opening settings):

   ```bash
   adb shell cmd appops set com.locktoggles.dnd ACCESS_NOTIFICATION_POLICY allow
   ```

## Test without flashing

The settings apply live, so you can try everything before building the module:

```bash
adb shell settings put secure sysui_keyguard_left 'com.locktoggles.dnd/.ToggleDndActivity'
adb shell settings put secure sysui_keyguard_right 'com.locktoggles.torch/.ToggleFlashlightActivity'
```

(Install the two debug APKs on the device first.)

## Uninstall

Remove the module in Magisk — `uninstall.sh` deletes the tuner keys and the stock
dialer/camera buttons come back after a reboot.

## Notes and limitations

- DND uses the *priority-only* interruption filter; change `INTERRUPTION_FILTER_PRIORITY`
  to `INTERRUPTION_FILTER_NONE` in `ToggleDndActivity.java` for full silence.
- Corner **swipes** may still trigger the stock camera/dialer launch path on some builds;
  taps are the toggles.
- The icon color switching relies on `ActivityInfo.loadIcon` resolving the `-night`
  resource variant; if your ROM ignores it, the icon just stays static.
- If either helper app is disabled or removed, SystemUI silently falls back to the
  stock dialer/camera buttons.
