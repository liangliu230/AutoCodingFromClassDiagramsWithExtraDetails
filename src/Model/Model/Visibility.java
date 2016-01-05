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

import Model.AccessModifiers;
import Model.StaticStrings;
import Model.interfaces.IVisibility;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Liu
 */
public class Visibility implements IVisibility
{
    private StringProperty _Visibility;
    public static final Visibility Public = new Visibility(StaticStrings.Public);
    public static final Visibility Private = new Visibility(StaticStrings.Private);
    public static final Visibility Protected = new Visibility(StaticStrings.Protected);
    public static final Visibility Package = new Visibility(StaticStrings.Package);
    
    public Visibility()
    {
        this(AccessModifiers.Public.name());
    }
    public Visibility(String visibility)
    {
        _Visibility = new SimpleStringProperty(visibility);
    }
    
    @Override
    public StringProperty GetVisibility()
    {
      return _Visibility;
    }
    
}
