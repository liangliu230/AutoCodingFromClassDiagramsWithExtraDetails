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
import Model.interfaces.IName;
import Model.interfaces.IPackage;
import Model.interfaces.IParameter;
import Model.interfaces.IProject;
import Model.interfaces.IVariable;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Liu
 */
public class JavaFileGeneratorTest
{

    String TestPackageString = "TestPackage";
    String InterfacesString = "Interfaces";
    IPackage _TestPackage = new Model.Model.Package(new FullName(new IName[0], new Name(TestPackageString)), true, 0, 0);
    IPackage _InterfacsPackage = new Model.Model.Package(new FullName(
            new IName[]
            {
                new Name(TestPackageString)
            },
            new Name(InterfacesString)), true, 0, 0);
    String _Enum_1 = "enum1";
    String _Enum_2 = "enum2";
    IField _Enum_Field_1 = new Field(new Name(_Enum_1), new FullName(), new Modifiers(), Visibility.Public, new FunctionInput(new Function(), new ArrayList<>()));
    IField _Enum_Field_2 = new Field(new Name(_Enum_2), new FullName(), new Modifiers(), Visibility.Public, new FunctionInput(new Function(), new ArrayList<>()));

    String _Interface_Method_Name = "AddIntToList";
    IMethod _InterfaceMethod;
    IParameter _InterfaceMethodInputParameter = new Parameter("number", new FullName(new IName[0],new Name("int")));
    
    
    IClass _Enum, _Abstract, _Interface, _Class, _DumpClass;
    IProject _Project;
    
    private IFunctionInput GetFunctionInput(String name, IVariable... variables)
    {
        IFunction function = new Function(name);
        List<IVariable> vars = new ArrayList<>();
        for(IVariable var : variables)
        {
            vars.add(var);
        }
        return new FunctionInput(function, vars);
    }
    private List<ILineLabel> GetLineLabels(String... labels)
    {
        List<ILineLabel> list = new ArrayList<>();
        for(String ll : labels)
        {
            ILineLabel l = new LineLabel(ll);
            list.add(l);
        }
        return list;
    }
    private IMethodLine GetMethodLine(String function, String outputVar, int linenumber, List<ILineLabel> labels, IFunctionInput... functionInputs)
    {
        IFunction func = new Function(function);
        IVariable output = new Variable(outputVar);
        List<IFunctionInput> inputs = new ArrayList<>();
        for(IFunctionInput fi : functionInputs)
            inputs.add(fi);
        ILineNumber n = new LineNumber(linenumber);
        return new MethodLine(func, output, inputs, labels, n);
    }

