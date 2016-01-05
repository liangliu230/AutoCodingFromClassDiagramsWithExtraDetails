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
import Model.interfaces.IVisibility;
import Util.interfaces.ISaveToXML;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
/**
 *
 * @author Liu
 */
public class SaveToXMLTest
{
    IFullName _Pac_Name = new FullName(new IName[]{new Name("pac1"),new Name("sub1")}, new Name("name"));
    double[] _Pac_position = new double[]{1.0,2.0};
    IPackage _Pac = new Model.Model.Package(_Pac_Name,true, _Pac_position[0], _Pac_position[1]);
    
    IFullName _Cls_Name = new FullName(new IName[]{new Name("pac1"),new Name("sub1"),new Name("name")}, new Name("cls"));
    IVisibility _cls_Visibility = new Visibility(StaticStrings.Public);
    IModifiers _cls_Modifiers = new Modifiers(false, false, true);
    
    IFullName _OutputType = new FullName(new IName[0], new Name("int"));
    IClass _Class;
    
    List<IMethod> _cls_methods = new ArrayList<>();
    
    String _Method_no_parameter_Name = "no_parameter_abstract_method";
    IVisibility _Mthd_Visibility = new Visibility(StaticStrings.Public);
    IModifiers _mthd_Modifiers = new Modifiers(false, false, false);
    boolean _Mthd_isStrictOrder = false;
    List<IParameter> _Method_InputList = new ArrayList<>();
    List<IMethodLine> _MethodBody = new ArrayList<>();
    List<IFunctionInput> _Annotations = new ArrayList<>();
    IFunctionInput _OverrideAnnotation = new FunctionInput(new Function("Override"), new ArrayList<>());
    IParameter _Parameter = new Parameter("pname", new FullName(new IName[]{new Name("java"), new Name("util")}, new Name("List<Integer>")));
    IFunction _Function_1 = new Function("throw");
    IVariable _Function_1_out = new Variable();
    List<IFunctionInput> _Function_1_inputList = new ArrayList<>();
    List<IVariable> _Function_1_input_varList = new ArrayList<>();
    IVariable _Function_1_input_var = new Variable("var");
    String Function_1_name = "UnsupportedOperation";
    IFunction _Function_1_input_function = new Function(Function_1_name);
    List<ILineLabel> _Function_1_LineLabels = new ArrayList<>();
    ILineLabel _LineLabel = new LineLabel("ll");
    ILineNumber _Function_1_LineNumber = new LineNumber(0);
    IMethodLine _MethodLine;
    IMethod _Method;
    
    List<IField> _Fields = new ArrayList<>();
    IName _FieldName = new Name("field");
    IFullName _FieldType = new FullName(new IName[0], new Name("int"));
    IVisibility _FieldVisibility = Visibility.Private;
    IFunctionInput _FieldDefaultValue = new FunctionInput(new Function(), new ArrayList<>());
    IModifiers _FieldModifiers = new Modifiers(false, false, false);
    IField _Field = new Field(_FieldName, _OutputType, _FieldModifiers, _FieldVisibility, _FieldDefaultValue);
    
    IFullName _cls_extends_1 = new FullName(new IName[]{new Name("javafx"), new Name("scene")}, new Name("*"));
    List<IFullName> _cls_extendsList = new ArrayList<>();
    List<IFullName> _Cls_implementsList = new ArrayList<>();
    
    double[] _cls_pos = new double[]{3,4};

    public SaveToXMLTest()
    {
       
        _cls_extendsList.add(_cls_extends_1);
        _Fields.add(_Field);
        _Function_1_LineLabels.add(_LineLabel);
        _Function_1_input_varList.add(_Function_1_input_var);
        IFunctionInput _Function_1_functioninput = new FunctionInput(_Function_1_input_function, _Function_1_input_varList);
        _Function_1_inputList.add(_Function_1_functioninput);
        _MethodLine = new MethodLine(_Function_1, _Function_1_out, _Function_1_inputList, _Function_1_LineLabels, _Function_1_LineNumber);
        _MethodBody.add(_MethodLine);
        _Method_InputList.add(_Parameter);
        _Annotations.add(_OverrideAnnotation);
        _Method = new Method(new Name(_Method_no_parameter_Name), _OutputType, _Method_InputList, _MethodBody, _Annotations, _Mthd_Visibility, _mthd_Modifiers, _Mthd_isStrictOrder, new ArrayList<>());
        _cls_methods.add(_Method);
        _Class = new ACClass(_Cls_Name, _cls_Visibility, _cls_Modifiers, _cls_methods, _cls_extendsList, _Fields, _Cls_implementsList, false, false,true, _cls_pos[0], _cls_pos[1]);
    }

    @Test
    public void testSave()
    {
        try{
        String saveFile = "./TestSaveFile.xml";
        ISaveToXML save = new SaveToXML();
        List<IPackage> plist = new ArrayList<>();
        plist.add(_Pac);
        List<IClass> clist= new ArrayList<>();
        clist.add(_Class);
        String projectName="abc";
        IProject project = new Project(new Name(projectName), clist, plist);
        
            save.Save(project, saveFile);
            String schemaFile = "./schema.xsd";
            XMLSchemaValidator.ValidateXMLDocument(saveFile, schemaFile);
            
        }
        catch(Exception exception)
        {
            exception.printStackTrace(System.out);
            Assert.fail("fail because of exception");
        }
                
        
    }
    
}
