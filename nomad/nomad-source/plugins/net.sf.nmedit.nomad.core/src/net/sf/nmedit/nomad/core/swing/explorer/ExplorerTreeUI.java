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
 * Created on Oct 29, 2006
 */
package net.sf.nmedit.nomad.core.swing.explorer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.plaf.TreeUI;
import javax.swing.plaf.metal.MetalTreeUI;
import javax.swing.tree.TreePath;

import net.sf.nmedit.nmutils.Platform;
import net.sf.nmedit.nomad.core.Nomad;
import net.sf.nmedit.nomad.core.swing.explorer.helpers.ExplorerCellRenderer;
import net.sf.nmedit.nomad.core.swing.explorer.helpers.TreeDynamicTreeExpansion;

public class ExplorerTreeUI extends MetalTreeUI
{
    private static Icon getIcon(String name)
    {
        URL url = ExplorerTreeUI.class.getClassLoader().getResource( "swing/browser/"+name);
        return url == null ? null : new ImageIcon(url);
    }

    private static Icon getIcon2(String name)
    {
        URL url = Nomad.sharedInstance().getClass()
        .getResource("/icons/tango/16x16/"+name);
        return url == null ? null : new ImageIcon(url);
    }

    static Icon openIcon = getIcon("node-state-opened.png");
    static Icon closedIcon = getIcon("node-state-closed.png");
    static Icon openIconHov = getIcon("node-state-opened-hovered.png");
    static Icon closedIconHov = getIcon("node-state-closed-hovered.png");

    public static final Icon DefaultFolderOpenedIcon = getIcon2("status/folder-open.png");
    public static final Icon DefaultFolderClosedIcon = getIcon2("places/folder.png");
    public static final Icon DefaultFileIcon = getIcon2("mimetypes/text-x-generic.png");
    //Icon leafIcon = new ImageIcon("net/sf/nmedit/nomad/cbrowser/images/...");
     
    public final static Color defaultSelectionBackground = Color.decode("#A8A8A8");
    private Color backgroundSelectionColor = null;
    private Color alternatingRowColor = null;

    public void installUI( JComponent c ) 
    {
        JTree tree = (JTree) c;
        tree.putClientProperty("JTree.lineStyle", "None");
        super.installUI( c );
        
        c.setBackground(Color.WHITE);
        alternatingRowColor = new Color(0xF0F0FF);
        
        tree.setRootVisible(false);
        ExplorerCellRenderer tcr = new ExplorerCellRenderer();
        tree.setCellRenderer(tcr);
        tcr.setOpenIcon(DefaultFolderOpenedIcon);
        tcr.setClosedIcon(DefaultFolderClosedIcon);
        tcr.setLeafIcon(DefaultFileIcon);
        tree.addTreeExpansionListener(new TreeDynamicTreeExpansion(tree));
        tree.setShowsRootHandles(true);
        tree.setScrollsOnExpand(false);
        
    }
    protected void installDefaults() 
    {
        //tree.putClientProperty("Tree.selectionBackground", defaultSelectionBackground);
        super.installDefaults();
        backgroundSelectionColor = defaultSelectionBackground;//UIManager.getColor("Tree.selectionBackground");
        setExpandedIcon(openIcon);
        setCollapsedIcon(closedIcon);
        
    }
    
		public void updateScrollPosition(Point location) {
		Rectangle visible = tree.getVisibleRect();
        int wScroll = Math.min(visible.width / 3, 40);
        int hScroll = Math.min(visible.height / 3, 40);
        // System.out.println("w " + wScroll + " h " + hScroll);
        
        Rectangle scrollTo = new Rectangle(location);
//        if (location.x < (visible.x + wScroll))
//        	scrollTo.x = Math.max(location.x - wScroll, 0);
//        if (location.x > (visible.x + visible.width - wScroll))
//        	scrollTo.x = location.x + wScroll;
//        
        if (location.y < (visible.y + hScroll))
        	scrollTo.y = Math.max(location.y - hScroll, 0);
        if (location.y > (visible.y + visible.height - hScroll))
        	scrollTo.y = location.y + hScroll;
        
        // System.out.println("location " + location.x + " " + location.y + " visible " + visible.x + " " + visible.y + " scrollto " + scrollTo.x + " " + scrollTo.y);
        
        tree.scrollRectToVisible(scrollTo);
	}
    


