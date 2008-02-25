package net.sf.nmedit.jpatch.randomizer;

import net.sf.nmedit.jpatch.PModule;
import net.sf.nmedit.jpatch.PModuleContainer;
import net.sf.nmedit.jpatch.PParameter;
import net.sf.nmedit.jpatch.PPatch;

public abstract class AbstractParameterIterator
{
    
    protected abstract void iterate(PParameter parameter);

    public abstract boolean includes(PParameter parameter);

    public abstract boolean includes(PModule module);

    public abstract boolean includes(PModuleContainer moduleContainer);
        
    public void iterate(PPatch patch)
    {
        for (int i=0;i<patch.getModuleContainerCount();i++)
        {
            PModuleContainer moduleContainer = patch.getModuleContainer(i);
            if (includes(moduleContainer))
              iterate(moduleContainer);
        }
    }

    public void iterate(PModuleContainer moduleContainer)
    {
        for (PModule module: moduleContainer)
        {
            if (includes(module))
              iterate(module);
        }
    }

    private void iterate(PModule module)
    {
        for (int i=0;i<module.getParameterCount();i++)
        {
            PParameter parameter = module.getParameter(i);
            if (includes(parameter))
            {
                iterate(parameter);
            }
        }
    }

}
