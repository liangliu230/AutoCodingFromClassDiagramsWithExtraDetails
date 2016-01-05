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
package Util;

import Model.StaticStrings;
import Model.interfaces.IClass;
import Model.interfaces.IField;
import Model.interfaces.IFullName;
import Model.interfaces.IFunction;
import Model.interfaces.IFunctionInput;
import Model.interfaces.ILineLabel;
import Model.interfaces.ILineNumber;
import Model.interfaces.IMethod;
import Model.interfaces.IMethodLine;
import Model.interfaces.IModifiers;
import Model.interfaces.IPackage;
import Model.interfaces.IParameter;
import Model.interfaces.IProject;
import Model.interfaces.IVariable;
import Model.interfaces.IVisibility;
import Util.interfaces.ISaveToXML;
import java.io.File;
import java.util.List;
import javafx.beans.property.BooleanProperty;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author Liu
 */
public class SaveToXML implements ISaveToXML
{
    private final Document _Document;
    public SaveToXML() throws Exception
    {
        _Document = this.GetDocument();
    }
    
    @Override
    public void Save(IProject project, String saveFile) throws Exception
    {
        Element rootElement = this.GetRootElement();
        _Document.appendChild(rootElement);
        Element packagesElement = this.GetPackagesElement(project.GetPackages());
        rootElement.appendChild(packagesElement);
        Element classesElement = this.GetClassesElement(project.GetClasses());
        rootElement.appendChild(classesElement);
        String projectName = project.GetName().GetName().getValue();
        rootElement.setAttribute(StaticStrings.Name, projectName);
        this.SaveToFile(saveFile);
    }
    private Element GetRootElement()
    {
        Element root = _Document.createElement(StaticStrings.Project);
        _Document.adoptNode(root);
        
        return root;
    }
    private Element GetPackagesElement(List<IPackage> packages)
    {
        Element packagesElement = _Document.createElement(StaticStrings.Packages);
        for(IPackage pac : packages)
        {
            Element pElement = this.GetPackageElement(pac);
            packagesElement.appendChild(pElement);
        }
        return packagesElement;
    }
    private Element GetPackageElement(IPackage package1)
    {
        Element packageElement = _Document.createElement(StaticStrings.Package);
        Element name = this.GetNameElementFromFullName(package1.GetFullName());
        Element pos = this.GetPositionElement(package1.GetLayoutX().getValue(), package1.GetLayoutY().getValue());
        packageElement.appendChild(name);
        packageElement.appendChild(pos);
        this.SetIsInitialize(packageElement, package1.IsInitialize());
        return packageElement;
    }
    private Element GetNameElementFromFullName(IFullName name)
    {
        Element nameElement = _Document.createElement(StaticStrings.Name);
        nameElement.setTextContent(name.toString());
        return nameElement;
    }
    private Element GetPositionElement(double x, double y)
    {
        Element posElement = _Document.createElement(StaticStrings.Position);
        Element XElement = _Document.createElement(StaticStrings.X);
        Element YElement = _Document.createElement(StaticStrings.Y);
        XElement.setTextContent(String.valueOf(x));
        YElement.setTextContent(String.valueOf(y));
        posElement.appendChild(XElement);
        posElement.appendChild(YElement);
        return posElement;
    }
    private Element GetClassesElement(List<IClass> classes)
    {
        Element classesElement = _Document.createElement(StaticStrings.Classes);
        for(IClass cls : classes)
        {
            Element cElement = this.GetClassElement(cls);
            classesElement.appendChild(cElement);
        }
        return classesElement;
    }
    private Element GetClassElement(IClass cls)
    {
        Element classElement = _Document.createElement(StaticStrings.ClassObject);
        Element nameElement = this.GetNameElementFromFullName(cls.GetFullName());
        Element extendsElement = this.GetExtendsElement(cls.GetExtends());
        Element implementsElement = this.GetImplementsElement(cls.GetImplements());
        Element fieldsElement = this.GetFieldsElement(cls.GetFields());
        Element methodsElement = this.GetMethodsElement(cls.GetMethods());
        classElement.appendChild(nameElement);
        classElement.appendChild(extendsElement);
        classElement.appendChild(implementsElement);
        classElement.appendChild(fieldsElement);
        classElement.appendChild(methodsElement);
        Element posElement = this.GetPositionElement(cls.GetLayoutX().getValue(), cls.GetLayoutY().getValue());
        classElement.appendChild(posElement);
        this.SetModifierAttributes(classElement, cls.GetModifiers());
        this.SetVisibilityAttribute(classElement, cls.GetVisibility());
        this.SetIsInitialize(classElement, cls.IsInitialize());
        String isInterfaceString = String.valueOf(cls.IsInterface().getValue());
        classElement.setAttribute(StaticStrings.IsInterface, isInterfaceString);
        String isEnumString = String.valueOf(cls.IsEnum().getValue());
        classElement.setAttribute(StaticStrings.IsEnum, isEnumString);
        return classElement;
    }
    private void SetIsInitialize(Element element, BooleanProperty isInitialize)
    {
       String isInitializeString = String.valueOf(isInitialize.getValue());
       element.setAttribute(StaticStrings.IsInitialize, isInitializeString);
    }
    private void SetVisibilityAttribute(Element element, IVisibility visibility)
    {
        element.setAttribute(StaticStrings.Visibility, visibility.GetVisibility().getValue());
    }
    private void SetModifierAttributes(Element element, IModifiers modifiers)
    {
        String isAbstract = String.valueOf(modifiers.IsAbstract().getValue());
        String isFinal = String.valueOf(modifiers.IsFinal().getValue());
        String isStatic = String.valueOf(modifiers.IsStatic().getValue());
        element.setAttribute(StaticStrings.IsAbstract, isAbstract);
        element.setAttribute(StaticStrings.IsFinal, isFinal);
        element.setAttribute(StaticStrings.IsStatic, isStatic);
    }
    private Element GetExtendsElement(List<IFullName> extendsList)
    {
        Element extendsElement = _Document.createElement(StaticStrings.Extends);
        for(IFullName exd : extendsList)
        {
            Element element = this.GetNameElementFromFullName(exd);
            extendsElement.appendChild(element);
        }
        return extendsElement;
    }
    private Element GetImplementsElement(List<IFullName> implementsList)
    {
        Element implementsElement = _Document.createElement(StaticStrings.Implements);
        for(IFullName imp : implementsList)
        {
            Element element = this.GetNameElementFromFullName(imp);
            implementsElement.appendChild(element);
        }
        return implementsElement;
    }
    private Element GetFieldsElement(List<IField> fieldList)
    {
        Element fieldsElement = _Document.createElement(StaticStrings.Fields);
        for(IField field : fieldList)
        {
            Element element = this.GetFieldElement(field);
            fieldsElement.appendChild(element);
        }
        return fieldsElement;
    }
    private Element GetFieldElement(IField field)
    {
        Element fieldElement = _Document.createElement(StaticStrings.Field);
        Element nameElement = this.GetNameElementFromString(field.GetName().GetName().getValue());
        Element typeElement = this.GetTypeElement(field.GetType());
        Element defaultValeElement = this.GetDefaultValueElement(field.GetDefaultValue());
        fieldElement.appendChild(nameElement);
        fieldElement.appendChild(typeElement);
        fieldElement.appendChild(defaultValeElement);
        this.SetModifierAttributes(fieldElement, field.GetModifiers());
        this.SetVisibilityAttribute(fieldElement, field.GetVisibility());
        return fieldElement;
    }
    private Element GetTypeElement(IFullName type)
    {
        Element typeElement = _Document.createElement(StaticStrings.Type);
        String str = type.toString();
        typeElement.setTextContent(str);
        return typeElement;
    }
    private Element GetDefaultValueElement(IFunctionInput defaultValue)
    {
        Element defaultValueElement = _Document.createElement(StaticStrings.DefaultValue);
        Element functionInput = this.GetFunctionInputElement(defaultValue);
        defaultValueElement.appendChild(functionInput);
        return defaultValueElement;
    }
    private Element GetMethodsElement(List<IMethod> methodList)
    {
        Element methodsElement = _Document.createElement(StaticStrings.Methods);
        for(IMethod method : methodList)
        {
            Element mElement = this.GetMethodElement(method);
            methodsElement.appendChild(mElement);
        }
        return methodsElement;
    }
    private Element GetMethodElement(IMethod method)
    {
        Element methodElement = _Document.createElement(StaticStrings.Method);
        Element nameElement = this.GetNameElementFromString(method.GetName().getValue());
        Element outputElement = this.GetOutputTypeElement(method.GetOutputType());
        Element methodInputsElement = this.GetMethodInputsElement(method.GetInputParameters());
        Element methodBodyElement = this.GetMethodLinesElement(method.GetMethodBody());
        Element annotationsElement = this.GetAnnotationsElement(method.GetAnnotations());
        methodElement.appendChild(nameElement);
        methodElement.appendChild(outputElement);
        methodElement.appendChild(methodInputsElement);
        methodElement.appendChild(methodBodyElement);
        methodElement.appendChild(annotationsElement);
        Element thrwsElement = this.GetThrowsElement(method.GetThrows());
        methodElement.appendChild(thrwsElement);
        this.SetVisibilityAttribute(methodElement, method.GetVisibility());
        this.SetModifierAttributes(methodElement, method.GetModifiers());
        return methodElement;
    }
    private Element GetThrowsElement(List<IFullName> thrwsList)
    {
        Element thrws = _Document.createElement(StaticStrings.Exceptions);
        for(IFullName fn : thrwsList)
        {
            Element fElement = this.GetNameElementFromFullName(fn);
            thrws.appendChild(fElement);
        }
        return thrws;
    }
    private Element GetOutputTypeElement(IFullName outputType)
    {
        Element outputTypeElement = _Document.createElement(StaticStrings.OutputType);
        outputTypeElement.setTextContent(outputType.toString());
        return outputTypeElement;
    }
    private Element GetMethodInputsElement(List<IParameter> methodInputList)
    {
        Element methodInputsElement = _Document.createElement(StaticStrings.MethodInputs);
        for(IParameter input : methodInputList)
        {
            Element mi = this.GetMethodInputElement(input);
            methodInputsElement.appendChild(mi);
        }
        return methodInputsElement;
    }
    private Element GetMethodInputElement(IParameter methodInput)
    {
        Element input = _Document.createElement(StaticStrings.MethodInput);
        Element name = this.GetNameElementFromString(methodInput.GetName().getValue());
        Element type = this.GetTypeElement(methodInput.GetType());
        input.appendChild(name);
        input.appendChild(type);
        return input;
    }
    private Element GetMethodLinesElement(List<IMethodLine> methodLineList)
    {
        Element methodLines = _Document.createElement(StaticStrings.MethodLines);
        for(IMethodLine ml : methodLineList)
        {
            Element line = this.GetMethodLineElement(ml);
            methodLines.appendChild(line);
        }
        return methodLines;
    }
    private Element GetMethodLineElement(IMethodLine methodLine)
    {
        Element methodLineElement = _Document.createElement(StaticStrings.MethodLine);
        Element functionElement = this.GetFunctionElement(methodLine.GetFunction());
        Element outputVariable = this.GetOutputVariableElement(methodLine.GetOutputVariable());
        Element functionInputsElement = this.GetFunctionInputsElement(methodLine.GetInputs());
        Element lineLabelsElement = this.GetLineLabelsElement(methodLine.GetLineLabels());
        Element lineNumberElement = this.GetLineNumberElement(methodLine.GetLineNumber());
        methodLineElement.appendChild(functionElement);
        methodLineElement.appendChild(outputVariable);
        methodLineElement.appendChild(functionInputsElement);
        methodLineElement.appendChild(lineNumberElement);
        methodLineElement.appendChild(lineLabelsElement);        
        return methodLineElement;
    }
    private Element GetFunctionElement(IFunction function)
    {
        Element fElement = _Document.createElement(StaticStrings.Function);
        fElement.setTextContent(function.GetFunction().getValue());
        return fElement;
    }
    private Element GetOutputVariableElement(IVariable outputVariable)
    {
        Element outputVar = _Document.createElement(StaticStrings.OutputVariable);
        outputVar.setTextContent(outputVariable.GetName().getValue());
        return outputVar;
    }
    private Element GetFunctionInputsElement(List<IFunctionInput> functionInputList)
    {
        Element functionInputsElement = _Document.createElement(StaticStrings.FunctionInputs);
        for(IFunctionInput input : functionInputList)
        {
            Element iElement = this.GetFunctionInputElement(input);
            functionInputsElement.appendChild(iElement);
        }
        return functionInputsElement;
    }
    private Element GetFunctionInputElement(IFunctionInput functionInput)
    {
        Element functionInputElement = _Document.createElement(StaticStrings.FunctionInput);
        Element funcElement = this.GetFuncElement(functionInput.GetFunction());
        Element inputsElement = this.GetInputsElement(functionInput.GetInputVariables());
        functionInputElement.appendChild(funcElement);
        functionInputElement.appendChild(inputsElement);
        return functionInputElement;
    }
    private Element GetFuncElement(IFunction function)
    {
        Element func = _Document.createElement(StaticStrings.Func);
        func.setTextContent(function.GetFunction().getValue());
        return func;
    }
    private Element GetInputsElement(List<IVariable> inputs)
    {
        Element inputsElement = _Document.createElement(StaticStrings.Inputs);
        for(IVariable input : inputs)
        {
            Element iElement = this.GetInputElement(input);
            inputsElement.appendChild(iElement);
        }
        return inputsElement;
    }
    private Element GetInputElement(IVariable input)
    {
        Element iElement = _Document.createElement(StaticStrings.Input);
        iElement.setTextContent(input.GetName().getValue());
        return iElement;
    }
    private Element GetLineNumberElement(ILineNumber lineNumber)
    {
        Element lineNumberElement = _Document.createElement(StaticStrings.LineNumber);
        String num = String.valueOf(lineNumber.GetLineNumber().getValue());
        lineNumberElement.setTextContent(num);
        return lineNumberElement;
    }
    private Element GetLineLabelsElement(List<ILineLabel> lineLabelList)
    {
        Element lineLabelsElement = _Document.createElement(StaticStrings.LineLabels);
        for(ILineLabel ll : lineLabelList)
        {
            Element llElement = this.GetLineLabelElement(ll);
            lineLabelsElement.appendChild(llElement);
        }
        return lineLabelsElement;
    }
    private Element GetLineLabelElement(ILineLabel lineLabel)
    {
        Element element = _Document.createElement(StaticStrings.LineLabel);
        element.setTextContent(lineLabel.GetName().getValue());
        return element;
    }
    private Element GetAnnotationsElement(List<IFunctionInput> annotationList)
    {
        Element annotations  = _Document.createElement(StaticStrings.Annotations);
        for(IFunctionInput input : annotationList)
        {
            Element functionInput = this.GetFunctionInputElement(input);
            annotations.appendChild(functionInput);
        }
        return annotations;
    }
    private Element GetNameElementFromString(String name)
    {
        Element nameElement = _Document.createElement(StaticStrings.Name);
        nameElement.setTextContent(name);
        return nameElement;
    }    
    
    private Document GetDocument() throws Exception
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.newDocument();
    }
    
    private void SaveToFile(String saveFile) throws Exception
    {
        TransformerFactory factory = TransformerFactory.newInstance();        
        Transformer transformer = factory.newTransformer();
        DOMSource source = new DOMSource(_Document);
        File file = new File(saveFile);
        StreamResult result = new StreamResult(file);
        transformer.transform(source, result);
    }
    
}
