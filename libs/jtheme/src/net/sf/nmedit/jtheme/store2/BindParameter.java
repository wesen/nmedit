/* Copyright (C) 2006-2007 Christian Schneider
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
package net.sf.nmedit.jtheme.store2;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to annotate methods with the signature void (JTControlAdapter) or
 * void (int, JTControlAdapter) where the first argument is an integer in the range 0,1,...,count()-1
 *  
 * @author Christian Schneider
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface BindParameter
{

    /**
     * If count()&lt;0 then name() returns the xml element name.
     * Otherwise valid the parameter names are name()+String.valueOf(i) where i is in the range [0,1,...,count()-1].
     * 
     * Example: for name = "time", count = 5 the parameter names are: time0,time1,...,time4
     * 
     * @return parameter / xml element name
     */
    String name();
    
    /**
     * If the count parameter is specified then the first argument of the
     * annotated method must be an integer in the range 0,1,...,count()-1.
     */
    int count() default -1;
    
}
