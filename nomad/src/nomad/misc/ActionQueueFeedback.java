package nomad.misc;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class ActionQueueFeedback extends JPanel {

	private JProgressBar bar = new JProgressBar();
	private ArrayList runnables = new ArrayList();
	private boolean isRunning = false;
	private JLabel lblAction = new JLabel();
	
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
	
	public void enque(Runnable run) {
		if (!isRunning)
			runnables.add(run);
		bar.setMaximum(runnables.size());
	}
	
	public void run() {
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
				
				// finishedProcessing();
			}
		})).start();
	}

}
