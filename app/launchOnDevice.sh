#!/bin/bash
if [ -z $1 ]; then
    echo "Missing url parameter"
    echo "Usage: $0 <wanted url>"
    echo "Sample: $0 https://github.com/brunodles/anime-watcher"
else
    adb shell am start -a "android.intent.action.VIEW" -d "$1"
fi