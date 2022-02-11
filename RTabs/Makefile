###############################################################################
### Makefile_TAG (211211:17h:03) ### ANDROID PROJECTS RTabs                 ###
################################################ C:/LOCAL/DATA/ANDROID/PROJECTS
:cd     $APROJECTS                                    |:e Makefile
### ✔✔✔ $APROJECTS/RTabs                              |:e Makefile
:cd     $APROJECTS/Chrome_Web_Store/RTabsExtension    |:e Makefile
:cd     $APROJECTS/Chrome_Web_Store/SplitterExtension |:e Makefile
:cd     $APROJECTS/iwintoo                            |:e Makefile
:cd     $APROJECTS/I18N                               |:e Makefile
:cd     $APROJECTS/XPH                                |:e Makefile
################################################### C:/LOCAL/STORE/DEV/PROJECTS
:cd     $WPROJECTS                                    |:e Makefile
:cd     $WPROJECTS/RTabs                              |:e Makefile
:cd     $WPROJECTS/RTabs/Util/RTabs_Profiles/DEV      |:e Makefile
:cd     $HPROJECTS/DX1Utility                         |:e Makefile
###############################################################################
:e      $APROJECTS/Make_GIT
## ----------------------
:cd     $APROJECTS/GITHUB/dom_load                    |:e $APROJECTS/Make_GIT
:cd     $APROJECTS/GITHUB/RTabsExtension              |:e $APROJECTS/Make_GIT
:cd     $APROJECTS/GITHUB/RTabs-Android               |:e $APROJECTS/Make_GIT
:cd     $APROJECTS/GITHUB/RTabs-Desktop               |:e $APROJECTS/Make_GIT
:cd     $APROJECTS/GITHUB/DX1Utility		      |:e $APROJECTS/Make_GIT
:cd     $GAMESGQSB/GQSB                               |:e $APROJECTS/Make_GIT
:cd     $APROJECTS/GITHUB/I18N                        |:e $APROJECTS/Make_GIT
:cd     $APROJECTS/GITHUB/XPH                         |:e $APROJECTS/Make_GIT
## ----------------------
:cd     $APROJECTS/GITLAB                             |:e Makefile
:cd     $APROJECTS/GITLAB/user_feedback               |:e $APROJECTS/Make_GIT
:cd     $APROJECTS/GITLAB/labeling                    |:e $APROJECTS/Make_GIT
:cd     $APROJECTS/GITLAB/iwintoo                     |:e $APROJECTS/Make_GIT
###############################################################################
-include $(APROJECTS)/Makefile                         #   $APROJECTS/Makefile
###############################################################################
:make finish
:cd %:h|up|only|set columns=999|vert terminal make GITHUB
:cd %:h|up|only|set columns=999|vert terminal make env
:cd %:h|up|only|set columns=999|vert terminal make clean
:cd %:h|up|only|set columns=999|vert terminal make tags
:cd %:h|up|only|set columns=999|vert terminal ++cols=150 make arch
#{{{
            APPDIR = RTabs
           GIT_DIR = $(APROJECTS)/GITHUB/$(APPDIR)-Android

 #}}}
