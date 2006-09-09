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
 * Created on Jan 6, 2006
 */
package net.sf.nmedit.nomad.theme.component;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Image;
import java.awt.Rectangle;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;

import net.sf.nmedit.nomad.core.nomad.NomadEnvironment;
import net.sf.nmedit.nomad.theme.laf.LabelUI;
import net.sf.nmedit.nomad.util.NomadUtilities;


/**
 * @author Christian Schneider
 */
public class NomadLabel extends NomadComponent
{

    private String              text                 = "label";
    private boolean             iconSupport          = true;
    private Image               image                = null;
    private String              ikey                 = null;

    public final static boolean DEFAULT_VERTICAL     = false;
    private boolean             flagVertText         = DEFAULT_VERTICAL;

    private final static Font   defaultLabelFont     = new Font( "SansSerif",
                                                             Font.PLAIN, 9 );
    
    private static Dimension defaultSize = null;

    public NomadLabel()
    {
        setFont(defaultLabelFont);
        setOpaque(false);
        setUI(createLabelUI());

        if (defaultSize==null)
            defaultSize = getPreferredSize();
        
        setSize(defaultSize);
    }
    
    public Image getImage()
    {
        return image;
    }
    
    private ComponentUI createLabelUI()
    {
        return LabelUI.createUI(this);
    }

    public void setText( String text )
    {
        this.text = text;
        if (iconSupport)
        {
            ikey = NomadUtilities.extractKeyFromImageString( text );
            if (ikey != null)
            {
                image = NomadEnvironment.sharedInstance().getImageTracker()
                        .getImage( ikey );
            }
        }
        
        // TODO invalidate
        //recalculateSize();
        autoResize();
        fireTextUpdateEvent();
    }
    
    protected void autoResize()
    {
        setSize(getPreferredSize());
    }
    
    public String getText()
    {
        return text;
    }

    public void setIconSupportEnabled( boolean enable )
    {
        // if (iconSupport!=enable) {
        iconSupport = enable;
        // if (enable)
        // tryLoadIcon();
        // fireTextUpdateEvent();
        // }
    }

    // TODO replace with property event integration
    protected void fireTextUpdateEvent()
    {
        repaint();
    }

    public void setVertical( boolean enable )
    {
        if (this.flagVertText != enable)
        {
            this.flagVertText = enable;
            fireTextUpdateEvent();
        }
    }

    public boolean isVertical()
    {
        return flagVertText;
    }

    public boolean getVertical()
    {
        return isVertical();
    }
    
    public static Rectangle getStringBounds( JComponent component, String string )
    {
        return getStringBounds( component, string, component.getFont() );
    }

    public static Rectangle getStringBounds( JComponent component,
            String string, Font font )
    {
        return getStringBounds( component, string, font, false );
    }

    public static Rectangle getStringBounds( JComponent component,
            String string, Font font, boolean vertical )
    {
        FontMetrics fm = component.getFontMetrics( font );
        Rectangle bounds = new Rectangle( 0, 0, 0, 0 );
        if (vertical)
        {
            bounds.width = fm.getMaxAdvance();
            bounds.height = ( fm.getHeight() + 1 ) * string.length();
            bounds.y = fm.getHeight();
        }
        else
        {
            bounds.width = fm.stringWidth( string );
            bounds.height = fm.getHeight();
            bounds.y = bounds.height - fm.getDescent();
        }
        return bounds;
    }

}
