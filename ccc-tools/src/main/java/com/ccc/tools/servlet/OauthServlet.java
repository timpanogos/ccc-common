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
package com.ccc.tools.servlet;

import java.util.Calendar;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.wicket.authroles.authentication.AbstractAuthenticatedWebSession;
import org.apache.wicket.authroles.authentication.AuthenticatedWebApplication;
import org.apache.wicket.markup.html.WebPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ccc.tools.TabToLevel;
import com.ccc.tools.app.PropertiesFile;
import com.ccc.tools.servlet.clientInfo.BaseClientInfo;
import com.ccc.tools.servlet.login.SignIn20Page;

@SuppressWarnings("javadoc")
public abstract class OauthServlet extends AuthenticatedWebApplication
{
    public static final String LogFilePathKey = "ccc.tools.log-file-path";
    public static final String LogFileBaseKey = "ccc.tools.log-file-base";
    public static final String ServletConfigKey = "ccc.tools.servlet.config";
//    public static final String ContextRealBaseKey = "ccc.tools.servlet.context-base";
    public static final String WicketPropertiesKey = "ccc.tools.wicket.properties"; // used to store file properties into wicket properties
    public static final String CopyrightYearKey = "ccc.tools.servlet.copyright-year";
    public static final String CopyrightOwnerKey = "ccc.tools.servlet.copyright-owner";
    public static final String OauthImplClassKey = "ccc.tools.servlet.oauth-class";
    public static final String CoreImplClassKey = "ccc.tools.servlet.core-class";
    public static final String BlockingExcecutorKey = "ccc.tools.executor.blocking-executor"; // system properties global key
    public static final String ScheduledExcecutorKey = "ccc.tools.executor.scheduled-executor"; // system properties global key
    
    public static final String CopyrightYearDefault = Integer.toString(Calendar.getInstance().get(Calendar.YEAR));
    public static final String CopyrightOwnerDefault = "";

    public static final String  GoogleCSEcxKey = "ccc.tools.servlet.gcse.cx";
    public static final String GoogleCSEcxDefault = "";
    
    protected final Logger log;
    protected volatile String contextPath;
    protected volatile CoreController coreController;
    protected volatile Properties properties;
    
    public OauthServlet()
    {
        log = LoggerFactory.getLogger(getClass());
    }
    
    public Properties getFileProperties()
    {
        return properties;
    }

    abstract protected String getLogFilePathDefault();
    abstract protected String getOauthImplClassDefault();
    abstract protected String getCoreImplClassDefault();
    abstract protected String getServletConfigDefault();
    abstract protected void init(TabToLevel sb);
    abstract protected Class<? extends AbstractAuthenticatedWebSession> getWebSessionImplClass();
    abstract protected Class<? extends WebPage> getSignInPageImplClass();
    abstract protected BaseClientInfo getBaseClientInfo();
   
    @Override
    protected Class<? extends AbstractAuthenticatedWebSession> getWebSessionClass()
    {
        return getWebSessionImplClass();
    }

    @Override
    protected Class<? extends WebPage> getSignInPageClass()
    {
        return SignIn20Page.class;
    }
    
    
    @Override
    public void init()
    {
        super.init();
        getDebugSettings().setAjaxDebugModeEnabled(false);
                
        contextPath = getServletContext().getRealPath("/");
        TabToLevel format = new TabToLevel();
        format.ttl("\n", getClass().getSimpleName(), ".init");
        format.inc();
        format.ttl("applicationKey: ", getApplicationKey());
        format.ttl("context path: ", contextPath);
        
        String fileNameIn = getInitParameter(ServletConfigKey);
        String fileName = getServletConfigDefault();
        if (fileNameIn != null)
            fileName = fileNameIn;
        else
            fileNameIn = "null";
        format.ttl("propertiesFile: ", fileName, " (", fileNameIn, ")");

        System.setProperty(LogFilePathKey, fileName);
        int idx = fileName.lastIndexOf(".");
        System.setProperty(LogFileBaseKey, fileName.substring(0, idx));
        
        properties = new Properties();
        String msg = "configuration properties file not found: " + fileName;
        try
        {
            PropertiesFile.load(properties, fileName);
            
            String googleIn = properties.getProperty(GoogleCSEcxKey);
            String google = GoogleCSEcxDefault;
            if (googleIn != null)
                google = googleIn;
            else
                googleIn = "null";
            format.ttl(GoogleCSEcxKey, ": ", google, " (", googleIn, ")");
            System.setProperty(GoogleCSEcxKey, google);
            
            String copyrightYearIn = properties.getProperty(CopyrightYearKey);
            String copyrightYear = CopyrightYearDefault;
            if (copyrightYearIn != null)
                copyrightYear = copyrightYearIn;
            else
                copyrightYearIn = "null";
            format.ttl(CopyrightYearKey, ": ", copyrightYear, " (", copyrightYearIn, ")");
            System.setProperty(CopyrightYearKey, copyrightYear);
            
            String copyrightOwnerIn = properties.getProperty(CopyrightOwnerKey);
            String copyrightOwner = CopyrightOwnerDefault;
            if (copyrightOwnerIn != null)
                copyrightOwner = copyrightOwnerIn;
            else
                copyrightOwnerIn = "null";
            format.ttl(CopyrightOwnerKey, ": ", copyrightOwner, " (", copyrightOwnerIn, ")");
            System.setProperty(CopyrightOwnerKey, copyrightOwner);

            String oauthImplClass = properties.getProperty(OauthImplClassKey, getOauthImplClassDefault());
            msg = "Invalid OAuth implementation class, " + OauthImplClassKey + " = " + oauthImplClass; 
            Class<?> clazz = Class.forName(oauthImplClass);
            OauthUserAuthenticator authenticator = (OauthUserAuthenticator) clazz.newInstance();
            authenticator.init(properties);
            
            Class<? extends WebPage> cbclass = authenticator.getOAuthCallbackClass();
            mountPage(authenticator.getOAuthCallbackMount(), cbclass);

            String coreImplClass = properties.getProperty(CoreImplClassKey, getCoreImplClassDefault());
            msg = "Invalid CoreController implementation class, " + CoreImplClassKey + " = " + coreImplClass;
            clazz = Class.forName(coreImplClass);
            coreController = (CoreController) clazz.newInstance();
            msg = "CoreController.init failed";
            coreController.init(properties, format);

            getServletContext().setAttribute(WicketPropertiesKey, properties);
//            properties.setProperty(ContextRealBaseKey, contextPath);
                        
            properties.remove(ServletConfigKey);
            properties.remove(LogFilePathKey);
            properties.remove(LogFileBaseKey);
            properties.remove(GoogleCSEcxKey);
            properties.remove(CopyrightYearKey);
            properties.remove(CopyrightOwnerKey);
            properties.remove(OauthImplClassKey);
            properties.remove(CoreImplClassKey);
            
            format.ttl("Other properties");
            format.inc();
            for (Entry<Object, Object> entry : properties.entrySet())
                format.ttl(entry.getKey().toString(), " = ", entry.getValue().toString());
            format.dec();
        } catch (Exception e)
        {
            format.ttl("\n\n" + msg);
            log.error(format.toString().toString(), e);
            throw new RuntimeException(msg, e);
        }
        init(format); // call log.info(sb.toString()) in this implementation
	}

    @Override
    protected void onDestroy()
    {
        if(coreController != null)
        {
            try
            {
                coreController.destroy();
            }catch(Exception e)
            {
                log.warn("CoreController.destroy failed to cleanup", e);
            }
        }
    }
}
