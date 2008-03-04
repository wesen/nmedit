package net.sf.nmedit.jpatch.clavia.nordmodular;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;

import net.sf.nmedit.jpatch.CopyOperation;
import net.sf.nmedit.jpatch.LayoutTool;
import net.sf.nmedit.jpatch.PConnection;
import net.sf.nmedit.jpatch.PConnectionManager;
import net.sf.nmedit.jpatch.PConnector;
import net.sf.nmedit.jpatch.PModule;

public class NMCopyOperation extends NMMoveOperation implements CopyOperation {

	 Hashtable<PModule,PModule> mapNew;
	 ArrayList<PConnection> conNew;
	 
	public NMCopyOperation(VoiceArea va) {
		super(va);
		mapNew = new Hashtable<PModule, PModule>();
		conNew = new ArrayList<PConnection>();
	}

	public void move() {
		copy();
	}
	
	public void copy() {
        checkOffset();
        
        if (isEmpty())
            return;
        
        List<PModule>copiedModules = new ArrayList<PModule>();
        for (PModule m : modules) {
        	if (m != null) {
        		PModule newM = m.cloneModule();
        		copiedModules.add(newM);
        		destination.add(newM);
        		mapNew.put(m, newM);
        	}
        }

        for (PModule m : modules) {
        	if (m != null && m.getParentComponent() != null)
        	{
        		PConnectionManager com = va.getConnectionManager();
        		PConnectionManager com2 = destination.getConnectionManager();
            	Collection<PConnection> connections = com.connections(modules);
            	for (PConnection c : connections) {
            		PModule a = c.getModuleA();
            		PModule b = c.getModuleB();
            		PModule a2 = getCopiedModule(a);
            		PModule b2 = getCopiedModule(b);

            		PConnector ca = c.getA();
        			PConnector cb = c.getB();
            		PConnector ca2 = null, cb2 = null;
            		if (a2 != null && b2 != null) {
            			ca2 = a2.getConnectorByComponentId(ca.getComponentId());
            			cb2 = b2.getConnectorByComponentId(cb.getComponentId());
            			// comment for now, this allows to "duplicate" (like in reaktor) modules
//            		} else if (a2 != null) {
//            			if (!ca.isOutput()) {
//            				ca2 = a2.getConnectorByComponentId(ca.getComponentId());
//            				cb2 = cb;
//            			}
//            		} else if (b2 != null) {
//            			if (!cb.isOutput()) {
//            				ca2 = ca;
//            				cb2 = b2.getConnectorByComponentId(cb.getComponentId());
//            			}
            		}
            		if (ca2 != null && cb2 != null) {
            			com2.add(ca2, cb2);
            			conNew.add(new PConnection(ca2, cb2));
            		}
            	}
        	}
        }
        
        LayoutTool layoutTool = new LayoutTool(destination, copiedModules);
        layoutTool.setDelta(dx, dy);
        Object[] data = layoutTool.move();
        List<PModule> tmpMoved = new ArrayList<PModule>(data.length/3);
        for (int i=0;i<data.length;i+=3)
        {
            PModule m = (PModule) data[i+0];
            int sx = (Integer) data[i+1];
            int sy = (Integer) data[i+2];
            m.setScreenLocation(sx, sy);
            tmpMoved.add(m);
        }
        
        opModules = tmpMoved;
	}

	public Collection<? extends PModule> getCopiedModules() {
		return getMovedModules();
	}

	public PModule getCopiedModule(PModule oldModule) {
		return mapNew.get(oldModule);
	}

	public Collection<? extends PConnection> getCopiedConnections() {
		return conNew;
	}

}
