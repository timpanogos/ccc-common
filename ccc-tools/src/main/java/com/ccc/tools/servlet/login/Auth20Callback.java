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
package com.ccc.tools.servlet.login;

import java.util.Timer;
import java.util.TimerTask;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.LoggerFactory;

import com.ccc.tools.servlet.UserAuthenticationHandler;
import com.ccc.tools.servlet.clientInfo.Base20ClientInfo;
import com.ccc.tools.servlet.clientInfo.SessionClientInfo;
import com.github.scribejava.core.model.OAuth2AccessToken;

@SuppressWarnings("javadoc")
public class Auth20Callback extends WebPage
{
    private static final long serialVersionUID = 1941601956616781465L;

    // TODO: centralize with a threadpool or somesuch ... no cleanup here right
    // now
    // what about multiple users? how to scale the refresh?
    // also need to cancel it in logout
    private Timer timer = new Timer();

    /**
     * This method must be overridden.
     * 
     * @param parameters
     *            the request query parameters
     * @throws Exception
     */
    public Auth20Callback(PageParameters parameters) throws Exception
    {
    }

    protected void handleCallback(PageParameters parameters, SessionClientInfo sessionClientInfo) throws Exception
    {
        Base20ClientInfo clientInfo = (Base20ClientInfo) sessionClientInfo.getOauthClientInfo();
        UserAuthenticationHandler handler = UserAuthenticationHandler.getInstance();
        clientInfo.setCode(parameters.get("code").toString());
        clientInfo.setState(parameters.get("state").toString());
        clientInfo.validateState();
        clientInfo.setAccessToken(handler.getAccessToken(clientInfo.getCode()));
        sessionClientInfo.setAuthenticated(true);
        long expiresIn = ((OAuth2AccessToken) clientInfo.getAccessToken()).getExpiresIn();
        expiresIn *= 1000;
        expiresIn -= 30 * 1000; // give it 30 seconds pre-expire
        timer.scheduleAtFixedRate(new RefreshTask(handler, clientInfo), expiresIn, expiresIn);
    }
    
    private class RefreshTask extends TimerTask
    {
        private final UserAuthenticationHandler handler;
        private final Base20ClientInfo clientInfo;

        private RefreshTask(UserAuthenticationHandler handler, Base20ClientInfo clientInfo)
        {
            this.handler = handler;
            this.clientInfo = clientInfo;
        }

        @Override
        public void run()
        {
            try
            {
                clientInfo.setAccessToken(handler.refreshAccessToken(((OAuth2AccessToken) clientInfo.getAccessToken()).getRefreshToken()));
            } catch (Exception e)
            {
                timer.cancel();
                LoggerFactory.getLogger(getClass()).error("Refresh Access Token failed", e);
            }
        }
    }
}
