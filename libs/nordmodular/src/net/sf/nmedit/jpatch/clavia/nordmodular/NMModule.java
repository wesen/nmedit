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

import java.awt.Point;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.event.EventListenerList;

import net.sf.nmedit.jpatch.Connection;
import net.sf.nmedit.jpatch.ConnectionManager;
import net.sf.nmedit.jpatch.Connector;
import net.sf.nmedit.jpatch.ConnectorDescriptor;
import net.sf.nmedit.jpatch.InvalidDescriptorException;
import net.sf.nmedit.jpatch.LightweightIterator;
import net.sf.nmedit.jpatch.Module;
import net.sf.nmedit.jpatch.ModuleDescriptor;
import net.sf.nmedit.jpatch.Parameter;
import net.sf.nmedit.jpatch.ParameterDescriptor;
import net.sf.nmedit.jpatch.event.ModuleContainerListener;
import net.sf.nmedit.jpatch.event.ModuleEvent;
import net.sf.nmedit.jpatch.event.ModuleListener;
import net.sf.nmedit.jpatch.spec.DefaultModuleDescriptor;
import net.sf.nmedit.nmutils.collections.EmptyIterator;


public class NMModule implements Cloneable, Module
{

    private static final String HEIGHT_PROPERTY = "height";
    private static final String PCLASS_PARAMETER = "parameter";
    private static final String PCLASS_CUSTOM = "custom";
    private static final String PCLASS_MORPH = "morph";

    private DefaultModuleDescriptor descriptor;

    private NMConnector[] connectorList;

    private NMParameter[] parameterList;

    private NMParameter[] morphList;

    private Map<ConnectorDescriptor, NMConnector> connectorMap = new HashMap<ConnectorDescriptor, NMConnector>();
    private Map<ParameterDescriptor, Parameter> parameterMap = new HashMap<ParameterDescriptor, Parameter>(); 

    private Custom[]    customList;
    
    private VoiceArea         voiceArea;

    private String            name;

    private int               x;

    private int               y;

    private int               index;
    
    private int height;
    
    //private ModuleUI ui;
    
    public NMModule( DefaultModuleDescriptor descriptor )
    {
        this.descriptor = descriptor;
        init();
    }
    
    public DefaultModuleDescriptor getDescriptor()
    {
        return descriptor;
    }
    
    private final void init()
    {
        DefaultModuleDescriptor ds = getDescriptor();
        height = ds.getIntegerAttribute(HEIGHT_PROPERTY, 1);
        
        this.index = -1;
        this.voiceArea = null;
        this.name = ds.getComponentName();
        
        connectorList = new NMConnector[ds.getConnectorCount()];
        for (int i = ds.getConnectorCount()-1; i >= 0; i--)
        {
            ConnectorDescriptor d = ds.getConnectorDescriptor( i );
            NMConnector c = new NMConnector( d, this );
            connectorList[i] = c;
            connectorMap.put(d, c);
        }

        ParameterDescriptor[] plist = ds.getParameterDescriptorList(PCLASS_PARAMETER);
        parameterList = new NMParameter[plist.length];
        for (int i=plist.length-1;i>=0;i--)
        {
            ParameterDescriptor d = plist[i];
            NMParameter p = new NMParameter( d, this );
            parameterList[i] = p;
            parameterMap.put(d, p);
        }

        plist = ds.getParameterDescriptorList(PCLASS_CUSTOM);
        customList = new Custom[plist.length];
        for (int i=plist.length-1;i>=0;i--)
        {
            ParameterDescriptor d = plist[i];
            Custom p = new Custom( d, this );
            customList[i] = p;
            parameterMap.put(d, p);
        }

        plist = ds.getParameterDescriptorList(PCLASS_MORPH);
        morphList = new NMParameter[plist.length];
        for (int i=plist.length-1;i>=0;i--)
        {
            ParameterDescriptor d = plist[i];
            NMParameter p = new NMParameter( d, this );
            morphList[i] = p;
            parameterMap.put(d, p);
        }
    }
    
    public int getModuleId()
    {
        return getDescriptor().getIndex();
    }

