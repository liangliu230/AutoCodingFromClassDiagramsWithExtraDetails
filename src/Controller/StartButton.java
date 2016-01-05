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
public class StartButton extends FlowChartButton
{

    public StartButton(String buttonType, String buttonLabel, Pane pane, Map<FlowChartButton, Map<FlowChartButton, Line[]>> lines)
    {
        super(buttonType, buttonLabel, pane, lines);
        this.setOnMouseClicked(ev->{
            OnlyLinkToLabel box = new OnlyLinkToLabel(this.GetLabels(),_LinkTo);
            Dialog dialog = new Dialog();
            dialog.getDialogPane().setContent(box);
            dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
            Optional<ButtonType> optional = dialog.showAndWait();
            if(optional.isPresent())
            {
                _LinkTo = box.GetLinkToLabel();
                FlowChartButton button = super.GetButtonWithLabel(_LinkTo);
                super.AddLines(this, button);
                this.SetTextContent();
            }
        });
        this.SetTextContent();
    }
    
    private String _LinkTo;

    @Override
    public void SetTextContent()
    {
        String str = "START| input parameters: \n" + "Label: " + _ButtonLabel + "| LinkTo: " + _LinkTo;
        this.setText(str);
    }
    
}
