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
 * Created on Jan 21, 2007
 */
package net.sf.nmedit.jtheme.component.plaf;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;
import java.util.Comparator;
import java.util.EventObject;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.UIDefaults;
import javax.swing.border.Border;

import net.sf.nmedit.jpatch.ImageSource;
import net.sf.nmedit.jpatch.ModuleDescriptions;
import net.sf.nmedit.jpatch.PModule;
import net.sf.nmedit.jpatch.PModuleDescriptor;
import net.sf.nmedit.jpatch.history.PUndoableEditSupport;
import net.sf.nmedit.jpatch.transform.PTModuleMapping;
import net.sf.nmedit.jpatch.transform.PTTransformations;
import net.sf.nmedit.jtheme.JTContext;
import net.sf.nmedit.jtheme.component.JTComponent;
import net.sf.nmedit.jtheme.component.JTModule;
import net.sf.nmedit.jtheme.util.JThemeUtils;
import net.sf.nmedit.nmutils.Platform;
import net.sf.nmedit.nmutils.swing.EscapeKeyListener;
import net.sf.nmedit.nmutils.swing.LimitedText;

public class JTModuleUI extends JTComponentUI
{

    public static final String moduleBorder = "ModuleUI.Border";
    public static final String moduleFont = "ModuleUI.Font";
    public static final String moduleSelectionBorder = "ModuleUI.Selected.Border";
    
    private static final String moduleTransIcon = "ModuleUI.transformationIcon";
    private static final String moduleTransIconHovered = "ModuleUI.transformationIcon.hovered";

    private static final Color DEFAULT_MTICON_COLOR = new Color(0x626262);
    private static final Color DEFAULT_MTICON_COLOR_HOVERED = new Color(0x929292);

    public static final String moduleActionMapKey = "module.actionMap";

    public static JTModuleUI createUI(JComponent c)
    {
        return new JTModuleUI((JTModule)c);
    }
    
    private JTModule module;
    private static JPopupMenu transformPopupMenu;
    
    protected JTModuleUI(JTModule module)
    {
        this.module = module;
    }
    
    private TitleEditor titleEditor;
    
    private void setEditModuleTitle(boolean edit)
    {
        if (edit)
        {
            titleEditor = new TitleEditor();
            module.add(titleEditor.textfield, 0);
            titleEditor.init();
        }
        else
        {
            TitleEditor te = titleEditor;
            if (te != null)
            {
                if (te.textfield != null)
                    module.remove(te.textfield);
                titleEditor = null;
            }
        }
    }
    
    // private static transient Map<ModuleDescriptor, Color> backgroundColors;

    private String currentTitle;
    private String currentShortTitle = ""; // never null
    
    private String getShortTitle(JTModule module)
    {
        String title = module.getTitle();
        String shortTitle = currentShortTitle;
        if (currentTitle == title) return shortTitle;
        
        shortTitle = JThemeUtils.getTitleNoColorKey(title);
        if (shortTitle == null) shortTitle = "";
        this.currentTitle = title;
        this.currentShortTitle = shortTitle;
        return shortTitle;
    }
    
    protected void updateTitle(JTModule module)
    {
        String title = module.getTitle();
        if (title != null)
        {
            int colorkey = JThemeUtils.getColorKey(title);
            setModuleColor(colorkey);
        }
        else
        {
            setModuleColor(0);
        }
    }

    private ImageIcon getModuleTransformationIcon(UIDefaults def)
    {
        Icon icon = def.getIcon(moduleTransIcon);
        if (icon == null || (!(icon instanceof ImageIcon)))
        {
            icon = new ImageIcon(renderModuleTransformationIcon(DEFAULT_MTICON_COLOR));
            def.put(moduleTransIcon, icon);
        }
        return (ImageIcon) icon;
    }

    protected Icon getModuleTransformationIconHovered(UIDefaults def)
    {
        Icon icon = def.getIcon(moduleTransIconHovered);
        if (icon == null)
        {
            icon = new ImageIcon(renderModuleTransformationIcon(DEFAULT_MTICON_COLOR_HOVERED));
            def.put(moduleTransIconHovered, icon);
        }
        return icon;
    }
    
