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
import Model.MethodLineFunctions;
import Model.ModelFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 *
 * @author Liu
 */
public class FlowChartNodeDialogContent extends ScrollPane
{

    private final MethodLine _MethodLine;
    private final List<MethodLine> _ExistingMethodLines;
    private final VBox _Base;
    private final IHintGenerator _HintGenerator;
    private final ACClass _CurrentCls;

    private final java.util.Map<CheckableComboBox, StringProperty> _LinkToLabelProjection = new HashMap<>();

    public FlowChartNodeDialogContent(
            String type, MethodLine methodLine,
            IHintGenerator generator, ACClass currentCls,
            List<MethodLine> methodLines,
            List<String> selectableFunctions)
    {
        super();
        Label typeLabel = new Label(type);
        Label selfLabel = new Label(methodLine.GetSelfLabel().getValue());
        _ExistingMethodLines = methodLines;
        _MethodLine = methodLine;
        HBox firstLine = new HBox(typeLabel, new Separator(Orientation.VERTICAL), selfLabel);
        CheckableComboBox functionType = new CheckableComboBox(_MethodLine.GetFunctionType());
        functionType.GetComboBox().setItems(FXCollections.observableArrayList(selectableFunctions));
        functionType.GetComboBox().valueProperty().addListener(listener -> this.OnFunctionTypeChange(functionType.GetComboBox().getValue()));
        _Base = new VBox();
        VBox box = new VBox(firstLine, new Separator(Orientation.HORIZONTAL), functionType, _Base);
        this.setContent(box);
        _HintGenerator = generator;
        _CurrentCls = currentCls;
    }

    private void OnFunctionTypeChange(String type)
    {
        _Base.getChildren().clear();

        switch (type)
        {
            case FlowChartFunctions.END: // add nothing
                break;
            case MethodLineFunctions.Break:
            case MethodLineFunctions.Continue:
            case MethodLineFunctions.Finally:
            case MethodLineFunctions.Default:
            case FlowChartFunctions.LoopEnd:
            case FlowChartFunctions.START:
                this.AddContentWithOnlyLintTo();
                break;
            case MethodLineFunctions.Case:
            case MethodLineFunctions.Catch:
            case MethodLineFunctions.Return:
            case MethodLineFunctions.For:
            case MethodLineFunctions.ForEach:
            case MethodLineFunctions.While:
                this.AddContentWithFunctionContentAndLinkTo();
                break;
            case MethodLineFunctions.Try:
                this.AddContentForTry();
                break;
            case MethodLineFunctions.If:
                this.AddContentForIf();
                break;
            case MethodLineFunctions.Switch:
                this.AddContentForSwitch();
                break;
            default:
                this.AddContentForNormalLines();
                break;
        }
    }

    private void UnbindAll()
    {
        _MethodLine.GetFunctionContent().unbind();
        _MethodLine.GetFunctionType().unbind();
        _MethodLine.GetOutputType().unbind();
        _MethodLine.GetOutputVariableName().unbind();
        _MethodLine.GetLinkToLabels().clear();
    }

    private void AddContentWithFunctionContentAndLinkTo()
    {
        CheckableComboBox content = this.GetFunctionContent(_MethodLine.GetFunctionContent());
        content.DisableCheckBox();
        if (_MethodLine.GetLinkToLabels().size() == 0)
        {
            _MethodLine.GetLinkToLabels().add(ModelFactory.GetStringProperty_Empty());
        }
        CheckableComboBox linkto = this.GetLinkToComboBox(_MethodLine.GetLinkToLabels().get(0));
        linkto.DisableCheckBox();

        _Base.getChildren().addAll(
                new HBox(new Label("Function Content : "), content),
                new HBox(new Label("Link to: "), linkto)
        );

    }

    private void AddContentWithOnlyLintTo()
    {
        if (_MethodLine.GetLinkToLabels().size() == 0)
        {
            _MethodLine.GetLinkToLabels().add(ModelFactory.GetStringProperty_Empty());
        }
        CheckableComboBox linkTo = this.GetLinkToComboBox(_MethodLine.GetLinkToLabels().get(0));
        linkTo.DisableCheckBox();
        _Base.getChildren().add(new HBox(new Label("Link to: "), linkTo));
    }

    private void AddContentForTry()
    {
        if (_MethodLine.GetLinkToLabels().size() == 0)
        {
            _MethodLine.GetLinkToLabels().add(ModelFactory.GetStringProperty_Empty());
        }
        CheckableComboBox normalCluse = this.GetLinkToComboBox(_MethodLine.GetLinkToLabels().get(0));
        normalCluse.DisableCheckBox();
        
        if (_MethodLine.GetLinkToLabels().size() <= 1)
        {
            _MethodLine.GetLinkToLabels().add(ModelFactory.GetStringProperty_Empty());
        }
        CheckableComboBox catchCluse = this.GetLinkToComboBox(_MethodLine.GetLinkToLabels().get(1));
        catchCluse.DisableCheckBox();
        
        _Base.getChildren().addAll(
                new HBox(new Label("Normal Cluse: "), normalCluse),
                new HBox(new Label("Catch Cluse: "), catchCluse)
        );
    }

