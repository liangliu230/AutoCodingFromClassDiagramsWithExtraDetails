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
package FileGeneration.Java;

import FileGeneration.IGenerateFiles;
import Model.Exceptions.ClassTypeCombinationWrongException;
import Model.Model.LineLabel;
import Model.StaticStrings;
import Model.interfaces.IClass;
import Model.interfaces.IField;
import Model.interfaces.IFullName;
import Model.interfaces.IFunction;
import Model.interfaces.IFunctionInput;
import Model.interfaces.ILineLabel;
import Model.interfaces.IMethod;
import Model.interfaces.IMethodLine;
import Model.interfaces.IModifiers;
import Model.interfaces.IName;
import Model.interfaces.IPackage;
import Model.interfaces.IParameter;
import Model.interfaces.IProject;
import Model.interfaces.IVariable;
import Model.interfaces.IVisibility;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Liu
 */
public class JavaFileGenerator implements IGenerateFiles
{

    @Override
    public void Generate(IProject project, String location) throws Exception
    {
        String reversslashedLocation = this.ReplaceSlash(location);
        if (reversslashedLocation.charAt(reversslashedLocation.length() - 1) != '/')
        {
            reversslashedLocation += "/";
        }
        reversslashedLocation += project.GetName().GetName().getValue() + "/";
        Path p = Paths.get(reversslashedLocation);
        if (Files.exists(p) == false)
        {
            Files.createDirectories(p);
        }
        this.GeneratePackages(project.GetPackages(), reversslashedLocation);
        this.GenerateClasses(project.GetClasses(), reversslashedLocation);
    }

    private String ReplaceDot(String str)
    {
        return this.Replace(str, ".", "/");
    }

    private String ReplaceSlash(String str)
    {
        return this.Replace(str, "\\", "/");
    }

    private String Replace(String str, String target, String replaceTo)
    {
        return str.replace(target, replaceTo);
    }

    private void GeneratePackages(List<IPackage> packages, String location) throws Exception
    {
        for (IPackage pkg : packages)
        {
            String name = pkg.GetFullName().toString();
            name = this.ReplaceDot(name);
            Path p = Paths.get(location, name);
            Files.createDirectories(p);
        }
    }

    private void GenerateClasses(List<IClass> classes, String location) throws Exception
    {
        for (IClass cls : classes)
        {
            this.GenerateClass(cls, location);
        }
    }

    private void GenerateClass(IClass _Class, String location) throws Exception
    {
        String packageLine = this.GetPackageLine(_Class.GetFullName());
        String classDeclaration = this.GetClassDeclarationLine(_Class);
        List<String> fieldPart = this.GetFields(_Class.GetFields());
        List<String> methodPart = this.GetMethods(_Class.GetMethods(), _Class.IsInterface().getValue(), _Class.IsEnum().getValue());

        StringBuilder builder = new StringBuilder();
        builder.append(packageLine).append("\n");
        builder.append(classDeclaration).append("\n");

        builder.append("{").append("\n");

        for (String fld : fieldPart)
        {
            builder.append(fld).append("\n");
        }
        for (String mthd : methodPart)
        {
            builder.append(mthd).append("\n");
        }

        builder.append("}");

        String fileContent = builder.toString();
        String fileName = this.GenerateFileName(_Class.GetFullName()) + ".java";
        Path pth = Paths.get(location, fileName);
        if (false == Files.exists(pth))
        {
            Files.createFile(pth);
        }
        Files.write(pth, fileContent.getBytes());
    }

    private String GenerateFileName(IFullName className)
    {
        String name = className.toString();
        return this.ReplaceDot(name);
    }

    private String GetPackageLine(IFullName className)
    {
        String name = className.toString();
        int index = name.lastIndexOf(".");
        String pkg = name.substring(0, index);
        return StaticStrings.Package + " " + pkg + ";";
    }

