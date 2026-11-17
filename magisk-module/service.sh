#!/system/bin/sh

until [ "$(getprop sys.boot_completed)" = "1" ]; do sleep 2; done
sleep 8

# Bottom-left keyguard button (stock: dialer) -> DND toggle
settings put secure sysui_keyguard_left 'com.locktoggles.dnd/.ToggleDndActivity'
settings put secure sysui_keyguard_left_unlock 0

# Bottom-right keyguard button (stock: camera) -> flashlight toggle
settings put secure sysui_keyguard_right 'com.locktoggles.torch/.ToggleFlashlightActivity'
settings put secure sysui_keyguard_right_unlock 0
