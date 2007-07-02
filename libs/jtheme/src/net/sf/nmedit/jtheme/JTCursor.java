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

import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.net.URL;

import javax.imageio.ImageIO;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class JTCursor
{

    public static final int JACK1 = 0;
    public static final int JACK2 = 1;
    public static final int JACKConnected1 = 2;
    public static final int JACKConnected2 = 3;
    
    private static String[] names =
        new String[] {"curJack1.png", "curJack2.png", "curJackConnected1.png"
        , "curJackConnected2.png"};
    
    private static URL getResource(int id)
    {
        return JTCursor.class.getResource("/net/sf/nmedit/jtheme/"+names[id]);
    }
    
    private static Cursor createCursor(int id)
    {
        Toolkit tk = Toolkit.getDefaultToolkit();
        URL res = getResource(id);
        
        if (res == null)
        {
            Log log = LogFactory.getLog(JTCursor.class);
            if (log.isWarnEnabled())
            {
                log.warn("Could not find cursor: id="+id+", url="+res);
            }
            return Cursor.getDefaultCursor();
        }
        
        Image img;
        try
        {
            img = ImageIO.read(res);
        }
        catch (IOException e)
        {
            Log log = LogFactory.getLog(JTCursor.class);
            if (log.isWarnEnabled())
            {
                log.warn("Could not find cursor: id="+id+", url="+res, e);
            }
            return Cursor.getDefaultCursor();
        }
        
        return tk.createCustomCursor(img, new Point(4,16), names[id]);
    }
    
    private static SoftReference<?>[] cursors = new SoftReference[4];
    
    public static Cursor getJackCursor(int id)
    {
        if (!(0<=id && id<names.length))
            return null;
        
        SoftReference<?> ref = cursors[id];
        
        Cursor cursor = null;
        if (ref == null)
        {
            cursor = createCursor(id);
            cursors[id] = new SoftReference<Cursor>(cursor);
            return cursor;
        }
        cursor = (Cursor) ref.get();
        if (cursor == null)
        {
            cursor = createCursor(id);
            cursors[id] = new SoftReference<Cursor>(cursor); 
        }
        
        return cursor;
    }
    
}

