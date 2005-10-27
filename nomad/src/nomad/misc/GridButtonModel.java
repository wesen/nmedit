package nomad.misc;
import javax.swing.Icon;

public interface GridButtonModel {

	/**
	 * Must return the number of buttons in a row
	 * @return the number of buttons in a row
	 */
	public int getButtonRowSize();
	/**
	 * Must return the number of buttons in a column
	 * @return the number of buttons in a column
	 */
	public int getButtonColSize();
	
	/**
	 * Returns the label text for the button
	 * @param row the row
	 * @param col the column
	 * @return the label
	 */
	public String getButtonLabel(int row, int col);
	
	/**
	 * Returns the icon for the button
	 * @param row the row
	 * @param col the column
	 * @return the icon
	 */
	public Icon getButtonIcon(int row, int col);
	
	/**
	 * Notification if a button was clicked
	 * @param row the row
	 * @param col the column
	 */
	public void buttonClicked(int row, int col);
	
	/**
	 * Returns true if the button should send continuously action events while pressed.
	 * @param row the row
	 * @param col the column
	 * @return true if the button should send continuously action events while pressed.
	 */
	public boolean getButtonHasAdvancedMouseHandling(int row, int col);
	
}
