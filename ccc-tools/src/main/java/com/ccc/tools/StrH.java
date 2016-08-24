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

import java.util.Map.Entry;
import java.util.Properties;

@SuppressWarnings("javadoc")
public class StrH
{
    public final static char ForwardSlashSeperator = '/';
    public final static char BackSlashSeperator = '\\';

    public static String getParameter(Properties properties, String key, TabToLevel format) throws Exception
    {
        return getParameter(properties, key, null, format, true);
    }
    
    public static String getParameter(Properties properties, String key, String defaultValue, TabToLevel format) throws Exception
    {
        return getParameter(properties, key, defaultValue, format, false);
    }
    
    public static String getParameter(Properties properties, String key, String defaultValue, TabToLevel format, boolean required) throws Exception
    {
        String value = properties.getProperty(key);
        if(value != null)
        {
            value = value.trim();
            if(format != null)
                format.ttl(key, " = ", value);
        }
        else
        {
            if(required)
                throw new Exception(key + " not specified in properties file");
            if(defaultValue != null)
                value = defaultValue.trim();
            if(format != null)
                format.ttl(key, " = ", value, " (default injected)");
        }
        properties.remove(key);
        return value;
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
    
    public static String insureTailingSeparator(String value, char separator)
    {
        if(value == null || value.length() == 0)
            return value;
        value = value.trim();
        if(value.charAt(value.length()-1) == separator)
            return value;
        return value += separator;
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
