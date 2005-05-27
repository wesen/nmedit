#!/bin/sh

LIBS="libs/libppf libs/libpdl libs/libnmpatch libs/libnmprotocol patchloader nmedit"
echo $LIBS
for d in ${LIBS}; do
	pushd $d
	scons $@
	popd
done

