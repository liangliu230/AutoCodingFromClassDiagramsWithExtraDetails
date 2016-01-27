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

import Controller.ArrowManagement;
import Controller.ButtonMoveEventHandler;
import Controller.SortNodeInFlowChartGraph;
import Hint.IHintGenerator;
import Model.ACClass;
import Model.Method;
import Model.MethodLine;
import Model.MethodLineFunctions;
import Model.ModelFactory;
import Model.StaticStrings;
import Utility.GetArrayList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javafx.beans.binding.Bindings;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Dialog;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;

/**
 *
 * @author Liu
 */
public class FlowChartDialogContent extends ScrollPane
{

    private final Method _Method;
    private final Pane _Pane;
    private final ContextMenu _ContextMenu;
    private final Map<Button, MethodLine> _MethodLineProjection = new HashMap<>();
    private final Map<Button, Map<Button, Line[]>> _Arrows = new HashMap<>();
    private final List<MethodLine> _MethodLinesInDialog = new ArrayList<>();
    private final IHintGenerator _HintGenerator;
    private final ACClass _Class;
    private double _MouseLocationX;
    private double _MouseLocationY;

    public FlowChartDialogContent(IHintGenerator generator, ACClass cls, Method method)
    {
        super();
        _Method = method;
        _HintGenerator = generator;
        _Class = cls;
        _Pane = new Pane();
        _Pane.setPrefSize(600, 600);

        MenuItem sequence = new MenuItem("Add Sequence Node");
        sequence.setOnAction(event -> this.OnAddSequenceNode());
        MenuItem branch = new MenuItem("Add Branch Node");
        branch.setOnAction(event -> this.OnAddBranchNode());
        MenuItem loop = new MenuItem("Add Loop Nodes");
        loop.setOnAction(event -> this.OnAddLoopNode());
        _ContextMenu = new ContextMenu(sequence, branch, loop);
        this.setContent(_Pane);
        
        this.setOnMousePressed(event -> this.ShowContextMenu(event));
        if (_Method.GetMethodBody().size() > 2) // start + end
        {
            this.TranslateMethodLines(_Method.GetMethodBody());
        } else
        {
            Button start = this.GetStartButton();
            Button end = this.GetEndButton();
            start.setLayoutX(10);
            start.setLayoutY(10);
            end.setLayoutX(30);
            end.setLayoutY(30);
        }
        
        for(Button btn : _MethodLineProjection.keySet())
        {
            MethodLine line = _MethodLineProjection.get(btn);
            this.CheckAndDrawArrow(btn, line);
        }
    }

    private Button GetStartButton()
    {
        MethodLine start = ModelFactory.GetMethodLine_Empty();
        start.GetFunctionType().setValue(FlowChartFunctions.START);
        start.GetSelfLabel().setValue(this.GetNodeLabel());

        Button btn = this.GetButtonBindWithMethodLine(start, FlowChartFunctions.START);
        this.AddButtonToGraph(btn, start);
        return btn;
    }

    private Button GetEndButton()
    {
        MethodLine end = ModelFactory.GetMethodLine_Empty();
        end.GetFunctionType().setValue(FlowChartFunctions.END);
        end.GetSelfLabel().setValue(this.GetNodeLabel());
        
        Button btn = this.GetButtonBindWithMethodLine(end, FlowChartFunctions.END);
        this.AddButtonToGraph(btn, end);
        return btn;
    }

    private void OnButtonClicked(MouseEvent event, String type)
    {
        if(event.isStillSincePress() == false)
            return;
        Button srcBtn = (Button) event.getSource();
        MethodLine line = _MethodLineProjection.get(srcBtn);

        FlowChartNodeDialogContent content
                = new FlowChartNodeDialogContent(
                        type,
                        line,
                        _HintGenerator,
                        _Class,
                        _MethodLinesInDialog,
                        this.GetSelectableFunctions(type));

        Dialog dialog = new Dialog();
        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dialog.setResizable(true);
        Optional<ButtonType> optional = dialog.showAndWait();
        if (optional.isPresent() && optional.get().equals(ButtonType.OK))
        {
            this.CheckAndDrawArrow(srcBtn, line);
        }
    }

    private void CheckAndDrawArrow(Button src, MethodLine line)
    {
        for (StringProperty label : line.GetLinkToLabels())
        {
            Button btn = this.GetButtonWithLabelName(label.getValue());
            if (btn != null)             
                this.DrawArrow(src, btn, false);
        }
    }

    private Button GetButtonWithLabelName(String labelName)
    {
        for (Button btn : _MethodLineProjection.keySet())
        {
            MethodLine line = _MethodLineProjection.get(btn);
            if (line.GetSelfLabel().getValue().equals(labelName))
            {
                return btn;
            }
        }
        return null;
    }

    private List<String> GetSelectableFunctions(String nodeType)
    {
        if (nodeType.equals(FlowChartNodeType.Sequence))
        {
            return this.GetSequenceFunctionTypes();
        } else if (nodeType.equals(FlowChartNodeType.Branch))
        {
            return this.GetBranchFunctionTypes();
        } else
        {
            if(nodeType.equals(FlowChartNodeType.LOOP))
                return this.GetLoopFunctionTypes();
            else
                return new ArrayList<>();
        }
    }
    private int _NodeCount = 0;

    private String GetNodeLabel()
    {
        String label = "O" + _NodeCount;
        _NodeCount++;
        return label;
    }

    private void OnAddLoopNode()
    {
        this.AddMethodLineAndItsCorrespondingButton(FlowChartNodeType.LOOP, null);
        this.AddLoopendNode(null);
    }

