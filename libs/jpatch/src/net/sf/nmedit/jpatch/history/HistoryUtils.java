package net.sf.nmedit.jpatch.history;

public class HistoryUtils
{

    public static String quote(Object o)
    {
        return (o == null) ? "\"\"" : "\""+String.valueOf(o)+"\"";
    }
    
}
