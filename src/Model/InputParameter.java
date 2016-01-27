/*
 * The MIT License
 *
 * Copyright 2016 Liu.
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
package Model;

import javafx.beans.binding.StringExpression;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 *
 * @author Liu
 */
@XmlRootElement(name = StaticStrings.InputParameter)
public class InputParameter implements IInputParameter, IGetBindExpressions
{
    @XmlAttribute(name = StaticStrings.Name)
    @XmlJavaTypeAdapter(StringPropertyAdapter.class)
    private final StringProperty _Name;
    @XmlAttribute(name = StaticStrings.Type)
    @XmlJavaTypeAdapter(StringPropertyAdapter.class)
    private final StringProperty _Type;
    public InputParameter()
    {
        this("", "");
    }
    public InputParameter(String type, String name)
    {
        _Name = ModelFactory.GetStringPropertyWithValue(name);
        _Type = ModelFactory.GetStringPropertyWithValue(type);
    }

    @Override
    public StringProperty GetName()
    {
        return _Name;
    }

    @Override
    public StringProperty GetType()
    {
        return _Type;
    }

    @Override
    public String toString()
    {
        return "type = " + _Type.getValue() +  " name = " + _Name.getValue();
    }

    @Override
    public StringExpression[] GetExpressions()
    {
        return new StringExpression[]{this.GetType(), new SimpleStringProperty(" "), this.GetName()};
    }
    
    
    
}
