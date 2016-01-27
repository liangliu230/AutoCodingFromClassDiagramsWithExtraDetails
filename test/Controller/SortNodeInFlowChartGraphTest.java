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
package Controller;

import Model.MethodLine;
import Model.MethodLineFunctions;
import Model.ModelFactory;
import Utility.GetArrayList;
import View.FlowChartFunctions;
import View.FlowChartNodeType;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.StringProperty;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Liu
 */
public class SortNodeInFlowChartGraphTest
{

    public SortNodeInFlowChartGraphTest()
    {
    }

    @Test
    public void testSort()
    {
        System.out.println("Test SortNodeInFlowChartGraph");
        System.out.println("\ntest case 1");
        test(this.GetTestCase1(), this.GetExpResultOfTestCase1());
        System.out.println("\ntest case 2");
        test(this.GetTestCase2_if(), this.GetExpResultOfTestCase2());
        System.out.println("\ntest case 3");
        test(this.GetTestCase3_for(), this.GetExpResultOfTestCase3());
        System.out.println("\ntest case 4");
        test(this.GetTestCase4_Switch(), this.GetExpResultOfTestCase4());
        System.out.println("\ntest case 5");
        test(this.GetTestCase5_IfCasecade(), this.GetExpResultOfTestCase5());
        System.out.println("\ntest case 6");
        test(this.GetTestCase6_CasecadeFor(), this.GetExpResultOfTestCase6());
        System.out.println("\ntest case 7");
        test(this.GetTestCase7_SEQUAndIF(), this.GetExpResultOfTestCase7());
        System.out.println("\ntest case 8");
        test(this.GetTestCase8_SequAndFor(), this.GetExpResultOfTestCase8());
        System.out.println("\ntest case 9");
        test(this.GetTestCase9_Try(), this.GetExpResultOfTestCase9());
    }
    
    private void test(List<MethodLine> input, List<MethodLine> expResult)
    {
        List<MethodLine> result = new SortNodeInFlowChartGraph().SortAndMark(input);
        Verification(result, expResult);
    }

    private void Verification(List<MethodLine> result, List<MethodLine> expResult)
    {
        assertEquals(result.size(), expResult.size());
        
        for (int index = 0; index < expResult.size(); index++)
        {
            assertEquals(expResult.get(index).GetSelfLabel().getValue(),
                    result.get(index).GetSelfLabel().getValue());
            assertEquals(expResult.get(index).GetFunctionType().getValue(),
                    result.get(index).GetFunctionType().getValue());
            System.out.println(expResult.get(index).GetFunctionType().getValue() + " " + expResult.get(index).GetFunctionType().getValue());
            this.TestListEqual(expResult.get(index).GetLineLabels(), result.get(index).GetLineLabels());
        }
    }

    private void TestListEqual(List<String> list1, List<String> list2)
    {
        assertEquals(list1.size(), list2.size());
        for (int index = 0; index < list1.size(); index++)
        {
            assertEquals(list1.get(index), list2.get(index));
        }
    }

    private MethodLine GetFlowChartStart()
    {
        MethodLine start = ModelFactory
                .GetMethodLineWithValue("", "", FlowChartFunctions.START, "", 0, 0, "o0", new ArrayList<String>(), new ArrayList<>());

        start.GetLinkToLabels().add(ModelFactory.GetStringPropertyWithValue("o2"));
        return start;
    }

    private MethodLine GetFlowChartEnd()
    {
        return ModelFactory.GetMethodLineWithValue("", "", FlowChartFunctions.END, "", 0, 0, "o1", new ArrayList<String>(), new ArrayList<>());
    }

           private List<MethodLine> GetTestCase1()
    {
        MethodLine sequ1 = ModelFactory.GetMethodLine_Empty();
        sequ1.GetFunctionType().setValue(MethodLineFunctions.Other);
        sequ1.GetSelfLabel().setValue("o4");
        sequ1.GetLinkToLabels().add(ModelFactory.GetStringPropertyWithValue("o2"));
        MethodLine sequ2 = ModelFactory.GetMethodLine_Empty();
        sequ2.GetFunctionType().setValue(MethodLineFunctions.Other);
        sequ2.GetSelfLabel().setValue("o2");
        sequ2.GetLinkToLabels().add(ModelFactory.GetStringPropertyWithValue("o1"));
        List<MethodLine> lines = new ArrayList<>();
        
        MethodLine start = this.GetFlowChartStart();
        start.GetLinkToLabels().clear();
        start.GetLinkToLabels().add(ModelFactory.GetStringPropertyWithValue("o4"));
        MethodLine end = this.GetFlowChartEnd();
        lines.add(start);
        lines.add(end);
        lines.add(sequ1);
        lines.add(sequ2);
        return lines;
    }

