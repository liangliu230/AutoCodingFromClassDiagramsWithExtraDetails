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
import Model.Method;
import Model.ModelFactory;
import Model.StaticStrings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 *
 * @author Liu
 */
public class ClassDialogContent extends DialogContentBase
{

    private final Map<IIsChecked, StringProperty> _ImpProjection = new HashMap<>();
    private final Map<IIsChecked, Field> _FieldProjection = new HashMap<>();
    private final Map<IIsChecked, Method> _MethodProjection = new HashMap<IIsChecked, Method>();

    public ClassDialogContent(ACClass currentClass, IHintGenerator hintGenerator)
    {
        super(hintGenerator, currentClass);

        Label clsName = new Label();
        Tooltip clsNameTooltip = new Tooltip("input class name");
        clsName.setTooltip(clsNameTooltip);
        clsName.textProperty().bindBidirectional(currentClass.GetName());

        ComboBox<String> visBox = this.GetVisibility(_Cls.GetVisibility());
        
        CheckBox isAbstract = this.GetCheckBox(_Cls.isAbstract(), StaticStrings.Abstract);
        CheckBox isFinal = this.GetCheckBox(_Cls.isFinal(), StaticStrings.Final);

        RadioButton typeCls = new RadioButton(StaticStrings.Class);
        RadioButton typeInterface = new RadioButton(StaticStrings.Interface);
        RadioButton typeEnum = new RadioButton(StaticStrings.Enum);
        typeInterface.selectedProperty().bindBidirectional(currentClass.IsInterface());
        typeEnum.selectedProperty().bindBidirectional(currentClass.IsEnum());
        ToggleGroup clsTypes = new ToggleGroup();
        typeCls.setToggleGroup(clsTypes);
        typeEnum.setToggleGroup(clsTypes);
        typeInterface.setToggleGroup(clsTypes);
        HBox modifiers = new HBox(visBox, isAbstract, isFinal, typeCls, typeEnum, typeInterface);
        StringProperty extd = ModelFactory.GetStringProperty_Empty();
        if (_Cls.GetExtends().size() > 0)
        {
            extd = _Cls.GetExtends().get(0);
        } else
        {
            _Cls.GetExtends().add(extd);
        }
        CheckableComboBox extendsBox = this.GetCheckableComboBox(extd);
        extendsBox.DisableCheckBox();

        BorderPane annotationPane = this.GetListWithAddDelBox(StaticStrings.Annotations);
        BorderPane impPane = this.GetListWithAddDelBox(StaticStrings.Implements);
        BorderPane fldPane = this.GetListWithAddDelBox(StaticStrings.Fields);
        BorderPane mthdPane = this.GetListWithAddDelBox(StaticStrings.Methods);

        VBox vbox = new VBox(clsName, modifiers, extendsBox, impPane, annotationPane, fldPane, mthdPane);
        this.setContent(vbox);
        this.setPrefSize(600, 600);
    }

    private BorderPane GetListWithAddDelBox(String name)
    {
        Label label = new Label(name);
        Button addBtn = new Button("Add");
        Button delBtn = new Button("Del");
        HBox hbox = new HBox(label, addBtn, delBtn);
        VBox list = new VBox();
        list.setPrefSize(200, 100);

        switch (name)
        {
            default:
                for (StringProperty imp : _Cls.GetImplements())
                {
                    CheckableComboBox impBox = this.GetCheckableComboBox(imp);
                    list.getChildren().add(impBox);
                    _ImpProjection.put(impBox, imp);
                }
                break;
            case StaticStrings.Annotations:
                for (StringProperty ann : _Cls.GetAnnotations())
                {
                    CheckableComboBox annbox = this.GetCheckableComboBox(ann);
                    list.getChildren().add(annbox);
                    _AnnotationsProjection.put(annbox, ann);
                }
                break;
            case StaticStrings.Fields:
                for (Field fld : _Cls.GetFields())
                {
                    CheckableButton fldButton = this.GetCheckableButton(fld.GetExpressions(), event -> this.OnFieldButtonClicked(event));
                    list.getChildren().add(fldButton);
                    _FieldProjection.put(fldButton, fld);
                }
                break;
            case StaticStrings.Methods:
                for (Method mthd : _Cls.GetMethods())
                {
                    CheckableButton mthdButton = this.GetCheckableButton(mthd.GetExpressions(), event -> this.OnMethodButtonClicked(event));
                    list.getChildren().add(mthdButton);
                    _MethodProjection.put(mthdButton, mthd);
                }
                break;
        }

        ScrollPane scrollPane = new ScrollPane(list);
        addBtn.setOnMouseClicked(event -> this.OnAddButton(list, name));
        delBtn.setOnMouseClicked(event -> this.OnDelButton(list));
        BorderPane borderPane = new BorderPane(scrollPane);
        borderPane.setTop(hbox);
        return borderPane;
    }

