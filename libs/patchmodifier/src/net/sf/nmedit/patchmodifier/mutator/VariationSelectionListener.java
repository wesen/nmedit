package net.sf.nmedit.patchmodifier.mutator;

import java.util.EventListener;

/**
 * 
 * @author Julien Pauty
 *
 */

/**
 * Listens for selection change in the mutator 
 * 
 * @param v the selected variation
 * 
 */
 
public interface VariationSelectionListener extends EventListener{
	void variationSelectionChanged(Variation v);
}
