/* Copyright (C) 2006 Christian Schneider
 * 
 * This file is part of Nomad.
 * 
 * Nomad is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * Nomad is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Nomad; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package net.sf.nmedit.nomad.core.forms;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;

public class ExceptionDialog extends JPanel
{

    /**
     * 
     */
    private static final long serialVersionUID = -1830717129957093522L;
    private static transient int iconType = -1;
    private static transient Icon messageIcon;
    
    private transient Icon icon;
    private int currentIconType;
    private Throwable throwable;
    private boolean tinfoVisible = false;
    private JComponent tinfoComponent;
    private transient String stackTrace;
    private JDialog dialog;
    
    public ExceptionDialog(JDialog dialog, Object message, Throwable throwable)
    {
        this(dialog, JOptionPane.ERROR_MESSAGE, message, throwable);
    }
    
    public ExceptionDialog(JDialog dialog, int messageIcon, Object message, Throwable throwable)
    {
        this.dialog = dialog;
        this.currentIconType = messageIcon;
        this.throwable = throwable;

        JPanel messagePan = new JPanel();
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        messagePan.setLayout(new BoxLayout(messagePan, BoxLayout.Y_AXIS));
        
        Box box = Box.createVerticalBox();
        Box box2 = Box.createHorizontalBox();
        
        JLabel label = new JLabel(String.valueOf(message), getIcon(), JLabel.LEFT);
        label.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        label.setAlignmentY(JComponent.TOP_ALIGNMENT);
        box2 = Box.createHorizontalBox();
        box2.add(label);
        box2.add(Box.createHorizontalGlue());
        box.add(box2);/**/
        messagePan.add(box);
        
        messagePan.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        //messagePan.add(Box.createVerticalStrut(4));
       // messagePan.add(new JSeparator(JSeparator.HORIZONTAL));
        //messagePan.add(Box.createVerticalStrut(4));
        //messagePan.add(Box.createVerticalStrut(4));
       
        setLayout(new BorderLayout());
        add(messagePan, BorderLayout.NORTH);

        box = Box.createHorizontalBox();

        JButton btn = new JButton(new DialogAction(DialogAction.MoreLess));
        btn.setAlignmentX(JComponent.RIGHT_ALIGNMENT);
        btn.setAlignmentY(JComponent.TOP_ALIGNMENT);
        box.add(btn);
        box.add(Box.createHorizontalStrut(4));
        btn = new JButton(new DialogAction(DialogAction.COPY));
        btn.setAlignmentX(JComponent.RIGHT_ALIGNMENT);
        btn.setAlignmentY(JComponent.TOP_ALIGNMENT);
        box.add(btn);
        btn = new JButton(new DialogAction(DialogAction.CLOSE));
        btn.setDefaultCapable(true);
        dialog.getRootPane().setDefaultButton(btn);
        
        btn.setAlignmentX(JComponent.RIGHT_ALIGNMENT);
        btn.setAlignmentY(JComponent.TOP_ALIGNMENT);
        box.add(Box.createGlue());
        box.add(btn);

        add(box, BorderLayout.SOUTH);        
    }
    
    public static void main(String[] args)
    {
        try
        {
            try
            {
                throw new RuntimeException("test");
            }
            catch (Throwable t)
            {
                throw new Exception("Wrapped Exception", t);
            }
        }
        catch (Throwable t)
        {
            ExceptionDialog.showErrorDialog(null, "The Title", "The Message", t);
        }
    }
    
    public static void showErrorDialog(JComponent parent, Object message, String title, Throwable throwable)
    {
        JDialog dialog = new JDialog();
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setTitle(title);
        dialog.setMinimumSize(new Dimension(180, 100));
        ExceptionDialog ed = new ExceptionDialog(dialog, message, throwable);
        dialog.getContentPane().setLayout(new BorderLayout());
        dialog.getContentPane().add(ed, BorderLayout.CENTER);
        dialog.setResizable(false);
        dialog.setModal(true);
        
        ed.checkBounds();
        
        dialog.setVisible(true);
    }
    
    private void checkBounds()
    {
        Dimension s = Toolkit.getDefaultToolkit().getScreenSize();
        dialog.pack();
        Dimension d = dialog.getSize();

        d.width = Math.min(d.width, s.width/2);
        d.height = Math.min(d.height, s.height/3);
        
        dialog.setBounds((s.width-d.width)/2, (s.height-d.height)/2, d.width, d.height);
    }

    public boolean isThrowableInfoVisible()
    {
        return tinfoVisible;
    }
    
    public void setThrowableInfoVisible(boolean visible)
    {
        if (this.tinfoVisible != visible)
        {
            this.tinfoVisible = visible;
            
            if (visible)
            {
                if (tinfoComponent == null)
                    tinfoComponent = createInfoComponent();
                add(tinfoComponent, BorderLayout.CENTER);
                checkBounds();
            }
            else if (tinfoComponent != null)
            {
                remove(tinfoComponent);
                checkBounds();
            }
        }
    }
    
    public void copyToClipBoard()
    {
        StringSelection stringSelection = new StringSelection( getStackTrace() );
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents( stringSelection, null );
    }
    
    private String getStackTrace()
    {
        if (stackTrace == null)
        {
            StringWriter sw = new StringWriter();
            throwable.printStackTrace(new PrintWriter(sw));
            stackTrace = sw.getBuffer().toString();
        }
        return stackTrace;
    }
    
    private JComponent createInfoComponent()
    {
        JTextArea text = new JTextArea();
        text.setFont(new Font("monospaced", Font.PLAIN, 11));
        text.setTabSize(4);
        text.setEditable(false);

        text.setText(getStackTrace());
        JScrollPane sp = new JScrollPane(text); 
        return sp;
    }
    
    public void close()
    {
        dialog.dispose();
    }
    
    private class DialogAction extends AbstractAction
    {

        /**
         * 
         */
        private static final long serialVersionUID = -192407757621759091L;
        public static final String CLOSE = "Close";
        public static final String COPY = "Copy to Clipboard";
        public static final String MoreLess = "MoreLess";

        public DialogAction (String command)
        {
            putValue(ACTION_COMMAND_KEY, command);
            if (command == CLOSE)
            {
                putValue(NAME, "Close");
                putValue(MNEMONIC_KEY, KeyEvent.VK_C);
            }
            else if (command == COPY)
            {
                putValue(NAME, "Copy to Clipboard");
            }
            else if (command == MoreLess)
            {
                putValue(NAME, "More");
            }
        }
        
        public void actionPerformed(ActionEvent e)
        {
            if (e.getActionCommand() == CLOSE)
                close();
            else if (e.getActionCommand() == COPY)
                copyToClipBoard();
            else if (e.getActionCommand() == MoreLess)
            {
                setThrowableInfoVisible(!isThrowableInfoVisible());
                if (isThrowableInfoVisible())
                    putValue(NAME, "Less");
                else
                    putValue(NAME, "More");
            }
        }
        
    }
    
    public Throwable getThrowable()
    {
        return throwable;
    }

    protected Icon getIcon()
    {
        if (icon == null)
            icon = getIconForType(currentIconType);
        return icon;
    }

    protected Icon getIconForType(int messageType) 
    {
        if (iconType == messageType && messageIcon != null)
            return messageIcon;
        
        if(messageType < 0 || messageType > 3)
            return null;
        
        String propertyName = null;
        switch(messageType) {
        case 0: propertyName = "OptionPane.errorIcon"; break;
        case 1: propertyName = "OptionPane.informationIcon"; break;
        case 2: propertyName = "OptionPane.warningIcon"; break;
        case 3: propertyName = "OptionPane.questionIcon"; break;
        }
        if (propertyName != null) 
        {
            Icon i = UIManager.getIcon(propertyName);
            if (i != null)
            {
                iconType = messageType;
                messageIcon = i;
                return i;
            }
        }
        return null;
    }

}
