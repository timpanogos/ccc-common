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
package com.ccc.servlet.wicket;

import org.apache.wicket.markup.html.WebPage;

import com.ccc.oauth.OauthUserAuthenticator;

/**
 * OAuth User Authenticator Interface
 */
public interface WicketUserAuthenticator extends OauthUserAuthenticator
{
    /**
     * Get the URL that the OAuthService calls back to.
     * 
     * @return The callback URL.
     */
    public String getOAuthCallbackMount();

    /**
     * Get the Callback class.
     * 
     * @return The callback class.
     */
    public Class<? extends WebPage> getOAuthCallbackClass();
}
