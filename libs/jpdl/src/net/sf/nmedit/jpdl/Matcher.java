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

package net.sf.nmedit.jpdl;

public abstract class Matcher
{
    public Matcher(Condition condition, boolean optional)
    {
	this.condition = condition;
	this.optional = optional;
    }
    
    public boolean isConditional()
    {
	return condition != null;
    }
    
    public boolean isOptional()
    {
	return optional;
    }
    
    public boolean trueCondition(Packet packet)
    {
	return condition != null ? condition.eval(packet) : false;
    }
  
    public abstract boolean match(Protocol protocol, BitStream data,
				  Packet result, int reserved);
    public abstract boolean apply(Protocol protocol, Packet packet,
				  IntStream data, BitStream result);
    public abstract int minimumSize();

    private boolean optional;
    private Condition condition;
    
    public String toString()
    {
        String params = toStringParams();
        if (params.length()>0)
            params=","+params;
        
        return getClass().getSimpleName()+"["
            +"matcher='"+getSource()+"'"+params+"]";
    }
    
    public abstract String getSource();

    protected String toStringParams()
    {
        return (condition==null?"":("condition='"+condition.getSource()+"'"));
    }

    protected void trace(Protocol protocol)
    {
        if (protocol.isTraceEnabled())
        {
            protocol.trace(getSource()
                    +"; "+getClass().getSimpleName()
                    +"["+toStringParams()+"]");
        }
    }
    
}
