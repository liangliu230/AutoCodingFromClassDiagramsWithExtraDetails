/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inputProjectFromConsole.Model;

import Model.Model.FullName;
import Model.interfaces.IName;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.easymock.EasyMock.*;
import org.easymock.Mock;
import org.easymock.TestSubject;
/**
 *
 * @author Liu
 */
public class FullNameTest
{
    @Mock
    IName _Package_1, _Package_2, _New_Package_1, _New_Package_2, _New_Package_3,_ClassName;
  
    StringProperty _RetName1 = new SimpleStringProperty("p1"),_RetName2 = new SimpleStringProperty("p2")
            , _RetName3 = new SimpleStringProperty("c1");
    @TestSubject
    FullName _FullName;
    IName[] _PackageList;
    
    
    public FullNameTest()
    {
        _Package_1 = createMock(IName.class);
        _Package_2 = createMock(IName.class);
        _New_Package_1 = createMock(IName.class);
        _New_Package_2 = createMock(IName.class);
        _New_Package_3 = createMock(IName.class);
        _PackageList = new IName[]{_Package_1,_Package_2};
        _ClassName = createMock(IName.class);
        _FullName = new FullName(_PackageList, _ClassName);        
    }

    @Test
    public void testGetPackageNames()
    {
        System.out.println("GetPackageNames");
        FullName instance = new FullName(_PackageList, _ClassName);;
        IName[] expResult = new IName[]{_Package_1,_Package_2};
        IName[] result = instance.GetPackageNames();
        assertEquals(expResult[0], result[0]);
        assertEquals(expResult[1], result[1]);
    }

    @Test
    public void testGetClassName()
    {
        System.out.println("GetClassName");
        FullName instance = new FullName(_PackageList, _ClassName);;
        IName expResult = _ClassName;
        IName result = instance.GetClassName();
        assertEquals(expResult, result);
    }

    @Test
    public void testSetPackageNames()
    {
        System.out.println("SetPackageNames");
        FullName instance = new FullName(_PackageList, _ClassName);
        IName[] arr = new IName[]{_New_Package_1,_New_Package_2,_New_Package_3};
        instance.SetPackageNames(arr);
        IName[] array = instance.GetPackageNames();
        assertEquals(_New_Package_1, array[0]);
        assertEquals(_New_Package_2, array[1]);
        assertEquals(_New_Package_3, array[2]);
    }

    
}
