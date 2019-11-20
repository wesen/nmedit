AC_INIT()
AC_CANONICAL_SYSTEM

AC_PROG_CC
AC_PROG_CXX

dnl Checks for programs.
AC_PROG_AWK
AC_PROG_INSTALL
AC_PROG_LN_S
AC_PROG_MAKE_SET


dnl Checks for header files.
AC_HEADER_STDC

dnl Checks for typedefs, structures, and compiler characteristics.
AC_C_CONST

dnl Checks for library functions.

AM_INIT_AUTOMAKE(patchloader, 1.0)

dnl Checks for libraries.
dnl Replace `main' with a function in -lfl:
AC_CHECK_LIB(pdl, main)
AC_CHECK_LIB(nmpatch, main)
AC_CHECK_LIB(nmprotocol, main)


dnl Checks for header files.
AC_HEADER_STDC
AC_CHECK_HEADERS(unistd.h)

dnl Checks for typedefs, structures, and compiler characteristics.
AC_C_CONST

dnl Checks for library functions.

AC_OUTPUT(Makefile src/Makefile)
