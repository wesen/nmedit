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
import net.sf.nmedit.nomad.theme.property.BooleanValue;
import net.sf.nmedit.nomad.theme.property.FontProperty;
import net.sf.nmedit.nomad.theme.property.FontValue;
import net.sf.nmedit.nomad.theme.property.Property;
import net.sf.nmedit.nomad.theme.property.PropertySet;
import net.sf.nmedit.nomad.theme.property.Value;
import net.sf.nmedit.nomad.theme.property.editor.CheckBoxEditor;
import net.sf.nmedit.nomad.theme.property.editor.Editor;
import net.sf.nmedit.nomad.theme.property.editor.TextEditor;
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
    
    public void registerProperties( PropertySet set )
    {
        super.registerProperties( set );
        set.add( new LabelTextProperty() );
        set.add( new VerticalTextProperty() );
        set.add( new LabelFontProperty() );
    }
    
    private static class LabelTextProperty extends Property
    {

        public LabelTextProperty()
        {
            super( "text" );
        }

        @Override
        public Value decode( String value )
        {
            return new LabelTextValue( this, value );
        }

        @Override
        public Value encode( NomadComponent component )
        {
            return new LabelTextValue( this, ( (NomadLabel) component )
                    .getText() );
        }

        @Override
        public Editor newEditor( NomadComponent component )
        {
            return new TextEditor( this, component );
        }
    }

    private static class LabelTextValue extends Value
    {

        public LabelTextValue( Property property, String representation )
        {
            super( property, representation );
        }

        public void assignTo( NomadComponent component )
        {
            ( (NomadLabel) component ).setText( getRepresentation() );
        }

    }

    private static class VerticalTextProperty extends Property
    {

        public VerticalTextProperty()
        {
            super( "vertical" );
        }

        @Override
        public Value decode( String value )
        {
            return new VerticalTextValue( this, value );
        }

        @Override
        public Value encode( NomadComponent component )
        {
            return new VerticalTextValue( this, ( (NomadLabel) component )
                    .isVertical() );
        }

        @Override
        public Editor newEditor( NomadComponent component )
        {
            CheckBoxEditor editor = new CheckBoxEditor( this, component );
            editor.setCheckedValue( new VerticalTextValue(
                    VerticalTextProperty.this, true ) );
            editor.setUncheckedValue( new VerticalTextValue(
                    VerticalTextProperty.this, false ) );
            editor.setSelected( ( (NomadLabel) component ).isVertical() );
            return editor;
        }
    }

    private static class VerticalTextValue extends BooleanValue
    {
        public VerticalTextValue( Property property, boolean value )
        {
            super( property, value );
            setDefaultState( getBooleanValue() == DEFAULT_VERTICAL );
        }

        public VerticalTextValue( Property property, String representation )
        {
            super( property, representation );
            setDefaultState( getBooleanValue() == DEFAULT_VERTICAL );
        }

        public void assignTo( NomadComponent component )
        {
            ( (NomadLabel) component ).setVertical( getBooleanValue() );
        }
    }

    private static class LabelFontProperty extends FontProperty
    {

        public LabelFontProperty()
        {
            super( "font" );
        }

        @Override
        public FontValue encodeFont( Font font )
        {
            return new LabelFont( this, font );
        }

        @Override
        public FontValue encodeFont( NomadComponent component )
        {
            return new LabelFont( this, component.getFont() );
        }

        @Override
        public FontValue decodeFont( String value )
        {
            return new LabelFont( this, value );
        }
    }

    private static class LabelFont extends FontValue
    {
        public LabelFont( Property property, Font font )
        {
            super( property, font );
            setDefaultState( getFontValue() == NomadLabel.defaultLabelFont ); // TODO
            // wrong,
            // compare
            // representation
            // instead
        }

        public LabelFont( Property property, String representation )
        {
            super( property, representation );
            setDefaultState( getFontValue() == NomadLabel.defaultLabelFont );
        }

        @Override
        public void assignTo( NomadComponent component )
        {
            component.setFont( getFontValue() );
            ( (NomadLabel) component ).fireTextUpdateEvent();
        }

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
