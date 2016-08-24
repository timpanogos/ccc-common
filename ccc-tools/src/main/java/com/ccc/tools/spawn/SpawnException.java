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
package com.ccc.tools.spawn;

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