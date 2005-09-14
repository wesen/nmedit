
%include "stl.i" // includes std_string.i, std_vector.i, ...
%include "cjstring.i" // std::string& -> stringbuffer
%include "make_public.i"
%include "exceptions.i"

/* Exception wrapper --------------------------------------------- */

PPFEXCEPTION(ppf::Parser::Parser);
PPFEXCEPTION(ppf::Parser::parse);
PPFEXCEPTION(ppf::BoundBundle::getBoundBundle);
PPFEXCEPTION(ppf::BoundBundle::getProperty);
/* PPFEXCEPTION(Bundle::getBundle);
PPFEXCEPTION(Bundle::getProperty); */

/* derive PPFException from java.lang.Exeption */
%typemap(javabase) ppf::PPFException "java.lang.Exception";

/* --------------------------------------------------------------- */

// Ignore package-private Konstruktur
// => no interface for class bundle will be created
%feature("ignore") ppf::BoundBundle::BoundBundle(Bundle*, int, string);
