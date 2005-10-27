package nomad.misc;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.Timer;

public class GridButtonComponent extends JComponent {

	private GridButtonModel model;
	
	public GridButtonComponent() {}

	public void setGridButtonModel(GridButtonModel model) {
		if (this.model != null)
			return;
		this.model = model;
		this.model = model;

		int rowcnt = model.getButtonRowSize();
		int colcnt = model.getButtonColSize();
			
		this.setLayout(new GridLayout(
				rowcnt, //rows,
				colcnt  //columns
			));

		GridButtonActionListener gbal = new GridButtonActionListener();
		
		Font btnFont = new Font("Dialog", Font.PLAIN, 9);

		for (int row=0;row<model.getButtonRowSize();row++) {
			for (int col=0;col<model.getButtonColSize();col++) {
				GridButton btn = new GridButton(row, col, model.getButtonHasAdvancedMouseHandling(row, col));
				btn.setText(model.getButtonLabel(row, col));
				btn.setIcon(model.getButtonIcon(row, col));
				btn.addActionListener(gbal);
				btn.setFont(btnFont);
				//btn.setPreferredSize(btn.getSize());
				//btn.setBorder(null);
				btn.setBorder(BorderFactory.createEtchedBorder());
				add(btn);
			}
		}
	}
	
	private class GridButtonActionListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			GridButton btn = (GridButton) event.getSource();
			model.buttonClicked(btn.row, btn.col);
		}
	}
	
	private class GridButton extends JButton {
		int row = 0;
		int col = 0;

		public GridButton(int row, int col, boolean hasAdvancedMouseActionHandling) {
			this.row = row;
			this.col = col;
			if (hasAdvancedMouseActionHandling)
				addMouseListener(new GBMouseListener());
		}
		
		private class GBMouseListener extends MouseAdapter {
			private Timer timer = null;
			private final int delay = 150;
			
			public void mousePressed(MouseEvent event) {
				if (timer==null) {
					/*
					GridButton.this.actionListener
						.actionPerformed( new ActionEvent(GridButton.this, ActionEvent.ACTION_PERFORMED, "text") );
					*/
					
					timer = new Timer(delay, GridButton.this.actionListener);
					timer.setInitialDelay(600);
					timer.start();
				}
			}

			public void mouseReleased(MouseEvent event) {
				if (timer !=null && timer.isRunning()) {
					timer.stop();
					timer = null;
				}
				event.consume();
			}
		}
		
	}
	
}
