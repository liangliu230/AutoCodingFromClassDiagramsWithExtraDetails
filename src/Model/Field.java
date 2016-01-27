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

import java.util.ArrayList;
import java.util.List;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 *
 * @author Liu
 */
@XmlRootElement(name = StaticStrings.Field)
public class Field implements IField, IGetBindExpressions
{
    @XmlElement(name = StaticStrings.Name)
    @XmlJavaTypeAdapter(StringPropertyAdapter.class)
    private final StringProperty _Name;
    @XmlElement(name = StaticStrings.Type)
    @XmlJavaTypeAdapter(StringPropertyAdapter.class)
    private final StringProperty _Type;
    @XmlElement(name = StaticStrings.DefaultValue)
    @XmlJavaTypeAdapter(StringPropertyAdapter.class)
    private final StringProperty _DefaultValue;
    @XmlElementWrapper(name = StaticStrings.Annotations)
    @XmlElement(name = StaticStrings.Annotation)
    @XmlJavaTypeAdapter(StringPropertyAdapter.class)
    private final List<StringProperty> _Annotations;
    @XmlAttribute(name = StaticStrings.Visibility)
    @XmlJavaTypeAdapter(StringPropertyAdapter.class)
    private final StringProperty _Visibility;
    @XmlAttribute(name = StaticStrings.IsStatic)
    @XmlJavaTypeAdapter(BooleanPropertyAdapter.class)
    private final BooleanProperty _IsStatic;
    @XmlAttribute(name = StaticStrings.IsFinal)
    @XmlJavaTypeAdapter(BooleanPropertyAdapter.class)
    private final BooleanProperty _IsFinal;

    public Field(String name, String type, String defaultValue, List<String> annotations, String visibility, boolean isStatic, boolean isFinal)
    {
        _Name = ModelFactory.GetStringPropertyWithValue(name);
        _Type = ModelFactory.GetStringPropertyWithValue(type);
        _DefaultValue = ModelFactory.GetStringPropertyWithValue(defaultValue);
        _Annotations = ModelFactory.GetStringPropertyList(annotations);
        _Visibility = ModelFactory.GetStringPropertyWithValue(visibility);
        _IsStatic = ModelFactory.GetBooleanPropertyWithValue(isStatic);
        _IsFinal = ModelFactory.GetBooleanPropertyWithValue(isFinal);
    }
    
    public Field()
    {
        this("", "", "", new ArrayList<>(), "", false, false);
    }

    @Override
    public StringProperty GetDefaultValue()
    {
        return _DefaultValue;
    }

    @Override
    public StringProperty GetVisibility()
    {
        return _Visibility;
    }

    @Override
    public BooleanProperty isFinal()
    {
        return _IsFinal;
    }

    @Override
    public BooleanProperty isStatic()
    {
        return _IsStatic;
    }

    @Override
    public List<StringProperty> GetAnnotations()
    {
        return _Annotations;
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
        StringBuilder builder = new StringBuilder();
        builder.append(_Visibility.getValue()).append(" ");
        if(_IsFinal.getValue())
            builder.append("final").append(" ");
        if(_IsStatic.getValue())
            builder.append("static ");
        builder.append(_Type.getValue()).append(" ");
        builder.append(_Name.getValue()).append(" ");
        builder.append(" = ").append(_DefaultValue.getValue());
        builder.append("\n");
        builder.append("annotations:\n");
        for(StringProperty str : _Annotations)
            builder.append(str).append("\n");
        builder.append("\n");
        return builder.toString();
    }

    @Override
    public StringExpression[] GetExpressions()
    {
        return new StringExpression[]{
                                        this.GetVisibility(), 
                                        new SimpleStringProperty(" "), 
                                        this.GetType(), 
                                        new SimpleStringProperty(" "),
                                        this.GetName()
                                    };
    }
    
    
}
