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
package net.sf.nmedit.jtheme.store2;

import java.io.IOException;
import java.io.Serializable;

import net.sf.nmedit.jpatch.PLight;
import net.sf.nmedit.jpatch.PLightDescriptor;
import net.sf.nmedit.jpatch.PModule;
import net.sf.nmedit.jpatch.PModuleDescriptor;
import net.sf.nmedit.jtheme.JTContext;
import net.sf.nmedit.jtheme.JTException;
import net.sf.nmedit.jtheme.component.JTComponent;
import net.sf.nmedit.jtheme.component.JTLight;
import net.sf.nmedit.jtheme.store.StorageContext;

import org.jdom.Attribute;
import org.jdom.Element;

public class LightElement extends AbstractElement implements Serializable
{

    /**
     * 
     */
    private static final long serialVersionUID = 2929771547830089798L;
    private static final String TYPE_LED = "led";
    private static final String TYPE_LED_ARRAY = "led-array";
    private static final String TYPE_METER = "meter";
    private static final String ATT_TYPE = "type";
    private static final String ATT_LEDONVALUE = "ledOnValue";
    
    private int type = JTLight.LED;
    private int LEDOnValue = Integer.MIN_VALUE;

    protected String componentId;
    protected transient PLightDescriptor cachedLightDescriptor;
    
    public static AbstractElement createElement(StorageContext context, Element element)
    {
        LightElement e = new LightElement();
        e.initElement(context, element);
        e.checkLocation();
        e.checkDimensions();
        return e;
    }
    
    @Override
    public JTComponent createComponent(JTContext context,
            PModuleDescriptor descriptor, PModule module) throws JTException
    {
        JTLight c = (JTLight) context.createComponent(JTContext.TYPE_LIGHT);
        c.setType(type);
        if (LEDOnValue!=Integer.MIN_VALUE)
            c.setLEDOnValue(LEDOnValue);
        setBounds(c);
        setLight(c, descriptor, module);
        return c;
    }

    @Override
    protected void initAttributes(StorageContext context, Attribute att)
    {
        String name = att.getName();
        if (ATT_COMPONENT_ID.equals(name))
        {
            this.componentId = att.getValue();
        }
        else if (ATT_TYPE.equals(name))
        {
            String typeName = att.getValue();
            if (TYPE_LED.equals(typeName))
                type = JTLight.LED;
            else if (TYPE_LED_ARRAY.equals(typeName))
                type = JTLight.LED_ARRAY;
            else if (TYPE_METER.equals(typeName))
                type = JTLight.METER;
            else
                throw new RuntimeException("invalid light type:"+typeName);
        }
        else if (ATT_LEDONVALUE.equals(name))
        {
            LEDOnValue = Integer.parseInt(att.getValue());
        }
        else
        {
            super.initAttributes(context, att);
        }
    }


    protected void setLight(JTLight c, PModuleDescriptor descriptor, PModule module)
    {
        PLightDescriptor lightDescriptor = null;
        if (cachedLightDescriptor != null)
            lightDescriptor = cachedLightDescriptor;
        else if (componentId != null)
            cachedLightDescriptor = lightDescriptor = descriptor.getLightDescriptorByComponentId(componentId);
        if (lightDescriptor != null)
        {
            if (module != null)
            {
                PLight light = module.getLight(lightDescriptor);
                if (light != null)
                {
                    c.setLight(light);
                }
            }
        }
    }

    private void writeObject(java.io.ObjectOutputStream out)
        throws IOException
    {
        out.defaultWriteObject();
    }
    
    private void readObject(java.io.ObjectInputStream in)
        throws IOException, ClassNotFoundException
    {
        in.defaultReadObject();
    }
    
}
