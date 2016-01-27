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

import java.util.HashMap;
import java.util.Map;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Line;

/**
 *
 * @author Liu
 */
public class ButtonMoveEventHandler implements EventHandler<MouseEvent>
{

    private double _OldScreenX = Double.NaN;
    private double _OldScreenY = Double.NaN;

    private Map<Button, Map<Button, Line[]>> _Projection;

    public ButtonMoveEventHandler(Map<Button, Map<Button, Line[]>> proj)
    {
        _Projection = proj;
    }

    @Override
    public void handle(MouseEvent event)
    {
        if (event.getSource() instanceof Button)
        {
            double newScreenX = event.getScreenX();
            double newScreenY = event.getScreenY();
            if (Double.isNaN(_OldScreenX))
            {
                _OldScreenX = newScreenX;
                _OldScreenY = newScreenY;
                return;
            }
            double diffX = newScreenX - _OldScreenX;
            double diffY = newScreenY - _OldScreenY;
            _OldScreenX = newScreenX;
            _OldScreenY = newScreenY;
            Button item = (Button) event.getSource();
            item.setLayoutX(item.getLayoutX() + diffX);
            item.setLayoutY(item.getLayoutY() + diffY);

            Map<Button, Line[]> targeted = this.GetEffectedLinesThatItemIsTarget(item);
            Map<Button, Line[]> source = this.GetEffectedLinesThatItemIsSource(item);

            for (Button btn : targeted.keySet())
            {
                ArrowManagement.RedrawArrow(targeted.get(btn), item, btn);
            }

            for (Button btn : source.keySet())
            {
                ArrowManagement.RedrawArrow(source.get(btn), btn, item);
            }
        }
    }

    private Map<Button, Line[]> GetEffectedLinesThatItemIsTarget(Button item)
    {
        Map<Button, Line[]> lines = new HashMap<>();
        for (Button key1 : _Projection.keySet())
        {
            if (key1 == item)
            {
                continue;
            }

            Map<Button, Line[]> value = _Projection.get(key1);
            for (Button key2 : value.keySet())
            {
                if (key2 == item)
                {
                    lines.put(key1, value.get(key2));
                }
            }
        }
        return lines;
    }

    private Map<Button, Line[]> GetEffectedLinesThatItemIsSource(Button item)
    {
        return _Projection.get(item);
    }

}
