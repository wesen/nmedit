/*
    Protocol Definition Language
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
package net.sf.nmedit.jpdl2.format;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sf.nmedit.jpdl2.PDLException;
import net.sf.nmedit.jpdl2.dom.PDLBlock;
import net.sf.nmedit.jpdl2.dom.PDLCaseStatement;
import net.sf.nmedit.jpdl2.dom.PDLChoice;
import net.sf.nmedit.jpdl2.dom.PDLCondition;
import net.sf.nmedit.jpdl2.dom.PDLDocument;
import net.sf.nmedit.jpdl2.dom.PDLFunction;
import net.sf.nmedit.jpdl2.dom.PDLItem;
import net.sf.nmedit.jpdl2.dom.PDLItemType;
import net.sf.nmedit.jpdl2.dom.PDLMultiplicity;
import net.sf.nmedit.jpdl2.dom.PDLPacketDecl;
import net.sf.nmedit.jpdl2.dom.PDLPacketRef;
import net.sf.nmedit.jpdl2.impl.PDLCompiledCondition;
import net.sf.nmedit.jpdl2.impl.PDLPacketDeclImpl;
import net.sf.nmedit.jpdl2.utils.PDLUtils;

/**
 * ensures
 * - start packet exists (if the start statement is present)
 * detects:
 * - mutual packet references
 * - unreachable code (after fail statements)
 * - variable(-list) name, label name, packet(-list)-binding collisions
 * - mutual exclusive statement: unreachable optional items (only a few cases are tested yet)
 * - invalid variable / label references
 * TODO:
 * - test that each path is tagged by a messageId (optional)
 */
public class PDLDocumentVerifier
{
    
    private PDLDocument doc;

    public PDLDocumentVerifier(PDLDocument doc)
    {
        this.doc = doc;
    }

    public void verify() throws PDLException
    {
        verifyStartDecl();
        verifyPacketDecl();
        // verifyPaths(); // test if each possible path contains a message() statement
    }
    
    private void verifyStartDecl() throws PDLException
    {
        // verify start packet
        if (doc.getStartPacketName()!=null && doc.getPacketDecl(doc.getStartPacketName())==null)
        {
            throw new PDLException("start packet not declared: "+doc.getStartPacketName());
        }
    }

    private void verifyPacketDecl() throws PDLException
    {
        Set<String> visitedPackets = new HashSet<String>();
        for (PDLPacketDecl decl: doc)
        {
            // verify local declaration of packet
            verifyPacketDecl(decl);
            // verify packet references and detect unconditional mutual recursion
            visitedPackets.clear();
            verifyMutualRecursion(visitedPackets, decl, false);
        }
    }

    private void verifyPacketDecl(PDLPacketDecl decl) throws PDLException
    {
        try
        {
            verifyBlock(decl, Collections.<String>emptySet(), decl, false);
        }
        catch (PDLException e)
        {
            throw new PDLException(e, decl);
        }
    }

    private boolean verifyBlock(PDLPacketDecl root, Set<String> declared, PDLBlock block, boolean conditionalPath) throws PDLException
    {
        // after 'break' or 'fail' statement, no further statements are allowed (in unconditional paths)
        boolean allowMoreStatements = true;
        
        // declarations in current block
        declared = new HashSet<String>(declared);
        
        for (int index = 0;index<block.getItemCount();index++)
        {
            PDLItem item = block.getItem(index);
            
            if ((!allowMoreStatements) )
            {
                error(item, "unreachable code");
            }
            try
            {
                switch (item.getType())
                {
                    // store reference names
                    case Label:
                        declare(declared, item, "@", item.asInstruction().getString());
                        break;
                    case Variable:
                        declare(declared, item, null, item.asVariable().getName());
                        break;
                    // items referencing other items
                    case Constant:
                        verifyMultiplicativeReference(root, declared, item, item.asConstant().getMultiplicity());
                        break;
                    case ImplicitVariable:
                    case AnonymousVariable:
                        declare(declared, item, null, item.asVariable().getName());
                        verifyFunction(root, declared, item, item.asVariable().getFunction());
                        break;
                    case VariableList:
                        // use unique prefix "%" for variables which can not be referenced
                        declare(declared, item, "%", item.asVariable().getName());
                        verifyMultiplicativeReference(root, declared, item, item.asVariable().getMultiplicity());
                        break;
                    case PacketRef:
                    {
                        PDLPacketRef ref = item.asPacketRef();
                        declare(declared, item, "$", ref.getBinding());
                        verifyPacketReference(item, ref);
                        break;
                    }
                    case InlinePacketRef:
                    {
                        PDLPacketRef ref = item.asPacketRef();
                        declare(declared, item, "$", "$");
                        verifyPacketReference(item, ref);
                        allowMoreStatements =
                            verifyBlock(root, declared, ref.getReferencedPacket(), conditionalPath);
                        break;
                    }
                    case PacketRefList:
                    {
                        PDLPacketRef ref = item.asPacketRef();
                        declare(declared, item, "$", ref.getBinding());
                        verifyPacketReference(item, ref);
                        verifyMultiplicativeReference(root, declared, item, ref.getMultiplicity());
                        break;
                    }
                    case Conditional:
                    {
                        PDLCondition condition = item.asConditional().getCondition();
                        verifyCondition(root, declared, item, condition);
                        verifyBlock(root, declared, item.asConditional(), true);
                        break;
                    }
                    case Optional:
                        verifyBlock(root, declared, item.asOptional(), true);
                        break;
                    case Choice:
                        for (PDLBlock nested: item.asChoice().getItems())
                            verifyBlock(root, declared, nested, true);
                        break;
                    case Block:
                        allowMoreStatements =
                            verifyBlock(root, declared, item.asBlock(), conditionalPath);
                        break;
                    case SwitchStatement:
                        for (PDLCaseStatement nested: item.asSwitchStatement().getItems())
                            verifyBlock(root, declared, nested.getBlock(), true);
                        break;
                    // no references
                    case MessageId: break;
                    case StringDef: break;
                    case Fail:
                        allowMoreStatements = false;
                        break;
                    default:
                        PDLUtils.unknownItemTypeError(item);
                        break;
                }
            }
            catch (PDLException e)
            {
                throw new PDLException(e, item);
            }
        }
        
        return allowMoreStatements;
    }

