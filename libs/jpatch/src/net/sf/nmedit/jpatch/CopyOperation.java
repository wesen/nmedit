package net.sf.nmedit.jpatch;

import java.util.Collection;

public interface CopyOperation extends MoveOperation {
    void copy();
    
    Collection<? extends PModule> getCopiedModules();
    PModule getCopiedModule(PModule oldModule);
    Collection<? extends PConnection> getCopiedConnections();
	public void setDuplicate(boolean isDuplicate);
	public boolean isDuplicate();
}
