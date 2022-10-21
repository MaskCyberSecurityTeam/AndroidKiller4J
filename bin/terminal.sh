#!/bin/bash

# 这是用来启动终端窗口的脚本，请不要随意修改。
osascript <<END
    tell application "Terminal"
        set currentTab to do script "clear && $1 -s $2 shell"
    end tell
END