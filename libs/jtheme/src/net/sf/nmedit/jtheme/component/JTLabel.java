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
 * Created on Nov 25, 2006
 */
package net.sf.nmedit.jtheme.component;

import net.sf.nmedit.jtheme.JTContext;
import net.sf.nmedit.jtheme.util.RetargetMouseEventSupport;

public class JTLabel extends JTComponent
{

    /**
     * 
     */
    private static final long serialVersionUID = -4347651399715148385L;
    final public static String uiClassID = "LabelUI";
    private String text;
    private String splitText[];

    public JTLabel( JTContext context )
    {
        super( context );
        setOpaque(false);
        setText("label");
        // capture mouse events for retargeting
        installRetargetMouseEventSupport();
    }

    protected void installRetargetMouseEventSupport()
    {
        RetargetMouseEventSupport rmes = RetargetMouseEventSupport.retargetToParent(this);
        rmes.install(this);
    }

    public String getUIClassID() 
    {
        return uiClassID;
    }

    public boolean isReducible()
    {
        return true;
    }
    
    public void setText(String text)
    {
        String oldText = this.text;
        
       text = text.replace("\\n", "\n");
        
        if (text==null||oldText==null||(!text.equals(oldText)))
        {
            this.text = text;
            splitText = text.split("\n");
            
            revalidate();
            repaint();
        }
    }
    
    public String getText()
    {
        return this.text;
    }

    public String[] getSplitText()
    {
        return this.splitText;
    }

}
