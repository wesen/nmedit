
INCLUDES = -I..

lib_LTLIBRARIES = libpdl.la

libpdl_la_SOURCES = pdlparser.yy pdllex.ll pdllexer.cc bitstream.cc condition.cc constantmatcher.cc intstream.cc matcher.cc packet.cc packetmatcher.cc packetparser.cc protocol.cc tracer.cc variablematcher.cc

libpdl_la_INCLUDES = pdl/bitstream.h pdl/condition.h pdl/constantmatcher.h pdl/intstream.h pdl/matcher.h pdl/packet.h pdl/packetmatcher.h pdl/packetparser.h pdl/parser.h pdl/protocol.h pdl/tracer.h pdl/variablematcher.h

libpdl_la_LDFLAGS = -release 1 -no-undefined -static

YFLAGS = -d -ppdl
LFLAGS = -+ -olex.yy.c -Ppdl

clean:
	rm -f pdllex.cc pdlparser.cc pdlparser.h
