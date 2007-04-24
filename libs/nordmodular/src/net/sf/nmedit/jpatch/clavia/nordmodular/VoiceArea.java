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
 * Created on Apr 7, 2006
 */
package net.sf.nmedit.jpatch.clavia.nordmodular;

import java.util.Iterator;

import javax.swing.event.EventListenerList;

import net.sf.nmedit.jpatch.ComponentDescriptor;
import net.sf.nmedit.jpatch.Connection;
import net.sf.nmedit.jpatch.ConnectionFactory;
import net.sf.nmedit.jpatch.ConnectionManager;
import net.sf.nmedit.jpatch.ConnectionManagerImpl;
import net.sf.nmedit.jpatch.Connector;
import net.sf.nmedit.jpatch.InvalidDescriptorException;
import net.sf.nmedit.jpatch.Module;
import net.sf.nmedit.jpatch.ModuleContainer;
import net.sf.nmedit.jpatch.ModuleDescriptor;
import net.sf.nmedit.jpatch.MoveOperation;
import net.sf.nmedit.jpatch.event.ModuleContainerEvent;
import net.sf.nmedit.jpatch.event.ModuleContainerListener;
import net.sf.nmedit.jpatch.spec.DefaultModuleDescriptor;
import net.sf.nmedit.nmutils.collections.ArrayMap;

public class VoiceArea implements ModuleContainer
{
    
    private static class SimpleConnection implements Connection
    {
        
        private Connector a;        
        private Connector b;

        public SimpleConnection(Connector a, Connector b)
        {
            this.a = a;
            this.b = b;
        }

        public boolean contains(Connector c)
        {
            return c==a||c==b;
        }
        
        public boolean contains(Module m)
        {
            return getDestinationModule()==m || getSourceModule() == m;
        }

        public Connector getDestination()
        {
            return a;
        }

        public Module getDestinationModule()
        {
            return getDestination().getOwner();
        }

        public Connector getSource()
        {
            return b;
        }

        public Module getSourceModule()
        {
            return getSource().getOwner();
        }
        
    }
    
    private static class ConnectionFactoryImpl implements ConnectionFactory
    {
        static ConnectionFactoryImpl instance = new ConnectionFactoryImpl();

        public Connection create(Connector a, Connector b)
        {
            return new SimpleConnection(a, b);
        }
        
    }

    public static final int UPDATE =-1;
    public static final int MOVE = 0;
    public static final int ADD = 1;
    public static final int REMOVE = 2;
    
    private int impWidth;
    private int impHeight;
    private NMPatch patch;
    private ConnectionManager connectionManager = new ConnectionManagerImpl(this, ConnectionFactoryImpl.instance);
    private ArrayMap<NMModule> modules ;
    
    private static final String ATTRIBUTE_CYCLES = "cycles";
    
    // cycles = (cyclesFixedPoint*10^(-cyclesFixedPointShift))
    private long cyclesFixedPoint = 0;
    private int cyclesFixedPointShift = 0; 
    
    public VoiceArea(NMPatch patch)
    {
        modules = new ArrayMap<NMModule>();
        modules.setMinKey(1);
        impWidth = 0;
        impHeight = 0;
        this.patch = patch;
    }

    private void registerCycles(double cycles)
    {
        // first shift cycles to the level of the fixed point value cyclesFixedPoint
        if (cyclesFixedPointShift>0)
            cycles = cycles * Math.pow(10,cyclesFixedPointShift);
        
        // shift10 = 1,10,100,1000,...
        int shift10 = 1;
        while ((Math.ceil(cycles*shift10) != 0))
        {
            shift10*=10;
        }
        
        if (shift10 > 1)
        {
            cyclesFixedPointShift += shift10/10;
            cyclesFixedPoint *= shift10;
            cycles *= shift10;
        }

        cyclesFixedPoint += (int) cycles;
    }

    private void unregisterCycles(double cycles)
    {
        // precondition: cycles are already registered
        cyclesFixedPoint -= (cycles*Math.pow(10,cyclesFixedPointShift));
        
    }

    public double getTotalCycles()
    {
        return cyclesFixedPoint * Math.pow(-10, cyclesFixedPointShift);
    }
    
    private void registerCycles(Module module)
    {
        registerCycles(((Double)module.getDescriptor().getAttribute(ATTRIBUTE_CYCLES)).doubleValue());
    }
    
    private void unregisterCycles(Module module)
    {
        unregisterCycles(((Double)module.getDescriptor().getAttribute(ATTRIBUTE_CYCLES)).doubleValue());
    }
    
    public ConnectionManager getConnectionManager()
    {
        return connectionManager;
    }
    
