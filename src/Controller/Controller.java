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

import Hint.HintFactory;
import Hint.IHintGenerator;
import Hint.ModelActions;
import Hint.Model_Java;
import Model.ACClass;
import Model.InputParameter;
import Model.Method;
import Model.ModelFactory;
import Model.Project;
import Model.StaticStrings;
import Translation.TranslateToJava;
import Utility.GetArrayList;
import View.ClassDialogContent;
import View.TreeViewContent;
import View.TreeViewItemType;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Dialog;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.stage.FileChooser;

/**
 *
 * @author Liu
 */
public class Controller
{

    private Project _Project;
    private IHintGenerator _HintGenerator;
    private TreeView<TreeViewContent> _TreeView;
    private BorderPane _BaseBorderPane;
    private Pane _MainPane;
    private final Map<Button, ACClass> _ButtonClsProj = new HashMap<>();
    private final Map<TreeViewContent, Button> _TreeViewItemButtonProj = new HashMap<>();
    private final Map<Button, Map<Button, Line[]>> _Arrows = new HashMap<>();

    public Scene GetMainScene()
    {
        Button newProj = new Button("new project");
        newProj.setOnMouseClicked(ev -> this.OnNewProject());
        Button save = new Button("save project");
        save.setOnMouseClicked(ev -> this.OnSave());
        Button load = new Button("load project");
        load.setOnMouseClicked(ev -> this.OnLoad());
        Button translate = new Button("translate to java");
        translate.setOnMouseClicked(ev -> this.OnTranslate());
        ToolBar toolBar = new ToolBar(newProj, save, load, translate);
        BorderPane borderPane = new BorderPane();
        _BaseBorderPane = borderPane;
        borderPane.setTop(toolBar);
        return new Scene(_BaseBorderPane, 1000, 1000);
    }

    private void OnSave()
    {
        if (_Project == null)
        {
            (new Alert(Alert.AlertType.ERROR, "No project", ButtonType.OK)).showAndWait();
        } else
        {
            try
            {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setInitialDirectory(new File("./"));
                File file = fileChooser.showSaveDialog(null);
                if (file == null)
                {
                    return;
                } else
                {
                    ModelFactory.SaveToXML(Paths.get(file.getParent()), Paths.get(file.getPath()), _Project);
                }
            } catch (Exception exception)
            {
                (new Alert(Alert.AlertType.ERROR, "Cannot Save.\nCause:\n" + exception.toString(), ButtonType.OK)).showAndWait();
            }
        }
    }

    private void OnLoad()
    {
        _Project = this.GetProject();
        if (_Project == null)
        {
            return;
        }
        _HintGenerator = this.GetHintGenerator(_Project);
        if (_HintGenerator == null)
        {
            return;
        }
        this.ClearMainPane(_Project.GetName());

        for (ACClass cls : _Project.GetClasses())
        {
            TreeItem<TreeViewContent> clsItem = this.CheckPackagesAndReturnLeave(cls.GetName().getValue());
            StringExpression[] expressions = this.GetClsNameBindings(clsItem);
            cls.GetName().bind(Bindings.concat(expressions));
            Button clsButton = this.GetButton(cls);
            this.AddClsToSystem(cls, clsButton, clsItem.getValue());
        }

        for (ACClass cls : _Project.GetClasses())
        {
            Button clsButton = this.GetButtonWithName(cls.GetName().getValue());
            this.CheckAndAddNewArrows(cls, clsButton);
        }

    }