    private MethodLine AddLoopendNode(String nodeLabel)
    {
        MethodLine line = ModelFactory.GetMethodLine_Empty();
        if (nodeLabel == null)
        {
            line.GetSelfLabel().setValue(this.GetNodeLabel());
        } else
        {
            line.GetSelfLabel().set(nodeLabel);
        }
        line.GetFunctionType().setValue(FlowChartFunctions.LoopEnd);
        Button btn = this.GetButtonBindWithMethodLine(line, FlowChartNodeType.LOOP);
        AddButtonToGraph(btn, line);
        return line;
    }

    private void AddButtonToGraph(Button btn, MethodLine line)
    {
        _Arrows.put(btn, new HashMap<>());
        _MethodLinesInDialog.add(line);
        _MethodLineProjection.put(btn, line);
        _Pane.getChildren().add(btn);
    }

    private void OnAddBranchNode()
    {
        this.AddMethodLineAndItsCorrespondingButton(FlowChartNodeType.Branch, null);
    }

    private void OnAddSequenceNode()
    {
        this.AddMethodLineAndItsCorrespondingButton(FlowChartNodeType.Sequence, null);
    }

    private Button AddMethodLineAndItsCorrespondingButton(String nodeType, MethodLine line)
    {
        if (line == null)
        {
            line = this.GetMethodLineWithOnlySelfLabel();
        }
        Button btn = this.GetButtonBindWithMethodLine(line, nodeType);
        this.AddButtonToGraph(btn, line);
        return btn;
    }

    private MethodLine GetMethodLineWithOnlySelfLabel()
    {
        MethodLine line = ModelFactory.GetMethodLine_Empty();
        line.GetSelfLabel().setValue(this.GetNodeLabel());
        return line;
    }

    private Button GetButtonBindWithMethodLine(MethodLine line, String nodeType)
    {
        Button btn = new Button(nodeType);
        if(line.GetLayoutX().getValue() == 0)
        btn.setLayoutX(_MouseLocationX);
        else
            btn.setLayoutX(line.GetLayoutX().getValue());
        if(line.GetLayoutY().getValue() == 0)
        btn.setLayoutY(_MouseLocationY);
        else
            btn.setLayoutY(line.GetLayoutY().getValue());
        line.GetLayoutX().bind(btn.layoutXProperty());
        line.GetLayoutY().bind(btn.layoutYProperty());
        btn.setOnMouseDragged(new ButtonMoveEventHandler(_Arrows));
        btn.setOnMouseClicked(ev -> this.OnButtonClicked(ev, nodeType));
        btn.textProperty().bind(Bindings.concat(line.GetExpressions()));
        return btn;
    }

    public List<MethodLine> TranslateIntoMethodFormat()
    {
        SortNodeInFlowChartGraph sort = new SortNodeInFlowChartGraph();
        return sort.SortAndMark(_MethodLinesInDialog);
    }

    private final String GetNodeTypeFromNodeFunction(String functionType)
    {
        switch (functionType)
        {
            default:
                return FlowChartNodeType.Sequence;
            case MethodLineFunctions.If:
            case MethodLineFunctions.Switch:
            case MethodLineFunctions.Try:
            case MethodLineFunctions.TryWithResources:
                return FlowChartNodeType.Branch;
            case MethodLineFunctions.For:
            case MethodLineFunctions.ForEach:
            case MethodLineFunctions.While:
                return FlowChartNodeType.LOOP;

        }
    }

    private final void TranslateMethodLines(List<MethodLine> methodLinesInMethodObject)
    {
        _NodeCount += methodLinesInMethodObject.size();// evade collision
        for (int index = 0; index < methodLinesInMethodObject.size(); index++)
        {
            MethodLine originalLine = methodLinesInMethodObject.get(index);
            MethodLine copiedLine = ModelFactory.GetMethodLineCopy(originalLine);

            if (copiedLine.GetFunctionType().getValue().equals(MethodLineFunctions.Else))
            {
                continue;
            }
            
            String nodeType = this.GetNodeTypeFromNodeFunction(copiedLine.GetFunctionType().getValue());
            this.AddMethodLineAndItsCorrespondingButton(nodeType, copiedLine);
        }
    }

    private void ShowContextMenu(MouseEvent event)
    {
        if (event.isSecondaryButtonDown())
        {
            _MouseLocationX = event.getSceneX();
            _MouseLocationY = event.getSceneY();

            _ContextMenu.show(_Pane, event.getScreenX(), event.getScreenY());
        }
    }

    private ContextMenu GetContextMenuForFlowChartNode()
    {
        MenuItem del = new MenuItem("Delete this node");
        ContextMenu menu = new ContextMenu(del);
        return menu;
    }

    private List<String> GetLoopFunctionTypes()
    {
        return (new GetArrayList<String>()).Get(
                MethodLineFunctions.For,
                MethodLineFunctions.ForEach,
                MethodLineFunctions.While
        );
    }

    private List<String> GetBranchFunctionTypes()
    {
        return (new GetArrayList<String>()).Get(
                MethodLineFunctions.If,
                MethodLineFunctions.Switch,
                MethodLineFunctions.Try
        );
    }

    private List<String> GetSequenceFunctionTypes()
    {
        return (new GetArrayList<String>()).Get(
                MethodLineFunctions.Break,
                MethodLineFunctions.Case,
                MethodLineFunctions.Catch,
                MethodLineFunctions.Continue,
                MethodLineFunctions.Default,
                MethodLineFunctions.Finally,
                MethodLineFunctions.Other,
                MethodLineFunctions.Throw,
                MethodLineFunctions.Return
        );
    }

    private void DrawArrow(Button from, Button to, boolean isDashed)
    {
        
        Line[] arrow = ArrowManagement.GetArrow(from, to, isDashed);
        _Pane.getChildren().addAll(arrow);
        _Arrows.get(from).put(to, arrow);
    }
}
