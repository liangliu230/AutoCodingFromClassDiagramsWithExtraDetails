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
package Controller;

import FileGeneration.IGenerateFiles;
import FileGeneration.Java.FunctionStaticStrings;
import FileGeneration.Java.JavaFileGenerator;
import Model.Model.ACClass;
import Model.Model.Field;
import Model.Model.FullName;
import Model.Model.Function;
import Model.Model.FunctionInput;
import Model.Model.Method;
import Model.Model.Modifiers;
import Model.Model.Name;
import Model.Model.Project;
import Model.Model.Visibility;
import Model.StaticStrings;
import Model.interfaces.IBase;
import Model.interfaces.IClass;
import Model.interfaces.IField;
import Model.interfaces.IFullName;
import Model.interfaces.IFunctionInput;
import Model.interfaces.IGetModifiers;
import Model.interfaces.IGetVisibility;
import Model.interfaces.IMethod;
import Model.interfaces.IMethodLine;
import Model.interfaces.IModifiers;
import Model.interfaces.IName;
import Model.interfaces.IPackage;
import Model.interfaces.IParameter;
import Model.interfaces.IProject;
import View.ClassControl;
import View.Interfaces.IGetRootPane;
import View.Interfaces.IGetToolBar;
import View.Interfaces.IGetTreeView;
import View.RootPane;
import View.TreeViewItem;
import View.TreeViewUseTreeViewItem;
import View.ViewStaticStrings;
import java.io.File;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import javafx.beans.property.ListProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.transform.Rotate;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

/**
 *
 * @author Liu
 */
public class Controller implements IController
{

    private final IGetToolBar _ToolBarGetter;
    private final IGetTreeView _TreeViewGetter;
    private final IGetRootPane _RootPaneGetter;
    private final Scene _Scene;
    private final BorderPane _BorderPane;
    private final Stage _Stage;

    private final Map<ClassControl, IClass> _ClassProjection;
    private final Map<TreeViewItem, ClassControl> _TreeItemClassProjection;
    private final Map<TreeViewItem, IPackage> _TreeViewIPackageProjection;

    private IProject _Project;
    private RootPane _RootPane;
    private TreeViewUseTreeViewItem _TreeView;
    /**
     * line[] length 3, index 0 solid line (extends) or dash line (implements),
     * 1 arrow left line 2 arrow right line
     */
    private final Map<ClassControl, Map<ClassControl, Line[]>> _ClsLineProjection;

    public Controller(IGetRootPane rpGetter, IGetTreeView tvGetter, IGetToolBar tbGetter, Stage stage)
    {
        _ToolBarGetter = tbGetter;
        _TreeViewGetter = tvGetter;
        _RootPaneGetter = rpGetter;
        _BorderPane = new BorderPane();
        ToolBar toolBar = _ToolBarGetter.Get(this);
        _BorderPane.setTop(toolBar);
        _Scene = new Scene(_BorderPane, 800, 600, true);
        _ClassProjection = new HashMap<>();
        _Stage = stage;
        _TreeItemClassProjection = new HashMap<>();
        _TreeViewIPackageProjection = new HashMap<>();
        _ClsLineProjection = new HashMap<>();
    }

    private void ClearProjections()
    {
        _ClassProjection.clear();
        _TreeItemClassProjection.clear();
        _TreeViewIPackageProjection.clear();
        _ClsLineProjection.clear();
    }

    @Override
    public Scene GetScene()
    {
        /*
        
         TreeViewUseTreeViewItem tv = _TreeViewGetter.Get(
         this, 
         new TreeViewItem(
         ViewStaticStrings.Project, 
         ViewStaticStrings.DefaultProjName,
         ViewStaticStrings.DefaultProjName) );
         ScrollablePane pane = _RootPaneGetter.Get(500, 500);
      
         _BorderPane = new BorderPane();
         //borderPane.setCenter(pane);
         //
         _BorderPane.setLeft(tv);
         */

        return _Scene;
    }

    @Override
    public void Exit()
    {

    }

