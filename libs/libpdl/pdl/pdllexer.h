/*
    Nord Modular patch file format 3.03 parser
    Copyright (C) 2002 Marcus Andersson

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/

#ifndef PDLLEXER_H
#define PDLLEXER_H

#include <string>
#include <iostream>
#include <stdio.h>

#define YYSTYPE string
#define YYSTYPE_IS_TRIVIAL 1

#if ! defined(yyFlexLexer)
#define yyFlexLexer pdlFlexLexer
#include <FlexLexer.h>
#endif

class PDLLexer : public pdlFlexLexer {
public:
	PDLLexer( std::istream* arg_yyin = 0, std::ostream* arg_yyout = 0 );
protected:
};

#endif
