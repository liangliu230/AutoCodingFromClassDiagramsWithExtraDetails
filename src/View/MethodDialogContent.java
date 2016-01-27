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
import Model.InputParameter;
import Model.Method;
import Model.MethodLine;
import Model.ModelFactory;
import Model.StaticStrings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 *
 * @author Liu
 */
public class MethodDialogContent extends DialogContentWithAnnotations
{
    private final Model.Method _Method;
    public MethodDialogContent(Method method, IHintGenerator generator, ACClass cls)
    {
        super(generator, cls, method);
        _Method = method;
        CheckableComboBox outputType = this.GetCheckableComboBox(_Method.GetOutputType());
        outputType.DisableCheckBox();
        outputType.SetValueChangeListener(this.GetComboBoxChangeListener(outputType.GetComboBox()));
        TextField name = this.GetTextField(_Method.GetName());        
        ComboBox<String> visibility = this.GetVisibility(_Method.GetVisibility());
        CheckBox isStatic = this.GetCheckBox(_Method.isStatic(), StaticStrings.Static);
        CheckBox isFinal = this.GetCheckBox(_Method.isFinal(), StaticStrings.Final);
        HBox modifiers = new HBox(visibility, isStatic, isFinal);
        BorderPane annotations = this.GetAnnotationBox(_Method.GetAnnotations());
        BorderPane inputParameters = this.GetInputParameterBox(_Method.GetInputParameters());
        Button body = new Button("Method Body");
        body.setOnMouseClicked(event -> this.OnMethodBodyButtonClicked());
        VBox content = new VBox(outputType, name, modifiers, inputParameters, annotations, body);
        this.setContent(content);
    }
    private void OnMethodBodyButtonClicked()
    {
        FlowChartDialogContent content = new FlowChartDialogContent(_Generator, _Cls, _Method);
        Dialog dialog = new Dialog();
        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dialog.setResizable(true);
        Optional<ButtonType> optional = dialog.showAndWait();
        if(optional.isPresent() && optional.get().equals(ButtonType.OK))
        {
            List<MethodLine> lines = content.TranslateIntoMethodFormat();
            _Method.GetMethodBody().clear();
            _Method.GetMethodBody().addAll(lines);
        }
    }
    
    private final java.util.Map<CheckableComboxAndTextField, InputParameter> _InputParameterProjection = new HashMap<>();
    private BorderPane GetInputParameterBox(List<InputParameter> inputParameters)
    {
        Label name = new Label(StaticStrings.InputParameters);
        Button addButton = new Button("Add");
        Button delButton = new Button("Del");
        
        HBox top = new HBox(name, addButton, delButton);
        VBox list = new VBox();
        list.setPrefSize(200, 100);
        ScrollPane scrollPane = new ScrollPane(list);
        BorderPane borderPane = new BorderPane(scrollPane);
        borderPane.setTop(top);
        addButton.setOnMouseClicked(event -> this.OnAddButtonAtInputBox(list));
        delButton.setOnMouseClicked(event -> this.OnDelButtonAtInputBox(list));
        for(InputParameter ip : inputParameters)
        {
            CheckableComboxAndTextField cct = this.GetCheckableComboxAndTextField(ip.GetType(), ip.GetName());
            cct.SetValueChangeListener(this.GetComboBoxChangeListener(cct.GetComboBox()));
            _InputParameterProjection.put(cct, ip);
            list.getChildren().add(cct);
        }
        return borderPane;
    }
    
    private void OnAddButtonAtInputBox(VBox list)
    {
        InputParameter inputParameter = ModelFactory.GetInputParameter_Empty();
        CheckableComboxAndTextField item = this.GetCheckableComboxAndTextField(inputParameter.GetType(), inputParameter.GetName());
        list.getChildren().add(item);
        _Method.GetInputParameters().add(inputParameter);
        _InputParameterProjection.put(item, inputParameter);
    }
    
    private void OnDelButtonAtInputBox(VBox list)
    {
        List<Node> delList = new ArrayList<>();
        for(Node node : list.getChildren())
        {
            IIsChecked item = (IIsChecked) node;
            if(item.IsChecked())
            {
                InputParameter inputParameter = _InputParameterProjection.remove(item);
                _Method.GetInputParameters().remove(inputParameter);
                delList.add(node);
            }
        }
        list.getChildren().removeAll(delList);
    }
    
    
    private CheckableComboxAndTextField GetCheckableComboxAndTextField(StringProperty type, StringProperty name)
    {
        CheckableComboxAndTextField item = new CheckableComboxAndTextField(type, name);
        item.SetValueChangeListener(this.GetComboBoxChangeListener(item.GetComboBox()));
        return item;
    }
    
}