    @Override
    public void SaveProject()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void NewProject()
    {
        if (_Project != null)
        {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Save existing project?", ButtonType.OK, ButtonType.NO);
            java.util.Optional<ButtonType> response = alert.showAndWait();
            if (response.isPresent() && response.get() == ButtonType.OK)
            {
                this.SaveProject();
            }
            _Project = null;
            this.ClearProjections();
        }
        _Project = new Project();
        TreeViewItem item = new TreeViewItem(ViewStaticStrings.Project, ViewStaticStrings.DefaultProjName, ViewStaticStrings.DefaultProjName);
        _Project.GetName().GetName().bind(item.GetTextField().textProperty());
        TreeViewUseTreeViewItem treeview = _TreeViewGetter.Get(this, item);
        ScrollPane scrollablePane = _RootPaneGetter.Get(500, 500);
        //scrollablePane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        _BorderPane.setLeft(treeview);
        _BorderPane.setCenter(scrollablePane);
        _TreeView = treeview;
        _RootPane = (RootPane) scrollablePane.getContent();
        _RootPane.setTranslateZ(0);
       // _Stage.titleProperty().bind(item.GetTextField().textProperty());
        _Stage.sizeToScene();
    }

    @Override
    public void LoadProject()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void TranlateProject()
    {
        if (_Project == null)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR, "No project.", ButtonType.OK);
            alert.showAndWait();
        } else
        {
            DirectoryChooser chooser = new DirectoryChooser();
            File dir = chooser.showDialog(_Stage);
            if (dir != null)
            {
                IGenerateFiles generator = new JavaFileGenerator();
                try
                {
                    generator.Generate(_Project, dir.getAbsolutePath());
                } catch (Exception exception)
                {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "There are an exception: \n" + exception.getLocalizedMessage(), ButtonType.OK);
                    alert.showAndWait();
                }
            }
        }
    }

    @Override
    public void AddPackage()
    {
        TreeViewItem pkg = (TreeViewItem) _TreeView.getSelectionModel().getSelectedItem();
        if (pkg == null)
        {
            return;
        }
        TreeViewItem item = new TreeViewItem(ViewStaticStrings.Package, ViewStaticStrings.DefaultPackageName, ViewStaticStrings.DefaultPackageName);

        pkg.getChildren().add(item);
    }

    @Override
    public void DeleteTreeItem()
    {
        TreeViewItem item = (TreeViewItem) _TreeView.getSelectionModel().getSelectedItem();
        if (item == null)
        {
            return;
        }
        String mark = item.GetLabelText();
        if (mark.equals(ViewStaticStrings.Package))
        {
            IPackage pkg = _TreeViewIPackageProjection.get(item);
            _Project.RemovePackage(pkg);
            item.getParent().getChildren().remove(item);
        } else
        {
            if (mark.equals(ViewStaticStrings.Class))
            {

                ClassControl clsControl = _TreeItemClassProjection.get(item);
                _RootPane.getChildren().remove(clsControl);
                IClass cls = _ClassProjection.get(clsControl);
                _Project.RemoveClass(cls);
                item.getParent().getChildren().remove(item);
            } else
            {
                return;
            }
        }
    }

    @Override
    public void AddClass()
    {
        Deque<StringProperty> stack = new ArrayDeque<>();
        TreeViewItem selected = (TreeViewItem) _TreeView.getSelectionModel().getSelectedItem();
        if (selected != null)
        {
            TreeViewItem item = selected;
            while (item != null && item.GetLabelText().equals(ViewStaticStrings.Project) == false)
            {
                stack.push(item.GetTextField().textProperty());
                item = (TreeViewItem) item.getParent();
            }
            IName[] pkgs = new IName[stack.size()];
            int count = 0;
            while (stack.isEmpty() == false)
            {
                StringProperty strp = stack.pop();
                pkgs[count] = new Name(strp.getValue());
                pkgs[count].GetName().bind(strp);
                count++;
            }
            Name name = new Name(ViewStaticStrings.DefaultClassName);
            TreeViewItem clsItem = new TreeViewItem(ViewStaticStrings.Class, ViewStaticStrings.DefaultClassName, ViewStaticStrings.DefaultClassName);
            selected.getChildren().add(clsItem);
            name.GetName().bindBidirectional(clsItem.GetTextField().textProperty());
            IFullName fullName = new FullName(pkgs, name);
            IClass cls = this.GetACClass(fullName);
            ClassControl clsControl = new ClassControl(20, 20, cls);
            cls.GetLayoutX().bind(clsControl.layoutXProperty());
            cls.GetLayoutY().bind(clsControl.layoutYProperty());
            clsControl.setOnMouseClicked(ev ->
            {
                ClassControl source = (ClassControl) ev.getSource();
                IClass sourceCls = source._ACClass;
                Label clsName = new Label(sourceCls.GetFullName().toString());
                ComboBox<String> visibility = this.GetVisibility(sourceCls);
                HBox clsType = this.GetClsType(sourceCls);
                HBox clsModifiers = this.GetModifiers(sourceCls);

                BorderPane fieldList
                        = this.GetClsList("field list",
                                new AddFieldsEventHandler(sourceCls.GetFields(), this),
                                new DeleteFieldsEventHandler(sourceCls.GetFields(), this)
                        );
                BorderPane methodList
                        = this.GetClsList("method list",
                                new AddMethodsEvendHandler(sourceCls.GetMethods(), this),
                                new DeleteMethodsEventHandler(sourceCls.GetMethods(), this)
                        );
                BorderPane extendList
                        = this.GetClsList("extends",
                                new AddFullItemEventHandler(sourceCls.GetExtends(), this),
                                new DeleteFullNameEventHandler(sourceCls.GetExtends(), this)
                        );
                BorderPane impList
                        = this.GetClsList("implements",
                                new AddFullItemEventHandler(sourceCls.GetImplements(), this),
                                new DeleteFullNameEventHandler(sourceCls.GetImplements(), this)
                        );
                VBox dialog = new VBox(clsName, visibility, clsType, clsModifiers, fieldList, methodList, extendList, impList);

                Dialog dialog1 = new Dialog();
                dialog1.getDialogPane().setPrefSize(400, 1000);
                dialog1.getDialogPane().setContent(dialog);
                dialog1.getDialogPane().getButtonTypes().addAll(ButtonType.OK);
                Optional<ButtonType> result = dialog1.showAndWait();
                if (result.isPresent())
                {
                    source.SetButtonText();
                    List<IFullName> extndList = sourceCls.GetExtends();
                    List<IFullName> implmntList = sourceCls.GetImplements();
                    this.DrawArrow(source, extndList, false);
                    this.DrawArrow(source, implmntList, true);
                }
            });
            clsControl.setOnMouseDragged(new MouseDragEventHandler());
            _RootPane.getChildren().add(clsControl);
            _ClassProjection.put(clsControl, cls);
            _TreeItemClassProjection.put(clsItem, clsControl);
            _Project.GetClasses().add(cls);
            _ClsLineProjection.put(clsControl, new HashMap<>());
        }
    }

    private class AddFieldsEventHandler extends ItemEventHandler<IField>
    {

        public AddFieldsEventHandler(List<IField> list, Controller controller)
        {
            super(list, controller);
        }

        @Override
        public void handle(MouseEvent event)
        {
            Label label = new Label();
            IField field = _Control.GetNewField();
            VBox fieldBox = _Control.GetField(field);
            _List.add(field);
            Dialog fieldDialog = new Dialog();
            fieldDialog.getDialogPane().setContent(fieldBox);
            fieldDialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
            fieldDialog.showAndWait();
            label.setText(field.toString());
            _GridPane.addColumn(0, new HBox(new CheckBox(), label));
        }

    }

    private class AddMethodsEvendHandler extends ItemEventHandler<IMethod>
    {

        public AddMethodsEvendHandler(List<IMethod> list, Controller controller)
        {
            super(list, controller);
        }

        @Override
        public void handle(MouseEvent event)
        {
            Label label = new Label();

            IMethod method = _Control.GetNewMethod();
            VBox methodBox = _Control.GetMethod(method);
            _List.add(method);
            Dialog methodDialog = new Dialog();
            methodDialog.getDialogPane().setContent(methodBox);
            methodDialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
            methodDialog.showAndWait();
            for (Node n : methodBox.getChildren())
            {
                if (n instanceof CheckBox)
                {
                    CheckBox box = (CheckBox) n;
                    if (box.isSelected())
                    {
                        method.GetThrows().add(new FullName(new IName[0], new Name("Exception")));
                    }
                }
            }
            label.setText(method.toString());
            _GridPane.addColumn(0, new HBox(new CheckBox(), label));
        }

    }

    private class AddFullItemEventHandler extends ItemEventHandler<IFullName>
    {

        public AddFullItemEventHandler(List<IFullName> list, Controller controller)
        {
            super(list, controller);
        }

        @Override
        public void handle(MouseEvent event)
        {
            Label label = new Label();

            IFullName newName = new FullName();
            ComboBox<String> box = _Control.GetCombobox(newName);

            _List.add(newName);
            Dialog nameDialog = new Dialog();
            nameDialog.getDialogPane().setContent(new HBox(box));
            nameDialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
            nameDialog.showAndWait();
            if (box.getValue() == null || box.getValue().equals(""))
            {
                return;
            }
            label.setText(newName.toString());
            _GridPane.addColumn(0, new HBox(new CheckBox(), label));
        }

    }

    private abstract class ItemEventHandler<T> implements EventHandler<MouseEvent>
    {

        protected GridPane _GridPane;
        protected List<T> _List;

        public void SetGridPane(GridPane pane)
        {
            _GridPane = pane;
        }
        protected Controller _Control;

        public ItemEventHandler(List<T> list, Controller controller)
        {
            _List = list;
            _Control = controller;
        }

    }

    class DeleteFullNameEventHandler extends ItemEventHandler<IFullName>
    {

        public DeleteFullNameEventHandler(List<IFullName> list, Controller controller)
        {
            super(list, controller);
        }

        @Override
        public void handle(MouseEvent event)
        {
            List<Node> innerlist = new ArrayList<>();
            for (Node node : _GridPane.getChildren())
            {
                if (node instanceof HBox)
                {
                    for (Node n : ((HBox) node).getChildren())
                    {
                        if (n instanceof CheckBox)
                        {
                            if (((CheckBox) n).isSelected())
                            {
                                innerlist.add(node);
                            }
                            break;
                        }
                    }
                }
            }
            List<IBase> delList = new ArrayList<>();
            for (Node node : innerlist)
            {
                HBox box = (HBox) node;
                for (Node content : box.getChildren())
                {
                    if (content instanceof Label)
                    {
                        Label label = (Label) content;
                        for (IFullName item : _List)
                        {

                            IFullName fullName = (IFullName) item;
                            if (label.getText().contains(fullName.GetClassName().GetName().getValue()))
                            {
                                delList.add(fullName);
                            }
                        }
                    }
                }
            }
            _List.removeAll(delList);
            _GridPane.getChildren().removeAll(innerlist);
        }

    }

    class DeleteMethodsEventHandler extends ItemEventHandler<IMethod>
    {

        public DeleteMethodsEventHandler(List<IMethod> list, Controller controller)
        {
            super(list, controller);
        }

        @Override
        public void handle(MouseEvent event)
        {
            List<Node> innerlist = new ArrayList<>();
            for (Node node : _GridPane.getChildren())
            {
                if (node instanceof HBox)
                {
                    for (Node n : ((HBox) node).getChildren())
                    {
                        if (n instanceof CheckBox)
                        {
                            if (((CheckBox) n).isSelected())
                            {
                                innerlist.add(node);
                            }
                            break;
                        }
                    }
                }
            }
            List<IBase> delList = new ArrayList<>();
            for (Node node : innerlist)
            {
                HBox box = (HBox) node;
                for (Node content : box.getChildren())
                {
                    if (content instanceof Label)
                    {
                        Label label = (Label) content;
                        for (IMethod item : _List)
                        {

                            IMethod method = (IMethod) item;
                            if (label.getText().contains(method.GetName().getValue()))
                            {
                                delList.add(method);
                            }

                        }
                    }
                }
            }
            _List.removeAll(delList);
            _GridPane.getChildren().removeAll(innerlist);
        }

    }

    class DeleteFieldsEventHandler extends ItemEventHandler<IField>
    {

        public DeleteFieldsEventHandler(List<IField> list, Controller controller)
        {
            super(list, controller);
        }

        @Override
        public void handle(MouseEvent event)
        {
            List<Node> innerlist = new ArrayList<>();
            for (Node node : _GridPane.getChildren())
            {
                if (node instanceof HBox)
                {
                    for (Node n : ((HBox) node).getChildren())
                    {
                        if (n instanceof CheckBox)
                        {
                            if (((CheckBox) n).isSelected())
                            {
                                innerlist.add(node);
                            }
                            break;
                        }
                    }
                }
            }
            List<IBase> delList = new ArrayList<>();
            for (Node node : innerlist)
            {
                HBox box = (HBox) node;
                for (Node content : box.getChildren())
                {
                    if (content instanceof Label)
                    {
                        Label label = (Label) content;
                        for (IField item : _List)
                        {

                            IField field = (IField) item;
                            if (label.getText().contains(field.GetName().GetName().getValue()))
                            {
                                delList.add(field);
                            }

                        }
                    }
                }
            }
            _List.removeAll(delList);
            _GridPane.getChildren().removeAll(innerlist);
        }
    }

    private void DrawArrow(ClassControl source, List<IFullName> list, boolean isDashed)
    {
        Map<ClassControl, Line[]> drawedLines = _ClsLineProjection.get(source);
        for (IFullName name : list)
        {
            ClassControl ctrl = this.GetButtonWithName(name.toString());
            if (drawedLines.containsKey(ctrl))
            {
                continue;
            }

            double startX = ctrl.getLayoutX(),
                    startY = ctrl.getLayoutY(),
                    endX = source.getLayoutX(),
                    endY = source.getLayoutY();
            Line line = new Line(startX, startY, endX, endY);
            if (isDashed)
            {
                line.getStrokeDashArray().addAll(10.0, 10.0);
            }
            double length = Math.sqrt((startX - endX) * (startX - endX) + (startY - endY) * (startY - endY));
            length /= _ArrowLength;
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
            drawedLines.put(ctrl, lines);
            _RootPane.getChildren().addAll(line, left, right);
        }
    }

    private ClassControl GetButtonWithName(String name)
    {
        IClass cls = null;
        for (IClass cs : _ClassProjection.values())
        {
            if (cs.GetFullName().toString().equals(name))
            {
                cls = cs;
                break;
            }
        }
        ClassControl clsCtrl = null;
        for (ClassControl ctrl : _ClassProjection.keySet())
        {
            if (_ClassProjection.get(ctrl).equals(cls))
            {
                clsCtrl = ctrl;
                break;
            }
        }
        return clsCtrl;
    }

    enum ListType
    {

        extend, implement, field, method
    }

    ComboBox<String> GetVisibility(IGetVisibility source)
    {
        ObservableList<String> list
                = FXCollections.observableArrayList(
                        StaticStrings.Public,
                        StaticStrings.Protected,
                        StaticStrings.Private,
                        StaticStrings.Package);
        ComboBox<String> box = new ComboBox<>(list);
        box.setValue(source.GetVisibility().GetVisibility().getValue());
        source.GetVisibility().GetVisibility().bind(box.valueProperty());
        box.setPrefWidth(170);
        return box;
    }

    BorderPane GetClsList(String name, ItemEventHandler addhandler, ItemEventHandler delHandler)
    {
        Label namelLabel = new Label(name);
        Button add = new Button("Add");
        Button del = new Button("Del");

        HBox top = new HBox(namelLabel, add, del);
        GridPane gridPane = new GridPane();
        gridPane.setPrefSize(500, 200);
        gridPane.addColumn(0);
        List list = addhandler._List;
        for (int index = 0; index < list.size(); index++)
        {
            Object item = list.get(index);
            Label str = new Label(item.toString());
            gridPane.add(str, 0, index);
        }
        addhandler.SetGridPane(gridPane);
        add.setOnMouseClicked(addhandler);
        delHandler.SetGridPane(gridPane);
        del.setOnMouseClicked(delHandler);

        BorderPane borderPane = new BorderPane(gridPane);
        borderPane.setTop(top);
        borderPane.setPrefSize(550, 240);
        return borderPane;
    }

    IField GetNewField()
    {
        return new Field(
                new Name(),
                new FullName(),
                new Modifiers(),
                new Visibility(StaticStrings.Public),
                new FunctionInput(new Function(), new ArrayList<>()));
    }

    IMethod GetNewMethod()
    {
        return new Method(
                new Name(),
                new FullName(),
                new ArrayList<IParameter>(),
                new ArrayList<IMethodLine>(),
                new ArrayList<IFunctionInput>(),
                new Visibility(StaticStrings.Public),
                new Modifiers(),
                true,
                new ArrayList<>());
    }

    private VBox GetField(IField field)
    {
        TextField nameField = new TextField(field.GetName().GetName().getValue());
        Label name = new Label("Name");
        HBox nameBox = new HBox(name, nameField);
        ComboBox<String> visibility = this.GetVisibility(field);
        HBox modifiers = this.GetModifiers(field);
        TextField defaultValue = new TextField(field.GetDefaultValue().GetFunction().GetFunction().getValue());
        Label defValue = new Label("Default Value");
        HBox valueBox = new HBox(defValue, defaultValue);
        field.GetVisibility().GetVisibility().bind(visibility.valueProperty());
        field.GetName().GetName().bind(nameField.textProperty());
        return new VBox(nameBox, visibility, modifiers, valueBox);
    }

    private VBox GetMethod(IMethod method)
    {
        Label nameLabel = new Label("Name");
        TextField nameTextField = new TextField(method.GetName().getValue());
        method.GetName().bind(nameTextField.textProperty());
        HBox nameHBox = new HBox(nameLabel, nameTextField);
        ComboBox<String> visibility = this.GetVisibility(method);
        HBox modifiers = this.GetModifiers(method);
        BorderPane inputParameters = this.GetInputParameterBorderPane(method);
        Label outputLabel = new Label("Output Type");
        TextField outputTextField = new TextField(method.GetOutputType().toString());
        method.GetOutputType().GetClassName().GetName().bind(outputTextField.textProperty());
        HBox outputHBox = new HBox(outputLabel, outputTextField);
        Button createMethodBody = new Button("Create Method Body");
        CheckBox throwException = new CheckBox("Throw Exception");
        throwException.setSelected(!method.GetThrows().isEmpty());
        createMethodBody.disableProperty().bind(method.GetModifiers().IsAbstract());
        createMethodBody.setOnMouseClicked(new OnMouseClickOnMethodBodyButton(method));
        VBox vBox = new VBox(nameHBox, visibility, modifiers, outputHBox, inputParameters, throwException, createMethodBody);
        return vBox;
    }

    private class OnMouseClickOnMethodBodyButton implements EventHandler<MouseEvent>
    {

        private final IMethod _Method;
        private int count = 0;

        public OnMouseClickOnMethodBodyButton(IMethod method)
        {
            _Method = method;
        }

        @Override
        public void handle(MouseEvent event)
        {
            Pane leftPane = new FlowChartPane();
            leftPane.setPrefSize(500, 500);
            ScrollPane scrollPane = new ScrollPane(leftPane);

            Dialog dialog = new Dialog();
            dialog.getDialogPane().setContent(scrollPane);
            dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
            Optional<ButtonType> optional = dialog.showAndWait();
            if (optional.isPresent())
            {
                List<Node> list = leftPane.getChildren();
                _Method.GetMethodBody().addAll(this.GetMethodLine(list));
            }
        }       

        private void PutMethodLineIntoPane(List<IMethodLine> list)
        {
            throw new UnsupportedOperationException();
        }

        private List<IMethodLine> GetMethodLine(List<Node> nodes)
        {
            throw new UnsupportedOperationException();
        }
    }

    private BorderPane GetInputParameterBorderPane(IMethod method)
    {
        Label input = new Label("Input parameters");
        Button add = new Button("Add");
        Button del = new Button("Del");
        HBox hBox = new HBox(input, add, del);
        FlowPane list = new FlowPane();
        list.setPrefSize(200, 100);
        BorderPane borderPane = new BorderPane(list);
        borderPane.setTop(hBox);
        return borderPane;
    }

    private ComboBox<String> GetCombobox(IFullName name)
    {
        ObservableList<String> list = FXCollections.observableArrayList();
        for (IClass cls : _Project.GetClasses())
        {
            list.add(cls.GetFullName().toString());
            System.out.println(cls.GetFullName().toString());
        }

        ComboBox<String> box = new ComboBox<>(list);
        name.GetClassName().GetName().bind(box.valueProperty());
        return box;
    }

    private HBox GetModifiers(IGetModifiers source)
    {
        CheckBox isAbstract = new CheckBox("abstract");
        isAbstract.setSelected(source.GetModifiers().IsAbstract().getValue());
        CheckBox isFinal = new CheckBox("final");
        isFinal.setSelected(source.GetModifiers().IsFinal().getValue());
        CheckBox isStatic = new CheckBox("static");
        isStatic.setSelected(source.GetModifiers().IsStatic().getValue());
        IModifiers modifiers = source.GetModifiers();
        modifiers.IsAbstract().bind(isAbstract.selectedProperty());
        modifiers.IsFinal().bind(isFinal.selectedProperty());
        modifiers.IsStatic().bind(isStatic.selectedProperty());
        return new HBox(isAbstract, isFinal, isStatic);
    }

    private HBox GetClsType(IClass source)
    {
        RadioButton isInterfaceRadioButton = new RadioButton("interface");
        RadioButton isEnum = new RadioButton("enum");
        RadioButton isCls = new RadioButton("class");
        isInterfaceRadioButton.setSelected(source.IsInterface().getValue());
        isEnum.setSelected(source.IsEnum().getValue());
        if (source.IsInterface().getValue() == false
                && source.IsEnum().getValue() == false)
        {
            isCls.setSelected(true);
        } else
        {
            isCls.setSelected(false);
        }
        HBox clsType = new HBox(isCls, isInterfaceRadioButton, isEnum);
        isInterfaceRadioButton.setOnMouseClicked(evirb ->
        {
            isEnum.setSelected(false);
            isCls.setSelected(false);
            isInterfaceRadioButton.setSelected(true);
        });
        isCls.setOnMouseClicked(evcrb ->
        {
            isEnum.setSelected(false);
            isInterfaceRadioButton.setSelected(false);
            isCls.setSelected(true);
        });
        isEnum.setOnMouseClicked(enerb ->
        {
            isInterfaceRadioButton.setSelected(false);
            isCls.setSelected(false);
            isEnum.setSelected(true);
        });
        source.IsEnum().bind(isEnum.selectedProperty());
        source.IsInterface().bind(isInterfaceRadioButton.selectedProperty());
        return clsType;
    }

    private IClass GetACClass(IFullName name)
    {
        return new ACClass(
                name,
                Visibility.Public,
                new Modifiers(false, false, false),
                new ArrayList<IMethod>(),
                new ArrayList<IFullName>(),
                new ArrayList<IField>(),
                new ArrayList<IFullName>(),
                false,
                false,
                true,
                20,
                20);

    }

    final double _ArrowLength = 10;
    final double _ArrowAngle = 10;

    class MouseDragEventHandler implements javafx.event.EventHandler<MouseEvent>
    {

        private double _OldScreenX = Double.NaN;
        private double _OldScreenY = Double.NaN;

        private Map<ClassControl, Line[]> GetLinesPointAtControl(ClassControl ctrl)
        {
            Map<ClassControl, Line[]> list = new HashMap<>();
            for (ClassControl key : _ClsLineProjection.keySet())
            {
                Map<ClassControl, Line[]> item = _ClsLineProjection.get(key);
                for (ClassControl clsCtrl : item.keySet())
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

        private void ResetLines(Line[] lines, ClassControl from, ClassControl to)
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

            length /= _ArrowLength;
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

        @Override
        public void handle(MouseEvent event)
        {
            if (event.getSource() instanceof ClassControl)
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
                ClassControl control = (ClassControl) event.getSource();
                control.setLayoutX(control.getLayoutX() + diffX);
                control.setLayoutY(control.getLayoutY() + diffY);

                Map<ClassControl, Line[]> lines = this.GetLinesPointAtControl(control);

                if (lines.isEmpty() == false)
                {
                    for (Map.Entry<ClassControl, Line[]> entrySet : lines.entrySet())
                    {
                        ClassControl key = entrySet.getKey();
                        Line[] value = entrySet.getValue();

                        this.ResetLines(value, control, key);
                    }
                }

                lines = _ClsLineProjection.get(control);
                if (lines.isEmpty() == false)
                {
                    for (Map.Entry<ClassControl, Line[]> entrySet : lines.entrySet())
                    {
                        ClassControl key = entrySet.getKey();
                        Line[] value = entrySet.getValue();
                        this.ResetLines(value, key, control);
                    }

                }

            }

        }

    }
}
