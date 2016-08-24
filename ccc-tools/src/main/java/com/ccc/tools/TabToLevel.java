/*
**  Copyright (c) 2016, Chad Adams.
**
**  This program is free software: you can redistribute it and/or modify
**  it under the terms of the GNU Lesser General Public License as 
**  published by the Free Software Foundation, either version 3 of the 
**  License, or any later version.
**
**  This program is distributed in the hope that it will be useful,
**  but WITHOUT ANY WARRANTY; without even the implied warranty of
**  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
**  GNU General Public License for more details.

**  You should have received copies of the GNU GPLv3 and GNU LGPLv3
**  licenses along with this program.  If not, see http://www.gnu.org/licenses
*/
package com.ccc.tools;

import java.util.concurrent.atomic.AtomicInteger;

@SuppressWarnings("javadoc")
public class TabToLevel
{
    public volatile StringBuilder sb;
    public final AtomicInteger level;
    
    public TabToLevel()
    {
        this((StringBuilder)null);
    }
    
    public TabToLevel(String initialLine)
    {
        this((StringBuilder)null);
        sb.append("\n");
        sb.append(initialLine);
        sb.append("\n");
        level.incrementAndGet();
    }
    
    public void clear()
    {
        level.set(0);
        sb = new StringBuilder();
    }
    
    public TabToLevel(StringBuilder sbIn)
    {
        if(sbIn == null)
            sbIn = new StringBuilder();
        sb = sbIn;
        level = new AtomicInteger(0);
    }
    
    public void ttl(Object ... values)
    {
        String[] array = new String[values.length];
        for(int i=0; i < array.length; i++)
            array[i] = (values[i] == null ? "null" : values[i].toString());
        tabToLevel(true, array);
    }

    public void ttln(Object ... values)
    {
        String[] array = new String[values.length];
        for(int i=0; i < array.length; i++)
            array[i] = (values[i] == null ? "null" : values[i].toString());
        tabToLevel(false, array);
    }
    
    public void tabToLevel(boolean eol, String ... values)
    {
        for(int i=0; i < level.get(); i++)
            sb.append("\t");
        for(int j=0; j < values.length; j++)
            sb.append(values[j]);
        if(eol)
            sb.append("\n");
    }
    
    public void inc()
    {
        level.incrementAndGet();
    }
    
    public void dec()
    {
        level.decrementAndGet();
    }
    
    public void indentedOk()
    {
        level.incrementAndGet();
        ttl("ok");
        level.decrementAndGet();
    }
    
    @Override
    public String toString()
    {
        return sb.toString();
    }
}
