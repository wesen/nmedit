package org.nomad.patch.ui;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Transparency;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

import javax.swing.BorderFactory;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import org.nomad.patch.Connector;
import org.nomad.patch.Module;
import org.nomad.patch.event.ModuleChangeAdapter;
import org.nomad.patch.ui.action.RemoveCablesAction;
import org.nomad.patch.ui.action.RemoveConnectedAction;
import org.nomad.patch.ui.action.RemoveModuleAction;
import org.nomad.theme.ModuleComponent;
import org.nomad.theme.component.ModuleGuiTitleLabel;
import org.nomad.theme.component.NomadComponent;
import org.nomad.util.graphics.ImageToolkit;
import org.nomad.util.iterate.ComponentIterator;
import org.nomad.util.misc.NomadUtilities;
import org.nomad.xml.dom.module.DModule;

public class ModuleUI extends NomadComponent implements ModuleComponent
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

    private final static LocationChangedAction locationListener = new LocationChangedAction();

    // --- drag
    private int dragX          = 0, dragY = 0;

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
                            dragX = event.getX();
                            dragY = event.getY();

                            m.setDraggingEnabled( true );
                        }
                        else if (event.isPopupTrigger())
                        {   // check if a popup should be shown
                            m.showPopup( event );
                        }
                    }
                    
                }
                break ;
            case MouseEvent.MOUSE_RELEASED:
                {
                final ModuleUI m = (ModuleUI) event.getComponent();
                if (m.draggingEnabled)
                {
                    m.setDraggingEnabled( false );

                    Point l = Metrics.getGridLocation( m );

                    if (m.getX() % Metrics.WIDTH - Metrics.WIDTH_DIV_2 >= 0) l.x++;

                    l.x = Math.max( 0, l.x );

                    m.getModule().setLocation( l );
                    Metrics.setModuleUILocation( m, m.getModule() );
                    m.getModuleSection().getModuleSection().rearangeModules(
                            m.getModule() );

                    scrollTo( m );
                }
                }
               break;
        }
        
        super.processMouseEvent(event);
    }

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
    }

    private void scrollTo( ModuleUI m )
    {
        Rectangle bounds = m.getBounds();
        NomadUtilities.enlarge( bounds, 20 );
        bounds.x = Math.max( bounds.x, 0 );
        bounds.y = Math.max( bounds.y, 0 );
        m.getModuleSection().scrollRectToVisible( bounds );
    }

    public ModuleUI( DModule info )
    {
        this.info = info;
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
            screen = getGraphicsConfiguration().createCompatibleImage(
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
                ImageToolkit.clearRegion( sharedG2, 0, 0, sw, sh );
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
    }

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
    }

    private ArrayList<NomadComponent> dirtyComponents = new ArrayList<NomadComponent>();
    
    public void registerDirtyComponent( NomadComponent component )
    {
        if (!dirtyComponents.contains(component))
            dirtyComponents.add(component);
    }

    public void buildDefaultPopup( JPopupMenu menu )
    {
        menu.add( new RemoveCablesAction( this ) );
        menu.addSeparator();
        menu.add( new RemoveModuleAction( this ) );
    }

    public void buildConnectorPopup( JPopupMenu menu, Connector c )
    {
        menu.add( new RemoveConnectedAction( this, c ) );
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

    public void setLocation( int x, int y )
    {
        super.setLocation( x, y );
        if (moduleSection != null)
        {
            moduleSection.locationSet( this );
        }
    }

    public ModuleUI( Module module )
    {
        this( module.getInfo() );
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
        module.removeModuleListener( locationListener );

        for (Iterator<NomadComponent> iter = new ComponentIterator<NomadComponent>(
                NomadComponent.class, this ); iter.hasNext();)
        {
            iter.next().unlink();
        }
    }

    public void removeCables()
    {
        getModuleSection().getModuleSection().removeCables( module );
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
        module.addModuleListener( locationListener );
    }

    private static class LocationChangedAction extends ModuleChangeAdapter
    {
        public void locationChanged( Module module )
        {
            Metrics.setModuleUILocation( module.getUI(), module );
        }
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

    public void setNameLabel( String name, int width )
    {
        nameLabel.setSize( width, 16 );
        nameLabel.setText( name );
    }

    public Module getModule()
    {
        return module;
    }

    public DModule getModuleInfo()
    {
        return info;
    }

    private boolean draggingEnabled = false;

    private void setDraggingEnabled( boolean enable )
    {
        if (draggingEnabled != enable)
        {
            draggingEnabled = enable;
            getModuleSection().setDraggedComponent( enable ? this : null );
        }
    }

    /*
     * class RemoveModule implements ActionListener { public void
     * actionPerformed(ActionEvent e) {
     * getModule().getModuleSection().remove(getModule()); } }
     */

}
