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

import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.StringProperty;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 *
 * @author Liu
 */
@XmlRootElement(name = StaticStrings.Project)
public class Project implements IProject
{
    @XmlElementWrapper(name = StaticStrings.Classes)
    @XmlElement(name = StaticStrings.Class)
    private final List<ACClass> _Classes;
    @XmlElement(name = StaticStrings.Name)
    @XmlJavaTypeAdapter(StringPropertyAdapter.class)
    private final StringProperty _Name;
    public Project()
    {
        _Classes = new ArrayList<>();
        _Name = ModelFactory.GetStringProperty_Empty();
    }
    public Project(List<ACClass> classes, String name)
    {
        _Classes = classes;
        _Name = ModelFactory.GetStringPropertyWithValue(name);
    }

    @Override
    public List<ACClass> GetClasses()
    {
        return _Classes;
    }

    @Override
    public StringProperty GetName()
    {
        return _Name;
    }    
}
