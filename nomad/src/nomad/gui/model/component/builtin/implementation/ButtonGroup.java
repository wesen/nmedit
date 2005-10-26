package nomad.gui.model.component.builtin.implementation;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import nomad.model.descriptive.DParameter;

public class ButtonGroup extends JPanel {
	private ArrayList buttons = new ArrayList();
	private boolean horizontal = true;
	private javax.swing.ButtonGroup btngroup = new 
		javax.swing.ButtonGroup();
	private final static int PADDING = 2;
	private ButtonActionListener btnAction = new
		ButtonActionListener();
	private ArrayList changeListeners = new ArrayList();
	private int value = 0;
	private boolean inverseOrder = false;

	private int btn_width = 28;
	private int btn_height = 14;
	private DParameter paramInfo = null;
	
	public ButtonGroup() {
		this.setLayout(null);
		setButtonCount(1);
		this.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
	}
	
	public void setButtonCount(int count) {
		if (count<=0) // at least one button
			return ;
		
		if (count<buttons.size()) {
			while(count<buttons.size()) {
				AbstractButton btn = (AbstractButton) buttons.remove(buttons.size()-1);
				if (btn!=null) {
					btn.removeActionListener(btnAction);
					btngroup.remove(btn);
				}
				this.remove(btn);
			}
			if (getButtonCount()==1) // make a standalone button
				btngroup.remove((AbstractButton)buttons.get(0));
		}
		else
		if (count>buttons.size()) {
			if (buttons.size()==1)
				btngroup.add((SimpleButton)buttons.get(0));
			
			while(count>buttons.size()) {
				int index = buttons.size();
				SimpleButton btn = new SimpleButton(index);
				btn.setSize(btn_width, btn_height);
				btn.addActionListener(btnAction);
				buttons.add(btn);
				if (count>1) // make a group button
					btngroup.add(btn);
				this.add(btn);
			}

		}
		
		if (value>=buttons.size()) {
			value=buttons.size()-1;
			notifyChangeListeners();
		}
		if (value>=0 && buttons.size()>1) {
			SimpleButton btn = (SimpleButton)buttons.get(value);
			if (!btn.isSelected())
				btn.setSelected(true);
		}
		updateComponents();
	}
	
	public int getButtonCount() {
		return buttons.size();
	}

	public boolean hasInverseOrder() {
		return inverseOrder;
	}
	
	public boolean hasHorizontalOrientation() {
		return horizontal;
	}
	
	public void setHorizontalOrientation(boolean horizontal) {
		if (this.horizontal==horizontal)
			return;
		
		this.horizontal = horizontal;
		updateComponents();
	}
	
	public void setInverseOrder(boolean inverseOrder) {
		if (this.inverseOrder==inverseOrder)
			return;
		
		this.inverseOrder = inverseOrder;
		updateComponents();
	}
	
	public int getValue() {
		return value;
	}
	
	public void setValue(int value) {
		if (this.value==value || value<0 || value>=buttons.size())
			return ;

		if (buttons.size()==1)
			((SimpleButton)buttons.get(0)).setSelected(value==1);
		
		else  // buttons.size() >1
			((SimpleButton)buttons.get(value)).setSelected(true);
		
		updateValue(value);
	}
	
	private void updateValue(int value) {
		this.value = value;
		if (buttons.size()==1 && paramInfo!=null) {
			((SimpleButton)buttons.get(0)).setToolTipText(
					paramInfo.getFormattedValue(value)
			);
		}
		
		notifyChangeListeners();
	}
	
	public void addChangeListener(ChangeListener listener) {
		if (changeListeners.indexOf(listener)<0)
			changeListeners.add(listener);
	}
	
	public void removeChangeListener(ChangeListener listener) {
		if (changeListeners.indexOf(listener)>=0)
			changeListeners.remove(listener);
	}
	
	private void notifyChangeListeners() {
		ChangeEvent event = new ChangeEvent(this);
		for (int i=0;i<changeListeners.size();i++)
			((ChangeListener)changeListeners.get(i)).stateChanged(event);
	}
	