    public boolean isPolyVoiceArea()
    {
        return patch.getPolyVoiceArea()==this;
    }
    
    public NMPatch getPatch()
    {
        return patch;
    }

    public boolean add( NMModule m )
    {
        if (isPolyVoiceArea() && m.isCvaOnly())
        {
            // can only be added to common voice area
            System.out.println("cva-only");
            return false;
        }
        
        int limit = m.getLimit(); //<=0 means unlimited
        if (limit>0)
        {
            for (NMModule m2 : modules)
            {
                if (m2.getID()==m.getID())
                {
                    limit --;
                    if (limit<=0)
                    {
                        // no more modules of this type allowed
                        return false;
                    }
                }
            }
        }
        
        if (m.getIndex()<0)
        {
            m.setIndex(modules.generateIndex());
        }
        else if (modules.containsKey(m.getIndex()))
        {
            return false;
        }
        
        modules.put(m.getIndex(), m);
        m.setVoiceArea(this);
        updateSize(m, ADD);

        fireModuleAdded(m);
        
        registerCycles(m);
        return true;
    }

    public boolean remove( NMModule m )
    {
        getPatch().getHistory().beginRecord();
        try
        {  
            if (modules.get(m.getIndex())!=m)
                return false;
            modules.remove(m.getIndex());
    
            m.makeUseless();
            updateSize(m, REMOVE);
            fireModuleRemoved(m);
            m.setVoiceArea(null);
            unregisterCycles(m);
            return true;
        }
        finally
        {
            getPatch().getHistory().endRecord();
        }
    }

    /*
    private void adjustImpliedSize()
    {
        Module m;
        int newW=0;
        int newH=0;
        
        for (int i=0;i<map.length;i++)
        {
            m = map[i];
            if (m!=null)
            {
                newW = Math.max(newW, m.getX()+1);
                newH = Math.max(newH, m.getY()+m.getHeight());
            }
        }

        if (newW!=impWidth || newH!=impHeight)
        {
            impWidth = newW;
            impHeight = newH;
            fireVoiceAreaResizedEvent();
        }
    }
    
    private void adjustImpliedSize( Module m, boolean added )
    {
        adjustImpliedSize();
    }

    void locationChanged( Module m )
    {
        adjustImpliedSize(null, false);
    }
*/
    public int getImpliedWidth()
    {
        return impWidth;
    }

    public int getImpliedHeight()
    {
        return impHeight;
    }
    /*
    void connected( NMConnector a, NMConnector b )
    {
        if (listenerList!=null)
        {
            EventChain<VoiceAreaListener> l = listenerList;
            Event e = EventBuilder.cableAdded(a, b);
            do
            {
                l.getListener().cablesAdded(e);
                l = l.getChain();
            }
            while (l!=null);
        }
    }

    void disconnected( NMConnector a, NMConnector b )
    {
        if (listenerList!=null)
        {
            EventChain<VoiceAreaListener> l = listenerList;
            Event e = EventBuilder.cableRemoved(this, a, b);
            do
            {
                l.getListener().cablesRemoved(e);
                l = l.getChain();
            }
            while (l!=null);
        }
    }
    
    public void updateConnection( NMConnector c )
    {
        if (listenerList!=null)
        {
            EventChain<VoiceAreaListener> l = listenerList;
            Event e = EventBuilder.cableGraphUpdate(c);
            do
            {
                l.getListener().cableGraphUpdated(e);
                l = l.getChain();
            }
            while (l!=null);
        }
    }
*/
    /*
    private void setNewSize(int w, int h)
    {
        if ((w!=impWidth) || (h!=impHeight))
        {
            impWidth = w;
            impHeight = h;
            
        //    fireVoiceAreaResizedEvent();
        }
    }*/


