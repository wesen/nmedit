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

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIDefaults;
import javax.swing.border.Border;

import net.sf.nmedit.jpatch.ImageSource;
import net.sf.nmedit.jpatch.ModuleDescriptions;
import net.sf.nmedit.jpatch.PModule;
import net.sf.nmedit.jpatch.PModuleDescriptor;
import net.sf.nmedit.jpatch.event.PModuleEvent;
import net.sf.nmedit.jpatch.event.PModuleListener;
import net.sf.nmedit.jpatch.transform.PTModuleMapping;
import net.sf.nmedit.jpatch.transform.PTTransformations;
import net.sf.nmedit.jtheme.JTContext;
import net.sf.nmedit.jtheme.cable.Cable;
import net.sf.nmedit.jtheme.cable.JTCableManager;
import net.sf.nmedit.jtheme.component.JTComponent;
import net.sf.nmedit.jtheme.component.JTImage;
import net.sf.nmedit.jtheme.component.JTLabel;
import net.sf.nmedit.jtheme.component.JTModule;
import net.sf.nmedit.jtheme.component.JTModuleContainer;
import net.sf.nmedit.jtheme.util.JThemeUtils;
import net.sf.nmedit.nmutils.Platform;
import net.sf.nmedit.nmutils.swing.EscapeKeyListener;
import net.sf.nmedit.nmutils.swing.LimitedText;
import net.sf.nmedit.nmutils.swing.NMLazyActionMap;

public class JTModuleUI extends JTComponentUI implements PModuleListener
{

    public static final String DELETE = "delete";
    public static final String moduleBorder = "ModuleUI.Border";
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
    private TitleLabel titleLabel;
    private static JPopupMenu transformPopupMenu;
    
    protected JTModuleUI(JTModule module)
    {
        this.module = module;
    }
    
    // private static transient Map<ModuleDescriptor, Color> backgroundColors;

    public void moduleChanged(JTModule c, PModule oldModule, PModule newModule)
    {
        if (oldModule != null)
        {
            oldModule.removeModuleListener(this);
        }
        if (newModule != null)
        {
            newModule.addModuleListener(this);
            
            /*
            if (c.getStaticLayerBackingStore()==null)
            {
                
                ModuleDescriptor md = newModule.getDescriptor();
                
                Color bg = backgroundColors == null ? null : backgroundColors.get(md);
                if (bg == null)
                {
                    Object o = md.getAttribute("background");

                    if (o != null)
                    
                    if (o!= null && o instanceof String)
                    {
                        String bgstring = (String) o;
                        // #FfFfFf                        
                        if (bgstring.length()==7 && bgstring.startsWith("#"))
                        {
                            int color = 0;
                            int i=1;
                            for (;i<bgstring.length();i++)
                            {
                                char ch = Character.toLowerCase(bgstring.charAt(i));
                                if ('0'<=ch && ch<='9')
                                    color = color*16+(ch-'0');
                                else if ('a'<=ch && ch<='f')
                                    color = color*16+(ch-'a'+10);
                                else break;
                            }
                            if (i==bgstring.length())
                            {
                                bg = new Color(color);
                                
                                if (backgroundColors == null)
                                    backgroundColors = new HashMap<ModuleDescriptor, Color>();
                                backgroundColors.put(md, bg);
                            }
                        }
                    }
                    
                }
                
                if (bg != null)
                {
                    c.setBackground(bg);
                }
            }
            */
        }
        else
        {
            // set default background
        }
        updateTitle();
    }
    
    protected void updateTitle()
    {
        PModule m = module.getModule();
        
        String title = m != null ? m.getTitle() : null;
        
        if (title != null)
            setTitleLabelText(title);
    }
        

    private Icon getModuleTransformationIcon(UIDefaults def)
    {
        Icon icon = def.getIcon(moduleTransIcon);
        if (icon == null)
        {
            icon = new ImageIcon(renderModuleTransformationIcon(DEFAULT_MTICON_COLOR));
            def.put(moduleTransIcon, icon);
        }
        return icon;
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
    
    private transient Polygon arrow ;
    
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
        if (arrow == null)
        {
            arrow = new Polygon();
            arrow.addPoint(2, 3);
            arrow.addPoint(w-2, 3);
            arrow.addPoint(w/2, h-2);
        }
        g2.fill(arrow);
    }

    private Border border;
    
