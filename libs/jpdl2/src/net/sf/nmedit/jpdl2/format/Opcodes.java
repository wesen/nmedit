/*
    Protocol Definition Language
    Copyright (C) 2003-2006 Marcus Andersson

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
package net.sf.nmedit.jpdl2.format;

/**
 * @see http://java.sun.com/docs/books/tutorial/java/nutsandbolts/operators.html
 */
public interface Opcodes
{

    // "no args"
    public static final int
    ipush = 0, // push integer constant
    bpush = 1, // push boolean
    lpush = 2, // push label
    vpush = 3, // push variable
    fpush = 4  // push field in stream
    ;
    
    // unary
    public static final int
    ineg = 5, // - (int)
    binv = 6, // ! (boolean)
    iinv = 7, // ~ (int)
    i2b  = 8, // (int) boolean
    b2i  = 9 // (boolean) int 
    ;
    
    // binary
    public static final int
    // multiplicative
    imul = 10,
    idiv = 11,
    imod = 12,
    // additive
    iadd = 13,
    isub = 14,
    // shift
    ishl = 15, // signed shift left
    ishr = 16, // shift right
    iushr = 17, // unsigned shift right
    // relational
    ilt = 18,
    igt = 19,
    ileq = 20,
    igeq = 21,
    // equality
    ieq = 22,
    ineq = 23,
    beq = 24,
    bneq = 25,
    // bitwise AND
    iand = 26,
    // bitwise xor
    ixor = 27,
    // bitwise or
    ior = 28,
    // logical and
    band = 29,
    // logical or
    bor = 30,
    // logical xor
    bxor = 31
    ;
    
    // list expression [[+*^|&], <start>, <end>, <size>, $]
    public static final int
    ladd = 32,
    lmul = 33,
    land = 34,
    lxor = 35,
    lor  = 36
    ;
}
