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

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;
import java.util.Map.Entry;

import com.ccc.tools.StrH.StringPair;

@SuppressWarnings("javadoc")
public class PropertiesHelper
{
    private PropertiesHelper()
    {
    }
    
    public static List<Entry<String, String>> getPropertiesForBaseKey(String baseKey, Properties properties)
    {
        ArrayList<Entry<String, String>> entries = new ArrayList<Entry<String, String>>();
        Hashtable<Integer, StringPair> orderingMap = new Hashtable<Integer, StringPair>();
        
        int found = 0;
        for (Entry<Object, Object> entry : properties.entrySet())
        {
            String key = (String) entry.getKey();
            int index = 0;
            if (key.startsWith(baseKey))
            {
                ++found;
                char[] chars = key.toCharArray();
                if(Character.isDigit(chars[chars.length-1]))
                {
                    int strIndex = 0;
                    for(int i=chars.length-1; i >=0; i--)
                    {
                        if(!Character.isDigit(chars[i]))
                        {
                            strIndex = i + 1;
                            break;
                        }
                    }
                    index = Integer.parseInt(key.substring(strIndex));
                }
                orderingMap.put(index, new StringPair(entry));
            }
        }
        int i=0;
        int hit = 0;
        do
        {
            StringPair pair = orderingMap.get(i);
            if(pair != null)
            {
                entries.add(pair);
                ++hit;
            }
            ++i;
        }while(hit < found);
        return entries;
    }
}
