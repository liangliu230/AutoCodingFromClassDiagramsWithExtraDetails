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

import Model.interfaces.IModifiers;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

/**
 *
 * @author Liu
 */
public class Modifiers implements IModifiers
{
    private BooleanProperty _IsAbstract;
    private BooleanProperty _IsStatic;
    private BooleanProperty _IsFinal;
    public Modifiers()
    {
        this(false, false, false);
    }
    public Modifiers(boolean isAbstract, boolean isStatic, boolean isFinal)
    {
        _IsAbstract = new SimpleBooleanProperty(isAbstract);
        _IsStatic = new SimpleBooleanProperty(isStatic);
        _IsFinal = new SimpleBooleanProperty(isFinal);
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        if(IsAbstract().getValue())
            builder.append("abstract ");
        if(IsFinal().getValue())
            builder.append("final ");
        if(IsStatic().getValue())
            builder.append("static ");
        return builder.toString();
    }
    
    
    
    @Override
    public BooleanProperty IsAbstract()
    {
        return _IsAbstract;
    }

    @Override
    public BooleanProperty IsStatic()
    {
        return _IsStatic;
    }

    @Override
    public BooleanProperty IsFinal()
    {
        return _IsFinal;
    }

}
