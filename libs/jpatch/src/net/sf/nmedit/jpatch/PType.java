package net.sf.nmedit.jpatch;

public class PType
{

    private int id;
    private String name;

    public PType(int id, String name)
    {
        this.id = id;
        this.name = name;
    }

    /**
     * Returns the name of this type. The return value
     * must not be <code>null</code>.
     * @return the name of the type
     */
    public String getName()
    {
        return name;
    }

    /**
     * Returns an identifier for this type.
     * The identifier is unique in the {@link PTypes defined type set}.
     * @return itentifier for this type.
     */
    public int getId()
    {
        return id;
    }

    /**
     * Returns the {@link getId() id} of this type.
     */
    public int hashCode()
    {
        return id;
    }
    
    public boolean equals(Object o)
    {
        if (o == this) return true;
        if (o == null || (!(o instanceof PType))) return false;
        
        PType t = (PType) o;
        return t.id == id && eq(t.name, name);
    }

    public String toString()
    {
        return getClass().getName()+"[id="+getId()+",name="+getName()+"]";
    }

    protected final boolean eq(Object a, Object b)
    {
        return a == b || (a!=null && a.equals(b));
    }

}
