package nomad.graphics;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.MemoryImageSource;

public abstract class BackgroundRenderer {

	private Image bimage = null;
	private Dimension dim = new Dimension(0,0);
	private boolean forceRender = true;

	public BackgroundRenderer() {
		super();
	}
	
	public void forceRender() {
		forceRender = true;
	}
	
	protected void hasRendered() {
		forceRender = false;
	}
	
	protected boolean isRenderingForced() {
		return forceRender;
	}

	protected void render(Component comp, Dimension dim) {
		bimage = renderImage(comp, dim); // setup
		this.dim = dim;
		hasRendered();
	}
	
	protected void checkRender(Component comp, Dimension dim) {
		if ((isRenderingForced()||(!this.dim.equals(dim)))&&dim.width>0&&dim.height>0)
			render(comp, dim);
	}
	
	public boolean hasAlpha() {
		return false;
	}

	protected Image renderImage(Component comp, Dimension dim) {
		// create image
		return createImage(dim, hasAlpha());
	}

	protected BufferedImage createImage(Dimension dim, boolean hasAlpha) {
		return new BufferedImage(dim.width, dim.height,
			hasAlpha?BufferedImage.TYPE_INT_ARGB:BufferedImage.TYPE_INT_RGB);
	}

	protected Image createImage(Component comp, Dimension dim, int [] pixels) {
	    return comp.createImage(
	      new MemoryImageSource(
	        dim.width, dim.height,
	        ColorModel.getRGBdefault(),
	        pixels, 0, dim.width
	      )
	    );
	}
	
	protected boolean isSetupFor(Dimension dim) {
		return dim.equals(this.dim);
	}

	public Dimension getSize() {
		return dim;
	}
	
	protected Image getBuffer() {
		return bimage;
	}

	public void drawTo(Component comp, Dimension dim, Graphics g) {
		checkRender(comp, dim);
		if (bimage!=null)
			g.drawImage(bimage, 0, 0, comp);
	}

}
