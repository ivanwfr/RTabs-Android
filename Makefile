###############################################################################
### Makefile_TAG (200731:16h:58) ### ANDROID (GITHUB) #########################
###############################################################################

### ANDROID:
### ✔✔✔ $APROJECTS/GITHUB/Makefile
###     $APROJECTS/Makefile

### DESKTOP:
###     $WPROJECTS/GITHUB/Makefile
###     $WPROJECTS/Makefile

ORIGIN = https://github.com/ivanwfr/RTabs-Android 
include Make_GIT

:cd %:h|silent! make view_project_on_GITHUB
view_project_on_GITHUB: #{{{
	explorer $(ORIGIN)

#}}}

:cd %:h|up|only|set columns=999|vert terminal ++cols=150 make links
links: #{{{
	(\
	    jar xvf RTabs_*.zip;\
	    )
	#}}}

:cd %:h|up|only|set columns=999|vert terminal ++cols=150 make clean
clean:                                                                         #{{{
	cd RTabs && make $@

# }}}

###############################################################################

# vim: noet ts=8 sw=8
