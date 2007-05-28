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
package net.sf.nmedit.jtheme.clavia.nordmodular.store;

import net.sf.nmedit.jpatch.PParameter;
import net.sf.nmedit.jpatch.PModule;
import net.sf.nmedit.jtheme.JTContext;
import net.sf.nmedit.jtheme.JTException;
import net.sf.nmedit.jtheme.clavia.nordmodular.NMNoteSeqEditor;
import net.sf.nmedit.jtheme.component.JTComponent;
import net.sf.nmedit.jtheme.component.JTParameterControlAdapter;
import net.sf.nmedit.jtheme.store.ControlStore;
import net.sf.nmedit.jtheme.store.StorageContext;

import org.jdom.Element;

public class NoteSeqEditorStore extends ControlStore
{

    protected String[] idlist;
    
    protected NoteSeqEditorStore(Element element)
    {
        super(element);
    }

    public static NoteSeqEditorStore create(StorageContext context, Element element)
    {
        return new NoteSeqEditorStore(element);
    }
    
    protected void initDescriptors(Element element)
    {
        idlist = new String[16];
        for (int i=0;i<16;i++)
        {
            idlist[i] = lookupChildElementComponentId("step"+(i+1));
        }
    }


    protected void link(JTContext context, JTComponent component, PModule module)
      throws JTException
    {
        NMNoteSeqEditor ed = (NMNoteSeqEditor) component;
        
        for (int i=0;i<idlist.length;i++)
        {
            PParameter p = module.getParameterByComponentId(idlist[i]);
            if (p != null)
            {
                ed.setControlAdapter(i, new JTParameterControlAdapter(p));
            }
        }
    }
    
    @Override
    public JTComponent createComponent(JTContext context) throws JTException
    {
        JTComponent component = context.createComponentInstance(NMNoteSeqEditor.class);
        applyName(component);
        applyLocation(component);
        applySize(component);
        return component;
    }

}
