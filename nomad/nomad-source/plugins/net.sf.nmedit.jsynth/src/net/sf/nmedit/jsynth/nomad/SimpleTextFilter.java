
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

package net.sf.nmedit.jsynth.nomad;

import java.util.regex.Pattern;

public class SimpleTextFilter
{
    
    private String filter = null;
    private boolean ignoreCase = true;
    private boolean acceptInfix = true;
    private Pattern pattern;

    public SimpleTextFilter()
    {
        super();
    }
    
    public boolean isFiltering()
    {
        return this.pattern != null; 
    }
    
    public void setIgnoreCaseEnabled(boolean enabled)
    {
        if (this.ignoreCase != enabled)
        {
            this.ignoreCase = enabled;
            updatePattern();
        }
    }
    
    public boolean isIgnoreCaseEnabled()
    {
        return ignoreCase;
    }
    
    public void setFilter(String filter)
    {
        if (this.filter == filter || this.filter != null && this.filter.equals(filter))
            return;
        if (filter != null)
        {
            int TIMES = 0;
            for (int i=0;i<filter.length();i++)
                if (filter.charAt(i)=='*')
                    TIMES++;
            if (filter.length()==TIMES)
                filter = "*";
        }
        this.filter = filter;
        updatePattern();
    }
    
    private void updatePattern()
    {
        this.pattern = filter == null ? null : createPattern(filter);
    }
    
    private Pattern createPattern(String filter)
    {
        if (filter.length() == 1 && filter.charAt(0) == '*')
            return null; 
        
        StringBuilder sb = new StringBuilder();
        for (int i=0;i<filter.length();i++)
        {
            char c = filter.charAt(i);
            switch (c)
            {
              case '*':
                  sb.append(".*");
                  break;
              case '[':
                  sb.append("\\[");
                  break;
              case ']':
                  sb.append("\\]");
                  break;
              case '(':
                  sb.append("\\(");
                  break;
              case ')':
                  sb.append("\\)");
                  break;
              case '{':
                  sb.append("\\{");
                  break;
              case '}':
                  sb.append("\\}");
                  break;
              case '\\':
                  sb.append("\\\\");
                  break;
              case '$':
                  sb.append("\\$");
                  break;
              case '^':
                  sb.append("\\^");
                  break;
              case '.':
                  sb.append("\\.");
                  break;
              default:
                  sb.append(c);
                  break;
            }
        }
        
        String p = sb.toString();
        /*if (acceptInfix)*/
            p = ".*"+p+".*";
        int flags = Pattern.DOTALL;
        if (ignoreCase) flags |= Pattern.CASE_INSENSITIVE;
        return Pattern.compile(p, flags);
    }

    public boolean contains(CharSequence s)
    {
        if (s == null)
            return false; 
        if (pattern == null)
            return true;
        return pattern.matcher(s).matches();
    }
    
}
