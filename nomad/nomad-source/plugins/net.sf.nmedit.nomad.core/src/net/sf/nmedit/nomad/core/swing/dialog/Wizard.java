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
 * Created on Oct 31, 2006
 */
package net.sf.nmedit.nomad.core.swing.dialog;

import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.event.ChangeListener;

public interface Wizard
{

    public final static int BACK = 0;
    public final static int CANCEL = 1;
    public final static int NEXTFINISH = 2;
    public final static int DONE = 3;
    public final static int CANCELED = 4;
    
    JComponent createCurrentUI();
    
    String getCurrentDescription();
    
    void cancel();
    
    void next();
    
    void back();
    
    void finish();
    
    boolean canGoBack();

    boolean canGoNext();

    boolean canFinish();
    
    void addChangeListener(ChangeListener l);
    
    void removeChangeListener(ChangeListener l);
    
    void addActionListener(ActionListener l);
    
    void removeActionListener(ActionListener l);
    
}
