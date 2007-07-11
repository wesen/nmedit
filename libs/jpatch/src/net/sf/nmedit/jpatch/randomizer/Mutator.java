package net.sf.nmedit.jpatch.randomizer;

import java.awt.Color;
import java.awt.GridLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JSlider;

import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class Mutator {
	
	private double probability = .65, range = .15;
	
	private Variation variations[][] = new Variation[4][4];
	
	public Color colorValue(int hue)
	{	
		return Color.getHSBColor(hue/127f, 1f, 0.8f);
	}
	
	public Mutator()
	{
		variations = new Variation[4][4];
		for (int series = 0; series < 4 ; series ++)
		{
			variations[series][0] = new Variation(64);
			for (int variation =1 ; variation < 4 ; variation++ )
			{
				variations[series][variation] = new Variation();
			}
		}
	}
    
	public Variation[][] getVariations() {
		return variations;
	}

	public static void main( String[] args )
    {
//		 the eq
	    final Mutator mutator = new Mutator();
	   
    	
	    JFrame f = new JFrame("Mutator test");
	    f.setBounds(30,30,300,300);
	    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    f.getContentPane().setLayout(new BoxLayout(f.getContentPane(),BoxLayout.PAGE_AXIS));
	    
	    JPanel variationStore = new JPanel(new GridLayout(4,4));
	    f.getContentPane().add(variationStore,0);
	    
	    final JSlider rangeSlider = new JSlider(0,50);
	    rangeSlider.setValue((int)(mutator.getRange()*50));
	    rangeSlider.addChangeListener(new ChangeListener(){
	    	public void stateChanged(ChangeEvent e) {
	    		mutator.setRange(rangeSlider.getValue()/50d);	
	    		System.out.println(mutator.getRange() + " = range");
	    	}
	    });	    

	    f.getContentPane().add(rangeSlider);
	    
	    final JSlider probSlider = new JSlider(0,100);
	    probSlider.setValue((int)(mutator.getProbability()*100));
	    probSlider.addChangeListener(new ChangeListener(){
	    	public void stateChanged(ChangeEvent e) {
	    		mutator.setProbability(probSlider.getValue()/100d);	
	    		System.out.println(mutator.getProbability()+" = prob");
	    	}
	    });	    

	    f.getContentPane().add(probSlider);
	    
	    
	    
	    final JButton randomizeBut = new JButton("Randomize");
	    randomizeBut.addActionListener(new ActionListener(){
	    	public void actionPerformed(ActionEvent arg0) {
	    		mutator.randomize();	    		
	    	}
	    });
	    
	    final JButton mutateBut = new JButton("Mutate");
	    mutateBut.addActionListener(new ActionListener(){
	    	public void actionPerformed(ActionEvent arg0) {
	    		mutator.mutate();	    		
	    	}
	    });
	    
	    Box buttonBox = new Box(BoxLayout.X_AXIS);
	    buttonBox.add(randomizeBut);
	    buttonBox.add(mutateBut);
	    f.getContentPane().add(buttonBox);
	    
	    for (int series = 0; series < 4 ; series ++)
	    {		
			for (int variation =0 ; variation < 4 ; variation++ )
			{
				variationStore.add(mutator.getVariations()[series][variation],series*4+variation);
			}
		}
        
        
        f.setVisible(true);
    }

	public void mutate(){
		for (int series = 0; series < 4 ; series ++)
	    {		
			for (int variation =1 ; variation < 4 ; variation++ )
			{
				variations[series][variation].mutate(variations[series][0], range, probability);
				variations[series][variation].repaint();
			}
			
		} 
	}
	
	public void randomize(){
		for (int series = 0; series < 4 ; series ++)
	    {		
			for (int variation =1 ; variation < 4 ; variation++ )
			{
				variations[series][variation].randomize();
				variations[series][variation].repaint();
			}
			
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

}
