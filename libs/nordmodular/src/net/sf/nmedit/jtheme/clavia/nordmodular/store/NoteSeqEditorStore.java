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

import java.io.Serializable;

import net.sf.nmedit.jpatch.PModule;
import net.sf.nmedit.jpatch.PModuleDescriptor;
import net.sf.nmedit.jpatch.PParameter;
import net.sf.nmedit.jtheme.JTContext;
import net.sf.nmedit.jtheme.JTException;
import net.sf.nmedit.jtheme.clavia.nordmodular.NMNoteSeqEditor;
import net.sf.nmedit.jtheme.component.JTComponent;
import net.sf.nmedit.jtheme.component.JTParameterControlAdapter;
import net.sf.nmedit.jtheme.store.StorageContext;
import net.sf.nmedit.jtheme.store2.AbstractMultiParameterElement;

import org.jdom.Element;

public class NoteSeqEditorStore extends AbstractMultiParameterElement
implements Serializable
{

    /**
     * 
     */
    private static final long serialVersionUID = -3406543447263312706L;
    private static final String[] PARAMETERS = {
        "step1",
        "step2",
        "step3",
        "step4",
        "step5",
        "step6",
        "step7",
        "step8",
        "step9",
        "step10",
        "step11",
        "step12",
        "step13",
        "step14",
        "step15",
        "step16",
    };
    
    protected NoteSeqEditorStore()
    {
        super(PARAMETERS);
    }

    protected void link(JTComponent component, PModule module)
    {
        NMNoteSeqEditor ed = (NMNoteSeqEditor) component;
        
        for (int i=0;i<componentIdList.length;i++)
        {
            PParameter p = module.getParameterByComponentId(componentIdList[i]);
            if (p != null)
                ed.setControlAdapter(i, new JTParameterControlAdapter(p));
        }
    }

    public static NoteSeqEditorStore createElement(StorageContext context, Element element)
    {
        NoteSeqEditorStore e = new NoteSeqEditorStore();
        e.initElement(context, element);
        e.checkDimensions();
        e.checkLocation();
        return e;
    }

    @Override
    public JTComponent createComponent(JTContext context, PModuleDescriptor descriptor, PModule module)
    throws JTException
    {
        JTComponent component = context.createComponentInstance(NMNoteSeqEditor.class);
        setName(component);
        setBounds(component);
        link(component, module);
        return component;
    }

}
