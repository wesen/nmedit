package org.nomad.util.misc;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

/**
 * ActionQueueFeedback is a visual component that processes a list of
 * tasks and gives the user feedback about the progress using
 * a progress bar to show the overall state and a label containing
 * information about the current task.
 * 
 * @author Christian Schneider
 */
public class ActionQueueFeedback extends JPanel {

	// the label component
	private JLabel lblAction = new JLabel();

	// the progress bar component
	private JProgressBar bar = new JProgressBar();

	// a list containing objects of type Runnable
	private ArrayList runnables = new ArrayList();

	// indicates if the processing has been started
	private boolean isRunning = false;

	/**
	 * Creates a new instance.
	 * @param title A string containing a summary of the tasks.
	 */
	public ActionQueueFeedback(String title) {
		setSize(300, 80);
		setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		setLayout(new FlowLayout(FlowLayout.CENTER));

		bar.setMinimum(0);
		bar.setValue(0);
		bar.setStringPainted(true);
		lblAction.setText(title);
		
		JPanel container = new JPanel();
		container.setLayout(new BorderLayout());
		container.add(lblAction, BorderLayout.NORTH);
		container.add(bar, BorderLayout.CENTER);

		add(container);
	}
	
	/**
	 * Appends the Runnable to the processing queue.
	 * If processing has started an IllegalStateException will be thrown.
	 * 
	 * @param run A task that will be appended to the processing queue
	 * @throws IllegalStateException processing has already started
	 */
	public void enque(Runnable run) {
		if (!isRunning) {
			runnables.add(run);
			bar.setMaximum(runnables.size());
		} else
			throw new IllegalStateException("Processing has already started.");
	}
	
	/**
	 * Starts the processing of the tasks. The tasks are processed in
	 * its own thread using the same order they are appended to the queue.
	 * 
	 * Before processing a task it will be removed from the queue.
	 * If all tasks are processed, the queue will be empty.
	 * 
	 * @throws IllegalStateException processing has already started
	 */
	public void run() {
		if (!isRunning) {
			isRunning = true;
	
			//setModal(true);
			setVisible(true);
			
			(new Thread(new Runnable() {
				public void run() {
					while (runnables.size()>0) {
						Runnable current = (Runnable) runnables.remove(0);
						lblAction.setText(current.toString());
						current.run();
						bar.setValue(bar.getValue()+1);
					}
					
					isRunning = false;
				}
			})).start();
		} else
			throw new IllegalStateException("Processing has already started.");
	}

}
