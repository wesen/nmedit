
INCLUDES = \
	-I.. \
	-I../../libs/libpdl \
	-I../../libs/libppf \
	-I../../libs/libnmpatch \
	-I../../libs/libnmprotocol \
	-I/usr/lib/wx/include/gtk-2.4 \
	`wxgtk-2.4-config --cppflags`

LDFLAGS = \
	-L../../libs/libpdl/src \
	-L../../libs/libppf/src \
	-L../../libs/libnmpatch/src \
	-L../../libs/libnmprotocol/src \
	`wxgtk-2.4-config --libs`


bin_PROGRAMS = nmedit

nmedit_SOURCES = nmapp.cc nmframe.cc synth.cc
nmedit_LDADD =-lpdl -lnmprotocol -lnmpatch -lppf -ltcl8.4 -lwx_gtk-2.4
