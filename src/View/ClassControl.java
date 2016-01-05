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

import Model.Model.Modifiers;
import Model.StaticStrings;
import Model.interfaces.IClass;
import Model.interfaces.IModifiers;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javafx.beans.property.ListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 *
 * @author Liu
 */
public class ClassControl extends Button
{

    public ClassControl(double layoutX, double layoutY, IClass _Class)
    {
        super();
        this.setLayoutX(layoutX);
        this.setLayoutY(layoutY);
        _ACClass = _Class;
        this.SetButtonText();
    }

    public final IClass _ACClass;

    public final void SetButtonText()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("type: ");
        if (_ACClass.IsInterface().getValue())
        {
            builder.append("interface");
        } else if (_ACClass.IsEnum().getValue())
        {
            builder.append("enum");
        } else
        {
            builder.append("class");
        }
        builder.append("\n");
        builder.append("name: ").append(_ACClass.GetFullName().toString());
        this.setText(builder.toString());
    }



}
