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
package net.sf.nmedit.nomad.patch.virtual;

import net.sf.nmedit.nomad.patch.virtual.event.EventChain;
import net.sf.nmedit.nomad.patch.virtual.event.EventListener;
import net.sf.nmedit.nomad.patch.virtual.event.ModuleEvent;
import net.sf.nmedit.nomad.patch.virtual.event.VoiceAreaEvent;
import net.sf.nmedit.nomad.patch.virtual.misc.ModuleSet;

public class VoiceArea extends ModuleSet implements EventListener<ModuleEvent>
{

    private int impWidth;
    private int impHeight;
    private Patch patch;
    
    private EventChain<VoiceAreaEvent> listenerList;
    private static VoiceAreaEvent eventMessage = new VoiceAreaEvent();

    public VoiceArea(Patch patch)
    {
        listenerList = null;
        impWidth = 0;
        impHeight = 0;
        this.patch = patch;
    }

    public void addListener(EventListener<VoiceAreaEvent> l)
    {
        listenerList = new EventChain<VoiceAreaEvent>(l, listenerList);
    }

    public void removeListener(EventListener<VoiceAreaEvent> l)
    {
        if (listenerList!=null)
            listenerList = listenerList.remove(l);
    }
    
    void fireEvent(VoiceAreaEvent e)
    {
        if (listenerList!=null)
            listenerList.fireEvent(e);
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
            eventMessage.beginUpdate(this);
            fireEvent(eventMessage);
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
            eventMessage.endUpdate(this);
            fireEvent(eventMessage);
        }
    }
    
    public boolean add( Module m )
    {
        if (super.add(m))
        {
            beginUpdate();
            
            m.setVoiceArea( this );
            adjustImpliedSize( m, true );
            locationChanged(m);
            
            eventMessage.moduleAdded(this, m);
            fireEvent(eventMessage);

            m.addListener(this);

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
            adjustImpliedSize( m, false );
            m.removeListener(this);    
            eventMessage.moduleRemoved(this, m);
            fireEvent(eventMessage);
            return true;
        }
        else
        {
            return false;
        }
    }

    public void event( ModuleEvent event )
    {
        if (event.getID() == ModuleEvent.MODULE_MOVED)
        {
            adjustImpliedSize();
        }
    }
    
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
            eventMessage.resized(this);
            fireEvent(eventMessage);
        }
    }
    
    private void adjustImpliedSize( Module m, boolean added )
    {
        int right = m.getX() + 1;
        int bottom = m.getY() + m.getHeight();

        int oldWidth = impWidth;
        int oldHeight = impHeight;
        
        if (added)
        {
            impWidth = Math.max( impWidth, right );
            impHeight = Math.max( impHeight, bottom );
        }
        else
        {
            // check if the module is on the border line
            if (impWidth <= right || impHeight <= bottom)
            {
                impWidth = size() > 0 ? 1 : 0;
                impHeight = 0;

                for (Module mm : this)
                {
                    impWidth = Math.max( impWidth, mm.getX() );
                    impHeight = Math.max( impHeight, mm.getY() + mm.getHeight() );
                }
            }
        }
        
        if (oldWidth!=impWidth || oldHeight!=impHeight)
        {
            eventMessage.resized(this);
            fireEvent(eventMessage);
        }
    }

    public int getImpliedWidth()
    {
        return impWidth;
    }

    public int getImpliedHeight()
    {
        return impHeight;
    }

    void locationChanged( Module m )
    {
        // Search modules that have to be reordered.
        // These modules are:
        // - in the same column
        // - intersecting with the module
        // - below the intersected module
        // - ...
        
        // in the same column the following cases appear for a module c
        // - c above module      : c.getY()+c.getHeight()<=m.getY()
        // - c below module      : c.getY()>m.getY()+m.getHeight()
        // - c intersects module : else (if c.getY()<m.getY() : moveY(m))
        
        if (m.getY()<0)
        {
            m.setYWithoutEventNotification(0);
        }
        
        int mbottom = m.getY()+m.getHeight();
        int cbottom ;

        Module[] moveList = null;
        int mlSize = 0;
        int moveMod= 0;
        
        // find intersection
        for (int i=size()-1;i>=0;i--)
        {
            Module c = map[i];
            
            if ((c!=null) && (c.getX() == m.getX()) && (c!=m))
            {
                cbottom = c.getY()+c.getHeight();
                
                if (m.getY()>c.getY() && m.getY()<=cbottom)
                {
                    // c intersects m
                    moveMod = cbottom;
                }
                else if (m.getY()<=c.getY())
                {
                    if (moveList == null)
                    {
                        // => (mlSize == 0)
                        
                        // create the move list with space
                        // for each of the following modules
                        moveList = new Module[i+1];

                        // c is below any other listed module
                        // so we append it to the end
                        moveList[0] = c;
                        mlSize = 1;
                    }
                    else
                    {
                        // c is between other modules
                        // we have to insert it at the correct place
                        for (int j=mlSize-1;j>=0;j--)
                        {
                            // move current field
                            moveList[j+1] = moveList[j]; 
                            if ((moveList[j].getY()<c.getY()))
                            {
                                // insert
                                moveList[j+1] = c;
                                break;
                            }
                            else if (j==0)
                            {
                                moveList[j] = c;
                                break;
                            }
                        }
                        
                        mlSize ++;
                    }
                }
            }
        }
        
        if (moveMod>0)
        {
            m.setYWithoutEventNotification(moveMod);
            // update mbottom
            mbottom = m.getY()+m.getHeight();
        }
        
        if (mlSize>0)  // implies moveList!=null
        {            
            // we have modules that should be moved down
            // the modules are already sorted
            
            int ylimit = mbottom;
            Module c;
            
            for (int i=0;i<mlSize;i++)
            {
                c = moveList[i];
                if (c.getY()<ylimit)
                {
                    // set y value
                    c.setYWithoutEventNotification(ylimit);
                    c.fireLocationChangedEvent();

                    // set next y limit
                    ylimit = c.getY()+c.getHeight();
                }
                else
                {
                    // no further intersection => done
                 //   break ;
                }
            }
        }
        
    }

    void connected( Connector a, Connector b )
    {
        eventMessage.connected(this, a, b);
        fireEvent(eventMessage);
    }

    void disconnected( Connector a, Connector b )
    {
        eventMessage.disconnected(this, a, b);  
        fireEvent(eventMessage); 
    }

    public void updateConnection( Connector c )
    {
        eventMessage.updateConnection(c);
        fireEvent(eventMessage);
    }

}
