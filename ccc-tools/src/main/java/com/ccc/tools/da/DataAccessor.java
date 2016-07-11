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
package com.ccc.tools.da;

import java.util.Properties;

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
    
    public void init(Properties properties) throws Exception;
    public boolean isUp();
    public void close();
}
