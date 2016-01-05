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

import Model.interfaces.IFullName;
import Model.interfaces.IFunctionInput;
import Model.interfaces.IMethod;
import Model.interfaces.IMethodLine;
import Model.interfaces.IModifiers;
import Model.interfaces.IName;
import Model.interfaces.IParameter;
import Model.interfaces.IVisibility;
import java.util.List;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Liu
 */
public class Method  implements IMethod
{
    private final IName _Name;
    private final IFullName _OutputType;
    private final ListProperty<IParameter> _InputParameters;
    private final ListProperty<IMethodLine> _MethodLines;
    private final ListProperty<IFunctionInput> _Annotations;
    private final IVisibility _Visibility;
    private final IModifiers _Modifiers;
    private final BooleanProperty _IsStrictOrder;
    private final ListProperty<IFullName> _Throws;
    
    public Method(IName name, 
                  IFullName outputType, 
                  List<IParameter> inputParameters,
                  List<IMethodLine> methodLines,
                  List<IFunctionInput> annotations,
                  IVisibility visibility,
                  IModifiers modifiers,
                  boolean isStrictOrder,
                  List<IFullName> _throws)
    {
        _Name = name;
        _OutputType = outputType;
        ObservableList<IParameter> parameters = FXCollections.observableArrayList(inputParameters);
        _InputParameters = new SimpleListProperty<>(parameters);
        ObservableList<IMethodLine> lines = FXCollections.observableArrayList(methodLines);
        _MethodLines = new SimpleListProperty<>(lines);
        ObservableList<IFunctionInput> anns = FXCollections.observableArrayList(annotations);
        _Annotations = new SimpleListProperty<>(anns);
        _Visibility = visibility;
        _Modifiers = modifiers;
        _IsStrictOrder = new SimpleBooleanProperty(isStrictOrder);
        ObservableList<IFullName> ts = FXCollections.observableArrayList(_throws);
        _Throws = new SimpleListProperty<>(ts);
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append(_Visibility.GetVisibility().getValue()).append(" ");        
        builder.append(_Modifiers.toString());
        builder.append(_Name.GetName().getValue()).append(" ");
        builder.append("(");
        for(IParameter parameter : _InputParameters)
            builder.append(parameter.GetType().toString()).append(" ").append(parameter.GetName().getValue()).append(",");
        if(!_InputParameters.isEmpty())
        builder.deleteCharAt(builder.lastIndexOf(","));
        builder.append(")").append(" ");
        if(_Throws.isEmpty() == false)
            builder.append("throws exception");
        return builder.toString();
    }
    
    @Override
    public IFullName GetOutputType()
    {
        return _OutputType;
    }

    @Override
    public ListProperty<IParameter> GetInputParameters()
    {
        return _InputParameters;
    }

    @Override
    public ListProperty<IMethodLine> GetMethodBody()
    {
        return _MethodLines;
    }

    @Override
    public ListProperty<IFunctionInput> GetAnnotations()
    {
        return _Annotations;
    }


    @Override
    public IVisibility GetVisibility()
    {
        return _Visibility;
    }

    @Override
    public IModifiers GetModifiers()
    {
        return _Modifiers;
    }

    @Override
    public BooleanProperty IsStrictOrder()
    {
        return _IsStrictOrder;
    }

    @Override
    public ListProperty<IFullName> GetThrows()
    {
        return _Throws;
    }

    @Override
    public StringProperty GetName()
    {
        return this._Name.GetName();
    }

    @Override
    public void RemoveInputParameter(IParameter parameter)
    {
       this._InputParameters.remove(parameter);
    }

    @Override
    public void RemoveMethodLine(IMethodLine methodLine)
    {
       this._MethodLines.remove(methodLine);
    }

    @Override
    public void RemoveAnnotation(IFunctionInput annotation)
    {
        this._Annotations.remove(annotation);
    }

    @Override
    public void RemoveException(IFullName exception)
    {
        this._Throws.remove(exception);
    }
    
}
