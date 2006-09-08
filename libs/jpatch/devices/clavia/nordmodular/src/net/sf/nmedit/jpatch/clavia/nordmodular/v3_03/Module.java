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

import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.event.Event;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.event.EventBuilder;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.event.EventChain;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.event.ModuleListener;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.spec.DModule;


public class Module 
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
                voiceArea.updateSize(this, VoiceArea.MOVE);

            fireLocationChangedEvent();
        }
    }

    void setYWithoutVANotification(int vy)
    {
        if (( this.y != vy ))
        {            
            this.y = vy;

            fireLocationChangedEvent();
        }
    }
    
    private EventChain<ModuleListener> listenerList = null;
    
    public void addModuleListener(ModuleListener l)
    {
        listenerList = new EventChain<ModuleListener>(l, listenerList);
    }
    
    public void removeModuleListener(ModuleListener l)
    {
        if (listenerList!=null)
            listenerList = listenerList.remove(l);
    }

    void fireLocationChangedEvent()
    {
        if (listenerList!=null)
        {
            Event e = EventBuilder.moduleMoved(this);
            EventChain<ModuleListener> l = listenerList;
            do
            {
                l.getListener().moduleMoved(e);
                l = l.getChain();
            }
            while (l!=null);
        }
    }
    
    void fireModuleRenamedEvent()
    {
        if (listenerList!=null)
        {
            Event e = EventBuilder.moduleRenamed(this);
            EventChain<ModuleListener> l = listenerList;
            do
            {
                l.getListener().moduleMoved(e);
                l = l.getChain();
            }
            while (l!=null);
        }
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
        if (this.name!=name)
        {
            this.name = name;
            /*
            if (ui!=null && name!=null)
            {
                ui.setTitle(name);
            }*/
            fireModuleRenamedEvent();
        }
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

    public int[] getParameterValues()
    {
        int[] values = new int[getParameterCount()];
        for (int i=getParameterCount()-1;i>=0;i--)
        {
            values[i] = getParameter(i).getValue();
        }
        return values;
    }

    public int[] getCustomValues()
    {
        int[] values = new int[getCustomCount()];
        for (int i=getCustomCount()-1;i>=0;i--)
        {
            values[i] = getCustom(i).getValue();
        }
        return values;
    }
    
}
