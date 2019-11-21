package com.dreamfabric;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import javax.swing.event.*;

public class DTest
{
	public static void main(String[] args) 
	{
	    JFrame win = new JFrame("DTest!");
	    win.getContentPane().setLayout(new BorderLayout());
	    win.setSize(120,140);

	    JPanel panel = new JPanel(new BorderLayout());
	    panel.setBackground(new Color(200,200,255));
	    win.getContentPane().add(panel, BorderLayout.CENTER);

	    DKnob ts;
	    JLabel jl;
	    ChangeListener cl;
	    panel.add(ts = new DKnob(), BorderLayout.CENTER);
	    panel.add(jl = new JLabel("Volume: 0"), BorderLayout.NORTH);
	    ts.setValue((float)1.0);
	    final JLabel jla = jl;
	    ts.addChangeListener(new ChangeListener() {
		    public void stateChanged(ChangeEvent e) {
			DKnob t = (DKnob) e.getSource();
			int vol;
			jla.setText("Volume: " + (vol = (int)(15 * t.getValue())));
		    }
		});
	    win.show();
	}
}


