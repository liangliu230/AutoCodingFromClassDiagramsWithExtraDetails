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
package Model.Model;

import Model.interfaces.IClass;
import Model.interfaces.IPackage;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.easymock.EasyMock.*;
import org.easymock.Mock;

/**
 *
 * @author Liu
 */
public class ProjectTest
{
    @Mock
    IClass _C1 = createMock(IClass.class),
            _C2 = createMock(IClass.class);
    @Mock
    IPackage _P1 = createMock(IPackage.class);
    
    public ProjectTest()
    {
    }

    @Test
    public void testGetClasses()
    {
        System.out.println("GetClasses");
        Project instance = new Project();
        List<IClass> result = instance.GetClasses();
        assertEquals(0, result.size());
        List<IClass> list = new ArrayList<>();
        list.add(_C1);list.add(_C2);
        List<IPackage> pl = new ArrayList<>();
        pl.add(_P1);
        instance = new Project(new Name("abc"),list,pl);
        result = instance.GetClasses();
        assertEquals(list, result);
        assertEquals(2, result.size());
        List<IPackage> ps = instance.GetPackages();
        assertEquals(ps, pl);
        assertEquals(ps.size(), 1);
        assertTrue("abc".equals(instance.GetName().GetName().getValue()));
    }

    
}
