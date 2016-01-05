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

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;

/**
 *
 * @author Liu
 */
public class RootPane extends Pane
{    
    private final Button _DoubleSize = new Button("Size x4");
    public RootPane(double xLayout,double yLayout)
    {
        this(xLayout, yLayout, 500, 500);
    }
    
    public RootPane(double layoutX, double layoutY, double maxX, double maxY)
    {
        super();
        this.setLayoutX(layoutX);
        this.setLayoutY(layoutY);
        this.setPrefSize(maxX, maxY);
        this.setMaxSize(maxX, maxY);
        _DoubleSize.setLayoutX(10);
        _DoubleSize.setLayoutY(10);
        _DoubleSize.setOnMouseClicked(ev->DoubleSize());
        this.getChildren().add(_DoubleSize);        
    } 
    
    public final void DoubleSize()
    {
        this.setMaxSize(this.getMaxWidth() * 2, this.getMaxHeight() * 2);
        this.setPrefSize(this.getMaxWidth(), this.getMaxHeight());
    }
}
