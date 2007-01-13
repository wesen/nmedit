/*
    Nord Modular Midi Protocol 3.03 Library
    Copyright (C) 2003-2006 Marcus Andersson

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/

/*
 * Created on Jan 13, 2007
 */
package net.sf.nmedit.jnmprotocol.utils;

import java.util.ArrayList;
import java.util.List;

import net.sf.nmedit.jnmprotocol.PatchListMessage;

/**
 * A helper class to extract the patch names from 
 * {@link net.sf.nmedit.jnmprotocol.PatchListMessage}.
 */
public class PatchList
{

    // minimum value of section
    public static final int MIN_SECTION = 0;
    // maximum value of section
    public static final int MAX_SECTION = 9-1;
    // minimum position
    public static final int MIN_POSITION = 0;
    // maximum position
    public static final int MAX_POSITION = 99-1;
    
    // contains the (valid) patch names and their location
    private List<PatchEntry> patchEntryList;
    // next location for GetPatchListMessage if valid
    private int nextSection;
    // next location for GetPatchListMessage if valid
    private int nextPosition;

    private PatchList(List<PatchEntry> patchEntryList, int endSection, int endPosition)
    {
        this.patchEntryList = patchEntryList;
        this.nextSection = endSection;
        this.nextPosition = endPosition;
        
        if (nextPosition>MAX_POSITION)
        {
            nextPosition = 0;
            nextSection++;
        }
        if (nextSection>MAX_SECTION)
        {
            nextPosition = -1;
            nextSection = -1;
        }
    }
    
    /**
     * Returns the number of patch names.
     */
    public int size()
    {
        return patchEntryList.size();
    }
    
    /**
     * Returns the patch name at the specified index.
     */
    public String getName(int index)
    {
        return patchEntryList.get(index).name;
    }
    
    /**
     * Returns the section of the patch at the specified index.
     */
    public int getSection(int index)
    {
        return patchEntryList.get(index).section;
    }
    
    /**
     * Returns the position of the patch at the specified index.
     */
    public int getPosition(int index)
    {
        return patchEntryList.get(index).position;
    }

    /**
     * Returns true if more patch names are available via
     * GetPatchListMessage.
     */
    public boolean hasNextLocation()
    {
        return MIN_SECTION<=nextSection && nextSection<=MAX_SECTION
            && MIN_POSITION<=nextPosition && nextPosition<=MAX_POSITION;
    }
    
    /**
     * Returns the section for the next GetPatchListMessage
     * if hasNextLocation() is true. Otherwise the return value
     * is undefined. 
     */
    public int getNextSection()
    {
        return nextSection;
    }
    
    /**
     * Returns the position for the next GetPatchListMessage
     * if hasNextLocation() is true. Otherwise the return value
     * is undefined.  
     */
    public int getNextPosition()
    {
        return nextPosition;
    }
    
    /**
     * Returns the nord modular display number of the patch at the specified index.
     */
    public int getDisplayPosition(int index)
    {
        return (getSection(index)+1)*100 + getPosition(index)+1;
    }
    
    /**
     * Returns true if between the patch at the specified index and the patch
     * at index-1 is at least one unused location.
     */
    public boolean hasGap(int index)
    {
        return index+1<size() && (getPosition(index)<getPosition(index+1)
                || getSection(index)<getSection(index+1));
    }
    
    /**
     * Returns true if the patch at the specified index and the next patch are in
     * different sections or the patch is the last in this list.
     */
    public boolean isLastInSection(int index)
    {
        return index+1==size() || (index+1<size() && getSection(index)<getSection(index+1));
    }
    
    /**
     * Returns an array containing all patch names.
     */
    public String[] getNames()
    {
        String[] names = new String[patchEntryList.size()];
        for (int i=patchEntryList.size()-1;i>=0;i--)
            names[i] = getName(i);
        return names;
    }

    /**
     * Creates a new patch list from the specified PatchListMessage.
     */
    public static PatchList createPatchList(PatchListMessage message)
    {
        List nameList = message.getNames();
        List<PatchEntry> entries = new ArrayList<PatchEntry>(nameList.size());
        // create entries
        int section = message.get("section");
        int position = message.get("position");
        
        for (Object n : nameList)
        {
            String name = (String) n;
            
            if (PatchListMessage.isEmptyPosition(name))
            {
                position++;
            }
            else if (PatchListMessage.hasGapBefore(name))
            {
                position = PatchListMessage.getSupplementaryValue(name);
                entries.add(new PatchEntry(PatchListMessage.removeGapInfoFromName(name), section, position));
                position++;
            }
            else if (PatchListMessage.isEndOfSection(name))
            {
                position = 0;
                section = PatchListMessage.getSupplementaryValue(name);
            }
            else
            {
                entries.add(new PatchEntry(name, section, position));
                position++;
            }
        }
        
        if (entries.isEmpty())
            section = position = -1;
        
        // create list
        return new PatchList(entries, section, position);
    }

    /**
     * The patch entry class.
     */
    private static class PatchEntry
    {
        String name;
        int section;
        int position;

        public PatchEntry(String name, int section, int position)
        {
            this.name = name;
            this.section = section;
            this.position = position;
        }
    }

}
