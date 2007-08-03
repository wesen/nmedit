package net.sf.nmedit.jpatch.randomizer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
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
    
	private void createUI() {
		JFrame f = new JFrame("Mutator test");
	    f.setBounds(30,30,300,300);
	    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    f.getContentPane().setLayout(new BoxLayout(f.getContentPane(),BoxLayout.PAGE_AXIS));
	    
	    JPanel variationStore = new JPanel(new GridLayout(3,6));
	  
	    
	    final JSpinner rangeSpinner = new JSpinner( new SpinnerNumberModel(10, 0, 50, 1));

	    Box rangeBox = new Box(BoxLayout.Y_AXIS);
	    
	    rangeSpinner.setValue((int)(range*50));
	    rangeSpinner.addChangeListener(new ChangeListener(){
	    	public void stateChanged(ChangeEvent e) {
	    		range = (Integer)(rangeSpinner.getValue())/50d;	
	    		System.out.println(range + " = range");
	    	}
	    });	    

	    rangeBox.add(rangeSpinner);
	    rangeBox.add(new JLabel("Range"));
	    
	    Box probBox = new Box(BoxLayout.Y_AXIS);
	    
	    final JSpinner probSpinner = new JSpinner(new SpinnerNumberModel(10, 0, 100, 1));
	    probSpinner.setValue((int)(probability*100));
	    probSpinner.addChangeListener(new ChangeListener(){
	    	public void stateChanged(ChangeEvent e) {
	    		probability = (Integer)(probSpinner.getValue())/100d;	
	    		System.out.println(probability+" = prob");
	    	}
	    });	    

	    probBox.add(probSpinner);
	    probBox.add(new JLabel("Prob."));
	    
	    Box spinBox = new Box(BoxLayout.X_AXIS);
	    
	    spinBox.add(probBox);
	    spinBox.add(rangeBox);
	    
	    Box varBox = new Box(BoxLayout.X_AXIS);
	    varBox.add(mother);
	    for (int i  = 0; i < variations.length; i++)
	    	varBox.add(variations[i]);
	    varBox.add(father);
	   
	    Box.Filler filler = new Box.Filler(new Dimension(10,10),new Dimension(50,50),new Dimension(10,10));
	    
	    f.getContentPane().add(filler);
	    f.getContentPane().add(spinBox);
	    f.getContentPane().add(filler);
	    f.getContentPane().add(varBox);
	    f.getContentPane().add(filler);
	    f.getContentPane().add(variationStore);
	    
	    
	    
	    JButton randomizeBut = new JButton("Randomize");
	    randomizeBut.addActionListener(new ActionListener(){
	    	public void actionPerformed(ActionEvent arg0) {
	    		randomize();	    		
	    	}
	    });
	    
	    JButton mutateBut = new JButton("Mutate");
	    mutateBut.addActionListener(new ActionListener(){
	    	public void actionPerformed(ActionEvent arg0) {
	    		mutate();	    		
	    	}
	    });
	    
	    spinBox.add(randomizeBut);
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
			variations[variation].repaint();
		}
	}
	
	public void randomize()
	{
		for (int variation =0 ; variation < variations.length ; variation++ )
		{
			variations[variation].randomize(mother.getNbValues());
			variations[variation].repaint();
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
