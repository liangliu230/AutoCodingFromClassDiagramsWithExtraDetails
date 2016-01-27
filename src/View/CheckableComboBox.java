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

import com.sun.javafx.scene.control.skin.ComboBoxListViewSkin;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;

/**
 *
 * @author Liu
 */
public class CheckableComboBox extends HBox implements IIsChecked
{

    private final CheckBox _CheckBox;
    private final ComboBox<String> _ComboBox;

    public CheckableComboBox(StringProperty stringProperty)
    {
        _CheckBox = new CheckBox();
        _ComboBox = new ComboBox<>();
        _ComboBox.setPrefWidth(150);
        _ComboBox.getEditor().setText(stringProperty.getValue());
        _ComboBox.setEditable(true);
        _ComboBox.setValue(stringProperty.getValue());
        stringProperty.bind(_ComboBox.getEditor().textProperty());
        //stringProperty.addListener(listener -> System.out.println(stringProperty.getValue()));
        _ComboBox.valueProperty().bind(_ComboBox.getEditor().textProperty());
        _ComboBox.getSelectionModel().selectedItemProperty().addListener(listener -> this.OnMouseClickOnDroplist());
       this.getChildren().addAll(_CheckBox, _ComboBox);
    }

    private void OnMouseClickOnDroplist()
    {           
       String selected = _ComboBox.getSelectionModel().getSelectedItem();

        if (selected == null || selected.equals(_ComboBox.getEditor().getText()))
        {
            return;
        }        
        String currentText = _ComboBox.getEditor().getText();
        String lastPart = currentText;
        
        int index = currentText.lastIndexOf(" ");
        if(index != -1)
            lastPart = currentText.substring(index);
        
        if(selected.toLowerCase().contains(lastPart.toLowerCase()))
        {            
            currentText = currentText.replace(lastPart, selected);
             _ComboBox.getEditor().setText(currentText);
        }
        else
        {
            _ComboBox.getEditor().setText(currentText+selected);            
        }
        _ComboBox.requestFocus();        
    }

    @Override
    public boolean IsChecked()
    {
        return _CheckBox.isSelected();
    }

    public void DisableCheckBox()
    {
        //_CheckBox.setVisible(false);
        _CheckBox.setSelected(false);
        this.getChildren().remove(_CheckBox);
    }

    public void SetValueChangeListener(ChangeListener<String> listener)
    {
        _ComboBox.getEditor().textProperty().addListener(listener);
    }

    public ComboBox<String> GetComboBox()
    {
        return _ComboBox;
    }
}
