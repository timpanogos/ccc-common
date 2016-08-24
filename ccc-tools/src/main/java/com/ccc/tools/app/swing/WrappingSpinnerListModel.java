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

import javax.swing.SpinnerListModel;
import javax.swing.SpinnerModel;

@SuppressWarnings("javadoc")
public class WrappingSpinnerListModel extends SpinnerListModel 
{
    private static final long serialVersionUID = -2910740320570355786L;
    private final Object first;
    private final Object last;
    private SpinnerModel linkedModel = null;

    public WrappingSpinnerListModel(Object[] values) 
    {
        super(values);
        first = values[0];
        last = values[values.length - 1];
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
            value = first;
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
            value = last;
            if (linkedModel != null) 
                linkedModel.setValue(linkedModel.getPreviousValue());
        }
        return value;
    }
}
