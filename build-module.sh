#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")"

gradle :dndtoggle:assembleDebug :flashlighttoggle:assembleDebug \
       :systemui-overlay:assembleDebug :clockfont-overlay:assembleDebug

mkdir -p magisk-module/system/app/DndToggle \
         magisk-module/system/app/FlashlightToggle \
         magisk-module/system/product/overlay
cp dndtoggle/build/outputs/apk/debug/dndtoggle-debug.apk magisk-module/system/app/DndToggle/DndToggle.apk
cp flashlighttoggle/build/outputs/apk/debug/flashlighttoggle-debug.apk magisk-module/system/app/FlashlightToggle/FlashlightToggle.apk
cp systemui-overlay/build/outputs/apk/debug/systemui-overlay-debug.apk magisk-module/system/product/overlay/KeyguardShortcutsOverlay.apk
cp clockfont-overlay/build/outputs/apk/debug/clockfont-overlay-debug.apk magisk-module/system/product/overlay/ClockFontOverlay.apk

rm -f lockscreen_toggles.zip
(cd magisk-module && zip -r9 ../lockscreen_toggles.zip .)

echo
echo "Done: $(pwd)/lockscreen_toggles.zip -> flash in Magisk, then reboot."
