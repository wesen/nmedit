package net.sf.nmedit.jpdl2.test;

import net.sf.nmedit.jpdl2.PDLItem;
import net.sf.nmedit.jpdl2.PDLItemType;
import net.sf.nmedit.jpdl2.impl.PDLConditionImpl;
import net.sf.nmedit.jpdl2.impl.PDLConditionalImpl;
import net.sf.nmedit.jpdl2.impl.PDLConstantImpl;
import net.sf.nmedit.jpdl2.impl.PDLImplicitVariableImpl;
import net.sf.nmedit.jpdl2.impl.PDLLabelImpl;
import net.sf.nmedit.jpdl2.impl.PDLMessageIdImpl;
import net.sf.nmedit.jpdl2.impl.PDLMultiplicityImpl;
import net.sf.nmedit.jpdl2.impl.PDLOptionalImpl;
import net.sf.nmedit.jpdl2.impl.PDLPacketRefImpl;
import net.sf.nmedit.jpdl2.impl.PDLPacketRefListImpl;
import net.sf.nmedit.jpdl2.impl.PDLVariableImpl;
import net.sf.nmedit.jpdl2.impl.PDLVariableListImpl;
import net.sf.nmedit.jpdl2.PDLException;

import org.junit.Test;

public class PDLItemTests
{

    public PDLItem getInstance(PDLItemType type)
    {
        switch (type)
        {
            case Label:
                return new PDLLabelImpl("label");
            case Constant:
                return new PDLConstantImpl(0,0);
            case Variable:
                return new PDLVariableImpl("name",1);
            case VariableList:
                return new PDLVariableListImpl("name",1, new PDLMultiplicityImpl(2));
            case Optional:
                return new PDLOptionalImpl();
            case PacketRef:
                return new PDLPacketRefImpl(null, "name", "binding");
            case PacketRefList:
                return new PDLPacketRefListImpl(null, "name", "binding", new PDLMultiplicityImpl(2));
            case Conditional:
                return new PDLConditionalImpl(new PDLConditionImpl("name",0,true));
            case ImplicitVariable:
                return new PDLImplicitVariableImpl("name",1);
            case MessageId:
                return new PDLMessageIdImpl("messageid");
            default:
                throw new InternalError("unknown type: "+type);
        }
    }
    
    @Test
    public void createExceptionTest()
    {
        for (PDLItemType type: PDLItemType.values())
        {
            PDLItem instance = getInstance(type);
            new PDLException(instance, "some message");
        }
    }
    
    @Test
    public void oneInstanceForEachItemType() throws Exception
    {
        for (PDLItemType type: PDLItemType.values())
        {
            PDLItem instance = getInstance(type);
            if (instance == null)
                throw new Exception("no instance for "+type);
        }
    }
    
}