    public NMModule clone()
    {
        try
        {
            NMModule copy = (NMModule) super.clone();
            copy.descriptor = descriptor;
            copy.name = name;
            copy.x = x;
            copy.y = y;
            copy.index = index;
            copy.init();

            for (int i=getParamCount()-1;i>=0;i--)
                copy.getParameter(i).setValue(getParameter(i).getValue());
            for (int i=getCustomCount()-1;i>=0;i--)
                copy.getCustom(i).setValue(getCustom(i).getValue());
            for (int i=getMorphCount()-1;i>=0;i--)
                copy.getMorph(i).setValue(getMorph(i).getValue());
            /*
            for (int i=getLightCount()-1;i>=0;i--)
                copy.getLight(i).setLightOn(getLight(i).isLightOn());*/
            
            return copy;
        }
        catch (CloneNotSupportedException e)
        {
            throw new InternalError(e.getMessage());
        }
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
            c.disconnect();
        }
    }
    
    public void deAssignKnobs()
    {
    }
    
    public void deAssignMorphs()
    {
    }
    
    public void deAssignMidiControllers()
    {
    }
    
    public void makeUseless()
    {
        disconnectCables();
        deAssignKnobs();
        deAssignMorphs();
        deAssignMidiControllers();
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
        vx = Math.max(0, vx);
        vy = Math.max(0, vy);
        
        if (( this.x != vx ) || ( this.y != vy ))
        {            
            this.x = vx;
            this.y = vy;

            if (voiceArea!=null)
                voiceArea.updateSize(this, VoiceArea.MOVE);

            fireLocationChanged();
        }
    }

    void setYWithoutVANotification(int vy)
    {
        vy = Math.max(0, vy);
        if (( this.y != vy ))
        {            
            this.y = vy;

            fireLocationChanged();
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
    
    public int getParameterCount()
    {
        return getParamCount()+getCustomCount()+getMorphCount();
    }
    
    public int getParamCount()
    {
        return parameterList.length;
    }
    
    public int getMorphCount()
    {
        return morphList.length;
    }
    
    public NMParameter getMorph(int index)
    {
        return morphList[index];
    }

    public int getCustomCount()
    {
        return customList.length;
    }

    public int getConnectorCount()
    {
        return connectorList.length;
    }

    public NMParameter getParameter( int index )
    {
        return parameterList[index];
    }

    public Custom getCustom( int index )
    {
        return customList[index];
    }

    public NMConnector getConnector( int index )
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
            // fireModuleRenamedEvent();
        }
    }

    public String getShortName()
    {
        return null;
    }

    public int getID()
    {
        return getDescriptor().getIndex();
    }

    public int getHeight()
    {
        return height;
    }

    public boolean isCvaOnly()
    {
        return getDescriptor().getBooleanAttribute("cva-only", false);
    }

    public boolean isLimited()
    {
        return getLimit()>0;
    }

    public int getLimit()
    {
        return getDescriptor().getIntegerAttribute("limit", -1);
    }

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
        int[] values = new int[getParamCount()];
        for (int i=getParamCount()-1;i>=0;i--)
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

    public int getModuleCount()
    {
        return 0;
    }

    public NMPatch getPatch()
    {
        VoiceArea va = getParent();
        return (va != null) ? va.getPatch() : null;
    }

    public boolean isContainer()
    {
        return false;
    }

    public void setVoiceArea( VoiceArea va )
    {
        this.voiceArea = va;
    }

    public VoiceArea getParent()
    {
        return voiceArea;
    }

    public boolean hasConnections()
    {
        VoiceArea va = getParent();
        if (va != null)
        {
            return va.getConnectionManager().getConnections(this).hasNext();
        }
        return false;
    }

    public boolean hasOutgoingConnections()
    {
        VoiceArea va = getParent();
        if (va != null)
        {
            LightweightIterator<Connection> iter = va.getConnectionManager().getConnections(this);
            while (iter.hasNext())
            {
                Connection c = iter.next();
                if (c.getDestinationModule()!=this || c.getSourceModule()!=this)
                    return true;
            }
        }
        return false;
    }

    public void removeConnections()
    {
        VoiceArea va = getParent();
        if (va!=null)
        {
            LightweightIterator<Connection> li = va.getConnectionManager().getConnections();
            while (li.hasNext())
            {
                li.next();
                li.remove();
            }
        }
    }

    public NMConnector getConnector( ConnectorDescriptor descriptor ) throws InvalidDescriptorException
    {
        NMConnector c = connectorMap.get(descriptor);
        if (c == null)
            throw new InvalidDescriptorException(descriptor);
        return c;
    }

    public Parameter getParameter( ParameterDescriptor descriptor ) throws InvalidDescriptorException
    {
        Parameter p = parameterMap.get(descriptor);
        if (p == null)
            throw new InvalidDescriptorException(descriptor);
        return p;
    }

    public Module createModule( ModuleDescriptor descriptor ) throws InvalidDescriptorException
    {
        return getParent().createModule(descriptor);
    }

    public void add( Module module )
    {
        // no op   
    }

    public void remove( Module module )
    {   
        // no op
    }

    public boolean contains( Module module )
    {
        return false;
    }

    public ConnectionManager getConnectionManager()
    {
        // no manager necessary
        return null;
    }

    public void addModuleContainerListener( ModuleContainerListener l )
    {
        // no op
    }

    public void removeModuleContainerListener( ModuleContainerListener l )
    {
        // no op
    }

    public Iterator<Module> iterator()
    {
        return new EmptyIterator<Module>();
    }

    public void setScreenLocation( int x, int y )
    {
        setLocation(x/255, y/15);
    }

    public void setScreenLocation( Point location )
    {
        setLocation(location.x, location.y);
    }

    public Point getScreenLocation()
    {
        return new Point(getScreenX(), getScreenY());
    }

    public int getScreenX()
    {
        return getX()*255;
    }

    public int getScreenY()
    {
        return getY()*15;
    }
    
    private EventListenerList eventListeners = null;
    private transient ModuleEvent mevent = null;

    public void addModuleListener( ModuleListener l )
    {
        if (eventListeners == null)
            eventListeners = new EventListenerList();
        eventListeners.add(ModuleListener.class, l);
    }

    public void removeModuleListener( ModuleListener l )
    {
        if (eventListeners!=null)
            eventListeners.remove(ModuleListener.class, l);
    }
    
    private ModuleEvent getModuleEvent()
    {
        if (mevent == null)
            mevent = new ModuleEvent(this);
        return mevent;
    }
    
    protected void fireLocationChanged()
    {
        if (eventListeners!=null)
        {
            Object[] listeners = eventListeners.getListenerList();
            for (int i=listeners.length-2;i>=0;i-=2)
            {
                if (listeners[i]==ModuleListener.class)
                {
                    ModuleEvent e = getModuleEvent();
                    ((ModuleListener)listeners[i+1]).moduleMoved(e);
                }
            }
        }
    }

}