    private void AddContentForIf()
    {
        CheckableComboBox booleanFunction = this.GetFunctionContent(_MethodLine.GetFunctionContent());
        booleanFunction.DisableCheckBox();

        if (_MethodLine.GetLinkToLabels().size() == 0)
        {
            _MethodLine.GetLinkToLabels().add(ModelFactory.GetStringProperty_Empty());
        }
        CheckableComboBox trueCluse = this.GetLinkToComboBox(_MethodLine.GetLinkToLabels().get(0));
        trueCluse.DisableCheckBox();
        if (_MethodLine.GetLinkToLabels().size() <= 1)
        {
            _MethodLine.GetLinkToLabels().add(ModelFactory.GetStringProperty_Empty());
        }
        CheckableComboBox falseCluse = this.GetLinkToComboBox(_MethodLine.GetLinkToLabels().get(1));
        falseCluse.DisableCheckBox();

        _Base.getChildren().addAll(
                new HBox(new Label("Boolean Function: "), booleanFunction),
                new HBox(new Label("True Cluse: "), trueCluse),
                new HBox(new Label("False Cluse: "), falseCluse)
        );
    }

    private void AddContentForSwitch()
    {
        CheckableComboBox varName = this.GetFunctionContent(_MethodLine.GetFunctionContent());
        varName.DisableCheckBox();
        if (_MethodLine.GetLinkToLabels().size() == 0)
        {
            _MethodLine.GetLinkToLabels().add(ModelFactory.GetStringProperty_Empty());
        }
        CheckableComboBox defaultCluse = this.GetLinkToComboBox(_MethodLine.GetLinkToLabels().get(0));
        defaultCluse.DisableCheckBox();

        Button addButton = new Button("Add");
        Button delButton = new Button("Del");
        VBox content = new VBox();
        addButton.setOnMouseClicked(event -> this.OnAddLinkTo(content));
        delButton.setOnMouseClicked(event -> this.OnDelLinTo(content));
        BorderPane borderPane = new BorderPane(new ScrollPane(content));
        borderPane.setTop(new HBox(new Label("Other Cases: "), addButton, delButton));
        _Base.getChildren().addAll(
                new HBox(new Label("Var Name: "), varName),
                new HBox(new Label("Default: "), defaultCluse),
                borderPane
        );
    }

    private void OnAddLinkTo(VBox box)
    {
        StringProperty sp = ModelFactory.GetStringProperty_Empty();
        CheckableComboBox linkto = this.GetLinkToComboBox(sp);
        _MethodLine.GetLinkToLabels().add(sp);
        box.getChildren().add(linkto);
    }

    private void OnDelLinTo(VBox box)
    {
        List<Node> delList = new ArrayList<Node>();
        for (Node node : box.getChildren())
        {
            IIsChecked item = (IIsChecked) node;
            if (item.IsChecked())
            {
                StringProperty value = _LinkToLabelProjection.remove(item);
                value.unbind();
                _MethodLine.GetLinkToLabels().remove(value);
                delList.add(node);
            }
        }
        box.getChildren().removeAll(delList);
    }

    private void AddContentForNormalLines()
    {
        CheckableComboBox outputType = this.GetFunctionContent(_MethodLine.GetOutputType());
        outputType.DisableCheckBox();
        TextField outputVarName = new TextField(_MethodLine.GetOutputVariableName().getValue());
        _MethodLine.GetOutputVariableName().bind(outputVarName.textProperty());
        CheckableComboBox functionContent = this.GetFunctionContent(_MethodLine.GetFunctionContent());
        functionContent.DisableCheckBox();
        if (_MethodLine.GetLinkToLabels().size() == 0)
        {
            _MethodLine.GetLinkToLabels().add(ModelFactory.GetStringProperty_Empty());
        }
       
        CheckableComboBox linkToBox = this.GetLinkToComboBox(_MethodLine.GetLinkToLabels().get(0));
        linkToBox.DisableCheckBox();
        _Base.getChildren().addAll(
                new HBox(new Label("Output Type: "), outputType),
                new HBox(new Label("Output Var Name: "), outputVarName),
                new HBox(new Label("Function Used: "), functionContent),
                new HBox(new Label("Link To: "), linkToBox));
    }

    private CheckableComboBox GetFunctionContent(StringProperty stringProperty)
    {
        CheckableComboBox box = new CheckableComboBox(stringProperty);
        box.SetValueChangeListener(new ComboBoxStringChangeListener(box.GetComboBox(), _HintGenerator, _ExistingMethodLines, _CurrentCls));
        return box;
    }

    private ComboBox<String> GetComboBox(StringProperty stringProperty)
    {
        ComboBox<String> box = new ComboBox<>();
        box.setValue(stringProperty.getValue());
        stringProperty.bind(box.valueProperty());
        return box;
    }
    
    private CheckableComboBox GetLinkToComboBox(StringProperty stringProperty)
    {
        CheckableComboBox box = new CheckableComboBox(stringProperty);
        box.GetComboBox().setItems(this.GetExistingLabels());
        return box;
    }

    private ObservableList<String> GetExistingLabels()
    {
        List<String> list = new ArrayList<>();
        for (MethodLine line : _ExistingMethodLines)
        {
            list.add(line.GetSelfLabel().getValue());
        }

        return FXCollections.observableArrayList(list);
    }
}
