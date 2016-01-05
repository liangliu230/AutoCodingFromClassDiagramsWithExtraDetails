/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.interfaces;

/**
 *
 * @author Liu
 */
public interface IFullName extends IBase
{
    void SetPackageNames(IName[] packageNames);
    IName[] GetPackageNames();
    IName GetClassName();

    @Override
    public String toString();    
}
