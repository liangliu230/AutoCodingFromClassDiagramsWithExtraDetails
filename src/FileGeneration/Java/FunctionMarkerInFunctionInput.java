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

/**
 * 
 * @author Liu
 */
public class FunctionMarkerInFunctionInput
{
    public static final String TypeAndValue = "typeAndValue";
    public static final String TypeNameAndValue = "typenameandvalue";
    public static final String VariableOnly = "variableOnly";
    public static final String ValueOnly = "valueOnly";
    public static final String Empty = "empty"; // occupy one location
    private static final String[] _All = new String[]{TypeAndValue, VariableOnly, ValueOnly, Empty, TypeNameAndValue};
    public static boolean IsMarker(String str)
    {
        for(String s : _All)
        {
            if(s.equals(str)) return true;
        }
        return false;
    }
}
