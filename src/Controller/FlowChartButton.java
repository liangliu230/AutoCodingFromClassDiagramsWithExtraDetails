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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.transform.Rotate;

/**
 *
 * @author Liu
 */
    abstract class FlowChartButton extends Button
    {
        protected final String _ButtonType;
        protected final String _ButtonLabel;        
        protected  String _Function = "";
        private final Pane _Pane;
        private final Map<FlowChartButton, Map<FlowChartButton, Line[]>> _Arrows;
        public FlowChartButton(String buttonType, String buttonLabel, Pane pane, Map<FlowChartButton, Map<FlowChartButton, Line[]>> lines)
        {
            _ButtonType = buttonType;
            _ButtonLabel = buttonLabel;
            _Pane = pane;
            _Arrows = lines;
        }
        protected FlowChartButton GetButtonWithLabel(String target)
        {
            System.out.println(target);
            for(Node node : _Pane.getChildren())
            {
                if(node instanceof FlowChartButton)
                {
                    FlowChartButton button = (FlowChartButton) node;
                    String label = button.GetButtoneLabel();
                    System.out.println(label + " " + label.equals(target));
                    if(label.equals(target))
                        return button;
                }
            }
            return null;
        }
        public String GetButtonType(){return _ButtonType;}
        public String GetButtoneLabel(){return _ButtonLabel;}      
        public abstract void SetTextContent();
        protected void AddLines(FlowChartButton from, FlowChartButton to)
        {
        Map<FlowChartButton, Line[]> drawedLines = _Arrows.get(from);
        
        
            if (drawedLines.containsKey(to))
            {
                return;
            }

            double startX = to.getLayoutX(),
                    startY = to.getLayoutY(),
                    endX = from.getLayoutX(),
                    endY = from.getLayoutY();
            Line line = new Line(startX, startY, endX, endY);

            double length = Math.sqrt((startX - endX) * (startX - endX) + (startY - endY) * (startY - endY));
            length /= 30;
            Line left = new Line(startX, startY, (endX - startX) / length + startX, (endY - startY) / length + startY);
            Line right = new Line(startX, startY, (endX - startX) / length + startX, (endY - startY) / length + startY);
            Rotate leftRotate = new Rotate(10, startX, startY);
            Rotate rightRotate = new Rotate(10, startX, startY);
            left.getTransforms().add(leftRotate);
            right.getTransforms().add(rightRotate);

            Line[] lines = new Line[]
            {
                line, left, right
            };
            drawedLines.put(to, lines);
            _Pane.getChildren().addAll(line, left, right);
        
    }
        protected List<String> GetLabels()
        {
            List<Node> nodeList = _Pane.getChildren();
            List<String> labelList = new ArrayList<>();
            for(Node node : nodeList)
            {
                if(node instanceof FlowChartButton)
                {
                FlowChartButton button = (FlowChartButton) node;
                labelList.add(button.GetButtoneLabel());
                }
            }
            return labelList;
        }
        protected Dialog GetMouseClickedDialog(VBox content)
        {
            Dialog dialog = new Dialog();
            dialog.getDialogPane().setContent(content);
            dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
            return dialog;
        }
        
    }