    private TreeItem<TreeViewContent> CheckPackagesAndReturnLeave(String clsName)
    {
        String[] parts = clsName.split("\\.");
        TreeItem<TreeViewContent> item = _TreeView.getRoot();
        for (int index = 0; index < parts.length; index++)
        {
            TreeItem<TreeViewContent> hasName = null;
            for (TreeItem<TreeViewContent> content : item.getChildren())
            {
                if (content.getValue().StringProperty().getValue().equals(parts[index]))
                {
                    hasName = content;
                    break;
                }
            }
            if (hasName != null)
            {
                item = hasName;
            } else
            {
                String type = TreeViewItemType.Package;
                if (index == parts.length - 1)
                {
                    type = TreeViewItemType.Class;
                }
                TreeItem<TreeViewContent> newItem = new TreeItem<>(new TreeViewContent(type, parts[index]));
                item.getChildren().add(newItem);
                item = newItem;
            }
        }
        return item;
    }

    private void OnTranslate()
    {
        if (_Project == null)
        {
            (new Alert(Alert.AlertType.ERROR, "No Project", ButtonType.OK)).showAndWait();
        } else
        {
            try
            {
                (new TranslateToJava()).Translate(_Project, "./test/");
            } catch (Exception exception)
            {
                (new Alert(Alert.AlertType.ERROR, "Cannot Save.\nCause:\n" + exception.toString(), ButtonType.OK)).showAndWait();
            }
        }
    }

    private void ClearMainPane(StringProperty projName)
    {
        _TreeView = new TreeView<>();
        TreeViewContent rootContent = new TreeViewContent(TreeViewItemType.Project, projName.getValue());
        projName.bind(rootContent.StringProperty());
        TreeItem<TreeViewContent> root = new TreeItem<>(rootContent);
        _TreeView.setRoot(root);
        _TreeView.setContextMenu(this.GetTreeViewContextMenu());
        if (_MainPane == null)
        {
            _MainPane = new Pane();
            _MainPane.setPrefSize(800, 600);
        }
        _MainPane.getChildren().clear();
        if (_BaseBorderPane.getCenter() == null)
        {
            _BaseBorderPane.setCenter(_MainPane);
        }
        _BaseBorderPane.setLeft(_TreeView);
        _ButtonClsProj.clear();
        _TreeViewItemButtonProj.clear();
        _Arrows.clear();
    }

