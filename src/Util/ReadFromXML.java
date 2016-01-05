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

import Model.Model.ACClass;
import Model.Model.Field;
import Model.Model.FullName;
import Model.Model.Function;
import Model.Model.FunctionInput;
import Model.Model.LineLabel;
import Model.Model.LineNumber;
import Model.Model.Method;
import Model.Model.MethodLine;
import Model.Model.Modifiers;
import Model.Model.Name;
import Model.Model.Parameter;
import Model.Model.Project;
import Model.Model.Variable;
import Model.Model.Visibility;
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
import Model.interfaces.IName;
import Model.interfaces.IPackage;
import Model.interfaces.IParameter;
import Model.interfaces.IProject;
import Model.interfaces.IVariable;
import Util.interfaces.IReadFromXML;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Liu
 */
public class ReadFromXML implements IReadFromXML
{

    @Override
    public IProject Read(String file, String schemaFile) throws Exception
    {
        Path filePath = Paths.get(file);
        Path schemaPath = Paths.get(schemaFile);

        if (false == Files.isReadable(filePath))
        {
            throw new FileNotFoundException(file);
        }
        
        if (false == Files.isReadable(schemaPath))
        {
            throw new FileNotFoundException(schemaFile);
        }
        
        XMLSchemaValidator.ValidateXMLDocument(file, schemaFile);

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(file);       

        Element rootNode = document.getDocumentElement();

        NodeList packagesNodeList = rootNode.getElementsByTagName(StaticStrings.Packages);
        Node packagesNode = packagesNodeList.item(0);
        NodeList classesNodeList = rootNode.getElementsByTagName(StaticStrings.Classes);
        Node classesNode = classesNodeList.item(0);

        List<IPackage> packageList = this.GetPackages(packagesNode);
        List<IClass> classList = this.GetClasses(classesNode);
        String name = this.GetAttributeWithName(rootNode, StaticStrings.Name);
        IName n = new Model.Model.Name(name);
        return new Project(n, classList, packageList);
    }
    
    private List<IPackage> GetPackages(Node packages)
    {
        List<Node> pList = this.GetNodesWithNodeName(packages, StaticStrings.Package);
        List<IPackage> list = new ArrayList<>();
        for (Node n : pList)
        {
            IPackage pkg = this.GetPackage(n);
            list.add(pkg);
        }
        return list;
    }

    private IPackage GetPackage(Node pkg)
    {
        Node packageName = this.GetFirstNode(pkg, StaticStrings.Name);
        Node layout = this.GetFirstNode(pkg, StaticStrings.Position);
        IFullName name = this.GetFullName(packageName);
        double[] pos = this.GetLayout(layout);
        String isInitializestr = this.GetAttributeWithName(pkg, StaticStrings.IsInitialize);
        boolean isInitialize = Boolean.valueOf(isInitializestr);
        IPackage package1 = new Model.Model.Package(name, isInitialize, pos[0], pos[1]);
        return package1;
    }

    private IFullName GetFullName(Node name)
    {
        String fullnm = name.getTextContent();
        System.out.println(fullnm);
        String[] arr = this.SplitWithDot(fullnm);
        if (arr.length <= 1)
        {
            return new FullName(new IName[0], this.GetNameFromString(arr[0]));
        }

        IName[] names = new IName[arr.length - 1];
        for (int index = 0; index < arr.length - 1; index++)
        {
            names[index] = this.GetNameFromString(arr[index]);
        }
        IName n = this.GetNameFromString(arr[arr.length - 1]);
        return new FullName(names, n);
    }

    private IName GetNameFromString(String name)
    {
        return new Name(name);
    }

    private double[] GetLayout(Node layout)
    {
        Node X = this.GetFirstNode(layout, StaticStrings.X);
        Node Y = this.GetFirstNode(layout, StaticStrings.Y);
        double posX = Double.valueOf(X.getTextContent());
        double posY = Double.valueOf(Y.getTextContent());
        return new double[]
        {
            posX, posY
        };
    }

    private List<IClass> GetClasses(Node classes)
    {
        List<Node> nodes = this.GetNodesWithNodeName(classes, StaticStrings.ClassObject);
        List<IClass> list = new ArrayList<>();
        for (Node n : nodes)
        {
            IClass cls = this.GetClass(n);
            list.add(cls);
        }
        return list;
    }

