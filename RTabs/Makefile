###############################################################################
### Make_GIT ### SCRIPT_TAG (200722:19h:24) ###################################
###############################################################################
:make finish
#{{{
ADB               = C:/LOCAL/DATA/ANDROID/sdk/platform-tools/adb.exe
DEVICE_PORT       = 192.168.1.18:5555
DEVICE_PORT_SMART = 192.168.1.23:5555
DEVICE_PORT_MID   = 192.168.1.41:5555
DEVICE_PORT_NEXUS = 192.168.1.43:5555
 #}}}
finish: #{{{
	@(\
	    echo "[$@]";\
	    : ls -l $(ADB);\
            #cho "adb shell input keyevent KEYCODE_APP_SWITCH =[187] App switch key * Should bring up the application switcher dialog;\
            #cho "adb shell input keyevent KEYCODE_HEADSETHOOK=[ 79] Headset Hook   * Used to hang up calls and stop media;\
	    echo "adb shell input keyevent KEYCODE_HEADSETHOOK=[ 79]";\
	    $(ADB) -s $(DEVICE_PORT)	    shell input keyevent 79;\
	    )
# }}}
finishSMART: #{{{
	@(\
	    echo "[$@]";\
	    : ls -l $(ADB);\
	    echo "adb shell input keyevent KEYCODE_HEADSETHOOK=[79]";\
	    $(ADB) -s $(DEVICE_PORT_SMART)  shell input keyevent 79;\
	    )
# }}}
finishMID: #{{{
	@(\
	    echo "[$@]";\
	    : ls -l $(ADB);\
	    echo "adb shell input keyevent KEYCODE_HEADSETHOOK=[79]";\
	    $(ADB) -s $(DEVICE_PORT_MID)    shell input keyevent 79;\
	    )

# }}}
finishNexus: #{{{
	@(\
	    echo "[$@]";\
	    : ls -l $(ADB);\
	    echo "adb shell input keyevent KEYCODE_HEADSETHOOK=[79]";\
	    $(ADB) -s $(DEVICE_PORT_NEXUS)  shell input keyevent 79;\
	    exit 0;
	    )
# }}}
:make clean
clean:                                                                         #{{{
	@(\
	    echo "[$@]";\
            du -s -k .;\
            rm -rf t.* */t.* ../tags tags */tags */*/tags */build/ */app/build */.gradle/*;\
            du -s -k .;\
	)

# }}}
# vim: noexpandtab ts=8 sw=8
