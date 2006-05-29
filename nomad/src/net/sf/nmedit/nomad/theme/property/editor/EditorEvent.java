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
 * Created on Mar 3, 2006
 */
package net.sf.nmedit.nomad.theme.property.editor;

/**
 * Editor event
 * 
 * @author Christian Schneider
 */
public class EditorEvent
{

    public enum EventId
    {
        EDITING_STOPPED, EDITING_CANCELED
    }

    private Editor        editor;

    private final EventId eventId;

    /**
     * Creates a new editor event.
     * 
     * @param editor  the changed editor
     * @param eventId the event id 
     */
    public EditorEvent( Editor editor, EventId eventId )
    {
        this.editor = editor;
        this.eventId = eventId;
    }

    /**
     * Returns the editor that fired this event.
     * @return the editor that fired this event 
     */
    public Editor getEditor()
    {
        return editor;
    }

    /**
     * Returns the id of this event.
     * @return the id of this event
     */
    public EventId getEventId()
    {
        return eventId;
    }

}
