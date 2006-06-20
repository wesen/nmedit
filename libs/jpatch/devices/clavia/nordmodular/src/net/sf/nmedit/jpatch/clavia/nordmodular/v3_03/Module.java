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
package net.sf.nmedit.jpatch.clavia.nordmodular.v3_03;

import java.awt.Point;

import javax.swing.JComponent;

import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.event.ListenableAdapter;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.event.ModuleEvent;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.spec.DModule;


public class Module extends ListenableAdapter<ModuleEvent>
{

    private final DModule     definition;

    private final Connector[] connectorList;

    private final Parameter[] parameterList;

    private final Custom[]    customList;

    private VoiceArea         voiceArea;

    private String            name;

    private int               x;

    private int               y;

    private int               index;

    //private ModuleUI ui;
    
    private static ModuleEvent eventMessage = new ModuleEvent();
    
    public Module( DModule definition )
    {
        this.index = -1;
        this.definition = definition;
        this.voiceArea = null;
        this.name = definition.getName();
        
        connectorList = new Connector[definition.getConnectorCount()];
        for (int i = definition.getConnectorCount()-1; i >= 0; i--)
            connectorList[i] = new Connector( definition.getConnector( i ), this );

        parameterList = new Parameter[definition.getParameterCount()];
        for (int i = definition.getParameterCount()-1; i >= 0; i--)
            parameterList[i] = new Parameter( definition.getParameter( i ), this );

        customList = new Custom[definition.getCustomParamCount()];
        for (int i = definition.getCustomParamCount()-1; i >= 0; i--)
            customList[i] = new Custom( definition.getCustomParam( i ) );
    }

    public void setIndex(int index)
    {
        if (voiceArea == null)
        {
            this.index = index;
        }
        else
        {
            throw new IllegalStateException("index not writeable");
        }
    }
    
    public int getIndex()
    {
        return index;
    }
    
    public void disconnectCables()
    {
        for (Connector c : connectorList)
        {
            c.disconnectCables();
        }
    }
    
    public void deAssignKnobs()
    {
        for (Parameter p : parameterList)
        {
            Knob k = p.getAssignedKnob();
            if (k!=null)
            {
                k.deAssign();
            }
        }
    }
    
    public void deAssignMorphs()
    {
        for (Parameter p : parameterList)
        {
            if (p.getAssignedMorph()!=null)
            {
                p.getAssignedMorph().remove(p);
            }
        }
    }
    
    public void deAssignMidiControllers()
    {
        Patch patch = getVoiceArea().getPatch();
        
        MidiControllerSet mset = patch.getMidiControllers();
        
        for (Parameter p : parameterList)
        {
            for (MidiController mc : mset)
            {
                if (p == mc.getAssignment())
                {
                    mc.deAssign();
                    break;
                }
            }
        }
    }
    
    public void makeUseless()
    {
        disconnectCables();
        deAssignKnobs();
        deAssignMorphs();
        deAssignMidiControllers();
    }
    
    public VoiceArea getVoiceArea()
    {
        return voiceArea;
    }
    
    void setVoiceArea(VoiceArea v)
    {
        this.voiceArea = v;
    }
    
    public void removeCables()
    {
        //
    }

    public void setLocation( Point location )
    {
        setLocation( location.x, location.y );
    }

    public void setLocation( int vx, int vy )
    {
        if (( this.x != vx ) || ( this.y != vy ))
        {
            this.x = vx;
            this.y = vy;

            if (voiceArea!=null)
            {
                voiceArea.locationChanged(this);
            }

            fireLocationChangedEvent();
        }
    }

    void fireLocationChangedEvent()
    {
        eventMessage.moduleMoved(this);
        fireEvent(eventMessage);
    }
    
    public int getX()
    {
        return x;
    }

    public void setX( int value )
    {
        setLocation( value, this.y );
    }

    public int getY()
    {
        return y;
    }
    
    public void setY( int value )
    {
        setLocation( this.x, value );
    }

    void setYWithoutEventNotification(int value)
    {
        this.y = value;
    }
    
    public final DModule getDefinition()
    {
        return definition;
    }

    public int getParameterCount()
    {
        return parameterList.length;
    }

    public int getCustomCount()
    {
        return customList.length;
    }

    public int getConnectorCount()
    {
        return connectorList.length;
    }

    public Parameter getParameter( int index )
    {
        return parameterList[index];
    }

    public Custom getCustom( int index )
    {
        return customList[index];
    }

    public Connector getConnector( int index )
    {
        return connectorList[index];
    }

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
        /*
        if (ui!=null && name!=null)
        {
            ui.setTitle(name);
        }*/
        eventMessage.moduleRenamed(this);
        fireEvent(eventMessage);
    }

    public String getShortName()
    {
        return definition.getShortName();
    }

    public int getID()
    {
        return definition.getModuleID();
    }

    public int getHeight()
    {
        return definition.getHeight();
    }

    public boolean isCvaOnly()
    {
        return definition.isCvaOnly();
    }

    public boolean isLimited()
    {
        return definition.isLimited();
    }

    public int getLimit()
    {
        return definition.getLimit();
    }

    /*
    public Component newUI( ModuleSectionUI sectionUI )
    {
        return ui = NomadEnvironment.sharedInstance().getBuilder().compose(this, sectionUI);
    }*/
    
    /*
    public void setUI(ModuleUI m)
    {
        this.ui = m;
    }

    public ModuleUI getUI()
    {
        return ui;
    }*/

    public String toString()
    {
        return "Module[ID="+getID()+",Index="+getIndex()+",Name="+getName()+"]";
    }

    // TODO remove link to ui
    private JComponent ui = null;
    
    public void setUI( JComponent ui )
    {
        this.ui = ui;
    }

    public JComponent getUI()
    {
        return ui;
    }
    
}
