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

import com.ccc.tools.app.serviceUtility.PropertiesFile;

@SuppressWarnings("javadoc")
public abstract class OAuthServlet extends AuthenticatedWebApplication
{
    public static final String LogFilePathKey = "ccc.tools.log-file-path";
    public static final String LogFileBaseKey = "ccc.tools.log-file-base";
    public static final String ServletConfigKey = "ccc.tools.servlet.config";
    public static final String ContextRealBaseKey = "ccc.tools.servlet.context-base";
    public static final String WicketPropertiesKey = "ccc.tools.wicket.properties"; // used to store file properties into wicket properties
    public static final String CopyrightYearKey = "ccc.tools.servlet.copyright-year";
    public static final String CopyrightOwnerKey = "ccc.tools.servlet.copyright-owner";
    public static final String OauthImplClassKey = "ccc.tools.servlet.oauth-class";
    public static final String CoreImplClassKey = "ccc.tools.servlet.core-class";
    
    public static final String CopyrightYearDefault = Integer.toString(Calendar.getInstance().get(Calendar.YEAR));
    public static final String CopyrightOwnerDefault = "";

    public static final String  GoogleCSEcxKey = "ccc.tools.servlet.gcse.cx";
    public static final String GoogleCSEcxDefault = "";
    
    protected final Logger log;
    protected volatile String contextPath;
    protected volatile OAuthUserAuthenticator authenticator;
    protected volatile CoreController coreController;
    protected volatile Properties properties;
    
    public OAuthServlet()
    {
        log = LoggerFactory.getLogger(getClass());
    }
    
    public Properties getFileProperties()
    {
        return properties;
    }

    public BaseClientInformation getClientInformation()
    {
        return authenticator.getClientInformation();
    }

    abstract protected String getLogFilePathDefault();
    abstract protected String getOauthImplClassDefault();
    abstract protected String getCoreImplClassDefault();
    abstract protected String getServletConfigDefault();
    abstract protected void init(StringBuilder sb);
    
    abstract protected Class<? extends AbstractAuthenticatedWebSession> getWebSessionImplClass();
    abstract protected Class<? extends WebPage> getSignInPageImplClass();
    
    @Override
    public void init()
    {
        super.init();
        getDebugSettings().setAjaxDebugModeEnabled(false);
                
        contextPath = getServletContext().getRealPath("/");
        StringBuilder sb = new StringBuilder("\n" + getClass().getSimpleName() + ".init");
        //@formatter:off
        sb.append("\n\tapplicationKey: " + getApplicationKey())
            .append("\n\tcontext path: " + contextPath);
        //@formatter:on
        
        String fileNameIn = getInitParameter(ServletConfigKey);
        String fileName = getServletConfigDefault();
        if (fileNameIn != null)
            fileName = fileNameIn;
        else
            fileNameIn = "null";
        sb.append("\n\tproperties: /classes/" + fileName +"(" +fileNameIn +")");

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
            sb.append("\n\t" + GoogleCSEcxKey + ": " + google + " (" + googleIn + ")");
            System.setProperty(GoogleCSEcxKey, google);
            
            String copyrightYearIn = properties.getProperty(CopyrightYearKey);
            String copyrightYear = CopyrightYearDefault;
            if (copyrightYearIn != null)
                copyrightYear = copyrightYearIn;
            else
                copyrightYearIn = "null";
            sb.append("\n\t" + CopyrightYearKey + ": " + copyrightYear + " (" + copyrightYearIn + ")");
            System.setProperty(CopyrightYearKey, copyrightYear);
            
            String copyrightOwnerIn = properties.getProperty(CopyrightOwnerKey);
            String copyrightOwner = CopyrightOwnerDefault;
            if (copyrightOwnerIn != null)
                copyrightOwner = copyrightOwnerIn;
            else
                copyrightOwnerIn = "null";
            sb.append("\n\t" + CopyrightOwnerKey + ": " + copyrightOwner + " (" + copyrightOwnerIn + ")");
            System.setProperty(CopyrightOwnerKey, copyrightOwner);
            
            properties.setProperty(ContextRealBaseKey, contextPath);
                        
            for (Entry<Object, Object> entry : properties.entrySet())
            {
                String key = entry.getKey().toString();
                //@formatter:off
                if(ServletConfigKey.equals(key) ||   
                   LogFilePathKey.equals(key) ||
                   LogFileBaseKey.equals(key) ||
                   CopyrightYearKey.equals(key) ||
                   CopyrightOwnerKey.equals(key) ||
                   OauthImplClassKey.equals(key) ||
                   CoreImplClassKey.equals(key) ||
                   GoogleCSEcxKey.equals(key))
                        continue;
                //@formatter:on
                sb.append("\n\t\t" + key + "=" + entry.getValue().toString());
            }
            
            msg = "core engine init failed";
        } catch (Exception e)
        {
            sb.append("\n\n" + msg);
            log.error(sb.toString(), e);
            throw new RuntimeException(sb.toString(), e);
        }

        String oauthImplClass = properties.getProperty(OauthImplClassKey, getOauthImplClassDefault());
        try
        {
            Class<?> clazz = Class.forName(oauthImplClass);
            authenticator = (OAuthUserAuthenticator) clazz.newInstance();
        } catch (Exception e)
        {
            throw new RuntimeException("Invalid OAuth implementation class, " + OauthImplClassKey + " = " + oauthImplClass);
        }
        authenticator.init(properties);
        
        Class<? extends WebPage> cbclass = authenticator.getOAuthCallbackClass();
        mountPage(authenticator.getOAuthCallbackMount(), cbclass);
        
        getServletContext().setAttribute(WicketPropertiesKey, properties);

        String coreImplClass = properties.getProperty(CoreImplClassKey, getCoreImplClassDefault());
        try
        {
            Class<?> clazz = Class.forName(coreImplClass);
            coreController = (CoreController) clazz.newInstance();
        } catch (Exception e)
        {
            throw new RuntimeException("Invalid CoreController implementation class, " + CoreImplClassKey + " = " + coreImplClass);
        }
        try
        {
            coreController.init(properties);
        } catch (Exception e)
        {
            throw new RuntimeException("CoreController.init failed", e);
        }
        init(sb); // call log.info(sb.toString()) in this implementation
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
    
    @Override
    protected Class<? extends AbstractAuthenticatedWebSession> getWebSessionClass()
    {
        return getWebSessionImplClass();
    }

    @Override
    protected Class<? extends WebPage> getSignInPageClass()
    {
        return getSignInPageImplClass();
    }
}