    private List<MethodLine> GetExpResultOfTestCase1()
    {
        MethodLine sequ1 = ModelFactory.GetMethodLine_Empty();
        sequ1.GetFunctionType().setValue(MethodLineFunctions.Other);
        sequ1.GetSelfLabel().setValue("o4");
        sequ1.GetLinkToLabels().add(ModelFactory.GetStringPropertyWithValue("o2"));
        MethodLine sequ2 = ModelFactory.GetMethodLine_Empty();
        sequ2.GetFunctionType().setValue(MethodLineFunctions.Other);
        sequ2.GetSelfLabel().setValue("o2");
        sequ2.GetLinkToLabels().add(ModelFactory.GetStringPropertyWithValue("o1"));
        List<MethodLine> lines = new ArrayList<>();
        
        MethodLine start = this.GetFlowChartStart();
        start.GetLinkToLabels().clear();
        start.GetLinkToLabels().add(ModelFactory.GetStringPropertyWithValue("o4"));
        MethodLine end = this.GetFlowChartEnd();
        lines.add(start);
        lines.add(sequ1);
        lines.add(sequ2);
        lines.add(end);
        
        return lines;
    }

    private List<MethodLine> GetTestCase2_if()
    {
        MethodLine ifLine = ModelFactory.GetMethodLine_Empty();
        ifLine.GetFunctionType().setValue(MethodLineFunctions.If);
        ifLine.GetSelfLabel().setValue("o5");
        ifLine.GetLinkToLabels().add(ModelFactory.GetStringPropertyWithValue("o2"));
        ifLine.GetLinkToLabels().add(ModelFactory.GetStringPropertyWithValue("o3"));
        MethodLine trueCluse = ModelFactory.GetMethodLine_Empty();
        trueCluse.GetFunctionType().setValue(MethodLineFunctions.Other);
        trueCluse.GetSelfLabel().setValue("o2");
        trueCluse.GetLinkToLabels().add(ModelFactory.GetStringPropertyWithValue("o1"));
        MethodLine falseCluse = ModelFactory.GetMethodLine_Empty();
        falseCluse.GetFunctionType().setValue(MethodLineFunctions.Other);
        falseCluse.GetSelfLabel().setValue("o3");
        falseCluse.GetLinkToLabels().add(ModelFactory.GetStringPropertyWithValue("o1"));
        MethodLine start = this.GetFlowChartStart();
        start.GetLinkToLabels().clear();
        start.GetLinkToLabels().add(ModelFactory.GetStringPropertyWithValue("o5"));
        MethodLine end = this.GetFlowChartEnd();

        return (new GetArrayList<MethodLine>()).Get(start,ifLine, trueCluse, falseCluse, end);
    }

