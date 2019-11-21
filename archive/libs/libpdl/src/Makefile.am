
INCLUDES = -I..

lib_LTLIBRARIES = libpdl.la

libpdl_la_SOURCES = pdlparser.yy pdllex.ll pdllexer.cc bitstream.cc condition.cc constantmatcher.cc intstream.cc matcher.cc packet.cc packetmatcher.cc packetparser.cc protocol.cc tracer.cc variablematcher.cc

libpdl_la_LDFLAGS = -release 1 -no-undefined -static

AM_YFLAGS = -d -ppdl
AM_LFLAGS = -+ -olex.yy.c -Ppdl

clean:
	rm -f pdllex.cc pdlparser.cc pdlparser.h
