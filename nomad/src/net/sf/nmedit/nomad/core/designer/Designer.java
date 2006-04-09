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
 * Created on Apr 1, 2006
 */
package net.sf.nmedit.nomad.core.designer;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;

public class Designer
{

    final static ColorUIResource panDark = new ColorUIResource(Color.decode("#D4CFC9"));
    final static ColorUIResource panBright = new ColorUIResource(Color.decode("#E7E1DB"));
    final static ColorUIResource tabBG = new ColorUIResource(Color.decode("#F4F4F4"));

    public Designer()
    {
        super();
    }
    
   // private static Class labelUIClass;

    public static void init()
    {
       // printKeyValues();
        setDefaultFont( new FontUIResource( "sansserif", Font.PLAIN, 11 ) );
/*        
        UIManager.put("Panel.background", panDark);
        UIManager.put("PopupMenu.background", panBright);

        InsetsUIResource mnMargin = new InsetsUIResource(8,8,8,8);
        
        UIManager.put("MenuItem.background", panBright);
        UIManager.put("MenuItem.margin", mnMargin);
        UIManager.put("CheckBoxMenuItem.background", panBright);
        UIManager.put("CheckBoxMenuItem.margin", mnMargin);
        UIManager.put("RadioButtonMenuItem.background", panBright);
        UIManager.put("RadioButtonMenuItem.margin", mnMargin);

        //UIManager.put("TabbedPane.highlight", panBright);
        UIManager.put("TabbedPane.darkShadow", panDark);
        UIManager.put("TabbedPane.light", panBright);
        UIManager.put("TabbedPane.selected", panBright);
        UIManager.put("TabbedPane.selectedHighlight", panBright.brighter());
        UIManager.put("TabbedPane.contentAreaColor", panDark);
        UIManager.put("TabbedPane.borderHightlightColor", new ColorUIResource(Color.decode("#716060")));
        UIManager.put("TabbedPane.unselectedBackground", tabBG);
        UIManager.put("TabbedPane.focus", new ColorUIResource(Color.decode("#F8F2EB")));

        UIManager.put("Menu.background", panDark);
        UIManager.put("Menu.margin", mnMargin);
*/        
    }
    /**
     * Sets the default font.
     * 
     * @param f
     *            default font
     */
    public static void setDefaultFont( FontUIResource f )
    {
        Enumeration enu = UIManager.getDefaults().keys();
        
        while (enu.hasMoreElements())
        {
            Object key = enu.nextElement();
            
            if (UIManager.get( key ) instanceof FontUIResource)
            {
                UIManager.put( key, f );
            }
        }
    }

    public static void printKeyValues()
    {
        Enumeration enu = UIManager.getDefaults().keys();
        ArrayList<String> list = new ArrayList<String>();
        while (enu.hasMoreElements())
        {
            Object key = enu.nextElement();
            String s = key+"="+UIManager.get(key);
            
            for (int i=0;i<list.size();i++)
            {
                if (list.get(i).compareTo(s)>=0)
                {
                    list.add(i, s);
                    s = null;
                    break;
                }
            }
            if (s!=null)
            {
                list.add(s);
            }
        }
        
        for (String s : list)
            System.out.println(s);
    }
}