    private List<MethodLine> GetExpResultOfTestCase2()
    {
        MethodLine ifLine = ModelFactory.GetMethodLine_Empty();
        ifLine.GetFunctionType().setValue(MethodLineFunctions.If);
        ifLine.GetSelfLabel().setValue("o5");
        ifLine.GetLinkToLabels().add(ModelFactory.GetStringPropertyWithValue("o2"));
        ifLine.GetLinkToLabels().add(ModelFactory.GetStringPropertyWithValue("o3"));
        MethodLine trueCluse = ModelFactory.GetMethodLine_Empty();
        trueCluse.GetFunctionType().setValue(MethodLineFunctions.Other);
        trueCluse.GetSelfLabel().setValue("o2");
        trueCluse.GetLinkToLabels().add(ModelFactory.GetStringPropertyWithValue("o1"));
        MethodLine falseCluse = ModelFactory.GetMethodLine_Empty();
        falseCluse.GetFunctionType().setValue(MethodLineFunctions.Other);
        falseCluse.GetSelfLabel().setValue("o3");
        falseCluse.GetLinkToLabels().add(ModelFactory.GetStringPropertyWithValue("o1"));

        MethodLine elseLine = ModelFactory.GetMethodLine_Empty();
        elseLine.GetFunctionType().setValue(MethodLineFunctions.Else);

        ifLine.GetLineLabels().addAll(
                (new GetArrayList<String>()).Get(
                        "IF0"
                )
        );
        trueCluse.GetLineLabels().add("IF0");
        elseLine.GetLineLabels().add("ELSE1");
        falseCluse.GetLineLabels().add("ELSE1");
        MethodLine start = this.GetFlowChartStart();
        start.GetLinkToLabels().clear();
        start.GetLinkToLabels().add(ModelFactory.GetStringPropertyWithValue("o5"));
        MethodLine end = this.GetFlowChartEnd();
        return (new GetArrayList<MethodLine>()).Get(start,ifLine, trueCluse, elseLine, falseCluse, end);
    }

    private List<MethodLine> GetTestCase3_for()
    {
        MethodLine forLine = this.GetMethodLine(MethodLineFunctions.For, "o2", "o3");
        MethodLine contentLine = this.GetMethodLine(MethodLineFunctions.Break, "o3", "o4");
        MethodLine loopEndLine = this.GetMethodLine(FlowChartFunctions.LoopEnd, "o4", "o1");
        MethodLine start = this.GetFlowChartStart();
        MethodLine end = this.GetFlowChartEnd();
        return (new GetArrayList<MethodLine>()).Get(start, end, forLine, loopEndLine, contentLine);
    }

    private List<MethodLine> GetExpResultOfTestCase3()
    {
        MethodLine forLine = this.GetMethodLine(MethodLineFunctions.For, "o2", "o3");
        MethodLine contentLine = this.GetMethodLine(MethodLineFunctions.Break, "o3", "o4");
        MethodLine loopEndLine = this.GetMethodLine(FlowChartFunctions.LoopEnd, "o4", "o1");
        forLine.GetLineLabels().add("FOR0");
        contentLine.GetLineLabels().add("FOR0");
                MethodLine start = this.GetFlowChartStart();
        MethodLine end = this.GetFlowChartEnd();
        return this.GetArraylist(start, forLine, contentLine, loopEndLine, end);
    }

    private List<MethodLine> GetTestCase4_Switch()
    {
        MethodLine start = this.GetFlowChartStart();
        MethodLine end = this.GetFlowChartEnd();
        MethodLine switchLine = this.GetMethodLine(MethodLineFunctions.Switch, "o2", "o3", "o4", "o5", "o9");
        MethodLine defaultLine = this.GetMethodLine(MethodLineFunctions.Default, "o3", "o6");
        MethodLine case1Line = this.GetMethodLine(MethodLineFunctions.Case, "o4", "o7");
        MethodLine case2lLine = this.GetMethodLine(MethodLineFunctions.Case, "o5", "o7");
        MethodLine case3lLine = this.GetMethodLine(MethodLineFunctions.Case, "o9", "o8");
        MethodLine o6line = this.GetMethodLine(MethodLineFunctions.Other, "o6", "o1");
        MethodLine o7line = this.GetMethodLine(MethodLineFunctions.Other, "o7", "o1");
        MethodLine o8liLine = this.GetMethodLine(MethodLineFunctions.Other, "o8", "o1");
        
        return this.GetArraylist(start, end, switchLine, defaultLine, case1Line, case2lLine, case3lLine, o6line, o7line, o8liLine);
    }

