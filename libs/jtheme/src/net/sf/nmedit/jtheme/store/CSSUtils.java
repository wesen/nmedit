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

import java.awt.Color;
import java.io.IOException;
import java.io.StringReader;

import org.jdom.Attribute;
import org.jdom.Element;
import org.w3c.css.sac.InputSource;
import org.w3c.dom.css.CSSPrimitiveValue;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSRuleList;
import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.css.CSSStyleRule;
import org.w3c.dom.css.CSSStyleSheet;
import org.w3c.dom.css.CSSValue;
import org.w3c.dom.css.CSSValueList;
import org.w3c.dom.css.RGBColor;

import com.steadystate.css.dom.CSSStyleDeclarationImpl;
import com.steadystate.css.dom.CSSStyleSheetImpl;
import com.steadystate.css.parser.CSSOMParser;
import com.steadystate.css.parser.SACParserCSS2;

public class CSSUtils
{

    public static float getPx(CSSStyleDeclaration styleDecl, String name, float alt)
    {
        CSSValue cssv = styleDecl.getPropertyCSSValue(name); 
        return cssv != null ? getPx(cssv, alt) : alt;
    }

    private static float getPx(CSSValue cssv, float alt)
    {
        if (cssv.getCssValueType() == CSSValue.CSS_PRIMITIVE_VALUE)
        {
            CSSPrimitiveValue pv = (CSSPrimitiveValue) cssv;
            return pv.getFloatValue(CSSPrimitiveValue.CSS_PX);
        }
        
        return alt;
    }

    public static boolean isBorderNone(CSSStyleDeclaration style, String name, boolean defaultValue)
    {
        CSSValue cssv = style.getPropertyCSSValue(name);
        
        if (cssv == null)
            return defaultValue;
        

        if (cssv.getCssValueType() == CSSValue.CSS_PRIMITIVE_VALUE)
        {
            CSSPrimitiveValue prim = (CSSPrimitiveValue) cssv;

            if (prim.getPrimitiveType() == CSSPrimitiveValue.CSS_IDENT)
            {
                if (prim.getCssText().equals("none"))
                    return true;
            }
        }
        
        if (cssv!=null && cssv.getCssValueType() == CSSValue.CSS_VALUE_LIST)
        {
            CSSValueList vlBorder = (CSSValueList) cssv;
            
            for (int i=0;i<vlBorder.getLength();i++)
            {
                CSSValue item = vlBorder.item(i);
                
                if (item.getCssValueType() == CSSValue.CSS_PRIMITIVE_VALUE)
                {
                    CSSPrimitiveValue prim = (CSSPrimitiveValue) item;
                    
                    if (prim.getPrimitiveType() == CSSPrimitiveValue.CSS_IDENT)
                    {
                        if (prim.getStringValue().equals("none"))
                            return true;
                    }
                }
            }
        }
        return defaultValue;
    }

    public static Color getColor(CSSStyleDeclaration style, String name)
    {
        CSSValue cssv = style.getPropertyCSSValue(name);
        if (cssv!=null && cssv.getCssValueType() == CSSValue.CSS_PRIMITIVE_VALUE)
        {
            CSSPrimitiveValue primFill = (CSSPrimitiveValue) cssv;
            if (primFill.getPrimitiveType() == CSSPrimitiveValue.CSS_RGBCOLOR)
            {
                RGBColor rgbColor = primFill.getRGBColorValue();
                return getColor(rgbColor);
            }
        }
        return null;
    }

    public static Color getColor(RGBColor rgbColor)
    {
        float r = rgbColor.getRed().getFloatValue(CSSPrimitiveValue.CSS_NUMBER)  /0xFF;
        float g = rgbColor.getGreen().getFloatValue(CSSPrimitiveValue.CSS_NUMBER)/0xFF;
        float b = rgbColor.getBlue().getFloatValue(CSSPrimitiveValue.CSS_NUMBER) /0xFF;
        
        return new Color(r, g, b);
    }
    
    public static CSSStyleDeclaration getStyleDeclaration(Element e, StorageContext context)
    {
        return getStyleDeclaration(e.getName(), e, context);
    }
    
    public static CSSStyleDeclaration getStyleDeclaration(String cssClassName, Element e, StorageContext context)
    {
        CSSStyleSheet css = context.getStyleSheet();
        CSSStyleRule rule = context.getStyleRule(cssClassName);
        
        CSSStyleDeclarationImpl decl = (CSSStyleDeclarationImpl) parseStyleDeclaration(e, css);
        
        if (decl != null)
        {
            if (rule != null)
                decl.setParentRule(rule);
            
            return decl;
        }
        
        if (rule != null)
            return rule.getStyle();
        
        return null;
    }

    
    public static CSSStyleDeclaration getStyleDeclaration(String cssClassName, String styleAttribute, StorageContext context)
    {  
        CSSStyleRule rule = context.getStyleRule(cssClassName);
        CSSStyleDeclarationImpl decl = null;
        if (styleAttribute != null)
        {
            CSSStyleSheet css = context.getStyleSheet();
            decl = (CSSStyleDeclarationImpl) parseStyleDeclaration(styleAttribute, css);

            if (rule != null)
                decl.setParentRule(rule);
            
            return decl;
        }
        if (rule != null)
            return rule.getStyle();
        
        return null;
    }

    private static CSSStyleDeclaration parseStyleDeclaration(Element e, CSSStyleSheet parent)
    {
        Attribute styleAtt = e.getAttribute("style");
        if (styleAtt != null)
            return parseStyleDeclaration(styleAtt.getValue(), parent);
        return null;
    }

    public static CSSStyleDeclaration parseStyleDeclaration(String style, CSSStyleSheet parent)
    {
        CSSOMParser cssParser = getCSSOMParser();
        cssParser.setParentStyleSheet((CSSStyleSheetImpl) parent);
        try
        {
            return cssParser.parseStyleDeclaration(new InputSource(new StringReader("{"+style+"}")));
        }
        catch (IOException ioe)
        {
            return null;
        }
    }

    
    private static transient SACParserCSS2 cssParser;
    private static transient CSSOMParser cssomparser;
    
    private static SACParserCSS2 getCSSParser()
    {
        if (cssParser == null)
            cssParser = new SACParserCSS2();
        return cssParser;
    }

    private static CSSOMParser __makeParser()
    {
        return new CSSOMParser(getCSSParser());
    }

    public static CSSOMParser getCSSOMParser()
    {
        if (cssomparser == null)
            cssomparser = __makeParser();
        else
            cssomparser.setParentStyleSheet(null);
        return cssomparser;
    }

    public static CSSStyleDeclaration getStyleDeclaration(CSSStyleSheet styleSheet, String name)
    {
        
        // TODO SLOW, INEFFICIENT, WRONG (lookup by name is missing)
        
        if (styleSheet == null)
            return null;
        
        CSSRuleList rl = styleSheet.getCssRules();
        for (int i=rl.getLength()-1;i>=0;i--)
        {
            CSSRule rule = rl.item(i);
           
            if (rule.getType() == CSSRule.STYLE_RULE)
            {
                CSSStyleRule srule = (CSSStyleRule) rule;
                if (name.equals(srule.getSelectorText()))
                    return srule.getStyle();
            }
        }
        return null;
    }

}

