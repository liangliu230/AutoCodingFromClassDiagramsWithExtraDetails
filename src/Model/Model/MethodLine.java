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

import Model.interfaces.IFunction;
import Model.interfaces.IFunctionInput;
import Model.interfaces.ILineNumber;
import Model.interfaces.ILineLabel;
import Model.interfaces.IMethodLine;
import Model.interfaces.IVariable;
import java.util.List;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Liu
 */
public class MethodLine implements IMethodLine
{
    private final IFunction _Function;
    private final IVariable _OutputVariable;
    private final ListProperty<IFunctionInput> _Inputs;
    private final ListProperty<ILineLabel> _LineLabels;
    private final ILineNumber _LineNumber;

    public MethodLine(IFunction function,
                      IVariable outputVariable,
                      List<IFunctionInput> inputs,
                      List<ILineLabel> lineLabels,
                      ILineNumber lineNumber)
    {
        _Function = function;
        _OutputVariable = outputVariable;
        ObservableList<IFunctionInput> inps = FXCollections.observableArrayList(inputs);
        _Inputs = new SimpleListProperty<>(inps);
        ObservableList<ILineLabel> labels = FXCollections.observableArrayList(lineLabels);
        _LineLabels = new SimpleListProperty<>(labels);
        _LineNumber = lineNumber;
    }

    @Override
    public IFunction GetFunction()
    {
        return _Function;
    }

    @Override
    public IVariable GetOutputVariable()
    {
        return _OutputVariable;
    }

    @Override
    public ListProperty<IFunctionInput> GetInputs()
    {
        return _Inputs;
    }

    @Override
    public ListProperty<ILineLabel> GetLineLabels()
    {
        return _LineLabels;
    }

    @Override
    public ILineNumber GetLineNumber()
    {
        return _LineNumber;
    }

    @Override
    public void RemoveInput(IFunctionInput input)
    {
        this._Inputs.remove(input);
    }

    @Override
    public void RemoveLineLabel(ILineLabel lineLabel)
    {
        this._LineLabels.remove(lineLabel);
    }

  
    
}