    public void installUI(JComponent c) 
    {
        JTModule module = (JTModule) c;

        BasicEventHandler beh = createEventHandler(module);
        beh.install(module);
        
        JTContext jtcontext = module.getContext();
        UIDefaults uidefaults = jtcontext.getUIDefaults();
        
        if (border == null)
            border = uidefaults.getBorder(moduleBorder);

        if (border != null)
            c.setBorder(border);
        
        Insets i = module.getInsets();
        int left = i.left;
        Icon ic = getModuleTransformationIcon(uidefaults);
        //Icon hov = getModuleTransformationIconHovered(uidefaults);
        
        if (ic != null && ic instanceof ImageIcon)
        {
            JTImage jti = new JTTransformer(jtcontext);
            jti.setIcon((ImageIcon) ic);
            jti.setUI(JTImageUI.createUI(jti));
            jti.setLocation(left, i.top);
            jti.setSize(jti.getPreferredSize());
            module.add(jti,0);
            left+=jti.getWidth();
        }
        
        titleLabel = new TitleLabel(jtcontext, module);
        titleLabel.setUI(JTLabelUI.createUI(titleLabel));
        titleLabel.setLocation(left, i.top);
        titleLabel.setSize(60, 13);
        module.add(titleLabel, 0);
        
        if (module.getModule()!=null)
        {
            setTitleLabelText(module.getModule().getTitle());
            module.getModule().addModuleListener(this);
        }
    }
    
    public void uninstallUI(JComponent c)
    {
        JTModule module = (JTModule) c;
        
        BasicEventHandler eventHandler = getEventHandler(module);
        if (eventHandler != null)
            eventHandler.uninstall(module);
    }
    

    private static final String MODULE_LISTENER_KEY = JTModuleUI.class.getName()+".MODULE_LISTENER";
    
    protected BasicEventHandler createEventHandler(JTModule control) 
    {
        BasicEventHandler eventHandler;
        
        JTContext context = control.getContext();
        UIDefaults defaults = (context != null) ? context.getUIDefaults() : null;
        
        if (defaults != null)
        {
            Object l = defaults.get(MODULE_LISTENER_KEY);
            if ((l != null) && (l instanceof BasicEventHandler))
            {
                eventHandler = (BasicEventHandler) l;
            }
            else
            {
                eventHandler = new BasicEventHandler();
                defaults.put(MODULE_LISTENER_KEY, eventHandler);
            }
        }
        else
        {
            eventHandler = new BasicEventHandler();
        }
        
        return eventHandler;
    }

    protected BasicEventHandler getEventHandler(JTModule module)
    {
        Object[] o = module.getListeners(MouseListener.class);
        for (int i=o.length-1;i>=0;i--)
            if (o[i] instanceof BasicEventHandler)
                return (BasicEventHandler) o[i];
        return null;
    }
    
    public static class BasicEventHandler implements MouseListener,
        ComponentListener
    {

        public void install(JTModule module)
        {
            //module.addComponentListener(this);
            module.addMouseListener(this);
            
//            installKeyboardActions(module);
        }

        public void uninstall(JTModule module)
        {
            //module.removeComponentListener(this);
            module.removeMouseListener(this);
            
//            uninstallKeyboardActions(module);
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
        }
        
        public void mouseClicked(MouseEvent e)
        {
            // no op
        }

        public void mouseEntered(MouseEvent e)
        {
            // no op
        }

        public void mouseExited(MouseEvent e)
        {
            // no op
        }
        
        public void mouseReleased(MouseEvent e)
        {
            // no op
        }

        public void componentHidden(ComponentEvent e)
        {
            // no op
        }

        public void componentMoved(ComponentEvent e)
        {
            ((JTModule)e.getComponent()).getUI().updateCables();

            /*  update cables (done in module container ui)
            if (!(e.getComponent() instanceof JTModule))
                return;
            
            JTModule mod = (JTModule) e.getComponent();
            // no op
            if (!(mod.getParent() instanceof JTModuleContainer))
                return;
            
            JTModuleContainer cont = (JTModuleContainer) mod.getParent();
            JTCableManager cm = cont.getCableManager();
            if (cm == null)
                return;
            
            LinkedList<Cable> visible = new LinkedList<Cable>();
            cm.getVisible(visible);
            for (Cable cable: visible)
            {
                cm.update(cable);
            }
            cm.notifyRepaintManager(); */
        }

        public void componentResized(ComponentEvent e)
        {
            // no op
        }

        public void componentShown(ComponentEvent e)
        {
            // no op
        }
    }
    
