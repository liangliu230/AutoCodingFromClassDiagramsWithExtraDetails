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
import Utility.GetArrayList;
import View.FlowChartFunctions;
import View.FlowChartNodeType;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Liu
 */
public class SortNodeInFlowChartGraph
{

    public List<MethodLine> SortAndMark(List<MethodLine> unsortedLines)
    {       
        List<MethodLine> sorted = this.Sort(unsortedLines);
        List<MethodLine> marked = this.Mark(sorted);
        return marked;
    }

    private List<MethodLine> Sort(List<MethodLine> unsortedLines)
    {
        FlatGraph fg = new FlatGraph(unsortedLines);
        MethodLine start = this.GetStartLine(unsortedLines);
        fg.Flat(start);
        return fg.GetSortedLine();
    }

    private MethodLine GetStartLine(List<MethodLine> lines)
    {
        for (MethodLine line : lines)
        {
            if (line.GetFunctionType().getValue().equals(FlowChartFunctions.START))
            {
                return line;
            }
        }
        throw new UnsupportedOperationException("no start");
    }

    private boolean IsPreviousLineTypeIsCaseOrDefault(int previousindex, List<MethodLine> list)
    {
        if (list.get(previousindex).GetFunctionType().getValue().equals(MethodLineFunctions.Case)
                || list.get(previousindex).GetFunctionType().getValue().equals(MethodLineFunctions.Default))
        {
            return true;
        } else
        {
            return false;
        }
    }

    private List<MethodLine> Mark(List<MethodLine> unmarkedSortedLines)
    {
        GetArrayList<String> copier = new GetArrayList<>();
        int totalLabelOccurrance = 0;
        List<MethodLine> markedList = new ArrayList<>();
        List<String> linelabels = new ArrayList<>();
        markedList.add(unmarkedSortedLines.get(0));//put start line in

        for (int index = 1; index < unmarkedSortedLines.size() - 1; index++)
        {
            MethodLine line = unmarkedSortedLines.get(index);
            int inEdgeNumber = this.GetInEdgeOfMethodLine(line, unmarkedSortedLines);
            String lineFunctype = line.GetFunctionType().getValue();

            if (inEdgeNumber > 1) //  correction by number
            {
                if (this.GetLastElementOfList(linelabels).toLowerCase().contains(MethodLineFunctions.Switch))
                {
                    if (false
                            == this.IsPreviousLineTypeIsCaseOrDefault(index - 1, unmarkedSortedLines))
                    {
                        linelabels = this.RemoveLastSeveral(1, linelabels); // only remove switch. this is because there might be many case and default, but only switch needs brackets
                    }
                } else
                {
                    linelabels = this.RemoveLastSeveral(inEdgeNumber - 1, linelabels);
                }
            }

            if (lineFunctype.equals(MethodLineFunctions.Else))
            {
                if (linelabels.get(linelabels.size() - 1).toLowerCase().contains(MethodLineFunctions.If))
                {
                    linelabels.remove(linelabels.size() - 1);

                } else
                {
                    linelabels = this.RemoveLastSeveral(2, linelabels); // remove the last non-if label and if
                }
                linelabels.add(MethodLineFunctions.Else.toUpperCase() + totalLabelOccurrance);
                totalLabelOccurrance++;
            }

            if (lineFunctype.equals(MethodLineFunctions.Catch)
                    || lineFunctype.equals(MethodLineFunctions.Finally))
            {
                if (linelabels.size() > 0) // finally can have two income edges, it is possible to have zero line labels in the list
                {
                    if (this.GetLastElementOfList(linelabels).toLowerCase().contains(MethodLineFunctions.Catch) == false
                            && this.GetLastElementOfList(linelabels).toLowerCase().contains(MethodLineFunctions.Finally) == false)
                    {
                        linelabels = this.RemoveLastSeveral(1, linelabels); // remove last try
                    } else
                    {
                        linelabels = this.RemoveLastSeveral(2, linelabels); // remove try and catch or try and finally)
                    }
                }

                if (line.GetFunctionType().getValue().equals(MethodLineFunctions.Catch))
                {
                    linelabels.add(MethodLineFunctions.Catch.toUpperCase() + totalLabelOccurrance);
                } else
                {
                    linelabels.add(MethodLineFunctions.Finally.toUpperCase() + totalLabelOccurrance);
                }
                totalLabelOccurrance++;
            }

            if (lineFunctype.equals(MethodLineFunctions.For))
            {
                linelabels.add(MethodLineFunctions.For.toUpperCase() + totalLabelOccurrance);
                totalLabelOccurrance++;
            }

            if (lineFunctype.equals(MethodLineFunctions.While))
            {
                linelabels.add(MethodLineFunctions.While.toUpperCase() + totalLabelOccurrance);
                totalLabelOccurrance++;
            }
            if (lineFunctype.equals(MethodLineFunctions.ForEach))
            {
                linelabels.add(MethodLineFunctions.ForEach.toUpperCase() + totalLabelOccurrance);
                totalLabelOccurrance++;
            }

            if (lineFunctype.equals(FlowChartFunctions.LoopEnd))
            {
                linelabels = this.RemoveLastSeveral(1, linelabels); // remove last for/while/foreach
            }

            if (lineFunctype.equals(MethodLineFunctions.If))
            {
                linelabels.add(MethodLineFunctions.If.toUpperCase() + totalLabelOccurrance);
                totalLabelOccurrance++;
            }
            if (lineFunctype.equals(MethodLineFunctions.Switch))
            {
                linelabels.add(MethodLineFunctions.Switch.toUpperCase() + totalLabelOccurrance);
                totalLabelOccurrance++;
            }

            if (lineFunctype.equals(MethodLineFunctions.Try))
            {
                linelabels.add(MethodLineFunctions.Try.toUpperCase() + totalLabelOccurrance);
                totalLabelOccurrance++;
            }

            if (lineFunctype.equals(FlowChartFunctions.LoopEnd) == false)
            {
                List<String> copy = copier.Copy(linelabels);
                line.GetLineLabels().clear();
                line.GetLineLabels().addAll(copy);
            }
            markedList.add(line);
        }
        
        markedList.add(unmarkedSortedLines.get(unmarkedSortedLines.size() - 1));// put end line in
        return markedList;
    }


    private String GetLastElementOfList(List<String> list)
    {
        return list.get(list.size() - 1);
    }

    private List<String> RemoveLastSeveral(int number, List<String> list)
    {
        ArrayList<String> l = new ArrayList<>();
        if (list.size() <= number)
        {
            return l;
        }
        for (int i = 0; i < list.size() - number; i++)
        {
            l.add(list.get(i));
        }
        return l;
    }

    private int GetInEdgeOfMethodLine(MethodLine line, List<MethodLine> lines)
    {
        int count = 0;
        String label = line.GetSelfLabel().getValue();
        for (MethodLine l : lines)
        {
            boolean isIn = this.IsLabelInList(l.GetLinkToLabels(), label);
            if (isIn)
            {
                count++;
            }
        }
        return count;
    }

    private boolean IsLabelInList(List<StringProperty> list, String label)
    {
        for (StringProperty sp : list)
        {
            if (sp.getValue().equals(label))
            {
                return true;
            }
        }
        return false;
    }
}
