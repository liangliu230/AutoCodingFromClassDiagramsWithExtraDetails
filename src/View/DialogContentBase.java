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
package View;

import Hint.IHintGenerator;
import Model.ACClass;
import Model.MethodLine;
import Model.ModelFactory;
import Model.StaticStrings;
import com.sun.javafx.scene.control.skin.ComboBoxListViewSkin;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 *
 * @author Liu
 */
public class DialogContentBase extends ScrollPane
{

    protected final IHintGenerator _Generator;
    protected final ACClass _Cls;

    public DialogContentBase(IHintGenerator generator, ACClass cls)
    {
        _Generator = generator;
        _Cls = cls;
    }

    protected ComboBox<String> GetVisibility(StringProperty visibility)
    {
        ComboBox<String> box = new ComboBox<>(this.GetVisibilityList());
        box.setValue(visibility.getValue());
        visibility.bind(box.valueProperty());
        box.setTooltip(new Tooltip("choose visibility"));
        return box;
    }
    
    protected CheckBox GetCheckBox(BooleanProperty property, String text)
    {
        CheckBox box = new CheckBox(text);
        box.setSelected(property.getValue());
        property.bind(box.selectedProperty());
        return box;
    }
    
    protected ObservableList<String> GetVisibilityList()
    {
        return FXCollections.observableArrayList(StaticStrings.Public,
                StaticStrings.Protected,
                StaticStrings.Private,
                StaticStrings.Package);
    }

    protected TextField GetTextField(StringProperty stringProperty)
    {
        TextField field = new TextField(stringProperty.getValue());
        field.setText(stringProperty.getValue());
        stringProperty.bind(field.textProperty());
        return field;
    }
    protected ComboBoxStringChangeListener GetComboBoxChangeListener(ComboBox<String> box)
    {
        return new ComboBoxStringChangeListener(box, _Generator, new ArrayList<MethodLine>(), _Cls);
    }

    protected CheckableButton GetCheckableButton(StringExpression[] bindExpressions, EventHandler<MouseEvent> handler)
    {
        return new CheckableButton(bindExpressions, handler);
    }

    protected CheckableComboBox GetCheckableComboBox(StringProperty bindStringProperty)
    {
        CheckableComboBox box = new CheckableComboBox(bindStringProperty);
        box.SetValueChangeListener(this.GetComboBoxChangeListener(box.GetComboBox()));       
        return box;
    }
    
    protected final java.util.Map<CheckableComboBox, StringProperty> _AnnotationsProjection = new HashMap<>();
    
    
}
