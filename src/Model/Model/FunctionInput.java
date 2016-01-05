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
public class FunctionInput implements IFunctionInput
{
    private final IFunction _Function;
    private final ListProperty<IVariable> _InputVariables;
    
    public FunctionInput(IFunction function, List<IVariable> variables)
    {
        _Function = function;
        ObservableList<IVariable> list = FXCollections.observableArrayList(variables);
        _InputVariables = new SimpleListProperty<>(list);
    }
    
    @Override
    public IFunction GetFunction()
    {
        return _Function;
    }

    @Override
    public ListProperty<IVariable> GetInputVariables()
    {
        return _InputVariables;
    }

    @Override
    public void RemoveInputVariable(IVariable var)
    {
        this._InputVariables.remove(var);
    }
    
}
