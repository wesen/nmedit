package nomad.application.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import nomad.plugin.NomadPlugin;
import nomad.plugin.PluginManager;

/**
 * This dialog lets the user setup the ComPort midi driver.
 * 
 * @author Christian Schneider
 */
public class PluginDialog extends JDialog {

	private JPanel titlePanel    = new JPanel();
	private JPanel userPanel    = new JPanel(); 
	private JPanel buttonPanel  = new JPanel();
	private JTable table = null;
	private JScrollPane scrollpane = null;

	private JButton btnClose = new JButton("Close");

	/**
	 * Creates te plugins dialog
	 */
	public PluginDialog() {
		this.setName("Plugins");
		this.setTitle("Plugins");

		titlePanel.setBackground(Color.WHITE);
		titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.X_AXIS));
		titlePanel.add(new JLabel(this.getName()));
		titlePanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

		btnClose.setDefaultCapable(true);
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
		buttonPanel.add(btnClose);
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

		this.setLayout(new BorderLayout());
		userPanel.setLayout(new BorderLayout());
		this.getContentPane().add(BorderLayout.NORTH, titlePanel);
		this.getContentPane().add(BorderLayout.CENTER, userPanel);
		this.getContentPane().add(BorderLayout.SOUTH, buttonPanel);
		
		this.setModal(true);

		btnClose.addActionListener(new CloseButtonActionAdapter());

		table = new JTable(new PluginTableModel());
	    table.getColumnModel().getColumn(0).setHeaderValue("Plugin");
	    table.getColumnModel().getColumn(1).setHeaderValue("Property");
	    table.getColumnModel().getColumn(2).setHeaderValue("Value");
	    scrollpane = new JScrollPane(table);
	    scrollpane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	    scrollpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
	    userPanel.add(scrollpane, BorderLayout.CENTER);
		
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setSize(400, 240);
		this.validate();
	}
	
	class PluginTableModel extends AbstractTableModel {

		Vector items = new Vector();
		
		class TableItem {
			public final static int TYPE_PLUGINNAME=0;
			public final static int TYPE_DESCRIPTION=1;
			public final static int TYPE_AUTHOR=2;
			public int type = 0;
			public Object value = null;
			public TableItem(int type, Object value) {
				this.type = type;
				this.value = value;
			}
		}
		
		public PluginTableModel() {
			for (int i=0;i<PluginManager.getPluginCount();i++) {
				NomadPlugin plugin = PluginManager.getPlugin(i);
				items.add(new TableItem(TableItem.TYPE_PLUGINNAME, plugin.getName()));
				items.add(new TableItem(TableItem.TYPE_DESCRIPTION, plugin.getDescription()));
				String[] authors = plugin.getAuthors();
				for (int j=0;j<authors.length;j++)
					items.add(new TableItem(TableItem.TYPE_AUTHOR, authors[j]));
			}
		}
		
		public int getRowCount() {
			return items.size();
		}

		public int getColumnCount() {
			return 3;
		}

		public Object getValueAt(int row, int col) {
			TableItem item = (TableItem) items.get(row);
			switch (item.type) {
				case TableItem.TYPE_PLUGINNAME:
					return (col==0)?item.value : "";
				case TableItem.TYPE_DESCRIPTION: 
					if (col==1)
						return "Description";
					else
						return (col==2)?item.value : "";
				case TableItem.TYPE_AUTHOR: 
					if (col==1)
						return "Author";
					else
						return (col==2)?item.value : "";
				default: return "";
			}
		}
		
	}

	class CloseButtonActionAdapter implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			setVisible(false);
		}
	}

	/**
	 * Creates a new plugins dialog instance.
	 */
	public static void invokeDialog() {
		PluginDialog window = new PluginDialog();
		
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int minh = Math.min(screen.height, window.getHeight());
		int minw = Math.min(screen.width, window.getWidth());
		window.setLocation(
				(screen.width-minw)/2,
				(screen.height-minh)/2
		);
		
		window.setVisible(true);
	}
	
}
