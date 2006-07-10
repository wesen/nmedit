package net.sf.nmedit.nomad.patch.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Point;
import java.awt.Transparency;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.NoSuchElementException;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.Connector;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.Module;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.event.EventListener;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.event.ModuleEvent;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.spec.DModule;
import net.sf.nmedit.nomad.patch.ui.action.BreakCablesAction;
import net.sf.nmedit.nomad.patch.ui.action.DisconnectCablesAction;
import net.sf.nmedit.nomad.patch.ui.action.RemoveCablesAction;
import net.sf.nmedit.nomad.patch.ui.action.RemoveModuleAction;
import net.sf.nmedit.nomad.theme.ModuleComponent;
import net.sf.nmedit.nomad.theme.NomadClassicColors;
import net.sf.nmedit.nomad.theme.component.ModuleGuiTitleLabel;
import net.sf.nmedit.nomad.theme.component.NomadComponent;
import net.sf.nmedit.nomad.util.graphics.GraphicsToolkit;


public class ModuleUI extends JComponent implements ModuleComponent, EventListener<ModuleEvent>
{

    public static class Metrics
    {

        public final static int WIDTH        = 255;

        public final static int HEIGHT       = 15;

        public final static int WIDTH_DIV_2  = 128;

        public final static int HEIGHT_DIV_2 = 8;

        public static void setModuleUILocation( ModuleUI ui, Module module )
        {
            ui.setLocation( Metrics.getPixelX( module.getX() ), Metrics
                    .getPixelY( module.getY() ) );
        }

        public static Point getPixelLocation( Module module )
        {
            return new Point( Metrics.getPixelX( module.getX() ), Metrics
                    .getPixelY( module.getY() ) );
        }

        public static Point getGridLocation( ModuleUI module )
        {
            return new Point( Metrics.getGridX( module.getX() ), Metrics
                    .getGridY( module.getY() ) );
        }

        public static int getPixelX( int gridX )
        {
            return gridX * Metrics.WIDTH;
        }

        public static int getPixelY( int gridY )
        {
            return gridY * Metrics.HEIGHT;
        }

        public static int getGridX( int pxX )
        {
            return pxX / Metrics.WIDTH;
        }

        public static int getGridY( int pxY )
        {
            return pxY / Metrics.HEIGHT;
        }

        public static int getHeight( DModule info )
        {
            return info.getHeight() * Metrics.HEIGHT;
        }
    }

    private final static Border                border           = BorderFactory
                                                                        .createRaisedBevelBorder();

    private ModuleGuiTitleLabel                nameLabel        = null;

    private Module                             module           = null;

    private ModuleSectionUI                    moduleSection    = null;

    // JMenuItem removeItem = new JMenuItem("Remove");

    private DModule                            info             = null;

    private boolean selected = false;
    
    protected void processMouseEvent(MouseEvent event)
    {
        switch (event.getID()){
            case MouseEvent.MOUSE_PRESSED:
                {
                    // get focus if mouse was pressed                        
                    event.getComponent().requestFocus();
                    
                    if (event.getComponent() instanceof ModuleUI)   
                    {
                        ModuleUI m = (ModuleUI) event.getComponent();
                        if (SwingUtilities.isLeftMouseButton( event ))
                        {
                            // dragging operation
                            m.getModuleSection().createDragAction(ModuleUI.this, event);
                        }
                        else if (event.isPopupTrigger())
                        {   // check if a popup should be shown
                            m.showPopup( event );
                        }
                    }
                    
                }
                break ;
        }
        
        super.processMouseEvent(event);
    }
    
    public boolean isSelected()
    {
        return selected;
    }

