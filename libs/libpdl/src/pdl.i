
%module pdl
%{
#include "pdl/bitstream.h"
#include "pdl/condition.h"
#include "pdl/matcher.h"
#include "pdl/constantmatcher.h"
#include "pdl/intstream.h"
#include "pdl/packet.h"
#include "pdl/packetmatcher.h"
#include "pdl/packetparser.h"
#include "pdl/parser.h"
#include "pdl/protocol.h"
#include "pdl/tracer.h"
#include "pdl/variablematcher.h"
#include "pdl/pdlexception.h"
%}

%include "../../java-nmcom/swig-i/pdl-config.i"

%include "pdl/bitstream.h"
%include "pdl/condition.h"
%include "pdl/matcher.h"
%include "pdl/constantmatcher.h"
%include "pdl/intstream.h"
%include "pdl/packet.h"
%include "pdl/packetmatcher.h"
%include "pdl/packetparser.h"
%include "pdl/parser.h"
%include "pdl/protocol.h"
%include "pdl/tracer.h"
%include "pdl/variablematcher.h"
%include "pdl/pdlexception.h"
