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

import java.util.Collection;

import net.sf.nmedit.jpatch.MoveOperation;
import net.sf.nmedit.jpatch.PConnection;
import net.sf.nmedit.jpatch.PConnectionManager;
import net.sf.nmedit.jpatch.PConnector;
import net.sf.nmedit.jpatch.PConnectorDescriptor;
import net.sf.nmedit.jpatch.PModule;
import net.sf.nmedit.jpatch.PModuleContainer;
import net.sf.nmedit.jpatch.PModuleDescriptor;
import net.sf.nmedit.jpatch.PParameter;
import net.sf.nmedit.jpatch.PParameterDescriptor;
import net.sf.nmedit.jpatch.PRuntimeException;

/**
 * Defines how the the parameters and connectors of two modules are mapped to each other.
 * @author Christian Schneider
 */
public class PTModuleMapping
{

    private PModuleDescriptor a;
    private PModuleDescriptor b;
    private PParameterDescriptor[] parameters;
    private PConnectorDescriptor[] connectors;
    private double covering = -1;
    
    /**
     * Creates a new mapping of the two modules a and b.
     * The specified parameter/connector arrays must contains the component pairs of both modules.
     * The elements at the index (2*index) must be components of module a.
     * The elements at the index (2*index+1) must be components of module b.
     * Each two fields ((2*index), (2*index+1)) are two components mapped to each other.
     * 
     * @param a the mapped module
     * @param b the mapped module
     * @param parameters the parameter mappings
     * @param connectors the connector mappings
     */
    public PTModuleMapping(PModuleDescriptor a, PModuleDescriptor b, PParameterDescriptor[] parameters,
            PConnectorDescriptor[] connectors)
    {
        this.a = a;
        this.b = b;
        this.parameters = parameters;
        this.connectors = connectors;
    }

    /**
     * Computes the relation of the parameters/connectors which are mapped and all existing components.
     * 
     * @param weightParameters weights the parameter relation
     * @param weightInputs weights the input connector relation
     * @param weightOutputs weights the outpput connector relation
     * @return returns a value in the range [0..1]. 1 means that all connectors/parameters of module A
     * are mapped to all connectors/parameters of module B, 0 means that no mapping is possible (this statement
     * is only true if the argements are all set to 1.0d). 
     */
    public double computeCovering(double weightParameters, double weightInputs, double weightOutputs)
    {
        final double epsilon = 10e-30;
        
        if (weightInputs<0||weightOutputs<0||weightParameters<0)
            throw new IllegalArgumentException("invalid weight:<0");

        // normalize
        {
            double totalWeight = weightParameters+weightInputs+weightOutputs;
            
            weightParameters/=totalWeight;
            weightInputs/=totalWeight;
            weightOutputs/=totalWeight;
        }

        // compute
        double covering = 0;
        if (weightParameters>epsilon)
        {
            int available = Math.max(a.getParameterDescriptorCount(), 
                    b.getParameterDescriptorCount());
            
            double value = (available == 0) ? 0 : (getParameterCount() / (double) available);
           
            covering+=value*weightParameters;
        }
        
        if (weightInputs+weightOutputs>epsilon)
        {
            int maxAvailableInputs;
            int maxAvailableOutputs;
            {
                int availableInputsA = 0;
                int availableInputsB = 0;
                int availableOutputsA = 0;
                int availableOutputsB = 0;
    
                for (int i=a.getConnectorDescriptorCount()-1;i>=0;i--)
                    if (a.getConnectorDescriptor(i).isOutput())
                        availableOutputsA++;
                    else
                        availableInputsA++;
                for (int i=b.getConnectorDescriptorCount()-1;i>=0;i--)
                    if (b.getConnectorDescriptor(i).isOutput())
                        availableOutputsB++;
                    else
                        availableInputsB++;
                
                maxAvailableInputs = Math.max(availableInputsA, availableInputsB);                
                maxAvailableOutputs = Math.max(availableOutputsA, availableOutputsB);
            }
            
            int inputs = 0;
            int outputs = 0;
            
            for (int i=getConnectorCount()-1;i>=0;i--)
            {
                // getConnectorA(i).isOutput()<=>getConnectorB(i).isOutput()
                if (getConnectorA(i).isOutput())
                    outputs++;
                else
                    inputs ++;
            }

            if (maxAvailableInputs > 0)
                covering+=weightInputs*(inputs/(double)maxAvailableInputs);
            if (maxAvailableOutputs > 0)
                covering+=weightOutputs*(outputs/(double)maxAvailableOutputs);
            
        }
        
        return covering;
    }
    
    public double getConnectorCovering()
    {
        return computeCovering(0,1,1);
    }

    public double getParameterCovering()
    {
        return computeCovering(1,0,0);
    }

    /**
     * Computes the relation of the parameters/connectors which are mapped and all existing components.
     * 
     * @return returns a value in the range [0..1]. 1 means that all connectors/parameters of module A
     * are mapped to all connectors/parameters of module B, 0 means that no mapping is possible. 
     */
    public double getCovering()
    {
        if (covering<0)
            covering = computeCovering(1,1,1);
        return covering;
    }
    
