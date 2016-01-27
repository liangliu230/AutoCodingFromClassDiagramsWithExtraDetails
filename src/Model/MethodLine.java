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
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 *
 * @author Liu
 */
@XmlRootElement(name = StaticStrings.MethodLine)
public class MethodLine implements IMethodLine, IGetBindExpressions
{
    @XmlElement(name = StaticStrings.X)
    @XmlJavaTypeAdapter(DoublePropertyAdapter.class)
    private final DoubleProperty _LayoutX;
    @XmlElement(name = StaticStrings.Y)
    @XmlJavaTypeAdapter(DoublePropertyAdapter.class)
    private final DoubleProperty _LayoutY;
    @XmlElement(name = StaticStrings.OutputType)
    @XmlJavaTypeAdapter(StringPropertyAdapter.class)
    private final StringProperty _OutputType;
    @XmlElement(name = StaticStrings.OutputVariable)
    @XmlJavaTypeAdapter(StringPropertyAdapter.class)
    private final StringProperty _OutputName;
    @XmlElement(name = StaticStrings.FunctionType)
    @XmlJavaTypeAdapter(StringPropertyAdapter.class)
    private final StringProperty _FunctionType;
    @XmlElement(name = StaticStrings.FunctionContent)
    @XmlJavaTypeAdapter(StringPropertyAdapter.class)
    private final StringProperty _FunctionContent;
    @XmlElement(name = "label")
    @XmlJavaTypeAdapter(StringPropertyAdapter.class)
    private final StringProperty _SelfLabel;
    @XmlElementWrapper(name = "linkTo")
    @XmlElement(name = "label")
    @XmlJavaTypeAdapter(StringPropertyAdapter.class)
    private final List<StringProperty> _LinkToLabels;
    @XmlElementWrapper(name = "lineLabels")
    @XmlElement(name = "label")
    private final List<String> _LineLabels;    
    
    public MethodLine()
    {
        this("", "", "", "", 0, 0, "", new ArrayList<>(), new ArrayList<>());
    }
    public MethodLine(String outputType, String outputVariableName, String functionType, String functionContent,
            double layoutX, double layoutY, String selfLabel, List<String> linkToLabels, List<String> lineLabels)
    {
        _LayoutX = ModelFactory.GetDoublePropertyWithValue(layoutX);
        _LayoutY = ModelFactory.GetDoublePropertyWithValue(layoutY);
        _OutputName = ModelFactory.GetStringPropertyWithValue(outputVariableName);
        _OutputType = ModelFactory.GetStringPropertyWithValue(outputType);
        _FunctionType = ModelFactory.GetStringPropertyWithValue(functionType);
        _FunctionContent = ModelFactory.GetStringPropertyWithValue(functionContent);
        _SelfLabel = ModelFactory.GetStringPropertyWithValue(selfLabel);
        _LinkToLabels = ModelFactory.GetStringPropertyList(linkToLabels);
        _LineLabels = lineLabels;
    }

    @Override
    public StringProperty GetFunctionContent()
    {
        return _FunctionContent;
    }
    

    @Override
    public StringProperty GetOutputType()
    {
       return _OutputType;
    }

    @Override
    public StringProperty GetOutputVariableName()
    {
        return _OutputName;
    }

    @Override
    public StringProperty GetFunctionType()
    {
        return _FunctionType;
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
    public StringProperty GetSelfLabel()
    {
        return _SelfLabel;
    }

    @Override
    public List<StringProperty> GetLinkToLabels()
    {
        return _LinkToLabels;
    }

    @Override
    public List<String> GetLineLabels()
    {
        return _LineLabels;
    }

    @Override
    public StringExpression[] GetExpressions()
    {
        return new StringExpression[]
        {
                ModelFactory.GetStringPropertyWithValue("Self: "),
            this.GetSelfLabel(),
            ModelFactory.GetStringPropertyWithValue("|"),
            ModelFactory.GetStringPropertyWithValue("Function"),
            this.GetFunctionType(),
            ModelFactory.GetStringPropertyWithValue("\n"),
            ModelFactory.GetStringPropertyWithValue("Content"),
            this.GetFunctionContent(),
            ModelFactory.GetStringPropertyWithValue("\n"),
            this.GetOutputType(),
            ModelFactory.GetStringPropertyWithValue(" "),
            this.GetOutputVariableName()
        };
    }    
}
