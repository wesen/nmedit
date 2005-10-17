package nomad.com;

import java.util.Vector;

/**
 * List of AbstractSynthListeners
 * @author Christian Schneider
 * @stereotype container
 * @has 1 has * nomad.com.AbstractSynthListener
 */
public class SynthListenerSubscriberList {
	
	/**
	 * List of AbstractSynthListeners
	 */
	private Vector subscribers = new Vector();

	/**
	 * Creates a new empty list of AbstractSynthListeners.
	 */
	public SynthListenerSubscriberList() {
		super();
	}
	
	/**
	 * Add a new listener to the list. If an listener is already
	 * in the list, it will not be added a second time.
	 * @param listener the new listener
	 */
	public void subscribeListener(AbstractSynthListener listener) {
		if (!subscribers.contains(listener))
			subscribers.add(listener);
	}
	
	/**
	 * If listener is contained in the list, it will be removed from
	 * the list
	 * @param listener the listener to be removed
	 */
	public void unsubscribeListener(AbstractSynthListener listener) {
		if (subscribers.contains(listener))
			subscribers.remove(listener);
	}

	/**
	 * Returns the number of subscribers.
	 * @return the number of subscribers.
	 */
	public int getSubscriberCount() {
		return subscribers.size(); 
	}
	
	/**
	 * Returns the subscriber at the specified index.
	 * @param index index of the subscriber to be returned
	 * @return the subscriber at the specified index
	 */
	public AbstractSynthListener getSubscriber(int index) {
		return (AbstractSynthListener) subscribers.get(index);
	}
	
}
