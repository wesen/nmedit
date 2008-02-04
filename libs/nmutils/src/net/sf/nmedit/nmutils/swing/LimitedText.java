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
 * Created on Sep 10, 2006
 */
package net.sf.nmedit.nmutils.swing;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;

public class LimitedText extends DefaultStyledDocument {
    
    /**
     * 
     */
    private static final long serialVersionUID = -1104470300584043715L;
    private int maxCharacters;

    public LimitedText(int maxCharacters) 
    {
        this.maxCharacters = maxCharacters;
    }

    public void insertString(int offs, String str, AttributeSet a) 
        throws BadLocationException {

        if (getLength() < maxCharacters)
        {
            // only insert if more characters are possible

            // truncate string?
            if (getLength() + str.length()>=maxCharacters)
            {
                // str is too large, but substring fits
                str = str.substring(0, maxCharacters-getLength()); // truncate string
            }
            
            super.insertString(offs, str, a);
        }
    }
}