    private List<IFullName> GetFullNames(Node node, String childNodeName)
    {
        List<Node> nodes = this.GetNodesWithNodeName(node, childNodeName);
        List<IFullName> list = new ArrayList<>();
        for (Node n : nodes)
        {
            IFullName fn = this.GetFullName(node);
            list.add(fn);
        }
        return list;
    }

    private List<IField> GetFields(Node node)
    {
        List<Node> nodes = this.GetNodesWithNodeName(node, StaticStrings.Field);
        List<IField> list = new ArrayList<>();
        for (Node n : nodes)
        {
            IField f = this.GetField(n);
            list.add(f);
        }
        return list;
    }

    private List<IMethod> GetMethods(Node node)
    {
        List<Node> nodes = this.GetNodesWithNodeName(node, StaticStrings.Method);
        List<IMethod> list = new ArrayList<>();
        for (Node n : nodes)
        {
            IMethod m = this.GetMethod(n);
            list.add(m);
        }
        return list;
    }

    private IClass GetClass(Node cls)
    {
        Node nameNode = this.GetFirstNode(cls, StaticStrings.Name);
        Node extendsNode = this.GetFirstNode(cls, StaticStrings.Extends);
        Node implementsNode = this.GetFirstNode(cls, StaticStrings.Implements);
        Node fieldsNode = this.GetFirstNode(cls, StaticStrings.Fields);
        Node methodsNode = this.GetFirstNode(cls, StaticStrings.Methods);
        Node positionNode = this.GetFirstNode(cls, StaticStrings.Position);
        IFullName clsName = this.GetFullName(nameNode);
        List<IFullName> extendsList = this.GetFullNames(extendsNode, StaticStrings.Extends);
        List<IFullName> implementsList = this.GetFullNames(implementsNode, StaticStrings.Implements);
        List<IField> fieldList = this.GetFields(fieldsNode);
        List<IMethod> methodList = this.GetMethods(methodsNode);
        double[] pos = this.GetLayout(positionNode);
        IModifiers modifiers = this.GetModifiers(cls);
        boolean isInterface = this.GetIsInterface(cls);
        boolean isEnum = this.GetIsEnum(cls);
        ObservableList<IFullName> exl = FXCollections.observableArrayList(extendsList);
        ListProperty<IFullName> exs = new SimpleListProperty<>(exl);
        ObservableList<IFullName> iml = FXCollections.observableArrayList(implementsList);
        ListProperty<IFullName> ims = new SimpleListProperty<>(iml);
        ObservableList<IField> fl = FXCollections.observableArrayList(fieldList);
        ListProperty<IField> fs = new SimpleListProperty<>(fl);
        ObservableList<IMethod> ml = FXCollections.observableArrayList(methodList);
        ListProperty<IMethod> ms = new SimpleListProperty<>(ml);
        Visibility v = this.GetVisibility(cls);
        return new ACClass(clsName, v, modifiers, ms, exs, fs, ims, isInterface, isEnum,true, pos[0], pos[1]);
    }

    private IField GetField(Node field)
    {
        Node nameNode = this.GetFirstNode(field, StaticStrings.Name);
        Node typeNode = this.GetFirstNode(field, StaticStrings.Type);
        Node defaultValueNode;
        IFunctionInput defaultValue;
        try
        {
            defaultValueNode = this.GetFirstNode(field, StaticStrings.DefaultValue);
            defaultValue = this.GetDefaultValue(defaultValueNode);
        } catch (IndexOutOfBoundsException ex)
        {
            defaultValue = new FunctionInput(new Function(), new ArrayList<>());
        }

        IName name = this.GetName(nameNode);
        IFullName type = this.GetFullName(typeNode);
        IModifiers modifiers = this.GetModifiers(field);
        Visibility v = this.GetVisibility(field);
        return new Field(name, type, modifiers, v, defaultValue);
    }

    private IModifiers GetModifiers(Node cls)
    {

        String isAbstractString;
        try
        {
            isAbstractString = this.GetAttributeWithName(cls, StaticStrings.IsAbstract);
        } catch (NullPointerException e)
        {
            isAbstractString = "false";
        }
        boolean isAbstract = Boolean.valueOf(isAbstractString);
        String isFinalString;
        try
        {
            isFinalString = this.GetAttributeWithName(cls, StaticStrings.IsFinal);
        } catch (NullPointerException e)
        {
            isFinalString = "false";
        }
        boolean isFinal = Boolean.valueOf(isFinalString);
        String isStaticString;
        try
        {
            isStaticString = this.GetAttributeWithName(cls, StaticStrings.IsStatic);
        } catch (NullPointerException ex)
        {
            isStaticString = "false";
        }
        boolean isStatic = Boolean.valueOf(isStaticString);
        return new Modifiers(isAbstract, isStatic, isFinal);
    }

