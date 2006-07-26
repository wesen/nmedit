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
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.event.ParameterEvent;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.spec.DParameter;
import net.sf.nmedit.nomad.theme.property.ParameterProperty;
import net.sf.nmedit.nomad.theme.property.ParameterValue;
import net.sf.nmedit.nomad.theme.property.PropertySet;
import net.sf.nmedit.nomad.theme.property.Value;

public class ADSRModDisplay extends ADDisplay
{

    public ADSRModDisplay()
    {
        configureADSR();
    }

    private DParameter infS = null;
    private DParameter infR = null;
    private DParameter infInv = null;
    private Parameter parS = null;
    private Parameter parR = null;
    private Parameter parInv = null;
    
    public void registerProperties(PropertySet set) {
        super.registerProperties(set);
        set.add(new ParSProperty());
        set.add(new ParRProperty());
        set.add(new ParInvProperty());
    }

    private static class ParSProperty extends ParameterProperty
    {
        public ParSProperty()
        {
            super(2);
        }
        
        public Value encode( NomadComponent component )
        {
            if (component instanceof ADSRModDisplay)
                return new ParameterValue( this, ( (ADSRModDisplay) component )
                        .infS );
            else
                return super.encode(component);
        }
        
        public Value decode( String value )
        {
            return new ParameterValue( this, value )
            {
                public void assignTo( NomadComponent component )
                {
                    if (component instanceof ADSRModDisplay) 
                    {
                        ( (ADSRModDisplay) component ).infS = getParameter() ;
                    }
                    else
                        super.assignTo(component);
                }

            };
        }
    }

    private static class ParRProperty extends ParameterProperty
    {
        public ParRProperty()
        {
            super(3);
        }
        
        public Value encode( NomadComponent component )
        {
            if (component instanceof ADSRModDisplay)
                return new ParameterValue( this, ( (ADSRModDisplay) component )
                        .infR );
            else
                return super.encode(component);
        }
        
        public Value decode( String value )
        {
            return new ParameterValue( this, value )
            {
                public void assignTo( NomadComponent component )
                {
                    if (component instanceof ADSRModDisplay) 
                    {
                        ( (ADSRModDisplay) component ).infR = getParameter() ;
                    }
                    else
                        super.assignTo(component);
                }

            };
        }
    }

    private static class ParInvProperty extends ParameterProperty
    {
        public ParInvProperty()
        {
            super(4);
        }
        
        public Value encode( NomadComponent component )
        {
            if (component instanceof ADSRModDisplay)
                return new ParameterValue( this, ( (ADSRModDisplay) component )
                        .infInv );
            else
                return super.encode(component);
        }
        
        public Value decode( String value )
        {
            return new ParameterValue( this, value )
            {
                public void assignTo( NomadComponent component )
                {
                    if (component instanceof ADSRModDisplay) 
                    {
                        ( (ADSRModDisplay) component ).infInv = getParameter() ;
                    }
                    else
                        super.assignTo(component);
                }

            };
        }
    }

    public void link(Module module) 
    {
        parS = module.getParameter(infS.getContextId());
        if (parS!=null) parS.addListener(this);
        parR = module.getParameter(infR.getContextId());
        if (parR!=null) parR.addListener(this);
        parInv = module.getParameter(infInv.getContextId());
        if (parInv!=null) parInv.addListener(this);
        super.link(module);
    }

    public void unlink() {
        if (parS!=null) parS.removeListener(this);
        if (parR!=null) parR.removeListener(this);
        if (parInv!=null) parInv.removeListener(this);
        parS = null;
        parR = null;
        parInv = null;
        super.unlink();
    }

    public void event(ParameterEvent event)
    {
        super.event(event);
        
        Parameter p = event.getParameter();
        
        if (event.getID()==ParameterEvent.PARAMETER_VALUE_CHANGED)
        {
            if (parS==p) setSustain(getDoubleValue(p));
            else if (parR==p) setRelease(getDoubleValue(p));
            else if (parInv==p) setInverse(parInv.getValue()==1);
        }
    }
    
    protected void updateValues()
    {
        if (parS!=null) setSustain(getDoubleValue(parS));
        if (parR!=null) setRelease(getDoubleValue(parR));
        if (parInv!=null) setInverse(parInv.getValue()==1);
        super.updateValues();
    }
    

}
