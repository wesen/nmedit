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
 * Created on Feb 24, 2006
 */
package org.nomad.util.shape;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import javax.swing.SwingUtilities;
import javax.swing.Timer;

import org.nomad.util.array.Array2D;
import org.nomad.util.graphics.Repainter;
import org.nomad.util.misc.NomadUtilities;

public class ShapeRenderManager<T extends Shape> extends Array2D<ShapeContainer<T>> {

	private HashMap<T, ShapeInfo<T>> map;
	private HashMap<T, ShapeInfo<T>> directMap;
	private ShapeRenderInfo renderInfo;
	private ShapeAdder<T> adder ;
	private ShapeRemover<T> remover ;
	private ShapeModifier<T> modifier ;
	private ShapeRefresher<T> refresher;
	private int updateCount = 0;

	private int updateSegL = 0;
	private int updateSegR = 0;
	private int updateSegT = 0;
	private int updateSegB = 0;
	private boolean modified = false;
	
	private Rectangle paintRect = new Rectangle(); // used for painting

	private Repainter repainter ;
	private ShapeRenderer<T> renderer;
	private ShapeRenderer<T> directRenderer;
	
	private RenderQueueProcessor<T> renderQueueProcessor ;
	
	private ArrayList<ShapeContainer<T>> renderQueue = new ArrayList<ShapeContainer<T>>();
	
	public ShapeRenderManager(ShapeRenderInfo renderInfo, Repainter repainter,
			ShapeRenderer<T> renderer, ShapeRenderer<T> directRenderer) {
		this.renderInfo = renderInfo;
		this.repainter = repainter;
		this.renderer = renderer;
		this.directRenderer = directRenderer;
		this.renderQueueProcessor = new RenderQueueProcessor<T>(this);
		adder 	= new ShapeAdder<T>(this);
		remover = new ShapeRemover<T>(this);
		modifier = new ShapeModifier<T>(this);
		refresher =  new ShapeRefresher<T>(this);
		map = new HashMap<T, ShapeInfo<T>>();
		directMap = new HashMap<T, ShapeInfo<T>>();
	}
	
	private int renderQueueStepSize = 10; // how many items are processed per call
	
	public void setShapeVisible(T shape, boolean visible) {
		ShapeInfo<T> info = map.get(shape) ;
		//if (info.isVisible() ^ visible) {
		beginUpdate();
			info.setVisible(visible) ;
			iterate(info, refresher);
		endUpdate();
		//}
	}
	
	public boolean isShapeVisible(T shape) {
		return map.get(shape).isVisible() ;
	}
    
	private Rectangle dirtyRegions = new Rectangle(0,0,0,0);
	
	private boolean processRenderQueue() {
		int cnt = Math.min(renderQueueStepSize, renderQueue.size());
        
        if (cnt>0)
        {
            dirtyRegions.setBounds(0,0,0,0);
        
    		while (cnt>0) 
            {
    			ShapeContainer<T> c = renderQueue.remove(0);
    			c.setHighQualityRendering(true);
    			c.forceRendering(renderInfo, renderer);
                
                SwingUtilities.computeUnion(c.getPxX(), c.getPxY(), renderInfo.getSegmentSize(), renderInfo.getSegmentSize(), dirtyRegions);
    			cnt --;
    		} 
            repainter.addDirtyRegion(dirtyRegions.x, dirtyRegions.y, dirtyRegions.width, dirtyRegions.height);
        }
		
		return renderQueue.size()>0;
	}
	
	public boolean isUpdating() {
		return updateCount>0;
	}
	
	public boolean containsDirectRenderedShape(T t) {
		return directMap.containsKey(t);
	}
	
	public boolean containsShape(T t) {
		return map.containsKey(t);
	}
	
	public ShapeRenderInfo getInfo() {
		return renderInfo;
	}
	
	protected ShapeContainer<T>[] newArray(int size) {
		return new ShapeContainer[size];
	}
	
	protected ShapeContainer<T> newCell(int x, int y) {
		return new ShapeContainer<T>(renderInfo.scaleToPixel(x), renderInfo.scaleToPixel(y));
	}

	public boolean hasDirectRendered() {
		return !directMap.isEmpty();
	}

