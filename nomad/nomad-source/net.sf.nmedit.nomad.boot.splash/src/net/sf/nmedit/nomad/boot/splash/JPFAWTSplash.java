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
 * Created on Nov 23, 2006
 */
package net.sf.nmedit.nomad.boot.splash;

import java.net.MalformedURLException;
import java.net.URL;

import org.java.plugin.boot.SplashHandler;
import org.java.plugin.util.ExtendedProperties;

public class JPFAWTSplash extends AWTSplashScreen implements SplashHandler
{
    
    private boolean visible = false;

    public URL getImage()
    {
        try
        {
        return getSplashImageURL();
        }
        catch (MalformedURLException e)
        {
            return null;
        }
    }

    public void setImage( URL value )
    {
        putClientProperty(LOCATION_KEY, value);
    }

    public boolean isVisible()
    {
        return visible;
    }

    public void setVisible( boolean value )
    {
        if (this.visible != value)
        {
            this.visible = value;
            if (visible)
            {
                //installProperties();
                advance();
            }
            else
                dispose();
        }
    }

    public Object getImplementation()
    {
        return this;
    }
    
    public void configure(ExtendedProperties properties)
    {
        for (Object key: properties.keySet())
        {
            putClientProperty(key, properties.get(key));
        }
    }

}
