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
package Translation;

import Model.ACClass;
import Model.Field;
import Model.InputParameter;
import Model.Method;
import Model.MethodLine;
import Model.MethodLineFunctions;
import Model.StaticStrings;
import Model.Project;
import Utility.FileSave;
import View.FlowChartFunctions;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Liu
 */
public class TranslateToJava
{

    public void Translate(Project project, String savePath) throws Exception
    {
        for (ACClass cls : project.GetClasses())
        {
            String clsName = cls.GetName().getValue();
            Path path = this.GetSavePath(savePath, clsName);
            List<String> fileContent = new ArrayList<>();
            String pkgName = this.GetPackageName(clsName);
            String pkgLine = this.GetPackage(pkgName);
            fileContent.add(pkgLine);
            String clsDeclaration = this.GetClassDeclaration(cls);
            fileContent.add(clsDeclaration);
            fileContent.add("{");
            for (Field field : cls.GetFields())
            {
                String fld = this.GetField(field);
                fileContent.add(fld);
            }
            for (Method mthd : cls.GetMethods())
            {
                String mthdDeclaration = this.GetMethodDeclaration(mthd);
                fileContent.add(mthdDeclaration);
                if (cls.IsInterface().getValue() == true)
                {
                    fileContent.add(";");
                } else
                {
                    fileContent.add("{");
                    List<MethodLine> list = mthd.GetMethodBody();
                    
                    for (int index = 0; index < list.size(); index++)
                    {
                        int previous = index - 1;
                        int afterwards = index + 1;
                        MethodLine current = list.get(index);
                        if (current.GetFunctionType().getValue().equals(FlowChartFunctions.LoopEnd))
                        {
                            continue;
                        }
                        if (current.GetFunctionType().getValue().equals(FlowChartFunctions.START)
                                || current.GetFunctionType().getValue().equals(FlowChartFunctions.END))
                        {
                            continue;
                        }
                        List<String> currentLineLabels = current.GetLineLabels();
                        List<String> after, prev;
                        if (previous < 0)
                        {
                            prev = new ArrayList<>();
                        } else
                        {
                            prev = list.get(previous).GetLineLabels();
                        }
                        if (afterwards >= list.size())
                        {
                            after = new ArrayList<>();
                        } else
                        {
                            after = list.get(afterwards).GetLineLabels();
                        }
                        int leftBracketNumber = this.GetHowManyItemsInSet2NotContainInSet1(prev, currentLineLabels);
                        int rightBracketNumber = this.GetHowManyItemsInSet2NotContainInSet1(after, currentLineLabels);
                        String leftBrackets = this.GetDuplicatedStrings(leftBracketNumber, "{");
                        String line = this.GetMethodLine(current);
                        String rightBrackets = this.GetDuplicatedStrings(rightBracketNumber, "}");

                        fileContent.add(line);
                        fileContent.add(leftBrackets);
                        fileContent.add(rightBrackets);
                    }
                    fileContent.add("}");
                }
            }
            fileContent.add("}");
            Path pkg = this.GetSaveFolderPath(savePath, pkgName);
            this.SaveFile(pkg, path, fileContent);
        }
    }

    Path GetSaveFolderPath(String baseSavePath, String pkgName)
    {
        String pkgPath = pkgName.replace(".", "/");
        pkgPath += "/";
        String bsPath = baseSavePath.replace("\\", "/");
        if (bsPath.endsWith("/") == false)
        {
            bsPath += "/";
        }
        return Paths.get(bsPath, pkgPath);
    }

    Path GetSavePath(String baseSavePath, String clsName)
    {
        String clsPath = clsName.replace(".", "/");
        clsPath += ".java";
        String bsPath = baseSavePath.replace("\\", "/");
        if (bsPath.endsWith("/") == false)
        {
            bsPath += "/";
        }
        clsPath = bsPath + clsPath;
        return Paths.get(clsPath);
    }

