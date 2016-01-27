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
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
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
@XmlRootElement(name = StaticStrings.Class)
public class ACClass implements IClass
{
    @XmlElement(name = StaticStrings.Name)
    @XmlJavaTypeAdapter(StringPropertyAdapter.class)
    private final StringProperty _Name;
    @XmlAttribute(name = StaticStrings.Visibility)
    @XmlJavaTypeAdapter(StringPropertyAdapter.class)
    private final StringProperty _Visibility;
    @XmlAttribute(name = StaticStrings.IsFinal)
    @XmlJavaTypeAdapter(BooleanPropertyAdapter.class)
    private final BooleanProperty _IsFinal;
    @XmlAttribute(name = StaticStrings.IsAbstract)
    @XmlJavaTypeAdapter(BooleanPropertyAdapter.class)
    private final BooleanProperty _IsAbstract;
    @XmlElementWrapper(name = StaticStrings.Extends)
    @XmlElement(name = StaticStrings.Name)
    @XmlJavaTypeAdapter(StringPropertyAdapter.class)
    private final List<StringProperty> _Extends;
    @XmlElementWrapper(name = StaticStrings.Implements)
    @XmlElement(name = StaticStrings.Name)
    @XmlJavaTypeAdapter(StringPropertyAdapter.class)
    private final List<StringProperty> _Implements;
    @XmlElementWrapper(name = StaticStrings.Fields)
    @XmlElement(name = StaticStrings.Field)
    private final List<Field> _Fields;
    @XmlElementWrapper(name = StaticStrings.Methods)
    @XmlElement(name = StaticStrings.Method)
    private final List<Method> _Methods;
    @XmlElementWrapper(name = StaticStrings.Annotations)
    @XmlElement(name = StaticStrings.Annotation)
    @XmlJavaTypeAdapter(StringPropertyAdapter.class)
    private final List<StringProperty> _Annotations;
    @XmlAttribute(name = StaticStrings.IsEnum)
    @XmlJavaTypeAdapter(BooleanPropertyAdapter.class)
    private final BooleanProperty _IsEnum;
    @XmlAttribute(name = StaticStrings.IsInterface)
    @XmlJavaTypeAdapter(BooleanPropertyAdapter.class)
    private final BooleanProperty _IsInterface;
    @XmlElement(name = StaticStrings.X)
    @XmlJavaTypeAdapter(DoublePropertyAdapter.class)
    private final DoubleProperty _LayoutX;
    @XmlElement(name = StaticStrings.Y)
    @XmlJavaTypeAdapter(DoublePropertyAdapter.class)
    private final DoubleProperty _LayoutY;
    public ACClass()
    {
        this("", "", false, false, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(),
                new ArrayList<>(), false, false, 0, 0);
    }
    public ACClass(String name, String visibility, boolean isFinal, boolean isAbstract, 
                    List<String> _extends, List<String> _implements, List<Field> fields,
                    List<Method> methods, List<String> annotations, boolean  isEnum,
                    boolean  isInterface, double layoutX, double layoutY)
    {
        _Name = ModelFactory.GetStringPropertyWithValue(name);
        _Visibility = ModelFactory.GetStringPropertyWithValue(visibility);
        _IsFinal = ModelFactory.GetBooleanPropertyWithValue(isFinal);
        _IsAbstract = ModelFactory.GetBooleanPropertyWithValue(isAbstract);
        _Extends = ModelFactory.GetStringPropertyList(_extends);
        _Implements = ModelFactory.GetStringPropertyList(_implements);
        _Fields = fields;
        _Methods = methods;
        _Annotations = ModelFactory.GetStringPropertyList(annotations);
        _IsEnum = ModelFactory.GetBooleanPropertyWithValue(isEnum);
        _IsInterface = ModelFactory.GetBooleanPropertyWithValue(isInterface);
        _LayoutX = ModelFactory.GetDoublePropertyWithValue(layoutX);
        _LayoutY = ModelFactory.GetDoublePropertyWithValue(layoutY);
    }

    @Override
    public List<StringProperty> GetExtends()
    {
        return _Extends;
    }

    @Override
    public List<StringProperty> GetImplements()
    {
        return _Implements;
    }

    @Override
    public List<Field> GetFields()
    {
        return _Fields;
    }

    @Override
    public List<Method> GetMethods()
    {
        return _Methods;
    }

    @Override
    public StringProperty GetName()
    {
        return _Name;
    }

    @Override
    public List<StringProperty> GetAnnotations()
    {
        return _Annotations;
    }

    @Override
    public StringProperty GetVisibility()
    {
        return _Visibility;
    }

    @Override
    public BooleanProperty isAbstract()
    {
        return _IsAbstract;
    }

    @Override
    public BooleanProperty isFinal()
    {
        return _IsFinal;
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();

        if (_IsEnum.getValue())
        {
            builder.append("enum");
        } else if (_IsInterface.getValue())
        {
            builder.append("interface");
        } else
        {
            builder.append("class");
        }
        builder.append(" ");
        builder.append(_Name.getValue()).append("\n");

        if (_IsAbstract.getValue())
        {
            builder.append("abstract ");
        }
        if (_IsFinal.getValue())
        {
            builder.append("final ");
        }

        return builder.toString();
    }

    @Override
    public DoubleProperty GetLayoutX()
    {
        return _LayoutX;
    }

    @Override
    public DoubleProperty GetLayoutY()
    {
        return _LayoutY;
    }

    @Override
    public BooleanProperty IsEnum()
    {
        return _IsEnum;
    }

    @Override
    public BooleanProperty IsInterface()
    {
        return _IsInterface;
    }
}
