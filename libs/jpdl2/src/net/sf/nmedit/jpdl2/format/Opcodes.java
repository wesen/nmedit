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
 * Defines constants for each operator.
 */
public interface Opcodes
{

    // "no args"
    /**
     * push integer constant.
     */
    public static final int ipush = 0;
    /**
     * push boolean constant.
     */
    public static final int bpush = 1;
    /**
     * push label value.
     */
    public static final int lpush = 2;
    /**
     * push variable value.
     */
    public static final int vpush = 3;
    /**
     * push field value (stream operator).
     */
    public static final int fpush = 4;
    
    // unary
    
    /**
     * unary minus '-'.
     */
    public static final int ineg = 5;
    
    /**
     * boolean (logical) NOT '!'
     */
    public static final int binv = 6;

    /**
     * bitwise NOT '~'
     */
    public static final int iinv = 7;
    
    /**
     * typecast int-to-boolean
     */
    public static final int i2b  = 8;

    /**
     * typecast boolean-to-int
     */
    public static final int b2i  = 9; 
    ;
    
    // integer operators
    
    // multiplicative
    /**
     * integer multiplication
     */
    public static final int imul = 10;
    
    /**
     * integer division
     */
    public static final int idiv = 11;
    
    /**
     * integer remainder
     */
    public static final int imod = 12;
    // additive
    
    /**
     * integer sum
     */
    public static final int iadd = 13;
    
    /**
     * integer minus
     */
    public static final int isub = 14;
    // shift
    
    /**
     * signed bitshift left
     */
    public static final int ishl = 15;

    /**
     * signed bitshift right
     */
    public static final int ishr = 16; 

    /**
     * unsigned bitshift right
     */
    public static final int iushr = 17;
    
    // relational
    /**
     * compare integer: less than
     */
    public static final int ilt = 18;

    /**
     * compare integer: greater than
     */
    public static final int igt = 19;

    /**
     * compare integer: less than or equals 
     */
    public static final int ileq = 20;

    /**
     * compare integer: greater than or equals 
     */
    public static final int igeq = 21;
    
    // equality
    
    /**
     * compare integer: equals
     */
    public static final int ieq = 22;
    
    /**
     * compare integer: not equals
     */
    public static final int ineq = 23;

    /**
     * compare boolean: equals
     */
    public static final int beq = 24;

    /**
     * compare boolean: not equals
     */
    public static final int bneq = 25;

    /**
     * bitwise and
     */
    public static final int iand = 26;

    /**
     * bitwise xor
     */
    public static final int ixor = 27;

    /**
     * bitwise or
     */
    public static final int ior = 28;

    /**
     * boolean (logical) and
     */
    public static final int band = 29;

    /**
     * boolean (logical) or
     */
    public static final int bor = 30;

    /**
     * boolean (logical) xor
     */
    public static final int bxor = 31;
    
    /**
     * Stream operator '+'.
     * stream expression [[+*^|&]; &lt;start&gt;; &lt;end&gt;; &lt;size&gt;; $]
     */
    public static final int ladd = 32;

    /**
     * Stream operator '*'.
     * stream expression [[+*^|&]; &lt;start&gt;; &lt;end&gt;; &lt;size&gt;; $]
     */
    public static final int lmul = 33;

    /**
     * Stream operator '&'.
     * stream expression [[+*^|&]; &lt;start&gt;; &lt;end&gt;; &lt;size&gt;; $]
     */
    public static final int land = 34;

    /**
     * Stream operator '^'.
     * stream expression [[+*^|&]; &lt;start&gt;; &lt;end&gt;; &lt;size&gt;; $]
     */
    public static final int lxor = 35;

    /**
     * Stream operator '|'.
     * stream expression [[+*^|&]; &lt;start&gt;; &lt;end&gt;; &lt;size&gt;; $]
     */
    public static final int lor  = 36;
}