    void updateSize( NMModule module, int op )
    {/*
        if (op==REMOVE)
        {
            int w = 0;
            int h = 0;
            for (int i=map.length-1;i>=0;i--)
            {
                w = Math.max(w, module.getX());
                h = Math.max(h, module.getY()+module.getHeight());
            }
            setNewSize(w, h);
            return ;
        }
        
        if (module.getY()<0)
            module.setYWithoutVANotification(0);
        
        NMModule[] temp = tempModuleList != null ? tempModuleList.get() : null;
        boolean updateReference = temp!=null;
        
        if (temp == null || temp.length<map.length)
        {
            temp = new NMModule[map.length];
            updateReference = true;
        }
        else
            Arrays.fill(temp, null);

        int right = 0;
        int bottom = 0;
        int pos = 0;
        int y = module.getY()+module.getHeight();

        for (int i=map.length-1;i>=0;i--)
        {
            NMModule m2 = map[i];
            if (m2!=null)
            {
                right = Math.max(right, m2.getX());
                bottom = Math.max(bottom, m2.getY()+m2.getHeight());
                if (m2!=module && m2.getX()==module.getX()&&(m2.getY()+m2.getHeight()>module.getY()))
                {
                    temp[pos++] = m2;
                }
            }
        }
        right++;

        Arrays.<NMModule>sort(temp, 0, pos, YOrder.instance);
        // temp are all modules in the same column and below the specified module

        for (int i=0;i<pos;i++)
        {
             NMModule m2 = temp[i];
             if (m2.getY()<y)
               m2.setYWithoutVANotification(y);
             y = m2.getY()+m2.getHeight();
        }
                       
        if (updateReference)
            tempModuleList = new WeakReference<NMModule[]>(temp);
        
        setNewSize(right, Math.max(y, bottom));
        */
    }
    
/*
    private WeakReference<NMModule[]> tempModuleList = null;
    private static class YOrder implements Comparator<NMModule>
    {
        final static Comparator<NMModule> instance = new YOrder();
        
        public int compare( NMModule o1, NMModule o2 )
        {
            return o1.getY()-o2.getY();
        }
    }
*/

    public NMModule createModule( int moduleId ) throws InvalidDescriptorException
    {
        ModuleDescriptor md = getPatch().getModules().get(moduleId);
        if (md == null)
            throw new InvalidDescriptorException("Module descriptor for id not found: "+moduleId);
        return createModule(md);
    }
    
    public NMModule createModule( Object moduleID )
    {
        throw new UnsupportedOperationException();
    }

    public boolean isContainer()
    {
        return true;
    }

    public NMModule createModule( ModuleDescriptor descriptor ) 
    {
        return new NMModule((DefaultModuleDescriptor)descriptor);
    }

    public void add( Module module )
    {
        this.add((NMModule)module);
    }

    public void remove( Module module )
    {
        this.remove((NMModule)module);
    }

    public int getModuleCount()
    {
        return modules.size();
    }

    public boolean contains( Module module )
    {
        return modules.contains(module);
    }
    
    private EventListenerList eventListeners = null;
    private transient ModuleContainerEvent mce = null;
    
    private ModuleContainerEvent getModuleContainerEvent()
    {
        if (mce == null)
            mce = new ModuleContainerEvent(this);
        return mce;
    }

    public void addModuleContainerListener( ModuleContainerListener l )
    {
        if (eventListeners == null)
            eventListeners = new EventListenerList();
        eventListeners.add(ModuleContainerListener.class, l);
    }

    public void removeModuleContainerListener( ModuleContainerListener l )
    {
        if (eventListeners != null)
            eventListeners.remove(ModuleContainerListener.class, l);
    }

    protected void fireModuleAdded(NMModule m)
    {
        ModuleContainerEvent event = null;
        if (eventListeners != null)
        {
            Object[] list = eventListeners.getListenerList();
            for (int i=list.length-2;i>=0;i-=2)
            {
                if (list[i]==ModuleContainerListener.class)
                {
                    if (event == null)
                    {
                        event = getModuleContainerEvent();
                        event.setModule(m);
                    }
                    ((ModuleContainerListener) list[i+1]).moduleAdded(event);
                }
            }
        }
    }
    
    protected void fireModuleRemoved(NMModule m)
    {
        ModuleContainerEvent event = null;
        if (eventListeners != null)
        {
            Object[] list = eventListeners.getListenerList();
            for (int i=list.length-2;i>=0;i-=2)
            {
                if (list[i]==ModuleContainerListener.class)
                {
                    if (event == null)
                    {
                        event = getModuleContainerEvent();
                        event.setModule(m);
                    }
                    ((ModuleContainerListener) list[i+1]).moduleRemoved(event);
                }
            }
        }
    }
    
    public String getName()
    {
        return getClass().getName();
    }

    public ComponentDescriptor getDescriptor()
    {
        // TODO
        return null;
    }

    public Iterator<Module> iterator()
    {
        return new Iterator<Module>()
        {

            Iterator<NMModule> i = modules.iterator();
            
            public boolean hasNext()
            {
                return i.hasNext();
            }

            public Module next()
            {
                return i.next();
            }

            public void remove()
            {
                i.remove();
            }
            
        };
    }

    public NMModule getModule( int index )
    {
        return modules.get(index);
    }
    
    public String toString()
    {
        return ((isPolyVoiceArea()) ? "PolyVoiceArea":"CommonVoiceArea")
        +"[modules="+getModuleCount()+"]";
            
    }

    public MoveOperation createMoveOperation()
    {
        return new NMMoveOperation(this);
    }

}
