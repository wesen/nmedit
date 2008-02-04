package net.sf.nmedit.jtheme.component;

import java.awt.Graphics;
import java.awt.Graphics2D;

import net.sf.nmedit.jtheme.JTContext;
import net.sf.nmedit.jtheme.cable.JTCableManager;

public class JTCableLayer extends JTBaseComponent
{

    public JTCableLayer(JTContext context)
    {
        super(context);
        setOpaque(false);
    }
    
    public boolean contains(int x, int y)
    {
        // ensure this component gets no mouse events or similar events
        return false;
    }
    
    private JTCableManager cableManager;
    protected void setCableManager(JTCableManager cableManager)
    {
        JTCableManager oldManager = this.cableManager;
        if (oldManager != cableManager)
        {
            if (oldManager != null)
            {
                oldManager.setOwner(null);
                oldManager.setView(null);
            }
            this.cableManager = cableManager;
            if (cableManager != null)
            {
                cableManager.setOwner(this);
                cableManager.setView(this);
            }
        }
    }
    
    protected void paintComponent(Graphics g)
    {
        if (cableManager != null)
        {
            Graphics gs = g.create();
            try
            {
                cableManager.paintCables((Graphics2D) gs);
            }
            finally
            {
                gs.dispose();
            }
        }
    }
    
    protected void paintChildren(Graphics g)
    {
        // nothing to paint
    }
    
}
