package nomad.application.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import nomad.com.ComPort;
import nomad.com.MidiDriver;
import nomad.com.MidiDriverList;

/**
 * This dialog lets the user setup the ComPort midi driver.
 * 
 * @author Christian Schneider
 */
public class ComPortSettingsDialog extends JDialog {

	private ComPort comPort=null;
	private JComboBox cbDrivers = new JComboBox();
	private JComboBox cbPortsIn   = new JComboBox();
	private JComboBox cbPortsOut   = new JComboBox();
	private JPanel titlePanel    = new JPanel();
	private JPanel userPanel    = new JPanel(); 
	private JPanel buttonPanel  = new JPanel();

	private JButton btnConfirm = new JButton("Accept");
	private JButton btnCancel  = new JButton("Cancel");

	/**
	 * Creates a dialog to choose the midi driver of the comPort object.
	 * @param comPort The ComPort instance that is set up.
	 */
	public ComPortSettingsDialog(ComPort comPort) {
		this.setName("ComPort settings");
		this.setTitle("ComPort settings");
		
		if (comPort == null)
			throw new NullPointerException("'comPort' must not be null");

		this.comPort = comPort;
		
		titlePanel.setBackground(Color.WHITE);
		titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.X_AXIS));
		titlePanel.add(new JLabel(this.getName()));
		titlePanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

		btnCancel.setDefaultCapable(true);
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
		buttonPanel.add(btnConfirm);
		buttonPanel.add(btnCancel);
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

		GridBagLayout upLayout = new GridBagLayout();
		GridBagConstraints upConstraints = new GridBagConstraints();
		userPanel.setLayout(upLayout);

		upConstraints.insets = new Insets(10,10,10,10);
		upConstraints.gridwidth = GridBagConstraints.HORIZONTAL;
		upConstraints .gridwidth=2;
		upConstraints.gridheight=3;
		upConstraints.fill = GridBagConstraints.BOTH;
		Component c;

		upConstraints.weightx=1.0;
		userPanel.add(c=new JLabel("Driver"));
		upLayout.setConstraints(c, upConstraints);

		upConstraints.weightx=4.0;
		upConstraints.gridwidth = GridBagConstraints.REMAINDER;		
		userPanel.add(c=cbDrivers);
		upLayout.setConstraints(c, upConstraints);

		upConstraints.weightx=1.0;
		upConstraints.fill = GridBagConstraints.BOTH;
		upConstraints.gridwidth = GridBagConstraints.HORIZONTAL;
		userPanel.add(c=new JLabel("Input Port"));
		upLayout.setConstraints(c, upConstraints);

		upConstraints.weightx=4.0;
		upConstraints.gridwidth = GridBagConstraints.REMAINDER;		
		userPanel.add(c=cbPortsIn);
		upLayout.setConstraints(c, upConstraints);
		userPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));

		upConstraints.weightx=1.0;
		upConstraints.fill = GridBagConstraints.BOTH;
		upConstraints.gridwidth = GridBagConstraints.HORIZONTAL;
		userPanel.add(c=new JLabel("Output Port"));
		upLayout.setConstraints(c, upConstraints);

		upConstraints.weightx=4.0;
		userPanel.add(c=cbPortsOut);
		upLayout.setConstraints(c, upConstraints);
		userPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		
		this.getContentPane().add(BorderLayout.NORTH, titlePanel);
		this.getContentPane().add(BorderLayout.CENTER, userPanel);
		this.getContentPane().add(BorderLayout.SOUTH, buttonPanel);
		
		this.setModal(true);
		
		//btnConfirm.setEnabled(false);

		MidiDriverList driverList = comPort.getDrivers();
		for (int i=0;i<driverList.getDriverCount();i++)
			cbDrivers.addItem(driverList.getDriver(i));

		cbDrivers.setSelectedIndex(0);
		cbDrivers.setEnabled(cbDrivers.getItemCount()>1);
		
		updatePortList();
		
		cbDrivers.addActionListener(new DriverListActionAdapter());
		//cbPortsIn.addActionListener(new PortListActionAdapter());
		//cbPortsOut.addActionListener(new PortListActionAdapter());
		btnConfirm.addActionListener(new ConfirmButtonActionAdapter());
		btnCancel.addActionListener(new CancelButtonActionAdapter());

		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setSize(300, 240);
		this.validate();
	}
	
	private void updatePortList() {
		cbPortsIn.removeAllItems();
		MidiDriver driver = (MidiDriver) cbDrivers.getSelectedItem();
		for (int i=0;i<driver.getPortCountIn();i++)
			cbPortsIn.addItem(driver.getPortIn(i));
		cbPortsIn.setEnabled(cbPortsIn.getItemCount()>1);

		cbPortsOut.removeAllItems();
		for (int i=0;i<driver.getPortCountOut();i++)
			cbPortsOut.addItem(driver.getPortOut(i));
		cbPortsOut.setEnabled(cbPortsOut.getItemCount()>1);
	}
	
	class DriverListActionAdapter implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			updatePortList();
			//btnConfirm.setEnabled(true);
		}
	}
	
	/*
	class PortListActionAdapter implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			btnConfirm.setEnabled(cbPortsIn.getItemCount()>1);
		}
	}*/
	
	class ConfirmButtonActionAdapter implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			storeSettings();
			setVisible(false);
		}
	}
	
	class CancelButtonActionAdapter implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			setVisible(false);
		}
	}

	private void storeSettings() {
		MidiDriver driver = (MidiDriver) cbDrivers.getSelectedItem();
		driver.setDefaultPortIn(cbPortsIn.getSelectedIndex());
		driver.setDefaultPortOut(cbPortsOut.getSelectedIndex());
		comPort.setDriver(driver);
	}

	/**
	 * Creates a new ComPortSettingsDialog instance for setting up
	 * the midi driver of the ComPort object.
	 * @param comPort the ComPort to set up
	 */
	public static void invokeDialog(ComPort comPort) {
		ComPortSettingsDialog window = new ComPortSettingsDialog(comPort);
		
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
