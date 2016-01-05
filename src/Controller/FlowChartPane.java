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

import FileGeneration.Java.FunctionStaticStrings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.transform.Rotate;

/**
 *
 * @author Liu
 */
public class FlowChartPane extends Pane
{
    private final Map<FlowChartButton, Map<FlowChartButton, Line[]>> _Arrows = new HashMap<FlowChartButton, Map<FlowChartButton, Line[]>>();
    
    public FlowChartPane()
    {
        this.setPrefSize(500, 500);
        Label label = new Label("Component Type");
        Button sequence = new Button("Sequence");
        sequence.setOnMouseClicked(ev->{
            String labelsq = this.GetLabel();
            FlowChartButton button = new SequencialFlowChartButton("SQNC", labelsq, this,_Arrows,"","");
            button.setLayoutX(130);
            button.setLayoutX(140);
            _Arrows.put(button, new HashMap<FlowChartButton, Line[]>());
            button.setOnMouseDragged(new OnMouseDragEvent());
            this.getChildren().add(button);
            button.SetTextContent();
        });
        Button branch = new Button("Branch");
        branch.setOnMouseClicked(ev->{
            String labelbh = this.GetLabel();
            FlowChartButton button = new BranchFlowChartButton("BRNCH", labelbh, this,_Arrows,"","","");
            button.setLayoutX(140);button.setLayoutX(150);
            _Arrows.put(button, new HashMap<FlowChartButton, Line[]>());
            button.setOnMouseDragged(new OnMouseDragEvent());
            this.getChildren().add(button);
            button.SetTextContent();
        });
        Button loop = new Button("Loop");
        loop.setOnMouseClicked(ev->{
            String lpst = this.GetLabel();
            String lped = this.GetLabel();
            FlowChartButton loopStart = new SequencialFlowChartButton("LPST", lpst, this,_Arrows,"","");
            FlowChartButton loopEnd = new SequencialFlowChartButton("LPED", lped, this,_Arrows,"","");
            loopStart.setLayoutX(140);loopStart.setLayoutY(150);
            loopEnd.setLayoutX(180);
            loopEnd.setLayoutY(180);
            loopStart.setOnMouseDragged(new OnMouseDragEvent());
            loopEnd.setOnMouseDragged(new OnMouseDragEvent());
            _Arrows.put(loopStart, new HashMap<FlowChartButton, Line[]>());
            _Arrows.put(loopEnd, new HashMap<FlowChartButton, Line[]>());
            this.getChildren().add(loopStart);
            this.getChildren().add(loopEnd);
            loopStart.SetTextContent();
            loopEnd.SetTextContent();
        });
        
        VBox box = new VBox(label,sequence,branch,loop);
        
        FlowChartButton start = new StartButton("START", this.GetLabel(), this, _Arrows);
        start.setOnMouseDragged(new OnMouseDragEvent());
        start.setLayoutX(120);start.setLayoutY(120);
        _Arrows.put(start, new HashMap<>());
        FlowChartButton end = new EndButton("END", this.GetLabel(), this, _Arrows);
        end.setOnMouseDragged(new OnMouseDragEvent());
        _Arrows.put(end, new HashMap<FlowChartButton, Line[]>());
        end.setLayoutX(200);end.setLayoutY(200);
        this.getChildren().add(box);
        this.getChildren().addAll(start,end);
    }
    
    private int _Count = 0;
    
    private String GetLabel()
    {
        return "O"+_Count++;
    }

    class OnMouseDragEvent implements EventHandler<MouseEvent>
    {
        private double _OldScreenX = Double.NaN;
        private double _OldScreenY = Double.NaN;

        private Map<FlowChartButton, Line[]> GetLinesPointAtControl(FlowChartButton ctrl)
        {
            Map<FlowChartButton, Line[]> list = new HashMap<>();
            for (FlowChartButton key : _Arrows.keySet())
            {
                Map<FlowChartButton, Line[]> item = _Arrows.get(key);
                for (FlowChartButton clsCtrl : item.keySet())
                {
                    if (clsCtrl == ctrl)
                    {
                        list.put(key, item.get(clsCtrl));
                        break;
                    }
                }
            }
            return list;
        }

        private void ResetLines(Line[] lines, FlowChartButton from, FlowChartButton to)
        {
            Line main = lines[0];
            main.setStartX(from.getLayoutX());
            main.setStartY(from.getLayoutY());
            main.setEndX(to.getLayoutX());
            main.setEndY(to.getLayoutY());
            Line left = lines[1];
            Line right = lines[2];
            double startX = main.getStartX(),
                    startY = main.getStartY(),
                    endX = main.getEndX(),
                    endY = main.getEndY();
            double length = Math.sqrt((startX - endX) * (startX - endX) + (startY - endY) * (startY - endY));

            length /= 30;
            left.setStartX(startX);
            left.setStartY(startY);
            left.setEndX((endX - startX) / length + startX);
            left.setEndY((endY - startY) / length + startY);
            right.setStartX(startX);
            right.setStartY(startY);
            right.setEndX((endX - startX) / length + startX);
            right.setEndY((endY - startY) / length + startY);
            Rotate leftR = new Rotate(-1 * 30, left.getStartX(), left.getStartY());
            left.getTransforms().clear();
            left.getTransforms().add(leftR);
            Rotate rightR = new Rotate(30, right.getStartX(), right.getStartY());
            right.getTransforms().clear();
            right.getTransforms().add(rightR);
        }

        @Override
        public void handle(MouseEvent event)
        {
            if (event.getSource() instanceof FlowChartButton)
            {
                if (Double.isNaN(_OldScreenX) || Double.isNaN(_OldScreenY))
                {
                    _OldScreenX = event.getScreenX();
                    _OldScreenY = event.getScreenY();
                    return;
                }

                double diffX = event.getScreenX() - _OldScreenX;
                double diffY = event.getScreenY() - _OldScreenY;
                _OldScreenX = event.getScreenX();
                _OldScreenY = event.getScreenY();
                FlowChartButton control = (FlowChartButton) event.getSource();
                control.setLayoutX(control.getLayoutX() + diffX);
                control.setLayoutY(control.getLayoutY() + diffY);

                Map<FlowChartButton, Line[]> lines = this.GetLinesPointAtControl(control);

                if (lines.isEmpty() == false)
                {
                    for (Map.Entry<FlowChartButton, Line[]> entrySet : lines.entrySet())
                    {
                        FlowChartButton key = entrySet.getKey();
                        Line[] value = entrySet.getValue();

                        this.ResetLines(value, control, key);
                    }
                }

                lines = _Arrows.get(control);
                if (lines.isEmpty() == false)
                {
                    for (Map.Entry<FlowChartButton, Line[]> entrySet : lines.entrySet())
                    {
                        FlowChartButton key = entrySet.getKey();
                        Line[] value = entrySet.getValue();
                        this.ResetLines(value, key, control);
                    }

                }

            }

        }
    }

}
