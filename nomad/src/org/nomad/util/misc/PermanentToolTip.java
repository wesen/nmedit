package org.nomad.util.misc;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Toolkit;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JWindow;
import javax.swing.UIManager;

public class PermanentToolTip extends JWindow {

	private JComponent component = null;
	private static PermanentToolTip singletone = null;
	private GridLayout gridLayout = new GridLayout();
	
	public PermanentToolTip(JComponent component) {
		if (singletone!=null) {
			singletone.removeTip();
		}
		singletone = this;
		this.component = component;
		setAlwaysOnTop(true);
		setSize(100,100);
		gridLayout.setColumns(2);
		gridLayout.setRows(0);
		gridLayout.setHgap(0);
		gridLayout.setVgap(0);
		
		JComponent pane = (JComponent) getContentPane();
		pane.setLayout(gridLayout);
		Color clTipBackground = UIManager.getColor("info");
		pane.setBackground(clTipBackground);
		pane.setBorder(BorderFactory.createLineBorder(clTipBackground.darker()));
	}
	
	public void removeTip() {
		singletone = null;
		setVisible(false);
		dispose();
	}
	
	private JLabel createLabel(String text) {
		JLabel label = new JLabel(text);
		label.setFont(new Font("Arial", Font.BOLD, 11));
		label.setForeground(Color.BLACK);
		return label;
	}
	
	private JLabel createValueLabel(String text) {
		JLabel label = new JLabel(text);
		label.setFont(new Font("Arial", Font.PLAIN, 11));
		label.setForeground(Color.BLUE);
		return label;
	}
	
	private JComponent emptyBox() {
		return new JLabel("");
	}

	public void addProperty(String label, JComponent display) {
		if (label==null) {
			getContentPane().add(emptyBox());
		} else {
			getContentPane().add(createLabel(label));
		}
		if (display==null) {
			getContentPane().add(emptyBox());
		} else {
			getContentPane().add(display);
		}
	}

	public JLabel addProperty(String label, String value) {
		JLabel lblComponent = createValueLabel(value);
		addProperty(label, lblComponent);
		return lblComponent;
	}
	
	public void showTip() {
		
		setSize(gridLayout.preferredLayoutSize(this));
		
		final int space = 10; // space from component in pixel
		
		// reference point 'top left', 'bottom right' 
		Point clocTL = component.getLocationOnScreen();
		/*Point clocBR = new Point(clocTL.x+component.getWidth(),
			clocTL.y+component.getHeight());*/

		int[] xvector = new int[]{clocTL.x-getWidth()-space,
			clocTL.x+(component.getWidth()-getWidth())/2,
			clocTL.x+component.getWidth()+space
		};
		int[] yvector = new int[]{clocTL.y-getHeight()-space,
			clocTL.y+(component.getHeight()-getHeight())/2,
			clocTL.y+component.getHeight()+space
		};
		
		Dimension sc = Toolkit.getDefaultToolkit().getScreenSize();
		Point scCenter = new Point(sc.width/2, sc.height/2);
		
		boolean[] xvalid = new boolean[] {
			xvector[0]>=0, true, xvector[2]<sc.width 	
		};
		xvalid[1] = xvalid[0] && xvalid[2];
		
		boolean[] yvalid = new boolean[] {
			yvector[0]>=0, true, yvector[2]<sc.height 	
		};
		yvalid[1] = yvalid[0] && yvalid[2];
		
		boolean[][] valid = new boolean[][] {
			{xvalid[0]&&yvalid[0], xvalid[1]&&yvalid[0], xvalid[2]&&yvalid[0]},
			{xvalid[0]&&yvalid[1], false,                xvalid[2]&&yvalid[1]},
			{xvalid[0]&&yvalid[2], xvalid[1]&&yvalid[2], xvalid[2]&&yvalid[2]}
		};

		// now check where the largest weights are
		int currentWeight = 0;
		int maxX = 0;
		int maxY = 0;
		
		for (int i=0;i<3;i++) {
			for (int j=0;j<3;j++) {
				int weight = !valid[i][j] ? 0
						: scCenter.x-Math.abs(scCenter.x-(xvector[j]+getWidth() /2))
						 +scCenter.y-Math.abs(scCenter.y-(yvector[i]+getHeight()/2));

				if (weight>currentWeight) {
					maxX = j;
					maxY = i;
					currentWeight = weight;
				}
				//System.out.print(weight+" - ");
			}
			//System.out.println();
		}
		//System.out.println("x,y="+maxX+","+maxY+" ... "+currentWeight);
		
		// maxX, maxY is the vector to use
		setLocation(xvector[maxX], yvector[maxY]);
		setVisible(true);
	}
	
}
