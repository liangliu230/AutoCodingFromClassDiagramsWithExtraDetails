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
import Model.interfaces.IName;
import Model.interfaces.IPackage;
import Model.interfaces.IProject;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Liu
 */
public class Project implements IProject
{
    private final List<IClass> _Classes;
    private final List<IPackage> _Packages;
    private final IName _Name;
    public Project(IName name, List<IClass> classes, List<IPackage> packages)
    {
        _Classes = classes;
        _Packages = packages;
        _Name = name;
    }
    
    public Project()
    {
        _Classes = new ArrayList<>();
        _Packages = new ArrayList<>();
        _Name = new Name();
    }
    
    @Override
    public List<IClass> GetClasses()
    {
        return _Classes;
    }

    @Override
    public List<IPackage> GetPackages()
    {
        return _Packages;
    }

    @Override
    public IName GetName()
    {
        return _Name;
    }

    @Override
    public void RemovePackage(IPackage pkg)
    {
        this._Packages.remove(pkg);
    }

    @Override
    public void RemoveClass(IClass cls)
    {
        this._Classes.remove(cls);
    }
    
}
