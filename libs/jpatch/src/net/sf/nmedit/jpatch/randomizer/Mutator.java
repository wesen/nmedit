/* Copyright (C) 2006-2007 Julien Pauty
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
package net.sf.nmedit.jpatch.randomizer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Mutator {
	
	private double probability = .65, range = .15;
	
	private Variation variations[] = new Variation[4];
	private Variation mother = new Variation(60), father = new Variation(60);
	
	public Color colorValue(int hue)
	{	
		return Color.getHSBColor(hue/127f, 1f, 0.8f);
	}
	
	public Mutator()
	{
		for (int i = 0 ; i < variations.length; i ++)
		{
			variations[i]= new Variation();
		}
		
		createUI();
	}

    private static <T extends JComponent> T centerx(T c){
        c.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        return c;
    }
    private static <T extends JComponent> T right(T c){
        c.setAlignmentX(JComponent.RIGHT_ALIGNMENT);
        return c;
    }
    private <T extends JComponent> T left(T c){
        c.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        return c;
    }
    private <T extends JComponent> T bottom(T c){
        c.setAlignmentY(JComponent.BOTTOM_ALIGNMENT);
        return c;
    }
	static final int STRUT = 4;
	
	private Component createFiller()
	{
	    // note: do not add the same component more than one time to a parent
	    //return new Box.Filler(new Dimension(10,10),new Dimension(50,50),new Dimension(10,10));
	    return Box.createRigidArea(new Dimension(0, 10));
	}
	
	private void createUI() {
		JFrame f = new JFrame("Mutator test");
		f.setResizable(false);
	    f.setBounds(30,30,400,320);
	    JPanel cp = new JPanel();
	    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    cp.setLayout(new BoxLayout(cp,BoxLayout.PAGE_AXIS));
	    f.setContentPane(cp);
	    cp.setBorder(BorderFactory.createEmptyBorder(2,4,2,4));
	    
	    JPanel variationStore = new JPanel(new GridLayout(3,6, 2, 2));
	  
	    
	    final JSpinner rangeSpinner = new JSpinner( new SpinnerNumberModel(10, 0, 50, 1));
	   
	    Box rangeBox = Box.createVerticalBox();
	    
	    rangeSpinner.setValue((int)(range*50));
	    rangeSpinner.addChangeListener(new ChangeListener(){
	    	public void stateChanged(ChangeEvent e) {
	    		range = (Integer)(rangeSpinner.getValue())/50d;	
	    		System.out.println(range + " = range");
	    	}
	    });	    

	    rangeBox.add(Box.createVerticalGlue());
        rangeBox.add(bottom(left(new JLabel("Range"))));
	    rangeBox.add(bottom(left(rangeSpinner)));
	    
	    Box probBox = Box.createVerticalBox();
	    
	    final JSpinner probSpinner = new JSpinner(new SpinnerNumberModel(10, 0, 100, 1));
	    probSpinner.setValue((int)(probability*100));
	    probSpinner.addChangeListener(new ChangeListener(){
	    	public void stateChanged(ChangeEvent e) {
	    		probability = (Integer)(probSpinner.getValue())/100d;	
	    		System.out.println(probability+" = prob");
	    	}
	    });	    

	    probBox.add(Box.createVerticalGlue());
        probBox.add(left(new JLabel("Prob.")));
	    probBox.add(left(probSpinner));
        probBox.setMaximumSize(new Dimension(100, Short.MAX_VALUE));
        rangeBox.setMaximumSize(new Dimension(100, Short.MAX_VALUE));
	    
	    Box spinBox = bottom(Box.createHorizontalBox());
	    
	    spinBox.add(left(probBox));
	    spinBox.add(Box.createHorizontalStrut(STRUT));
	    spinBox.add(left(rangeBox));
        spinBox.add(Box.createHorizontalStrut(STRUT));
	    
	    Box varBox = Box.createHorizontalBox();

        Box parentBox;
        
        parentBox = bottom(left(Box.createVerticalBox()));
        parentBox.add(bottom(centerx(new JLabel("Mother"))));
        parentBox.add(bottom(centerx(mother)));
        Box motherBox = parentBox;

        parentBox = bottom(right(Box.createVerticalBox()));
        parentBox.add(bottom(centerx(new JLabel("Father"))));
        parentBox.add(bottom(centerx(father)));
        Box fatherBox = parentBox;
	    
	    varBox.add(motherBox);
        varBox.add(Box.createHorizontalGlue());

        Box childrenCaptionBox = bottom(centerx(Box.createHorizontalBox()));
        Box childrenVariationBox = bottom(centerx(Box.createHorizontalBox()));

        childrenCaptionBox.add(centerx(bottom(new JLabel("Children"))));
        
        Box childrenBox = bottom(centerx(Box.createVerticalBox()));
        childrenBox.add(childrenCaptionBox);
        childrenBox.add(childrenVariationBox);
        
	    for (int i  = 0; i < variations.length; i++)
	    {
	        if (i>0) childrenVariationBox.add(Box.createHorizontalStrut(4));
	        childrenVariationBox.add(left(bottom(variations[i])));
	    }
	    varBox.add(childrenBox);
        varBox.add(Box.createHorizontalGlue());
	    varBox.add(fatherBox);

	    f.getContentPane().add(spinBox);
        f.getContentPane().add(Box.createVerticalGlue());
	    f.getContentPane().add(varBox);
	    f.getContentPane().add(createFiller());
	    f.getContentPane().add(variationStore);
	    
	    
	    
	    JButton randomizeBut = bottom(right(new JButton("Randomize")));
	    randomizeBut.addActionListener(new ActionListener(){
	    	public void actionPerformed(ActionEvent arg0) {
	    		randomize();	    		
	    	}
	    });
	    
	    JButton mutateBut = bottom(right(new JButton("Mutate")));
	    mutateBut.addActionListener(new ActionListener(){
	    	public void actionPerformed(ActionEvent arg0) {
	    		mutate();	    		
	    	}
	    });

        spinBox.add(Box.createHorizontalGlue());
	    spinBox.add(randomizeBut);
        spinBox.add(Box.createHorizontalStrut(STRUT));
	    spinBox.add(mutateBut);
	    
	    for (int series = 0; series < 3 ; series ++)
	    {		
			for (int variation =0 ; variation < 6 ; variation++ )
			{
				variationStore.add(new Variation(),series*4+variation);
			}
		}
        
        
        f.setVisible(true);
	}

	public Variation[] getVariations() {
		return variations;
	}


	public static void main( String[] args )
    {
//		 the eq
	    final Mutator mutator = new Mutator();
	   
    	
	    
    }

	public void mutate()
	{
		for (int variation =0 ; variation < variations.length ; variation++ )
		{
			variations[variation].mutate(mother, range, probability);
		}
	}
	
	public void randomize()
	{
		for (int variation =0 ; variation < variations.length ; variation++ )
		{
			variations[variation].randomize(mother.getNbValues());
		}
	}
	
	
	public double getProbability() {
		return probability;
	}

	public void setProbability(double probability) {
		this.probability = probability;
	}

	public double getRange() {
		return range;
	}

	public void setRange(double range) {
		this.range = range;
	}

	public Variation getFather() {
		return father;
	}

	public void setFather(Variation father) {
		this.father = father;
	}

	public Variation getMother() {
		return mother;
	}

	public void setMother(Variation mother) {
		this.mother = mother;
	}

	public void setVariations(Variation[] variations) {
		this.variations = variations;
	}

}