    protected void paintRow(Graphics g, Rectangle clipBounds,
                Insets insets, Rectangle bounds, TreePath path,
                int row, boolean isExpanded,
                boolean hasBeenExpanded, boolean isLeaf) 
    {

        // Don't paint the renderer if editing this row.
        if(editingComponent != null && editingRow == row)
            return;

        int h = tree.getRowHeight();
        if (tree.isRowSelected(row))
        {
            g.setColor(backgroundSelectionColor);
            g.fillRect(clipBounds.x, h*row, clipBounds.width, h );
            

            /*if(shouldPaintExpandControl(path, row, isExpanded,
                        hasBeenExpanded, isLeaf)) {
                        */
  //          }
        }
        else if (alternatingRowColor != null && row%2==0)
        {
            g.setColor(alternatingRowColor);
            g.fillRect(clipBounds.x, h*row, clipBounds.width, h );
        }
        paintExpandControl(g, bounds, insets, bounds,
                path, row, isExpanded,
                hasBeenExpanded, isLeaf);
        
        super.paintRow(g, clipBounds, insets, bounds, path, row, isExpanded,
                hasBeenExpanded, isLeaf);
    }
    
    public void paint(Graphics g, JComponent c)
    {
        super.paint(g, c);

        Insets i = tree.getInsets();
        int rc = getRowCount(tree);
        int rh = getRowHeight();
        
        if (rh < 1)
            return;
        
        if (rc%2==1)
            rc++;
        
        int y = i.top + rc*rh;
        int bottom = tree.getHeight()-i.bottom;

        g.setColor(alternatingRowColor);
        //int r = tree.getWidth()-i.left-i.right;
        while (y<bottom)
        {
            g.fillRect(i.left, y, tree.getWidth(), rh);
            y+=rh*2;
        }
    }

    ExpandControlHoverEffect eche = new ExpandControlHoverEffect();
    protected void installListeners() 
    {
        tree.addMouseListener(eche);
        tree.addMouseMotionListener(eche);
        super.installListeners();
    }

    protected void uninstallListeners() 
    {
        tree.removeMouseMotionListener(eche);
        tree.removeMouseListener(eche);
        super.uninstallListeners();
    }

    protected void paintExpandControl(Graphics g,
                      Rectangle clipBounds, Insets insets,
                      Rectangle bounds, TreePath path,
                      int row, boolean isExpanded,
                      boolean hasBeenExpanded,
                      boolean isLeaf) {
        //Object       value = path.getLastPathComponent();
    
        // Draw icons if not a leaf and either hasn't been loaded,
        // or the model child count is > 0.
        //if (!isLeaf && (treeModel.getChildCount(value) > 0)) 
        {
            if (hoveredRow==row)
            {
                setExpandedIcon(openIconHov);
                setCollapsedIcon(closedIconHov);
            }
            else
            {
                setExpandedIcon(openIcon);
                setCollapsedIcon(closedIcon);
            }
            
            super.paintExpandControl(g, clipBounds, insets, bounds, path, row, isExpanded,
                    hasBeenExpanded, isLeaf);
        }/*
        else {
            super.paintExpandControl(g, clipBounds, insets, bounds, path, row, isExpanded,
                    hasBeenExpanded, isLeaf);
        }*/
    }

    
    public Rectangle getPathBounds(JTree tree, TreePath path) {
    if(tree != null && treeState != null) {
        Insets           i = tree.getInsets();
        Rectangle        bounds = treeState.getBounds(path, null);

        if(bounds != null && i != null) {
        bounds.x += i.left;
        bounds.y += i.top;
        // we use the full row width instead of only the label bounds
        bounds.width = tree.getWidth()-i.right-bounds.x;
        //treeState.getBounds(path, bounds); // write back
        }
        return bounds;
    }
    return null;
    }
    
