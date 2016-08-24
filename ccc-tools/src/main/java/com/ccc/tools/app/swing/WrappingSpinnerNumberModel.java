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
package com.ccc.tools.app.swing;

import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

@SuppressWarnings("javadoc")
public class WrappingSpinnerNumberModel extends SpinnerNumberModel 
{
    private static final long serialVersionUID = 5176701528390057370L;
    private final int first;
    private final int last;
    private SpinnerModel linkedModel = null;

    public WrappingSpinnerNumberModel(int value, int min, int max, int step) 
    {
        super(value, min, max, step);
        first = min;
        last = max;
    }

    public void setLinkedModel(SpinnerModel linkedModel) 
    {
        this.linkedModel = linkedModel;
    }

    @Override
    public Object getNextValue() 
    {
        Object value = super.getNextValue();
        if (value == null)
        {
            value = new Integer(first);
            if (linkedModel != null) 
                linkedModel.setValue(linkedModel.getNextValue());
        }
        return value;
    }

    @Override
    public Object getPreviousValue() 
    {
        Object value = super.getPreviousValue();
        if (value == null) 
        {
            value = new Integer(last);
            if (linkedModel != null) 
                linkedModel.setValue(linkedModel.getPreviousValue());
        }
        return value;
    }
}
