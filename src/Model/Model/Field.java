/*
 * The MIT License
 *
 * Copyright 2015 Liu.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package Model.Model;

import Model.interfaces.IField;
import Model.interfaces.IFullName;
import Model.interfaces.IFunctionInput;
import Model.interfaces.IModifiers;
import Model.interfaces.IName;
import Model.interfaces.IVisibility;

/**
 *
 * @author Liu
 */
public class Field implements IField
{
    private final IModifiers _Modifiers;
    private final IVisibility _Visibility;
    private final IFunctionInput _DefaultValue;
    private final IName _Name;
    private final IFullName _Type;
    
    public Field(IName name, IFullName type, IModifiers modifiers, IVisibility visibility, IFunctionInput defaultValue)
    {
        _Modifiers = modifiers;
        _Visibility = visibility;
        _DefaultValue = defaultValue;
        _Name = name;
        _Type = type;
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append(_Visibility.GetVisibility().getValue()).append(" ");
        builder.append(_Modifiers.toString()).append(" ");
        builder.append(_Name.GetName().getValue()).append(" ");
        System.out.println(_DefaultValue.GetFunction().GetFunction().getValue().isEmpty());
      
        if(_DefaultValue != null
          
           && _DefaultValue.GetFunction().GetFunction().getValue().isEmpty() == false)
        {
            builder.append("= ");
            builder.append(_DefaultValue.GetFunction().GetFunction().getValue());
        }
        return builder.toString();
    }
    
    

    @Override
    public IFunctionInput GetDefaultValue()
    {
        return _DefaultValue;
    }

    @Override
    public IModifiers GetModifiers()
    {
        return _Modifiers;
    }

    @Override
    public IVisibility GetVisibility()
    {
        return _Visibility;
    }

    @Override
    public IFullName GetType()
    {
        return _Type;
    }

    @Override
    public IName GetName()
    {
        return _Name;
    }
    
    
    
}
