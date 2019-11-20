AC_INIT()
AC_CANONICAL_SYSTEM

AC_PROG_CC
AC_PROG_CXX

dnl Checks for programs.
AC_PROG_AWK
AM_PROG_LEX
AC_PROG_YACC
AC_PROG_INSTALL
AC_PROG_LN_S
AC_PROG_MAKE_SET

dnl Checks for libraries.
AC_PROG_LIBTOOL

dnl Checks for header files.
AC_HEADER_STDC

dnl Checks for typedefs, structures, and compiler characteristics.
AC_C_CONST

dnl Checks for library functions.


AM_INIT_AUTOMAKE(pdl, 0.1)

dnl Checks for libraries.
dnl Replace `main' with a function in -lfl:
AC_CHECK_LIB(fl, main)

dnl Replace `main' with a function in -lnmprotocol:
dnl AC_CHECK_LIB(nmprotocol, main)

dnl Replace `main' with a function in -lpdl:
dnl AC_CHECK_LIB(pdl, main)


dnl Checks for header files.
AC_HEADER_STDC
AC_CHECK_HEADERS(unistd.h)

dnl Checks for typedefs, structures, and compiler characteristics.
AC_C_CONST

dnl Checks for library functions.

AC_OUTPUT(Makefile src/Makefile pdl/Makefile test/Makefile doc/Makefile)