    private void OnAddButton(VBox list, String name)
    {
        switch (name)
        {
            case StaticStrings.Annotations:
                StringProperty ann = ModelFactory.GetStringProperty_Empty();
                CheckableComboBox annbox = this.GetCheckableComboBox(ann);
                list.getChildren().add(annbox);
                _AnnotationsProjection.put(annbox, ann);
                _Cls.GetAnnotations().add(ann);
                break;
            case StaticStrings.Methods:
                Method mthd = ModelFactory.GetMethod_Empty();
                CheckableButton mthdButton = this.GetCheckableButton(mthd.GetExpressions(), event -> this.OnMethodButtonClicked(event));
                list.getChildren().add(mthdButton);
                _MethodProjection.put(mthdButton, mthd);
                _Cls.GetMethods().add(mthd);
                break;
            case StaticStrings.Fields:
                Field fld = ModelFactory.GetField_Empty();
                CheckableButton fldButton = this.GetCheckableButton(fld.GetExpressions(), event -> this.OnFieldButtonClicked(event));
                list.getChildren().add(fldButton);
                _FieldProjection.put(fldButton, fld);
                _Cls.GetFields().add(fld);
                break;
            default:
                StringProperty imp = ModelFactory.GetStringProperty_Empty();
                CheckableComboBox impbox = this.GetCheckableComboBox(imp);
                list.getChildren().add(impbox);
                _ImpProjection.put(impbox, imp);
                _Cls.GetImplements().add(imp);
                break;
        }
    }

   
    private void OnDelButton(VBox list)
    {
        List<IIsChecked> delList = new ArrayList<>();
        for (Node node : list.getChildren())
        {
            IIsChecked item = (IIsChecked) node;
            if (item.IsChecked())
            {
                delList.add(item);

                Field fld = _FieldProjection.remove(item);
                StringProperty imp = _ImpProjection.remove(item);
                Method mthd = _MethodProjection.remove(item);
                StringProperty ann = _AnnotationsProjection.remove(item);
                if (ann != null)
                {
                    _Cls.GetAnnotations().remove(ann);
                }
                if (fld != null)
                {
                    _Cls.GetFields().remove(fld);
                }
                if (imp != null)
                {
                    _Cls.GetImplements().remove(imp);
                }
                if (mthd != null)
                {
                    _Cls.GetMethods().remove(mthd);
                }
            }
        }
        list.getChildren().removeAll(delList);
    }

  
    private void OnFieldButtonClicked(MouseEvent event)
    {
        Dialog dialog = new Dialog();
        CheckableButton btn = this.GetParentFromButton(event);
        Field field = ModelFactory.GetField_Empty(); 
        if(_FieldProjection.containsKey(btn))
            field = _FieldProjection.get(btn);
        FieldDialogContent content = new FieldDialogContent(field, _Generator, _Cls);
        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK,ButtonType.CANCEL);
        Optional<ButtonType> optional = dialog.showAndWait();
        if(optional.isPresent() && optional.get().equals(ButtonType.OK))
        {
            if(_Cls.GetFields().contains(field) == false)
            _Cls.GetFields().add(field);     
        }
    }

    private CheckableButton GetParentFromButton(MouseEvent event)
    {
        Button btn = (Button) event.getSource();
        CheckableButton cb = (CheckableButton) btn.getParent();
        return cb;
    }
    
    private void OnMethodButtonClicked(MouseEvent event)
    {
        Dialog dialog = new Dialog();
        CheckableButton btn = this.GetParentFromButton(event);
        Method method = ModelFactory.GetMethod_Empty();
        if(_MethodProjection.containsKey(btn))
            method = _MethodProjection.get(btn);
        MethodDialogContent content = new MethodDialogContent(method, _Generator, _Cls);
        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        Optional<ButtonType> optional = dialog.showAndWait();
        if(optional.isPresent() && optional.get().equals(ButtonType.OK))
        {
            if(_Cls.GetMethods().contains(method) == false)
                _Cls.GetMethods().add(method);
        }
    }

}
