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
import Model.ModelFactory;
import Model.StaticStrings;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/**
 *
 * @author Liu
 */
public class ModelActions
{

    public void SaveToFile(Path folder, Path file, Model_Java item) throws Exception
    {
        JAXBContext context = JAXBContext.newInstance(Model_Java.class);
        Marshaller marshaller = context.createMarshaller();
        if (Files.exists(folder) == false)
        {
            Files.createDirectories(folder);
        }
        marshaller.marshal(item, file.toFile());
    }

    public Model_Java LoadFromFile(Path file) throws Exception
    {
        System.out.println("loading cache file");
        JAXBContext context = JAXBContext.newInstance(Model_Java.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        Model_Java model = (Model_Java) unmarshaller.unmarshal(file.toFile());
        System.out.println("load cache file ended.");
        return model;
    }
    
    final String JAVA = "java";
    final String JAVAX = "javax";
    final String JAVAFX = "javafx";
    final String RTPath = "C:\\Program Files\\Java\\jre1.8.0_66\\lib\\rt.jar";
    final String FXPath = "C:\\Program Files\\Java\\jre1.8.0_66\\lib\\ext\\jfxrt.jar";
    final String ENDING = ".class";
    final String SubClass = "$";
    final String Internal = "internal";

    public void CreateModel(Path folder, Path filePath) throws Exception
    {
        Path rtPath = Paths.get(RTPath);
        Path jfxrtPath = Paths.get(FXPath);
        File[] files = new File[]
        {
            rtPath.toFile(), jfxrtPath.toFile()
        };
        List<ACClass> clsList = new ArrayList<>();
        for (File file : files)
        {
            System.out.println("reading " + file);
            try (JarInputStream jarInputStream
                    = new JarInputStream(
                            new BufferedInputStream(
                                    new FileInputStream(file))))
            {
                JarEntry jarEntry = null;
                while ((jarEntry = jarInputStream.getNextJarEntry()) != null)
                {

                    if (jarEntry.isDirectory())
                    {
                        continue;
                    }
                    String jarEntryName = jarEntry.getName();
                    if (jarEntryName.contains(SubClass)
                            || jarEntryName.contains(Internal) || jarEntryName.contains("_"))
                    {
                        continue;
                    }
                    if (jarEntryName.startsWith(JAVAFX)
                            || jarEntryName.startsWith(JAVA)
                            || jarEntryName.startsWith(JAVAX))
                    {

                        String clsName = jarEntryName.substring(0, jarEntryName.lastIndexOf("."));
                        clsName = clsName.replace("/", ".");
                        Class<?> cls = Class.forName(clsName);
                        List<String> clsAnnotations = this.GetAnnotations(cls.getAnnotations());
                        String visibility = this.GetVisibility(cls.getModifiers());
                        boolean isFinal = this.GetIsFinal(cls.getModifiers());
                        boolean isAbstract = this.GetIsAbstract(cls.getModifiers());
                        boolean isInterface = cls.isInterface();
                        boolean isEnum = cls.isEnum();
                        String name = cls.getName();
                        List<Field> fields = this.GetFields(cls.getFields());
                        List<Method> methods = this.GetMethods(cls.getMethods());
                        List<String> extds = this.GetExtends(cls);
                        List<String> imps = this.GetImplements(cls);
                        ACClass accls = ModelFactory.GetClassWithValue(name, visibility, isFinal, isAbstract, extds, imps, fields, methods, clsAnnotations, isEnum, isInterface, 0, 0);
                        clsList.add(accls);
                    }
                }
                            } catch (Exception e)
                            {
                                e.printStackTrace(System.out);
                            }

        }
        Model_Java model = new Model_Java(System.getProperty("java.version"), clsList);
        this.SaveToFile(folder, filePath, model);
    }

    private String GetVisibility(int modifiers)
    {
        if (Modifier.isPrivate(modifiers))
        {
            return StaticStrings.Private;
        }
        if (Modifier.isProtected(modifiers))
        {
            return StaticStrings.Protected;
        }
        if (Modifier.isPublic(modifiers))
        {
            return StaticStrings.Public;
        }
        return StaticStrings.Package;
    }

    private boolean GetIsStatic(int modifiers)
    {
        return Modifier.isStatic(modifiers);
    }

    private boolean GetIsFinal(int modifiers)
    {
        return Modifier.isFinal(modifiers);
    }

    private boolean GetIsAbstract(int modifiers)
    {
        return Modifier.isAbstract(modifiers);
    }

    private List<String> GetAnnotations(Annotation[] annotations)
    {
        List<String> list = new ArrayList<>();
        for (Annotation ann : annotations)
        {
            String str = ann.toString();
            list.add(str.substring(1));
        }
        return list;
    }

    private List<Field> GetFields(java.lang.reflect.Field[] fields)
    {
        List<Field> list = new ArrayList<>();
        for (java.lang.reflect.Field field : fields)
        {
            String visibility = this.GetVisibility(field.getModifiers());
            boolean isStatic = this.GetIsStatic(field.getModifiers());
            boolean isFinal = this.GetIsFinal(field.getModifiers());
            String type = field.getGenericType().toString();
            String name = field.getName();
            if(name.contains("_"))
                continue;
            List<String> annotationList = this.GetAnnotations(field.getAnnotations());
            String defaultValue = "";

            Field item = ModelFactory.GetFieldWithValue(type, name, visibility, defaultValue, annotationList, isStatic, isFinal);
            list.add(item);
        }
        return list;
    }

    private List<Method> GetMethods(java.lang.reflect.Method[] methods)
    {
        List<Method> list = new ArrayList<>();
        for (java.lang.reflect.Method mthd : methods)
        {
            String visibility = this.GetVisibility(mthd.getModifiers());
            boolean isStatic = this.GetIsStatic(mthd.getModifiers());
            boolean isfinal = this.GetIsFinal(mthd.getModifiers());
            String outputType = mthd.getGenericReturnType().toString();
            List<InputParameter> inputParameters = this.GetInputParameters(mthd.getParameters());
            List<String> annotations = this.GetAnnotations(mthd.getAnnotations());
            String name = mthd.getName();
            if(name.contains("_")) continue;
            boolean isThrowExceptions = false;
            if (mthd.getExceptionTypes().length > 0)
            {
                isThrowExceptions = true;
            }
            Method method = ModelFactory.GetMethodWithValue(outputType, name, visibility, isfinal, isStatic, isThrowExceptions, inputParameters, annotations, new ArrayList<>());
            list.add(method);
        }
        return list;
    }

    private List<InputParameter> GetInputParameters(Parameter[] pars)
    {
        List<InputParameter> list = new ArrayList<>();
        for (Parameter par : pars)
        {
            String name = par.getName();
            String type = par.getType().toString();
            int lastIndexOfSpace = type.lastIndexOf(" ");
            if (lastIndexOfSpace != -1)
            {
                type = type.substring(lastIndexOfSpace);
            }
            if (par.getParameterizedType() instanceof ParameterizedType)
            {
                type = ((ParameterizedType) par.getParameterizedType()).getTypeName();
            }
            InputParameter ip = ModelFactory.GetInputParameterWithValue(type, name);
            list.add(ip);
        }
        return list;
    }

    private List<String> GetExtends(Class<?> cls)
    {
        List<String> list = new ArrayList<>();
        if (cls.getGenericSuperclass() != null)
        {
            String name = cls.getGenericSuperclass().getTypeName();
            list.add(name);
        }
        return list;
    }

    private List<String> GetImplements(Class<?> cls)
    {
        List<String> list = new ArrayList<>();
        for (Type interf : cls.getGenericInterfaces())
        {
            list.add(interf.getTypeName());
        }
        return list;
    }

}
