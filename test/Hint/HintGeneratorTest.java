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
import Model.InputParameter;
import Model.Method;
import Model.MethodLine;
import Model.MethodLineFunctions;
import Model.ModelFactory;
import Model.Project;
import Utility.GetArrayList;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Liu
 */
public class HintGeneratorTest
{
    HintGenerator _HintGenerator;
    List<MethodLine> _CurrentMethodLines;
    List<Field> _CurrentFields;
    ACClass _CurrentCLS;
    public HintGeneratorTest()
    {
        GetArrayList<Field> fieldListGetter = new GetArrayList<>();
        GetArrayList<Method> methodListGetter = new GetArrayList<>();
        GetArrayList<ACClass> clsListGetter = new GetArrayList<>();
        
        Field mjfa = ModelFactory.GetFieldWithValue("", "mjfa", "public", "", new ArrayList<String>(), false, false);
        Field mjfbs = ModelFactory.GetFieldWithValue("", "mjfbs", "public", "", new ArrayList<String>(), true, false);
        List<Field> mjfields = fieldListGetter.Get(mjfa, mjfbs);
        
        Method mjma = ModelFactory.GetMethodWithValue("void", "mjma", "public", false, false, false, new ArrayList<InputParameter>(), new ArrayList<String>(), new ArrayList<>());
        List<Method> mjmethods = methodListGetter.Get(mjma);
        
        ACClass mjcls = ModelFactory.GetClassWithValue("mjcls", "public", false, false, new ArrayList<String>(), new ArrayList<String>(), mjfields, mjmethods, new ArrayList<String>(), false, false, 0, 0);
        List<ACClass> mjclses = clsListGetter.Get(mjcls);
        
        Model_Java mj = new Model_Java("", mjclses);
        
        MethodLine pamal1 = ModelFactory.GetMethodLineWithValue("int", "index", MethodLineFunctions.Other, "", 0, 0, "", new ArrayList<String>(), new ArrayList<>());
        MethodLine pamal2 = ModelFactory.GetMethodLineWithValue("", "", MethodLineFunctions.For, "int id = 0; id < 5; id++", 0, 0, "", new ArrayList<String>(), new ArrayList<>());
        List<MethodLine> pamamethodlLines = (new GetArrayList<MethodLine>()).Get(pamal1,pamal2);
        _CurrentMethodLines = pamamethodlLines;
        Method pama = ModelFactory.GetMethodWithValue("void", "pama", "public", false, false, false, new ArrayList<InputParameter>(), new ArrayList<String>(), pamamethodlLines);
        List<Method> pamethods = methodListGetter.Get(pama);
        Field pafa = ModelFactory.GetFieldWithValue("", "pafa", "public", "", new ArrayList<String>(), false, false);
        Field pafbs = ModelFactory.GetFieldWithValue("", "pafbs", "public", "", new ArrayList<String>(), true, true);
        List<Field> pafields = fieldListGetter.Get(pafa, pafbs);
        _CurrentFields = pafields;
        ACClass pacls = ModelFactory.GetClassWithValue("pacls", "public", false, false, new ArrayList<String>(), new ArrayList<String>(), pafields, pamethods, new ArrayList<String>(), false, false, 0, 0);
        _CurrentCLS = pacls;
        List<ACClass> paclses = clsListGetter.Get(pacls);
        
        Project pj = ModelFactory.GetProjectWithValue(paclses, "test");
        
        _HintGenerator = new HintGenerator(pj, mj);
    }

    @Test
    public void testGetClsNamesFromSDKPkgs()
    {
        System.out.println("GetClsNamesFromSDKPkgs");
        HintGenerator instance = _HintGenerator;
        List<String> result = instance.GetClsNamesFromSDKPkgs();
        assertEquals(result.size(), 1);
        assertTrue(result.contains("mjcls"));
        assertFalse(result.contains("pacls"));
    }

    @Test
    public void testGetMthdNamesFromSDKPkgs()
    {
        System.out.println("GetMthdNamesFromSDKPkgs");
        HintGenerator instance = _HintGenerator;
        List<String> result = instance.GetMthdNamesFromSDKPkgs();
        assertTrue(result.contains("mjma"));
        assertFalse(result.contains("pama"));
    }

    @Test
    public void testGetStaticFieldNamesFromSDKPkgs()
    {
        System.out.println("GetStaticFieldNamesFromSDKPkgs");
        HintGenerator instance = _HintGenerator;
        List<String> result = instance.GetStaticFieldNamesFromSDKPkgs();
        assertEquals(result.size(), 1);
        assertTrue(result.contains("mjcls.mjfbs"));
    }

