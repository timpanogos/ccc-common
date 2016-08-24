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
package com.ccc.tools.app.status;




/**
 * A configurable application status tracking interface.
 * </p>
 * This interface extends the <code>StatusTracker</code> interface with an init method
 * which takes a custom configuration object.  The configuration object is typically 
 * declared and typed by the <code>Service <code>T</code> implementation. 
 * @param <T> the custom <code>Service</code> configuration implementation object .
 * @see Service
 */
public interface CustomStatusTracker<T> extends StatusTracker
{
    /**
     * Initialize the status tracker with the given custom configuration object. 
     * @param config the custom configuration object.  Can be declared <code>&ltVoid&gt</code>.
     */
    public void init(T config);
}
