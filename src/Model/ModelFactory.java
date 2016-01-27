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
package Model;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/**
 *
 * @author Liu
 */
public class ModelFactory
{
    public static void SaveToXML(Path saveFolderPath, Path saveFilePath, Project item) throws Exception
    {
        JAXBContext jAXBContext = JAXBContext.newInstance(Project.class);
        Marshaller marshaller = jAXBContext.createMarshaller();
        if(Files.exists(saveFolderPath) == false)
            Files.createDirectories(saveFolderPath);
        marshaller.marshal(item, saveFilePath.toFile());        
    }
    
    public static Project LoadFromXML(Path filePath) throws Exception
    {
        JAXBContext context = JAXBContext.newInstance(Project.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        Project project = (Project) unmarshaller.unmarshal(filePath.toFile());
        return project;
    }
    public static Project GetProject_Empty()
    {
        return new Project(new ArrayList<>(), "");
    }
    public static Project GetProjectWithValue(List<ACClass> classes, String name)
    {
        return new Project(classes, name);
    }
    public static Method GetMethod_Empty()
    {
        return new Method("", "", "", false, false, false, new ArrayList<InputParameter>(), new ArrayList<String>(), new ArrayList<>());
    }
    public static Method GetMethodWithValue(String outputType, String methodName, String visibility, boolean  isFinal, boolean isStatic,
            boolean isThrowExceptions, List<InputParameter> inputParameters, List<String> annotations, List<MethodLine> methodLines)    
    {
        return new Method(outputType, methodName, visibility, isFinal, isStatic, isThrowExceptions, inputParameters, annotations, methodLines);
    }
    
    public static MethodLine GetMethodLineCopy(MethodLine line)
    {
        return new  MethodLine(
                line.GetOutputType().getValue(),
                line.GetOutputVariableName().getValue(), 
                line.GetFunctionType().getValue(), 
                line.GetFunctionContent().getValue(), 
                line.GetLayoutX().getValue(), 
                line.GetLayoutY().getValue(), 
                line.GetSelfLabel().getValue(), 
                ModelFactory.CopyStringPropertyList(line.GetLinkToLabels()), 
                ModelFactory.CopyStringList(line.GetLineLabels()));
    }
    public static List<String> CopyStringPropertyList(List<StringProperty> list)
    {
        List<String> nlist = new ArrayList<>();
        for(StringProperty sp : list)
            nlist.add(sp.getValue());
        return nlist;
    }
    private static List<String> CopyStringList(List<String> list)
    {
        List<String> nlist = new ArrayList<>();
        for(String str : list)
            nlist.add(str);
        return nlist;
    }
    public static MethodLine GetMethodLine_Empty()
    {
        return new MethodLine("", "", "", "", 0, 0, "", new ArrayList<>(), new ArrayList<>());
    }
    public static MethodLine GetMethodLineWithValue(String outputType, String outputVariableName, String functionType, String functionContent, double layoutX, double layoutY, String selfLabel, List<String> linkToLabels, List<String> lineLabels)
    {
        return new MethodLine(outputType, outputVariableName, functionType, functionContent, layoutX, layoutY, selfLabel, linkToLabels, lineLabels);
    }
    public static Field GetField_Empty()
    {
        return new Field("", "", "", new ArrayList<String>(), "", false, false);
    }
    public static Field GetFieldWithValue(String type, String name, String visibility, String defaultValue, List<String> annotations, boolean isStatic, boolean isFinal)
    {
        return new Field(name, type, defaultValue, annotations, visibility, isStatic, isFinal);
    }
    public static ACClass GetClass_Empty()            
    {
        return new ACClass("", "", false, false, new ArrayList<String>(), new ArrayList<String>(), new ArrayList<Field>(), new ArrayList<Method>(), new ArrayList<String>(), false, false, 0, 0);
    }
    public static ACClass GetClassWithValue(String name, String visibility, boolean isFinal, boolean isAbstract, 
                    List<String> _extends, List<String> _implements, List<Field> fields,
                    List<Method> methods, List<String> annotations, boolean  isEnum,
                    boolean  isInterface, double layoutX, double layoutY)
    {
        return new ACClass(name, visibility, isFinal, isAbstract, _extends, _implements, fields, methods, annotations, isEnum, isInterface, layoutX, layoutY);
    }
    public static DoubleProperty GetDoubleProperty_Empty()
    {
        return new SimpleDoubleProperty(0.0);
    }
    public static DoubleProperty GetDoublePropertyWithValue(double value)
    {
        return new SimpleDoubleProperty(value);
    }
    public static BooleanProperty GetBooleanProperty_Empty()
    {
        return new SimpleBooleanProperty(false);
    }
    public static BooleanProperty GetBooleanPropertyWithValue(boolean  value)
    {
        return new SimpleBooleanProperty(value);
    }
    public static StringProperty GetStringProperty_Empty()
    {
        return new SimpleStringProperty("");
    }
    public static StringProperty GetStringPropertyWithValue(String value)
    {
        return new SimpleStringProperty(value);
    }
    public static InputParameter GetInputParameter_Empty()
    {
        String type = "";
        String name = "";
        return new InputParameter(type, name);
    }
    public static InputParameter GetInputParameterWithValue(String type, String name)
    {
        return new InputParameter(type, name);
    }
    static List<StringProperty> GetStringPropertyList(List<String> list)
    {
        List<StringProperty> retList = new ArrayList<>();
        for(String str : list)
        {
            StringProperty sp = GetStringPropertyWithValue(str);
            retList.add(sp);
        }
        return retList;
    }
}