    public void setSelected(boolean selected)
    {
        if (this.selected!=selected)
        {
            this.selected = selected;
            repaint();
        }
    }
    
    
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        if (isOpaque() && getBackground()!=null) {
            g2.setColor(getBackground());
            g2.fillRect(0, 0, getWidth(),getHeight());
            //paintNomadBorder(g2);
        }
    }
    
    public void setLocationEx(int x, int y)
    {
        if (getX()==x && getY()==y)
            return;
        
        setLocation(x,y);
        Point l = Metrics.getGridLocation( this );

        if (getX() % Metrics.WIDTH - Metrics.WIDTH_DIV_2 >= 0) l.x++;

        l.x = Math.max( 0, l.x );

        getModule().setLocation( l );
        Metrics.setModuleUILocation( this, getModule() );
        /*
        m.getModuleSection().getModuleSection().rearangeModules(
                m.getModule() );
                
        *
        scrollTo( m );*/
    }
    /*
    protected void processMouseMotionEvent(MouseEvent event)
    {

        switch (event.getID()){
            case MouseEvent.MOUSE_DRAGGED:
                {

                if (SwingUtilities.isLeftMouseButton( event ))
                {
                    ModuleUI m = (ModuleUI) event.getComponent();
                    // ModuleUI m = (ModuleUI) event.getComponent();
                    int x = m.getX() + ( event.getX() - dragX );
                    int y = m.getY() + ( event.getY() - dragY );
                    m.setLocation( x, y );
                    scrollTo( m );
                }
                }
                break;
        }
        
        super.processMouseMotionEvent(event);
    }*/
    final static Font font = new Font("sanserif",Font.PLAIN,9);
/*
    static Background light
    = new GradientSectorBackground(
      new GradientPaint(
              -800, 1600,
              NomadClassicColors.MODULE_BACKGROUND.darker(),
              2000, 0,
              NomadClassicColors.MODULE_BACKGROUND
      )      
    );
    */
    
    public ModuleUI( DModule info )
    {
        this.info = info;
        setFont(font);
        setForeground(Color.BLACK);
        // setBackground(UIManager.getColor("Button.background"));
        setOpaque( true );
        setDoubleBuffered( true );

        enableEvents(MouseEvent.MOUSE_EVENT_MASK);
        enableEvents(MouseEvent.MOUSE_MOTION_EVENT_MASK);
        /*
        addMouseListener( mouseAction );
        addMouseMotionListener( mouseAction );
        */
        
        setBorder( border );

        // removeItem.addActionListener(new RemoveModule());
        // menu.add(removeItem);

        setLayout( null );

        nameLabel = new ModuleGuiTitleLabel( info );
        nameLabel.setLocation( 3, 0 );
        add( nameLabel );

        setFocusable( true );

        setSize( Metrics.WIDTH, Metrics.getHeight( info ) );

        setBackground(NomadClassicColors.MODULE_BACKGROUND);
       // setContentAreaFilled(true);
        
        // TODO ?Why is the icon empty here?
        /*
         * if (module.getDModule().getIcon() != null) { NomadImageView imageView =
         * new NomadImageView(); imageView.setDynamicOverlay(false); //
         * otherwise it is cached and on the module imageView.setLocation(3,3);
         * imageView.setImage( ( new ImageIcon(module.getDModule().getIcon())
         * ).getImage() ); add(imageView); }
         */
    }

    private BufferedImage screen   = null;
    private int sw = 0;
    private int sh = 0;

    public void paint( Graphics g )
    {
        int w = getWidth();
        int h = getHeight();
        if (w <= 0 || h <= 0)
        {
            return;
        }
        
        boolean fullRepaintRequired = false;
        
        if (screen == null || w != sw || h != sh)
        {
            GraphicsConfiguration gc = getGraphicsConfiguration();
            if (gc == null) gc = GraphicsToolkit.getDefaultGraphicsConfiguration();
            
            screen = gc.createCompatibleImage(
                    w, h, isOpaque() ? Transparency.OPAQUE : Transparency.TRANSLUCENT );
            fullRepaintRequired = true;
        }
        else 
        {
            fullRepaintRequired = hasChildrenWithDirtyDisplay();
        }

        sw = w;
        sh = h;
        // w, h>0
        if (fullRepaintRequired)
        {
            Graphics2D sharedG2 = null;
            
            if (!isOpaque())
            {
                GraphicsToolkit.clearRegion( sharedG2, 0, 0, sw, sh );
            }
            try
            {
                sharedG2 = screen.createGraphics();
                super.paint( sharedG2 );
            }
            finally
            {
                sharedG2.dispose();
                sharedG2 = null;
            }
        }

        // flip offscreen buffer
        g.drawImage( screen, 0, 0, null );
        
        if (isSelected())
        {
            g.setColor(Color.RED);
            g.drawRect(0,0,getWidth()-1,getHeight()-1);
        }
    }
    /*
    private boolean hasChildrenWithDirtyDisplay()
    {
        for (int i=getComponentCount()-1;i>=0;i--)
        {
            Component c = getComponent(i);
            if (c instanceof NomadComponent)
            {
                if (((NomadComponent)c).isDisplayDirty())
                {
                    return true;
                }
            }
        }
        return false;
    }*/

    // private ArrayList<NomadComponent> dirtyComponents = new ArrayList<NomadComponent>();
