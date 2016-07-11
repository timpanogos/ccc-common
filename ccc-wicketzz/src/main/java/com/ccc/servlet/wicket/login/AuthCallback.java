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
package com.ccc.servlet.wicket.login;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.ccc.oauth.clientInfo.SessionClientInfo;
import com.ccc.oauth.login.Auth20Callback;

@SuppressWarnings("javadoc")
public abstract class AuthCallback extends WebPage
{
    private static final long serialVersionUID = 7397831975135168252L;

    @SuppressWarnings("unused")
    public AuthCallback(PageParameters parameters) throws Exception
    {
    }

    protected void handleCallback(PageParameters parameters, SessionClientInfo sessionClientInfo) throws Exception
    {
        Auth20Callback callback = new Auth20Callback();
        callback.handleCallback(parameters.get("code").toString(), parameters.get("state").toString(), sessionClientInfo);
    }
}
