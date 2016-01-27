Readme

Project name: Automatic Coding from Class Diagrams with Extra Details

Author: Liang Liu

Current Version: Working Prototype 1

1. Declaration

This software is licensed under MIT license.

This software is only used to demonstrate my idea on creating computer programs in complete graphical user interface which using class diagrams of UML to represent the project organization and using flow chart to represent the method body. 

This software is in working prototype statues. The basic functions are all workings. The user can create, save and load projects, and can translate current project into java. However, the author did not implement some of the important functions like delete classes or flow chart nodes. There are also user experience and performance problems exists.

2. Project Purpose

UML has been created for several decades, flow chart has been used much longer than UML. However, programmers still create computer programs in plain text environment. Modern IDEs give better and more convenient text interface. However, it is not visually direct. We cannot have runnable program from UML designs. That is why we can still hear UML is not convenient or we only draw concepts with UMLs. They are right in some sense. 

The aim of this project is to create a computer program that can create complete computer program from its design. That is the program accepts class diagrams + flow charts as inputs and gives actual computer programs as a translation of the input.

Note: the class diagrams and flow charts are not necessary the exact form in UML or original flow chart design, as the main target is to generate code through them. So only their concept will be used and their form are not considered as important. 

3. Software Worth Mention

One software is notable. It is Drakon. The details of it are on Wikipedia. The reason I put it here is because if the Russians can put OO concepts in it, there will be no this project. The other software I want to mention is jGrasp. This project is on the opposite direction of it. In my understanding, jGrasp generate class diagrams and flow charts from source files (and it has many other visual aids on text input), and this project attempts to generate source files from class diagrams and flow charts.

4. A Simple Description

There are four buttons on the toolbar. New project is used to create new project, save project save the current displaying project into the file selected by the user. The file format is in xml, and only this format is accepted. Load project load saved project into main screen. Translate project translate the displaying project in to java with default folder "./test/".

After created a new project or loaded a project, the project/package/class name can be modified at the left tree view. Additional package and class can be added through context menu by right click on desired tree item.

After a class object(can be interface, enum and class) is added, there will be a new button displayed on the area at right to the tree view. Left click the button will bring up the class dialog. In which, all information about this class object can be modified. Click the add button in field list section will add new fields to the class object. Method can be added though the same way. 

Method body of a method can be added through the method dialog. This dialog is opened by click the method button in the method list. In this dialog, there is a button named “Create method body” at the button. Click it will bring up the flow chart dialog. In the flow chart dialog, the method body can be created and be saved.

The organization of the interface can be seen below:

Main Interface
|
- - Tool Bar – New project
             - Save project
             - Load project
             - Translate project
|
- - Tree View – Create and change project/package/class object names
|
- - Main Pane 
     |– Class objects displayed as buttons
        |
        - - Class dialog – input information about current class
             |
             - - Field dialog – input information about current field
             |
             - - Method dialog – input information about current method
                  |
                  - - Flow Chart dialog
                       |
                       - - Flow Chart Node dialog

In the current version, flow chart nodes are categorized by whether its translated line need brackets to keep the correct operate of the translate program. There are four types of nodes, 1. Start and end nodes of flow chart. They represent the start point and end point of a flow chart. They will not appear in final translated program. 2. Sequence nodes. These nodes can be carried sequentially and don’t need brackets in translated programs. 3. Branch nodes. They need brackets to denote its boundary. This type of nodes includes: if, switch and try. 4. Loop nodes. Unlike branch nodes, loop nodes are appeared in pairs. The first node represents the loop conditions and the start of the loop, and the second node represents the end of the loop. This design is used because unlike branch nodes, the end of a loop is not obvious, that is, in the graph, the end of the loop only have one in edge, which cannot denote the loop is end. Unlike the situation in branch nodes where two or more in edges can be found in the node after the branch block. For example, the if node will always be a node which will receive its true clause branch and false clause branch. The same design can also be found in Drakon.
            
Function content in flow chart node dialog is a reused name. In this project, some key words like if, for, switch are seen as functions. Therefore, function content is used to represent their inputs like normal function calls. Some of the key words like break, continue and default do not need inputs. Other the type for normal lines which usually consist of output type, output variable name, and the function used (include the function inputs). This type is considered as sequence type of node.

On the first running, a file named “java.xml” will be created at “./”. This file stores information of rt.jar (a part, packages started with java and javax) and jfxrt.jar and is used for hint generation. The generation of this file will require one or two minutes.

At each new project creation or project loading, the hint generator needs to be initialized. This process will read “java.xml” and will cost about one minute.

5. Working Environment

The GUI is created with JavaFX. The JavaSE version used is 8u66. 

 
