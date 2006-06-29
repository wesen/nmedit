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
 * Created on Jun 15, 2006
 */
package net.sf.nmedit.nomad.theme.xml;

import net.sf.nmedit.nomad.theme.xml.dom.ComponentNode;
import net.sf.nmedit.nomad.theme.xml.dom.ModuleNode;
import net.sf.nmedit.nomad.theme.xml.dom.ThemeNode;
import net.sf.nmedit.jmisc.xml.builder.ElementHandler;
import net.sf.nmedit.jmisc.xml.builder.XMLProcessingException;
import net.sf.nmedit.jmisc.xml.builder.impl.AbstractElementHandler;
import net.sf.nmedit.jmisc.xml.builder.impl.AbstractDocumentHandler;
import net.sf.nmedit.jmisc.xml.builder.impl.AbstractAttributeHandler;

import org.xmlpull.v1.XmlPullParser;

public class ThemeBuilder  extends AbstractDocumentHandler
{
    private ThemeNode dom;
    private ElTheme root;
    
    public ThemeBuilder(ThemeNode dom)
    {
        this.dom = dom;
        this.root = new ElTheme();
    }

    public ThemeNode getThemeNode()
    {
        return dom;
    }
    
    public ElementHandler getRootElement()
    {
        return root;
    }

    class ElTheme extends AbstractElementHandler
    {
        public ElTheme()
        {
            super( "theme" );
            new ElModule(this);
        }
    }

    class ElModule extends AbstractElementHandler
    {
        
        private ModuleNode moduleNode = null;

        public ElModule( ElTheme parent )
        {
            super( parent, "module" );
            new ElComponent(this);
            new AttName();
        }

        public void element(XmlPullParser parser) throws XMLProcessingException
        {
            super.element(parser);
            dom.putModuleNode(moduleNode);
        }
        
        private class AttName extends AbstractAttributeHandler
        {
            public AttName()
            {
                super( ElModule.this, "id", true);
            }

            public void attribute( XmlPullParser parser, int attributeIndex )
            {
                int mID = Integer.parseInt(
                        parser.getAttributeValue(attributeIndex));
                moduleNode = new ModuleNode(mID);
            }
        }
        
    }
    
    class ElComponent extends AbstractElementHandler
    {
        
        private ComponentNode compNode = null;

        public ElComponent( ElModule parent )
        {
            super( parent, "component" );
            new ElProperty(this);
            new AttName();
        }

        public void element(XmlPullParser parser) throws XMLProcessingException
        {
            super.element(parser);
            ((ElModule) getParent()).moduleNode
            .addComponentNode(compNode);
        }
        
        private class AttName extends AbstractAttributeHandler
        {
            public AttName()
            {
                super( ElComponent.this, "name", true);
            }

            public void attribute( XmlPullParser parser, int attributeIndex )
            {
                String cName = parser.getAttributeValue(attributeIndex);
                compNode = new ComponentNode(cName);
            }
        }

        ComponentNode getComponentNode()
        {
            return compNode;
        }
        
    }

    static class ElProperty extends AbstractElementHandler
    {
        
        private String pName = null;
        private String pValue = null;
        
        public ElProperty(ElComponent parent)
        {
            super( parent, "property" );
            new AttName();
            new AttValue();
        }

        public void element(XmlPullParser parser) throws XMLProcessingException
        {
            super.element(parser);

            ((ElComponent)getParent()).getComponentNode()
            .putProperty(pName, pValue);
        }
        
        private class AttName extends AbstractAttributeHandler
        {
            public AttName()
            {
                super( ElProperty.this, "name", true );
            }

            public void attribute( XmlPullParser parser, int attributeIndex )
            {
                pName = parser.getAttributeValue(attributeIndex);
            }
        }

        private class AttValue extends AbstractAttributeHandler
        {
            public AttValue()
            {
                super( ElProperty.this, "value", true );
            }

            public void attribute( XmlPullParser parser, int attributeIndex )
            {
                pValue = parser.getAttributeValue(attributeIndex);
            }
        }
        
    }
}
