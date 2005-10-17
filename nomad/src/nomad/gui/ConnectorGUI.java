package nomad.gui;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class ConnectorGUI extends JLabel {
	private ImageIcon icon;

	public ConnectorGUI(String text) {
		icon = new ImageIcon("./grafix/_con_in_red.gif");
		
		setSize(icon.getIconWidth(), icon.getIconHeight());
		setIcon(icon);
		
		setToolTipText(text);
	}
}
