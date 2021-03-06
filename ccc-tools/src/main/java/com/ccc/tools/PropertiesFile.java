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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;

import com.ccc.tools.StrH.StringPair;

/**
 * Static properties file helper
 */
public class PropertiesFile
{

    private PropertiesFile()
    {
        // prevent construction
    }

    /**
     * Load a properties file from the given path from the filesystem.
     * 
     * @param url
     *            The URL to load. This must not be null.
     * @return the properties object loaded with the name/value pairs from the given file. Will not return null.
     * @throws FileNotFoundException
     *             if the file could not be found on the filesystem.
     * @throws IOException
     *             if there was an error reading the file.
     * @throws IllegalArgumentException
     *             if file is null.
     */
    @SuppressWarnings("resource")
    public static Properties load(URL url) throws IOException
    {
        if (url == null)
            throw new IllegalArgumentException("url == null");
        Properties properties = new Properties();
        InputStream stream = url.openStream();
        try
        {
            properties.load(stream);
        } finally
        {
            try
            {
                if (stream != null)
                    stream.close();
            } catch (Exception e)
            {
                e.toString(); // lose findbugs warning best try
            }
        }
        return properties;
    }

    /**
     * Load a properties file from the given path, either from the filesystem or from the classpath.
     * 
     * @param properties
     *            The properties object to be populated from the contents of the file. This must not be null.
     * @param path
     *            The path to the file. This must not be null.
     * @throws FileNotFoundException
     *             if the file could not be found on the filesystem or in the classpath.
     * @throws IOException
     *             if there was an error reading the file.
     */
    public static void load(Properties properties, String path) throws FileNotFoundException, IOException
    {
        if (properties == null || path == null)
            throw new IllegalArgumentException("properties == null || path == null");
        try
        {
            loadFile(properties, new File(path));
        } catch (FileNotFoundException ex)
        {
            try
            {
                loadResource(properties, path);
            } catch (FileNotFoundException e)
            {
                throw new FileNotFoundException(path + " not found on filesystem or classpath");
            }
        }
    }

    /**
     * Load a properties file from the given path from the filesystem.
     * 
     * @param properties
     *            The properties object to be populated from the contents of the file. This must not be null.
     * @param file
     *            The file to load. This must not be null.
     * @throws FileNotFoundException
     *             if the file could not be found on the filesystem.
     * @throws IOException
     *             if there was an error reading the file.
     */
    public static void loadFile(Properties properties, File file) throws FileNotFoundException, IOException
    {
        if (properties == null || file == null)
            throw new IllegalArgumentException("properties == null || path == null");
        @SuppressWarnings("resource")
        InputStream isPrimary = null;
        try
        {
            isPrimary = new FileInputStream(file);
            properties.load(isPrimary);
        } finally
        {
            try
            {
                if (isPrimary != null)
                    isPrimary.close();
            } catch (Exception e)
            {
                // Ignore
            }
        }
    }

    /**
     * Load a properties file from the given path from the classpath.
     * 
     * @param properties
     *            The properties object to be populated from the contents of the file. This must not be null.
     * @param path
     *            The path to the file. This must not be null.
     * @throws FileNotFoundException
     *             if the file could not be found on the classpath.
     * @throws IOException
     *             if there was an error reading the file.
     */
    @SuppressWarnings("resource")
    public static void loadResource(Properties properties, String path) throws FileNotFoundException, IOException
    {
        if (properties == null || path == null)
            throw new IllegalArgumentException("properties == null || path == null");
        InputStream isPrimary = null;
        try
        {
            isPrimary = ClassLoader.getSystemResourceAsStream(path);
            if (isPrimary == null)
            {
                isPrimary = ClassLoader.getSystemResourceAsStream(path.substring(path.lastIndexOf('/')));
                if (isPrimary == null)
                {
                    throw new FileNotFoundException(path + " not found on classpath");
                }
            }
            properties.load(isPrimary);
        } finally
        {
            try
            {
                if (isPrimary != null)
                    isPrimary.close();
            } catch (Exception e)
            {
                // Ignore
            }
        }
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
