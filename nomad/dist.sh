echo Cleaning Up ------------------------------
ant uninstall-libraries
ant clean-libraries
ant clean  

echo Building/Installing libraries ------------
ant build-libraries
ant install-libraries

echo Building Nomad ----------------------------
ant
# removing editor
rm ant.dist/nomad/editor.jar

echo Cleaning libraries --------------------
ant uninstall-libraries
ant clean-libraries

echo Finished...
