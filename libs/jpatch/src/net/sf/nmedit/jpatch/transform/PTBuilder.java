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
package net.sf.nmedit.jpatch.transform;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.nmedit.jpatch.ModuleDescriptions;
import net.sf.nmedit.jpatch.PConnectorDescriptor;
import net.sf.nmedit.jpatch.PModuleDescriptor;
import net.sf.nmedit.jpatch.PParameterDescriptor;
import net.sf.nmedit.jpatch.PRuntimeException;

/**
 * Automat to build the structure as specified in the 
 * <a href="../../../../../../html/transform/transform.html#toc4." >Transformations XML v1.1</a> document format.
 * 
 * Each element (except the root element) of the XML Format has one or two corresponding methods in this class:
 * 
 * <ul>
 * <li><code>&lt;group&gt;</code>: {@link #beginGroup()}</li>
 * <li><code>&lt;/group&gt;</code>: {@link #endGroup()}</li>
 * <li><code>&lt;module&gt;</code>: {@link #beginModule(String)}</li>
 * <li><code>&lt;/module&gt;</code>: {@link #endModule()}</li>
 * <li><code>&lt;parameter ... /&gt;</code>: {@link #parameter(String, String)}</li>
 * <li><code>&lt;connector ... &gt;</code>: {@link #connector(String, String)}</li>
 * </ul>
 * 
 * @author Christian Schneider
 */
public class PTBuilder
{
    
    private ModuleDescriptions md;
    
    private boolean done = false;
    
    private PTModuleSelector currentModule;
    private List<PTModuleSelector> currentGroup;
    private List<List<PTModuleSelector>> groups;
    private Map<String, Integer> selectorIdMap;
    private Map<String, Integer> selectorTypeMap;
    private int selectorIdCounter = Integer.MIN_VALUE;
    private Integer TYPE_INPUT = 1;
    private Integer TYPE_OUTPUT = 1;
    private Integer TYPE_PARAMETER = 3;
    
    private PTModuleSelector[] merged;
    
    /**
     * Stores the component ids of the modules in the current group.
     * Used to find out if a module is declared multiple times which is an error.
     */ 
    private Set<Object> modules ;

    /**
     * Stores the component ids of the parameters of the current module.
     * Used to find out if a parameter is declared multiple times which is an error.
     */ 
    private Set<Object> parameters ;

    /**
     * Stores the component ids of the connectors of the current module.
     * Used to find out if a connector is declared multiple times which is an error.
     */ 
    private Set<Object> connectors ;
    
    /**
     * An automat for building the XML Transformation v1.1 document structure.
     * @param moduleDescriptions the module descriptors
     */
    public PTBuilder(ModuleDescriptions moduleDescriptions)
    {
        this.md = moduleDescriptions;
        selectorIdMap = new HashMap<String, Integer>();
        selectorTypeMap = new HashMap<String, Integer>();
        groups = new ArrayList<List<PTModuleSelector>>();
        modules = new HashSet<Object>();
        parameters = new HashSet<Object>();
        connectors = new HashSet<Object>();
    }

    /**
     * Begins a new group.
     * For each call to beginGroup() a call to {@link #endGroup()} must follow
     * after the modules were added to the group.
     * @throws PRuntimeException if {@link #done()} was called before or 
     *  a previous group was not completed by {@link #endGroup()}.
     */
    public void beginGroup()
    {
        ensureNotDone();
        if (currentGroup != null)
            throw new PRuntimeException("previous group not completed");

        selectorIdMap.clear();
        selectorTypeMap.clear();
        modules.clear();
        
        currentGroup = new ArrayList<PTModuleSelector>();
    }
    
    /**
     * Completes the group which was started using {@link #beginGroup()}. 
     * @throws PRuntimeException if {@link #done()} was called before or 
     *  no group was created by a previous call to {@link #beginGroup()}.
     */
    public void endGroup()
    {
        ensureNotDone();
        if (currentGroup == null)
            throw new PRuntimeException("no group selected");
        
        if (currentGroup.size()>1)
        {
            fixSelectorIds(currentGroup);
            // at least two modules must be present
            groups.add(currentGroup);
        }
        currentGroup = null;
    }

    /**
     * Adjusts the selector ids in the specified group. Afterwards
     * if a component from this group and a component from the previous
     * groups have the same selector id, then these components can
     * be mapped to each other. 
     * 
     * @param group the group in which the selector ids are adjusted
     */
    private void fixSelectorIds(List<PTModuleSelector> group)
    {
        Map<Integer, Integer> fixedIds = new HashMap<Integer, Integer>(group.size()*2);
        for (PTModuleSelector module: group)
        {
            fixSelectorIds(fixedIds, module);
        }
        
        // set new id's
        for (PTModuleSelector module: group)
        {
            boolean fixed = false;
            for (PTSelector sel: module)
            {
                Integer newId = fixedIds.get(sel.getSelectorId());
                if (newId != null)
                {
                    sel.setSelectorId(newId.intValue());
                    fixed = true;
                }
            }
            if (fixed) module.rebuildMap();
        }
    }

