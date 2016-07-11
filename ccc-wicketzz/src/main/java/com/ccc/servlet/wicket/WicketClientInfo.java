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

import org.apache.wicket.protocol.http.request.WebClientInfo;
import org.apache.wicket.request.cycle.RequestCycle;

import com.ccc.oauth.clientInfo.BaseClientInfo;
import com.ccc.oauth.clientInfo.SessionClientInfo;

@SuppressWarnings("javadoc")
public class WicketClientInfo extends WebClientInfo implements SessionClientInfo
{
    private static final long serialVersionUID = 2619172563751052839L;
    
    private boolean authenticated;
    private BaseClientInfo oauthClientInfo;
    
    public WicketClientInfo(RequestCycle requestCycle)
    {
        super(requestCycle);
    }

    public synchronized void setAuthenticated(boolean value)
    {
        authenticated = value;
    }
    
    public synchronized boolean isAuthenticated()
    {
        return authenticated;
    }

    public synchronized BaseClientInfo getOauthClientInfo()
    {
        return oauthClientInfo;
    }

    public synchronized void setOauthClientInfo(BaseClientInfo oauthClientInfo)
    {
        this.oauthClientInfo = oauthClientInfo;
    }
}
