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
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.spec.DParameter;
import net.sf.nmedit.nomad.theme.property.ParameterProperty;
import net.sf.nmedit.nomad.theme.property.ParameterValue;
import net.sf.nmedit.nomad.theme.property.PropertySet;
import net.sf.nmedit.nomad.theme.property.Value;

public class ADDisplay extends EnvelopeDisp implements EventListener<ParameterEvent> 
{

    private DParameter infA = null;
    private DParameter infD = null;
    private Parameter parA = null;
    private Parameter parD = null;
    
    public ADDisplay()
    {
        configureAD();
    }


    public void registerProperties(PropertySet set) {
        super.registerProperties(set);
        set.add(new ParAProperty());
        set.add(new ParBProperty());
    }

    private static class ParAProperty extends ParameterProperty
    {
        public ParAProperty()
        {
            super(0);
        }
        
        public Value encode( NomadComponent component )
        {
            if (component instanceof ADDisplay)
                return new ParameterValue( this, ( (ADDisplay) component )
                        .infA );
            else
                return super.encode(component);
        }
        
        public Value decode( String value )
        {
            return new ParameterValue( this, value )
            {
                public void assignTo( NomadComponent component )
                {
                    if (component instanceof ADDisplay) 
                    {
                        ( (ADDisplay) component ).infA = getParameter() ;
                    }
                    else
                        super.assignTo(component);
                }

            };
        }
    }

    private static class ParBProperty extends ParameterProperty
    {
        public ParBProperty()
        {
            super(1);
        }
        
        public Value encode( NomadComponent component )
        {
            if (component instanceof ADDisplay)
                return new ParameterValue( this, ( (ADDisplay) component )
                        .infD);
            else
                return super.encode(component);
        }
        
        public Value decode( String value )
        {
            return new ParameterValue( this, value )
            {
                public void assignTo( NomadComponent component )
                {
                    if (component instanceof ADDisplay) 
                    {
                        ( (ADDisplay) component ).infD = getParameter() ;
                    }
                    else
                        super.assignTo(component);
                }

            };
        }
    }
    
    public void link(Module module) {
        parA = module.getParameter(infA.getContextId());
        if (parA!=null) parA.addListener(this);

        parD = module.getParameter(infD.getContextId());
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
    
    protected double getDoubleValue(Parameter p)
    {
        return (p.getValue()-p.getMinValue())
        / (double) (p.getMaxValue()-p.getMinValue());
    }
    
}
