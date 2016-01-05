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
package Model.interfaces;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Liu
 */
public interface IMethod extends IName, IGetVisibility, IGetModifiers, IBase
{
    IFullName GetOutputType();
    ListProperty<IParameter> GetInputParameters();
    ListProperty<IMethodLine> GetMethodBody();
    ListProperty<IFunctionInput> GetAnnotations();
    BooleanProperty IsStrictOrder();
    ListProperty<IFullName> GetThrows();
    void RemoveInputParameter(IParameter parameter);
    void RemoveMethodLine(IMethodLine methodLine);
    void RemoveAnnotation(IFunctionInput annotation);
    void RemoveException(IFullName exception);

    @Override
    public String toString();
    
}
