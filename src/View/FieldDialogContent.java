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
import Model.Field;
import Model.ModelFactory;
import Model.StaticStrings;
import java.util.ArrayList;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 *
 * @author Liu
 */
public class FieldDialogContent extends DialogContentWithAnnotations
{
    private final Field _Field;
   
    public FieldDialogContent(Field field, IHintGenerator generator, ACClass cls)
    {
        super(generator, cls, field);
        _Field = field;
        TextField name = this.GetTextField(field.GetName());
        name.setTooltip(new Tooltip("input field name"));
        CheckableComboBox defaultValue = this.GetCheckableComboBox(_Field.GetDefaultValue());
        defaultValue.DisableCheckBox();
        defaultValue.GetComboBox().setTooltip(new Tooltip("input field default value"));
        ComboBox<String> visibility = new ComboBox<>(this.GetVisibilityList());
        
        CheckableComboBox type = this.GetCheckableComboBox(_Field.GetType());
        type.DisableCheckBox();
        type.GetComboBox().setTooltip(new Tooltip("input field type"));
        CheckBox isStatic = this.GetCheckBox(_Field.isStatic(), StaticStrings.Static);
        CheckBox isFinal = this.GetCheckBox(_Field.isFinal(), StaticStrings.Final);
        BorderPane annotations = this.GetAnnotationBox(_Field.GetAnnotations());
        HBox modifiers = new HBox(visibility, isStatic, isFinal);
        VBox content = new VBox(type, name, defaultValue, modifiers, annotations);
        this.setContent(content);
    }
    

}
