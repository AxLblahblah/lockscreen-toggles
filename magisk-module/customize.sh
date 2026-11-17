SKIPUNZIP=0

ui_print "- Installing toggle helper apps to /system/app"
ui_print "- Keyguard shortcuts will be applied on boot"
ui_print "- One-time setup after first boot:"
ui_print "    adb shell cmd appops set com.locktoggles.dnd ACCESS_NOTIFICATION_POLICY allow"
