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
package com.ccc.oauth.login;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.LoggerFactory;

import com.ccc.oauth.CoreController;
import com.ccc.oauth.UserAuthenticationHandler;
import com.ccc.oauth.clientInfo.Base20ClientInfo;
import com.ccc.oauth.clientInfo.SessionClientInfo;
import com.ccc.oauth.events.AuthEventListener;
import com.github.scribejava.core.model.OAuth2AccessToken;

@SuppressWarnings("javadoc")
public class Auth20Callback
{

    // TODO: centralize with a threadpool or somesuch ... no cleanup here right
    // now
    // what about multiple users? how to scale the refresh?
    // also need to cancel it in logout
    private Timer timer = new Timer();
    private long expiresIn;

    public Auth20Callback() throws Exception
    {
    }

    public void handleCallback(String code, String state, SessionClientInfo sessionClientInfo) throws Exception
    {
        Base20ClientInfo clientInfo = (Base20ClientInfo) sessionClientInfo.getOauthClientInfo();
        UserAuthenticationHandler handler = UserAuthenticationHandler.getInstance();
        clientInfo.setCode(code);
        clientInfo.setState(state);
        clientInfo.validateState();
        clientInfo.setAccessToken(handler.getAccessToken(clientInfo.getCode()));
        sessionClientInfo.setAuthenticated(true);
        expiresIn = ((OAuth2AccessToken) clientInfo.getAccessToken()).getExpiresIn();
        expiresIn *= 1000;
        expiresIn -= 60 * 1000; // give it 60 seconds pre-expire
        timer.schedule(new RefreshTask(timer, handler, clientInfo), expiresIn);
    }
    
    private class RefreshTask extends TimerTask
    {
        private final Timer timer;
        private final AtomicBoolean inRetry;
        private final UserAuthenticationHandler handler;
        private final Base20ClientInfo clientInfo;
        

        private RefreshTask(Timer timer, UserAuthenticationHandler handler, Base20ClientInfo clientInfo)
        {
            this.timer = timer;
            inRetry = new AtomicBoolean();
            this.handler = handler;
            this.clientInfo = clientInfo;
        }

        @Override
        public void run()
        {
            try
            {
LoggerFactory.getLogger(getClass()).info("refresh accessToken attempt");                
                clientInfo.setAccessToken(handler.refreshAccessToken(((OAuth2AccessToken) clientInfo.getAccessToken()).getRefreshToken()));
                timer.schedule(new RefreshTask(timer, handler, clientInfo), expiresIn);
                CoreController.getController().fireAuthenticatedEvent(clientInfo, AuthEventListener.Type.Refreshed);
LoggerFactory.getLogger(getClass()).info("refresh accessToken success");                
            } catch (Exception e)
            {
                timer.schedule(new RefreshTask(timer, handler, clientInfo), 1000 * 60);
                LoggerFactory.getLogger(getClass()).error("Refresh Access Token failed, retry in 1 minute", e);
            }
        }
    }
}
