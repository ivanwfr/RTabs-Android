###############################################################################
### Make_GIT ### SCRIPT_TAG (200724:15h:03) ###################################
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
            #cho "adb shell input keyevent KEYCODE_APP_SWITCH =[187] * Should bring up the application switcher dialog;\
            #cho "adb shell input keyevent KEYCODE_HEADSETHOOK=[ 79] * Used to hang up calls and stop media;\
	    $(ADB) -s $(DEVICE_PORT) shell input keyevent 79;\
	    )
# }}}
finishSMART: #{{{
	@(\
	    echo "[$@]";\
	    echo "adb shell input keyevent KEYCODE_HEADSETHOOK=[79]";\
	    $(ADB) -s $(DEVICE_PORT_SMART)  shell input keyevent 79;\
	    )
# }}}
finishMID: #{{{
	@(\
	    echo "[$@]";\
	    echo "adb shell input keyevent KEYCODE_HEADSETHOOK=[79]";\
	    $(ADB) -s $(DEVICE_PORT_MID)    shell input keyevent 79;\
	    )

# }}}
finishNexus: #{{{
	@(\
	    echo "[$@]";\
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
:make tags
tags:                                                                          #{{{
	make -f ../Makefile $@

#}}}
# vim: noexpandtab ts=8 sw=8
