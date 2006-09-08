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
package net.sf.nmedit.jpatch.clavia.nordmodular.v3_03;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.Comparator;

import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.event.Event;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.event.EventBuilder;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.event.EventChain;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.event.ModuleListener;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.event.VoiceAreaListener;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.misc.ModuleSet;

public class VoiceArea extends ModuleSet implements ModuleListener
{

    public static final int UPDATE =-1;
    public static final int MOVE = 0;
    public static final int ADD = 1;
    public static final int REMOVE = 2;
    
    private int impWidth;
    private int impHeight;
    private Patch patch;
    private double cyclesTotal = 0;
    
    public VoiceArea(Patch patch)
    {
        impWidth = 0;
        impHeight = 0;
        this.patch = patch;
    }
    
    private EventChain<VoiceAreaListener> listenerList = null;
    
    public void addVoiceAreaListener(VoiceAreaListener l)
    {
        listenerList = new EventChain<VoiceAreaListener>(l, listenerList);
    }
    
    public void removeVoiceAreaListener(VoiceAreaListener l)
    {
        if (listenerList!=null)
            listenerList = listenerList.remove(l);
    }
    
    void fireModuleAddedEvent(Module m)
    {
        if (listenerList!=null)
        {
            EventChain<VoiceAreaListener> l = listenerList;
            Event e = EventBuilder.moduleAdded(m);
            do
            {
                l.getListener().moduleAdded(e);
                l = l.getChain();
            }
            while (l!=null);
        }
    }
    
    void fireModuleRemovedEvent(Module m)
    {
        if (listenerList!=null)
        {
            EventChain<VoiceAreaListener> l = listenerList;
            Event e = EventBuilder.moduleRemoved(this, m);
            do
            {
                l.getListener().moduleRemoved(e);
                l = l.getChain();
            }
            while (l!=null);
        }
    }
    
    void fireVoiceAreaResizedEvent()
    {
        if (listenerList!=null)
        {
            EventChain<VoiceAreaListener> l = listenerList;
            Event e = EventBuilder.voiceAreaResized(this);
            do
            {
                l.getListener().voiceAreaResized(e);
                l = l.getChain();
            }
            while (l!=null);
        }
    }

    public boolean isPolyVoiceArea()
    {
        return patch.getPolyVoiceArea()==this;
    }
    
    public Patch getPatch()
    {
        return patch;
    }

    private int updateCount = 0;
    
    public void beginUpdate()
    {
        if (updateCount == 0)
        {
            // fire begin update
        }
        updateCount ++;
    }

    public void endUpdate()
    {
        if (updateCount<=0)
            throw new IllegalStateException();
        updateCount --;
        if (updateCount==0)
        {
            // fire end update
        }
    }
    
    public boolean add( Module m )
    {
        if (isPolyVoiceArea() && m.isCvaOnly())
        {
            // can only be added to common voice area
            return false;
        }
        
        int limit = m.getLimit(); //<=0 means unlimited
        if (limit>0)
        {
            for (Module m2 : this)
                if (m2.getID()==m.getID())
                    limit --;
            if (limit<=0)
            {
                // no more modules of this type allowed
                return false;
            }
        }
        
        if (super.add(m))
        {
            beginUpdate();

            m.setVoiceArea( this );
            updateSize(m, ADD);

            cyclesTotal += m.getDefinition().getCycles();
            patch.setProperty( isPolyVoiceArea() ? Patch.VAPOLY_CYCLES : Patch.VACOMMON_CYCLES, cyclesTotal);
            fireModuleAddedEvent(m);
            m.addModuleListener(this);

            endUpdate();
            
            return true;
        }
        else
        {
            return false;
        }
    }

    public boolean remove( Module m )
    {
        if (super.remove(m))
        {
            m.makeUseless();
            m.removeModuleListener(this);  
            updateSize(m, REMOVE); 
            cyclesTotal -= m.getDefinition().getCycles();
            patch.setProperty( isPolyVoiceArea() ? Patch.VAPOLY_CYCLES : Patch.VACOMMON_CYCLES, cyclesTotal);
            fireModuleRemovedEvent(m);
            return true;
        }
        else
        {
            return false;
        }
    }

    public void updateCycles()
    {
        double c = 0;
        for (Module m : this)
            c+=m.getDefinition().getCycles();
        cyclesTotal = c;
        patch.setProperty( isPolyVoiceArea() ? Patch.VAPOLY_CYCLES : Patch.VACOMMON_CYCLES, cyclesTotal);
        // TODO event
    }
    
    public double getCyclesTotal()
    {
        return cyclesTotal;
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
    
    void connected( Connector a, Connector b )
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

    void disconnected( Connector a, Connector b )
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
    
    public void updateConnection( Connector c )
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

    public void moduleRenamed( Event e )
    {
    }

    public void moduleMoved( Event e )
    {
        // we are notified directly
    }
    
    private void setNewSize(int w, int h)
    {
        if ((w!=impWidth) || (h!=impHeight))
        {
            impWidth = w;
            impHeight = h;
            fireVoiceAreaResizedEvent();
        }
    }


    void updateSize( Module module, int op )
    {
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
        
        Module[] temp = tempModuleList != null ? tempModuleList.get() : null;
        boolean updateReference = temp!=null;
        
        if (temp == null || temp.length<map.length)
        {
            temp = new Module[map.length];
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
            Module m2 = map[i];
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

        Arrays.<Module>sort(temp, 0, pos, YOrder.instance);
        // temp are all modules in the same column and below the specified module

        for (int i=0;i<pos;i++)
        {
             Module m2 = temp[i];
             if (m2.getY()<y)
               m2.setYWithoutVANotification(y);
             y = m2.getY()+m2.getHeight();
        }
                       
        if (updateReference)
            tempModuleList = new WeakReference<Module[]>(temp);
        
        setNewSize(right, Math.max(y, bottom));
    }
    

    private WeakReference<Module[]> tempModuleList = null;
    private static class YOrder implements Comparator<Module>
    {
        final static Comparator<Module> instance = new YOrder();
        
        public int compare( Module o1, Module o2 )
        {
            return o1.getY()-o2.getY();
        }
    }

}
