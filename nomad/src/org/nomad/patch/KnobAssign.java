package org.nomad.patch;

class KnobAssign {

//1 section index: 0 = common section, 1 = poly section, 2 = morph section (the morph has module index 1, section index 2)
//4 knob index: 0 = knob 1, ..., 17 = knob 18, 19 = Pedal, 20 = Aftertouch, 22 = On/Off switch

	private int section, module, parameter, knob;

	KnobAssign(int newSection, int newModule, int newParameter, int newKnob) {
		section = newSection;
		module = newModule;
		parameter = newParameter;
		knob = newKnob;
	}

// Getters

	public int getSection() {
		return section;
	}
	
	public int getModule() {
		return module;
	}
	
	public int getParameter() {
		return parameter;
	}
	
	public int getKnob() {
		return knob;
	}
}	

///* 
// * DKnob example
// * (c) 2000, Joakim Eriksson, 
// * Instructions at:  
// * http://www.dreamfabric.com/java/knob/knob.html
// * Please e-mail joakim@dreamfabric.com for comments or
// * questions.
// */
//
//import java.applet.Applet;
//import java.awt.*;
//import javax.swing.*;
//import javax.swing.event.*;
//
//public class TestKnob extends Applet
//{
//	public void start() {
//		DKnob knob;
//		JLabel jl;
//
//		setLayout(new BorderLayout());	    
//		JPanel jp = new JPanel(new BorderLayout());
//		jp.add(knob = new DKnob(), BorderLayout.CENTER);
//		jp.add(jl = new JLabel("Value: 0"), BorderLayout.NORTH);
//		add(jp, BorderLayout.CENTER);
//		final JLabel jla = jl;
//
//		// Add a change listener to the knob
//		knob.addChangeListener(new ChangeListener() {
//			public void stateChanged(ChangeEvent e) {
//			DKnob t = (DKnob) e.getSource();
//			jla.setText("Value: " + 
//					((int)(100 * t.getValue()))/100.0 );
//			}
//		});
//	}
//}
