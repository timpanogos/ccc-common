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

import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.model.OAuthConfig;

@SuppressWarnings("javadoc")
public class ScribeApi20Impl extends DefaultApi20 {
	private String loginUrl = null;
	private String tokenUrl = null;

	public ScribeApi20Impl(String loginUrl, String tokenUrl) {
		this.loginUrl = loginUrl;
		this.tokenUrl = tokenUrl;
	}

	@Override
	public String getAccessTokenEndpoint() {
		if (tokenUrl == null || tokenUrl.length() == 0)
			throw new RuntimeException("serverBaseUrl is not properly initialized");
		return tokenUrl;
	}

	@Override
	public String getAuthorizationUrl(OAuthConfig config)
	{
		if (loginUrl == null || loginUrl.length() == 0)
			throw new RuntimeException("loginBaseUrl is not properly initialized");
		return loginUrl;
	}
}