package editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import nomad.model.descriptive.DConnector;
import nomad.model.descriptive.DModule;
import nomad.model.descriptive.DParameter;
import nomad.patch.ModuleSection.ModulePixDimension;

public class WorkBenchPane extends JPanel {

	DModule module = null;
	private ModulePane modulePane = null;
	private OptionsTablePane optionsTable=null;
	
	public WorkBenchPane(OptionsTablePane optionsTable) {
		this.setBackground(Color.LIGHT_GRAY);//.DARK_GRAY);
		this.setBorder(BorderFactory.createEmptyBorder(100,50,100,50));
		this.setPreferredSize(new Dimension(100+ModulePixDimension.PIXWIDTH,300));
		this.optionsTable = optionsTable;
	}
	
	public void setModule(DModule module) {
		this.module=module;

		if (modulePane!=null) {
			this.remove(modulePane);
		}
		
		modulePane = new ModulePane(module);
		modulePane.setForeground(Color.GRAY);
		modulePane.setSize(new Dimension(ModulePixDimension.PIXWIDTH, 
				module.getHeight()*ModulePixDimension.PIXHEIGHT*2));
		modulePane.setPreferredSize(new Dimension(ModulePixDimension.PIXWIDTH, 
				module.getHeight()*ModulePixDimension.PIXHEIGHT*2));
		modulePane.setBorder(BorderFactory.createRaisedBevelBorder());
		
		addUIComponents();
		
		this.add(BorderLayout.CENTER, modulePane);
		this.updateUI();
		
		optionsTable.setModulePane(modulePane);
	}

	void addUIComponents() {
		int pad=5;
		int line=0;
		int lineHeight=17;
		//modulePane.setLayout(new BoxLayout(modulePane, BoxLayout.Y_AXIS));
		modulePane.setLayout(null);
		JLabel label;
		label=new JLabel(new ImageIcon(module.getIcon()));
		label.setLocation(pad,pad);
		label.setSize(16,16);
		modulePane.add(label);
		label=new JLabel(module.getName());
		label.setFont(new Font("Dialog", Font.BOLD, 10));
		label.setSize(10*module.getName().length(),16);
		label.setLocation(pad+16+10,pad);
		modulePane.add(label);
		
		line++;
		
		for (int i=0;i<module.getParameterCount();i++) {
			DParameter info = module.getParameter(i);
			//new ParamView(modulePane,info,pad,pad+line*lineHeight);
			
			
			ParamSlider slider=new ParamSlider(info);
			slider.setLocation(pad+110,pad+line*lineHeight);
			slider.setSize(100, 16);
			modulePane.add(slider);
			
			ListenChanges listener=new ListenChanges(slider);
			listener.stateChanged(null);		
			modulePane.setParamControl(info, slider);
			
			line++;
		}
		
		if (module.getConnectorCount()>0) {
			ConnectorRow.addRow(module, modulePane, pad, line, lineHeight);
		}
		
		modulePane.validate();
		modulePane.updateUI();
	}
	
	private class ListenChanges implements ChangeListener {
		private ParamSlider slider = null;
		public ListenChanges(ParamSlider slider) {
			this.slider=slider;
		}
		public void stateChanged(ChangeEvent arg0) {
			String formatted = slider.toString(slider.getValue()); 
			slider.setToolTipText(formatted);
		}
	}
	
}

class ParamSlider extends JSlider {
	
	private DParameter info;

	public ParamSlider(DParameter info) {
		new Draggable(this);
		//this.setBackground(Color.DARK_GRAY);
		this.info = info;
		this.setOrientation(JSlider.HORIZONTAL);
		this.setMinimum(0);
		this.setMaximum(info.getNumStates()-1);
		this.setValue(info.getDefaultValue());
		//this.setPreferredSize(new Dimension(60, this.getHeight()));
	}

	public String toString() {
		return toString(this.getValue());
	}
	public String toString(int value) {
		return info.getName()+": "+info.getFormattedValue(value);
	}
	
}

class ConnectorRow {
	
	public static void addRow(DModule module, JPanel target, int pad, int line, int lineHeight)  {
		for (int i=0;i<module.getConnectorCount();i++) {
			JPanel pp = new ConnectorPane(module.getConnector(i));
			
			pp.setSize(16,16);
			pp.setLocation(pad+i*18,pad+line*lineHeight);
			pp.validate();
			target.add(pp);
		
		}
	}
}

class ConnectorPane extends JPanel {
	private DConnector connector ;
	private int w;
	private int h;
	public ConnectorPane(DConnector connector) {
		new Draggable(this);
		this.connector = connector;
		Image img = connector.getIcon(true);
		w = img.getWidth(null);
		h = img.getHeight(null);
		this.setPreferredSize(new Dimension(w, h));
		this.setToolTipText(connector.getName()+","+connector.getConnectionTypeName()+","+
				connector.getSignalName());
	}
	
	public void paint(Graphics g) {
		g.drawImage(connector.getIcon(true),0,0,w,h,null);
	}
	
}

class Draggable extends MouseAdapter implements MouseMotionListener {
    Point mLastPoint;
    Component mWindow;
    public Draggable(Component w) {
        w.addMouseMotionListener(this);
        w.addMouseListener(this);
        mWindow = w;
    }
    public void mousePressed(MouseEvent me) {
        mLastPoint = me.getPoint();
    }
    
    public void mouseReleased(MouseEvent me) {
        mLastPoint = null;
    }
    public void mouseMoved(MouseEvent me) {}
    public void mouseDragged(MouseEvent me) {
        int x, y;
        
        if (mLastPoint != null) {
            x = mWindow.getX() + (me.getX() - (int)mLastPoint.getX());
            y = mWindow.getY() + (me.getY() - (int)mLastPoint.getY());
            mWindow.setLocation(x, y);
        }
    }
}