/**
 * 
 */
package net.sf.nmedit.jpatch.randomizer;

import net.sf.nmedit.jpatch.PModule;
import net.sf.nmedit.jpatch.PModuleContainer;
import net.sf.nmedit.jpatch.PParameter;
import net.sf.nmedit.jpatch.PRoles;
import net.sf.nmedit.jpatch.impl.PBasicRoles;

abstract class DefaultParameterIterator extends AbstractParameterIterator
{
    
    private static final boolean DEBUG = false;

    private static final PRoles RejectedModules = PBasicRoles.setOf("morph");
    private static final PRoles RejectedParameters = PBasicRoles.setOf(
            "level", 
            "mute", // level 
            "morph", // morph
            "ui" // modifies user interface but not the sound
    );
    
    @Override
    public boolean includes(PParameter c)
    {
        PRoles roles = c.getRoles();
        if (RejectedParameters.intersects(roles))
        {
            if (DEBUG)
            {
                System.out.println("rejected: "+c.getName()+" (parameter), "+roles);
            }
            return false;
        }
        return true;
    }

    @Override
    public boolean includes(PModule c)
    {
        PRoles roles = c.getRoles();
        if (RejectedModules.intersects(roles))
        {
            if (DEBUG)
            {
                System.out.println("rejected: "+c.getName()+" (module), "+roles);
            }
            return false;
        }
        return true;
    }

    @Override
    public boolean includes(PModuleContainer moduleContainer)
    {
        return true;
    }
    
}