	protected void iterate(ShapeInfo<T> info, CellCallback cb) {
		int[] coords = info.getSegmentMask();
		int sz       = info.getMaskSize()<<1; // *2
		
		for (int i=0;i<sz;i+=2) 
			cb.cell(coords[i], coords[i+1]);
	}

	public void transformDirectRendering() {
		if (hasDirectRendered()) {
			beginUpdate();
			
			for (T t : directMap.keySet()) {
				ShapeInfo<T> info = directMap.get(t);
				map.put(t, info);
				add(info);
				iterate(info, modifier);
			}
			
			directMap.clear();
			endUpdate();
		}
	}
	
	public void addDirectRendered(T t) {
		beginUpdate();
		ShapeInfo<T> info = new ShapeInfo<T>(t);
		directMap.put(t, info);
		info.update(renderInfo);
		iterate(info, modifier);
		endUpdate();
	}

	public void removeDirectRendered(T t) {
		beginUpdate();
		ShapeInfo<T> info = directMap.remove(t);
		if (info!=null) 
			iterate(info, modifier);
		endUpdate();
	}
	
	public void add(T t) {
		ShapeInfo<T> info = new ShapeInfo<T>(t);
		map.put(t, info);
		info.update(renderInfo);
		beginUpdate();
		add(info);
		endUpdate();
	}
	
	public void remove(T t) {
		ShapeInfo<T> info = map.remove(t);
		if (info!=null) {
			beginUpdate();
			remove(info);
			endUpdate();
		}
	}
	
	public void update(T t) {
		ShapeInfo<T> info = map.get(t);
		if (info!=null) {
			beginUpdate();
			
			remove(info);
			
			info.update(renderInfo);
			add(info);
			
			endUpdate();
		} else {
			info = directMap.get(t);
			if (info!=null) {
				beginUpdate();
				iterate(info, modifier);
				info.update(renderInfo);
				iterate(info, modifier);
				endUpdate();
			}
		}
	}
	
	public void endUpdate() {
		if (updateCount  == 0)
			throw new IllegalStateException("endUpdate without beginUpdate");
		--updateCount;
		update();
	}
    
	public void paint(Graphics g) {
		g.getClipBounds(paintRect);
		
			renderInfo.scaleToSegment(paintRect);
			NomadUtilities.enlarge(paintRect, 1); // make it a bit larger so that scaling errors are gone
			paintRect.x      = Math.max(paintRect.x, 0);
			paintRect.y      = Math.max(paintRect.y, 0);

		int r = Math.min(paintRect.x+paintRect.width -1, getWidth() -1);
		int b = Math.min(paintRect.y+paintRect.height-1, getHeight()-1);

		for (int i=paintRect.x;i<=r;i++)
			for (int j=paintRect.y;j<=b;j++)
				getCell(i, j).paint(g, renderInfo, renderer);
	}
	
	public void paintDirect(Graphics g) {
		// direct rendering
		Set<T> set = directMap.keySet();
		if (!set.isEmpty()) {
			Graphics2D g2 = (Graphics2D) g;
			for (T t : set)
				directRenderer.paint(g2, t);
		}
	}
	
	private void update() {
		if (updateCount==0) {
			modified = false;
			// make sure update region might not be in bounds
			// this must be ignored due to the case that cells were removed
			// and so the grid size is smaller than the update region
			/* updateSegL = Math.max(updateSegL, 0);
			updateSegT = Math.max(updateSegT, 0);
			updateSegR = Math.min(updateSegR, getWidth());
			updateSegB = Math.min(updateSegB, getHeight()); */
			
			paintRect.setBounds(
				renderInfo.scaleToPixel(updateSegL),
				renderInfo.scaleToPixel(updateSegT),
				renderInfo.scaleToPixel(updateSegR-updateSegL+1),
				renderInfo.scaleToPixel(updateSegB-updateSegT+1)		
			);
			
			NomadUtilities.enlargeToGrid(paintRect, renderInfo.getSegmentSize());
			
			repainter.addDirtyRegion( paintRect.x, paintRect.y, paintRect.width, paintRect.height );
			
			renderQueueProcessor.start();
			
		}
	}