    private void declare(Set<String> declared, PDLItem item, Object prefix, String name) throws PDLException
    {
        String fullName = prefix == null ? name : (prefix+name);
        
        if (declared.contains(name)
                ||declared.contains("@"+name)
                ||declared.contains("$"+name)
                ||declared.contains("%"+name))
            error(item, "name already in use: "+fullName);
        declared.add(fullName);
    }

    private void verifyMutualRecursion(Set<String> visitedPackets,
            PDLPacketDecl packet, boolean conditionalPath) throws PDLException
    {
        if (conditionalPath) return;
        if (visitedPackets.contains(packet.getName()))
            error(packet, "mutual recursion detected in packet "+packet.getName());
        visitedPackets.add(packet.getName());
        verifyMutualRecursionInBlock(visitedPackets, packet, conditionalPath);
        visitedPackets.remove(packet.getName());
    }

    private void verifyMutualRecursionInBlock(Set<String> visitedPackets,
            PDLBlock block, boolean conditionalPath) throws PDLException
    {

        for (int index = 0;index<block.getItemCount();index++)
        {
            PDLItem item = block.getItem(index);
                switch (item.getType())
                {
                    // no children
                    case Label: break;
                    case Variable: break;
                    case Constant: break;
                    case ImplicitVariable: break;
                    case AnonymousVariable: break;
                    case VariableList: break;
                    case MessageId: break;
                    case StringDef: break;
                    case Fail: break;
                    // packets
                    case InlinePacketRef:
                        verifyMutualRecursion(visitedPackets, 
                                item.asPacketRef().getReferencedPacket(), conditionalPath);
                        break;
                    case PacketRef:
                        verifyMutualRecursion(visitedPackets, 
                                item.asPacketRef().getReferencedPacket(), conditionalPath);
                        break;
                    case PacketRefList:
                        verifyMutualRecursion(visitedPackets, 
                                item.asPacketRef().getReferencedPacket(), true);
                        break;
                    // blocks
                    case Conditional:
                        verifyMutualRecursionInBlock(visitedPackets, 
                                item.asConditional(), true);
                        break;
                    case Optional:
                        verifyMutualRecursionInBlock(visitedPackets, 
                                item.asOptional(), true);
                        break;
                    case Choice:
                    {
                        PDLChoice me = item.asChoice();
                        for (PDLBlock nested: me.getItems())
                            verifyMutualRecursionInBlock(visitedPackets, nested, true);
                        verifyNoUnrechableCode(me);
                        break;
                    }
                    case Block:
                        verifyMutualRecursionInBlock(visitedPackets, 
                                item.asBlock(), conditionalPath);
                        break;
                    case SwitchStatement:
                        for (PDLCaseStatement nested: item.asSwitchStatement().getItems())
                            verifyMutualRecursionInBlock(visitedPackets, 
                                    nested.getBlock(), true);
                        break;
                    // no references
                    default:
                        PDLUtils.unknownItemTypeError(item);
                        break;
                }
        }
    }
    
