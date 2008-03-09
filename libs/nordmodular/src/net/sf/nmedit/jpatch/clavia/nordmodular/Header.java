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
 * Created on Apr 10, 2006
 */
package net.sf.nmedit.jpatch.clavia.nordmodular;

/**
 * Patch header section.
 * 
 * @author Christian Schneider
 */
public class Header 
{

    private int[] data;
    
    private NMPatch patch;

    public Header(NMPatch patch)
    {
        this.patch = patch;
        data = new int[Format.VALUE_COUNT_HEADER];
        
        data[Format.HEADER_KEYBOARD_RANGE_MAX] = Format.HEADER_KEYBOARD_RANGE_MAX_DEFAULT;
        data[Format.HEADER_VELOCITY_RANGE_MIN] = Format.HEADER_VELOCITY_RANGE_MIN_DEFAULT;
        data[Format.HEADER_VELOCITY_RANGE_MAX] = Format.HEADER_VELOCITY_RANGE_MAX_DEFAULT;
        data[Format.HEADER_BEND_RANGE] = Format.HEADER_BEND_RANGE_DEFAULT;
        data[Format.HEADER_PORTAMENTO_TIME] = Format.HEADER_PORTAMENTO_TIME_DEFAULT;
        data[Format.HEADER_PORTAMENTO] = Format.HEADER_PORTAMENTO_DEFAULT;
        data[Format.HEADER_REQUESTED_VOICES] = Format.HEADER_REQUESTED_VOICES_DEFAULT+1;
        data[Format.HEADER_SECTION_SEPARATOR_POSITION] = Format.HEADER_SECTION_SEPARATOR_POSITION_DEFAULT;
        data[Format.HEADER_OCTAVE_SHIFT] = Format.HEADER_OCTAVE_SHIFT_DEFAULT;
        data[Format.HEADER_VOICE_RETRIGGER_POLY] = Format.HEADER_VOICE_RETRIGGER_POLY_DEFAULT;
        data[Format.HEADER_VOICE_RETRIGGER_COMMON] = Format.HEADER_VOICE_RETRIGGER_COMMON_DEFAULT;
        data[Format.HEADER_UNKNOWN1] = Format.HEADER_UNKNOWN1_DEFAULT;
        data[Format.HEADER_UNKNOWN2] = Format.HEADER_UNKNOWN2_DEFAULT;
        data[Format.HEADER_UNKNOWN3] = Format.HEADER_UNKNOWN3_DEFAULT;
        data[Format.HEADER_UNKNOWN4] = Format.HEADER_UNKNOWN4_DEFAULT;
        data[Format.HEADER_CABLE_VISIBILITY_RED] = Format.HEADER_CABLE_VISIBLE;
        data[Format.HEADER_CABLE_VISIBILITY_BLUE] = Format.HEADER_CABLE_VISIBLE;
        data[Format.HEADER_CABLE_VISIBILITY_YELLOW] = Format.HEADER_CABLE_VISIBLE;
        data[Format.HEADER_CABLE_VISIBILITY_GRAY] = Format.HEADER_CABLE_VISIBLE;
        data[Format.HEADER_CABLE_VISIBILITY_GREEN] = Format.HEADER_CABLE_VISIBLE;
        data[Format.HEADER_CABLE_VISIBILITY_PURPLE] = Format.HEADER_CABLE_VISIBLE;
        data[Format.HEADER_CABLE_VISIBILITY_WHITE] = Format.HEADER_CABLE_VISIBLE;
    }

    public int getUnknown( int index )
    {
        if (index >= 4) throw new IndexOutOfBoundsException();
        return data[Format.HEADER_UNKNOWN1 + index];
    }

    public void setUnknown( int index, int value )
    {
        if (index >= 4) throw new IndexOutOfBoundsException();
        setValue( Format.HEADER_UNKNOWN1 + index, value );
    }

