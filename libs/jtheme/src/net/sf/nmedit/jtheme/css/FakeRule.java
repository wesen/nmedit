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
 */package net.sf.nmedit.jtheme.css;

import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.css.CSSStyleRule;
import org.w3c.dom.css.CSSStyleSheet;


public class FakeRule implements CSSStyleRule
{ 
    private static final FakeRule instance = new FakeRule();
    
    public static FakeRule instance()
    {
        return FakeRule.instance;
    }
    
    public static boolean isFake(Object rule) { return rule == instance; }
    public String getSelectorText() { return null; }
    public CSSStyleDeclaration getStyle() { return null; }
    public void setSelectorText(String selectorText) { }
    public String getCssText() { return null; }
    public CSSRule getParentRule() { return null; }
    public CSSStyleSheet getParentStyleSheet() { return null; }
    public short getType() { return 0; }
    public void setCssText(String cssText){ }
    
}