    @Test
    public void testGetNamesStartWithInput()
    {
        System.out.println("GetNamesStartWithInput");
        String input = "abc";
        GetArrayList<String> getter = new GetArrayList<>();
        
        List<String> variableNames = getter.Get("abcd", "abd", "def", "def.abcd");
        HintGenerator instance = _HintGenerator;
        List<String> result = instance.GetNamesStartWithInput(input, variableNames);
        assertEquals(2,result.size());
        assertTrue(result.contains("abcd"));
        assertTrue(result.contains("def.abcd"));
    }

    @Test
    public void testGetStaticVariableNamesOfCurrentProject()
    {
        System.out.println("GetStaticVariableNamesOfCurrentProject");
        HintGenerator instance = _HintGenerator;
        List<String> result = instance.GetStaticFieldNamesOfCurrentProject();
        assertEquals(result.size(), 1);
        assertTrue(result.contains("pacls.pafbs"));
    }

    @Test
    public void testGetMethodNamesOfCurrentProject()
    {
        System.out.println("GetMethodNamesOfCurrentProject");
        HintGenerator instance = _HintGenerator;
        List<String> result = instance.GetMethodNamesOfCurrentProject();
        assertEquals(1, result.size());
        assertTrue(result.contains("pama"));
        assertFalse(result.contains("mjma"));
    }

    @Test
    public void testGetClassNamesOfCurrentProject()
    {
        System.out.println("GetClassNamesOfCurrentProject");
        HintGenerator instance = _HintGenerator;
        List<String> result = instance.GetClassNamesOfCurrentProject();
        assertEquals(1, result.size());
        assertTrue(result.contains("pacls"));
    }

    @Test
    public void testGetVariablesOfCurrentMethod()
    {
        System.out.println("GetVariablesOfCurrentMethod");
        List<MethodLine> methodlines = _CurrentMethodLines;
        HintGenerator instance = _HintGenerator;
        List<String> result = instance.GetVariablesOfCurrentMethod(methodlines);
        assertEquals(1, result.size());
        assertTrue(result.contains("index"));
    }

    @Test
    public void testGetFieldNamesOfCurrentCls()
    {
        System.out.println("GetFieldNamesOfCurrentCls");
        List<Field> fields = _CurrentFields;
        HintGenerator instance = _HintGenerator;
        List<String> result = instance.GetFieldNamesOfCurrentCls(fields);
        assertEquals(1, result.size());
        assertTrue(result.contains("pafa"));
    }

    @Test
    public void testGetHint()
    {
        System.out.println("GetHint");
        String input = "";
        List<MethodLine> methodLines = _CurrentMethodLines;
        ACClass currentCls = _CurrentCLS;
        HintGenerator instance = _HintGenerator;
        List<String> result = instance.GetHint(input, methodLines, currentCls);
        GetArrayList<String> getter = new GetArrayList<>();
        List<String> list = getter.Get("void","mjcls", "mjcls.mjfbs", "mjma", "index", "pama", "pafa", "pacls.pafbs", "pacls", "new", "this");
        assertEquals(list.size(), result.size());
        assertTrue(result.containsAll(list));
        input = "p";
        list = getter.Get("pama", "pafa", "pacls.pafbs", "pacls");
        result = instance.GetHint(input, methodLines, currentCls);
        assertEquals(list.size(), result.size());
        assertTrue(result.containsAll(list));
        input = "mjfbs";
        list = getter.Get("mjcls.mjfbs");
        result = instance.GetHint(input, methodLines, currentCls);
        assertEquals(list.size(), result.size());
        assertTrue(result.containsAll(list));
        input = "abc.";
        list = getter.Get("mjcls",  "mjcls.mjfbs", "mjma", "index", "pama", "pafa", "pacls.pafbs", "pacls", "new", "this", "void");
        result = instance.GetHint(input, methodLines, currentCls);
        assertEquals(list.size(), result.size());
        assertTrue(result.containsAll(list));
    }
    
    @Test
    public void testIsReset()
    {
        String input = "";
        boolean result = _HintGenerator.IsReset(input);
        assertEquals(true, result);
        input = "in";
        result = _HintGenerator.IsReset(input);
        assertEquals(false, result);
        input = "int ";
        result = _HintGenerator.IsReset(input);
        assertEquals(true, result);
        input = "int in";
        result = _HintGenerator.IsReset(input);
        assertEquals(false, result);
        input = "int index";
        result = _HintGenerator.IsReset(input);
        assertEquals(false, result);
        input = "int index ";
        result = _HintGenerator.IsReset(input);
        assertEquals(true, result);
        input = "index =";
        result = _HintGenerator.IsReset(input);
        assertEquals(true, result);
        input = " = 1";
        result = _HintGenerator.IsReset(input);
        assertEquals(false, result);
        input = "ModelFactory.";
        result = _HintGenerator.IsReset(input);
        assertEquals(true, result);
        input = "IsReset(";
        result = _HintGenerator.IsReset(input);
        assertEquals(true, result);
        input = "List<";
        result = _HintGenerator.IsReset(input);
        assertEquals(true, result);                
    }
}
