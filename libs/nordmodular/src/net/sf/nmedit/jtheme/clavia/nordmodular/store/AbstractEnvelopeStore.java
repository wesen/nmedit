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
import net.sf.nmedit.jpatch.PParameter;
import net.sf.nmedit.jtheme.JTContext;
import net.sf.nmedit.jtheme.JTException;
import net.sf.nmedit.jtheme.clavia.nordmodular.JTEnvelopeDisplay;
import net.sf.nmedit.jtheme.component.JTComponent;
import net.sf.nmedit.jtheme.component.JTParameterControlAdapter;
import net.sf.nmedit.jtheme.store.ControlStore;

import org.jdom.Element;

public abstract class AbstractEnvelopeStore extends ControlStore
{

    private String decayParameterId;
    private String holdParameterId;
    private String sustainParameterId;
    private String releaseParameterId;
    private String attackTypeParameterId;
    private String inverseParameterId;
    
    protected AbstractEnvelopeStore(Element element)
    {
        super(element);
    }

    protected void initDescriptors()
    {
        parameterId = lookupChildElementComponentId("attack");
        decayParameterId = lookupChildElementComponentId("decay");
        holdParameterId = lookupChildElementComponentId("hold");
        sustainParameterId = lookupChildElementComponentId("sustain");
        releaseParameterId = lookupChildElementComponentId("release");
        attackTypeParameterId = lookupChildElementComponentId("attack-type");
        inverseParameterId = lookupChildElementComponentId("inverse");
    }

    @Override
    public abstract JTComponent createComponent(JTContext context) throws JTException;

    protected void link(JTContext context, JTComponent component, PModule module)
      throws JTException
    {
        PParameter attack = module.getParameterByComponentId(parameterId);
        PParameter decay = module.getParameterByComponentId(decayParameterId);
        PParameter hold = module.getParameterByComponentId(holdParameterId);
        PParameter sustain = module.getParameterByComponentId(sustainParameterId);
        PParameter release = module.getParameterByComponentId(releaseParameterId);
        PParameter attackType = module.getParameterByComponentId(attackTypeParameterId) ;
        PParameter Inverse = module.getParameterByComponentId(inverseParameterId);
        
        JTEnvelopeDisplay disp = (JTEnvelopeDisplay) component;

        if (attack != null) disp.setAttackAdapter(new JTParameterControlAdapter(attack));
        if (decay != null) disp.setDecayAdapter(new JTParameterControlAdapter(decay));
        if (hold != null) disp.setHoldAdapter(new JTParameterControlAdapter(hold));
        if (sustain != null) disp.setSustainAdapter(new JTParameterControlAdapter(sustain));
        if (release != null) disp.setReleaseAdapter(new JTParameterControlAdapter(release));
        if (attackType != null) disp.setAttackTypeAdapter(new JTParameterControlAdapter(attackType));
        if (Inverse != null) disp.setInverseAdapter(new JTParameterControlAdapter(Inverse));
    }
    
    protected void link2(JTContext context, JTComponent component, PModule module, PParameter parameter)
    {
        throw new UnsupportedOperationException();
    }
    
}
