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

import Model.interfaces.IClass;
import Model.interfaces.IField;
import Model.interfaces.IFullName;
import Model.interfaces.IMethod;
import Model.interfaces.IModifiers;
import Model.interfaces.IVisibility;
import java.util.List;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Liu
 */
public class ACClass implements IClass
{
    private final ListProperty<IField> _Fields;
    private final ListProperty<IFullName> _Implements;
    private final BooleanProperty _IsInterface;
    private final BooleanProperty _IsEnum;
    private final IFullName _Name;
    private final IVisibility _Visibility;
    private final IModifiers _Modifiers;
    private final ListProperty<IFullName> _Extends;
    private final ListProperty<IMethod> _Methods;
    private final DoubleProperty _Layout_X;
    private final DoubleProperty _Layout_Y;
    private final BooleanProperty _IsInitialize;
    
    public ACClass(IFullName name, IVisibility visibility,
                   IModifiers modifiers, List<IMethod> methods,
                   List<IFullName> _extends, List<IField> fields,
                   List<IFullName> _implements, boolean isInterface, boolean isEnum,boolean isInitialize,
                   double x, double y)
    {
                _Name = name;
        _Visibility = visibility;
        ObservableList<IMethod> ml = FXCollections.observableArrayList(methods);
        _Methods = new SimpleListProperty<>(ml);
        _Modifiers = modifiers;
        ObservableList<IFullName> fl = FXCollections.observableArrayList(_extends);
        _Extends = new SimpleListProperty<>(fl);
        ObservableList<IField> fields1 = FXCollections.observableList(fields);
        _Fields = new SimpleListProperty<>(fields1);
        ObservableList<IFullName> f1 = FXCollections.observableList(_implements);
        _Implements = new SimpleListProperty<>(f1);
        _IsInterface = new SimpleBooleanProperty(isInterface);
        _IsEnum = new SimpleBooleanProperty(isEnum);
        _Layout_X = new SimpleDoubleProperty(x);
        _Layout_Y = new SimpleDoubleProperty(y);
        _IsInitialize = new SimpleBooleanProperty(isInitialize);
    }

    @Override
    public BooleanProperty IsEnum()
    {
        return _IsEnum;
    }
    
@Override
    public ListProperty<IFullName> GetExtends()
    {
        return _Extends;
    }

    @Override
    public ListProperty<IMethod> GetMethods()
    {
        return _Methods;
    }


    @Override
    public IFullName GetFullName()
    {
        return _Name;
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
    public BooleanProperty IsInterface()
    {
      return _IsInterface;
    }
    
    @Override
    public ListProperty<IField> GetFields()
    {
        return _Fields;
    }

    @Override
    public ListProperty<IFullName> GetImplements()
    {
        return _Implements;
    }

    @Override
    public DoubleProperty GetLayoutX()
    {
        return _Layout_X;
    }

    @Override
    public DoubleProperty GetLayoutY()
    {
        return _Layout_Y;
    }

    @Override
    public BooleanProperty IsInitialize()
    {
        return _IsInitialize;
    }

    @Override
    public void RemoveField(IField field)
    {
        this._Fields.remove(field);        
    }

    @Override
    public void RemoveMethod(IMethod method)
    {
        this._Methods.remove(method);
    }

    @Override
    public void RemoveExtend(IFullName xtnd)
    {
        this._Extends.remove(xtnd);
    }

    @Override
    public void RemoveImplement(IFullName imp)
    {
        this._Implements.remove(imp);
    }
    
    
}
