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

import Model.MethodLineFunctions;
import Model.ModelFactory;
import Model.StaticStrings;
import Model.ACClass;
import Model.Field;
import Model.InputParameter;
import Model.Method;
import Model.MethodLine;
import Model.Project;
import Utility.GetArrayList;
import View.FlowChartFunctions;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Liu
 */
public class TranslateToJavaTest
{

    public TranslateToJavaTest()
    {
    }

    @Test
    public void testTranslate()
    {
        System.out.println("Translate");
        MethodLine line0 = ModelFactory.GetMethodLineWithValue("", "", FlowChartFunctions.START, "in int index, int abc", 0, 0, "", new ArrayList<String>(), new ArrayList<>());
        MethodLine line1 = ModelFactory.GetMethodLineWithValue("int", "i", MethodLineFunctions.Other, "1", 0, 0, "", new ArrayList<String>(), new ArrayList<>());
        MethodLine line2 = ModelFactory.GetMethodLineWithValue("", "", MethodLineFunctions.For, "int index = 0; index <= 1; index++", 0, 0, "", new ArrayList<String>(), (new GetArrayList<String>()).Get("FOR"));
        MethodLine line3 = ModelFactory.GetMethodLineWithValue("", "", MethodLineFunctions.If, "index == 0", 0, 0, "", new ArrayList<String>(), (new GetArrayList<String>()).Get("FOR", "IF"));
        MethodLine line4 = ModelFactory.GetMethodLineWithValue("", "", MethodLineFunctions.Other, "System.out.println(\" is 0 \")", 0, 0, "", new ArrayList<String>(), (new GetArrayList<String>()).Get("FOR", "IF"));
        MethodLine line5 = ModelFactory.GetMethodLineWithValue("", "", MethodLineFunctions.Else, "", 0, 0, "", new ArrayList<String>(), (new GetArrayList<String>()).Get("FOR", "ELSE"));
        MethodLine line6 = ModelFactory.GetMethodLineWithValue("", "", MethodLineFunctions.Other, "System.out.println(\" is 1\")", 0, 0, "", new ArrayList<String>(), (new GetArrayList<String>()).Get("FOR","ELSE"));
        MethodLine line61 = ModelFactory.GetMethodLineWithValue("", "", FlowChartFunctions.LoopEnd, "", 0, 0, "", new ArrayList<>(), new ArrayList<>());
        MethodLine line7 = ModelFactory.GetMethodLineWithValue("", "i", MethodLineFunctions.Other, "2", 0, 0, "", new ArrayList<String>(), new ArrayList<>());
        MethodLine line8 = ModelFactory.GetMethodLineWithValue("", "", FlowChartFunctions.END, "", 0, 0, "", new ArrayList<String>(), new ArrayList<>());
        List<MethodLine> body = (new GetArrayList<MethodLine>()).Get(line1,line2,line3,line4,line5,line6,line61,line7);
        Method method = ModelFactory.GetMethodWithValue("void", "clsmthd", StaticStrings.Public, false, false, false, new ArrayList<InputParameter>(), new ArrayList<String>(), body);
        List<Method> methods = (new GetArrayList<Method>()).Get(method);
        ACClass cls = ModelFactory.GetClassWithValue("a.b.cls", StaticStrings.Public, false, false, new ArrayList<String>(), new ArrayList<String>(), new ArrayList<Field>(), methods, new ArrayList<String>(), false, false, 0, 0);
        List<ACClass> classes = new ArrayList<>();
        classes.add(cls);
        Project project = ModelFactory.GetProjectWithValue(classes, "test");
        String savePath = "./test";
        TranslateToJava instance = new TranslateToJava();
        try
        {
            instance.Translate(project, savePath);
        } catch (Exception exception)
        {exception.printStackTrace(System.out);
            fail("exception.");
            
        }
    }

    @Test
    public void testGetFile()
    {
        System.out.println("GetFile");
        String baseSavePath = "./";
        String clsName = "a.b.c";
        TranslateToJava instance = new TranslateToJava();
        Path file = instance.GetSavePath(baseSavePath, clsName);
        assertTrue(file.startsWith("./a/b/"));
    }

    @Test
    public void testSaveFile() throws Exception
    {
        System.out.println("SaveFile");
        Path file = Paths.get("./testsavefile");
        List<String> content = (new GetArrayList<String>()).Get("abcdefg");
        TranslateToJava instance = new TranslateToJava();
        instance.SaveFile(Paths.get("./"),file, content);
        List<String> cList = Files.readAllLines(file);
        assertEquals(cList.size(), content.size());
        for (int i = 0; i < cList.size(); i++)
        {
            assertEquals(cList.get(i), content.get(i));
        }
    }