    private String GetAttributeWithName(Node node, String attributeName)
    {
        NamedNodeMap nnm = node.getAttributes();
        Node n = nnm.getNamedItem(attributeName);
        return n.getTextContent();
    }

    private Visibility GetVisibility(Node cls)
    {
        String visibilityString;
        try
        {
            visibilityString = this.GetAttributeWithName(cls, StaticStrings.Visibility);
        } catch (NullPointerException e)
        {
            visibilityString = "exception";
        }

        switch (visibilityString)
        {
            case StaticStrings.Public:
            default:
                return new Visibility(StaticStrings.Public);
            case StaticStrings.Private:
                return new Visibility(StaticStrings.Private);
            case StaticStrings.Protected:
                return new Visibility(StaticStrings.Protected);
            case StaticStrings.Package:
                return new Visibility(StaticStrings.Package);
        }
    }

    private boolean GetIsInterface(Node cls)
    {
        String isInterfaceString = this.GetAttributeWithName(cls, StaticStrings.IsInterface);
        return Boolean.valueOf(isInterfaceString);
    }

    private boolean GetIsEnum(Node cls)
    {
        String str = this.GetAttributeWithName(cls, StaticStrings.IsEnum);
        return Boolean.valueOf(str);
    }

    private IFunctionInput GetDefaultValue(Node defaultValue)
    {
        Node funcinputNode = this.GetFirstNode(defaultValue, StaticStrings.FunctionInput);
        return this.GetFunctionInput(funcinputNode);
    }

    private IFunction GetEmptyFunction()
    {
        return new Function();
    }

    private IFunctionInput GetFunctionInput(Node functionInput)
    {
        IFunction function;
        try
        {
            Node funcNode = this.GetFirstNode(functionInput, StaticStrings.Function);
            function = this.GetFunction(funcNode);
        } catch (IndexOutOfBoundsException exception)
        {
            function = this.GetEmptyFunction();
        }
        Node inputsNode = this.GetFirstNode(functionInput, StaticStrings.Inputs);

        List<IVariable> variables = this.GetVariables(inputsNode);
        return new FunctionInput(function, variables);
    }

    private IName GetName(Node name)
    {
        String str = name.getTextContent();
        return new Name(str);
    }

    private List<IFunctionInput> GetFunctionInputs(Node node)
    {
        List<Node> nodes = this.GetNodesWithNodeName(node, StaticStrings.FunctionInput);
        List<IFunctionInput> list = new ArrayList<>();
        for (Node n : nodes)
        {
            IFunctionInput f = this.GetFunctionInput(n);
            list.add(f);
        }
        return list;
    }

    private List<IParameter> GetMethodInputs(Node node)
    {
        List<Node> nodes = this.GetNodesWithNodeName(node, StaticStrings.MethodInput);
        List<IParameter> list = new ArrayList<>();
        for (Node n : nodes)
        {
            IParameter p = this.GetMethodInput(n);
            list.add(p);
        }
        return list;
    }

    private List<IMethodLine> GetMethodLines(Node node)
    {
        List<Node> nodes = this.GetNodesWithNodeName(node, StaticStrings.MethodLine);
        List<IMethodLine> list = new ArrayList<>();
        for (Node n : nodes)
        {
            IMethodLine line = this.GetMethodLine(n);
            list.add(line);
        }
        return list;
    }
    
    private List<IFullName> GetThrows(Node _throws )
    {
        List<Node> nl = this.GetNodesWithNodeName(_throws, StaticStrings.Name);
        List<IFullName> list = new ArrayList<>();
        for(Node n : nl)
        {
            IFullName fn = this.GetFullName(n);
            list.add(fn);
        }
        return list;
    }
    private IMethod GetMethod(Node method)
    {
        IModifiers modifiers = this.GetModifiers(method);
        Visibility visibility = this.GetVisibility(method);
        boolean isStrictOrder = this.GetIsStrictOrider(method);
        Node nameNode = this.GetFirstNode(method, StaticStrings.Name);
        Node typeNode;
        IFullName type;
        try
        {
            typeNode = this.GetFirstNode(method, StaticStrings.Type);
            type = this.GetFullName(typeNode);
        } catch (NullPointerException | IndexOutOfBoundsException e)
        {
            type = new FullName();
        }
        Node annotationsNode = this.GetFirstNode(method, StaticStrings.Annotations);
        Node parametersNode = this.GetFirstNode(method, StaticStrings.MethodInputs);
        Node bodyNode = this.GetFirstNode(method, StaticStrings.MethodLines);
        IName name = this.GetName(nameNode);
        List<IFunctionInput> annotations = this.GetFunctionInputs(annotationsNode);
        List<IParameter> methodInputs = this.GetMethodInputs(parametersNode);
        List<IMethodLine> body = this.GetMethodLines(bodyNode);
        Node throwsNode = this.GetFirstNode(method, StaticStrings.Exceptions);
        List<IFullName> _throws = this.GetThrows(throwsNode);
        return new Method(name, type, methodInputs, body, annotations, visibility, modifiers, isStrictOrder, _throws);
    }

