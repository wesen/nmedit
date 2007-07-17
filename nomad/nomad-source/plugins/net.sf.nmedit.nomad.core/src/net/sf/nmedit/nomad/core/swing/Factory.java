package net.sf.nmedit.nomad.core.swing;

import java.awt.Insets;

import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.JButton;

public class Factory
{
    /** Defines the margin used in toolbar buttons. */
    private static final Insets TOOLBAR_BUTTON_MARGIN = new Insets(1, 1, 1, 1);

    public static void setupToolBarButton(AbstractButton button) 
    {
        button.setFocusPainted(false);
        button.setMargin(TOOLBAR_BUTTON_MARGIN);
    }

    public static JButton createToolBarButton(Action action) 
    {
        JButton button = new JButton(action);
        setupToolBarButton(button);
        return button;
    }
    
}
