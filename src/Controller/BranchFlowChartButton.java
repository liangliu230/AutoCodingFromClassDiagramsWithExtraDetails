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
import java.util.Map;
import java.util.Optional;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;

/**
 *
 * @author Liu
 */
    class BranchFlowChartButton extends FlowChartButton
    {
        private String _TrueBranchLabel="", _FalseBranchLabel="";
        
        public String GetTrueBranchLabel(){return _TrueBranchLabel;}
        public String GetFalseBranchLabel(){return _FalseBranchLabel;}
        public BranchFlowChartButton(String buttonType, String buttonLabel, Pane pane,Map<FlowChartButton, Map<FlowChartButton, Line[]>> lines,String currentFunction, String currentTrueLabel, String currentFalseLabel)
        {
            super(buttonType, buttonLabel, pane,lines);
            
            this.setOnMouseClicked(ev->{
                List<String> list = this.GetLabels();
                WithFunctionDiscriptionAndTwoLinkToLabel box = new WithFunctionDiscriptionAndTwoLinkToLabel(list,currentFunction,currentTrueLabel,currentFalseLabel);
                Dialog dialog = this.GetMouseClickedDialog(box);
                Optional<ButtonType> optional = dialog.showAndWait();
                if(optional.isPresent())
                    {
                        _Function = box.GetFunctionText();
                        _TrueBranchLabel = box.GetTrueBranchLink();
                        _FalseBranchLabel = box.GetFalseBranchLink();
                        
                    FlowChartButton toTrue = super.GetButtonWithLabel(_TrueBranchLabel);
                    super.AddLines(this, toTrue);
                    
                    FlowChartButton toFalse = super.GetButtonWithLabel(_FalseBranchLabel);
                    super.AddLines(this, toFalse);
                    }
                this.SetTextContent();
            });
        }        

        @Override
        public void SetTextContent()
        {
            String str = super._ButtonType + " | " + _Function + "\n"
                    + "Label: " + super._ButtonLabel + " | " + "True branch: " + _TrueBranchLabel + " False branch: " + _FalseBranchLabel; 
            this.setText(str);
        }        
        
    } 
