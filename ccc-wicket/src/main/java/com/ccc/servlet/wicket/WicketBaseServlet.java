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
package com.ccc.servlet.wicket;

import java.util.Calendar;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.wicket.authroles.authentication.AbstractAuthenticatedWebSession;
import org.apache.wicket.authroles.authentication.AuthenticatedWebApplication;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ccc.oauth.CoreController;
import com.ccc.oauth.clientInfo.BaseClientInfo;
import com.ccc.oauth.clientInfo.SessionClientInfo;
import com.ccc.servlet.wicket.login.SignInPage;
import com.ccc.tools.PropertiesFile;
import com.ccc.tools.StrH;
import com.ccc.tools.TabToLevel;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.util.StatusPrinter;

@SuppressWarnings("javadoc")
public abstract class WicketBaseServlet extends AuthenticatedWebApplication
{
    public static final String ServletConfigKey = "ccc.tools.servlet.config";
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
    protected volatile Properties properties;
    
    public WicketBaseServlet()
    {
        log = LoggerFactory.getLogger(getClass());
        if(log.isInfoEnabled())
        {
            LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
            // TODO: StatusPrinter.setPrintStream
            StatusPrinter.print(lc);
        }
    }
    
    public static BaseClientInfo getClientInfo()
    {
        return ((SessionClientInfo)WebSession.get().getClientInfo()).getOauthClientInfo();
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
        return SignInPage.class;
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

        properties = new Properties();
        String msg = "configuration properties file not found: " + fileName;
        try
        {
            PropertiesFile.load(properties, fileName);

            String logfile = StrH.getParameter(properties, CoreController.LogFilePathKey, getLogFilePathDefault(), format);
            System.setProperty(CoreController.LogFilePathKey, logfile);
            int idx = logfile.lastIndexOf(".");
            System.setProperty(CoreController.LogFileBaseKey, logfile.substring(0, idx));
            
            String coreImplClass = StrH.getParameter(properties, CoreImplClassKey, getCoreImplClassDefault(), format);
            msg = "Invalid CoreController implementation class, " + CoreImplClassKey + " = " + coreImplClass;
            Class<?> clazz = Class.forName(coreImplClass);
            CoreController coreController = (CoreController) clazz.newInstance();
            msg = "CoreController.init failed";
            coreController.init(properties, format);

            String google = StrH.getParameter(properties, GoogleCSEcxKey, GoogleCSEcxDefault, format);
            System.setProperty(GoogleCSEcxKey, google);
            
            String copyrightYear = StrH.getParameter(properties, CopyrightYearKey, CopyrightYearDefault, format);
            System.setProperty(CopyrightYearKey, copyrightYear);
            
            String copyrightOwner = StrH.getParameter(properties, CopyrightOwnerKey, CopyrightOwnerDefault, format);
            System.setProperty(CopyrightOwnerKey, copyrightOwner);

            String oauthImplClass = StrH.getParameter(properties, OauthImplClassKey, getOauthImplClassDefault(), format);
            msg = "Invalid OAuth implementation class, " + OauthImplClassKey + " = " + oauthImplClass; 
            clazz = Class.forName(oauthImplClass);
            WicketUserAuthenticator authenticator = (WicketUserAuthenticator) clazz.newInstance();
            authenticator.init(properties);
            
            Class<? extends WebPage> cbclass = authenticator.getOAuthCallbackClass();
            mountPage(authenticator.getOAuthCallbackMount(), cbclass);

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
        CoreController controller = CoreController.getController();
        if(controller != null)
        {
            try
            {
                controller.destroy();
            }catch(Exception e)
            {
                log.warn("CoreController.destroy failed to cleanup", e);
            }
        }
    }
}
