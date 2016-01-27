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
package Hint;

import Model.ACClass;
import Model.Field;
import Model.Method;
import Model.MethodLine;
import Model.MethodLineFunctions;
import Model.Project;
import Utility.GetArrayList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Liu
 */
class HintGenerator implements IHintGenerator
{

    final Project _Project;
    final Model_Java _SDKPackages;
    final List<String> _ClsNames;
    final List<String> _MthdNames;
    final List<String> _StaticFieldNames;

    public HintGenerator(Project project, Model_Java sdkPackages)
    {
        _Project = project;
        _SDKPackages = sdkPackages;
        _ClsNames = this.GetClsNamesFromSDKPkgs();
        _MthdNames = this.GetMthdNamesFromSDKPkgs();
        _StaticFieldNames = this.GetStaticFieldNamesFromSDKPkgs();
    }

    final List<String> GetClsNamesFromSDKPkgs()
    {
        List<String> list = new ArrayList<>();

        for (ACClass cls : _SDKPackages.GetClasses())
        {
            String name = cls.GetName().getValue();
            if (list.contains(name) == false)
            {
                list.add(name);
            }
        }
        return Collections.unmodifiableList(list);
    }

    final List<String> GetMthdNamesFromSDKPkgs()
    {
        List<String> list = new ArrayList<>();
        for (ACClass cls : _SDKPackages.GetClasses())
        {
            for (Method mthd : cls.GetMethods())
            {
                String name = mthd.GetName().getValue();
                if(mthd.isStatic().getValue())
                    name = cls.GetName().getValue() + "." + name;
                if (list.contains(name) == false)
                {
                    list.add(name);
                }
            }
        }
        return list;
    }

    final List<String> GetStaticFieldNamesFromSDKPkgs()
    {
        List<String> list = new ArrayList<>();
        for (ACClass cls : _SDKPackages.GetClasses())
        {
            String clsName = cls.GetName().getValue();
            for (Field field : cls.GetFields())
            {
                if (field.isStatic().getValue())
                {
                    String fldName = clsName + "." + field.GetName().getValue();
                    if (list.contains(fldName) == false)
                    {
                        list.add(fldName);
                    }
                }
            }
        }
        return list;
    }

    List<String> GetNamesStartWithInput(String input, List<String> names)
    {
        String lowCaseInput = input.toLowerCase();
        List<String> list = new ArrayList<>();
        for (String name : names)
        {
            String lowCaseName = name.toLowerCase();
            

            if (name.contains("."))
            {
                String lastPart = name.substring(name.lastIndexOf(".") + 1);
                String lowCaseLastPart = lastPart.toLowerCase();
                if (lowCaseLastPart.startsWith(lowCaseInput))
                {
                    list.add(name);
                }
            }

            if (list.contains(name) == false && lowCaseName.startsWith(lowCaseInput))
            {
                list.add(name);
            }

        }
        return list;
    }

    List<String> GetStaticFieldNamesOfCurrentProject()
    {
        List<String> list = new ArrayList<>();
        for (ACClass cls : _Project.GetClasses())
        {
            String clsName = cls.GetName().getValue();
            for (Field fld : cls.GetFields())
            {
                if (fld.isStatic().getValue())
                {
                    String fldName = clsName + "." + fld.GetName().getValue();
                    if (list.contains(fldName) == false)
                    {
                        list.add(fldName);
                    }
                }
            }
        }

        return list;
    }

    List<String> GetMethodNamesOfCurrentProject()
    {
        List<String> list = new ArrayList<>();
        for (ACClass cls : _Project.GetClasses())
        {
            for (Method mthd : cls.GetMethods())
            {
                String name = mthd.GetName().getValue();
                if(mthd.isStatic().getValue())
                    name = cls.GetName().getValue() + "." + name;
                if (list.contains(name) == false)
                {
                    list.add(name);
                }
            }
        }
        return list;
    }

    List<String> GetClassNamesOfCurrentProject()
    {
        List<String> list = new ArrayList<>();
        for (ACClass cls : _Project.GetClasses())
        {
            String name = cls.GetName().getValue();
            if (list.contains(name) == false)
            {
                list.add(name);
            }
        }
        return list;
    }

    /**
     * Only the variable names of normal (other) type of method lines will be
     * returned.
     *
     * @param methodlines
     * @return
     */
    List<String> GetVariablesOfCurrentMethod(List<MethodLine> methodlines)
    {
        List<String> names = new ArrayList<>();
        for (MethodLine ml : methodlines)
        {
            if (ml.GetFunctionType().getValue().equals(MethodLineFunctions.Other))
            {
                names.add(ml.GetOutputVariableName().getValue());
                // currently onlt get output var names from other type (normal lines)
                // for/foreach/try with resources should output its new var names from its content here
            }
        }
        return names;
    }

    List<String> GetFieldNamesOfCurrentCls(List<Field> fields)
    {
        List<String> list = new ArrayList<>();
        for (Field fld : fields)
        {
            String name = fld.GetName().getValue();
            if (fld.isStatic().getValue())
            {
                continue; // not duplicate with get static fields from current project
            } else if (list.contains(name) == false)
            {
                list.add(name);
            }
        }
        return list;
    }

    @Override
    public List<String> GetHint(String input, List<MethodLine> methodLines, ACClass currentCls)
    {
        List<String> projClsNameList = this.GetClassNamesOfCurrentProject();
        List<String> projMthdNameList = this.GetMethodNamesOfCurrentProject();
        List<String> clsFieldList = this.GetFieldNamesOfCurrentCls(currentCls.GetFields());
        List<String> projStaticFieldList = this.GetStaticFieldNamesOfCurrentProject();
        List<String> varList = this.GetVariablesOfCurrentMethod(methodLines);
        List<String> total = new ArrayList<>();
        boolean isReset = this.IsReset(input);
        if (isReset)
        {
            total.addAll(_ClsNames);
            total.addAll(_MthdNames);
            total.addAll(_StaticFieldNames);
            total.addAll(projClsNameList);
            total.addAll(projMthdNameList);
            total.addAll(clsFieldList);
            total.addAll(projStaticFieldList);
            total.addAll(varList);
            total.add("new"); // special var/type names
            total.add("this");
            total.add("void");
        } else
        {
            input = this.GetLastPart(input);
            total.addAll(this.GetNamesStartWithInput(input, _ClsNames));
            total.addAll(this.GetNamesStartWithInput(input, _MthdNames));
            total.addAll(this.GetNamesStartWithInput(input, _StaticFieldNames));
            total.addAll(this.GetNamesStartWithInput(input, projClsNameList));
            total.addAll(this.GetNamesStartWithInput(input, projMthdNameList));
            total.addAll(this.GetNamesStartWithInput(input, projStaticFieldList));
            total.addAll(this.GetNamesStartWithInput(input, clsFieldList));
            varList.add("new");
            varList.add("this");
            varList.add("void");
            total.addAll(this.GetNamesStartWithInput(input, varList));
        }
        Collections.sort(total);
        return total;
    }
    
    private String GetLastPart(String input)
    {
        int space = input.lastIndexOf(" ");
        return input.substring(space + 1);
    }

    boolean IsReset(String input)
    {
        if (input.isEmpty())
        {
            return true;
        }
        if (input.endsWith(".") || input.endsWith("(") || input.endsWith("<") || input.endsWith(" ") || input.endsWith("="))
        {
            return true;
        }
        return false;
    }

}
