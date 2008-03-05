package net.sf.nmedit.jtheme.util;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.SwingUtilities;

import net.sf.nmedit.jpatch.PModule;
import net.sf.nmedit.jtheme.cable.Cable;
import net.sf.nmedit.jtheme.cable.CableRenderer;
import net.sf.nmedit.jtheme.cable.JTCableManager;
import net.sf.nmedit.jtheme.component.JTModule;
import net.sf.nmedit.jtheme.component.JTModuleContainer;
import net.sf.nmedit.jtheme.component.plaf.SelectionPainter;

public class ModuleImageRenderer
{

    private boolean renderCables = true;
    private Dimension maximumSize = null;
    private JTModuleContainer container = null;
    private List<JTModule> modules = new ArrayList<JTModule>();
    private boolean extraBorder;
    
    public ModuleImageRenderer()
    {
        super();
    }
    
    public ModuleImageRenderer(Collection<? extends JTModule> modules)
    {
        super();
        for (JTModule module: modules)
            add(module);
    }
    
    public boolean add(JTModule module)
    {
        if (container == null)
        {
            if (module.getParent() instanceof JTModuleContainer)
                container = (JTModuleContainer)module.getParent();
        }
        else
        {
            if (container != module.getParent())
                return false; // not in the same container
        }
        modules.add(module);
        return true;
    }
    
    public void setRenderCablesEnabled(boolean enable)
    {
        this.renderCables = enable;
    }
    
    public void setMaximumSize(Dimension max)
    {
        this.maximumSize = max;
    }
    
    public Image render()
    {
        BufferedImage image;
        if (container != null)
        {
            synchronized (container.getTreeLock())
            {
                image = _render();
            }
        }
        else
        {
            image = _render();
        }
        // scale image if necessary
        if (maximumSize != null && 
                (maximumSize.width<image.getWidth() || maximumSize.height<image.getHeight()))
        {
            // TODO
        }
        
        
        return image;
    }
    
    private BufferedImage _render()
    {
        Rectangle bounds = new Rectangle(0,0,0,0);
        boolean first = true;
        for (JTModule module: modules)
        {
            if (first)
            {
                bounds.setBounds(module.getBounds());
                first = false;
            }
            else
            {
                bounds = SwingUtilities.computeUnion(
                        module.getX(), module.getY(),
                        module.getWidth(), module.getHeight(),
                        bounds);
            }
        }
        
        boolean opaque = (modules.size() == 1 && modules.get(0).isOpaque());
        
        BufferedImage image = new BufferedImage(bounds.width, bounds.height, 
                opaque ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB );
        
        Set<PModule> pmodules = null;
        if (renderCables)
            pmodules = new HashSet<PModule>();
        Graphics2D g2 = image.createGraphics();
        try
        {
            if (container != null)
            {
                g2.setFont(container.getFont());
                g2.setColor(container.getBackground());
            }
            
            // paint modules
            for (JTModule module: modules)
            {
                Graphics2D gg2 = (Graphics2D) g2.create();
                try
                {
                    // translate 
                    gg2.setFont(module.getFont());
                    gg2.setColor(module.getBackground());
                    gg2.translate(module.getX()-bounds.x, module.getY()-bounds.y);
                    module.print(gg2);
                }
                finally
                {
                    gg2.dispose();
                }
                if (renderCables)
                {
                    PModule pmodule = module.getModule();
                    if (pmodule != null)
                        pmodules.add(pmodule);
                }
            }
            
            if (extraBorder)
                SelectionPainter.paintSelectionBox(g2, modules, -bounds.x, -bounds.y);
            
            // render cables
            if (container != null && renderCables)
            {
                JTCableManager cm = container.getCableManager();
                if (cm != null)
                {
                    List<Cable> cables = new ArrayList<Cable>();
                    cm.getCables(cables, pmodules); // get cables
                    if (!cables.isEmpty())
                    {
                        CableRenderer cr = cm.getCableRenderer();
                        cr.initRenderer(g2);
                        g2.translate(-bounds.x, -bounds.y);
                        for (Cable cable: cables)
                        {
                            if (pmodules.contains(cable.getSourceModule())
                                    && pmodules.contains(cable.getDestinationModule()))
                            {
                                // only render cables which connect modules in selection
                                cr.render(g2, cable);
                            }
                        }
                        g2.translate(bounds.x, bounds.y);
                    }
                }
            }
        }
        finally
        {
            g2.dispose();
        }
        
        
        return image;
    }

    public void setPaintExtraBorder(boolean extra)
    {
        this.extraBorder = extra;
    }
    
}