	private void setModified(int x, int y) {
		if (!modified) {
			updateSegL = x;
			updateSegR = x;
			updateSegT = y;
			updateSegB = y;
			modified = true;
		} else {
			updateSegL = Math.min(updateSegL, x);
			updateSegR = Math.max(updateSegR, x);
			updateSegT = Math.min(updateSegT, y);
			updateSegB = Math.max(updateSegB, y);
		}
	}
	
	void beginUpdate() {
		updateCount ++;
		renderQueueProcessor.stop();
	}
	
	private void addToCell(int x, int y, ShapeInfo<T> t) {
		ShapeContainer<T> cell = getCell(x, y);
		cell.add(t);
		setModified(x, y);
		
		if (!renderQueue.contains(cell)) {
			cell.setHighQualityRendering(false);
			renderQueue.add(cell);
		}
	}

	private void removeFromCell(int x, int y, ShapeInfo<T> t) {
		ShapeContainer<T> cell = getCell(x, y);
		if(cell.remove(t)) {
			setModified(x, y);
		}
	}
	
	private void add(ShapeInfo<T> t) {
		Rectangle bounds = t.getSegBounds();
		
		int minw = bounds.x+bounds.width -1 +2;
		int minh = bounds.y+bounds.height-1 +2;
		
		// assure cells are available
		if (getWidth()<minw||getHeight()<minh) {
			resize(Math.max(getWidth(), minw), Math.max(getHeight(), minh));
		}

		adder.sinfo = t;
		iterate(t, adder);
	}
	
	private void remove(ShapeInfo<T> t) {
		remover.sinfo = t;
		iterate(t, remover);
	}

	public Iterator<T> getDirectRendered() {
		return directMap.keySet().iterator();
	}
	
	public Iterator<T> getBuffered() {
		return map.keySet().iterator();
	}

	public void clear() {
		beginUpdate();
		directMap.clear();
		map.clear();
		resize(0);
		endUpdate();
	}
	
	private static class RenderQueueProcessor<T extends Shape> 
		implements ActionListener {
		ShapeRenderManager<T> manager;
		Timer timer;
		public RenderQueueProcessor(ShapeRenderManager<T> m) {
			final int delay=10;
			this.manager = m;
			timer = new Timer(delay, this);
			timer.setRepeats(true);
			timer.setDelay(delay);
			timer.setCoalesce(true); // warning: must not be false . See javadocs
		}

		public void stop() {
			if (timer.isRunning()) {
				timer.stop();
			}
		}
		
		public void start() 
        {
			if ((!manager.renderQueue.isEmpty())&&(!timer.isRunning())) {
				timer.start();
			}
		}

		public void actionPerformed(ActionEvent event) {
			if (manager.isUpdating()) {
				stop();
			} else {
				if (!manager.processRenderQueue()) {
					stop();
				}
			}
		}
	}
	
	private abstract static class ShapeCellCallback<T extends Shape> implements CellCallback {
		ShapeRenderManager<T> manager;
		public ShapeCellCallback(ShapeRenderManager<T> m) {
			this.manager = m;
		}
	}
	
	private static class ShapeAdder<T extends Shape> extends ShapeCellCallback<T> {
		ShapeInfo<T> sinfo;
		public ShapeAdder(ShapeRenderManager<T> m) { super(m); }
		public void cell(int x, int y) {
			if (manager.hasCell(x, y)) manager.addToCell(x, y, sinfo);
		}
	}
	
	private static class ShapeRemover<T extends Shape> extends ShapeCellCallback<T> {
		ShapeInfo<T> sinfo;
		public ShapeRemover(ShapeRenderManager<T> m) { super(m); }
		public void cell(int x, int y) {
			if (manager.hasCell(x, y)) manager.removeFromCell(x, y, sinfo);
		}
	}
	
	private static class ShapeModifier<T extends Shape> extends ShapeCellCallback<T> {
		public ShapeModifier(ShapeRenderManager<T> m) { super(m); }
		public void cell(int x, int y) { manager.setModified(x, y); }
	}
	
	private static class ShapeRefresher<T extends Shape> extends ShapeModifier<T> {
		public ShapeRefresher(ShapeRenderManager<T> m) { super(m); }
		public void cell(int x, int y) { manager.getCell(x,y).setModified(); super.cell(x,y); }
	}
	
}