    private void verifyNoUnrechableCode(PDLChoice m) throws PDLException
    {
        List<PDLBlock> list = m.getItems();

        if (list.size()<2)
            error(m, "choice statement has less than two elements");
        
        for (int i=0;i<list.size()-1;i++)
        {
            PDLItem a = meGetItem(list.get(i));
            PDLItem sa = getSimpleDataTypeFor(a);

            int sza = getSimpleDataItemSize(sa);
            for (int j=i+1;j<list.size();j++)
            {
                PDLItem b = meGetItem(list.get(j));
                PDLItem sb = getSimpleDataTypeFor(b);
                
                // now compare pair (a,b)
                // a parsed/tested before b

                boolean moreTests = true;

                int szb = getSimpleDataItemSize(sb);
                if (sza<=szb && sza>0 && szb>0)
                {   
                    // but if a is constant and b is not a constant, then we accept 
                    if (sa.getType() == PDLItemType.Constant
                            && sb.getType() != PDLItemType.Constant)
                    {
                        // ok
                        moreTests = false;
                    }
                    else
                    {
                        throw new PDLException(b, "item never reached");
                    }
                }
                else
                {
                    moreTests = sa==null || sb==null;
                }

                if (moreTests)
                {
                    
                    // now test for variable list/constant list
                    // TODO

                    // now test minimum size property
                    if (a.getMinimumSize()<b.getMinimumSize())
                        error(b, "elements must be ordered by getMinimumSize()");
                    if (a.getMinimumCount()<b.getMinimumCount())
                        error(b, "elements must be ordered by getMinimumCount()");
                }
            }
        }
    }

    private PDLItem meGetItem(PDLBlock item)
    {
        if (item.getType() == PDLItemType.Block && item.getItemCount()==1)
        {
            return item.getItem(0);
        }
        return item;
    }

    private PDLItem getSimpleDataTypeFor(PDLItem item)
    {
        switch (item.getType())
        {
            case Constant:
                return item;
            case Variable:
                return item;
            case ImplicitVariable:
                return item;
        }
        
        if (item instanceof PDLBlock)
        {
            PDLBlock block = (PDLBlock) item;
            if (block.getItemCount() == 1)
                return getSimpleDataTypeFor(block.getItem(0));
        }
        return null;
    }

    private int getSimpleDataItemSize(PDLItem item)
    {
        if (item == null) return -1;
        switch (item.getType())
        {
            case Constant:
                if(item.asConstant().getMultiplicity() != null)
                    return -1; // constant list
                return item.asConstant().getSize();
            case Variable:
                return item.asVariable().getSize();
            case ImplicitVariable:
                return item.asVariable().getSize();
            default:
                return -1;
        }
    }

    private void verifyCondition(PDLPacketDecl root, Set<String> declared, PDLItem item, PDLCondition condition) throws PDLException
    {
        if (condition instanceof PDLCompiledCondition)
        {
            ensureReferencedExist(root, declared, item, ((PDLCompiledCondition) condition).getDependencies());
        }
    }

    private void verifyFunction(PDLPacketDecl root, Set<String> declared, PDLItem item, PDLFunction function) throws PDLException
    {
        ensureReferencedExist(root, declared, item, function.getDependencies());
    }

    private void ensureReferencedExist(PDLPacketDecl root, Set<String> declared, PDLItem item,
            Collection<String> dependencies) throws PDLException
    {
        for (String name: dependencies)
            ensureReferencedExists(root, declared, item, name);
    }

    private void verifyPacketReference(PDLItem item, PDLPacketRef packetRef) throws PDLException
    {
        if (doc.getPacketDecl(packetRef.getPacketName())==null)
            error(item, "packet referenced but not declared:"+packetRef.getPacketName());
        
        // TODO ensure packetRef is not inlined
        
    }

    private void verifyMultiplicativeReference(PDLPacketDecl root, Set<String> declared,
            PDLItem item, PDLMultiplicity multiplicity) throws PDLException
    {
        if (multiplicity == null)
            return;
        if (multiplicity.getType() == PDLMultiplicity.Type.Variable)
            ensureReferencedExists(root, declared, item, multiplicity.getVariable());
    }

    private void ensureReferencedExists(PDLPacketDecl root, Set<String> declared, PDLItem item, String name) throws PDLException
    {
        if (name == null)
            throw new NullPointerException("reference name must not be null");

        if (!declared.contains(name))
        {
            boolean allowInlining = true;
            if (root.getName().equals(doc.getStartPacketName()))
                allowInlining = false;
            
            if (allowInlining)
            {
                ((PDLPacketDeclImpl) root).setInlined(true);
                // TODO warn
            }
            else
            {
                error(item, "item references '"+name+"' before assignment");
            }
        }
    }

    private void error(PDLPacketDecl item, String string) throws PDLException
    {
        throw new PDLException(string, item);
    }

    private void error(PDLItem item, String string) throws PDLException
    {
        throw new PDLException(item, string);
    }
    
}
