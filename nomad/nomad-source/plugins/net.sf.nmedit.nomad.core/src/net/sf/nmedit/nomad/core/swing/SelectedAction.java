/**
 * 
 */
package net.sf.nmedit.nomad.core.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.ButtonGroup;

public class SelectedAction extends AbstractAction implements ItemListener
{
    /**
     * 
     */
    private static final long serialVersionUID = -2469667964205220429L;
    private ButtonGroup bg;
    private Action currentAction;

    public SelectedAction()
    {
        this.bg = new ButtonGroup();
        setEnabled(false);
    }
    
    public void add(AbstractButton ab)
    {
        ab.addItemListener(this);
        bg.add(ab);
        setEnabled(true);
        if (bg.getSelection() == null)
            ab.setSelected(true);
    }
    
    public void actionPerformed(ActionEvent e)
    {
        if (currentAction != null)
            currentAction.actionPerformed(e);
    }
    
    protected void setCurrentAction(Action o)
    {
        if (currentAction != o)
        {
            currentAction = o;
            
            Object actionCommand = null;
            if (currentAction != null)
            {
                actionCommand = currentAction.getValue(ACTION_COMMAND_KEY);
            }
            putValue(ACTION_COMMAND_KEY, actionCommand);
        }
        setEnabled(currentAction != null);
    }
    
    protected Action getCurrentAction()
    {
        return currentAction;
    }

    public void itemStateChanged(ItemEvent e)
    {
        Object o = e.getSource();
        if (o instanceof AbstractButton)
            setCurrentAction(((AbstractButton)o).getAction());
        else
            setCurrentAction(null);
    }
    
}