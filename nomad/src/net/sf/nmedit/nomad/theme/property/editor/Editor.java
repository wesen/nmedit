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

/*
 * Created on Mar 2, 2006
 */
package net.sf.nmedit.nomad.theme.property.editor;

import java.util.LinkedList;

import javax.swing.JComponent;

import net.sf.nmedit.nomad.theme.component.NomadComponent;
import net.sf.nmedit.nomad.theme.property.Property;
import net.sf.nmedit.nomad.theme.property.Value;
import net.sf.nmedit.nomad.theme.property.editor.EditorEvent.EventId;


/**
 * An editor for a component's property.
 * 
 * @author Christian Schneider
 */
public abstract class Editor
{

    private NomadComponent             component;

    private boolean                    dialogModeEnabled;

    private LinkedList<EditorListener> editorListenerList;

    private Property                   property;

    private JComponent                 editorComponenent = null;

    /**
     * Creates a new editor for given component and property. The editor
     * component should be displayed in it's own window if dialog mode is
     * enabled
     * 
     * @param property
     *            the editing property
     * @param component
     *            the editing component
     * @param dialogModeEnabled
     *            true if the editor component should be displayed in it's own
     *            window
     */
    public Editor( Property property, NomadComponent component,
            boolean dialogModeEnabled )
    {
        this.property = property;
        this.editorListenerList = new LinkedList<EditorListener>();
        this.component = component;
        this.dialogModeEnabled = dialogModeEnabled;
    }

    /**
     * Returns the editing property.
     * 
     * @return the editing property
     */
    public Property getProperty()
    {
        return property;
    }

    /**
     * Returns the editing component.
     * 
     * @return the editing component
     */
    public NomadComponent getComponent()
    {
        return component;
    }

    /**
     * Set's the editor component.
     * 
     * @param editor
     *            the editor component
     */
    protected void setEditorComponent( JComponent editor )
    {
        this.editorComponenent = editor;
    }

    /**
     * Returns the editor component.
     * 
     * @return the editor component
     */
    public JComponent getEditorComponent()
    {
        return editorComponenent;
    }

    /**
     * Cancels editing.
     */
    public void cancelEditing()
    {
        fireEditorEvent( EventId.EDITING_CANCELED );
    }

    /**
     * Returns the selected value.
     * 
     * @return the selected value
     */
    public abstract Value getValue();

    /**
     * Returns true if the editor component should be displayed in it's own
     * window.
     * 
     * @return true if the editor component should be displayed in it's own
     *         window
     */
    public boolean isDialogModeEnabled()
    {
        return dialogModeEnabled;
    }

    /**
     * Sets the dialog mode
     * 
     * @param dialogModeEnabled
     * @see #isDialogModeEnabled()
     */
    protected void setDialogModeEnabled( boolean dialogModeEnabled )
    {
        this.dialogModeEnabled = dialogModeEnabled;
    }

    /**
     * Adds an editor listener
     * 
     * @param l
     *            the editor listener
     */
    public void addEditorListener( EditorListener l )
    {
        if (!editorListenerList.contains( l )) editorListenerList.add( l );
    }

    /**
     * Removes an editor listener
     * 
     * @param l
     *            the editor listener
     */
    public void removeEditorListener( EditorListener l )
    {
        editorListenerList.remove( l );
    }

    /**
     * Notifies all editor listeners.
     * 
     * @param eventId
     */
    public void fireEditorEvent( EditorEvent.EventId eventId )
    {
        EditorEvent event = new EditorEvent( this, eventId );
        
        for (int i=editorListenerList.size()-1;i>=0;i--)
            editorListenerList.get(i).editorChanged( event );
    }

}