    /**
     * Returns the number of mapped parameters.
     * @return the number of mapped parameters
     */
    public int getParameterCount()
    {
        return parameters.length/2;
    }

    /**
     * Returns the number of mapped connectors.
     * @return the number of mapped connectors
     */    
    public int getConnectorCount()
    {
        return connectors.length/2;
    }

    /**
     * Returns module A.
     * @return module A
     */
    public PModuleDescriptor getModuleA()
    {
        return a;
    }

    /**
     * Returns module B.
     * @return module B
     */
    public PModuleDescriptor getModuleB()
    {
        return b;
    }

    /**
     * Returns the parameter of the module A at the specified index.
     * @return parameter of module A
     */
    public PParameterDescriptor getParameterA(int index)
    {
        return parameters[(2*index)];
    }

    /**
     * Returns the parameter of the module B at the specified index.
     * @return parameter of module B
     */
    public PParameterDescriptor getParameterB(int index)
    {
        return parameters[(2*index)+1];
    }

    /**
     * Returns the connector of the module A at the specified index.
     * @return connector of module A
     */
    public PConnectorDescriptor getConnectorA(int index)
    {
        return connectors[(2*index)];
    }

    /**
     * Returns the connector of the module B at the specified index.
     * @return connector of module B
     */
    public PConnectorDescriptor getConnectorB(int index)
    {
        return connectors[(2*index)+1];
    }
    
    /**
     * Transforms the specified module.
     * 
     * @param source the source module
     * @throws PRuntimeException if the module has no parent (module container) 
     *  or the neither module A nor module B describe the specified source module,
     *  thus if this mapping is not able to transform the source module 
     */
    public PModule transform(PModule source)
    {
        int offset1;
        int offset2;
        PModuleDescriptor destination;
        
        if (a.equals(source.getDescriptor()))
        {
            offset1 = 0;
            offset2 = 1;
            destination = b;
        }
        else if (b.equals(source.getDescriptor()))
        {
            offset1 = 1;
            offset2 = 0;
            destination = a;
        }
        else throw new PRuntimeException("transformation failed");

        PModuleContainer container = source.getParentComponent();
        
        if (container == null)
            throw new PRuntimeException("module has no parent");
        
        PConnectionManager cm = container.getConnectionManager();
        
        // create new module
        PModule m1 = source;
        PModule m2 = container.createModule(destination);
        int m1index = m1.getComponentIndex();
        m2.setScreenLocation(m1.getScreenX(), m1.getScreenY());

        // assign parameters
        for (int i=parameters.length-2;i>=0;i-=2)
        {
            PParameter p1 = m1.getParameter(parameters[i+offset1]);
            PParameter p2 = m2.getParameter(parameters[i+offset2]);
            p2.setFloatValue(p1.getFloatValue());
        }

        // remember connections 
        Collection<PConnection> connections = null;
        
        if (cm != null)
        {
            connections = cm.connections(m1);
            cm.removeAllConnections(connections);
        }
        
        // replace module
        if (!container.remove(m1)) return null;
        container.add(m1index, m2);
        MoveOperation move = m2.getParentComponent().createMoveOperation();
        move.setScreenOffset(0, 0);
        move.add(m2);
        move.move();

        // recreate connections
        if (connections != null)
        {
            for (PConnection connection: connections)
            {
                PConnectorDescriptor prevA;
                PConnector fix;
                
                if (connection.getModuleA() == m1)
                {
                    prevA = connection.getDescriptorA();
                    fix = connection.getB();
                }
                else if (connection.getModuleB() == m1)
                {
                    prevA = connection.getDescriptorB();
                    fix = connection.getA();
                }
                else
                {
                    continue;
                }

                PConnectorDescriptor newA = getDestination(prevA);
                if (newA != null)
                {
                    PConnector newConnector = m2.getConnector(newA);
                    if (newConnector == null)
                        throw new PRuntimeException("connector not found: "+newA);
    
                    cm.add(fix, newConnector);
                }
            }
        }
        
        return m2;
    }

    private PConnectorDescriptor getDestination(PConnectorDescriptor d)
    {
        for (int i=getConnectorCount()-1;i>=0;i--)
        {
            PConnectorDescriptor a = getConnectorA(i);
            PConnectorDescriptor b = getConnectorB(i);
            
            if (a==d) return b;
            else if (b==d) return a;
            else if (a.equals(d)) return b;
            else if (b.equals(d)) return a;
        }
        return null;
    }

    public PModuleDescriptor getTarget(PModuleDescriptor source)
    {
        if (source == a) return b;
        else if (source == b) return a;
        else if (source.equals(a)) return b;
        else if (source.equals(b)) return a;
        else return null;
    }
    
    public String toString()
    {
        return getClass().getName()+"[a="+a+",b="+b+"]";
    }
    
}