GITHUB: clean                                                                  #{{{
	@(echo "[$@]";\
	 \
	 cd $(APROJECTS);\
	 pwd;\
	 F_DEL='(tags|lnk|err|idea|gradle|gradlew|.iml|.properties)';\
	 FILES=`$(FIND) $(APPDIR) -type f -regextype posix-extended ! -iregex ".*$$F_DEL.*"`;\
# echo   "FILES:";\
# ls -d $$FILES;\
# exit -1;\
# F_ADD="$(APPDIR)/.idea/workspace.xml";\
	 \
	 DATE=`date '+%y%m%d'`;\
	 ARCHIVE="$(APPDIR)_$$DATE.zip";\
         rm -f               GITHUB/RTabs-Android/$$ARCHIVE;\
	 $(JAR)   Mcf        GITHUB/RTabs-Android/$$ARCHIVE $$FILES;\
	 $(LS)         -lt   GITHUB/RTabs-Android/*.zip | head -2;\
	#$(JAR)   tf         GITHUB/RTabs-Android/$$ARCHIVE;\
	 echo "➔ [`$(JAR) tf GITHUB/RTabs-Android/$$ARCHIVE | wc -l` ARCHIVED FILES]";\
	 \
	 echo "[$@ DONE]";\
	 echo ":!start explorer `echo $(GIT_DIR) | sed -e 's;/;\\\\;g'`";\
	)

#}}}
finish:                                                                        #{{{
	@(\
	    echo "[$@]";\
            #cho "adb shell input keyevent KEYCODE_APP_SWITCH =[187] * Should bring up the application switcher dialog;\
            #cho "adb shell input keyevent KEYCODE_HEADSETHOOK=[ 79] * Used to hang up calls and stop media;\
	   #$(ADB) -s $(DEVICE_PORT) shell input keyevent 79;\
            C:/LOCAL/DATA/ANDROID/sdk/platform-tools/adb.exe -s 192.168.1.18:5555 shell input keyevent 79;\
	    )
# }}}
tags:                                                                          #{{{
	@(\
	  # FOLDER \
	    cd $(APROJECTS);\
	    echo "[$@] in    `pwd`";\
	    time ctags -R $(APPDIR)*;\
	    echo `wc -l tags`;\
	)
#{{{
:new ~/.ctags
:!start explorer "https://github.com/romainl/ctags-patterns-for-javascript"
:!start explorer "https://stackoverflow.com/questions/33647614/how-to-configure-ctags-to-work-with-css-scss-html"
:!start explorer "https://www.georgevreilly.com/blog/2009/03/23/ExuberantCtagsAndJavaScript.html"
#}}}
#}}}
clean:                                                                         #{{{
	@(\
	    echo "[$@]";\
	    set -x;\
            du -s -k .;\
            rm -rf t.* */t.* ../tags tags */tags */*/tags */build/ */app/build */.gradle/*;\
            du -s -k .;\
	)

# }}}
arch:                                                                          #{{{
	@(\
	    echo "[$@ $(APROJECTS)]";\
            \
	    echo "cd APROJECTS  [$(APROJECTS)]";\
	    cd $(APROJECTS);\
            \
	    ARCHDIR="$(ANDROID_ARCHIVES)/$(APPDIR)";\
	    echo "ARCHDIR [$$ARCHDIR]";\
            \
	    FILES=`$(FIND) $(APPDIR) -type f\
	    | sed                       \
            -e "s/ /?/g"                \
            \
	    -e "/P\.txt/d"		\
	    -e "/tags/d"		\
	    \
	    -e "/\.font/d"		\
	    -e "/\.html/d"		\
	    -e "/\.orig/d"		\
	    -e "/\.ttf/d"		\
	    -e "/_zip/d"		\
            \
	    -e "/\.apk.*/d"		\
	    -e "/\.application/d"	\
	    -e "/\.bin/d"		\
	    -e "/\.dll/d"		\
	    -e "/\.err.*/d"		\
	    -e "/\.exe.*/d"		\
	    -e "/\.git.*/d"		\
	    -e "/\.idea.*/d"		\
	    -e "/\.lnk/d"		\
            \
	    -e "/\.zip/d"		\
	    -e "/\.jar/d"		\
	    -e "/\.rar/d"		\
            \
	    -e "/\/ARCHIVES/d"		\
	    -e "/\/BAK/d"		\
	    -e "/\/DISTRIBUTE/d"	\
	    -e "/\/GITHUB/d"	        \
	    -e "/\/LINKS/d"		\
	    -e "/\/LOCAL/d"		\
	    -e "/\/RTabsDoc/d"		\
	    -e "/\/TMP/d"	        \
	    -e "/\/bin/d"	        \
	    -e "/\/build/d"		\
	    -e "/\/bujild/d"		\
	    -e "/\/capture/d"		\
	    -e "/\/obj/d"	        \
	    `;\
            \
	    DATE=`date '+%y%m%d'`;\
	    ARCHIVE="$(APPDIR)_$$DATE.rar";\
#            rm -f                    $$ARCHDIR/$$ARCHIVE;\
	    : echo $(RAR)   a $(RAS) $$ARCHDIR/$$ARCHIVE $$FILES;\
#	    $(RAR)   a $(RAS)        $$ARCHDIR/$$ARCHIVE $$FILES;\
	    $(LS)         -lt        $$ARCHDIR/*.rar | head -2;\
#	    $(RAR)         l         $$ARCHDIR/$$ARCHIVE;\
	)

# }}}
# vim: noexpandtab ts=8 sw=4
