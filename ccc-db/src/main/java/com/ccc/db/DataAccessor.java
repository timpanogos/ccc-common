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
package com.ccc.db;

import java.util.Properties;
import java.util.concurrent.ExecutorService;

@SuppressWarnings("javadoc")
public interface DataAccessor
{
    public static String DaDataSourceTomcatKey = "ccc.tools.da.datasource.tomcat";
    public static String DaImplKey = "ccc.tools.da.impl-class";
    public static String DaUserKey = "ccc.tools.da.user";
    public static String DaPassKey = "ccc.tools.da.password";
    public static String DaHostKey = "ccc.tools.da.host";
    public static String DaPortKey = "ccc.tools.da.port";
    public static String DaDbNameKey = "ccc.tools.da.db-name";
    public static String DaUrlPrefixKey = "ccc.tools.da.url-prefix";
    public static String DaUrlKey = "ccc.tools.da.url";

    public void init(Properties properties) throws Exception;
    public void setExecutor(ExecutorService executor) throws Exception;
    public boolean isUp();
    public void close();
}