    void SaveFile(Path folders, Path savePath, List<String> content) throws Exception
    {
        FileSave.Save(folders, savePath, content);
    }

    String GetPackage(String packageName)
    {
        return StaticStrings.Package + " " + packageName + ";";
    }

    String GetPackageName(String clsName)
    {
        return clsName.substring(0, clsName.lastIndexOf("."));
    }

    String GetClassDeclaration(ACClass cls)
    {
        StringBuilder builder = new StringBuilder();
        builder.append(this.GetAnnotations(cls.GetAnnotations()));
        if (cls.GetVisibility().getValue().equals(StaticStrings.Package) == false)
        {
            builder.append(cls.GetVisibility().getValue()).append(" ");
        }
        if (cls.isAbstract().getValue())
        {
            builder.append(StaticStrings.Abstract).append(" ");
        }
        if (cls.isFinal().getValue())
        {
            builder.append(StaticStrings.Final).append(" ");
        }
        if (cls.IsInterface().getValue())
        {
            builder.append(StaticStrings.Interface).append(" ");
        } else if (cls.IsEnum().getValue())
        {
            builder.append(StaticStrings.Enum).append(" ");
        } else
        {
            builder.append(StaticStrings.Class).append(" ");
        }
        String fullName = cls.GetName().getValue();
        int lastindex = fullName.lastIndexOf(".");
        if (lastindex != -1)
        {
            builder.append(fullName.substring(fullName.lastIndexOf(".") + 1)).append(" ");
        } else
        {
            builder.append(fullName).append(" ");
        }
        if (cls.GetExtends().isEmpty() == false && cls.IsInterface().getValue() == false)
        {
            builder.append(this.GetExtends(cls.GetExtends())).append(" ");
        }
        if (cls.GetImplements().isEmpty() == false)
        {
            if (cls.IsInterface().getValue())
            {
                builder.append(this.GetExtends(cls.GetImplements()));
            } else
            {
                builder.append(this.GetImplements(cls.GetImplements()));
            }
        }
        return builder.toString();
    }

    String GetExtends(List<StringProperty> extds)
    {
        return this.ConvertListOfStringToOneString(extds, StaticStrings.Extends);
    }

    String GetImplements(List<StringProperty> imps)
    {
        return this.ConvertListOfStringToOneString(imps, StaticStrings.Implements);
    }

    private String ConvertListOfStringToOneString(List<StringProperty> list, String head)
    {
        StringBuilder builder = new StringBuilder();
        builder.append(head).append(" ");
        boolean addedItem = false;
        for (int index = 0; index < list.size(); index++)
        {

            StringProperty sp = list.get(index);
            if (sp.getValue().equals(""))
            {
                continue;
            } else
            {
                builder.append(sp.getValue());
                if (index < (list.size() - 1))
                {
                    builder.append(",");
                }
                addedItem = true;
            }
        }
        if (addedItem)
        {
            return builder.toString();
        } else
        {
            return "";
        }
    }

    String GetAnnotations(List<StringProperty> annotations)
    {
        StringBuilder builder = new StringBuilder();
        for (StringProperty sp : annotations)
        {
            builder.append("@").append(sp.getValue()).append("\n");
        }
        return builder.toString();
    }

    String GetField(Field field)
    {
        StringBuilder builder = new StringBuilder();
        builder.append(this.GetAnnotations(field.GetAnnotations()));
        if (field.GetVisibility().getValue().equals(StaticStrings.Package) == false)
        {
            builder.append(field.GetVisibility().getValue()).append(" ");
        }
        if (field.isStatic().getValue())
        {
            builder.append(StaticStrings.Static).append(" ");
        }
        if (field.isFinal().getValue())
        {
            builder.append(StaticStrings.Final).append(" ");
        }

        builder.append(field.GetType().getValue()).append(" ");
        builder.append(field.GetName().getValue()).append(" ");
        if (field.GetDefaultValue().getValue().equals("") == false)
        {
            builder.append("= ").append(field.GetDefaultValue().getValue());
        }
        builder.append(";");
        return builder.toString();
    }

