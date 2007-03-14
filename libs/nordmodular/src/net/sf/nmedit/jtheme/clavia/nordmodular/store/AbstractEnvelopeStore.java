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

import net.sf.nmedit.jpatch.Module;
import net.sf.nmedit.jpatch.Parameter;
import net.sf.nmedit.jtheme.JTContext;
import net.sf.nmedit.jtheme.JTException;
import net.sf.nmedit.jtheme.clavia.nordmodular.JTEnvelopeDisplay;
import net.sf.nmedit.jtheme.component.JTComponent;
import net.sf.nmedit.jtheme.component.JTParameterControlAdapter;
import net.sf.nmedit.jtheme.store.ControlStore;
import net.sf.nmedit.jtheme.store.helpers.ParameterDescriptorHelper;

import org.jdom.Element;

public abstract class AbstractEnvelopeStore extends ControlStore
{

    protected ParameterDescriptorHelper decayParameterHelper;
    private ParameterDescriptorHelper holdParameterHelper;
    private ParameterDescriptorHelper sustainParameterHelper;
    private ParameterDescriptorHelper releaseParameterHelper;
    private ParameterDescriptorHelper attackTypeParameterHelper;
    private ParameterDescriptorHelper inverseParameterHelper;
    
    protected AbstractEnvelopeStore(Element element)
    {
        super(element);
    }

    protected void initDescriptors(Element element)
    {
        parameterDescriptorHelper = ParameterDescriptorHelper.createHelper(element.getChild("attack"));
        decayParameterHelper = ParameterDescriptorHelper.createHelper(element.getChild("decay"));
        holdParameterHelper = ParameterDescriptorHelper.createHelper(element.getChild("hold"));
        sustainParameterHelper = ParameterDescriptorHelper.createHelper(element.getChild("sustain"));
        releaseParameterHelper = ParameterDescriptorHelper.createHelper(element.getChild("release"));
        attackTypeParameterHelper = ParameterDescriptorHelper.createHelper(element.getChild("attack-type"));
        inverseParameterHelper = ParameterDescriptorHelper.createHelper(element.getChild("inverse"));
    }

    @Override
    public abstract JTComponent createComponent(JTContext context) throws JTException;

    protected void link(JTContext context, JTComponent component, Module module)
      throws JTException
    {
        Parameter attack = parameterDescriptorHelper.lookup(module);
        Parameter decay = decayParameterHelper.lookup(module);
        Parameter hold = holdParameterHelper.lookup(module);
        Parameter sustain = sustainParameterHelper.lookup(module);
        Parameter release = releaseParameterHelper.lookup(module);
        Parameter attackType = attackTypeParameterHelper.lookup(module);
        Parameter Inverse = inverseParameterHelper.lookup(module);
        
        JTEnvelopeDisplay disp = (JTEnvelopeDisplay) component;

        if (attack != null) disp.setAttackAdapter(new JTParameterControlAdapter(attack));
        if (decay != null) disp.setDecayAdapter(new JTParameterControlAdapter(decay));
        if (hold != null) disp.setHoldAdapter(new JTParameterControlAdapter(hold));
        if (sustain != null) disp.setSustainAdapter(new JTParameterControlAdapter(sustain));
        if (release != null) disp.setReleaseAdapter(new JTParameterControlAdapter(release));
        if (attackType != null) disp.setAttackTypeAdapter(new JTParameterControlAdapter(attackType));
        if (Inverse != null) disp.setInverseAdapter(new JTParameterControlAdapter(Inverse));
    }
    
    protected void link2(JTContext context, JTComponent component, Module module, Parameter parameter)
    {
        throw new UnsupportedOperationException();
    }
    
}
