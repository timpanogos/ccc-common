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
package com.ccc.oauth;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuth1RequestToken;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthConfig;
import com.github.scribejava.core.oauth.OAuth10aService;
import com.github.scribejava.core.oauth.OAuth20Service;
import com.github.scribejava.core.oauth.OAuthService;

@SuppressWarnings("javadoc")
public class UserAuthenticationHandler
{
    private static final String ResponseType = "response_type";
    private static final String ResponseTypeDefault = "code";
    private static final String RedirectUri = "redirect_uri";
    private static final String ClientId = "client_id";
    private static final String Scope = "scope";
    private static final String State = "state";
    
    private static UserAuthenticationHandler instance;

    public static UserAuthenticationHandler getInstance()
    {
        if (instance == null)
            throw new IllegalStateException("UserAuthenticationHandler instance not set");
        return instance;
    }

    public static void createInstance(OauthUserAuthenticator oauthUserAuthenticator)
    {
        if (instance != null)
            return;
        instance = new UserAuthenticationHandler(oauthUserAuthenticator);
    }

    private boolean isOauth10a = false;
    private OAuth10aService oauth10aService;
    private OAuth20Service oauth20Service;

    private UserAuthenticationHandler(OauthUserAuthenticator oauthUserAuthenticator)
    {
        OAuthService oauthService = oauthUserAuthenticator.getOAuthService();
        if (oauthService instanceof OAuth10aService)
        {
            this.oauth10aService = (OAuth10aService) oauthService;
            this.isOauth10a = true;
        }else
        {
            this.oauth20Service = (OAuth20Service) oauthService;
            this.isOauth10a = false;
        }
    }

    public AuthenticateUserData authenticateUser() throws Exception
    {
        if (isOauth10a)
        {
            OAuth1RequestToken requestToken = oauth10aService.getRequestToken();
            //@formatter:off
		    return new AuthenticateUserData(
		            true,
                    requestToken,
                    oauth10aService.getAuthorizationUrl(requestToken),
                    oauth10aService.getConfig());
            //@formatter:on
        }
        
        OAuthConfig config = oauth20Service.getConfig(); 
        Map<String, String> eveParams = new HashMap<>();
        eveParams.put(ResponseType, ResponseTypeDefault);
        eveParams.put(RedirectUri, config.getCallback());
        eveParams.put(ClientId, config.getApiKey());
        eveParams.put(Scope, config.getScope());
        eveParams.put(State, config.getState());
        
        //@formatter:off
        return new AuthenticateUserData(
                false,
                null,
                oauth20Service.getAuthorizationUrl(eveParams),
                oauth20Service.getConfig());
        //@formatter:on
    }

    public OAuth1AccessToken getAccessToken(OAuth1RequestToken requestToken, String oauthVerifier) throws IOException
    {
        if (isOauth10a)
            return oauth10aService.getAccessToken(requestToken, oauthVerifier);
        throw new IllegalArgumentException("This method only supports OAuth10aService, try the other getAccessToken method.");
    }

    public OAuth2AccessToken getAccessToken(String code) throws IOException
    {
        if (isOauth10a)
            throw new IllegalArgumentException("This method only supports OAuth20Service, try the other getAccessToken method.");
        return oauth20Service.getAccessToken(code);
    }

    public OAuth2AccessToken refreshAccessToken(String refresh) throws IOException
    {
        if (isOauth10a)
            throw new IllegalArgumentException("This method only supports OAuth20Service, try the other getAccessToken method.");
        return oauth20Service.refreshAccessToken(refresh);
    }

    public static class AuthenticateUserData
    {
        public final boolean oauth10a;
        public final OAuth1RequestToken requestToken;
        public final String loginUrl;
        public final OAuthConfig oauthConfig;

        public AuthenticateUserData(boolean oauth10a, OAuth1RequestToken requestToken, String loginUrl, OAuthConfig oauthConfig)
        {
            this.oauth10a = oauth10a;
            this.requestToken = requestToken;
            this.loginUrl = loginUrl;
            this.oauthConfig = oauthConfig;
        }
    }

}
