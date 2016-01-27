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
import Model.StaticStrings;
import java.util.List;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Liu
 */
public class LineLabelGetter
{

    public void AddLinelabelToMethodLine(List<MethodLine> methodLines, String startLabel, String endLabel)
    {

    }

    private boolean NeedAddNewLineLabel(String functionType)
    {
        switch (functionType)
        {
            case MethodLineFunctions.Catch:
            case MethodLineFunctions.Finally:
            case MethodLineFunctions.For:
            case MethodLineFunctions.ForEach:
            case MethodLineFunctions.If:
            case MethodLineFunctions.Switch:
            case MethodLineFunctions.Try:
            case MethodLineFunctions.TryWithResources:
            case MethodLineFunctions.While:
                return true;
            default:
                return false;
        }
    }

    private String GetCandidateLabel(String functionType, int currentLinkToListIndex)
    {
        switch (functionType)
        {
            case MethodLineFunctions.If:
                if (currentLinkToListIndex == 0)
                {
                    return MethodLineFunctions.If.toUpperCase();
                } else
                {
                    return MethodLineFunctions.Else.toUpperCase();
                }
            case MethodLineFunctions.Switch:
                return MethodLineFunctions.Switch.toUpperCase();
            case MethodLineFunctions.For:
                return MethodLineFunctions.For.toUpperCase();
            case MethodLineFunctions.ForEach:
                return MethodLineFunctions.ForEach.toUpperCase();
            case MethodLineFunctions.While:
                return MethodLineFunctions.While.toUpperCase();
            case MethodLineFunctions.Try:
                return MethodLineFunctions.Try.toUpperCase();
            case MethodLineFunctions.TryWithResources:
                return MethodLineFunctions.TryWithResources.toUpperCase();
            case MethodLineFunctions.Catch:
                return MethodLineFunctions.Catch.toUpperCase();
            case MethodLineFunctions.Finally:
                return MethodLineFunctions.Finally.toUpperCase();
            default:
                throw new UnsupportedOperationException("should not reach");
        }
    }

    private void RemoveLastItem(List<String> list)
    {
        int lastIndex = list.size() - 1;
        String lastItem = list.get(lastIndex);
        list.remove(lastItem);
    }

    private String GetNewLineLabel(List<String> lineLabels, String labelCandidate)
    {
        for (int index = lineLabels.size() - 1; index >= 0; index--)
        {
            if (lineLabels.get(index).startsWith(labelCandidate))// already contains this type
            {
                return lineLabels.get(index) + "_";             // make it different
            }
        }
        return labelCandidate;
    }

    private boolean haveMultipleInEdge(MethodLine currentLine, List<MethodLine> methodLines)
    {
        int count = 0;
        String label = currentLine.GetSelfLabel().getValue();
        for (MethodLine ml : methodLines)
        {

            for (StringProperty linkToLabel : ml.GetLinkToLabels())
            {
                if (linkToLabel.getValue().equals(label))
                {
                    count++;
                    break;
                }
            }
        }

        if (count > 1)
        {
            return true;
        } else
        {
            return false;
        }
    }

}