    private void fixSelectorIds(Map<Integer, Integer> fixedIds, PTModuleSelector module)
    {
        final Object mid = module.getDescriptor().getComponentId();
        
        for (List<PTModuleSelector> group: groups)
        {
            for (PTModuleSelector mreference: group)
            {
                if (mreference.getDescriptor().getComponentId().equals(mid))
                {
                    // same module
                    for (PTSelector refsel: mreference)
                    {
                        Object refid = refsel.getDescriptor().getComponentId();
                        for (PTSelector compsel: module)
                        {
                            if (refid.equals(compsel.getDescriptor().getComponentId()))
                            {
                                if (compsel.getSelectorId() != refsel.getSelectorId())
                                    fixedIds.put(compsel.getSelectorId(), refsel.getSelectorId());
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Begins a new module.
     * The specified componentId is used to lookup the module in the module descriptions. 
     * 
     * {@link #beginGroup()} must have been called before. 
     * 
     * @param componentId the component id if the module
     * @throws PRuntimeException if {@link #done()} was called before, 
     *  {@link #beginGroup()} was not called before or if the module
     *  with the specified component id does not exist.
     */
    public void beginModule(String componentId)
    {
        ensureNotDone();
        if (currentModule != null)
            throw new PRuntimeException("nested modules are not supported");
        PModuleDescriptor descriptor = md.getModuleById(componentId);
        if (descriptor == null)
            throw new PRuntimeException("module[id="+componentId+"] does not exist");
        if (modules.contains(descriptor.getComponentId()))
            throw new PRuntimeException("ambigious module "+descriptor);
        parameters.clear();
        connectors.clear();
        currentModule = new PTModuleSelector(descriptor);
        modules.add(descriptor.getComponentId());
    }

    /**
     * Completes the current module. {@link #beginModule(String)} must have been called before.
     * @throws PRuntimeException if {@link #done()} was called before or
     * {@link #beginModule(String)} was not called before.
     */
    public void endModule()
    {
        ensureNotDone();
        ensureModulePresent();
        currentGroup.add(currentModule);
        currentModule = null;
    }
    
    /**
     * Adds the connector to the current module element. The componentId is used to
     * lookup the connector descriptor in the currently selected module.  
     * 
     * @param componentId the connector id
     * @param selectorName selector
     * @throws PRuntimeException if {@link #beginModule(String)} was not called before,
     * {@link #done()} was called before or the connector with the specified componentId does not exist.
     */
    public void connector(String componentId, String selectorName)
    {
        ensureNotDone();
        ensureModulePresent();
        PConnectorDescriptor descriptor = currentModule.getDescriptor().getConnectorByComponentId(componentId);
        if (descriptor == null)
            throw new PRuntimeException("connector[id="+componentId+"] does not exist in "+currentModule);

        // ensure descriptor is only defined once
        if (connectors.contains(descriptor.getComponentId()))
            throw new PRuntimeException("ambigious connector "+descriptor);
        connectors.add(descriptor.getComponentId());
        
        int selectorId = getSelectorId(selectorName, descriptor.isOutput() ? TYPE_OUTPUT : TYPE_INPUT);
        currentModule.add(new PTSelector(descriptor, selectorId));
    }

    /**
     * Adds the parameter to the current module element. The componentId is used to
     * lookup the parameter descriptor in the currently selected module.  
     * 
     * @param componentId the parameter id
     * @param selectorName selector
     * @throws PRuntimeException if {@link #beginModule(String)} was not called before,
     * {@link #done()} was called before or the parameter with the specified componentId does not exist.
     */
    public void parameter(String componentId, String selectorName)
    {
        ensureNotDone();
        ensureModulePresent();
        PParameterDescriptor descriptor = currentModule.getDescriptor().getParameterByComponentId(componentId);
        if (descriptor == null)
            throw new PRuntimeException("parameter[id="+componentId+"] does not exist in "+currentModule);

        // ensure descriptor is only defined once
        if (parameters.contains(descriptor.getComponentId()))
            throw new PRuntimeException("ambigious parameter "+descriptor);
        parameters.add(descriptor.getComponentId());
        
        int selectorId = getSelectorId(selectorName, TYPE_PARAMETER);
        currentModule.add(new PTSelector(descriptor, selectorId));
    }

    /**
     * Ensures that {@link #beginModule(String)} was called but not completed
     * by {@link #endModule()}.
     * @throws PRuntimeException if the current module is null
     */
    private void ensureModulePresent()
    {
        if (currentModule == null)
            throw new PRuntimeException("no module selected");
    }

    /**
     * Ensures that {@link #done()} was not called before.
     * @throws PRuntimeException if done() was called before
     */
    private void ensureNotDone()
    {
        if (done)
            throw new PRuntimeException("can not add more elements");
    }
    
    /**
     * Returns the id for the specified selector.
     * The argument type is used to ensure that no connector has the same selector as a parameter.
     * The argument type can take one of the values
     * <ul>
     * <li>{@link #TYPE_INPUT}</li>
     * <li>{@link #TYPE_OUTPUT}</li>
     * <li>{@link #TYPE_PARAMETER}</li>
     * </ul>
     * 
     * @param selectorName name of the selector
     * @param type type of the selected component (parameter or connector) 
     * @return the selector id
     * @throws PRuntimeException if the selector name is used by connectors and parameters
     */
    private int getSelectorId(String selectorName, Integer type)
    {
        int selectorId;
        
        // get the selector id
        Integer selected = selectorIdMap.get(selectorName);
        
        // see if the selector id is already defined, otherwise create a new id
        if (selected != null)
        {
            // id is already defined => now ensure that parameters and connectors do not have the same id
            if (!type.equals(selectorTypeMap.get(selectorName)))
                throw new PRuntimeException("parameter/input/output have the same selector name:"+selectorName+", "+currentModule);
            // set the id
            selectorId = selected.intValue();
        }
        else
        {
            // create a new id
            selectorId = selectorIdCounter++;
            // store the id for the selector name
            selectorIdMap.put(selectorName, selectorId);
            // store the type of the selector (connector or parameter)
            selectorTypeMap.put(selectorName, type);
        }
        // return the selector id
        return selectorId;
    }
    
    /**
     * Completes the document. This must be called before {@link #getGroups()} can be called.
     * 
     * The first time the method is called the groups are tested to ensure that the 
     * defined module combinations are not ambiguous (two groups must not contain same pairs of modules). 
     * 
     * @throws PRuntimeException If the latest call to
     *  {@link #beginGroup()} was not completed by {@link #endGroup()}.  
     */
    public void done() 
    {
        if (currentGroup != null)
            throw new PRuntimeException("current group not completed");
        
        if (done) return;
        validateGroups();
        
        merged = merge(groups);
        
        done = true;
    }
    
    /**
     * Returns the defined module mappings.
     * 
     * @return the defined module mappings.
     * @throws PRuntimeException {@link #done()} was not called before
     */
    public PTModuleSelector[] getSelectors()
    {
        if (!done) throw new PRuntimeException("groups are not completed");
        return merged;
    }

    /**
     * Ensures that two different groups do not contain the same module pairs. 
     * More formally ensures that the condition |intersection(g1, g2)|&lt;2 is 
     * true. 
     */
    protected void validateGroups()
    {
        // groups g1, g2, g1!=g2
        // modules m1, m2, m1!=m2
        // intersection({m1,m2},  g1) != intersection({m1,m2}, g2)
        //
        // thus if m1 and m2 are both in g1 then
        //  both can not be in g2 at the same time
        //
        // this implies that |intersection(g1, g2)|<2
        
        // for each group g1, g2, g1!=g2:
        
        final int groupCount = groups.size();
        
        for (int i=0;i<groupCount-2;i++)
        {
            List<PTModuleSelector> g1 = groups.get(i);
            for (int j=i+1;j<groupCount-1;j++)
            {
                List<PTModuleSelector> g2 = groups.get(j);
                // count number of elements in intersection
                int intersection = 0;
                for (PTModuleSelector m: g1)
                {
                    if (contains(g2, m))
                    {
                        // => (m in g1) and (m in g2)
                        if (intersection>=2)
                        {
                            // condition is violated
                            throw new PRuntimeException("ambigious module "+m.getDescriptor()
                                    +"/condition violated: '|{m : m in g1, m in g2|<2'");
                        }
                    }
                }
            }
        }
    }

    public static PTModuleSelector[] merge(List<List<PTModuleSelector>> groups)
    {
        List<PTModuleSelector> modules = new ArrayList<PTModuleSelector>(groups.size()*2);
        
        // add all module selectors
        for (List<PTModuleSelector> group: groups)
            modules.addAll(group);
        
        // merge module selectors with the same module descriptor
        for (int i=0;i<modules.size()-1;i++)
        {
            PTModuleSelector a = modules.get(i);
            for (int j=i+1;j<modules.size();)
            {
                PTModuleSelector b = modules.get(j);
                
                if (a.getDescriptor().getComponentId().equals(b.getDescriptor().getComponentId()))
                {
                    // remove b
                    modules.remove(j);
                    
                    // a <- union (a,b)
                    for (PTSelector s: b)
                        if (!a.containsKey(s.getSelectorId()))
                            a.add(s);
                    
                    continue; // do not increment j because element was removed
                }
                j++;
            }
        }
        
        return modules.toArray(new PTModuleSelector[modules.size()]);
    }
    /**
     * Returns true if the module is already in the group.
     * @param group set of module selectors
     * @param m the module
     * @return true if the module is already in the group
     */
    private boolean contains(List<PTModuleSelector> group, PTModuleSelector m)
    {
        for (PTModuleSelector b: group)
        {
            if (b.getDescriptor().getComponentId().equals(m.getDescriptor().getComponentId()))
                return true;
        }
        return false;
    }

}
