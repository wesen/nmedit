
INCLUDES = \
	-I.. \
	-I../../libs/libpdl \
	-I../../libs/libppf \
	-I../../libs/libnmpatch \
	-I../../libs/libnmprotocol

LDFLAGS = \
	-L../../libs/libpdl/src \
	-L../../libs/libppf/src \
	-L../../libs/libnmpatch/src \
	-L../../libs/libnmprotocol/src


bin_PROGRAMS = patchloader

patchloader_SOURCES = patchloader.cc
patchloader_LDADD =-lpdl -lnmprotocol -lnmpatch -lppf -ltcl8.4
