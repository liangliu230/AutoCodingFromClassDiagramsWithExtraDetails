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
package Util;

import Model.interfaces.IClass;
import Model.interfaces.IField;
import Model.interfaces.IMethod;
import Model.interfaces.IMethodLine;
import Model.interfaces.IPackage;
import Model.interfaces.IProject;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Liu
 */
public class ReadFromXMLTest
{

    public ReadFromXMLTest()
    {
    }

    @Test
    public void testRead() throws Exception
    {
        System.out.println("Read");
        String file = "./test_1.xml";
        String schemaFile = "./schema.xsd";

        ReadFromXML instance = new ReadFromXML();
        IProject result = instance.Read(file, schemaFile);
        String n = result.GetName().GetName().getValue();
        assertTrue(n.equals("abc"));
        List<IClass> classes = result.GetClasses();
        List<IPackage> packages = result.GetPackages();

        assertEquals(3, packages.size());
        assertEquals(4, classes.size());

        for (IPackage pac : packages)
        {
            switch (pac.GetFullName().GetClassName().GetName().getValue())
            {
                default:
                    assertFalse("unknow package name: " + pac.GetFullName().toString(), true);
                    break;
                case "package1":
                    assertTrue("package1".equals(pac.GetFullName().toString()));
                    assertTrue(pac.GetLayoutX().getValue() == 0.0);
                    assertTrue(pac.GetLayoutY().getValue() == 0.0);
                    assertTrue(pac.IsInitialize().getValue());
                    break;
                case "package2":
                    assertTrue("package2".equals(pac.GetFullName().toString()));
                    assertTrue(pac.GetLayoutX().getValue() == 1.0);
                    assertTrue(pac.GetLayoutY().getValue() == 1.0);
                    assertTrue(pac.IsInitialize().getValue());
                    break;
                case "interfaces":
                    System.out.println(pac.GetFullName().GetPackageNames().length);
                    System.out.println("full name: " + pac.GetFullName().toString());
                    assertTrue("package1.interfaces".equals(pac.GetFullName().toString()));
                    assertTrue(pac.GetLayoutX().getValue() == 1.0);
                    assertTrue(pac.GetLayoutY().getValue() == 1.0);
                    assertTrue(pac.IsInitialize().getValue());
                    break;
            }
        }

        for (IClass cls : classes)
        {
            switch (cls.GetFullName().GetClassName().GetName().getValue())
            {
                case "abstractclass":
                    assertTrue("package1.abstractclass".equals(cls.GetFullName().toString()));
                    assertTrue(cls.IsInitialize().getValue());
                    assertTrue(true == cls.GetModifiers().IsAbstract().getValue());
                    assertTrue(false == cls.GetModifiers().IsFinal().getValue());
                    assertTrue(false == cls.GetModifiers().IsStatic().getValue());
                    assertTrue(false == cls.IsInterface().getValue());
                    assertTrue(false == cls.IsEnum().getValue());
                    System.out.println(cls.GetVisibility().GetVisibility().getValue());
                    assertTrue("public".equals(cls.GetVisibility().GetVisibility().getValue()));
                    assertTrue(0 == cls.GetExtends().size());
                    assertTrue(0 == cls.GetImplements().size());
                    assertTrue(2 == cls.GetFields().size());
                    for (IField field : cls.GetFields())
                    {
                        switch (field.GetName().GetName().getValue())
                        {
                            case "_Integer":

                                assertTrue(field.GetType().toString().equals("int"));
                                assertTrue(field.GetModifiers().IsAbstract().getValue() == false);
                                assertTrue(field.GetModifiers().IsFinal().getValue() == false);
                                assertTrue(field.GetModifiers().IsStatic().getValue() == false);
                                assertTrue("protected".equals(field.GetVisibility().GetVisibility().getValue()));

                                assertTrue(field.GetDefaultValue().GetInputVariables().get(0).GetName().getValue().equals("1"));
                                break;
                            case "_List":
                                assertTrue(field.GetType().toString().equals("java.util.List<Integer>"));
                                assertTrue(field.GetModifiers().IsAbstract().getValue() == false);
                                assertTrue(field.GetModifiers().IsFinal().getValue() == true);
                                assertTrue(field.GetModifiers().IsStatic().getValue() == false);
                                assertTrue("protected".equals(field.GetVisibility().GetVisibility().getValue()));
                                break;
                            default:
                                assertFalse("unknown field in abstractclass: " + field.GetName().GetName().getValue(), true);
                                break;
                        }
                    }
                    assertEquals(5, cls.GetMethods().size());
                    for (IMethod method : cls.GetMethods())
                    {
                        switch (method.GetName().getValue())
                        {
                            case "abstractclass":
                                System.out.println("abstract class : " + method.GetOutputType().toString());
                                assertTrue("".equals(method.GetOutputType().toString()));
                                assertTrue(0 == method.GetInputParameters().size());
                                assertTrue(0 == method.GetAnnotations().size());
                                assertTrue(1 == method.GetMethodBody().size());
                                IMethodLine line = method.GetMethodBody().get(0);
                                assertTrue("new List<Integer>".equals(line.GetFunction().GetFunction().getValue()));
                                assertTrue("_List".equals(line.GetOutputVariable().GetName().getValue()));
                                assertTrue(0 == line.GetLineNumber().GetLineNumber().getValue());
                                assertTrue(0 == line.GetLineLabels().size());
                                assertTrue(method.GetVisibility().GetVisibility().getValue().equals("public"));
                                assertTrue(method.GetModifiers().IsAbstract().getValue() == false);
                                assertTrue(false == method.GetModifiers().IsFinal().getValue());
                                assertTrue(false == method.GetModifiers().IsStatic().getValue());
                                assertTrue(true == method.IsStrictOrder().getValue());
                                break;
                            case "SetInteger":
                                break;
                            case "GetInteger":
                                break;
                            case "GetList":
                                break;
                            case "GetVersion":
                                break;
                            default:
                                fail("unknown method in abstractclass: ");
                                break;
                        }
                    }
                    assertEquals(10.0, cls.GetLayoutX().getValue(), 0.0);
                    assertEquals(10.0, cls.GetLayoutY().getValue(), 0.0);
                    break;
                case "enumclass":
                    assertEquals(true, cls.IsEnum().getValue());
                    break;
                case "IAddArray":
                    assertEquals(true, cls.IsInterface().getValue());
                    break;
                case "TestClass":
                    assertEquals(true, cls.GetModifiers().IsFinal().getValue());
                    break;
                default:
                    assertFalse("unknow name: " + cls.GetFullName().toString(), true);
                    break;
            }
        }

    }
}
