/*
 * The MIT License
 *
 * Copyright 2015 Liu.
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

import Controller.IController;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.HBox;

/**
 *
 * @author Liu
 */
public class TreeViewUseTreeViewItem extends TreeView<HBox>
{

    private final IController _Controller;

    public TreeViewUseTreeViewItem(IController controller)
    {
        super();
        _Controller = controller;
        this.setOnContextMenuRequested(ev->this.GetMouseContextMenu());
    }

    public TreeViewUseTreeViewItem(IController controller, TreeViewItem root)
    {
        super(root);
        _Controller = controller;
        this.setOnContextMenuRequested(ev->this.GetMouseContextMenu());
    }

    private void OnAddTreeItem(boolean isCls)
    {
        if (isCls)
        {
            _Controller.AddClass();
        } else
        {
            _Controller.AddPackage();
        }
    }

    private void OnDelTreeItem()
    {
        TreeViewItem node = (TreeViewItem) this.getSelectionModel().getSelectedItem();
        String str = node.GetLabelText();

        if (str.equals(ViewStaticStrings.Class) || str.equals(ViewStaticStrings.Package))
        {
            _Controller.DeleteTreeItem();
            node.getParent().getChildren().remove(node);
        }
    }

    private void GetMouseContextMenu()
    {
        MenuItem del = new MenuItem("Delete");
        del.setOnAction(evDel -> this.OnDelTreeItem());

        TreeViewItem item = (TreeViewItem) this.getSelectionModel().getSelectedItem();
        String text = item.GetLabelText();
        if (text.equals(ViewStaticStrings.Package) || text.equals(ViewStaticStrings.Project))
        {
            MenuItem addPkg = new MenuItem("Add Package");
            MenuItem addCls = new MenuItem("Add ClassObject");

            addPkg.setOnAction(evAddPkg ->
            {
                _Controller.AddPackage();
            });
            addCls.setOnAction(evAddCls ->
            {
                _Controller.AddClass();
            });
            ContextMenu menu = new ContextMenu(addPkg, new SeparatorMenuItem(), addCls, new SeparatorMenuItem(), del);
            this.setContextMenu(menu);
        } else
        {
            if(text.equals(ViewStaticStrings.Class))
            {
                ContextMenu menu = new ContextMenu(del);
                this.setContextMenu(menu);
            }
        }
    }
}