    public JavaFileGeneratorTest()
    {
        IFullName clsExtName = new FullName(new IName[]{new Name("TestPackage")}, new Name("AbstractClass"));
        IFullName clsImpName = new FullName(new IName[]{new Name("TestPackage"),new Name("Interfaces")}, new Name("IAddIntToList"));
        List<IFullName> clsExtList = new ArrayList<>();
        List<IFullName> clsImpList = new ArrayList<>();
        clsExtList.add(clsExtName);
        clsImpList.add(clsImpName);
        IField clsField = new Field(new Name("_List"), new FullName(new IName[]{new Name("java"),new Name("util")}, new Name("List<Integer>")), new Modifiers(false, false, true), Visibility.Private, new FunctionInput(new Function(), new ArrayList<>()));
        List<IField> clsFieldList = new ArrayList<>();
        clsFieldList.add(clsField);
               
        IVariable clsCnstrctrMLInputVar = new Variable("java.util.ArrayList<Integer>");
        List<IVariable> clsCnstrctrMLInputVarList = new ArrayList<>();
        clsCnstrctrMLInputVarList.add(clsCnstrctrMLInputVar);
        IFunctionInput clsCnstrctrMLInput 
                = new FunctionInput(new Function(FunctionMarkerInFunctionInput.TypeAndValue) , clsCnstrctrMLInputVarList);
        
        List<IFunctionInput> clsCnstrctrMLInputList = new ArrayList<>();
        clsCnstrctrMLInputList.add(clsCnstrctrMLInput);        
        IMethodLine clsCnstrctrML = new MethodLine(
                new Function(FunctionStaticStrings.New), 
                new Variable("_List"), 
                clsCnstrctrMLInputList, 
                new ArrayList<ILineLabel>(), 
                new LineNumber(0));
        List<IMethodLine> clsCntrctrMLList = new ArrayList<>();
        clsCntrctrMLList.add(clsCnstrctrML);
        IMethod clsContructor = new Method(
                    new Name("TestClass"), 
                    new FullName(), 
                    new ArrayList<IParameter>(), 
                    clsCntrctrMLList, 
                    new ArrayList<IFunctionInput>(), 
                    Visibility.Public, 
                    new Modifiers(false, false, false), 
                    true, 
                    new ArrayList<>());        
        
        IName clsAddMthdName = new Name("AddIntToList");
        IFullName clsAddMthdOutType = new FullName(new IName[0], new Name(StaticStrings.Void));
        IParameter clsAddMthdInput = new Parameter("number", new FullName(new IName[0], new Name("int")) );
        List<IParameter> clsAddMthdInputll = new ArrayList<>();
        clsAddMthdInputll.add(clsAddMthdInput);
        IVariable clsAddMthdMLInputPrmtr = new Variable("number");
        List<IVariable> clsAddMthdMLInputPList = new ArrayList<>();
        clsAddMthdMLInputPList.add(clsAddMthdMLInputPrmtr);
        IFunctionInput clsAddMthdMLInput = new FunctionInput(new Function(FunctionMarkerInFunctionInput.VariableOnly), clsAddMthdMLInputPList);
        List<IFunctionInput> clsAddMthdMLInputList = new ArrayList<>();
        clsAddMthdMLInputList.add(clsAddMthdMLInput);
        IFunctionInput annotation = new FunctionInput(new Function("Override"), new ArrayList<>());
        List<IFunctionInput> annotationList = new ArrayList<>();
        annotationList.add(annotation);
        IMethodLine clsAddMthML = new MethodLine(new Function("_List.add"), new Variable(), clsAddMthdMLInputList, new ArrayList<ILineLabel>(), new LineNumber());
        List<IMethodLine> clsAddMthdMLList = new ArrayList<>();
        clsAddMthdMLList.add(clsAddMthML);
        IMethod clsAddMthd = new Method(
                clsAddMthdName, 
                clsAddMthdOutType, 
                clsAddMthdInputll, 
                clsAddMthdMLList, 
                annotationList, 
                Visibility.Public, 
                new Modifiers(false, false, false), 
                true, 
                new ArrayList<>());
        
        IName clsPrntMthdName = new Name("print");
        IFullName clsPrntMthdOutput = clsAddMthdOutType;
        IParameter clsPrntMthdParameter = new Parameter("str", new FullName(new IName[0], new Name("String")));
        List<IParameter> clsPrntMthdPList = new ArrayList<>();
        clsPrntMthdPList.add(clsPrntMthdParameter);
        IFullName excptn = new FullName(new IName[0],new Name("UnsupportedOperationException"));
        List<IFullName> clsPrntMthdExcptnList = new ArrayList<>();
        clsPrntMthdExcptnList.add(excptn);
        List<IFunctionInput> clsPrntMthdAnnotationList = new ArrayList<>();
        
        String sysout = "System.out.println";
        String strLength = "str.length()";
        IFunctionInput l0in = this.GetFunctionInput(FunctionStaticStrings.LargerThan, new Variable(strLength), new Variable("2"));
        List<ILineLabel> l0l = this.GetLineLabels("IF_1");
        IMethodLine l0 = this.GetMethodLine(FunctionStaticStrings.If, "", 0, l0l, l0in);
        IFunctionInput l1in = this.GetFunctionInput(FunctionMarkerInFunctionInput.ValueOnly, new Variable("\"larger than 2\""));
        List<ILineLabel> l1l = this.GetLineLabels("IF_1");
        IMethodLine l1 = this.GetMethodLine(sysout, "", 1, l1l, l1in);
        List<ILineLabel> l2l = this.GetLineLabels("ELSE");
        IMethodLine l2 = this.GetMethodLine(FunctionStaticStrings.Else, "", 2, l2l);
        IFunctionInput l3in = this.GetFunctionInput(FunctionMarkerInFunctionInput.ValueOnly, new Variable("\"less or equal to 2\""));
        List<ILineLabel> l3l = this.GetLineLabels("ELSE");
        IMethodLine l3 = this.GetMethodLine(sysout, "", 3, l3l, l3in);
        IFunctionInput l4in = this.GetFunctionInput(FunctionMarkerInFunctionInput.VariableOnly, new Variable("str"));
        List<ILineLabel> l4l = this.GetLineLabels("SW");
        IMethodLine l4 = this.GetMethodLine(FunctionStaticStrings.Switch, "", 4, l4l, l4in);
        IFunctionInput l5in = this.GetFunctionInput(FunctionMarkerInFunctionInput.ValueOnly, new Variable("\"abc\""));
        List<ILineLabel> l5l = this.GetLineLabels("SW");
        IMethodLine l5 = this.GetMethodLine(FunctionStaticStrings.Case, "", 5, l4l, l5in);
        IFunctionInput l6f = this.GetFunctionInput(FunctionMarkerInFunctionInput.VariableOnly, new Variable("\"is abc\""));
        IMethodLine l6 = this.GetMethodLine(sysout, "", 6, l4l, l6f);        
        IMethodLine l7 = this.GetMethodLine(FunctionStaticStrings.Break, "", 7, l4l);
        IFunctionInput l8f = this.GetFunctionInput(FunctionMarkerInFunctionInput.ValueOnly, new Variable("\"def\""));
        List<ILineLabel> l8l = this.GetLineLabels("SW");
        IMethodLine l8 = this.GetMethodLine(FunctionStaticStrings.Case, "", 7, l4l, l8f);
        IFunctionInput l9f = this.GetFunctionInput(FunctionMarkerInFunctionInput.ValueOnly, new Variable("\"is def\""));
        IMethodLine l9 = this.GetMethodLine(sysout, "", 9, l4l, l9f);
        IMethodLine l10 = this.GetMethodLine(FunctionStaticStrings.Break, "", 10, l4l);
        List<ILineLabel> l11l = this.GetLineLabels("SW");
        IMethodLine l11 = this.GetMethodLine(FunctionStaticStrings.Default, "", 11, l4l);
        IFunctionInput l12f = this.GetFunctionInput(FunctionMarkerInFunctionInput.ValueOnly, new Variable("\"other\""));
        IMethodLine l12 = this.GetMethodLine(sysout, "", 12, l4l, l12f);
        IMethodLine l13 = this.GetMethodLine(FunctionStaticStrings.Break, "", 12, l4l);
        IFunctionInput l14f1 = this.GetFunctionInput(FunctionStaticStrings.NewVariable, new Variable("int"),new Variable("index"));
        IFunctionInput l14f2 = this.GetFunctionInput(FunctionStaticStrings.SetValue, new Variable("index"), new Variable("0"));
        IFunctionInput l14f3 = this.GetFunctionInput(FunctionStaticStrings.LessThan, new Variable("index"), new Variable(strLength));
        IFunctionInput l14f4 = this.GetFunctionInput(FunctionStaticStrings.AddValue, new Variable("index"), new Variable("1"));
        List<ILineLabel> l14l = this.GetLineLabels("FR");
        IMethodLine l14 = this.GetMethodLine(FunctionStaticStrings.For, "", 14, l14l, l14f1,l14f2,l14f3,l14f4);
        IFunctionInput l15f = this.GetFunctionInput("str.charAt", new Variable("index"));
        IMethodLine l15 = this.GetMethodLine(sysout, "", 15, l14l, l15f);
        IFunctionInput l16f = this.GetFunctionInput(FunctionMarkerInFunctionInput.TypeAndValue, new Variable("int"),new Variable("wIndex"));
        IMethodLine l16 = this.GetMethodLine(FunctionStaticStrings.NewVariable, "", 16, new ArrayList<>(), l16f);
        IFunctionInput l17f = this.GetFunctionInput(FunctionMarkerInFunctionInput.ValueOnly, new Variable("0"));
        IMethodLine l17 = this.GetMethodLine(FunctionStaticStrings.SetValue, "wIndex", 17, new ArrayList<>(), l17f);
        IFunctionInput l18f = this.GetFunctionInput(FunctionStaticStrings.LessThan, new Variable("wIndex"),new Variable(strLength));
        List<ILineLabel> l18l = this.GetLineLabels("WHL");
        IMethodLine l18 = this.GetMethodLine(FunctionStaticStrings.While, "", 18, l18l, l18f);
        IFunctionInput l19f = this.GetFunctionInput("str.charAt", new Variable("wIndex"));
        IMethodLine l19 = this.GetMethodLine(sysout, "", 19, l18l, l19f);
        IFunctionInput l20f = this.GetFunctionInput(FunctionMarkerInFunctionInput.ValueOnly, new Variable("1"));
        IMethodLine l20 = this.GetMethodLine(FunctionStaticStrings.AddValue, "wIndex", 20, l18l, l20f);
        IFunctionInput l21f1 = this.GetFunctionInput(FunctionMarkerInFunctionInput.TypeAndValue, new Variable("char"),new Variable("ch"));
        IFunctionInput l21f2 = this.GetFunctionInput(FunctionMarkerInFunctionInput.VariableOnly, new Variable("str.toCharArray()"));
        List<ILineLabel> l21l = this.GetLineLabels("FH");
        IMethodLine l21 = this.GetMethodLine(FunctionStaticStrings.ForEach, "", 21, l21l, l21f1,l21f2);
        IFunctionInput l22f = this.GetFunctionInput(FunctionMarkerInFunctionInput.VariableOnly, new Variable("ch"));
        IMethodLine l22 = this.GetMethodLine(sysout, "", 22, l21l, l22f);
        List<ILineLabel> l23l = this.GetLineLabels("TR");
        IMethodLine l23 = this.GetMethodLine(FunctionStaticStrings.Try, "", 23, l23l);
        IFunctionInput l24f = this.GetFunctionInput(FunctionMarkerInFunctionInput.TypeAndValue, new Variable("UnsupportedOperationException"), new Variable("\"not supported yet\""));
        IMethodLine l24 = this.GetMethodLine(FunctionStaticStrings.Throw, "", 24, l23l, l24f);
        IFunctionInput l25f = this.GetFunctionInput(FunctionMarkerInFunctionInput.TypeAndValue, new Variable("UnsupportedOperationException"),new Variable("ex"));
        List<ILineLabel> l25l = this.GetLineLabels("CTH");
        IMethodLine l25 = this.GetMethodLine(FunctionStaticStrings.Catch, "", 25, l25l, l25f);
        IFunctionInput l26f = this.GetFunctionInput(FunctionMarkerInFunctionInput.ValueOnly, new Variable("\"exception catched\""));
        IMethodLine l26 = this.GetMethodLine(sysout, "", 26, l25l, l26f);
        
        List<IMethodLine> clsPrntMthdMLList = new ArrayList<>();
        clsPrntMthdMLList.add(l0);
        clsPrntMthdMLList.add(l1);
        clsPrntMthdMLList.add(l2);
        clsPrntMthdMLList.add(l3);
        clsPrntMthdMLList.add(l4);
        clsPrntMthdMLList.add(l5);
        clsPrntMthdMLList.add(l6);
        clsPrntMthdMLList.add(l7);
        clsPrntMthdMLList.add(l8);
        clsPrntMthdMLList.add(l9);
        clsPrntMthdMLList.add(l10);
        clsPrntMthdMLList.add(l11);
        clsPrntMthdMLList.add(l12);
        clsPrntMthdMLList.add(l13);
        clsPrntMthdMLList.add(l14);
        clsPrntMthdMLList.add(l15);
        clsPrntMthdMLList.add(l16);
        clsPrntMthdMLList.add(l17);
        clsPrntMthdMLList.add(l18);
        clsPrntMthdMLList.add(l19);
        clsPrntMthdMLList.add(l20);
        clsPrntMthdMLList.add(l21);
        clsPrntMthdMLList.add(l22);
        clsPrntMthdMLList.add(l23);
        clsPrntMthdMLList.add(l24);
        clsPrntMthdMLList.add(l25);
        clsPrntMthdMLList.add(l26);
        IMethod clsPrntMethod = new Method(clsPrntMthdName, clsPrntMthdOutput, clsPrntMthdPList, clsPrntMthdMLList, clsPrntMthdAnnotationList, Visibility.Public, new Modifiers(false, true, true), true, clsPrntMthdExcptnList);
        List<IMethod> clsMthdList = new ArrayList<>();
        clsMthdList.add(clsContructor);
        clsMthdList.add(clsAddMthd);
        clsMthdList.add(clsPrntMethod);
        
        _Class = new ACClass(
                new FullName(new IName[]{new Name("TestPackage")}, new Name("TestClass")), 
                Visibility.Public, 
                        new Modifiers(false, false, true), 
                        clsMthdList, 
                        clsExtList, 
                        clsFieldList, 
                        clsImpList, 
                false, false, true, 0, 0);
        
        IVariable abstractClassFieldVariable = new Variable("1.0");
        List<IVariable> abstractClassFieldVariableList = new ArrayList<>();
        abstractClassFieldVariableList.add(abstractClassFieldVariable);
        IField abstractClassField = new Field(new Name("_Version"), new FullName(new IName[0], new Name("double")), new Modifiers(false, true, true), Visibility.Private, new FunctionInput(new Function(FunctionMarkerInFunctionInput.ValueOnly), abstractClassFieldVariableList));
        List<IField> absClsFList = new ArrayList<>();
        absClsFList.add(abstractClassField);
        List<IFunctionInput> abstractClassMethodLineInputList = new ArrayList<>();
        List<IVariable> abstClsVList = new ArrayList<>();
        abstClsVList.add(new Variable("_Version"));
        IFunctionInput abstractClassMethodLineInput = new FunctionInput(new Function(FunctionMarkerInFunctionInput.VariableOnly), abstClsVList);
        abstractClassMethodLineInputList.add(abstractClassMethodLineInput);
        IMethodLine abstractClassMethodLine = new MethodLine(new Function(FunctionStaticStrings.Return), new Variable(),abstractClassMethodLineInputList,  new ArrayList<ILineLabel>(), new LineNumber(-1));
        List<IMethodLine> abstclsMLList = new ArrayList<>();
        abstclsMLList.add(abstractClassMethodLine);
        IMethod abstClsMethod = new Method(new Name("GetVersion"), new FullName(new IName[0], new Name(StaticStrings.Void)), new ArrayList<IParameter>(), abstclsMLList, new ArrayList<IFunctionInput>(), Visibility.Public, new Modifiers(false, false, true), false,new ArrayList<>());
        List<IMethod> absClsMList = new ArrayList<>();
        absClsMList.add(abstClsMethod);
        _Abstract = new ACClass(
                new FullName(new IName[]{new Name("TestPackage")}, new Name("AbstractClass")), 
                Visibility.Public, 
                new Modifiers(true, false, false), 
                absClsMList, 
                new ArrayList<IFullName>(), 
                absClsFList, 
                new ArrayList<IFullName>(), 
                false, false, true, 0, 0);
        
        _DumpClass = new ACClass(new FullName(), Visibility.Public, new Modifiers(), new ArrayList<IMethod>(), new ArrayList<IFullName>(), new ArrayList<IField>(), new ArrayList<IFullName>(), false, false, false, 0, 0);
        List<IParameter> inputParameters = new ArrayList<>();
        inputParameters.add(_InterfaceMethodInputParameter);
        _InterfaceMethod = new Method(new Name(_Interface_Method_Name), new FullName(new IName[0], new Name(StaticStrings.Void)), inputParameters, new ArrayList<IMethodLine>(), new ArrayList<IFunctionInput>(), Visibility.Public, new Modifiers(false, false, false), true,new ArrayList<>());
        List<IMethod> interfaceMethodList = new ArrayList<>();
        interfaceMethodList.add(_InterfaceMethod);
        _Interface = new ACClass(
                new FullName(new IName[]{new Name("TestPackage"),new Name("interfaces")},
                        new Name("IAddIntToList")), 
                Visibility.Public, 
                new Modifiers(false, false, false), 
                interfaceMethodList, 
                new ArrayList<>(), 
                new ArrayList<>(), 
                new ArrayList<>(), 
                true, 
                false, 
                true,
                0, 0);
        List<IField> enumFields = new ArrayList<>();
        enumFields.add(_Enum_Field_1);
        enumFields.add(_Enum_Field_2);

        _Enum = new ACClass(new FullName(new IName[]
        {
            new Name(TestPackageString)
        }, new Name("EnumClass")),
                Visibility.Public,
                new Modifiers(false, false, false),
                new ArrayList<>(),
                new ArrayList<>(),
                enumFields,
                new ArrayList<>(),
                false, true, true, 0, 0);
        
        List<IPackage> packages = new ArrayList<>();
        packages.add(_TestPackage);
        packages.add(_InterfacsPackage);
        List<IClass> classes = new ArrayList<>();
        classes.add(_Enum);
        classes.add(_Interface);
        classes.add(_Abstract);
        classes.add(_Class);
        _Project = new Project(new Name("abc"), classes, packages);

    }

    @Test
    public void testGenerate()
    {
        try
        {
        System.out.println("Generate");
        IProject project = _Project;
        JavaFileGenerator instance = new JavaFileGenerator();
        instance.Generate(project,"./TestCase/");
        
        }
        catch(Exception exception)
        {
            exception.printStackTrace(System.out);
            fail("fail because of exception");
        }
    }

}