    public TreePath getClosestPathForLocation(JTree tree, int x, int y)
    {   
        Insets insets = tree.getInsets();
        int maxY = tree.getRowCount()*tree.getRowHeight()-insets.top;
        
        if (y>maxY)
            return null;
        
        return super.getClosestPathForLocation(tree, x, y);
    }

    protected void selectPathForEvent(TreePath path, MouseEvent event) {
    	if (getSelectionModel().isPathSelected(path) && Platform.isPopupTrigger(event)) {
    		// we don't want to deselect with right clicking
    		return;
    	}
    	super.selectPathForEvent(path, event);
    }
    
    protected boolean isToggleSelectionEvent(MouseEvent event) {
    	return Platform.isToggleSelectionEvent(event);
    }


    
    int hoveredRow = -1;
    int hovx = 0;
    int hovy = 0;
    
    private static class ExpandControlHoverEffect
        implements MouseMotionListener, MouseListener
    {
        public void mousePressed(MouseEvent e)
        {
        	forwardMouseEvent(e);

        	Component c = e.getComponent();
            if (!(c instanceof JTree)) return;
            JTree tree = (JTree) c;

            // avoid having a mouse press select, do it on mouse release on macosx
            if (Platform.isFlavor(Platform.OS.MacOSFlavor)) {
            	Point p = e.getPoint();
            	TreePath path = tree.getClosestPathForLocation(p.x, p.y);
            	if (path != null && tree.isPathSelected(path)) {
            		e.consume();
            	}
            }
        }

        public void mouseReleased(MouseEvent e)
        {
            forwardMouseEvent(e);
        }
        
        // ExpandControlHoverEffect
        public void mouseMoved(MouseEvent e)
        {
            Component c = e.getComponent();
            if (!(c instanceof JTree)) return;
            JTree tree = (JTree) c;
            TreeUI treeUI = tree.getUI();
            if (!(treeUI instanceof ExplorerTreeUI)) return;
            ExplorerTreeUI etUI = (ExplorerTreeUI) treeUI;
            
            int lastRow = etUI.hoveredRow;
            TreePath tp = 
                etUI.getClosestPathForLocation(tree, e.getX(), e.getY());
            if (tp == null)
                return ;
            
            int row = tree.getUI().getRowForPath(tree,tp);
            if (etUI.isLocationInExpandControl(
                    row, tp.getPathCount()-1, e.getX(), e.getY()
            ))
            {
                etUI.hoveredRow = row;
            }
            else
            {
                etUI.hoveredRow=-1;
            }

            if (etUI.hoveredRow!=lastRow)
            {
                tree.repaint(etUI.hovx-15, etUI.hovy-15,30,30);
                tree.repaint(e.getX()-15, e.getY()-15,30,30);

                etUI.hovx = e.getX();
                etUI.hovy = e.getY();
            }
        }

        public void mouseDragged(MouseEvent e)
        {
        		// nothing
        }

        public void mouseClicked(MouseEvent e)
        {
            forwardMouseEvent(e);
        }

        public void mouseEntered(MouseEvent e)
        {
            forwardMouseEvent(e);
        }

        public void mouseExited(MouseEvent e)
        {
            forwardMouseEvent(e);
        }

        public void forwardMouseEvent(MouseEvent e)
        {
            Component c = e.getComponent();
            if (!(c instanceof ExplorerTree))
                return;
            
            ExplorerTree tree = (ExplorerTree) c;
            TreePath tp = tree.getPathForLocation(e.getX(), e.getY());
            if (tp == null)
                return;
            Object o = tp.getLastPathComponent();
            
            if (o instanceof ETreeNode)
            {
                 ((ETreeNode)o).processEvent(e);
            }
        }
    }
    

}
