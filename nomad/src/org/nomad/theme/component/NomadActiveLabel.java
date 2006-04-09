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
 * Created on Jan 9, 2006
 */
package org.nomad.theme.component;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;

import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.nomad.patch.Module;
import org.nomad.patch.Parameter;
import org.nomad.theme.NomadClassicColors;
import org.nomad.theme.property.IntegerProperty;
import org.nomad.theme.property.IntegerValue;
import org.nomad.theme.property.ParameterProperty;
import org.nomad.theme.property.Property;
import org.nomad.theme.property.PropertySet;
import org.nomad.theme.property.Value;
import org.nomad.xml.dom.module.DParameter;

/**
 * @author Christian Schneider
 */
public class NomadActiveLabel extends NomadLabel
{

    public final static int     DEFAULT_PADDING = 2;

    private int                 padding         = DEFAULT_PADDING;

    private DParameter          parameterInfo   = null;

    private final static Border defaultBorder   = NomadBorderFactory
                                                        .createNordEditor311Border();

    public NomadActiveLabel()
    {
        super();
        setSizePropertyEnabled( true );
        setIconSupportEnabled( false );
        setDynamicOverlay( true );
        setBackground( NomadClassicColors.TEXT_DISPLAY_BACKGROUND );
        setForeground( Color.WHITE );
        setAutoResize( false );
        setBorder( defaultBorder );
    }

    public void registerProperties( PropertySet set )
    {
        super.registerProperties( set );
        set.add( new PaddingProperty() );
        set.add( new LabelParamProperty() );
    }

    private static class LabelParamProperty extends ParameterProperty
    {
        public void setDParameter( NomadComponent component, DParameter p )
        {
            ( (NomadActiveLabel) component ).setParameter( p );

        }

        public DParameter getDParameter( NomadComponent component )
        {
            return ( (NomadActiveLabel) component ).getParameterInfo();
        }
    }

    public DParameter getParameterInfo()
    {
        return parameterInfo;
    }

    public void setParameter( DParameter p )
    {
        parameterInfo = p;
        /*
         * if (parameterInfo!=null) { setText(parameterInfo.getName()); }
         */
    }

    protected void recalculateSize()
    {
        super.recalculateSize();
        Insets ins = getInsets();

        int cw = getContentWidth();
        int ch = getContentHeight();

        cw += 2 * padding - 1 + ins.left + ins.right;
        ch += 2 * padding - 1 + ins.bottom + ins.top;
        setContentSize( cw, ch );
    }

    public void paintDecoration( Graphics2D g2 )
    {
        if (getBackground() != null)
        {
            g2.setColor( getBackground() );
            Insets ins = getInsets();
            g2.fillRect( ins.left, ins.top, getWidth() - 1 - ins.bottom,
                    getHeight() - 1 - ins.right );
        }
        paintNomadBorder( g2 );
    }

    public void paintDynamicOverlay( Graphics2D g2 )
    {
        { // text
            Graphics2D gtext = (Graphics2D) g2.create();
            Insets ins = getInsets();
            gtext.translate( padding + ins.left, padding + ins.top );
            gtext.setClip( new Rectangle( 0, 0, getWidth() - 2 * padding
                    - ( +ins.left + ins.right ), getHeight() - 2 * padding
                    - ( ins.bottom + ins.top ) ) );
            super.paintDecoration( gtext );
            gtext.dispose();
        }
    }

    public void setPadding( int padding )
    {
        if (this.padding != padding)
        {
            this.padding = padding;
            fireTextUpdateEvent();
        }
    }

    public int getPadding()
    {
        return padding;
    }

    private static class PaddingProperty extends IntegerProperty
    {
        public PaddingProperty()
        {
            super( "padding" );
        }

        public Value decode( String value )
        {
            return new PaddingValue( this, value );
        }

        public Value encode( NomadComponent component )
        {
            return new PaddingValue( this, ( (NomadActiveLabel) component )
                    .getPadding() );
        }
    }

    private static class PaddingValue extends IntegerValue
    {

        public PaddingValue( Property property, int value )
        {
            super( property, value );
            setDefaultState( value == DEFAULT_PADDING );
        }

        public PaddingValue( Property property, String representation )
        {
            super( property, representation );
            setDefaultState( getIntegerValue() == DEFAULT_PADDING );
        }

        public void assignTo( NomadComponent component )
        {
            NomadActiveLabel lbl = (NomadActiveLabel) component;
            lbl.setPadding( getIntegerValue() );
        }

    }

    public void link( Module module )
    {
        if (getParameterInfo() != null) // TODO remove this
            parameter = module.findParameter( getParameterInfo() );
        if (parameter != null)
        {
            paramListener = new ParameterChangeListener();
            parameter.addChangeListener( paramListener );
            updateParamText();
        }
    }

    public void unlink()
    {
        if (parameter != null)
        {
            parameter.removeChangeListener( paramListener );
            parameter = null;
            paramListener = null;
        }
    }

    private ParameterChangeListener paramListener = null;

    private Parameter               parameter     = null;

    private void updateParamText()
    {
        if (parameter != null)
        {
            setText( parameter.getInfo().getFormattedValue(
                    parameter.getValue() ) );
        }
    }

    private class ParameterChangeListener implements ChangeListener
    {
        public void stateChanged( ChangeEvent event )
        {
            updateParamText();
        }
    }

}
