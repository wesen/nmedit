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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.net.URL;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIDefaults;
import javax.swing.border.Border;

import net.sf.nmedit.jpatch.ImageSource;
import net.sf.nmedit.jpatch.Module;
import net.sf.nmedit.jpatch.ModuleDescriptor;
import net.sf.nmedit.jpatch.event.ModuleEvent;
import net.sf.nmedit.jpatch.event.ModuleListener;
import net.sf.nmedit.jpatch.spec.ModuleDescriptions;
import net.sf.nmedit.jpatch.transformation.TransformTool;
import net.sf.nmedit.jpatch.transformation.TransformableModule;
import net.sf.nmedit.jpatch.transformation.Transformations;
import net.sf.nmedit.jtheme.JTContext;
import net.sf.nmedit.jtheme.component.JTComponent;
import net.sf.nmedit.jtheme.component.JTImage;
import net.sf.nmedit.jtheme.component.JTLabel;
import net.sf.nmedit.jtheme.component.JTModule;
import net.sf.nmedit.nmutils.swing.EscapeKeyListener;
import net.sf.nmedit.nmutils.swing.LimitedText;

public class JTModuleUI extends JTComponentUI implements ModuleListener
{

    public static final String moduleBorder = "ModuleUI.Border";
    private static final String moduleTransIcon = "ModuleUI.transformationIcon";
    
    private static final Color DEFAULT_MTICON_COLOR = new Color(0x626262);
    
    public static JTModuleUI createUI(JComponent c)
    {
        return new JTModuleUI((JTModule)c);
    }
    
    private JTModule module;
    private TitleLabel titleLabel;
    
    protected JTModuleUI(JTModule module)
    {
        this.module = module;
    }
    
    // private static transient Map<ModuleDescriptor, Color> backgroundColors;

    public void moduleChanged(JTModule c, Module oldModule, Module newModule)
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
        Module m = module.getModule();
        
        String title = m != null ? m.getTitle() : null;
        if (title != null)
            titleLabel.setText(title);
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
    
    protected Image renderModuleTransformationIcon(Color c)
    {
        BufferedImage bi = new BufferedImage(11, 11, BufferedImage.TYPE_INT_ARGB);
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
        if (ic != null && ic instanceof ImageIcon)
        {
            JTImage jti = new JTTransformer(jtcontext);
            jti.setUI(JTImageUI.createUI(jti));
            jti.setIcon((ImageIcon) ic);
            jti.setLocation(left, i.top);
            jti.setSize(jti.getPreferredSize());
            module.add(jti, 0);
            left+=jti.getWidth();
        }
        
        titleLabel = new TitleLabel(jtcontext, module);
        titleLabel.setUI(JTLabelUI.createUI(titleLabel));
        titleLabel.setLocation(left, i.top);
        titleLabel.setSize(100, 13);
        module.add(titleLabel, 0);
        
        if (module.getModule()!=null)
            module.getModule().addModuleListener(this);
        
    }
    
    public void uninstallUI(JComponent c)
    {
        JTModule module = (JTModule) c;
        
        BasicEventHandler eventHandler = getEventHandler(module);
        if (eventHandler != null)
            eventHandler.uninstall(module);
    }
    
    private BasicEventHandler eventHandler;