	private class ButtonActionListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			SimpleButton btn = (SimpleButton) event.getSource();
			if (btn!=null) {
				switch (getButtonCount()) {
					case 0:	{updateValue(0); break;}
					case 1:	{updateValue(btn.isSelected()?1:0);break;}
					default:{updateValue(btn.getIndex()); break;}
				}					
			}
		}
	}
	
	private void updateComponents() {
		if (horizontal) {
			for (int i=0;i<buttons.size();i++) {
				SimpleButton btn = (SimpleButton) buttons.get(inverseOrder?buttons.size()-i-1:i);
				btn.setSize(btn_width, btn_height);
				btn.setPreferredSize(btn.getSize());
				btn.setLocation(PADDING+i*(SimpleButton.BUTTON_SPACING+btn_width), PADDING);
			}
			this.setSize(
				2*PADDING+buttons.size()*(SimpleButton.BUTTON_SPACING+btn_width),
				(2*PADDING+1)+ btn_height
			);
		} else { // vertical
			for (int i=0;i<buttons.size();i++) {
				SimpleButton btn = (SimpleButton) buttons.get(inverseOrder?buttons.size()-i-1:i);
				btn.setSize(btn_width, btn_height);
				btn.setPreferredSize(btn.getSize());
				btn.setLocation(PADDING, PADDING+i*(SimpleButton.BUTTON_SPACING+btn_height));
			}
			this.setSize(
				(2*PADDING+1)+btn_width,
				2*PADDING+buttons.size()*(SimpleButton.BUTTON_SPACING+btn_height)
			);
		}
		this.setPreferredSize(this.getSize());
		updateTextProperties(false);
	}
	
	public Dimension getButtonSize() {
		return new Dimension(btn_width, btn_height);
	}
	
	public void setButtonSize(Dimension d) {
		if (btn_width!=d.width || btn_height!=d.height) {
			btn_width = d.width;
			btn_height = d.height;
			updateComponents();
		}
	}
	
	public void setParamInfo(DParameter paramInfo) {
		if (this.paramInfo!=paramInfo) {
			this.paramInfo = paramInfo;
			updateTextProperties(true);
		}
	}
	
	private void updateTextProperties(boolean newParam) {
		if (paramInfo!=null) {
			for (int i=0;i<buttons.size();i++) {
				SimpleButton btn = (SimpleButton) buttons.get(inverseOrder?buttons.size()-i-1:i);
				btn.setToolTipText(paramInfo.getFormattedValue(i));
				if (newParam)
					btn.setText((buttons.size()==1) ? paramInfo.getName() : paramInfo.getFormattedValue(i));
			}
		} else {
			for (int i=0;i<buttons.size();i++) {
				SimpleButton btn = (SimpleButton) buttons.get(inverseOrder?buttons.size()-i-1:i);
				btn.setToolTipText(""+i);
				btn.setText(""+i);
			}
		}
	}
	
	public void setButtonDisplay(int button, String text) {
		SimpleButton btn = (SimpleButton) buttons.get(inverseOrder?buttons.size()-button-1:button);
		btn.setIcon(null);
		btn.setText(text);
	}
	
	public String getButtonText(int button) {
		SimpleButton btn = (SimpleButton) buttons.get(inverseOrder?buttons.size()-button-1:button);
		return btn.getText();
	}
	
	public void setButtonDisplay(int button, Icon icon) {
		SimpleButton btn = (SimpleButton) buttons.get(inverseOrder?buttons.size()-button-1:button);
		btn.setText(null);
		btn.setIcon(icon);
	}
	
	public void setButtonDisplay(int button, Image image) {
		setButtonDisplay(button, new ImageIcon(image));
	}
}

class SimpleButton extends JToggleButton {
	public final static int BUTTON_SPACING = 1;
	private int index = 1;
	private final static Font defaultFont = new Font("Dialog", Font.PLAIN, 9);
	
	public SimpleButton(int index) {
		super(Integer.toString(index));
		this.index = index;
		this.setFont(defaultFont);
		this.setMargin(new Insets(0,0,0,0));
	}
	
	public int getIndex() {
		return index;
	}
}
