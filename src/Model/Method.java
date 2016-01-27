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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 *
 * @author Liu
 */
@XmlRootElement(name = StaticStrings.Method)
public class Method implements IMethod, IGetBindExpressions
{

    @XmlElement(name = StaticStrings.OutputType)
    @XmlJavaTypeAdapter(StringPropertyAdapter.class)
    private final StringProperty _OutputType;
    @XmlElement(name = StaticStrings.Visibility)
    @XmlJavaTypeAdapter(StringPropertyAdapter.class)
    private final StringProperty _Visibility;
    @XmlElement(name = StaticStrings.Name)
    @XmlJavaTypeAdapter(StringPropertyAdapter.class)
    private final StringProperty _Name;
    @XmlElement(name = StaticStrings.IsFinal)
    @XmlJavaTypeAdapter(BooleanPropertyAdapter.class)
    private final BooleanProperty _IsFinal;
    @XmlElement(name = StaticStrings.IsStatic)
    @XmlJavaTypeAdapter(BooleanPropertyAdapter.class)
    private final BooleanProperty _IsStatic;
    @XmlElement(name = StaticStrings.IsThrowExceptions)
    @XmlJavaTypeAdapter(BooleanPropertyAdapter.class)
    private final BooleanProperty _IsThrowExceptions;
    @XmlElementWrapper(name = StaticStrings.InputParameters)
    @XmlElement(name = StaticStrings.InputParameter)
    private final List<InputParameter> _InputParameters;
    @XmlElementWrapper(name = StaticStrings.Annotations)
    @XmlElement(name = StaticStrings.Annotation)
    @XmlJavaTypeAdapter(StringPropertyAdapter.class)
    private final List<StringProperty> _Annotations;
    @XmlElementWrapper(name = StaticStrings.MethodLines)
    @XmlElement(name = StaticStrings.MethodLine)
    private final List<MethodLine> _MethodLines;

    public Method(String outputType, String methodName, String visibility, boolean isFinal, boolean isStatic,
            boolean isThrowExceptions, List<InputParameter> inputParameters, List<String> annotations, List<MethodLine> methodLines)
    {
        _OutputType = ModelFactory.GetStringPropertyWithValue(outputType);
        _Name = ModelFactory.GetStringPropertyWithValue(methodName);
        _Visibility = ModelFactory.GetStringPropertyWithValue(visibility);
        _IsFinal = ModelFactory.GetBooleanPropertyWithValue(isFinal);
        _IsStatic = ModelFactory.GetBooleanPropertyWithValue(isStatic);
        _IsThrowExceptions = ModelFactory.GetBooleanPropertyWithValue(isThrowExceptions);
        _InputParameters = inputParameters;
        _Annotations = ModelFactory.GetStringPropertyList(annotations);
        _MethodLines = methodLines;
    }

    public Method()
    {
        this("", "", "", false, false, false, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    }

    @Override
    public StringProperty GetOutputType()
    {
        return _OutputType;
    }

    @Override
    public List<InputParameter> GetInputParameters()
    {
        return _InputParameters;
    }

    @Override
    public BooleanProperty IsThrowExceptions()
    {
        return _IsThrowExceptions;
    }

    @Override
    public List<MethodLine> GetMethodBody()
    {
        return _MethodLines;
    }

    @Override
    public StringProperty GetName()
    {
        return _Name;
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
    public StringExpression[] GetExpressions()
    {
        StringExpression[] searray = new StringExpression[_InputParameters.size() * 3 + 7];
        searray[0] = this.GetVisibility();
        searray[1] =  new SimpleStringProperty(" ");
        searray[2] =    this.GetOutputType();
        searray[3] =    new SimpleStringProperty(" ");
        searray[4] =    this.GetName();
        searray[5] = new SimpleStringProperty("(");
        searray[searray.length - 1] = new SimpleStringProperty(")");
        int count = 6;
        for (int index = 0; index < _InputParameters.size(); index++)
        {
            StringExpression[] se = _InputParameters.get(index).GetExpressions();
            searray[count] = se[0]; count++;
            searray[count] = se[1]; count++;
            searray[count] = se[2]; count++;
        }

        return searray;
    }

}
