/* Copyright (C) 2008 Christian Schneider
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
package net.sf.nmedit.nmutils.properties.type;

public abstract class Property<E>
{

    private java.lang.String name;
    private E defaultValue ;
    private E value;

    public Property(E defaultValue)
    {
        this.value = defaultValue;
        this.defaultValue = defaultValue;
    }
    
    public E getValue()
    {
        return value;
    }
    
    public void setValue(E value)
    {
        this.value = value;
    }
    
    public E getDefaultValue()
    {
        return defaultValue;
    }
    
    public abstract Class<E> getType();
    
    public java.lang.String getTypeName()
    {
        return getType().getSimpleName();
    }
    
    public java.lang.String getValueString()
    {
        return toString(value);
    }
    
    public java.lang.String getDefaultValueString()
    {
        return toString(defaultValue);
    }

    // returns null if the object is null
    public abstract java.lang.String toString(E value) throws IllegalArgumentException;
    public abstract E parseString(java.lang.String stringValue) throws IllegalArgumentException;
    
    public boolean equals(Object o)
    {
        if (o == null) return false;
        try
        {
            Property p = (Property) o;
            return p.name.equals(name);
        }
        catch (ClassCastException e)
        {
            return false;
        }
    }

    public int hashCode()
    {
        return name.hashCode();
    }
    
    public static java.lang.String intListToString(int ... values)
    {
        if (values == null)
            return null;
        StringBuilder sb = new StringBuilder(values.length*2-1);
        java.lang.String stringValue = null;
        if (values.length > 0)
        {
            sb.append(java.lang.String.valueOf(values[0]));
            for (int i=1;i<values.length;i++)
            {
                sb.append(',');
                sb.append(java.lang.String.valueOf(values[i]));
            }
            stringValue = sb.toString();
        }
        return stringValue;
    }

    public static int[] parseIntList(java.lang.String stringValue, int count)
    {
        if (stringValue == null) return null;
        java.lang.String[] pieces = stringValue.split(",");
        
        if (pieces.length!=count && count>=0)
            return null;
        
        int[] values = new int[pieces.length];

        try
        {
            for (int i=0;i<pieces.length;i++)
            {
                values[i] = Integer.parseInt(pieces[i].trim());
            }
        }
        catch (NumberFormatException e)
        {
            return null;
        }
        
        return values;
    }

    public static class String extends Property<java.lang.String>
    {
        public String(java.lang.String defaultValue)
        {
            super(defaultValue);
        }
        
        public Class<java.lang.String> getType()
        {
            return java.lang.String.class;
        }

        @Override
        public java.lang.String parseString(java.lang.String stringValue)
                throws IllegalArgumentException
        {
            return stringValue;
        }

        @Override
        public java.lang.String toString(java.lang.String value) throws IllegalArgumentException
        {
            return value;
        }
    }
    
    public static class Rectangle extends Property<java.awt.Rectangle>
    {
        public Rectangle(java.awt.Rectangle defaultValue)
        {
            super(defaultValue);
        }

        @Override
        public java.awt.Rectangle parseString(java.lang.String stringValue)
                throws IllegalArgumentException
        {
            if (stringValue == null)
                return null;
            int[] values = parseIntList(stringValue, 4);
            if (values == null)
                throw new IllegalArgumentException("invalid argument:"+stringValue);
            return new java.awt.Rectangle(values[0], values[1], values[2], values[3]);
        }

        @Override
        public java.lang.String toString(java.awt.Rectangle value) throws IllegalArgumentException
        {
            return value == null ? null : intListToString(value.x, value.y, value.width, value.height);
        }

        @Override
        public Class<java.awt.Rectangle> getType()
        {
            return java.awt.Rectangle.class;
        }
    }
    
    public static class Boolean extends Property<java.lang.Boolean>
    {
        public Boolean(java.lang.Boolean defaultValue)
        {
            super(defaultValue);
        }

        @Override
        public java.lang.Boolean parseString(java.lang.String stringValue)
                throws IllegalArgumentException
        {
            if (stringValue == null)
                return null;
            try
            {
                return java.lang.Boolean.parseBoolean(stringValue);
            }
            catch (NumberFormatException e)
            {
                throw new IllegalArgumentException(e);
            }
        }

        @Override
        public java.lang.String toString(java.lang.Boolean value) throws IllegalArgumentException
        {
            return value == null ? null : java.lang.String.valueOf(value);
        }
        @Override
        public Class<java.lang.Boolean> getType()
        {
            return java.lang.Boolean.class;
        }
    }
    
    public static class Dimension extends Property<java.awt.Dimension>
    {
        public Dimension(java.awt.Dimension defaultValue)
        {
            super(defaultValue);
        }

        @Override
        public java.awt.Dimension parseString(java.lang.String stringValue)
                throws IllegalArgumentException
        {
            if (stringValue == null)
                return null;
            int[] values = parseIntList(stringValue, 2);
            if (values == null)
                throw new IllegalArgumentException("invalid argument:"+stringValue);
            return new java.awt.Dimension(values[0], values[1]);
        }

        @Override
        public java.lang.String toString(java.awt.Dimension value) throws IllegalArgumentException
        {
            return value == null ? null : intListToString(value.width, value.height);
        }
        @Override
        public Class<java.awt.Dimension> getType()
        {
            return java.awt.Dimension.class;
        }
    }
    
    public static class Point extends Property<java.awt.Point>
    {
        public Point(java.awt.Point defaultValue)
        {
            super(defaultValue);
        }

        @Override
        public java.awt.Point parseString(java.lang.String stringValue)
                throws IllegalArgumentException
        {
            if (stringValue == null)
                return null;
            int[] values = parseIntList(stringValue, 2);
            if (values == null)
                throw new IllegalArgumentException("invalid argument:"+stringValue);
            return values == null ? null : new java.awt.Point(values[0], values[1]);
        }

        @Override
        public java.lang.String toString(java.awt.Point value) throws IllegalArgumentException
        {
            return value == null ? null : intListToString(value.x, value.y);
        }
        @Override
        public Class<java.awt.Point> getType()
        {
            return java.awt.Point.class;
        }
    }
    
}
