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
package View;

import Controller.IController;
import View.Interfaces.IGetToolBar;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;

/**
 *
 * @author Liu
 */
public class ToolBarGetter implements IGetToolBar
{

    @Override
    public ToolBar Get(IController controller)
    {
        Button New = new Button("New Project");
        Button Save = new Button("Save");
        Button Load = new Button("Load");
        Button Translate = new Button("Translate");
        
        New.setOnMouseClicked(e->{controller.NewProject();});
        Save.setOnMouseClicked(e->{controller.SaveProject();});
        Load.setOnMouseClicked(e->{controller.LoadProject();});
        Translate.setOnMouseClicked(e->{controller.TranlateProject();});
        
        ToolBar toolBar = new ToolBar(New,Save,Load,Translate);
        toolBar.setOrientation(Orientation.HORIZONTAL);
        return toolBar;
    }
   
}
