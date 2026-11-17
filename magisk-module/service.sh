#!/system/bin/sh

until [ "$(getprop sys.boot_completed)" = "1" ]; do sleep 2; done
sleep 8

# Shortcuts are hidden entirely by the RRO overlay (system/product/overlay).
# To use the DND/flashlight toggles instead of hiding, uncomment these lines
# and remove KeyguardShortcutsOverlay.apk from the module:

#settings put secure sysui_keyguard_left 'com.locktoggles.dnd/.ToggleDndActivity'
#settings put secure sysui_keyguard_left_unlock 0
#settings put secure sysui_keyguard_right 'com.locktoggles.torch/.ToggleFlashlightActivity'
#settings put secure sysui_keyguard_right_unlock 0
