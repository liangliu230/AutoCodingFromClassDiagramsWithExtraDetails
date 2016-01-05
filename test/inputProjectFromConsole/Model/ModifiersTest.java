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
package inputProjectFromConsole.Model;

import Model.Model.Modifiers;
import javafx.beans.property.BooleanProperty;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Liu
 */
public class ModifiersTest
{
    
    public ModifiersTest()
    {
    }


    @Test
    public void testIsAbstract()
    {
        System.out.println("IsAbstract");
        Modifiers instance = new Modifiers();
        BooleanProperty result = instance.IsAbstract();
        assertEquals(false, result.getValue());
        instance = new Modifiers(true, false, false);
        result = instance.IsAbstract();
        assertEquals(true, result.getValue());
    }

    @Test
    public void testIsStatic()
    {
        System.out.println("IsStatic");
        Modifiers instance = new Modifiers();
        BooleanProperty result = instance.IsStatic();
        assertEquals(false, result.getValue());
        instance = new Modifiers(false, true, false);
        result = instance.IsStatic();
        assertEquals(true, result.getValue());
    }

    @Test
    public void testIsFinal()
    {
        System.out.println("IsFinal");
        Modifiers instance = new Modifiers();
        BooleanProperty result = instance.IsFinal();
        assertEquals(false, result.getValue());
        instance = new Modifiers(false,false,true);
        result = instance.IsFinal();
        assertEquals(true, result.getValue());
    }

    
}
