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
import Model.MethodLine;
import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import javafx.scene.input.KeyEvent;

/**
 *
 * @author Liu
 */
public class ComboBoxStringChangeListener implements ChangeListener<String>
{

    private final IHintGenerator _HintGenerator;
    private final List<MethodLine> _MethodLines;
    private final ACClass _CurrentCls;
    private final ComboBox<String> _Source;

    public ComboBoxStringChangeListener(ComboBox<String> source, IHintGenerator hintGenerator, List<MethodLine> methodLines, ACClass cls)
    {
        _HintGenerator = hintGenerator;
        _MethodLines = methodLines;
        _CurrentCls = cls;
        _Source = source;
    }

    @Override
    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
    {
        if (_Source.getEditor().getText()== null)
        {
            return;
        }
        if (_Source.getEditor().getText().length() <= 1)
        {
            return;
        }

        List<String> srcList = _HintGenerator.GetHint(_Source.getEditor().getText(), _MethodLines, _CurrentCls);
        ObservableList<String> list = FXCollections.observableArrayList(srcList);

        _Source.setItems(list);
        _Source.show();
    }
}
