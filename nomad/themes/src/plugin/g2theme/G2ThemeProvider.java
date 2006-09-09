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
 * Created on Jul 7, 2006
 */
package plugin.g2theme;

import net.sf.nmedit.nomad.theme.NMTheme;
import net.sf.nmedit.nomad.theme.plugin.ThemePluginProvider;

public class G2ThemeProvider extends ThemePluginProvider
{

    private final
    String[] author_list = 
        new String[]{"Christian Schneider"};
    
    public String getName() {
        return "G2 Theme";
    }

    public String[] getAuthors() {
        return author_list;
    }

    public String getDescription() {
        return "Theme of the official G2 editor.";
    }

    public NMTheme getFactory() {
        return new G2ThemeFactory();
    }

    public boolean supportsCurrentPlatform() {
        // supports any platform
        return true;
    }

    @Override
    public String getHomepage()
    {
        return "http://nmedit.sourceforge.net";
    }
    
    @Override
    public String getVersion()
    {
        return "0.1 (Preview)";
    }

}
