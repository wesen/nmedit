/* MidiException */

%define MIDIEXCEPTION(METHOD)
%javaexception("nmcom.swig.nmprotocol.MidiException") METHOD %{
  try {
     $action
  } catch (MidiException &e) {
    jclass clazz = jenv->FindClass("nmcom/swig/nmprotocol/MidiException");
    if (clazz!=0) {
      jmethodID mid = jenv->GetMethodID(clazz, "<init>", "(Ljava/lang/String;I)V");
      if (mid!=0) {
        jstring jerrMsg = jenv->NewStringUTF(e.getMessage().data());
        jint jerrCode = e.getError();

        jthrowable e_wrap = (jthrowable) jenv->NewObject(clazz, mid, jerrMsg, jerrCode);
        if (e_wrap!=0)
        {
          jenv->Throw(e_wrap);
        }
        else SWIG_exception(SWIG_RuntimeError, "Can't create object for nmprotocol::MidiException.");
      }
      else SWIG_exception(SWIG_RuntimeError, "Can't find constructor for nmprotocol::MidiException.");
    }
    else SWIG_exception(SWIG_RuntimeError, "Can't find class nmprotocol::MidiException.");
  }
%}
%enddef

/* PDLException */

%define PDLEXCEPTION(METHOD)
%javaexception("nmcom.swig.pdl.PDLException") METHOD %{
  try {
     $action
  } catch (PDLException &e) {
    jclass clazz = jenv->FindClass("nmcom/swig/pdl/PDLException");
    if (clazz!=0) {
      jmethodID mid = jenv->GetMethodID(clazz, "<init>", "(Ljava/lang/String;I)V");
      if (mid!=0) {
        jstring jerrMsg = jenv->NewStringUTF(e.getMessage().data());
        jint jerrCode = e.getError();

        jthrowable e_wrap = (jthrowable) jenv->NewObject(clazz, mid, jerrMsg, jerrCode);
        if (e_wrap!=0)
        {
          jenv->Throw(e_wrap);
        }
        else SWIG_exception(SWIG_RuntimeError, "Can't create object for pdl::PDLException.");
      }
      else SWIG_exception(SWIG_RuntimeError, "Can't find constructor for pdl::PDLException.");
    }
    else SWIG_exception(SWIG_RuntimeError, "Can't find class pdl::PDLException.");
  }
%}
%enddef

/* PDLException and MidiException */

%define MIDIPDLEXCEPTION(METHOD)
%javaexception("nmcom.swig.nmprotocol.MidiException, nmcom.swig.pdl.PDLException") METHOD %{
  try {
     $action
  } catch (MidiException &e) {
    jclass clazz = jenv->FindClass("nmcom/swig/nmprotocol/MidiException");
    if (clazz!=0) {
      jmethodID mid = jenv->GetMethodID(clazz, "<init>", "(Ljava/lang/String;I)V");
      if (mid!=0) {
        jstring jerrMsg = jenv->NewStringUTF(e.getMessage().data());
        jint jerrCode = e.getError();

        jthrowable e_wrap = (jthrowable) jenv->NewObject(clazz, mid, jerrMsg, jerrCode);
        if (e_wrap!=0)
        {
          jenv->Throw(e_wrap);
        }
        else SWIG_exception(SWIG_RuntimeError, "Can't create object for nmprotocol::MidiException.");
      }
      else SWIG_exception(SWIG_RuntimeError, "Can't find constructor for nmprotocol::MidiException.");
    }
    else SWIG_exception(SWIG_RuntimeError, "Can't find class nmprotocol::MidiException.");
  } catch (PDLException &e) {
    jclass clazz = jenv->FindClass("nmcom/swig/pdl/PDLException");
    if (clazz!=0) {
      jmethodID mid = jenv->GetMethodID(clazz, "<init>", "(Ljava/lang/String;I)V");
      if (mid!=0) {
        jstring jerrMsg = jenv->NewStringUTF(e.getMessage().data());
        jint jerrCode = e.getError();

        jthrowable e_wrap = (jthrowable) jenv->NewObject(clazz, mid, jerrMsg, jerrCode);
        if (e_wrap!=0)
        {
          jenv->Throw(e_wrap);
        }
        else SWIG_exception(SWIG_RuntimeError, "Can't create object for pdl::PDLException.");
      }
      else SWIG_exception(SWIG_RuntimeError, "Can't find constructor for pdl::PDLException.");
    }
    else SWIG_exception(SWIG_RuntimeError, "Can't find class pdl::PDLException.");
  }

%}
%enddef

/* PatchException */

%define PATCHEXCEPTION(METHOD)
%javaexception("nmcom.swig.nmpatch.PatchException") METHOD %{
  try {
     $action
  } catch (PatchException &e) {
    jclass clazz = jenv->FindClass("nmcom/swig/nmpatch/PatchException");
    if (clazz!=0) {
      jmethodID mid = jenv->GetMethodID(clazz, "<init>", "(Ljava/lang/String;I)V");
      if (mid!=0) {
        jstring jerrMsg = jenv->NewStringUTF(e.getMessage().data());
        jint jerrCode = e.getError();

        jthrowable e_wrap = (jthrowable) jenv->NewObject(clazz, mid, jerrMsg, jerrCode);
        if (e_wrap!=0)
        {
          jenv->Throw(e_wrap);
        }
        else SWIG_exception(SWIG_RuntimeError, "Can't create object for nmpatch::PatchException.");
      }
      else SWIG_exception(SWIG_RuntimeError, "Can't find constructor for nmpatch::PatchException.");
    }
    else SWIG_exception(SWIG_RuntimeError, "Can't find class nmpatch::PatchException.");
  }
%}
%enddef

/* PPFException */

%define PPFEXCEPTION(METHOD)
%javaexception("nmcom.swig.ppf.PPFException") METHOD %{
  try {
     $action
  } catch (ppf::PPFException &e) {
    jclass clazz = jenv->FindClass("nmcom/swig/pdl/PPFException");
    if (clazz!=0) {
      jmethodID mid = jenv->GetMethodID(clazz, "<init>", "(Ljava/lang/String;I)V");
      if (mid!=0) {
        jstring jerrMsg = jenv->NewStringUTF(e.getMessage().data());
        jint jerrCode = e.getError();

        jthrowable e_wrap = (jthrowable) jenv->NewObject(clazz, mid, jerrMsg, jerrCode);
        if (e_wrap!=0)
        {
          jenv->Throw(e_wrap);
        }
        else SWIG_exception(SWIG_RuntimeError, "Can't create object for ppf::PPFException.");
      }
      else SWIG_exception(SWIG_RuntimeError, "Can't find constructor for ppf::MidiException.");
    }
    else SWIG_exception(SWIG_RuntimeError, "Can't find class ppf::PPFException.");
  }
%}
%enddef

