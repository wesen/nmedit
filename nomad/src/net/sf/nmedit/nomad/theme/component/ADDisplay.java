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
 * Created on Jul 26, 2006
 */
package net.sf.nmedit.nomad.theme.component;

import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.Module;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.Parameter;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.event.EventListener;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.event.ParameterEvent;
import net.sf.nmedit.nomad.theme.property.ParameterProperty;
import net.sf.nmedit.nomad.theme.property.PropertySet;

public class ADDisplay extends EnvelopeDisp implements EventListener<ParameterEvent> 
{

    private Parameter parA = null;
    private Parameter parD = null;

    protected final static String IATTACK = "parameter#0";
    protected final static String IDECAY = "parameter#1";
    
    public ADDisplay()
    {
        configureAD();
    }

    public void registerProperties(PropertySet set) {
        super.registerProperties(set);
        set.add(new ParameterProperty(0));
        set.add(new ParameterProperty(1));
    }

    public void link(Module module) {
        parA = module.getParameter(getParameterInfo(IATTACK).getContextId());
        if (parA!=null) parA.addListener(this);

        parD = module.getParameter(getParameterInfo(IDECAY).getContextId());
        if (parD!=null) parD.addListener(this);
        
        updateValues();
    }

    public void unlink() {
        if (parA!=null) parA.removeListener(this);
        if (parD!=null) parD.removeListener(this);

        parA = null;
        parD = null;
    }

    protected void updateValues()
    {
        if (parA!=null) setAttack(getDoubleValue(parA));
        if (parD!=null) setDecay(getDoubleValue(parD));
    }
    
    public void event(ParameterEvent event)
    {
        Parameter p = event.getParameter();
        
        if (event.getID()==ParameterEvent.PARAMETER_VALUE_CHANGED)
        {
            if (parA==p) setAttack(getDoubleValue(p));
            else if (parD==p) setDecay(getDoubleValue(p));
        }
    }
    
}