    @Test
    public void testGetPackage()
    {
        System.out.println("GetPackage");
        String packageName = "a.b";
        TranslateToJava instance = new TranslateToJava();
        String expResult = StaticStrings.Package + " a.b;";
        String result = instance.GetPackage(packageName);
        assertEquals(expResult, result);
    }

    @Test
    public void testGetPackageName()
    {
        System.out.println("GetPackageName");
        String clsName = "a.b.c";
        TranslateToJava instance = new TranslateToJava();
        String expResult = "a.b";
        String result = instance.GetPackageName(clsName);
        assertEquals(expResult, result);
    }

    @Test
    public void testGetClassDeclaration()
    {
        System.out.println("GetClassDeclaration");
        ACClass cls = ModelFactory.GetClassWithValue(
                "cls",
                StaticStrings.Public,
                true,
                true,
                (new Utility.GetArrayList<String>()).Get("d.e.f"),
                (new GetArrayList<String>()).Get("h.i.j"),
                new ArrayList<Field>(),
                new ArrayList<Method>(),
                (new GetArrayList<String>()).Get("ann1"),
                false,
                false,
                0,
                0);
        TranslateToJava instance = new TranslateToJava();
        String expResult = "@ann1\n" + StaticStrings.Public + " " + StaticStrings.Abstract + " " + StaticStrings.Final + " " + "class " + "cls "
                + StaticStrings.Extends + " d.e.f "
                + StaticStrings.Implements + " h.i.j";
        String result = instance.GetClassDeclaration(cls);
        System.out.println(expResult);
        System.out.println(result);
        assertEquals(expResult, result);
    }

    @Test
    public void testGetExtends()
    {
        System.out.println("GetExtends");
        List<StringProperty> extds = (new GetArrayList<StringProperty>()).Get(new SimpleStringProperty("d.e.f"), new SimpleStringProperty("h.i.j"));
        TranslateToJava instance = new TranslateToJava();
        String expResult = StaticStrings.Extends + " d.e.f,h.i.j";
        String result = instance.GetExtends(extds);
        assertEquals(expResult, result);
    }

    @Test
    public void testGetImplements()
    {
        System.out.println("GetImplements");
        List<StringProperty> imps = (new GetArrayList<StringProperty>()).Get(new SimpleStringProperty("d.e.f"), new SimpleStringProperty("h.i.j"));;
        TranslateToJava instance = new TranslateToJava();
        String expResult = StaticStrings.Implements + " " + "d.e.f,h.i.j";
        String result = instance.GetImplements(imps);
        assertEquals(expResult, result);
    }

    @Test
    public void testGetAnnotations()
    {
        System.out.println("GetAnnotations");
        List<StringProperty> annotations = (new GetArrayList<StringProperty>()).Get(new SimpleStringProperty("annotation"), new SimpleStringProperty("secondannotation"));;
        TranslateToJava instance = new TranslateToJava();
        String expResult = "@" + "annotation\n"
                + "@secondannotation\n";
        String result = instance.GetAnnotations(annotations);
        assertEquals(expResult, result);
    }

    @Test
    public void testGetField()
    {
        System.out.println("GetField");
        Field field = ModelFactory.GetFieldWithValue(
                StaticStrings.Type, StaticStrings.Name, StaticStrings.Public, StaticStrings.DefaultValue,
                (new GetArrayList<String>()).Get("ann1", "ann2"), true, true);
        TranslateToJava instance = new TranslateToJava();
        String expResult = "@ann1\n@ann2\n"
                + StaticStrings.Public + " " + StaticStrings.Static + " " + StaticStrings.Final + " "
                + StaticStrings.Type + " " + StaticStrings.Name + " = " + StaticStrings.DefaultValue + ";";
        String result = instance.GetField(field);
        assertEquals(expResult, result);
    }

    @Test
    public void testGetMethod()
    {
        System.out.println("GetMethod");
        Method method = ModelFactory.GetMethodWithValue(
                StaticStrings.Type,
                StaticStrings.Name,
                StaticStrings.Public,
                true,
                true,
                true,
                (new GetArrayList<InputParameter>()).Get(
                        ModelFactory.GetInputParameterWithValue(StaticStrings.Type, StaticStrings.Name),
                        ModelFactory.GetInputParameterWithValue("a", "b")),
                (new GetArrayList<String>()).Get("ann1", "ann2"),
                new ArrayList<>());
        TranslateToJava instance = new TranslateToJava();
        String expResult = "@ann1\n@ann2\n"
                + StaticStrings.Public + " " + StaticStrings.Static + " " + StaticStrings.Final + " "
                + StaticStrings.Type + " " + StaticStrings.Name + "("
                + StaticStrings.Type + " " + StaticStrings.Name + ",a b)"
                + StaticStrings.Throws + " " + StaticStrings.Exception;
        String result = instance.GetMethodDeclaration(method);
        assertEquals(expResult, result);
    }