    protected BasicEventHandler createEventHandler(JTModule module)
    {
        if (eventHandler == null)
            eventHandler = new BasicEventHandler();
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
    
    private static class BasicEventHandler implements MouseListener,
        ComponentListener
    {
        
        public BasicEventHandler()
        {
            
        }

        public void install(JTModule module)
        {
            //module.addComponentListener(this);
            module.addMouseListener(this);
        }

        public void uninstall(JTModule module)
        {
            //module.removeComponentListener(this);
            module.removeMouseListener(this);
        }

        public void mousePressed(MouseEvent e)
        {
        }
        
        private void createPopup(MouseEvent e, Module source)
        {
            ModuleDescriptions md = source.getDescriptor().getModuleDescriptions();
            
            ClassLoader loader = md.getModuleDescriptionsClassLoader();
            
            Transformations t = md.getTransformations();
            if (t == null)
                return ;
            
            TransformTool tool = t.createTransformation(source.getDescriptor());
                        
            JPopupMenu popup = null;
            for (TransformableModule tm: tool.getTargets())
            {       
                if (popup == null)
                    popup = new JPopupMenu();
                
                Action a = new TransformAction(loader, tool, source, tm);
                popup.add(a);
            }
            
            if (popup != null)
                popup.show(e.getComponent(), e.getX(), e.getY());
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
            if (e.isConsumed())
                return;
            
            if (!SwingUtilities.isLeftMouseButton(e))
                return;
            
            if (!(e.getComponent() instanceof JTModule))
                return;
            
            JTModule m = (JTModule) e.getComponent();
            
            Component c = m.getComponentAt(e.getX(), e.getY());
            
            if (!(c instanceof JTTransformer))
                return;
            
            if (m == null)
                return;
            Module mm = m.getModule();
            if (mm == null)
                return;
            
            e.consume();
            
            createPopup(e, mm);
        }

        public void componentHidden(ComponentEvent e)
        {
            // no op
        }

        public void componentMoved(ComponentEvent e)
        {
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
        public JTTransformer(JTContext context)
        {
            super(context);
        }
        
        public boolean isReducible()
        {
            return false;
        }
    }
    
    private static class TransformAction extends AbstractAction
    {

        private TransformTool tool;
        private Module source;
        private TransformableModule target;

        public TransformAction(ClassLoader loader, TransformTool tool, 
                Module source, TransformableModule tm)
        {
            this.tool = tool;
            this.source = source;
            this.target = tm;
            
            ModuleDescriptor md = tm.getTarget();
            putValue(NAME, md.getDisplayName());
            setEnabled(tm.getTarget() != source.getDescriptor());
            ImageSource is = md.getImage("icon16x16");
            if (is != null)                
            {
                Icon ic = getIcon(is, loader);
                if (ic != null)
                    putValue(SMALL_ICON, ic);
            }
        }

        public void actionPerformed(ActionEvent e)
        {
            if (isEnabled())
                tool.transformReplace(target, source);
        }

        private Icon getIcon(ImageSource is, ClassLoader loader)
        {
            URL iconURL = loader.getResource(is.getSource());
            return iconURL == null ? null : new ImageIcon(iconURL);
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
        
        if (module.isSelected())
        {
            // TODO lookup selection color
            
            final Color selection = Color.blue;
            g.setColor(selection);
            g.drawRect(0, 0, module.getWidth()-1, module.getHeight()-1);
        }
    }
    
    private class TitleLabel extends JTLabel implements 
        MouseListener, FocusListener, ActionListener
    {
        
        private JTModule module;
        private boolean titleSet = false;

        public TitleLabel(JTContext context, JTModule module)
        {
            super(context);
            this.module = module;
            addMouseListener(this);
        }

        protected boolean opacityOverwrite(boolean isOpaque)
        {
            return isOpaque;
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
                Module m = module.getModule();
                if (m != null)
                {
                    setText(m.getTitle());
                    titleSet = true;
                }
                //setSize(getPreferredSize());
                
            }
            
            super.paint(g);
        }

        public void mouseClicked(MouseEvent e)
        {
            if (e.getClickCount()==2 && SwingUtilities.isLeftMouseButton(e))
            {
                editModuleName();
            }
        }

        private void editModuleName()
        {
            Module m = module.getModule();
            if (m == null)
                return ;

            JTextField tf = new JTextField(new LimitedText(16), m.getTitle(), 16);
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

        public void mouseEntered(MouseEvent e)
        {
            // no op
        }

        public void mouseExited(MouseEvent e)
        {
            // no op
        }

        public void mousePressed(MouseEvent e)
        {
            // no op
        }

        public void mouseReleased(MouseEvent e)
        {
            // no op
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

    public void moduleMoved(ModuleEvent e)
    {
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

    public void moduleRenamed(ModuleEvent e)
    {
        titleLabel.setText(e.getModule().getTitle());
    }
    
}
