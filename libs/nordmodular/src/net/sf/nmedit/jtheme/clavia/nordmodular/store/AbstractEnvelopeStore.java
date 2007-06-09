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

import net.sf.nmedit.jpatch.PModule;
import net.sf.nmedit.jpatch.PModuleDescriptor;
import net.sf.nmedit.jpatch.PParameter;
import net.sf.nmedit.jtheme.JTContext;
import net.sf.nmedit.jtheme.JTException;
import net.sf.nmedit.jtheme.clavia.nordmodular.JTEnvelopeDisplay;
import net.sf.nmedit.jtheme.component.JTParameterControlAdapter;
import net.sf.nmedit.jtheme.store2.AbstractMultiParameterElement;

public abstract class AbstractEnvelopeStore extends AbstractMultiParameterElement
{

    private static final String[] PARAMETERS = {"attack", "decay", "hold",
        "sustain", "release", "attack-type", "inverse"};

    protected AbstractEnvelopeStore()
    {
        super(PARAMETERS);
    }

    @Override
    public JTEnvelopeDisplay createComponent(JTContext context, PModuleDescriptor descriptor, PModule module)
        throws JTException
    {
        JTEnvelopeDisplay component = (JTEnvelopeDisplay) context.createComponentInstance(JTEnvelopeDisplay.class);
        setName(component);
        setBounds(component);
        link(component, module);
        return component; 
    }

    protected void link(JTEnvelopeDisplay disp, PModule module)
      throws JTException
    {
        PParameter attack = module.getParameterByComponentId(componentIdList[0]);
        PParameter decay = module.getParameterByComponentId(componentIdList[1]);
        PParameter hold = module.getParameterByComponentId(componentIdList[2]);
        PParameter sustain = module.getParameterByComponentId(componentIdList[3]);
        PParameter release = module.getParameterByComponentId(componentIdList[4]);
        PParameter attackType = module.getParameterByComponentId(componentIdList[5]);
        PParameter Inverse = module.getParameterByComponentId(componentIdList[6]);
        
        if (attack != null) disp.setAttackAdapter(new JTParameterControlAdapter(attack));
        if (decay != null) disp.setDecayAdapter(new JTParameterControlAdapter(decay));
        if (hold != null) disp.setHoldAdapter(new JTParameterControlAdapter(hold));
        if (sustain != null) disp.setSustainAdapter(new JTParameterControlAdapter(sustain));
        if (release != null) disp.setReleaseAdapter(new JTParameterControlAdapter(release));
        if (attackType != null) disp.setAttackTypeAdapter(new JTParameterControlAdapter(attackType));
        if (Inverse != null) disp.setInverseAdapter(new JTParameterControlAdapter(Inverse));
    }
    
}