    private List<MethodLine> GetExpResultOfTestCase4()
    {
        MethodLine switchLine = this.GetMethodLine(MethodLineFunctions.Switch, "o2", "o3", "o4", "o5", "o9");
        switchLine.GetLineLabels().add("SWITCH0");
        MethodLine defaultLine = this.GetMethodLine(MethodLineFunctions.Default, "o3", "o6");
        defaultLine.GetLineLabels().add("SWITCH0");
        MethodLine case1Line = this.GetMethodLine(MethodLineFunctions.Case, "o4", "o7");
        case1Line.GetLineLabels().add("SWITCH0");
        MethodLine case2lLine = this.GetMethodLine(MethodLineFunctions.Case, "o5", "o7");
        this.AddLineLabels(case2lLine, "SWITCH0");
        MethodLine case3lLine = this.GetMethodLine(MethodLineFunctions.Case, "o9", "o8");
        this.AddLineLabels(case3lLine, "SWITCH0");
        MethodLine o6line = this.GetMethodLine(MethodLineFunctions.Other, "o6", "o1");
        this.AddLineLabels(o6line, "SWITCH0");
        MethodLine o7line = this.GetMethodLine(MethodLineFunctions.Other, "o7", "o1");
        this.AddLineLabels(o7line, "SWITCH0");
        MethodLine o8Line = this.GetMethodLine(MethodLineFunctions.Other, "o8", "o1");
        this.AddLineLabels(o8Line, "SWITCH0");
        MethodLine start = this.GetFlowChartStart();
        MethodLine end = this.GetFlowChartEnd();
        return this.GetArraylist(start,switchLine, defaultLine, o6line, case1Line, case2lLine, o7line, case3lLine, o8Line,end);
    }

    private List<MethodLine> GetTestCase5_IfCasecade()
    {
        MethodLine start = this.GetFlowChartStart();
        MethodLine end = this.GetFlowChartEnd();
        MethodLine if1line = this.GetMethodLine(MethodLineFunctions.If, "o2", "o3", "o4");
        MethodLine if2line = this.GetMethodLine(MethodLineFunctions.If, "o3", "o5", "o6");
        MethodLine true2 = this.GetMethodLine(MethodLineFunctions.Other, "o5", "o1");
        MethodLine false2 = this.GetMethodLine(MethodLineFunctions.Other, "o6", "o1");
        MethodLine if3line = this.GetMethodLine(MethodLineFunctions.If, "o4", "o7", "o8");
        MethodLine true3 = this.GetMethodLine(MethodLineFunctions.Other, "o7", "o1");
        MethodLine false3 = this.GetMethodLine(MethodLineFunctions.Other, "o8", "o1");

        return this.GetArraylist(start, end, if1line, if2line, if3line, true2, true3, false2, false3);
    }

    private List<MethodLine> GetExpResultOfTestCase5()
    {
        MethodLine if1line = this.GetMethodLine(MethodLineFunctions.If, "o2", "o3", "o4");
        this.AddLineLabels(if1line, "IF0");
        MethodLine if2line = this.GetMethodLine(MethodLineFunctions.If, "o3", "o5", "o6");
        this.AddLineLabels(if2line, "IF0", "IF1");
        MethodLine true2 = this.GetMethodLine(MethodLineFunctions.Other, "o5", "o1");
        this.AddLineLabels(true2, "IF0", "IF1");
        MethodLine else2 = this.GetMethodLine(MethodLineFunctions.Else, "", "");
        this.AddLineLabels(else2, "IF0", "ELSE2");
        MethodLine false2 = this.GetMethodLine(MethodLineFunctions.Other, "o6", "o1");
        this.AddLineLabels(false2, "IF0", "ELSE2");
        MethodLine else1 = this.GetMethodLine(MethodLineFunctions.Else, "", "");
        this.AddLineLabels(else1, "ELSE3");
        MethodLine if3line = this.GetMethodLine(MethodLineFunctions.If, "o4", "o7", "o8");
        this.AddLineLabels(if3line, "ELSE3", "IF4");
        MethodLine true3 = this.GetMethodLine(MethodLineFunctions.Other, "o7", "o1");
        this.AddLineLabels(true3, "ELSE3", "IF4");
        MethodLine else3 = this.GetMethodLine(MethodLineFunctions.Else, "", "");
        this.AddLineLabels(else3, "ELSE3", "ELSE5");
        MethodLine false3 = this.GetMethodLine(MethodLineFunctions.Other, "o8", "o1");
        this.AddLineLabels(false3, "ELSE3", "ELSE5");
                MethodLine start = this.GetFlowChartStart();
        MethodLine end = this.GetFlowChartEnd();
        return this.GetArraylist(start,if1line, if2line, true2, else2, false2, else1, if3line, true3, else3, false3,end);
    }

