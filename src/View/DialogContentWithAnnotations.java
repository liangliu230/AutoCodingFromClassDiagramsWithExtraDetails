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
import Model.IGetAnnotations;
import Model.ModelFactory;
import Model.StaticStrings;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 *
 * @author Liu
 */
public class DialogContentWithAnnotations extends DialogContentBase
{
    protected final IGetAnnotations _IGetAnnotations;

    public DialogContentWithAnnotations(IHintGenerator generator, ACClass cls, IGetAnnotations getAnnotations)
    {
        super(generator, cls);
        _IGetAnnotations = getAnnotations;
    }
    
        protected BorderPane GetAnnotationBox(List<StringProperty> annotations)            
    {
        Label label = new Label(StaticStrings.Annotations);
        Button addButton = new Button("Add");
        Button delButton = new Button("Del");
        HBox hBox = new HBox(label, addButton, delButton);
        VBox vbox = new VBox();
        vbox.setPrefSize(200, 100);
        ScrollPane scrollPane = new ScrollPane(vbox);
        addButton.setOnMouseClicked(event -> this.OnAddButton(vbox));
        delButton.setOnMouseClicked(event -> this.OnDelButton(vbox));
        BorderPane borderPane = new BorderPane(scrollPane);
        borderPane.setTop(hBox);
        
        for(StringProperty annotation : annotations)
        {
            CheckableComboBox ccb = this.GetCheckableComboBox(annotation);
            ccb.SetValueChangeListener(this.GetComboBoxChangeListener(ccb.GetComboBox()));
            _AnnotationsProjection.put(ccb, annotation);
            vbox.getChildren().add(ccb);
        }
        
        return borderPane;
    }
    
    protected void OnAddButton(VBox list)
    {
        StringProperty annotation = ModelFactory.GetStringProperty_Empty();
        CheckableComboBox box = this.GetCheckableComboBox(annotation);
        list.getChildren().add(box);
        _IGetAnnotations.GetAnnotations().add(annotation);
        _AnnotationsProjection.put(box, annotation);
    }
    
    protected void OnDelButton(VBox list)
    {
        java.util.List<javafx.scene.Node> delList = new ArrayList<>();
        for(Node node : list.getChildren())
        {
            IIsChecked item = (IIsChecked) node;
            if(item.IsChecked())
            {
                delList.add(node);
                StringProperty sp = _AnnotationsProjection.remove(item);
                _IGetAnnotations.GetAnnotations().remove(sp);
            }
        }
        list.getChildren().removeAll(delList);        
    }
    
}