    public int getUnknown1()
    {
        return data[Format.HEADER_UNKNOWN1];
    }

    public int getUnknown2()
    {
        return data[Format.HEADER_UNKNOWN2];
    }

    public int getUnknown3()
    {
        return data[Format.HEADER_UNKNOWN3];
    }

    public int getUnknown4()
    {
        return data[Format.HEADER_UNKNOWN4];
    }

    public void setUnknown1( int value )
    {
        setValue( Format.HEADER_UNKNOWN1, value );
    }

    public void setUnknown2( int value )
    {
        setValue( Format.HEADER_UNKNOWN2, value );
    }

    public void setUnknown3( int value )
    {
        setValue( Format.HEADER_UNKNOWN3, value );
    }

    public void setUnknown4( int value )
    {
        setValue( Format.HEADER_UNKNOWN4, value );
    }

    public boolean isCableVisible( Signal c )
    {
        switch (c)
        {
            case AUDIO:
                return data[Format.HEADER_CABLE_VISIBILITY_RED] != 0;
            case CONTROL:
                return data[Format.HEADER_CABLE_VISIBILITY_BLUE] != 0;
            case LOGIC:
                return data[Format.HEADER_CABLE_VISIBILITY_YELLOW] != 0;
            case SLAVE:
                return data[Format.HEADER_CABLE_VISIBILITY_GRAY] != 0;
            case USER1:
                return data[Format.HEADER_CABLE_VISIBILITY_GREEN] != 0;
            case USER2:
                return data[Format.HEADER_CABLE_VISIBILITY_PURPLE] != 0;
            case NONE:
                return data[Format.HEADER_CABLE_VISIBILITY_WHITE] != 0;
            default:
                throw new IllegalArgumentException( "unknown cable color" );
        }
    }

    public void setCableVisible( Signal c, boolean visible )
    {
        int intValue = visible ? 1 : 0;
        switch (c)
        {
            case AUDIO:
                setValue( Format.HEADER_CABLE_VISIBILITY_RED, intValue );
                break;
            case CONTROL:
                setValue( Format.HEADER_CABLE_VISIBILITY_BLUE, intValue );
                break;
            case LOGIC:
                setValue( Format.HEADER_CABLE_VISIBILITY_YELLOW, intValue );
                break;
            case SLAVE:
                setValue( Format.HEADER_CABLE_VISIBILITY_GRAY, intValue );
                break;
            case USER1:
                setValue( Format.HEADER_CABLE_VISIBILITY_GREEN, intValue );
                break;
            case USER2:
                setValue( Format.HEADER_CABLE_VISIBILITY_PURPLE, intValue );
                break;
            case NONE:
                setValue( Format.HEADER_CABLE_VISIBILITY_WHITE, intValue );
                break;
            default:
                throw new IllegalArgumentException( "unknown cable color" );
        }
    }

    public int getBendRange()
    {
        return data[Format.HEADER_BEND_RANGE];
    }

    public void setBendRange( int bend_range )
    {
        setValue( Format.HEADER_BEND_RANGE, bend_range );
    }

    public int getKeyboardRangeMax()
    {
        return data[Format.HEADER_KEYBOARD_RANGE_MAX];
    }

    public void setKeyboardRangeMax( int keyboard_range_max )
    {
        setValue( Format.HEADER_KEYBOARD_RANGE_MAX, keyboard_range_max );
    }

    public void setKeyboardRange( int min, int max )
    {
        setKeyboardRangeMin( min );
        setKeyboardRangeMax( max );
    }

    public int getKeyboardRangeMin()
    {
        return data[Format.HEADER_KEYBOARD_RANGE_MIN];
    }

    public void setKeyboardRangeMin( int keyboard_range_min )
    {
        setValue( Format.HEADER_KEYBOARD_RANGE_MIN, keyboard_range_min );
    }

    public int getOctaveShift()
    {
        return data[Format.HEADER_OCTAVE_SHIFT];
    }

