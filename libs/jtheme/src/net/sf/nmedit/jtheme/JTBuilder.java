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
package net.sf.nmedit.jtheme;

import java.lang.ref.SoftReference;
import java.util.HashSet;
import java.util.Set;

import net.sf.nmedit.jtheme.reflect.DefaultPropertyExclusionFilter;
import net.sf.nmedit.jtheme.reflect.PropertyAccessGroup;
import net.sf.nmedit.jtheme.reflect.PropertyDatabase;
import net.sf.nmedit.jtheme.reflect.PropertyExclusionFilter;

@Deprecated
public class JTBuilder
{

    private SoftReference<PropertyDatabase> propertyDatabaseReference
        = new SoftReference<PropertyDatabase>(null);
    
    public PropertyDatabase getPropertyDatabase()
    {
        PropertyDatabase db = propertyDatabaseReference.get();
        if (db == null)
        {
            db = new PropertyDatabase();
            db.setExclusionFilter(createPropertyFilter());
            propertyDatabaseReference = new SoftReference<PropertyDatabase>(db);
        }
        return db;
    }
    
    public PropertyAccessGroup getPropertyAccessGroup(Class<?> target)
    {
        return getPropertyDatabase().getPropertyAccessGroup(target);
    }
    
    protected PropertyExclusionFilter createPropertyFilter()
    {
        Set<String> set = new HashSet<String>();
        addExclusiveProperties(set);
        return set.size() > 0 ? new DefaultPropertyExclusionFilter(set) : null;
    }
    
    protected void addExclusiveProperties(Set<String> exclusive)
    {
        exclusive.add("actionMap");
        exclusive.add("alignmentX");
        exclusive.add("alignmentY");
        exclusive.add("autoscrolls");
        exclusive.add("bounds");
        exclusive.add("componentOrientation");
        exclusive.add("componentPopupMenu");
        exclusive.add("cursor");
        exclusive.add("debugGraphicsOptions");
        exclusive.add("doubleBuffered");
        exclusive.add("dropTarget");
        exclusive.add("focusCycleRoot");
        exclusive.add("focusTraversalKeysEnabled");
        exclusive.add("focusTraversalPolicy");
        exclusive.add("focusTraversalPolicyProvider");
        exclusive.add("focusable");
        exclusive.add("ignoreRepaint");
        exclusive.add("inheritsPopupMenu");
        exclusive.add("inputVerifier");
        exclusive.add("locale");
        exclusive.add("verifyInputWhenFocusTarget");
        exclusive.add("visible");
        exclusive.add("transferHandler");
        exclusive.add("requestFocusEnabled");
        exclusive.add("layout");
        exclusive.add("defaultValue");
        exclusive.add("minValue");
        exclusive.add("maxValue");
        exclusive.add("minimumSize");
        exclusive.add("maximumSize");
        exclusive.add("nextFocusableComponent");
        exclusive.add("normalizedValue");
        exclusive.add("toolTipText");
        exclusive.add("value");
        exclusive.add("preferredSize");
        exclusive.add("enabled");
        exclusive.add("border");
     
        exclusive.add("opaque");
        exclusive.add("name");
       // exclusive.add("background");
     //   exclusive.add("foreground");
    }
    
}

