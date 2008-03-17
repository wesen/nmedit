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
package net.sf.nmedit.jtheme.cable;

import java.awt.Graphics2D;
import java.util.Collection;

import javax.swing.JComponent;

import net.sf.nmedit.jpatch.PModule;
import net.sf.nmedit.jtheme.component.JTConnector;

public interface JTCableManager extends Iterable<Cable>
{

    void shake();
    void add(Cable cable);
    void remove(Cable cable);
    void remove(Cable[] cables);
    void remove(Collection<Cable> cables);

    void update(Cable cable);
    void update(Cable[] cables);
    void update(Collection<Cable> cables);
    
    int size();
    void clear();
    
 //   void getVisible(Collection<Cable> c);
    
    void getCables(Collection<Cable> c, PModule module);
    void getCables(Collection<Cable> c, Collection<? extends PModule> modules);
    /*
    Iterator<Cable> getCables();
*//*
    void setVisibleRegion(int x, int y, int width, int height);
    void setVisibleRegion(Rectangle r);
    
    Rectangle getVisibleRegion();
    Rectangle getVisibleRegion(Rectangle r);
*/
    void paintCables(Graphics2D g2, CableRenderer cableRenderer);
    void paintCables(Graphics2D g2);
    
    void setCableRenderer(CableRenderer cr);
    CableRenderer getCableRenderer();
  /*  
    void notifyRepaintManager();
*/
    void setView(JComponent view);
    JComponent getView();

    Cable createCable(JTConnector source, JTConnector destination);
    JComponent getOwner();
    void setOwner(JComponent owner);

    /**
     * Important setAutoRepaintDisabled() must follow a call to
     * clearAutoRepaintDisabled(). Always use following construct:
     * JTCableManager cm;
     * try
     * {
     *    cm.setAutoRepaintDisabled();
     * }
     * finally
     * {
     *    cm.clearAutoRepaintDisabled();
     * }
     *
     */
    void setAutoRepaintDisabled();
    void clearAutoRepaintDisabled();
    boolean isAutoRepaintEnabled();
    
}

