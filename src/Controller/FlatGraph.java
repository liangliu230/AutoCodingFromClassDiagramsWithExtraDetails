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
import java.util.List;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Liu
 */
public class FlatGraph
{
    private final java.util.List<MethodLine> _SortedLines = new java.util.ArrayList<>();
    private final List<MethodLine> _UnsortedLines;
    
    public FlatGraph(List<MethodLine> unsortedLines)
    {
        _UnsortedLines = unsortedLines;
    }
    
    public List<MethodLine> GetSortedLine()
    {
        return _SortedLines;
    }
    
    public void Flat(MethodLine startLine)
    {        
        if(_SortedLines.contains(startLine))
            return;
        
        _SortedLines.add(startLine);
        for(int index = 0; index < startLine.GetLinkToLabels().size(); index++)
        {            
            if(index == 1 && startLine.GetFunctionType().getValue().equals(MethodLineFunctions.If))
            {
                MethodLine elseLine = this.GetElseLine();
                _SortedLines.add(elseLine);
            }                       
            
            String label = startLine.GetLinkToLabels().get(index).getValue();
            MethodLine line = this.GetMethodLineWithLabel(label);
            int difference = this.GetAppearanceDifferenceBetweenUnsortedLinesAndSortedLines(line);
            if(difference > 0)
                return;
            else
            {
               this.Flat(line);
            }
        }
    }
    
    private MethodLine GetElseLine()
    {
        MethodLine elseLine = ModelFactory.GetMethodLine_Empty();
        elseLine.GetFunctionType().setValue(MethodLineFunctions.Else);
        return elseLine;
    }
    
    private MethodLine GetMethodLineWithLabel(String label)
    {
        for(MethodLine line : _UnsortedLines)
        {
            if(line.GetSelfLabel().getValue().equals(label))
                return line;
        }
        throw  new UnsupportedOperationException("unknown label : " + label);
    }
    
    private int GetAppearanceDifferenceBetweenUnsortedLinesAndSortedLines(MethodLine line)
    {
        int count = 0;
        String label = line.GetSelfLabel().getValue();
        
        for(MethodLine l : _UnsortedLines)
        {
             boolean contains = this.IsLabelInList(l.GetLinkToLabels(), label);
             if(contains)
                 count++;
        }
        for(MethodLine l : _SortedLines)
        {
            boolean contains = this.IsLabelInList(l.GetLinkToLabels(), label);
            if(contains)
                count --;
        }
        if(count < 0)
            throw new UnsupportedOperationException("count = " + count + "\n" + line.GetFunctionType().getValue() + " " + line.GetSelfLabel().getValue());
        
        return count;
    }
    
    private boolean IsLabelInList(List<StringProperty> list, String label)
    {
        for(StringProperty sp : list)
        {
            if(sp.getValue().equals(label))
                return true;
        }
        return false;
    }
}