    private String GetClassDeclarationLine(IClass _Class) throws Exception
    {
        String visibility = this.GetVisibility(_Class.GetVisibility());
        String modifiers = this.GetModifiers(_Class.GetModifiers());
        String classType = this.GetClassType(_Class);
        String extds = this.GetExtends(_Class.GetExtends());
        String imps = this.GetImplements(_Class.GetImplements());
        String className = this.GetName(_Class.GetFullName().GetClassName());
        StringBuilder builder = new StringBuilder();
        builder.append(visibility).append(" ");
        builder.append(modifiers).append(" ");
        builder.append(classType).append(" ");
        builder.append(className).append(" ");
        builder.append(extds).append(" ");
        builder.append(imps).append(" ");
        return builder.toString();
    }

    private String GetVisibility(IVisibility visibility)
    {
        return visibility.GetVisibility().getValue();
    }

    private String GetModifiers(IModifiers modifiers)
    {
        StringBuilder builder = new StringBuilder();
        if (modifiers.IsAbstract().getValue() == true)
        {
            builder.append(StaticStrings.Abstract).append(" ");
        }
        if (modifiers.IsStatic().getValue() == true)
        {
            builder.append(StaticStrings.Static).append(" ");
        }
        if (modifiers.IsFinal().getValue() == true)
        {
            builder.append(StaticStrings.Final).append(" ");
        }
        return builder.toString();
    }

    private String GetName(IName name)
    {
        return name.GetName().getValue();
    }

    private String GetType(IFullName fullName)
    {
        return fullName.toString();
    }

    private String GetClassType(IClass _Class) throws Exception
    {
        boolean isEnum = _Class.IsEnum().getValue();
        boolean isInterface = _Class.IsInterface().getValue();
        if (isEnum == true && isInterface == true)
        {
            throw new ClassTypeCombinationWrongException(_Class.GetFullName().toString());
        } else if (isEnum)
        {
            return StaticStrings.Enum;
        } else if (isInterface)
        {
            return StaticStrings.Interface;
        } else
        {
            return StaticStrings.Class;
        }
    }