    public void setOctaveShift( int octave_shift )
    {
        setValue( Format.HEADER_OCTAVE_SHIFT, octave_shift );
    }

    public boolean isPortamentoAutoEnabled()
    {
        return data[Format.HEADER_PORTAMENTO] != 0;
    }

    public int getPortamento()
    {
        return data[Format.HEADER_PORTAMENTO];
    }

    public void setPortamentoAutoEnabled( boolean enable )
    {
        setValue( Format.HEADER_PORTAMENTO, enable ? 1 : 0 );
    }

    public void setPortamento( int portamento_time, boolean autoEnabled )
    {
        setPortamentoTime( portamento_time );
        setPortamentoAutoEnabled( autoEnabled );
    }

    public int getPortamentoTime()
    {
        return data[Format.HEADER_PORTAMENTO_TIME];
    }

    public void setPortamentoTime( int portamento_time )
    {
        setValue( Format.HEADER_PORTAMENTO_TIME, portamento_time );
    }

    public int getRequestedVoices()
    {
        return data[Format.HEADER_REQUESTED_VOICES];
    }

    public void setRequestedVoices( int requested_voices )
    {
        setValue( Format.HEADER_REQUESTED_VOICES, requested_voices );
    }

    public int getSeparatorPosition()
    {
        return data[Format.HEADER_SECTION_SEPARATOR_POSITION];
    }

    public void setSeparatorPosition( int separator_position )
    {
        setValue( Format.HEADER_SECTION_SEPARATOR_POSITION, separator_position );
    }

    public int getVelocityRangeMax()
    {
        return data[Format.HEADER_VELOCITY_RANGE_MAX];
    }

    public void setVelocityRangeMax( int velocity_range_max )
    {
        setValue( Format.HEADER_VELOCITY_RANGE_MAX, velocity_range_max );
    }

    public int getVelocityRangeMin()
    {
        return data[Format.HEADER_VELOCITY_RANGE_MIN];
    }

    public void setVelocityRangeMin( int velocity_range_min )
    {
        setValue( Format.HEADER_VELOCITY_RANGE_MIN, velocity_range_min );
    }

    public void setVelocityRange( int min, int max )
    {
        setVelocityRangeMin( min );
        setVelocityRangeMax( max );
    }

    public boolean isVoiceRetriggerCommonActive()
    {
        return data[Format.HEADER_VOICE_RETRIGGER_COMMON] != 0;
    }

    public void setVoiceRetriggerActive( boolean poly_active,
            boolean common_active )
    {
        setVoiceRetriggerCommonActive( common_active );
        setVoiceRetriggerPolyActive( poly_active );
    }

    public void setVoiceRetriggerCommonActive( boolean active )
    {
        setValue( Format.HEADER_VOICE_RETRIGGER_COMMON, active ? 1 : 0 );
    }

    public boolean isVoiceRetriggerPolyActive()
    {
        return data[Format.HEADER_VOICE_RETRIGGER_POLY] != 0;
    }

    public void setVoiceRetriggerPolyActive( boolean active )
    {
        setValue( Format.HEADER_VOICE_RETRIGGER_POLY, active ? 1 : 0 );
    }

    public void setValue( int index, int value )
    {
        int oldValue = data[index];
        if (oldValue != value)
        {
            data[index] = value;
            boolean ignoreModified = (index == Format.HEADER_SECTION_SEPARATOR_POSITION);
            firePatchSettingsChanged(ignoreModified);
        }
    }

    public int getValue( int index )
    {
        return data[index];
    }
    
    public void firePatchSettingsChanged(boolean ignoreModified)
    {
        patch.firePatchSettingsChanged(ignoreModified);
    }

    public void setValueWithoutNotification( int index, int value )
    {
        data[index] = value;
    }

    public NMPatch getPatch()
    {
        return patch;
    }

    public int[] getData()
    {
        return (int[]) data.clone();
    }
    
}
