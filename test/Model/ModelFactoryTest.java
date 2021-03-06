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

import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Liu
 */
public class ModelFactoryTest
{
    
    public ModelFactoryTest()
    {
    }

    @Test
    public void testSaveToXML()
    {
        Project project = (Project) ModelFactory.GetProject_Empty();
        Path saveFolder = Paths.get("./000/");
        Path filePath = Paths.get("./000/xml.xml");
        try
        {
        ModelFactory.SaveToXML(saveFolder, filePath, project);
        }
        catch(Exception exception)
        {
            exception.printStackTrace(System.out);
            fail("exception");
        }
    }
    
    @Test
    public void testLoadFromXML()
    {
        Path loadFolder = Paths.get("./000/");
        Path filePath = Paths.get("./000/xml.xml");
        try{
            IProject project =ModelFactory.LoadFromXML(filePath);
            assertTrue(project.GetClasses().isEmpty());
        }
        catch(Exception exception)
        {
            exception.printStackTrace(System.out);
            fail();
        }
    }
    
}
