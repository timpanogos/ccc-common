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

import java.util.Properties;

import com.github.scribejava.core.oauth.OAuthService;

/**
 * OAuth User Authenticator Interface
 */
public interface OauthUserAuthenticator
{
    /**
     * Initialize the OAuthUserAuthenticator with the specified properties.
     * 
     * @param properties
     *            The properties to be passed to the OAuthUserAuthenticator.
     * @throws Exception if the initialization fails
     */
    public void init(Properties properties) throws Exception;

    /**
     * Get an OAuthService object. This may be a OAuth10aService or OAuth20Service.
     * 
     * @return The OAuthService object.
     */
    public OAuthService getOAuthService();
}
