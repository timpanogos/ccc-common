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

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.lang.management.ManagementFactory;

import javax.management.MBeanServer;
import javax.management.ObjectName;

/**
 * Interact with the OS ProcessID, or pid
 */
public class ProcessID {
	
	private ProcessID() {
        /* Prevent construction. */
    }
	
    /**
     * From OS, extract ProcessID (PID) for this app and write it to file 
     * @param filename Full path filename of file to write pid to (not written for null)
     * @return The pid
     * @throws Exception Throw if pid number cannot be written to a file.
     */
    public static int fileWrite(String filename) throws Exception {
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        String pid = (String) mbs.getAttribute(new ObjectName("java.lang:type=Runtime"), "Name");
        if( pid.indexOf('@') != -1 )
            pid = pid.substring(0, pid.indexOf('@'));
        if (filename != null) {	
        	DataOutputStream out = new DataOutputStream(new FileOutputStream(filename));
        	try {
        		out.writeBytes(pid);
        	} finally {
        		out.close();
        	}
        }
    	return Integer.parseInt(pid);
    }	
}