    @Test
    public void testGetMethodLine()
    {
        double layoutX = 0, layoutY = 0;
        String type = "type", name = "name", content = "content", selfLabel = "self";
        ArrayList<String> list = new ArrayList<>();
        System.out.println("GetMethodLine");
        MethodLine methodLineContinue = ModelFactory.GetMethodLineWithValue(type, name, MethodLineFunctions.Continue, content, layoutX, layoutY, selfLabel, list, list),
                methodLineReturn = ModelFactory.GetMethodLineWithValue(type, name, MethodLineFunctions.Return, content, layoutX, layoutY, selfLabel, list, list),
                methodLineBreak = ModelFactory.GetMethodLineWithValue(type, name, MethodLineFunctions.Break, content, layoutX, layoutY, selfLabel, list, list),
                methodLineElse = ModelFactory.GetMethodLineWithValue(type, name, MethodLineFunctions.Else, content, layoutX, layoutY, selfLabel, list, list),
                methodLineTry = ModelFactory.GetMethodLineWithValue(type, name, MethodLineFunctions.Try, content, layoutX, layoutY, selfLabel, list, list),
                methodLineTryWithResources = ModelFactory.GetMethodLineWithValue(type, name, MethodLineFunctions.TryWithResources, content, layoutX, layoutY, selfLabel, list, list),
                methodLineCatch = ModelFactory.GetMethodLineWithValue(type, name, MethodLineFunctions.Catch, content, layoutX, layoutY, selfLabel, list, list),
                methodLineFinally = ModelFactory.GetMethodLineWithValue(type, name, MethodLineFunctions.Finally, content, layoutX, layoutY, selfLabel, list, list),
                methodLineFor = ModelFactory.GetMethodLineWithValue(type, name, MethodLineFunctions.For, content, layoutX, layoutY, selfLabel, list, list),
                methodLineForEach = ModelFactory.GetMethodLineWithValue(type, name, MethodLineFunctions.ForEach, content, layoutX, layoutY, selfLabel, list, list),
                methodLineWhile = ModelFactory.GetMethodLineWithValue(type, name, MethodLineFunctions.While, content, layoutX, layoutY, selfLabel, list, list),
                methodLineDoWhile = ModelFactory.GetMethodLineWithValue(type, name, MethodLineFunctions.Do, content, layoutX, layoutY, selfLabel, list, list),
                methodLineIf = ModelFactory.GetMethodLineWithValue(type, name, MethodLineFunctions.If, content, layoutX, layoutY, selfLabel, list, list),
                methodLineSwitch = ModelFactory.GetMethodLineWithValue(type, name, MethodLineFunctions.Switch, content, layoutX, layoutY, selfLabel, list, list),
                methodLineCase = ModelFactory.GetMethodLineWithValue(type, name, MethodLineFunctions.Case, content, layoutX, layoutY, selfLabel, list, list),
                methodLineDefault = ModelFactory.GetMethodLineWithValue(type, name, MethodLineFunctions.Default, content, layoutX, layoutY, selfLabel, list, list),
                methodLineThrow = ModelFactory.GetMethodLineWithValue(type, name, MethodLineFunctions.Throw, content, layoutX, layoutY, selfLabel, list, list),
                methodLineOther = ModelFactory.GetMethodLineWithValue(type, name, MethodLineFunctions.Other, content, layoutX, layoutY, selfLabel, list, list);

        TranslateToJava instance = new TranslateToJava();

        String expResult = MethodLineFunctions.Break + ";";
        String result = instance.GetMethodLine(methodLineBreak);
        assertEquals(expResult, result);
        expResult = MethodLineFunctions.Throw + " " + content + ";";
        result = instance.GetMethodLine(methodLineThrow);
        assertEquals(expResult, result);
        expResult = type + " " + name + " = " + content + ";";
        result = instance.GetMethodLine(methodLineOther);
        assertEquals(expResult, result);
        expResult = MethodLineFunctions.Continue + ";";
        result = instance.GetMethodLine(methodLineContinue);
        assertEquals(expResult, result);
        expResult = MethodLineFunctions.Return + " " + content + ";";
        result = instance.GetMethodLine(methodLineReturn);
        assertEquals(expResult, result);
        expResult = MethodLineFunctions.Try;
        result = instance.GetMethodLine(methodLineTry);
        assertEquals(expResult, result);
        expResult = MethodLineFunctions.Try + "( " + content + ")";
        result = instance.GetMethodLine(methodLineTryWithResources);
        assertEquals(expResult, result);
        expResult = MethodLineFunctions.Catch + "( " + content + ")";
        result = instance.GetMethodLine(methodLineCatch);
        assertEquals(expResult, result);
        expResult = MethodLineFunctions.Finally;
        result = instance.GetMethodLine(methodLineFinally);
        assertEquals(expResult, result);
        expResult = MethodLineFunctions.For + "( " + content + ")";
        result = instance.GetMethodLine(methodLineFor);
        assertEquals(expResult, result);
        expResult = MethodLineFunctions.For + "( " + content + ")";
        result = instance.GetMethodLine(methodLineForEach);
        assertEquals(expResult, result);
        expResult = MethodLineFunctions.While + "( " + content + ")";
        result = instance.GetMethodLine(methodLineWhile);
        assertEquals(expResult, result);
        expResult = MethodLineFunctions.Do;
        result = instance.GetMethodLine(methodLineDoWhile);
        assertEquals(expResult, result);
        expResult = MethodLineFunctions.Switch + "( " + content + ")";
        result = instance.GetMethodLine(methodLineSwitch);
        assertEquals(expResult, result);
        expResult = MethodLineFunctions.Case + " " + content + ":";
        result = instance.GetMethodLine(methodLineCase);
        assertEquals(expResult, result);
        expResult = MethodLineFunctions.Default + ":";
        result = instance.GetMethodLine(methodLineDefault);
        assertEquals(expResult, result);
        expResult = MethodLineFunctions.If + "( " + content + ")";
        result = instance.GetMethodLine(methodLineIf);
        assertEquals(expResult, result);
        expResult = MethodLineFunctions.Else;
        result = instance.GetMethodLine(methodLineElse);
        assertEquals(expResult, result);
    }

