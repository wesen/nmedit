CXXFLAGS = -DALSA
INCLUDES = -I.. -I../../libpdl -I../../libnmpatch


lib_LTLIBRARIES = libnmprotocol.la

libnmprotocol_la_SOURCES = alsadriver.cc iammessage.cc lightmessage.cc mididriver.cc midimessage.cc nmprotocol.cc patchmessage.cc ackmessage.cc requestpatchmessage.cc getpatchlistmessage.cc patchlistmessage.cc newpatchinslotmessage.cc voicecountmessage.cc getpatchmessage.cc

libnmprotocol_la_LIBADD = -L../../libpdl/src -lpdl
libnmprotocol_la_LDFLAGS = -release 1 -no-undefined -static
