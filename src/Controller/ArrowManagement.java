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

import javafx.scene.control.Button;
import javafx.scene.shape.Line;
import javafx.scene.transform.Rotate;

/**
 *
 * @author Liu
 */
public class ArrowManagement
{

    private static final double _ArrowSwingLength = 10;
    private static final double _ArrowAngle = 10;

    public static final Line[] GetArrow(Button from, Button to, boolean isDashed)
    {
        double startX = to.getLayoutX();
        double startY = to.getLayoutY();
        double endX = from.getLayoutX();
        double endY = from.getLayoutY();
        Line line = new Line(startX, startY, endX, endY);
        if (isDashed)
        {
            line.getStrokeDashArray().addAll(10.0, 10.0);
        }
        double length = GetLength(startX, startY, endX, endY);
        Line left = new Line(startX, startY, (endX - startX) / length + startX, (endY - startY) / length + startY);
        Line right = new Line(startX, startY, (endX - startX) / length + startX, (endY - startY) / length + startY);
        Rotate leftRotate = new Rotate(10, startX, startY);
        Rotate rightRotate = new Rotate(10, startX, startY);
        left.getTransforms().add(leftRotate);
        right.getTransforms().add(rightRotate);

        Line[] arrow = new Line[]
        {
            line, left, right
        };

        return arrow;
    }

    public static final void RedrawArrow(Line[] arrow, Button from, Button to)
    {
        Line main = arrow[0];
            main.setStartX(from.getLayoutX());
            main.setStartY(from.getLayoutY());
            main.setEndX(to.getLayoutX());
            main.setEndY(to.getLayoutY());
            Line left = arrow[1];
            Line right = arrow[2];
            double startX = main.getStartX(),
                    startY = main.getStartY(),
                    endX = main.getEndX(),
                    endY = main.getEndY();
            double length = GetLength(startX, startY, endX, endY);
            left.setStartX(startX);
            left.setStartY(startY);
            left.setEndX((endX - startX) / length + startX);
            left.setEndY((endY - startY) / length + startY);
            right.setStartX(startX);
            right.setStartY(startY);
            right.setEndX((endX - startX) / length + startX);
            right.setEndY((endY - startY) / length + startY);
            Rotate leftR = new Rotate(-1 * _ArrowAngle, left.getStartX(), left.getStartY());
            left.getTransforms().clear();
            left.getTransforms().add(leftR);
            Rotate rightR = new Rotate(_ArrowAngle, right.getStartX(), right.getStartY());
            right.getTransforms().clear();
            right.getTransforms().add(rightR);
    }

    private static final double GetLength(double startX, double startY, double endX, double endY)
    {
        return Math.sqrt((startX - endX) * (startX - endX) + (startY - endY) * (startY - endY)) / _ArrowSwingLength;
    }

}