    private Project GetProject()
    {
        javafx.stage.FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("./"));
        File file = fileChooser.showOpenDialog(null);
        if (file == null)
        {
            new Alert(Alert.AlertType.ERROR, "No file", ButtonType.CLOSE).showAndWait();
        } else
        {
            try
            {
                return ModelFactory.LoadFromXML(file.toPath());
            } catch (Exception exception)
            {
                new Alert(Alert.AlertType.ERROR, "Error:\n" + exception.toString(), ButtonType.CLOSE).showAndWait();
            }
        }
        return null;
    }

    private IHintGenerator GetHintGenerator(Project project)
    {
        Path javaPath = Paths.get("./java.xml");
        ModelActions actions = new ModelActions();
        if (Files.exists(javaPath) == false)
        {
            try
            {
                actions.CreateModel(Paths.get("./"), Paths.get("./java.xml"));
            } catch (Exception exception)
            {
                exception.printStackTrace(System.out);
                new Alert(Alert.AlertType.ERROR, "Create cache file failed.\n Cause:\n" + exception.toString(), ButtonType.CLOSE).showAndWait();

                return null;
            }
        }
        try
        {
            Model_Java model = actions.LoadFromFile(javaPath);
            return HintFactory.GetHintGenerator(project, model);
        } catch (Exception exception)
        {
            exception.printStackTrace(System.out);
            new Alert(Alert.AlertType.ERROR, "Loading failed.\n Cause:\n" + exception.toString(), ButtonType.CLOSE).showAndWait();
            return null;
        }
    }

    private void OnNewProject()
    {
        Dialog inputProjName = new Dialog();
        TextField projNameTextField = new TextField("input name");
        inputProjName.getDialogPane().setContent(projNameTextField);
        inputProjName.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        Optional<ButtonType> optional = inputProjName.showAndWait();
        if (optional.isPresent() && optional.get().equals(ButtonType.OK))
        {

            this.ClearMainPane(projNameTextField.textProperty());
            _Project = ModelFactory.GetProject_Empty();

            _HintGenerator = this.GetHintGenerator(_Project);
            if (_HintGenerator == null)
            {
                return;
            }
            TreeViewContent root = new TreeViewContent(TreeViewItemType.Project, projNameTextField.getText());
            TreeItem<TreeViewContent> rootItem = new TreeItem<>(root);
            _TreeView = new TreeView<>(rootItem);
            _TreeView.setContextMenu(this.GetTreeViewContextMenu());
            _Project.GetName().bind(root.StringProperty());
            TreeViewContent pkg = new TreeViewContent(TreeViewItemType.Package, projNameTextField.getText());
            TreeItem<TreeViewContent> pkgItem = new TreeItem<>(pkg);
            rootItem.getChildren().add(pkgItem);

            TreeViewContent mainCls = new TreeViewContent(TreeViewItemType.Class, projNameTextField.getText().toUpperCase());
            TreeItem<TreeViewContent> clsItem = new TreeItem<>(mainCls);
            pkgItem.getChildren().add(clsItem);

            ACClass cls = ModelFactory.GetClass_Empty();
            Method mthd
                    = ModelFactory.GetMethod_Empty();
            mthd.isStatic().setValue(true);
            mthd.GetName().setValue("main");
            mthd.GetOutputType().setValue("void");
            mthd.GetVisibility().setValue(StaticStrings.Public);
            mthd.GetInputParameters().add(ModelFactory.GetInputParameterWithValue("String[]", "args"));
            cls.GetMethods().add(mthd);
            cls.GetName().bind(Bindings.concat(pkg.StringProperty(), ".", mainCls.StringProperty()));
            Button button = this.GetButton(cls);
            button.setOnMouseClicked(event -> this.OnClickOnClsButton(event));

            _Project.GetClasses().add(cls);

            this.AddClsToSystem(cls, button, mainCls);
            ScrollPane scrollPane = new ScrollPane(_MainPane);
            _BaseBorderPane.setLeft(_TreeView);
            _BaseBorderPane.setCenter(scrollPane);
        }
    }

    private void OnClickOnClsButton(MouseEvent event)
    {
        if(event.isStillSincePress() == false)
            return;
        Button button = (Button) event.getSource();
        ACClass src = _ButtonClsProj.get(button);
        Dialog dialog = new Dialog();
        ClassDialogContent content = new ClassDialogContent(src, _HintGenerator);
        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        Optional<ButtonType> optional = dialog.showAndWait();
        if (optional.isPresent() && optional.get().equals(ButtonType.OK))
        {
            this.CheckAndAddNewArrows(src, button);
        }        
    }

    private void CheckAndAddNewArrows(ACClass src, Button button)
    {
        if (src.GetExtends().size() > 0)
        {
            String extdcls = src.GetExtends().get(0).getValue();
            if (extdcls.equals("") == false)
            {
                Button extdButton = this.GetButtonWithName(extdcls);
                if (extdButton == null)
                {
                    new Alert(Alert.AlertType.ERROR, "No button with name : " + extdcls, ButtonType.CLOSE).showAndWait();
                } else if (_Arrows.get(button).containsKey(extdButton) == false)
                {
                    boolean isDashed = false;
                    this.DrawArrow(button, extdButton, isDashed);
                }
            }
        }
        for (StringProperty imp : src.GetImplements())
        {
            String impString = imp.getValue();
            Button impButton = this.GetButtonWithName(impString);
            if (impButton == null)
            {
                new Alert(Alert.AlertType.ERROR, "No class/interface with name : " + impString, ButtonType.CLOSE).showAndWait();
            } else if (_Arrows.get(button).containsKey(impButton) == false)
            {
                boolean isDashed = true;
                this.DrawArrow(button, impButton, isDashed);
            }
        }
    }

    private Button GetButtonWithName(String name)
    {
        for (Button btn : _ButtonClsProj.keySet())
        {
            if (btn.textProperty().getValue().equals(name))
            {
                return btn;
            }
        }
        return null;
    }

    private void DrawArrow(Button from, Button to, boolean isDashed)
    {
        Line[] arrow = ArrowManagement.GetArrow(from, to, isDashed);
        _MainPane.getChildren().addAll(arrow);
        _Arrows.get(from).put(to, arrow);
    }

    private ContextMenu GetTreeViewContextMenu()
    {
        MenuItem addPkg = new MenuItem("Add Pkg");
        MenuItem addCls = new MenuItem("Add cls/interface/enum");
        //   MenuItem delItem = new MenuItem("Delete Item");
        addPkg.setOnAction(ev -> this.OnAddPkgOnTreeView());
        addCls.setOnAction(ev -> this.OnAddClsOnTreeView());
        //  delItem.setOnAction(ev-> this.OnDelItemOnTreeView(ev));
        return new ContextMenu(addPkg, addCls);
    }

    private void OnAddPkgOnTreeView()
    {
        TreeItem<TreeViewContent> activeItem = _TreeView.getSelectionModel().getSelectedItem();
        TreeItem<TreeViewContent> newItem = new TreeItem<>(new TreeViewContent(TreeViewItemType.Package, "new pkg"));
        activeItem.getChildren().add(newItem);
    }

    private void OnAddClsOnTreeView()
    {
        TreeItem<TreeViewContent> activeItem = _TreeView.getSelectionModel().getSelectedItem();
        TreeItem<TreeViewContent> clsItem = new TreeItem<>(new TreeViewContent(TreeViewItemType.Class, "new cls"));
        activeItem.getChildren().add(clsItem);
        ACClass cls = ModelFactory.GetClass_Empty();

        Button clsButton = this.GetButton(cls);
        cls.GetName().bind(Bindings.concat(this.GetClsNameBindings(clsItem)));

        this.AddClsToSystem(cls, clsButton, clsItem.getValue());
        _Project.GetClasses().add(cls);
    }

    private void AddClsToSystem(ACClass cls, Button clsButton, TreeViewContent clsItem)
    {
        _MainPane.getChildren().add(clsButton);
        _Arrows.put(clsButton, new HashMap<>());
        _ButtonClsProj.put(clsButton, cls);
        _TreeViewItemButtonProj.put(clsItem, clsButton);
    }

    private StringExpression[] GetClsNameBindings(TreeItem<TreeViewContent> currentTreeItem)
    {
        TreeItem<TreeViewContent> item = currentTreeItem;
        Deque<TreeViewContent> stack = new ArrayDeque<>();
        while (item.getParent() != null)
        {
            if (item == _TreeView.getRoot())
            {
                break;
            }

            stack.push(item.getValue());
            item = item.getParent();
        }
        StringExpression[] expressions = new StringExpression[stack.size()];
        for (int i = 0; i < expressions.length; i++)
        {
            expressions[i] = stack.pop().StringProperty();
            if (i < expressions.length - 1)
            {
                expressions[i] = expressions[i].concat(".");
            }
        }
        return expressions;
    }

    private void OnDelItemOnTreeView(ActionEvent event)
    {
        throw new UnsupportedOperationException();
    }

    private Button GetButton(ACClass cls)
    {
        Button button = new Button(cls.GetName().getValue());
        button.textProperty().bind(cls.GetName());
        button.setLayoutX(cls.GetLayoutX().getValue());
        button.setLayoutY(cls.GetLayoutY().getValue());
        button.setOnMouseDragged(new ButtonMoveEventHandler(_Arrows));
        button.setOnMouseClicked(event -> this.OnClickOnClsButton(event));
        button.textProperty().bind(cls.GetName());
        cls.GetLayoutX().bind(button.layoutXProperty());
        cls.GetLayoutY().bind(button.layoutYProperty());
        return button;
    }
}