    @Test
    public void testGetInputParameter()
    {
        System.out.println("GetInputParameter");
        InputParameter inputParameter = ModelFactory.GetInputParameterWithValue("a", "b");
        TranslateToJava instance = new TranslateToJava();
        String expResult = "a b";
        String result = instance.GetInputParameter(inputParameter);
        assertEquals(expResult, result);
    }

    @Test
    public void testGetHowManyItemsInSet2NotContainInSet1()
    {
        System.out.println("GetHowManyItemsInSet2NotContainInSet1");
        List<String> set1 = (new GetArrayList<String>()).Get("0", "1");
        List<String> set2 = (new GetArrayList<String>()).Get("1", "2");
        TranslateToJava instance = new TranslateToJava();
        int expResult = 1;
        int result = instance.GetHowManyItemsInSet2NotContainInSet1(set1, set2);
        assertEquals(expResult, result);
        set1 = new ArrayList<>();
        set2 = new ArrayList<>();
        result = instance.GetHowManyItemsInSet2NotContainInSet1(set1, set2);
        assertEquals(0, result);
        set1 = (new GetArrayList<String>()).Get("0", "1");
        set2 = new ArrayList<>();
        result = instance.GetHowManyItemsInSet2NotContainInSet1(set1, set2);
        assertEquals(0, result);
        set1 = new ArrayList<>();
        set2 = new ArrayList<>((new GetArrayList<String>()).Get("0", "1"));
        result = instance.GetHowManyItemsInSet2NotContainInSet1(set1, set2);
        assertEquals(2, result);
        set1 = (new GetArrayList<String>()).Get("0");
        set2 = (new GetArrayList<String>()).Get("0");
        result = instance.GetHowManyItemsInSet2NotContainInSet1(set1, set2);
        assertEquals(0, result);
        set1 = (new GetArrayList<String>()).Get("0");
        set2 = (new GetArrayList<String>()).Get("0", "1");
        result = instance.GetHowManyItemsInSet2NotContainInSet1(set1, set2);
        assertEquals(1, result);
        set1 = (new GetArrayList<String>()).Get("0", "1");
        set2 = (new GetArrayList<String>()).Get("0");
        result = instance.GetHowManyItemsInSet2NotContainInSet1(set1, set2);
        assertEquals(0, result);
    }

    @Test

    public void TestDuplicatedStrings()
    {
        String str = "abc";
        int count = 3;
        String result = new TranslateToJava().GetDuplicatedStrings(count, str);
        assertTrue(result.length() == 9);
        result = result.replace("abc", ".");
        assertTrue(result.length() == 3);
    }

}
