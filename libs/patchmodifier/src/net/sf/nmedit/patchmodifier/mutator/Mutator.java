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
package net.sf.nmedit.patchmodifier.mutator;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;

import java.awt.event.ActionEvent;
import java.util.HashMap;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.sf.nmedit.jpatch.PPatch;

public class Mutator implements VariationSelectionListener{

    private static final boolean DEBUG = false;
    
    
    public static final String ACTION_RANDOMIZE = "mutator.randomize";
    public static final String ACTION_MUTATE = "mutator.mutate";

    
	private double probability = .65, range = .15;
	
	private Variation variations[] = new Variation[4];
	// TODO: rename and remove static size
	private Variation variationStorage[] = new Variation[18]; 
	private Variation mother = new Variation(), father = new Variation();
	
	private HashMap<PPatch, MutatorState> mutatorStates = new HashMap<PPatch, MutatorState>();

    private MutatorState state = null;
    
	public Color colorValue(int hue)
	{	
		return Color.getHSBColor(hue/127f, 1f, 0.8f);
	}
	
	public Mutator()
	{
		for (int i = 0 ; i < variations.length; i ++)
		{
			variations[i]= new Variation();
			variations[i].addVariationSelectionListener(this);
		}
		
		for (int i = 0 ; i < variationStorage.length; i ++)
		{
			variationStorage[i]= new Variation();
			variationStorage[i].addVariationSelectionListener(this);
		}
		
		mother.addVariationSelectionListener(this);
		father.addVariationSelectionListener(this);
		
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
	
	
	private JFrame frame;
	
	private void createUI() {
		frame = new JFrame("Patch Mutator");
		JFrame f = frame;
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
	    		if (DEBUG) System.out.println(range + " = range");
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
	    		if (DEBUG) System.out.println(probability+" = prob");
	    	}
	    });	    

	    probBox.add(Box.createVerticalGlue());
        probBox.add(left(new JLabel("Prob.")));
	    probBox.add(left(probSpinner));
        probBox.setMaximumSize(new Dimension(100, Short.MAX_VALUE));
        rangeBox.setMaximumSize(new Dimension(100, Short.MAX_VALUE));
	    
	    Box spinBox = (bottom(Box.createHorizontalBox()));
	    
	    spinBox.add(left(bottom(probBox)));
	    spinBox.add(Box.createHorizontalStrut(STRUT));
	    spinBox.add(left(bottom(rangeBox)));
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
	    
	    
	    // actions
	    MutatorAction actionRandomize = new MutatorAction(ACTION_RANDOMIZE);
	    MutatorAction actionMutate = new MutatorAction(ACTION_MUTATE);
	    JButton randomizeBut = bottom(right(new JButton(actionRandomize)));
	    JButton mutateBut = bottom(right(new JButton(actionMutate)));

        spinBox.add(Box.createHorizontalGlue());
	    spinBox.add(randomizeBut);
        spinBox.add(Box.createHorizontalStrut(STRUT));
	    spinBox.add(mutateBut);
	    
	    for (int series = 0; series < 3 ; series ++)
	    {		
			for (int variation =0 ; variation < 6 ; variation++ )
			{
			//	Variation v = new Variation();
				variationStore.add(variationStorage[series*6+variation],series*4+variation);
			//	variationStorage[series*6+variation] = v; 
			}
		}
	}
	
	private class MutatorAction extends AbstractAction 
	{
        /**
         * 
         */
        private static final long serialVersionUID = -8901333546202462292L;
        private boolean lengthyOperation = true;
        // in case there are calls even if the last one is not completed
        private boolean ignoreActionEvent = false;
        
	    public MutatorAction(String command)
	    {
	        setEnabled(false);
	        putValue(ACTION_COMMAND_KEY, command);
	        
	        // TODO add icons
	        if (ACTION_RANDOMIZE.equals(command))
	        {
                setEnabled(true);
	            putValue(NAME, "Randomize");
	        }
	        else if (ACTION_MUTATE.equals(command))
	        {
	            setEnabled(true);
	            putValue(NAME, "Mutate");
	        }
	    }

        public void actionPerformed(final ActionEvent e)
        {
            if (!isEnabled()) return;
            if (ignoreActionEvent) return; 
            if (lengthyOperation)
            {
                ignoreActionEvent = true;
                SwingUtilities.invokeLater(new Runnable(){
                    public void run()
                    {
                        performAction(e.getActionCommand());
                    }
                });
            }
            else
            {
                performAction(e.getActionCommand());
            }
        }
        
        public void performAction(String command)
        {
            // we do the try finally stuff so that
            // the ignoreActionEvent is guaranteed to be
            // set back to false
            try 
            {
                // exceptions are ok to occure here
                performActionUnsave(command);
            }
            finally
            {
                ignoreActionEvent = false;
            }
        }

        private void performActionUnsave(String command)
        {
            if (ACTION_RANDOMIZE.equals(command))
            {
                randomize();
            }
            else if (ACTION_MUTATE.equals(command))
            {
                mutate();
            }
        }

	}

	public void addPatch(PPatch p) {
		mutatorStates.put(p, new MutatorState(p));
	}
	
	public void removePatch(PPatch p) {
		mutatorStates.remove(p);
	}
	
	public void selectPatch(PPatch p) {
		if (p!= null)
			setState(mutatorStates.get(p));
		else setState(null);
	}
	
	private void setState(MutatorState s) {
		if (s !=null) {
			state = s;

			mother.setState(s.getMother());
			father.setState(s.getFather());
			for (int i = 0 ; i < variationStorage.length; i ++) {
				variationStorage[i].setState(s.getVariations().get(i));				
			}
			for (int i = 0 ; i < variations.length; i ++) {
				variations[i].setState(s.getWorkingVariations().get(i));				
			}			

			state.setSelectedVariation(mother.getState());
		} 
		else {
			state = null;
			mother.setState(null);
			father.setState(null);
			for (int i = 0 ; i < variationStorage.length; i ++) {
				variationStorage[i].setState(null);				
			}
			for (int i = 0 ; i < variations.length; i ++) {
				variations[i].setState(null);				
			}
		}
		
	}
	
	public Variation[] getVariations() {
		return variations;
	}


	public static void main( String[] args )
    {
//		 the eq
	    final Mutator mutator = new Mutator();
        mutator.getFrame().setVisible(true);
    }

	public void mutate()
	{
		if (state != null)
			for (int variation =0 ; variation < variations.length ; variation++ )
			{			
				variations[variation].getState().mutate(mother.getState(), range, probability);
			}
	}
	
	public void randomize()
	{
		if (state != null)
			for (int variation =0 ; variation < variations.length ; variation++ )
			{
				variations[variation].getState().randomize();
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


    public JFrame getFrame()
    {
    	return frame;
    }

    public void variationSelectionChanged(Variation v) {
    	if (state != null) {
    		state.setSelectedVariation(v.getState());	
    		for(int i = 0 ; i < state.getParameters().size(); i++) {    		
    			
    			v.getState().getParameters().get(i).setValue(v.getState().getValues().get(i));
    		}
    	}
	}

}
