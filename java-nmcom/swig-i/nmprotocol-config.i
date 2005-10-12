/* java import statements */

%typemap(javaimports) SWIGTYPE %{
import nmcom.swig.nmpatch.*;
import nmcom.swig.pdl.*;
import nmcom.swig.ppf.*;
%}

%pragma(java) jniclassimports=%{
import nmcom.swig.nmpatch.*;
import nmcom.swig.pdl.*;
import nmcom.swig.ppf.*;
%}

%include "stl.i"           // includes std_string.i, std_vector.i, ...
%include "cjstring.i"      // std::string& -> stringbuffer
%include "make_public.i"
%include "swig_std_list.i" // std::list<T>
%include "exceptions.i"

/* Templates ----------------------------------------------------- */

namespace std {

// std::vector<unsigned char>
%template(UnsignedCharVector) vector<unsigned char>;

// std::list<string>
%template(StringList) list<std::string>;

// std::list<BitStream>;
specialize_std_list(BitStream);
%template(BitStreamList) list<BitStream>;

} // namespace std

/* Exception wrapper --------------------------------------------- */

PDLEXCEPTION(MidiMessage::usePDLFile);

MIDIPDLEXCEPTION(NMProtocol::heartbeat);

MIDIEXCEPTION(GetPatchListMessage::GetPatchListMessage);
MIDIEXCEPTION(GetPatchListMessage::notifyListener);
MIDIEXCEPTION(GetPatchMessage::GetPatchMessage);
MIDIEXCEPTION(NMProtocol::NMProtocol);
MIDIEXCEPTION(MidiDriver::createDriver);
MIDIEXCEPTION(MidiDriver::connect);
MIDIEXCEPTION(MidiDriver::disconnect);
MIDIEXCEPTION(MidiDriver::send);
MIDIEXCEPTION(MidiDriver::receive);
MIDIEXCEPTION(VoiceCountMessage::getBitStream);
MIDIEXCEPTION(MidiMessage::notifyListener);
MIDIEXCEPTION(MidiMessage::getBitStream);
MIDIEXCEPTION(LightMessage::getBitStream);
MIDIEXCEPTION(PatchListMessage::getBitStream);
MIDIEXCEPTION(NewPatchInSlotMessage::getBitStream);
MIDIEXCEPTION(PatchMessage::getPatch);
MIDIEXCEPTION(PatchMessage::storeEndPosition);
MIDIEXCEPTION(RequestPatchMessage::RequestPatchMessage);

/* derive MidiException from java.lang.Exeption */
%typemap(javabase) MidiException "java.lang.Exception";

/* Directors ----------------------------------------------------- */

// Link class Patch to package nmcom.swig.nmpatch
%typemap("javapackage") Patch, Patch *, Patch & "nmcom.swig.nmpatch";

/**
 * Next line is a workaround for a Bug (swig 1.3.25/13.09.2005)
 * see: http://mailman.cs.uchicago.edu/pipermail/swig/2005-September/012608.html
 *
 * %typemap(directorin,descriptor="L$packagepath/$&javaclassname;")
 *   SWIGTYPE "$input = 0; *(($&1_ltype*)(void *)&$input) = &$1;"
 */
%typemap(directorin,descriptor="L$packagepath/$&javaclassname;")
  SWIGTYPE "$input = 0; *(($&1_ltype*)(void *)&$input) = &$1;"

// enable directors for listener classes
/*%feature("director") NMProtocolListener;*/
%feature("director") NMProtocolListener;
%feature("director") SynthListener;
/* %feature("director") ActivePidListener;
%feature("nodirector") ActivePidListener::getActivePid; */

/* enabling directors for specific methods is not supported in swig,
   although the documentation says it is (10.9.2005)
// enable directors for listener classes
%feature("director") SynthListener::newPatchInSlot;
%feature("director") SynthListener::patchListChanged;
%feature("director") SynthListener::slotStateChanged;
%feature("director") ActivePidListener::messageReceived;
%feature("director") ActivePidListener::messageReceived;
*/
