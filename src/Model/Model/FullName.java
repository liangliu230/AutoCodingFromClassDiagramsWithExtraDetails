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
import Model.interfaces.IName;

/**
 *
 * @author Liu
 */
public class FullName implements IFullName
{
    private IName[] _Packages;
    private final IName _Name;
    
    public FullName()
    {
        _Packages = new IName[0];
        _Name = new Name();
    }
    public FullName(IName[] packages, IName name)
    {
        _Packages = packages;
        _Name = name;
    }
    
    @Override
    public IName[] GetPackageNames()
    {
        return _Packages;
    }

    @Override
    public IName GetClassName()
    {
      return _Name;
    }

    @Override
    public void SetPackageNames(IName[] packageNames)
    {
        _Packages = packageNames;
    }

    @Override
    public String toString()
    {
       StringBuilder builder = new StringBuilder();
       for(IName p : _Packages)
           builder.append(p.GetName().getValue()).append(".");
       builder.append(_Name.GetName().getValue());
       return builder.toString();
    } 
    
}
