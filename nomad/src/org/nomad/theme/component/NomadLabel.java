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
package org.nomad.theme.component;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import javax.swing.JComponent;

import net.sf.nmedit.nomad.core.nomad.NomadEnvironment;

import org.nomad.theme.property.BooleanValue;
import org.nomad.theme.property.FontProperty;
import org.nomad.theme.property.FontValue;
import org.nomad.theme.property.Property;
import org.nomad.theme.property.PropertySet;
import org.nomad.theme.property.Value;
import org.nomad.theme.property.editor.CheckBoxEditor;
import org.nomad.theme.property.editor.Editor;
import org.nomad.theme.property.editor.TextEditor;
import org.nomad.util.misc.NomadUtilities;

/**
 * @author Christian Schneider
 */
public class NomadLabel extends NomadComponent
{

    private String              text                 = "label";

    private String              ikey                 = null;

    private Image               image                = null;

    public final static boolean DEFAULT_ANTIALIASING = false;

    public final static boolean DEFAULT_VERTICAL     = false;

    private boolean             iconSupport          = true;

    private boolean             flagVertText         = DEFAULT_VERTICAL;

    private boolean             flagTextAntialiasing = DEFAULT_ANTIALIASING;

    private boolean             flagAutoResize       = true;

    private int                 contentWidth         = 30;

    private int                 contentHeight        = 12;

    private int                 ty                   = contentHeight - 2;

    private final static Font   defaultLabelFont     = new Font( "SansSerif",
                                                             Font.PLAIN, 9 );

    public NomadLabel()
    {
        super();
        setSizePropertyEnabled( false );
        super.setFont( defaultLabelFont );
        setForeground( Color.BLACK );
        useContentSize();
    }

    public void setFont( Font f )
    {
        if (text != null)
        {
            recalculateSize();
        }
        super.setFont( f );
    }

    /*
     * public Dimension getContentSize() { return contentSize; }
     */

    public int getContentWidth()
    {
        return contentWidth;
    }

    public int getContentHeight()
    {
        return contentHeight;
    }

    public void setContentSize( int w, int h )
    {
        this.contentWidth = w;
        this.contentWidth = h;
    }

    public void setContentSize( Dimension size )
    {
        contentWidth = size.width;
        contentHeight = size.height;
    }

    public boolean isAutoResizeEnabled()
    {
        return flagAutoResize;
    }

