jar cvmf Nomad.mf Nomad.jar main modules
rd Nomad /s /q Nomad\
md Nomad\
md Nomad\patches\
md Nomad\grafix\
xcopy /Y modules.properties Nomad\
xcopy /Y version.txt Nomad\
xcopy /Y Nomad.jar Nomad\
xcopy /Y Nomad.bat Nomad\
xcopy /y patches\*.* Nomad\patches\
xcopy /y grafix\*.* Nomad\grafix\
del Nomad\patches\*_.*
del Nomad\patches\korg.*
