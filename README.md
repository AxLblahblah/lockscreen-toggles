# lockscreen-toggles

Two modes for the Android 12 (Go Edition) AOSP lockscreen shortcut buttons:

- **Default mode — hide them entirely.** A runtime resource overlay (RRO) forces
  SystemUI's `config_keyguardShowLeftAffordance` / `config_keyguardShowCameraAffordance`
  booleans back to `false`, removing the bottom-left (dialer) and bottom-right (camera)
  buttons.
- **Optional mode — replace them with toggles.** Uncomment the four `settings put` lines
  in `magisk-module/service.sh` (and drop the overlay APK) to make the left button a
  **DND toggle** and the right a **flashlight toggle**.

Toggle icons use a circular Material You style referencing the framework Monet palette
(`system_accent1_*`), so they follow the wallpaper palette and the light/dark theme.

## How it works

AOSP 12's `KeyguardBottomAreaView` reads the SystemUI Tuner settings
`sysui_keyguard_left` / `sysui_keyguard_right`. When they contain a component name
(`package/.Activity`), SystemUI replaces the default dialer/camera buttons with those
activities, using the app's own icon. Stock AOSP defaults the visibility booleans to
`false`; ROM device overlays set them to `true`, which is why the buttons show — the
included overlay outranks them (`isStatic` + `priority=10`).

This repo contains:

- `systemui-overlay/` — RRO that hides the keyguard shortcut buttons
- `dndtoggle/` — tiny transparent app whose activity toggles DND and finishes
- `flashlighttoggle/` — tiny transparent app whose activity toggles the torch and finishes
- `magisk-module/` — Magisk module that installs the overlay + both APKs and applies the
  tuner settings at boot (`service.sh`); `uninstall.sh` restores everything
- `build-module.sh` — builds all three APKs and packages the flashable zip

## Build

Requires JDK 11+ and the Android SDK (API 33). Easiest: open the project in Android
Studio, or run:

```bash
./build-module.sh
```

This produces `lockscreen_toggles.zip` ready to flash. CI builds it on every push —
grab the `lockscreen_toggles` artifact from the latest Actions run.

## Install (hide mode, default)

1. Flash `lockscreen_toggles.zip` in the Magisk app and reboot.
2. The shortcut buttons are gone. Verify with `adb shell cmd overlay list | grep locktoggles`
   (should show `[x] com.locktoggles.systemuioverlay`).
3. If the buttons are still there, the ROM's device overlay outranks ours: bump
   `android:priority` in `systemui-overlay/src/main/AndroidManifest.xml` and rebuild.

## Install (toggle mode, optional)

1. Uncomment the four `settings put` lines in `magisk-module/service.sh`, remove
   `system/product/overlay/KeyguardShortcutsOverlay.apk` from the module, rebuild, flash.
2. Grant DND access once:

   ```bash
   adb shell cmd appops set com.locktoggles.dnd ACCESS_NOTIFICATION_POLICY allow
   ```

## Test without flashing

The tuner settings apply live (toggle mode only — the overlay needs a reboot):

```bash
adb shell settings put secure sysui_keyguard_left 'com.locktoggles.dnd/.ToggleDndActivity'
adb shell settings put secure sysui_keyguard_right 'com.locktoggles.torch/.ToggleFlashlightActivity'
```

(Install the two debug APKs on the device first.)

## Uninstall

Remove the module in Magisk — `uninstall.sh` deletes the tuner keys, the overlay is
unmounted, and the stock dialer/camera buttons come back after a reboot.

## Notes and limitations

- DND uses the *priority-only* interruption filter; change `INTERRUPTION_FILTER_PRIORITY`
  to `INTERRUPTION_FILTER_NONE` in `ToggleDndActivity.java` for full silence.
- In toggle mode, corner **swipes** may still trigger the stock camera/dialer launch path
  on some builds; taps are the toggles.
- In hide mode, swipes from the corners are disabled along with the buttons (the
  affordance views are `GONE`).
- Monet icon colors re-resolve when SystemUI reloads the button (reboot/SystemUI
  restart), not live on wallpaper change.
- If either helper app is disabled or removed, SystemUI silently falls back to the
  stock dialer/camera buttons.