    public void setAutoResize( boolean enabled )
    {
        if (flagAutoResize != enabled)
        {
            flagAutoResize = enabled;
            if (flagAutoResize) fireTextUpdateEvent();
        }
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
        recalculateSize();
        fireTextUpdateEvent();
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

    protected void recalculateSize()
    {
        if (flagAutoResize)
        {
            if (image == null || !iconSupport)
            {
                FontMetrics fm = getFontMetrics( getFont() );
                if (!isVertical())
                {
                    contentWidth = fm.stringWidth( text );
                    contentHeight = fm.getHeight();
                    // ty = contentSize.height - fm.getDescent();
                }
                else
                {
                    contentWidth = fm.getMaxAdvance();
                    contentHeight = ( fm.getHeight() + 1 ) * text.length();
                }
                ty = contentHeight;
            }
            else
            {
                contentWidth = image.getWidth( null );
                contentHeight = image.getHeight( null );
            }
            useContentSize();
        }
    }

    protected void useContentSize()
    {
        if (flagAutoResize)
        {
            setSize( contentWidth, contentHeight );
        }
    }

    public boolean isTextAntialiased()
    {
        return flagTextAntialiasing;
    }

    public void setTextAntialiased( boolean enable )
    {
        if (flagTextAntialiasing != enable)
        {
            flagTextAntialiasing = enable;
            fireTextUpdateEvent();
        }
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

    // TODO replace with property event integration
    protected void fireTextUpdateEvent()
    {
        repaint();
    }

    public String getText()
    {
        return text;
    }

    public void registerProperties( PropertySet set )
    {
        super.registerProperties( set );
        set.add( new LabelTextProperty() );
        set.add( new VerticalTextProperty() );
        set.add( new LabelFontProperty() );
        set.add( new AntialiasTextProperty() );
    }

    private static class AntialiasTextProperty extends Property
    {

        public AntialiasTextProperty()
        {
            super( "antialiasing" );
        }

        @Override
        public Value decode( String value )
        {
            return new AntialiasValue( this, value );
        }

        @Override
        public Value encode( NomadComponent component )
        {
            return new AntialiasValue( this, ( (NomadLabel) component )
                    .isTextAntialiased() );
        }

        @Override
        public Editor newEditor( NomadComponent component )
        {
            CheckBoxEditor editor = new CheckBoxEditor( this, component );
            editor.setCheckedValue( new AntialiasValue(
                    AntialiasTextProperty.this, true ) );
            editor.setUncheckedValue( new AntialiasValue(
                    AntialiasTextProperty.this, false ) );
            editor.setSelected( ( (NomadLabel) component ).isTextAntialiased() );
            return editor;
        }
    }

    private static class AntialiasValue extends BooleanValue
    {
        public AntialiasValue( Property property, boolean value )
        {
            super( property, value );
            setDefaultState( getBooleanValue() == DEFAULT_ANTIALIASING );
        }

        public AntialiasValue( Property property, String representation )
        {
            super( property, representation );
            setDefaultState( getBooleanValue() == DEFAULT_ANTIALIASING );
        }

        public void assignTo( NomadComponent component )
        {
            ( (NomadLabel) component ).setTextAntialiased( getBooleanValue() );
        }
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

        /*
         * public LabelTextProperty() { setName("text"); } public String
         * getValue(NomadComponent component) { return
         * ((NomadLabel)component).getText(); } public void
         * setValue(NomadComponent component, String value) {
         * ((NomadLabel)component).setText(value); } public PropertyValue
         * getValue(String value) { return new StringValue(this, value); }
         */
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

    /*
     * private static class VerticalTextProperty extends BooleanProperty {
     * public VerticalTextProperty() { setName("vertical"); } public void
     * setBoolean(NomadComponent component, boolean value) {
     * ((NomadLabel)component).setVertical(value); } public boolean
     * getBoolean(NomadComponent component) { return
     * ((NomadLabel)component).isVertical(); } public boolean
     * isInDefaultState(NomadComponent component) { return
     * ((NomadLabel)component).flagVertText==DEFAULT_VERTICAL; } public
     * PropertyValue getCurrentValue(NomadComponent component) { return new
     * BooleanValue(this, getBoolean(component)); } }
     */

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

        /*
         * public LabelFontProperty() { } public Font getFont(NomadComponent
         * component) { return component.getFont(); } public void
         * setFont(NomadComponent component, Font f) { component.setFont(f);
         * ((NomadLabel)component).fireTextUpdateEvent(); } public boolean
         * isInDefaultState(NomadComponent component) { return
         * ((NomadLabel)component).getFont()==NomadLabel.defaultLabelFont; }
         */
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

    public void paintDecoration( Graphics2D g2 )
    {
        if (image != null)
        {
            g2.drawImage( image, 0, 0, this );
        }
        else
        {
            if (flagTextAntialiasing)
            {
                g2.setRenderingHint( RenderingHints.KEY_TEXT_ANTIALIASING,
                        RenderingHints.VALUE_TEXT_ANTIALIAS_ON );
            }

            g2.setFont( getFont() );
            g2.setColor( getForeground() );

            if (isVertical())
            {
                FontMetrics fm = getFontMetrics( getFont() );
                for (int i = 0; i < text.length(); i++)
                    g2.drawString( Character.toString( text.charAt( i ) ), 0,
                            ( fm.getHeight() + 1 ) * ( i + 1 ) - 1 );
            }
            else
            {
                g2.drawString( text, 0, ty );
            }
        }
    }

}