    private List<MethodLine> GetTestCase6_CasecadeFor()
    {
        MethodLine start = this.GetFlowChartStart();
        MethodLine end = this.GetFlowChartEnd();
        MethodLine for1 = this.GetMethodLine(MethodLineFunctions.For, "o2", "o3");
        MethodLine for2 = this.GetMethodLine(MethodLineFunctions.For, "o3", "o4");
        MethodLine content = this.GetMethodLine(MethodLineFunctions.Other, "o4", "o5");
        MethodLine loopend1 = this.GetMethodLine(FlowChartFunctions.LoopEnd, "o5", "o6");
        MethodLine loopend2 = this.GetMethodLine(FlowChartFunctions.LoopEnd, "o6", "o1");
        return this.GetArraylist(start, end, for1, for2, content, loopend1, loopend2);
    }

    private List<MethodLine> GetExpResultOfTestCase6()
    {
        MethodLine for1 = this.GetMethodLine(MethodLineFunctions.For, "o2", "o3");
        this.AddLineLabels(for1, "FOR0");
        MethodLine for2 = this.GetMethodLine(MethodLineFunctions.For, "o3", "o4");
        this.AddLineLabels(for2, "FOR0", "FOR1");
        MethodLine content = this.GetMethodLine(MethodLineFunctions.Other, "o4", "o5");
        this.AddLineLabels(content, "FOR0", "FOR1");
        MethodLine loopend1 = this.GetMethodLine(FlowChartFunctions.LoopEnd, "o5", "o6");
        MethodLine loopend2 = this.GetMethodLine(FlowChartFunctions.LoopEnd, "o6", "o1");
                MethodLine start = this.GetFlowChartStart();
        MethodLine end = this.GetFlowChartEnd();
        return this.GetArraylist(start,for1, for2, content, loopend1, loopend2,end);
    }

    private List<MethodLine> GetTestCase7_SEQUAndIF()
    {
        MethodLine start = this.GetFlowChartStart();
        MethodLine end = this.GetFlowChartEnd();
        MethodLine sequ1 = this.GetMethodLine(MethodLineFunctions.Other, "o2", "o3");
        MethodLine ifline = this.GetMethodLine(MethodLineFunctions.If, "o3", "o4", "o5");
        MethodLine true1 = this.GetMethodLine(MethodLineFunctions.Other, "o4", "o1");
        MethodLine sequ2 = this.GetMethodLine(MethodLineFunctions.Other, "o5", "o1");
        return this.GetArraylist(start, end, sequ1, sequ2, ifline, true1);
    }

    private List<MethodLine> GetExpResultOfTestCase7()
    {
        MethodLine sequ1 = this.GetMethodLine(MethodLineFunctions.Other, "o2", "o3");
        MethodLine ifline = this.GetMethodLine(MethodLineFunctions.If, "o3", "o4", "o5");
        this.AddLineLabels(ifline, "IF0");
        MethodLine true1 = this.GetMethodLine(MethodLineFunctions.Other, "o4", "o1");
        this.AddLineLabels(true1, "IF0");
        MethodLine else1 = this.GetMethodLine(MethodLineFunctions.Else, "", "");
        this.AddLineLabels(else1, "ELSE1");
        MethodLine sequ2 = this.GetMethodLine(MethodLineFunctions.Other, "o5", "o1");
        this.AddLineLabels(sequ2, "ELSE1");
                MethodLine start = this.GetFlowChartStart();
        MethodLine end = this.GetFlowChartEnd();
        return this.GetArraylist(start, sequ1, ifline, true1, else1, sequ2,end);
    }

    private List<MethodLine> GetTestCase8_SequAndFor()
    {
        MethodLine start = this.GetFlowChartStart();
        MethodLine end = this.GetFlowChartEnd();
        MethodLine sequ1 = this.GetMethodLine(MethodLineFunctions.Other, "o2", "o3");
        MethodLine for0 = this.GetMethodLine(MethodLineFunctions.For, "o3", "o4");
        MethodLine content = this.GetMethodLine(MethodLineFunctions.Other, "o4", "o5");
        MethodLine loopend = this.GetMethodLine(FlowChartFunctions.LoopEnd, "o5", "o6");
        MethodLine sequ2 = this.GetMethodLine(MethodLineFunctions.Other, "o6", "o1");
        return this.GetArraylist(start, end, sequ1, sequ2, for0, loopend, content);
    }