private boolean dirty = false;
    private boolean hasChildrenWithDirtyDisplay()
    {
        return dirty;
    }
    public void registerDirtyComponent( NomadComponent component )
    {/*
        if (!dirtyComponents.contains(component))
            dirtyComponents.add(component);*/
        dirty=true;
    }

    public void buildDefaultPopup( JPopupMenu menu )
    {
        menu.add( new RemoveCablesAction( this ) );
        menu.addSeparator();
        menu.add( new RemoveModuleAction( this ) );
    }

    public void buildConnectorPopup( JPopupMenu menu, Connector c )
    {
        menu.add( new DisconnectCablesAction( this, c ) );
        menu.add( new BreakCablesAction( this, c ) );
    }

    public void showPopup( MouseEvent event )
    {
        JPopupMenu menu = new JPopupMenu();
        buildDefaultPopup( menu );
        menu.show( event.getComponent(), event.getX(), event.getY() );
    }

    public void showConnectorPopup( MouseEvent event, Connector c )
    {
        JPopupMenu menu = new JPopupMenu();
        buildConnectorPopup( menu, c );
        buildDefaultPopup( menu );
        menu.show( event.getComponent(), event.getX(), event.getY() );
    }
/*
    public void setLocation( int x, int y )
    {
        super.setLocation( x, y );
        if (moduleSection != null)
        {
           moduleSection.locationSet( this );
        }
    }
*/
    public ModuleUI( Module module )
    {
        this( module.getDefinition() );
        setModule( module );
    }

    public void link( Module module )
    {
        for (int i = getComponentCount() - 1; i >= 0; i--)
        {
            Component c = getComponent( i );
            if (c instanceof NomadComponent)
                ( (NomadComponent) c ).link( module );
        }
    }

    public void unlink()
    {
        module.removeListener( this );
        for (Component c : getComponents())
        {
            if (c instanceof NomadComponent)
                ((NomadComponent) c).unlink();
        }
    }

    public void disconnectCables()
    {
        module.disconnectCables();
    }

    public void removeModule()
    {
        unlink();
        getModuleSection().getModuleSection().remove( module );
    }

    public void setModule( Module module )
    {
        this.module = module;
        nameLabel.setModule( module );

        link( module );
        Metrics.setModuleUILocation( this, module );
        module.addListener( this );
    }

    public void event( ModuleEvent event )
    {
        Metrics.setModuleUILocation( this, module );
    }
    
    public ModuleSectionUI getModuleSection()
    {
        return moduleSection;
    }

    public void setModuleSectionUI( ModuleSectionUI moduleSection )
    {
        this.moduleSection = moduleSection;
    }

    public Iterator<NomadComponent> getExportableNomadComponents()
    {
        return new Iterator<NomadComponent>() {
            int index = 0;

            public boolean hasNext()
            {
                if (index < getComponentCount())
                {
                    if (getComponent( index ) == nameLabel) index++;
                    return index < getComponentCount();
                }
                return false;
            }

            public NomadComponent next()
            {
                if (!hasNext()) throw new NoSuchElementException();
                return (NomadComponent) getComponent( index++ );
            }

            public void remove()
            {
                throw new UnsupportedOperationException();
            }
        };
    }
/*
    public void setNameLabel( String name, int width )
    {
        nameLabel.setText( name );
        //nameLabel.setSize( width, 16 );
        //nameLabel.setSize(nameLabel.getPreferredSize());
    }
*/
    public void setTitle( String name )
    {
        nameLabel.setText(name);
  //      nameLabel.setSize(nameLabel.getPreferredSize());
    }

    public Module getModule()
    {
        return module;
    }

    public DModule getModuleInfo()
    {
        return info;
    }

    /*
     * class RemoveModule implements ActionListener { public void
     * actionPerformed(ActionEvent e) {
     * getModule().getModuleSection().remove(getModule()); } }
     */

}
