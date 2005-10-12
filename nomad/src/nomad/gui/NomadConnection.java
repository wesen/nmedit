package nomad.gui;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class NomadConnection extends JLabel {
	private ImageIcon icon;

	public NomadConnection(String text) {
		icon = new ImageIcon("./grafix/_con_in_red.gif");
		
		setSize(icon.getIconWidth(), icon.getIconHeight());
		setIcon(icon);
		
		setToolTipText(text);
	}
}
