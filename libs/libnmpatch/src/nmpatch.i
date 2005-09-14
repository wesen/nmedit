
%module(directors="1") nmpatch
%{
#include "nmpatch/cable.h"
#include "nmpatch/ctrlmap.h"
#include "nmpatch/knobmap.h"
#include "nmpatch/module.h"
#include "nmpatch/modulesection.h"
#include "nmpatch/morph.h"
#include "nmpatch/morphmap.h"
#include "nmpatch/note.h"
#include "nmpatch/patch.h"
#include "nmpatch/patchexception.h"
#include "nmpatch/moduletype.h"
#include "nmpatch/modulelistener.h"
#include "nmpatch/modulesectionlistener.h"
%}

%include "../../java-nmcom/swig-i/nmpatch-config.i"

%include "nmpatch/cable.h"
%include "nmpatch/ctrlmap.h"
%include "nmpatch/knobmap.h"
%include "nmpatch/module.h"
%include "nmpatch/modulesection.h"
%include "nmpatch/morph.h"
%include "nmpatch/morphmap.h"
%include "nmpatch/note.h"
%include "nmpatch/patch.h"
%include "nmpatch/patchexception.h"
%include "nmpatch/moduletype.h"
%include "nmpatch/modulelistener.h"
%include "nmpatch/modulesectionlistener.h"

%import  "../libppf/ppf/boundbundle.h"
