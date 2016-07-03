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