    private IParameter GetMethodInput(Node methodInput)
    {
        Node nameNode = this.GetFirstNode(methodInput, StaticStrings.Name);
        IName name = this.GetName(nameNode);

        IFullName type;
        try
        {
            Node typeNode = this.GetFirstNode(nameNode, StaticStrings.Type);
            type = this.GetFullName(typeNode);
        } catch (IndexOutOfBoundsException exception)
        {
            type = new FullName();
        }

        return new Parameter(name.GetName().getValue(), type);
    }

    private boolean GetIsStrictOrider(Node method)
    {
        String str = this.GetAttributeWithName(method, StaticStrings.IsStrictOrder);
        return Boolean.valueOf(str);
    }

    private IMethodLine GetMethodLine(Node methodLine)
    {
        Node funcNode = this.GetFirstNode(methodLine, StaticStrings.Function);
        Node outputNode = this.GetFirstNode(methodLine, StaticStrings.OutputVariable);
        Node inputsNode = this.GetFirstNode(methodLine, StaticStrings.FunctionInputs);
        Node lineNumberNode = this.GetFirstNode(methodLine, StaticStrings.LineNumber);
        Node lineLabelsNode = this.GetFirstNode(methodLine, StaticStrings.LineLabels);
        IFunction function = this.GetFunction(funcNode);
        IVariable variable = this.GetVariable(outputNode);
        ILineNumber lineNumber = this.GetLineNumber(lineNumberNode);
        List<ILineLabel> lineLabels = this.GetLineLabels(lineLabelsNode);
        List<IFunctionInput> functionInputs = this.GetFunctionInputs(inputsNode);
        return new MethodLine(function, variable, functionInputs, lineLabels, lineNumber);
    }

    private IFunction GetFunction(Node function)
    {
        String content = function.getTextContent();
        return new Function(content);
    }

    private List<IVariable> GetVariables(Node node)
    {
        List<Node> nodes = this.GetNodesWithNodeName(node, StaticStrings.Input);
        List<IVariable> list = new ArrayList<>();
        for (Node n : nodes)
        {
            IVariable variable = this.GetVariable(n);
            list.add(variable);
        }
        return list;
    }

    private IVariable GetVariable(Node variable)
    {
        String content = variable.getTextContent();
        return new Variable(content);
    }

    private List<ILineLabel> GetLineLabels(Node lineLabels)
    {
        List<Node> labels = this.GetNodesWithNodeName(lineLabels, StaticStrings.LineLabel);
        List<ILineLabel> list = new ArrayList<>();
        for (Node n : labels)
        {
            ILineLabel label = this.GetLineLabel(n);
            list.add(label);
        }
        return list;
    }

    private ILineLabel GetLineLabel(Node lineLabel)
    {
        String str = lineLabel.getTextContent();
        return new LineLabel(str);
    }

    private ILineNumber GetLineNumber(Node lineNumber)
    {
        String str = lineNumber.getTextContent();
        if (str.equals(""))
        {
            return new LineNumber(-1);
        }
        int number = Integer.valueOf(str);
        return new LineNumber(number);
    }

    private Node GetFirstNode(Node node, String childNodeName)
    {
        List<Node> list = this.GetNodesWithNodeName(node, childNodeName);
        return list.get(0);
    }

    private List<Node> GetNodesWithNodeName(Node node, String nodeName)
    {
        List<Node> list = new ArrayList<>();
        NodeList nodeList = node.getChildNodes();
        for (int index = 0; index < nodeList.getLength(); index++)
        {
            Node n = nodeList.item(index);
            if (n.getNodeName().equals(nodeName))
            {
                list.add(n);
            }
        }
        return list;
    }

    private String[] SplitWithDot(String str)
    {
        return str.split("\\.");
    }

}
