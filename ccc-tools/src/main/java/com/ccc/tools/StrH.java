/*
**  Copyright (c) 2016, Cascade Computer Consulting.
**
**  Permission to use, copy, modify, and/or distribute this software for any
**  purpose with or without fee is hereby granted, provided that the above
**  copyright notice and this permission notice appear in all copies.
**
**  THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
**  WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
**  MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR
**  ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
**  WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
**  ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF
**  OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
*/
package com.ccc.tools;

import java.util.Map.Entry;


@SuppressWarnings("javadoc")
public class StrH
{
    public final static char ForwardSlashSeperator = '/';
    public final static char BackSlashSeperator = '\\';

    public static StringBuilder ttl(StringBuilder sb, int level, Object ... values)
    {
        Object[] array = new String[values.length];
        for(int i=0; i < array.length; i++)
            array[i] = (values[i] == null ? "null" : values[i].toString());
        return tabToLevel(sb, level, true, array);
    }

    /**
     * Tab to level.
     *
     * @param sb to add the values to
     * @param level number of tabs
     * @param eol add eol at end of values
     * @param values list of values to add
     * @return the string builder that has handed in.
     */
    public static StringBuilder tabToLevel(StringBuilder sb, int level, boolean eol, Object ... values)
    {
        for(int i=0; i < level; i++)
            sb.append("\t");
        for(int j=0; j < values.length; j++)
        {
            Object v = values[j];
            if(v == null)
                v = new String("null");
            else
                v = v.toString();
            sb.append(v);
        }
        if(eol)
            sb.append("\n");
        return sb;
    }
    
    public static String trim(String value)
    {
        if(value == null)
            return value;
        return value.trim();
    }
    
    /**
     * Pad string to width and add to StringBuilder.
     *
     * @param sb to add newValue to
     * @param newValue the newValue to be padded. 
     * @param length desired length of newValue
     * @return the input sb if it was not null, otherwise one was provided.
     */
    public static StringBuilder pad(StringBuilder sb, String newValue, int length)
    {
        if(sb == null)
            sb = new StringBuilder();
        if(newValue == null)
            newValue = "";
        sb.append(newValue);
        int delta = length - newValue.length();
        if(delta <= 0)
            return sb;
        for(int i=0; i < delta; i++)
            sb.append(" ");
        return sb;
    }
    
    /**
     * Gets the atomic name from a character separated full name.
     *
     * @param name the fully distinguished name
     * @param seperator the name space separator
     * @return the atomic name
     */
    public static String getAtomicName(String name, char seperator)
    {
        if (name == null)
            return null;

        int index = name.lastIndexOf(seperator);
        if (index == -1)
            return name;
        return name.substring(++index);
    }
    
    /**
     * Gets the penultimate name.
     *
     * @param name the name
     * @param seperator the separator
     * @return the penultimate name
     */
    public static String getPenultimateName(String name, char seperator)
    {
        if (name == null)
            return null;

        int index = name.lastIndexOf(seperator);
        if (index == -1)
            return null;
        return (name.substring(0, index));
    }
    
    /**
     * Gets the atomic name from a file system path
     * @param name the name
     * @return the atomic name from the path
     */
    public static String getAtomicNameFromPath(String name)
    {
        if (name == null)
            return null;

        int index = name.lastIndexOf(ForwardSlashSeperator);
        if (index == -1)
            index = name.lastIndexOf(BackSlashSeperator);
        if (index == -1)
            index = name.lastIndexOf(".");
        if (index == -1)
            return name;

        ++index;
        return (name.substring(index));
    }
    
    /**
     * Gets the penultimate name from a file system path.
     * @param name the name
     * @return the penultimate name
     */
    public static String getPenultimateNameFromPath(String name)
    {
        if (name == null)
            return null;

        int index = name.lastIndexOf(ForwardSlashSeperator);
        if (index == -1)
            index = name.lastIndexOf(BackSlashSeperator);
        if (index == -1)
            return null;

        return (name.substring(0, index));
    }
    
    public static String stripTrailingSeparator(String value)
    {
        if(value == null || value.length() == 0)
            return value;
        value = value.trim();
        if(value.charAt(value.length() - 1) == '/')
            value = value.substring(0, value.length() - 1);
        if(value.charAt(value.length() - 1) == '\\')
            value = value.substring(0, value.length() - 1);
        return value;
    }
    
    public static String insureLeadingSeparator(String value, char separator)
    {
        if(value == null || value.length() == 0)
            return value;
        value = value.trim();
        if(value.charAt(0) == separator)
            return value;
        return separator + value;
    }
    
    public static class StringPair implements Entry<String, String>
    {
        private final String key;
        private String value;
        
        public StringPair(Entry<Object, Object> entry)
        {
            key = (String) entry.getKey();
            value = (String) entry.getValue();
        }
        
        @Override
        public String setValue(String value)
        {
            String old = this.value;
            this.value = value;
            return old;
        }

        @Override
        public String getKey()
        {
            return key;
        }

        @Override
        public String getValue()
        {
            return value;
        }
        
        @Override
        public String toString()
        {
            return key+"="+value; 
        }
    }
}
