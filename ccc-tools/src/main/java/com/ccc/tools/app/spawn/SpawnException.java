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

package com.ccc.tools.app.spawn;

@SuppressWarnings("javadoc")
public class SpawnException extends Exception
{
    private static final long serialVersionUID = 8757489065842361569L;
    private final int ccode;
	private final String inputStream;
	private final String outputStream;
	private final String errorStream;

	public SpawnException(String message, int ccode, String inputStream, String outputStream, String errorStream)
	{
		this(message, ccode, inputStream, outputStream, errorStream, null);
	}
	
	public SpawnException(String message, int ccode, String inputStream, String outputStream, String errorStream, Throwable rootCause )
	{
		super(message, rootCause);
		this.ccode = ccode;
		this.inputStream = inputStream;
		this.outputStream = outputStream;
		this.errorStream = errorStream;
	}

	public String getInputStream()
	{
		return inputStream;
	}

	public String getOutputStream()
	{
		return outputStream;
	}

	public String getErrorStream()
	{
		return errorStream;
	}

	public int getCompletionCode()
	{
		return (ccode);
	}
}