
INCLUDES = \
	-I.. \
	-I../../libs/libpdl \
	-I../../libs/libppf \
	-I../../libs/libnmpatch \
	-I../../libs/libnmprotocol \
	-I/usr/lib/wx/include/gtk-2.4 \
	`fltk-config --cxxflags`

LDFLAGS = \
	-L../../libs/libpdl/src \
	-L../../libs/libppf/src \
	-L../../libs/libnmpatch/src \
	-L../../libs/libnmprotocol/src \
	`fltk-config --ldflags`


bin_PROGRAMS = nmedit

nmedit_SOURCES = main.cc synth.cc mainwindow.cc synthview.cc sectionview.cc moduleview.cc
nmedit_LDADD =-lpdl -lnmprotocol -lnmpatch -lppf -ltcl8.4
