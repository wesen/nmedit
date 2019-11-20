jar cvmf jMod.mf jMod.jar main modules
rd jMod /s /q jMod\
md jMod\
md jMod\patches\
md jMod\grafix\
xcopy /Y modules.properties jMod\
xcopy /Y version.txt jMod\
xcopy /Y jMod.jar jMod\
xcopy /Y jMod.bat jMod\
xcopy /y patches\*.* jMod\patches\
xcopy /y grafix\*.* jMod\grafix\
del jMod\patches\*_.*
del jMod\patches\korg.*
