
INCLUDES = -I..

lib_LTLIBRARIES = libnmpatch.la

libnmpatch_la_SOURCES = patchparser.yy patchlexer.ll nmlexer.cc cable.cc ctrlmap.cc knobmap.cc module.cc modulesection.cc morph.cc morphmap.cc note.cc patch.cc 

libnmpatch_la_INCLUDES = nmpatch/cable.h nmpatch/ctrlmap.h nmpatch/knobmap.h nmpatch/module.h nmpatch/moduleseciton.h nmpatch/morph.h nmpatch/morphmap.h nmpatch/note.h nmpatch/parser.h nmpatch/patch.h nmpatch/nmlexer.h

libnmpatch_la_LDFLAGS = -release 1 -no-undefined -static

YFLAGS = -d -pnm
LFLAGS = -+ -olex.yy.c -Pnm

clean:
	rm -f patchlexer.cc patchparser.cc patchparser.h
