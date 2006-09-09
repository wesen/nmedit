/* Copyright (C) 2006 Christian Schneider
 * 
 * This file is part of Nomad.
 * 
 * Nomad is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * Nomad is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Nomad; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

/*
 * Created on Sep 8, 2006
 */
package net.sf.nmedit.jtheme.types;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;

import net.sf.nmedit.jtheme.PropertyUtilities;

public abstract class ValueType<T>
{

    private Class<? super T> clazzType;

    public ValueType(Class<? super T> clazzType)
    {
        this.clazzType = clazzType;
    }
    
    public Class<? super T> getClassType()
    {
        return clazzType;
    }
    
    public abstract T fromString(String representation);
    
    public abstract String toString(T value);

    public static class IntegerType extends ValueType<Integer> 
    {
        public IntegerType( )
        {   super( Integer.class ); }
        @Override
        public Integer fromString( String representation )
        {   return Integer.getInteger(representation); }
        @Override
        public String toString( Integer value )
        {   return Integer.toString(value); }   
    }

    public static class IntegerTypeType extends ValueType<Integer> 
    {
        public IntegerTypeType( )
        {   super( Integer.TYPE ); }
        public Integer fromString( String representation )
        {   return Integer.parseInt(representation); }
        public String toString( Integer value )
        {   return Integer.toString(value); }
    }

    public static class LongType extends ValueType<Long> 
    {
        public LongType( )
        {   super( Long.class ); }
        @Override
        public Long fromString( String representation )
        {   return Long.getLong(representation); }
        @Override
        public String toString( Long value )
        {   return Long.toString(value); }   
    }

    public static class FontType extends ValueType<Font> 
    {
        public FontType( )
        {   super( Font.class ); }
        @Override
        public Font fromString( String representation )
        {   return PropertyUtilities.decodeFont(representation); }
        @Override
        public String toString( Font value )
        {   return PropertyUtilities.encodeFont(value); }   
    }

    public static class DoubleType extends ValueType<Double> 
    {
        public DoubleType( )
        {   super( Double.class ); }
        @Override
        public Double fromString( String representation )
        {   return Double.parseDouble(representation); }
        @Override
        public String toString( Double value )
        {   return Double.toString(value); }   
    }
    
    public static class BooleanType extends ValueType<Boolean> 
    {
        public BooleanType( )
        { super( Boolean.class ); }

        @Override
        public Boolean fromString( String representation )
        { return Boolean.valueOf(representation); }
        
        @Override
        public String toString( Boolean value )
        { return Boolean.toString(value); }
    }
    
    public static class PointType extends ValueType<Point> 
    {
        public PointType( )
        { super( Point.class ); }

        @Override
        public Point fromString( String representation )
        {
            Dimension d =  DimensionType.strToDimension(representation);
            return new Point(d.width, d.height);
        }
        
        @Override
        public String toString( Point value )
        { return value.x+","+value.y; }
    }

    public static class DimensionType extends ValueType<Dimension> 
    {
        public DimensionType( )
        {   super( Dimension.class ); }

        @Override
        public Dimension fromString( String representation )
        { return strToDimension(representation); }
        public static Dimension strToDimension( String representation )
        {
            int w=0;
            int h=0;
            Dimension d = new Dimension(); // width = position, height = value

            d.width=skipws(representation, 0);
            number(representation, d);
            w = d.height;
            d.width=skipws(representation, d.width);
            
            if (d.width>=representation.length())
                throw new IllegalArgumentException("expected ','");
            d.width ++;
            d.width=skipws(representation, d.width);
            number(representation, d);
            h = d.height;
            d.width=skipws(representation, d.width);
            if (d.width<representation.length())
                throw new IllegalArgumentException("invalid character: '"+representation.charAt(d.width)+"'");
            
            d.width = w;
            d.height = h;
            return d;
        }
        
        static void number(String s, Dimension temp)
        {
            int pos = temp.width;
            
            if (pos>=s.length())
                throw new IllegalArgumentException("expected (-?[0-9])");

            boolean neg = false;
            if (s.charAt(pos)=='-')
            {
                neg = true;
                pos ++;
            }
            
            int value = 0;
            
            int digitpos=pos;
            char c;
            while (digitpos<s.length()&&Character.isDigit(c=s.charAt(digitpos)))
            {
                value= (value*10) +(c-'0');
                digitpos ++;
            }
            if (!(digitpos>pos))
                throw new IllegalArgumentException("expected (-?[0-9])");

            temp.width = digitpos;
            temp.height = neg ? -value : value;
        }
        
        static int skipws(String s, int pos)
        {
            while (pos<s.length() && Character.isWhitespace(s.charAt(pos)))
                pos ++;
            return pos;
        }
        
        @Override
        public String toString( Dimension value )
        {   return value.width+","+value.height; }   
    }

    public static class StringType extends ValueType<String>
    {
        public StringType()
        { super( String.class ); }
        @Override
        public String fromString( String representation )
        { return representation; }
        @Override
        public String toString( String value )
        { return value; }
    }

}
