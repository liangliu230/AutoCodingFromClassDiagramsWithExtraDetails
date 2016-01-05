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
public class FunctionStaticStrings
{
    public static final String New = "new";
    public static final String NewVariable = "newVariable";
    public static final String Return = "return";
    public static final String If = "if";
    public static final String Else = "else";
    public static final String Switch = "switch";
    public static final String Case = "case";
    public static final String Default = "default";
    public static final String For = "for";
    public static final String ForEach = "foreach";
    public static final String While = "while";
    public static final String Do = "do";
    
    public static final String SetValue = "setValue";
    public static final String Break = "break";
    public static final String Continue = "continue";

    public static final String Try = "try";
    public static final String TryWithResources = "trywithresources";
    public static final String Catch = "catch";
    public static final String Throw = "throw";
    public static final String Finally = "finally";
    

    
    public static final String Equals = "==";
    public static final String LargerThan = ">";
    public static final String LessThan = "<";
    public static final String NotEqual = "!=";
    public static final String LargerOrEqualThan = ">=";
    public static final String LessOrEqualThan = "<=";
    public static final String Not = "!";

    public static final String AddValue = "+=";
    public static final String SubValue = "-=";
    public static final String MulValue = "*=";
    public static final String DivValue = "/=";
    
    public static final String[] BooleanOperators 
            = new String[]{Equals, LessThan, LessOrEqualThan, NotEqual, LargerThan, LargerOrEqualThan, Not};
    public static final boolean IsBooleanOperator(String str)
    {
        return IsContains(str, BooleanOperators);
    }
    
    private static final boolean IsContains(String str, String[] array)
    {
        for(String bstr : array)
        {
            if(bstr.equals(str)) return true;
        }
        return false;
    }
    public static final String[] UniOperator
            = new String[]{AddValue,SubValue,MulValue,DivValue};
    public static final boolean  IsUniOperator(String str)
    {
        return IsContains(str, UniOperator);
    }
    
    
}
