/* java import statements */

%typemap(javaimports) SWIGTYPE %{
import nmcom.swig.ppf.*;
%}
%pragma(java) jniclassimports=%{
import nmcom.swig.ppf.*;
%}

%include "stl.i" // includes std_string.i, std_vector.i, ...
%include "cjstring.i" // std::string& -> stringbuffer
%include "make_public.i"
%include "swig_std_list.i" // std::list<T>
%include "exceptions.i"

/* Templates ----------------------------------------------------- */

namespace std {

// std::list<Cable*>;
specialize_std_list(Cable*);
%template(CableList) list<Cable*>;

// std::list<CtrlMap*>;
specialize_std_list(CtrlMap*);
%template(CtrlMapList) list<CtrlMap*>;

// std::list<Module*>;
specialize_std_list(Module*);
%template(ModuleList) list<Module*>;

// std::list<KnobMap*>;
specialize_std_list(KnobMap*);
%template(KnobMapList) list<KnobMap*>;

// std::list<MorphMap*>;
specialize_std_list(MorphMap*);
%template(MorphMapList) list<MorphMap*>;

// std::list<Note*>;
specialize_std_list(Note*);
%template(NoteList) list<Note*>;

} // namespace std

/* Exception wrapper --------------------------------------------- */

/* derive PatchException from java.lang.Exeption */
%typemap(javabase) PatchException "java.lang.Exception"

PATCHEXCEPTION(Patch::Patch);
PPFEXCEPTION(ModuleSection::usePPFFile);

/* Directors ----------------------------------------------------- */

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
%feature("director") ModuleListener;
%feature("director") ModuleSectionListener;