    private String GetExtends(List<IFullName> exds)
    {
        if (exds.isEmpty())
        {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        builder.append(StaticStrings.Extends).append(" ");
        for (IFullName exd : exds)
        {
            builder.append(exd.toString()).append(",").append(" ");
        }
        builder.deleteCharAt(builder.lastIndexOf(","));
        builder.append(" ");
        return builder.toString();
    }

    private String GetImplements(List<IFullName> imps)
    {
        if (imps.isEmpty())
        {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        builder.append(StaticStrings.Implements).append(" ");
        for (IFullName imp : imps)
        {
            builder.append(imp.toString()).append(",").append(" ");
        }
        builder.deleteCharAt(builder.lastIndexOf(","));
        builder.append(" ");
        return builder.toString();
    }

    private List<String> GetFields(List<IField> fields) throws Exception
    {
        List<String> fieldList = new ArrayList<>();
        for (IField fld : fields)
        {
            String fieldLine = this.GetFieldLine(fld);
            fieldList.add(fieldLine);
        }
        return fieldList;
    }

    private String GetFieldLine(IField field) throws Exception
    {
        String type = this.GetType(field.GetType());
        String name = this.GetName(field.GetName());
        String modifiers = this.GetModifiers(field.GetModifiers());
        String visibility = this.GetVisibility(field.GetVisibility());
        String defaultValue = this.GetFieldDefaultValue(field.GetDefaultValue());
        StringBuilder builder = new StringBuilder();
        builder.append(visibility).append(" ");
        builder.append(modifiers).append(" ");
        builder.append(type).append(" ");
        builder.append(name).append(" ");
        if (defaultValue == null || defaultValue.isEmpty() || defaultValue.equals(""))
        {
            builder.append(";");
        } else
        {
            builder.append(" ").append("=").append(" ");
            builder.append(defaultValue).append(";");
        }
        builder.append("\n");
        return builder.toString();
    }

    private String GetFieldDefaultValue(IFunctionInput defaultValue) throws Exception
    {
        String func = defaultValue.GetFunction().GetFunction().getValue();

        List<IVariable> list = defaultValue.GetInputVariables();
        if (list.isEmpty())
        {
            return "";
        }

        switch (func)
        {
            case FunctionMarkerInFunctionInput.ValueOnly:
            case FunctionMarkerInFunctionInput.VariableOnly:
                return list.get(0).GetName().getValue();
            case FunctionMarkerInFunctionInput.TypeAndValue:
                StringBuilder builder = new StringBuilder();
                builder.append(FunctionStaticStrings.New).append(" ");
                builder.append(list.get(0).GetName().getValue());
                if (list.size() == 1)
                {
                    builder.append("();");
                } else
                {
                    builder.append("(");
                    for (int index = 1; index < list.size(); index++)
                    {
                        builder.append(list.get(index).GetName().getValue());
                        if ((index + 1) < list.size())
                        {
                            builder.append(",");
                        }
                    }
                    builder.append(");");
                }
                return builder.toString();
            default:
                return func + ";";
            //throw new Exception("other mark strings in functioninput: " + func);
        }
    }

    private List<String> GetMethods(List<IMethod> methods, boolean isInterface, boolean isEnum) throws Exception
    {
        List<String> list = new ArrayList<>();
        for (IMethod mthd : methods)
        {
            String str = this.GetMethod(mthd, isInterface, isEnum);
            list.add(str);
        }
        return list;
    }

    private String GetMethod(IMethod method, boolean isInterface, boolean isEnum) throws Exception
    {
        String declaration = this.GetMethodDeclaration(method, isInterface, isEnum);
        String annotations = this.GetAnnotations(method.GetAnnotations());
        List<String> body = this.GetMethodBody(method.GetMethodBody());
        StringBuilder builder = new StringBuilder();
        builder.append(annotations).append("\n");
        builder.append(declaration).append("\n");
        if (isInterface)
        {
            builder.append(";");
        } else
        {
            builder.append("{").append("\n");
            for (String line : body)
            {
                builder.append(line).append("\n");
            }
            builder.append("}").append("\n");
        }
        return builder.toString();
    }

    private String GetMethodDeclaration(IMethod method, boolean isInterface, boolean isEnum)
    {
        String visibility = this.GetVisibility(method.GetVisibility());
        String modifiers = this.GetModifiers(method.GetModifiers());
        String returnType = this.GetType(method.GetOutputType());
        String inputList = this.GetInputList(method.GetInputParameters());
        String exceptions = this.GetExceptions(method.GetThrows());
        String name = this.GetName(method);
        StringBuilder builder = new StringBuilder();
        if (false == isInterface && false == isEnum)
        {
            builder.append(visibility).append(" ");
        }
        builder.append(modifiers).append(" ");
        builder.append(returnType).append(" ");
        builder.append(name).append(" ");
        builder.append("(").append(" ");
        builder.append(inputList).append(" ");
        builder.append(")").append(" ");
        if (method.GetThrows().isEmpty() == false)
        {
            builder.append(StaticStrings.Throws).append(" ");
            builder.append(exceptions).append(" ");
        }
        return builder.toString();
    }

    private String GetExceptions(List<IFullName> excptns)
    {
        if (excptns.isEmpty())
        {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for (IFullName excptn : excptns)
        {
            builder.append(excptn.toString()).append(",");
        }
        builder.deleteCharAt(builder.lastIndexOf(","));
        return builder.toString();
    }

    private String GetAnnotations(List<IFunctionInput> annotations)
    {
        StringBuilder builder = new StringBuilder();
        for (IFunctionInput input : annotations)
        {
            builder.append("@");
            builder.append(input.GetFunction().GetFunction().getValue());
            List<IVariable> list = input.GetInputVariables();
            if (list.isEmpty())
            {
                continue;
            } else
            {
                builder.append("(");
                for (IVariable var : list)
                {
                    builder.append(var.GetName().getValue()).append(",");
                }
                builder.deleteCharAt(builder.lastIndexOf(","));
                builder.append(")");
            }
            builder.append("\n");
        }
        return builder.toString();
    }

    private String GetInputList(List<IParameter> parameters)
    {
        if (parameters.isEmpty())
        {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for (IParameter par : parameters)
        {
            String type = this.GetType(par.GetType());
            String name = this.GetName(par);
            builder.append(type).append(" ").append(name).append(",");
        }
        builder.deleteCharAt(builder.lastIndexOf(","));
        return builder.toString();
    }

    private List<String> GetMethodBody(List<IMethodLine> methodLines) throws Exception
    {
        List<String> body = new ArrayList<>();

        if (methodLines.isEmpty())
        {
            return body;
        }

        for (int index = 0; index < methodLines.size(); index++)
        {
            IMethodLine line = methodLines.get(index);
            int preIndex = index - 1;
            int afterIndex = index + 1;
            List<ILineLabel> preSet, afterSet;
            if (preIndex >= 0)
            {
                preSet = methodLines.get(preIndex).GetLineLabels();
            } else
            {
                preSet = new ArrayList<>();
                preSet.add(new LineLabel(StaticStrings.LabelBeforeFirstLine));
            }
            if (afterIndex < methodLines.size())
            {
                afterSet = methodLines.get(afterIndex).GetLineLabels();
            } else
            {
                afterSet = new ArrayList<>();
                afterSet.add(new LineLabel(StaticStrings.LabelAfterLastLine));
            }
            String ml = this.GetMethodLine(line, preSet, afterSet);
            body.add(ml);
        }
        return body;
    }

    private String GetMethodLine(IMethodLine methodLine, List<ILineLabel> preSet, List<ILineLabel> afterSet) throws Exception
    {
        IFunction function = methodLine.GetFunction();

        int leftBracket = this.DifferentItemsInSet2WhichNotExistInSet1(preSet, methodLine.GetLineLabels());
        int rightBracket = this.DifferentItemsInSet2WhichNotExistInSet1(afterSet, methodLine.GetLineLabels());
        StringBuilder builder = new StringBuilder();
        String content;
        String func = function.GetFunction().getValue();
        switch (func)
        {
            case FunctionStaticStrings.New:
                content = this.GetMethodLineWithNewFunction(methodLine);
                break;
            case FunctionStaticStrings.NewVariable:
                content = this.GetMethodLineWithNewVariableFunction(methodLine);
                break;
            case FunctionStaticStrings.Return:
                content = this.GetMethodLineWithReturnFunction(methodLine);
                break;
            case FunctionStaticStrings.If:
                content = this.GetMethodLineWithIfFunction(methodLine);
                break;
            case FunctionStaticStrings.Else:
                content = this.GetMethodLineWithElseFunction(methodLine);
                break;
            case FunctionStaticStrings.Switch:
                content = this.GetMethodLineWithSwitchFunction(methodLine);
                break;
            case FunctionStaticStrings.Case:
                content = this.GetMethodLineWithCaseFunction(methodLine);
                break;
            case FunctionStaticStrings.Default:
                content = this.GetMethodLineWithDefaultFunction(methodLine);
                break;
            case FunctionStaticStrings.For:
                content = this.GetMethodLineWithForFunction(methodLine);
                break;
            case FunctionStaticStrings.ForEach:
                content = this.GetMethodLineWithForeachFunction(methodLine);
                break;
            case FunctionStaticStrings.While:
                content = this.GetMethodLineWithWhileFunction(methodLine);
                break;
            case FunctionStaticStrings.Do:
                content = this.GetMethodLineWithDoFunction(methodLine);
                break;
            case FunctionStaticStrings.SetValue:
                content = this.GetMethodLineWithSetValueFunction(methodLine);
                break;
            case FunctionStaticStrings.Try:
                content = this.GetMethodLineWithTryFunction(methodLine);
                break;
            case FunctionStaticStrings.TryWithResources:
                content = this.GetMethodLineWithTryWithResourcesFunction(methodLine);
                break;
            case FunctionStaticStrings.Catch:
                content = this.GetMethodLineWithCatchFunction(methodLine);
                break;
            case FunctionStaticStrings.Continue:
                content = this.GetMethodLineWithContinueFunction(methodLine);
                break;
            case FunctionStaticStrings.Break:
                content = this.GetMethodLineWithBreakFunction(methodLine);
                break;
            case FunctionStaticStrings.Throw:
                content = this.GetMethodLineWithThrowFunction(methodLine);
                break;
            case FunctionStaticStrings.Finally:
                content = this.GetMethodLineWithFinallyFunction(methodLine);
                break;
            default:
                content = this.GetMethodLineWithOtherFunctions(methodLine);
                break;
        }

        builder.append(content);
        builder = this.AppendLeftCurveBrackets(builder, leftBracket);
        builder = this.AppendRightCurveBrackets(builder, rightBracket);
        return builder.toString();
    }

    private String GetMethodLineWithThrowFunction(IMethodLine line)
    {
        StringBuilder builder = new StringBuilder();
        builder.append(FunctionStaticStrings.Throw).append(" ");
        List<IFunctionInput> inputList = line.GetInputs();
        IFunctionInput input = inputList.get(0);
        IFunction func = input.GetFunction();
        if (func.GetFunction().getValue().equals(FunctionMarkerInFunctionInput.VariableOnly))
        {
            builder.append(input.GetInputVariables().get(0).GetName().getValue());
        } else
        {
            builder.append(FunctionStaticStrings.New).append(" ");
            List<IVariable> list = input.GetInputVariables();
            builder.append(list.get(0).GetName().getValue());
            builder.append("(");
            if (list.size() > 1)
            {
                for (int index = 1; index < list.size(); index++)
                {
                    builder.append(list.get(index).GetName().getValue());
                    if (index != list.size() - 1)
                    {
                        builder.append(",");
                    }
                }
            }
            builder.append(")");
        }
        builder.append(";");
        return builder.toString();
    }

    private String GetMethodLineWithFinallyFunction(IMethodLine line)
    {
        return FunctionStaticStrings.Finally;
    }

    private String GetMethodLineWithBreakFunction(IMethodLine line)
    {
        return FunctionStaticStrings.Break + ";";
    }

    private String GetMethodLineWithContinueFunction(IMethodLine line)
    {
        return FunctionStaticStrings.Continue + ";";
    }

    private String GetMethodLineWithCatchFunction(IMethodLine line)
    {
        StringBuilder builder = new StringBuilder();
        builder.append(FunctionStaticStrings.Catch);
        builder.append("(");
        IFunctionInput input = line.GetInputs().get(0);
        builder.append(input.GetInputVariables().get(0).GetName().getValue()).append(" ");
        builder.append(input.GetInputVariables().get(1).GetName().getValue());
        builder.append(")");
        return builder.toString();
    }

    private String GetMethodLineWithTryFunction(IMethodLine line)
    {
        return FunctionStaticStrings.Try;
    }

    private String GetMethodLineWithTryWithResourcesFunction(IMethodLine line)
    {
        StringBuilder builder = new StringBuilder();
        builder.append(FunctionStaticStrings.Try);
        builder.append("(");
        List<IFunctionInput> list = line.GetInputs();
        for (int index = 0; index < list.size() - 1; index += 2)
        {
            IFunctionInput typeAndName = list.get(index);
            IFunctionInput newVar = list.get(index + 1);
            String type = typeAndName.GetInputVariables().get(0).GetName().getValue();
            String name = typeAndName.GetInputVariables().get(1).GetName().getValue();
            String realtype = newVar.GetInputVariables().get(0).GetName().getValue();
            builder.append(type).append(" ");
            builder.append(name).append(" ");
            builder.append("=").append(" ");
            builder.append(FunctionStaticStrings.New).append(" ");
            builder.append(realtype).append("(");
            if (newVar.GetInputVariables().size() == 1)
            {
                builder.append(")");
            } else
            {
                for (int j = 1; j < newVar.GetInputVariables().size(); j++)
                {
                    builder.append(newVar.GetInputVariables().get(j).GetName().getValue());
                    if (j < newVar.GetInputVariables().size() - 1)
                    {
                        builder.append(",");
                    }
                }
            }
            builder.append(")");
            if (index < list.size() - 2)
            {
                builder.append(";");
            }
        }
        builder.append(")");
        return builder.toString();
    }

    private String GetMethodLineWithSetValueFunction(IMethodLine line)
    {
        IVariable output = line.GetOutputVariable();
        IFunctionInput input = line.GetInputs().get(0);
        StringBuilder builder = new StringBuilder();
        builder.append(output.GetName().getValue()).append(" ");
        builder.append("=").append(" ");
        builder.append(input.GetInputVariables().get(0).GetName().getValue());
        builder.append(";");
        return builder.toString();
    }

    private String GetMethodLineWithDoFunction(IMethodLine line)
    {
        return FunctionStaticStrings.Do;
    }

    private String GetMethodLineWithWhileFunction(IMethodLine line)
    {
        StringBuilder builder = new StringBuilder();
        builder.append(FunctionStaticStrings.While);
        builder.append("(");
        IFunctionInput input = line.GetInputs().get(0);
        String function = input.GetFunction().GetFunction().getValue();
        if (FunctionStaticStrings.IsBooleanOperator(function))
        {
            String var1 = input.GetInputVariables().get(0).GetName().getValue();
            String var2 = input.GetInputVariables().get(1).GetName().getValue();
            builder.append(var1).append(function).append(var2);
        } else // normal function call
        {
            builder.append(function);
            builder.append("(");
            for (int index = 0; index < input.GetInputVariables().size(); index++)
            {
                IVariable var = input.GetInputVariables().get(index);
                builder.append(var.GetName().getValue());
                if (index < input.GetInputVariables().size() - 1)
                {
                    builder.append(",");
                }
            }
            builder.append(")");
        }
        builder.append(")");
        return builder.toString();
    }

    private String GetMethodLineWithForeachFunction(IMethodLine line)
    {
        StringBuilder builder = new StringBuilder();
        builder.append(FunctionStaticStrings.For);
        builder.append("(");
        IFunctionInput typeAndName = line.GetInputs().get(0);
        IFunctionInput variable = line.GetInputs().get(1);
        String type = typeAndName.GetInputVariables().get(0).GetName().getValue();
        String name = typeAndName.GetInputVariables().get(1).GetName().getValue();
        String var = variable.GetInputVariables().get(0).GetName().getValue();
        builder.append(type).append(" ").append(name).append(":").append(var);
        builder.append(")");
        return builder.toString();
    }

    private String GetMethodLineWithForFunction(IMethodLine line)
    {
        StringBuilder builder = new StringBuilder();
        builder.append(FunctionStaticStrings.For);
        builder.append("(");
        List<IFunctionInput> list = line.GetInputs();
        IFunctionInput typeAndName = list.get(0);
        IFunctionInput initValue = list.get(1);
        IFunctionInput stopFunc = list.get(2);
        IFunctionInput stepFunction = list.get(3);
        if (false == typeAndName.GetFunction().GetFunction().getValue().equals(FunctionMarkerInFunctionInput.Empty))
        {
            String type = typeAndName.GetInputVariables().get(0).GetName().getValue();
            String name = typeAndName.GetInputVariables().get(1).GetName().getValue();
            String val = initValue.GetInputVariables().get(1).GetName().getValue();
            builder.append(type).append(" ").append(name).append("=").append(val);
        }
        builder.append(";");
        if (false == stopFunc.GetFunction().GetFunction().getValue().equals(FunctionMarkerInFunctionInput.Empty))
        {
            builder.append(this.GetIFunctionInputThatMightUseBooleanOperators(stopFunc));
        }
        builder.append(";");
        if (false == stepFunction.GetFunction().GetFunction().getValue().equals(FunctionMarkerInFunctionInput.Empty))
        {
            String func = stepFunction.GetFunction().GetFunction().getValue();
            if (FunctionStaticStrings.IsUniOperator(func))
            {
                String var1 = stepFunction.GetInputVariables().get(0).GetName().getValue();
                String var2 = stepFunction.GetInputVariables().get(1).GetName().getValue();
                builder.append(var1).append(func).append(var2);
            } else
            {
                builder.append(func).append("(");
                List<IVariable> vars = stepFunction.GetInputVariables();
                for (int index = 0; index < vars.size(); index++)
                {
                    String var = vars.get(index).GetName().getValue();
                    builder.append(var);
                    if (index < vars.size() - 1)
                    {
                        builder.append(",");
                    }
                }
                builder.append(")");
            }
        }
        builder.append(")");
        return builder.toString();
    }

    private String GetIFunctionInputThatMightUseBooleanOperators(IFunctionInput input)
    {
        StringBuilder builder = new StringBuilder();
        if (FunctionStaticStrings.IsBooleanOperator(input.GetFunction().GetFunction().getValue()))
        {
            String Var1 = input.GetInputVariables().get(0).GetName().getValue();
            String var2 = input.GetInputVariables().get(1).GetName().getValue();
            builder.append(Var1).append(input.GetFunction().GetFunction().getValue()).append(var2);
        } else
        {
            String func = input.GetFunction().GetFunction().getValue();
            List<IVariable> vars = input.GetInputVariables();
            builder.append(func).append("(");
            for (int index = 0; index < vars.size(); index++)
            {
                String var = vars.get(index).GetName().getValue();
                builder.append(var);
                if (index < vars.size() - 1)
                {
                    builder.append(",");
                }
            }
            builder.append(")");
        }
        return builder.toString();
    }

    private String GetMethodLineWithDefaultFunction(IMethodLine line)
    {
        return FunctionStaticStrings.Default + ":";
    }

    private String GetMethodLineWithCaseFunction(IMethodLine line)
    {
        IFunctionInput input = line.GetInputs().get(0);
        String var = input.GetInputVariables().get(0).GetName().getValue();
        StringBuilder builder = new StringBuilder();
        builder.append(FunctionStaticStrings.Case).append(" ").append(var).append(":");
        return builder.toString();
    }

    private String GetMethodLineWithSwitchFunction(IMethodLine line)
    {
        IFunctionInput input = line.GetInputs().get(0);
        String var = input.GetInputVariables().get(0).GetName().getValue();
        StringBuilder builder = new StringBuilder();
        builder.append(FunctionStaticStrings.Switch);
        builder.append("(").append(var).append(")");
        return builder.toString();
    }

    private String GetMethodLineWithElseFunction(IMethodLine line)
    {
        return FunctionStaticStrings.Else;
    }

    private String GetMethodLineWithIfFunction(IMethodLine line)
    {
        StringBuilder builder = new StringBuilder();
        builder.append(FunctionStaticStrings.If);
        builder.append("(");
        IFunctionInput input = line.GetInputs().get(0);
        builder.append(this.GetIFunctionInputThatMightUseBooleanOperators(input));
        builder.append(")");
        return builder.toString();
    }

    private String GetMethodLineWithReturnFunction(IMethodLine line)
    {
        StringBuilder builder = new StringBuilder();
        builder.append(FunctionStaticStrings.Return).append(" ");
        String var = line.GetInputs().get(0).GetInputVariables().get(0).GetName().getValue();
        builder.append(var);
        builder.append(";");
        return builder.toString();
    }

    private String GetMethodLineWithOtherFunctions(IMethodLine line)
    {
        String output = line.GetOutputVariable().GetName().getValue();
        String Function = line.GetFunction().GetFunction().getValue();
        IFunctionInput input = line.GetInputs().get(0);
        String func = input.GetFunction().GetFunction().getValue();
        List<IVariable> vars = input.GetInputVariables();
        StringBuilder builder = new StringBuilder();
        builder.append(output);
        if ((false == output.equals("")) && (false == FunctionStaticStrings.IsUniOperator(Function)))
        {
            builder.append("=");
        }
        builder.append(Function);
        if (Function.endsWith(")"))
        {
            builder.append(";");
        } else
        {
            builder.append("(");
            if (false == FunctionMarkerInFunctionInput.IsMarker(func)
                    && false == FunctionStaticStrings.IsUniOperator(Function))
            {
                builder.append(func).append("(");
            }
            for (int index = 0; index < vars.size(); index++)
            {
                String var = vars.get(index).GetName().getValue();
                builder.append(var);
                if (index < vars.size() - 1)
                {
                    builder.append(",");
                }
            }
            if (false == FunctionMarkerInFunctionInput.IsMarker(func)
                    && false == FunctionStaticStrings.IsUniOperator(Function))
            {
                builder.append(")");
            }
            builder.append(")").append(";");
        }
        return builder.toString();
    }

    private String GetMethodLineWithNewFunction(IMethodLine line)
    {
        StringBuilder builder = new StringBuilder();
        String output = line.GetOutputVariable().GetName().getValue();
        IFunctionInput typeAndName = line.GetInputs().get(0);
        String type = typeAndName.GetInputVariables().get(0).GetName().getValue();
        builder.append(output).append("=").append(FunctionStaticStrings.New).append(" ").append(type).append("(");
        if (typeAndName.GetInputVariables().size() > 1)
        {
            for (int index = 1; index < typeAndName.GetInputVariables().size(); index++)
            {
                String var = typeAndName.GetInputVariables().get(index).GetName().getValue();
                builder.append(var);
                if (index < typeAndName.GetInputVariables().size() - 1)
                {
                    builder.append(",");
                }
            }
        }
        builder.append(")").append(";");
        return builder.toString();
    }

    private String GetMethodLineWithNewVariableFunction(IMethodLine line)
    {
        IFunctionInput input = line.GetInputs().get(0);

        String type = input.GetInputVariables().get(0).GetName().getValue();
        String name = input.GetInputVariables().get(1).GetName().getValue();
        if (input.GetFunction().equals(FunctionMarkerInFunctionInput.TypeAndValue))
        {
            return type + " " + name + ";";
        } else
        {
            String value = input.GetInputVariables().get(2).GetName().getValue();
            return type + " " + name + " = " + value + ";";
        }

    }

    private StringBuilder AppendLeftCurveBrackets(StringBuilder builder, int number)
    {
        return this.AppendItems(builder, "{", number);
    }

    private StringBuilder AppendRightCurveBrackets(StringBuilder builder, int number)
    {
        return this.AppendItems(builder, "}", number);
    }

    private StringBuilder AppendItems(StringBuilder builder, String item, int number)
    {
        if (number == 0)
        {
            return builder;
        }
        for (int index = 0; index < number; index++)
        {
            builder.append(item);
        }
        return builder;
    }

    private int DifferentItemsInSet2WhichNotExistInSet1(List<ILineLabel> set1, List<ILineLabel> set2)
    {
        int count = 0;
        for (ILineLabel label : set2)
        {
            String labelstring = label.GetName().getValue();
            for (ILineLabel l : set1)
            {
                String lstring = l.GetName().getValue();
                if (lstring.equals(labelstring) == false)
                {
                    count += 1;
                }
            }
        }
        return count;
    }

}
