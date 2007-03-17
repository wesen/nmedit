package net.sf.nmedit.jsynth.clavia.nordmodular.worker;

import net.sf.nmedit.jnmprotocol.AckMessage;
import net.sf.nmedit.jnmprotocol.NmProtocolListener;
import net.sf.nmedit.jsynth.SynthException;
import net.sf.nmedit.jsynth.clavia.nordmodular.NordModular;
import net.sf.nmedit.jsynth.clavia.nordmodular.utils.NmUtils;
import net.sf.nmedit.jsynth.worker.RequestPatchWorker;

public class ReqPatchWorker extends
    NmProtocolListener implements RequestPatchWorker, ScheduledWorker
{

    private NordModular synth;
    private int slotId;
    private int patchId = 0;
    private boolean fakeWorker;
    private boolean called = false;  
    private boolean error = false;
    
    private int state = START;
    private static final int START = 0;
    private static final int WAIT_FOR_ACK = 1;
    private static final int ACK_RECEIVED = 2;
    private static final int DONE = 3;
    

    public ReqPatchWorker(NordModular synth, int slotId, boolean fakeWorker)
    {
        this.synth = synth;
        this.slotId = slotId;
        this.fakeWorker = fakeWorker;
    }

    public void requestPatch() throws SynthException
    {
        if (!synth.isConnected())
            throw new SynthException("not connected");
        
        if (called)
            throw new SynthException("worker already used");
        
        called = true;
        
        if (fakeWorker)
            return;
        
        synth.getScheduler().offer(this);
    }

    public void aborted()
    {
        error = true;
        synth.removeProtocolListener(this);
    }

    public boolean isWorkerFinished()
    {
        return error || (!synth.isConnected()) || state>=DONE;
    }
    
    private long ackTimeout = 0;

    public void runWorker() throws SynthException
    {      
        if (state==START)
        {
            ackTimeout = System.currentTimeMillis()+2000;
            state = WAIT_FOR_ACK;
            synth.addProtocolListener(this);
            try
            {
                synth.getProtocol().send(NmUtils.createRequestPatchMessage(slotId));
            }
            catch (Exception e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return;
        }
        if (state == WAIT_FOR_ACK)
        {
            if (ackTimeout<System.currentTimeMillis())
            {
                throw new SynthException("timeout: ack message");
            }
            
            return;
        }
        if (state == ACK_RECEIVED)
        {
            synth.removeProtocolListener(this);
            state = DONE;
            
            if (patchId == 0)
            {
                // no patch id
                return;
            }
            
            GetPatchWorker worker = new GetPatchWorker(synth, slotId, patchId);
            synth.getScheduler().offer(worker);
        }
    }

    public void messageReceived(AckMessage message)
    {
        if (slotId == message.get("slot"))
        {
            patchId = message.get("pid1");
            state = ACK_RECEIVED;
        }
    }
    
}
