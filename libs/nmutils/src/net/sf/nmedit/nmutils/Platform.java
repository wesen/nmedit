package net.sf.nmedit.nmutils;

import java.awt.event.MouseEvent;

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
public class Platform
{
    /* some values for System.getProperty("os.name")
      // unix flavor
      AIX 
      Digital Unix
      FreeBSD
      HP UX // also HP-UX
      Irix
      Linux
      Solaris
      UnixWare
      OpenUnix
      OpenServer
      Compaq's Digital UNIX
      OSF1
      
      // BeOS Flavor
      BeOS
      // ??? Haiku ???
      
      // mac os flavor
      Mac OS          
      
      // windows flavor
      Windows 2000    
      Windows 95
      Windows 98
      Windows NT
      Windows XP
      OS/2
      
      // unknown flavor
      MPE/iX
      Netware 4.11
     */
    public static enum OS
    {
        WindowsFlavor,
        UnixFlavor,
        MacOSFlavor,
        BeOSFlavor,
        UnknownFlavor
    }
    
    private static OS flavor;
    
    static
    {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.indexOf("windows")>=0 || os.indexOf("nt")>=0 || os.indexOf("os/2")>=0)
            Platform.flavor = OS.WindowsFlavor;
        else if (os.indexOf("beos")>=0)
            Platform.flavor = OS.BeOSFlavor;
        else if (os.indexOf("mac")>=0)
            Platform.flavor = OS.MacOSFlavor;
        else if (os.indexOf("linux")>=0 || 
                os.indexOf("solaris")>=0 || 
                os.indexOf("freebsd")>=0 ||
                os.indexOf("unix")>=0)
            Platform.flavor = OS.UnixFlavor;
        else
            Platform.flavor = OS.UnknownFlavor;
    }
    
    public static OS flavor()
    {
        return flavor;
    }
    
    public static boolean isFlavor(OS flavor)
    {
        return Platform.flavor == flavor;
    }
    
    public static boolean isPopupTrigger(MouseEvent e)
    {
        if (Platform.isFlavor(OS.MacOSFlavor))
        {
            return (e.getID() == MouseEvent.MOUSE_RELEASED) && e.isPopupTrigger();
        }
        else
        {
            return (e.getID() == MouseEvent.MOUSE_PRESSED) && e.isPopupTrigger();
        }
    }
  
}
