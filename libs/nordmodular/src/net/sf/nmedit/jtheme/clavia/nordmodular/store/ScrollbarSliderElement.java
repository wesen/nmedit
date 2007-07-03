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

import java.awt.Component;
import java.awt.Container;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.sf.nmedit.jpatch.PModule;
import net.sf.nmedit.jpatch.PModuleDescriptor;
import net.sf.nmedit.jpatch.PParameter;
import net.sf.nmedit.jtheme.JTContext;
import net.sf.nmedit.jtheme.JTException;
import net.sf.nmedit.jtheme.clavia.nordmodular.NMNoteSeqEditor;
import net.sf.nmedit.jtheme.clavia.nordmodular.NMScrollbar;
import net.sf.nmedit.jtheme.component.JTComponent;
import net.sf.nmedit.jtheme.component.JTControlAdapter;
import net.sf.nmedit.jtheme.component.JTSlider;
import net.sf.nmedit.jtheme.store.StorageContext;
import net.sf.nmedit.jtheme.store2.AbstractElement;
import net.sf.nmedit.jtheme.store2.SliderElement;

import org.jdom.Attribute;
import org.jdom.Element;

public class ScrollbarSliderElement extends SliderElement
{

    private static final long serialVersionUID = -3341440830935958705L;
    private String noteSequencerName;
    
    public static AbstractElement createElement(StorageContext context, Element element)
    {
        ScrollbarSliderElement e = new ScrollbarSliderElement();
        e.initElement(context, element);
        e.checkDimensions();
        e.checkLocation();
        return e;
    }

    @Override
    protected void initAttributes(StorageContext context, Attribute att)
    {
        if ("note-sequencer".equals(att.getName()))
        {
            this.noteSequencerName = att.getValue();
        }
        else
        {
            super.initAttributes(context, att);
        }
        
    }
    @Override
    public JTComponent createComponent(JTContext context,
            PModuleDescriptor descriptor, PModule module) throws JTException
    {
        NMScrollbar slider = context.createComponentInstance(NMScrollbar.class);
        /*
        JTBasicSliderScrollbarUI ui = JTBasicSliderScrollbarUI.createUI(slider);
        slider.setUI(ui);
        */
        setBounds(slider);
        setName(slider);

        if (noteSequencerName != null)
            slider.setAdapter(new ScrollAdapter(noteSequencerName, slider));
        
        setParameter(slider, descriptor, module);
        slider.setOrientation(orientation);
        return slider;
    }
    
    private static class ScrollAdapter implements JTControlAdapter, ChangeListener
    {
        
        private ChangeListener changeListener;
        private JTSlider scrollbar;
        private JTComponent component;
        private NMNoteSeqEditor noteEditor;
        private boolean lookup = true;
        private String noteSequencerName;

        public ScrollAdapter(String noteSequencerName, JTSlider scrollbar)
        {
            this.noteSequencerName = noteSequencerName;
            this.scrollbar = scrollbar;
        }
        
        private boolean lookupNoteEditor()
        {
            if (noteEditor == null && lookup && scrollbar != null)
            {
                lookup = false;
                
                Container module = scrollbar.getParent();
                for (int i=module.getComponentCount()-1;i>=0;i--)
                {
                    Component c = module.getComponent(i);
                    String n = c.getName();
                    if (n != null && n.equals(noteSequencerName)
                            && (c instanceof NMNoteSeqEditor))
                    {
                        noteEditor = (NMNoteSeqEditor) c;
                        noteEditor.addChangeListener(this);
                        break;
                    }
                }
                
            }
            return noteEditor != null;
        }

        public void setChangeListener(ChangeListener l)
        {
            this.changeListener = l;
        }

        public ChangeListener getChangeListener()
        {
            return changeListener;
        }

        public JTComponent getComponent()
        {
            return component;
        }

        public void setComponent(JTComponent c)
        {
            this.component = c;
            this.noteEditor = null;
            this.lookup = true;
        }

        public int getDefaultValue()
        {
            return 0;
        }
        
        public int getMaxValue()
        {
            if (lookupNoteEditor())
            {
                return noteEditor.getMaxTranslation();
            }
            return 1;
        }

        public int getMinValue()
        {
            return 0;
        }

        public int getValue()
        {
            return lookupNoteEditor() ? noteEditor.getTranslation() : 0;
        }

        public double getNormalizedValue()
        {
            if (lookupNoteEditor())
            {
                int t = noteEditor.getTranslation();
                int m = noteEditor.getMaxTranslation();
                if (m<=0) m = 1;
                return t/(double)m;
            }
            return 0;
        }

        public void setNormalizedValue(double value)
        {
            if (lookupNoteEditor())
            {
                int t = (int) (value*noteEditor.getMaxTranslation());
                noteEditor.setTranslation(t);
                notifyChangeListener();
            }
        }

        public void setValue(int value)
        {
            if (lookupNoteEditor())
            {
                noteEditor.setTranslation(value);
                notifyChangeListener();
            }
        } 

        private void notifyChangeListener()
        {
            if (changeListener != null)
                changeListener.stateChanged(new ChangeEvent(this));
        }

        public PParameter getParameter()
        {
            return null;
        }

        public void setDefaultValue(int defaultValue)
        {
            // no op
        }

        public void setMaxValue(int maxValue)
        {
            // no op
        }

        public void setMinValue(int minValue)
        {
            // no op
        }

        public void stateChanged(ChangeEvent e)
        {
            scrollbar.repaint();
        }
        
    }
    
}
