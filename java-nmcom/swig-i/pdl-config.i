%include "stl.i" // includes std_string.i, std_vector.i, ...
%include "cjstring.i" // std::string& -> stringbuffer
%include "make_public.i"
%include "swig_std_list.i" // std::list<T>
%include "exceptions.i"

/* Templates ----------------------------------------------------- */

namespace std {

// std::list<int>;
%template(VariableList) list<int>;

// std::list<Packet*>;
specialize_std_list(Packet*);
%template(PacketList) list<Packet*>;

} // namespace std

/* Exception wrapper --------------------------------------------- */

/* derive PDLException from java.lang.Exeption */
%typemap(javabase) PDLException "java.lang.Exception";

PDLEXCEPTION(Protocol::Protocol);