    String GetMethodDeclaration(Method method)
    {
        StringBuilder builder = new StringBuilder();
        builder.append(this.GetAnnotations(method.GetAnnotations()));
        if (method.GetVisibility().getValue().equals(StaticStrings.Package) == false)
        {
            builder.append(method.GetVisibility().getValue()).append(" ");
        }
        if (method.isStatic().getValue())
        {
            builder.append(StaticStrings.Static).append(" ");
        }
        if (method.isFinal().getValue())
        {
            builder.append(StaticStrings.Final).append(" ");
        }
        builder.append(method.GetOutputType().getValue()).append(" ");
        builder.append(method.GetName().getValue());
        builder.append("(");
        for (InputParameter ip : method.GetInputParameters())
        {
            builder.append(this.GetInputParameter(ip)).append(",");
        }
        if (method.GetInputParameters().isEmpty() == false)
        {
            builder.deleteCharAt(builder.lastIndexOf(","));
        }
        builder.append(")");
        if (method.IsThrowExceptions().getValue())
        {
            builder.append(StaticStrings.Throws).append(" ").append(StaticStrings.Exception);
        }
        return builder.toString();
    }

    String GetMethodLine(MethodLine methodLine)
    {
        switch (methodLine.GetFunctionType().getValue())
        {
            case MethodLineFunctions.Break:
            case MethodLineFunctions.Continue:
                return methodLine.GetFunctionType().getValue() + ";";
            case MethodLineFunctions.Else:
            case MethodLineFunctions.Try:
            case MethodLineFunctions.Do:
            case MethodLineFunctions.Finally:
                return methodLine.GetFunctionType().getValue();
            case MethodLineFunctions.Default:
                return methodLine.GetFunctionType().getValue() + ":";
            case MethodLineFunctions.Case:
                return methodLine.GetFunctionType().getValue() + " " + methodLine.GetFunctionContent().getValue() + ":";
            case MethodLineFunctions.Switch:
            case MethodLineFunctions.For:
            case MethodLineFunctions.Catch:
            case MethodLineFunctions.While:
            case MethodLineFunctions.If:
                return methodLine.GetFunctionType().getValue() + "( " + methodLine.GetFunctionContent().getValue() + ")";
            case MethodLineFunctions.ForEach:
                return MethodLineFunctions.For + "( " + methodLine.GetFunctionContent().getValue() + ")";
            case MethodLineFunctions.TryWithResources:
                return MethodLineFunctions.Try + "( " + methodLine.GetFunctionContent().getValue() + ")";
            case MethodLineFunctions.Return:
            case MethodLineFunctions.Throw:
                return methodLine.GetFunctionType().getValue() + " " + methodLine.GetFunctionContent().getValue() + ";";
            default: // other
            {
                String outputVariableName = methodLine.GetOutputVariableName().getValue();
                String outputType = methodLine.GetOutputType().getValue();
                String function = methodLine.GetFunctionContent().getValue();
                if (outputVariableName.equals(""))
                {
                    return function + ";";
                } else
                {
                    return outputType + " " + outputVariableName + " = " + function + ";";
                }
            }
        }
    }

    String GetDuplicatedStrings(int number, String str)
    {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < number; i++)
        {
            builder.append(str);
        }
        return builder.toString();
    }

    String GetInputParameter(InputParameter inputParameter)
    {
        return inputParameter.GetType().getValue() + " " + inputParameter.GetName().getValue();
    }

    int GetHowManyItemsInSet2NotContainInSet1(List<String> set1, List<String> set2)
    {
        int count = 0;
        for (String str : set2)
        {
            if (set1.contains(str) == false)
            {
                count += 1;
            }
        }
        return count;
    }
}
