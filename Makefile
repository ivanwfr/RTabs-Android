###############################################################################
### https://github.com/ivanwfr/RTabs-Android ##### Makefile_TAG (200722:18h:35)
###############################################################################
# VARS {{{
 ORIGIN = https://github.com/ivanwfr/RTabs-Android 
    JAR = jar.exe
#}}}

include Make_GIT

:cd %:h|make view_project
view_project: #{{{
	explorer $(ORIGIN)

#}}}

:cd %:h|up|only|set columns=999|vert terminal ++cols=150 make links
links: #{{{
	(\
	    $(JAR) xvf RTabs_*.zip;\
	    )
	#}}}

###############################################################################

# vim: noet ts=8 sw=8
