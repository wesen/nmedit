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
package net.sf.nmedit.jtheme.store;

import org.jdom.Attribute;
import org.jdom.Element;

import net.sf.nmedit.jpatch.PLight;
import net.sf.nmedit.jpatch.PModule;
import net.sf.nmedit.jtheme.JTContext;
import net.sf.nmedit.jtheme.JTException;
import net.sf.nmedit.jtheme.component.JTComponent;
import net.sf.nmedit.jtheme.component.JTLight;

public class LightStore extends DefaultStore
{

    private static final String TYPE_LED = "led";
    private static final String TYPE_LED_ARRAY = "led-array";
    private static final String TYPE_METER = "meter";
    
    private String lightId;
    private int type = JTLight.LED;
    private int LEDOnValue = Integer.MIN_VALUE;

    protected LightStore(Element element)
    {
        super(element);
        Attribute a = element.getAttribute("component-id");
        lightId = a == null ? null : a.getValue();
        
        a = element.getAttribute("type");
        if (a != null)
        {
            String typeName = a.getValue();
            if (TYPE_LED.equals(typeName))
                type = JTLight.LED;
            else if (TYPE_LED_ARRAY.equals(typeName))
                type = JTLight.LED_ARRAY;
            else if (TYPE_METER.equals(typeName))
                type = JTLight.METER;
            else
                throw new RuntimeException("invalid light type:"+typeName);
        }
        
        a = element.getAttribute("ledOnValue");
        if (a != null)
            LEDOnValue = Integer.parseInt(a.getValue());
    }

    public static Store create(StorageContext context, Element element)
    {
        return new LightStore(element);
    }

    @Override
    public JTComponent createComponent(JTContext context) throws JTException
    {
        JTLight c = (JTLight) context.createComponent(JTContext.TYPE_LIGHT);
        c.setType(type);
        
        if (LEDOnValue!=Integer.MIN_VALUE)
            c.setLEDOnValue(LEDOnValue);
        
        applyLocation(c);
        applySize(c);
        return c;
    }

    protected void link(JTContext context, JTComponent component, PModule module)
      throws JTException
    {
        PLight light = module.getLightByComponentId(lightId);
        
        
        if (light != null)
            ((JTLight)component).setLight(light);
    }

}