    protected Image renderModuleTransformationIcon(Color c)
    {
        BufferedImage bi = new BufferedImage(12, 12, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = bi.createGraphics();
        try
        {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            paintModuleTransformationIcon(g2, c, 11, 11);
        }
        finally
        {
            g2.dispose();
        }
        return bi;
    }
    private void paintModuleTransformationIcon(Graphics2D g2, Color c, int w, int h)
    {
        g2.setColor(c);
        // outline
        g2.drawRect(0, 0, w-1, h-1);
        g2.drawLine(0, 1, 1, 1); // TL
        g2.drawLine(w-2, 1, w-1, 1); // TR
        g2.drawLine(0, h-2, 1, h-2); // BL
        g2.drawLine(w-2, h-2, w-1, h-2); // BR
        // arrow
        Polygon arrow = new Polygon();
        arrow.addPoint(2, 3);
        arrow.addPoint(w-2, 3);
        arrow.addPoint(w/2, h-2);
        g2.fill(arrow);
    }

    private Border border;
    private Border selectionBorder;
    private ImageIcon transformationIcon;
    
    private static Insets cachedInsets;
    private static Font currentFont;
    private static FontMetrics currentFontMetrics;
    
    private FontMetrics getFontMetrics(JComponent c, Font font)
    {
        FontMetrics fm = currentFontMetrics;
        if (currentFont == font && fm != null)
            return fm;
        
        fm = c.getFontMetrics(font);
        currentFontMetrics = fm;
        currentFont = font;
        return fm;
    }
    
    private Insets getInsets(JComponent c)
    {
        return cachedInsets = c.getInsets(cachedInsets);
    }
    
    public void installUI(JComponent c) 
    {
        JTModule module = (JTModule) c;

        BasicEventHandler beh = createEventHandler(module);
        beh.install(module);
        
        JTContext jtcontext = module.getContext();
        UIDefaults uidefaults = jtcontext.getUIDefaults();
        
        if (border == null)
            border = uidefaults.getBorder(moduleBorder);
        if (selectionBorder == null)
            selectionBorder = uidefaults.getBorder(moduleSelectionBorder);

        Font font = uidefaults.getFont(moduleFont);
        if (font != null)
            module.setFont(font);
        
        c.setBorder(module.isSelected() ? selectionBorder : border);
        
        transformationIcon = getModuleTransformationIcon(uidefaults);
        
    }
    
    public void uninstallUI(JComponent c)
    {
        JTModule module = (JTModule) c;
        
        BasicEventHandler eventHandler = getEventHandler(module);
        if (eventHandler != null)
            eventHandler.uninstall(module);
    }

    private static BasicEventHandler beh;
    protected BasicEventHandler createEventHandler(JTModule module) 
    {
        if (beh == null)
            beh = new BasicEventHandler();
        return beh;
    }

    protected BasicEventHandler getEventHandler(JTModule module)
    {
        Object[] o = module.getListeners(MouseListener.class);
        for (int i=o.length-1;i>=0;i--)
            if (o[i] instanceof BasicEventHandler)
                return (BasicEventHandler) o[i];
        return null;
    }
    
    public static class BasicEventHandler extends MouseAdapter implements
        PropertyChangeListener, FocusListener
    {
        
        private JTModule getModule(EventObject e)
        {
            Object src = e.getSource();
            if (src != null && src instanceof JTModule)
                return (JTModule) src;
            return null;
        }

        public void install(JTModule module)
        {
            module.addMouseListener(this);
            module.addFocusListener(this);
            module.addPropertyChangeListener(this);
        }

        public void uninstall(JTModule module)
        {
            module.removeMouseListener(this);
            module.removeFocusListener(this);
            module.removePropertyChangeListener(this);
        }

        public void mousePressed(MouseEvent e)
        {
            Component c = e.getComponent();
            if (c instanceof JTModule)
            {
                if (!c.hasFocus())
                {
                    c.requestFocus();
                }
            }
            JTModule module = getModule(e);
            if (module == null || module.getUI() == null)
                return;
            
            if (module.getUI().handleTransformPopup(e))
                return;
            if (module.getUI().handleLabelClick(e))
                return;
        }

        public void propertyChange(PropertyChangeEvent evt)
        {
            JTModule module = getModule(evt);
            if (module == null || module.getUI() == null)
                return;
            
            if (JTModule.PROPERTY_TITLE.equals(evt.getPropertyName()))
            {
                module.getUI().updateTitle(module);
                module.repaint();
            }
            else if (JTModule.PROPERTY_SELECTED.equals(evt.getPropertyName()))
            {
                JTModuleUI ui = module.getUI();
                module.setBorder(module.isSelected() ? ui.selectionBorder : ui.border);   
            }
        }

        public void focusGained(FocusEvent e)
        {
            e.getComponent().repaint();
        }

        public void focusLost(FocusEvent e)
        {
            e.getComponent().repaint();
        }
        
    }
    
    private boolean handleLabelClick(MouseEvent e)
    {
        if (e.getClickCount() == 2 && Platform.isLeftMouseButtonOnly(e)
                && hitTitle(e.getX(), e.getY()))
        {
            setEditModuleTitle(true);
            return true;
        }
        return false;
    }
    
    private boolean handleTransformPopup(MouseEvent me)
    {
        if (Platform.isLeftMouseButtonOnly(me)
                && hitTransformationIcon(me.getX(), me.getY()) && me.getClickCount()==1)
        {
            if (transformPopupMenu != null && transformPopupMenu.getInvoker()==me.getComponent()) 
            {
                // close popup
                transformPopupMenu.setVisible(false);
                transformPopupMenu = null;
            }
            else
            {
                // show popup
                PModule m = module.getModule();
                if (m != null)
                {
                    createPopup(me, m);
                    return true;
                }
            }
        }
        return false;
    }

    private void createPopup(MouseEvent e, final PModule source)
    {
        ModuleDescriptions md = source.getDescriptor().getModules();
        
        ClassLoader loader = md.getModuleDescriptionsClassLoader();
        
        PTTransformations t = md.getTransformations();
        if (t == null)
            return ;
        
        PTModuleMapping[] mappings = t.getMappings(source.getDescriptor());

        // PTBasicTransformations.sort(mappings);
        Arrays.sort(mappings, new Comparator<PTModuleMapping>() {
            public int compare(PTModuleMapping o1, PTModuleMapping o2) {
                PModuleDescriptor md1 = o1.getTarget(source.getDescriptor());
                PModuleDescriptor md2 = o2.getTarget(source.getDescriptor());
                return md1.getName().compareTo(md2.getName());
            }
        });
        
        transformPopupMenu = null;
        if (mappings.length>0)
        {
            for (int i=0;i<mappings.length;i++)
            {
                if (transformPopupMenu == null) {
                    transformPopupMenu = new JPopupMenu();
                }
                transformPopupMenu.add(new TransformAction(loader, source, mappings[i]));
            }
            transformPopupMenu.show(e.getComponent(), e.getX(), e.getY());
        }
    }

    
    private static class TransformAction extends AbstractAction
    {

        /**
         * 
         */
        private static final long serialVersionUID = -682154271222280870L;
        private PModule source;
        private PTModuleMapping mapping;

        public TransformAction(ClassLoader loader, 
                PModule source, PTModuleMapping mapping)
        {
            this.source = source;
            this.mapping = mapping;

            PModuleDescriptor md = mapping.getTarget(source.getDescriptor());
            if (md == null)
            {
                putValue(NAME, "#error");
                setEnabled(false);
                return;
            }
            
            //float covering = ((int)(mapping.getCovering()*10000))/100f;
            
          putValue(NAME, md.getName());
            
            String hint =
                "<html><body><strong>transformed</strong><br/>connector(s): "+mapping.getConnectorCount()
                +"<br/>parameter(s): "+mapping.getParameterCount()
                +"</body></html>";
            putValue(AbstractAction.SHORT_DESCRIPTION, hint);
            ImageSource is = md.get16x16IconSource();
            if (is != null)                
            {
                Image img = md.getModules().getImage(is);
                if (img != null)
                    putValue(SMALL_ICON, new ImageIcon(img));
            }
        }

        public void actionPerformed(ActionEvent e)
        {
            if (isEnabled()) {
            	// XXX color hack
                PUndoableEditSupport ues = source.getParentComponent().getEditSupport();
                String t1 = source.getTitle();
                
        		ues.beginUpdate("Transform " + source.getTitle());
        		try {
        			PModule result = mapping.transform(source);
        			if (result != null) {
        				int colorKey = JThemeUtils.getColorKey(t1);
        				result.setTitle(JThemeUtils.setColorKey(result.getTitle(),colorKey));
        			}
        		} finally {
        			ues.endUpdate();
        		}

            }
        }
    }
    
    private boolean hitRect(int x, int y, int rwidth, int rheight)
    {
        return (0<=x) && (x<rwidth) && (0<=y) && (y<rheight);
    }
    
    public boolean hitTransformationIcon(int x, int y)
    {
        if (transformationIcon == null)
            return false;
        
        Insets insets = getInsets(module);
        x-=insets.left;
        y-=insets.top;
        int w = transformationIcon.getIconWidth();
        int h = transformationIcon.getIconHeight();
        return hitRect(x, y, w, h);
    }
    
    
    public void paintStaticLayer(Graphics2D g, JTComponent c)
    {
        if (c.isOpaque())
        {
            Insets insets = getInsets(c);
            g.setColor(c.getBackground());

            int left = insets.left;
            
            Border border = c.getBorder();
            
            if (border == null || border.isBorderOpaque())
            {
                g.fillRect(0, 0, c.getWidth(), c.getHeight());
            }
            else
            {
                g.fillRect(insets.left, insets.top, 
                        c.getWidth()-(insets.left+insets.right-1), c.getHeight()-(insets.top+insets.bottom-1));
            }
            
            
            if (transformationIcon != null)
            {
                transformationIcon.paintIcon(c, g, insets.left, insets.top);
                left+=transformationIcon.getIconWidth();
            }
            left+=2; //padding - even if no icon is painted

            Font font = module.getFont();
            FontMetrics fm = getFontMetrics(module, font);
            if (module.hasFocus())
            {
                g.setColor(Color.WHITE);
                g.fillRect(left, insets.left, TITLE_WIDTH, fm.getHeight());
            }
            

            String title = getShortTitle(module);
            if (title != null && title.length()>0)
            {
                g.setFont(font);
                g.setColor(Color.BLACK);
                g.drawString(title, left, insets.top+fm.getHeight()-fm.getDescent());
            }
        }
    }
    
    final int TITLE_WIDTH = 56; // TODO hard coded values are bad
    
    public boolean hitTitle(int x, int y)
    {
        Insets insets = getInsets(module);
        int l = insets.left;
        int t = insets.top;
        int h = 0;
        int w = TITLE_WIDTH; // constant width
        if (transformationIcon != null)
        {
            l+=transformationIcon.getIconWidth();
        }
        l+=2; //padding - even if no icon is painted

        String title = getShortTitle(module);
        if (title != null && title.length()>0)
        {
            FontMetrics fm = getFontMetrics(module, module.getFont());
            h = fm.getHeight();
        }
        
        x-=l;
        y-=t;

        return hitRect(x, y, w, h);
    }
    
    private Point getTitleTopLeft()
    {
        Insets insets = getInsets(module);
        int l = insets.left;
        int t = insets.top;
        if (transformationIcon != null)
        {
            l+=transformationIcon.getIconWidth();
        }
        l+=2; //padding - even if no icon is painted
        return new Point(l, t);
    }
    
    public void paintDynamicLayer(Graphics2D g, JTComponent c)
    {
        // no op
    }
    
    private class TitleEditor implements FocusListener, ActionListener
    {
        
        /**
         * 
         */
        private static final long serialVersionUID = 7086106076035449738L;
        private JTextField textfield;

        public TitleEditor()
        {
            textfield = new JTextField(new LimitedText(16), getShortTitle(module), 16);
            textfield.setFont(module.getFont());
            textfield.addKeyListener(new EscapeKeyListener(this, 0, "escape"));
            textfield.addActionListener(this);
            textfield.addFocusListener(this);
            textfield.setBorder(null);
        }
        
        public void init()
        {
            textfield.setLocation(getTitleTopLeft());
            textfield.setSize(textfield.getPreferredSize());
            if (!textfield.requestFocusInWindow())
                setEditModuleTitle(false);
        }
        
        public void focusGained(FocusEvent e)
        {
            // no op
        }

        public void focusLost(FocusEvent e)
        {
            setEditModuleTitle(false);
        }

        public void actionPerformed(ActionEvent e)
        {
            Object s = e.getSource();
            if (!(s instanceof JTextField))
                return;
            
            JTextField tf = (JTextField) s;
                
            if ("escape".equals(e.getActionCommand()))
            {
                setEditModuleTitle(false);
            }
            else if (e.getID() == ActionEvent.ACTION_PERFORMED)
            {
                String title = module.getTitle();
                int colorkey = JThemeUtils.getColorKey(title);

                title = tf.getText();
                if (colorkey>0)
                    title += "$"+colorkey;
                module.setTitle(title);
                setEditModuleTitle(false);
                module.repaint();
            }
        }

    }
    
    private Color fill = null;
    private Color getFill()
    {
        if (fill == null)
        {
            Object f = module.getClientProperty("fill");
            if (f instanceof Color)
                fill = (Color) f;
        }
        return fill;
    }
    
    protected void setModuleColor(int colorCode){
        Color bg = null;
        if (colorCode>0) {
        	UIDefaults defaults = module.getContext().getUIDefaults();
        	Color c = defaults.getColor("module.background$"+colorCode);
        	if (c != null)
        		bg = c;
        }
        if (bg == null) bg = getFill();
        module.setBackground(bg);
    }
  
}
