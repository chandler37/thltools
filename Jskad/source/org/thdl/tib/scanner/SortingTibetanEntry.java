package org.thdl.tib.scanner;

import org.thdl.tib.text.*;

class SortingTibetanEntry implements Comparable
{
    private String fields[];
    private TibetanString header;
    
    public SortingTibetanEntry (String header, String fields[])
    {
        this.header = new TibetanString(header);
        this.fields = fields;
    }
    
    public int compareTo(Object o)
    {
        SortingTibetanEntry ste = (SortingTibetanEntry)o;
        return header.compareTo(ste.header);
    }
    
    public String[] get()
    {
        return fields;
    }
}