    private static class JTTransformer extends JTImage
    {
        /**
         * 
         */
        private static final long serialVersionUID = 3285475892052794842L;

        public JTTransformer(JTContext context)
        {
            super(context);
            enableEvents(MouseEvent.MOUSE_EVENT_MASK|MouseEvent.MOUSE_MOTION_EVENT_MASK);
        }

        public boolean isReducible()
        {
            return false;
        }
        /*
        public void paintStaticLayer(Graphics2D g2)
        {
            getIcon().paintIcon(this, g2, 0, 0);
        }
*/
        protected void processEvent(AWTEvent e)
        {
            if (!(e instanceof MouseEvent))
            {
                super.processEvent(e);
                return;
            }
            
            MouseEvent me = (MouseEvent) e;
            
            if ((!me.isConsumed()) && e.getID() == MouseEvent.MOUSE_PRESSED && SwingUtilities.isLeftMouseButton(me))
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
                    Container p = getParent();
                    if (p instanceof JTModule)
                    {
                        PModule m = ((JTModule) p).getModule();
                        if (m != null)
                        {
                            createPopup(me, m);
                            return;
                        }
                    }
            	}
            }
            
            super.processEvent(e);
        }

        protected void processMouseMotionEvent(MouseEvent e)
        {
            MouseEvent me = JThemeUtils.convertMouseEvent(e.getComponent(), e, getParent());
            getParent().dispatchEvent(me);
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
            
            float covering = ((int)(mapping.getCovering()*10000))/100f;
            
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
                String t1 = source.getTitle();
                
                PModule result = mapping.transform(source);

                if (result != null) {
                	String t2 = result.getTitle();
                	int sep = t1.lastIndexOf('$');
                	if (sep>=0) {
                		t2 = t2 + t1.substring(sep);
                	}
                	result.setTitle(t2);
                }
            }
        }
    }
    
    public void paintStaticLayer(Graphics2D g, JTComponent c)
    {
        if (c.isOpaque())
        {
            g.setColor(c.getBackground());
            
            Border border = c.getBorder();
            if (border == null || border.isBorderOpaque())
            {
                g.fillRect(0, 0, c.getWidth(), c.getHeight());
            }
            else
            {
                Insets i = c.getInsets();
                g.fillRect(i.left, i.top, c.getWidth()-(i.left+i.right-1), c.getHeight()-(i.top+i.bottom-1));
            }
        }
    }
    
    public void paintDynamicLayer(Graphics2D g, JTComponent c)
    {
        // no op
    }

    public void paintSelection(Graphics g, JTModule module)
    {
        
        if (module.isSelected() || module.hasFocus())
        {
            // TODO lookup selection color
            
        	// System.out.println("iwdthL " + module.getWidth() + " height " + module.getHeight());
        	
            final Color selection = Color.BLUE;
        	g.setColor(selection);
            g.drawRect(0, 0, module.getWidth()-1, module.getHeight()-1);
        }
    }
    
    private class TitleLabel extends JTLabel implements 
        FocusListener, ActionListener
    {
        
        /**
         * 
         */
        private static final long serialVersionUID = 7086106076035449738L;
        private JTModule module;
        private boolean titleSet = false;

        public TitleLabel(JTContext context, JTModule module)
        {
            super(context);
            this.module = module;
            enableEvents(MouseEvent.MOUSE_EVENT_MASK|MouseEvent.MOUSE_MOTION_EVENT_MASK);
        }

        protected boolean opacityOverwrite(boolean isOpaque)
        {
            return isOpaque;
        }
        
        public String getText() {
        	String text = super.getText();
            int colorSeparator = text.lastIndexOf('$');
            
            if (colorSeparator>=0)
            {
            	return text.substring(0, colorSeparator);
            } else

        	return text;
        }
        
        public String[] getSplitText() {
        	return new String[] { getText() };
        }

        protected void paintStaticLayerOrBackingStore(Graphics2D g2)
        {
            paintStaticLayer(g2);
        }
        
        public boolean isReducible()
        {
            return false;
        }
        
        public void paint(Graphics g)
        {
            if (!titleSet)
            {
                PModule m = module.getModule();
                if (m != null)
                {
                    setText(m.getTitle());
                    titleSet = true;
                }
                //setSize(getPreferredSize());
                
            }
            
            super.paint(g);
        }
        
        protected void processEvent(AWTEvent e)
        {
            if (e instanceof MouseEvent)
            {
                MouseEvent me = (MouseEvent)e;
                
                if (me.getID() == MouseEvent.MOUSE_CLICKED && 
                        me.getClickCount()==2 && SwingUtilities.isLeftMouseButton(me))
                {
                    editModuleName();
                    return;
                }
            }

            super.processEvent(e);
        }
        
        private void editModuleName()
        {
            PModule m = module.getModule();
            if (m == null)
                return ;

            JTextField tf = new JTextField(new LimitedText(16), m.getShortTitle(), 16);
            tf.setLocation(getLocation());
            module.add(tf, 0);
            tf.setSize(tf.getPreferredSize());
            tf.addActionListener(this);
            tf.addKeyListener(new EscapeKeyListener(this, 0, "escape"));
            
            tf.addFocusListener(this);
            
            if (!tf.requestFocusInWindow())
            {
                module.remove(tf);
                return ;
            }
            module.repaint();
        }

        public void focusGained(FocusEvent e)
        {
            // no op
        }

        public void focusLost(FocusEvent e)
        {
            disposeEditor(e.getComponent());
        }
        
        private void disposeEditor(Component ed)
        {
            if (ed instanceof JTextField)
            {
                JTextField tf = (JTextField) ed;
                tf.removeFocusListener(this);
                tf.removeActionListener(this);
                module.remove(tf);
                module.repaint();
            }
        }

        public void actionPerformed(ActionEvent e)
        {
            Object s = e.getSource();
            if (!(s instanceof JTextField))
                return;
            
            JTextField tf = (JTextField) s;
                
            if ("escape".equals(e.getActionCommand()))
            {
                disposeEditor(tf);
            }
            else if (e.getID() == ActionEvent.ACTION_PERFORMED)
            {
                String text = tf.getText();
                
                module.getModule().setTitle(text);
                setText(text);
                disposeEditor(tf);
                setSize(getPreferredSize());
                repaint();
            }
        }

    }

    private void updateCables()
    {
        JTModuleContainer parent;
        try
        {
            parent = (JTModuleContainer) module.getParent();
        }
        catch (ClassCastException cce)
        {
            // ignore
            parent = null;
        }
        
        if (parent != null)
        {
            JTCableManager cm = parent.getCableManager();
            PModule pmodule = module.getModule();
            if (cm != null && pmodule != null)
            {
                java.util.List<Cable> cables = new LinkedList<Cable>();
                cm.getCables(cables, pmodule);
                cm.update(cables);
                for (Cable cable: cables)
                    cable.updateEndPoints();
                cm.update(cables);
            }
        }
    }
    
    public void moduleMoved(PModuleEvent e)
    {
        try
        {
            JTModuleContainer jtc = (JTModuleContainer) module.getParent();
            if (jtc != null)
                jtc.updateModuleContainerDimensions();
        }
        catch (ClassCastException cce)
        {
            // ignore
        }
        
        /*
        Module m = e.getModule();
        History h = m.getPatch().getHistory();
        
        if (h != null)
            h.beginRecord();
        try
        {
            module.setLocation(m.getScreenLocation());
            
            List<Cable> cables = new LinkedList<Cable>();
            JTModuleContainer c = (JTModuleContainer) module.getParent();
            c.getCableManager().getCables(cables, e.getModule());
            for (Cable cable: cables)
                c.getCableManager().update(cable);
        }
        finally
        {
            if (h!=null)
                h.endRecord();
        }*/
    }

    public void moduleRenamed(PModuleEvent e)
    {
        setTitleLabelText(e.getModule().getShortTitle());
    }
    
    public void moduleColorChanged(PModuleEvent e) {
        setModuleColor(e.getModule().getColorCode());
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
    
    protected void setTitleLabelText(String text)
    {
        if (text == null)
            text = "";
        titleLabel.setText(text);
    }
    
    protected void setModuleColor(String colorCode){
        Color bg = getFill();
        if (bg == null)
            return;
        
        if (colorCode.length() > 0) {
        	colorCode = "module.background$"+colorCode;
        	UIDefaults defaults = module.getContext().getUIDefaults();
        	Color c = defaults.getColor(colorCode);
        	if (c != null)
        		bg = c;
        }
        module.setBackground(bg);
    }
}