    private List<MethodLine> GetExpResultOfTestCase8()
    {
        MethodLine sequ1 = this.GetMethodLine(MethodLineFunctions.Other, "o2", "o3");
        MethodLine for0 = this.GetMethodLine(MethodLineFunctions.For, "o3", "o4");
        this.AddLineLabels(for0, "FOR0");
        MethodLine content = this.GetMethodLine(MethodLineFunctions.Other, "o4", "o5");
        this.AddLineLabels(content, "FOR0");
        MethodLine loopend = this.GetMethodLine(FlowChartFunctions.LoopEnd, "o5", "o6");
        MethodLine sequ2 = this.GetMethodLine(MethodLineFunctions.Other, "o6", "o1");
                MethodLine start = this.GetFlowChartStart();
        MethodLine end = this.GetFlowChartEnd();
        return this.GetArraylist(start,sequ1, for0, content, loopend, sequ2,end);
    }

    private List<MethodLine> GetTestCase9_Try()
    {
        MethodLine start = this.GetFlowChartStart();
        MethodLine end = this.GetFlowChartEnd();
        MethodLine try0 = this.GetMethodLine(MethodLineFunctions.Try, "o2", "o3", "o4");
        MethodLine content0 = this.GetMethodLine(MethodLineFunctions.Other, "o3", "o5");
        MethodLine catch0 = this.GetMethodLine(MethodLineFunctions.Catch, "o4", "o6");
        MethodLine content1 = this.GetMethodLine(MethodLineFunctions.Other, "o6", "o5");
        MethodLine finally0 = this.GetMethodLine(MethodLineFunctions.Finally, "o5", "o7");
        MethodLine content2 = this.GetMethodLine(MethodLineFunctions.Other, "o7", "o1");
        return this.GetArraylist(start, end, try0, catch0, finally0, content0, content1, content2);

    }

    private List<MethodLine> GetExpResultOfTestCase9()
    {
        MethodLine try0 = this.GetMethodLine(MethodLineFunctions.Try, "o2", "o3", "o4");
        this.AddLineLabels(try0, "TRY0");
        MethodLine content0 = this.GetMethodLine(MethodLineFunctions.Other, "o3", "o5");
        this.AddLineLabels(content0, "TRY0");
        MethodLine catch0 = this.GetMethodLine(MethodLineFunctions.Catch, "o4", "o6");
        this.AddLineLabels(catch0, "CATCH1");
        MethodLine content1 = this.GetMethodLine(MethodLineFunctions.Other, "o6", "o5");
        this.AddLineLabels(content1, "CATCH1");
        MethodLine finally0 = this.GetMethodLine(MethodLineFunctions.Finally, "o5", "o7");
        this.AddLineLabels(finally0, "FINALLY2");
        MethodLine content2 = this.GetMethodLine(MethodLineFunctions.Other, "o7", "o1");
        this.AddLineLabels(content2, "FINALLY2");
                MethodLine start = this.GetFlowChartStart();
        MethodLine end = this.GetFlowChartEnd();
        return this.GetArraylist(start,try0, content0, catch0, content1, finally0, content2,end);
    }

    private void AddLineLabels(MethodLine line, String... labels)
    {
        line.GetLineLabels().addAll((new GetArrayList<String>()).Get(labels));
    }

    private List<MethodLine> GetArraylist(MethodLine... lines)
    {
        return (new GetArrayList<MethodLine>()).Get(lines);
    }

    private MethodLine GetMethodLine(String funcType, String selfLabel, String... linkToLabels)
    {
        MethodLine line = ModelFactory.GetMethodLine_Empty();
        line.GetFunctionType().setValue(funcType);
        line.GetSelfLabel().setValue(selfLabel);
        for (String str : linkToLabels)
        {
            StringProperty sp = ModelFactory.GetStringPropertyWithValue(str);
            line.GetLinkToLabels().add(sp);
        }
        return line;
    }
}
