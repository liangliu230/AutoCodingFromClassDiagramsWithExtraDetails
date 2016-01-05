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
package Controller;

import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

/**
 *
 * @author Liu
 */
public class WithFunctionDiscriptionAndTwoLinkToLabel extends WithFunctionDiscriptionAndLinkToLabel
{
    private final ComboBox<String> _TrueBranchLinkToLabel;
    private final ComboBox<String> _FalseBranchLinkToLabel;
    public WithFunctionDiscriptionAndTwoLinkToLabel(List<String> list, String currentFunction, String currentTrueLabel, String currentFalseLabel)
    {
        super(list, "", currentFunction);
        this.getChildren().clear();
        super._FunctionDiscription = new TextField(currentFunction);
        Label usedFunction = new Label("Used Function: ");
        HBox function = new HBox(usedFunction, _FunctionDiscription);
        ObservableList<String> ol = FXCollections.observableArrayList(list);
        _TrueBranchLinkToLabel = new ComboBox<>(ol);
        _TrueBranchLinkToLabel.setValue(currentTrueLabel);
        _FalseBranchLinkToLabel = new ComboBox<>(ol);
        _FalseBranchLinkToLabel.setValue(currentFalseLabel);
        Label trueLabel = new Label("Label for True Branch: ");
        Label falseLabel = new Label("Label for False Branch: ");
        HBox tBox = new HBox(trueLabel, _TrueBranchLinkToLabel);
        HBox fBox = new HBox(falseLabel, _FalseBranchLinkToLabel);
        this.getChildren().addAll(function, tBox, fBox);
    }

   public String GetTrueBranchLink(){return _TrueBranchLinkToLabel.getValue();}
   public String GetFalseBranchLink(){return _FalseBranchLinkToLabel.getValue();}
    
}
