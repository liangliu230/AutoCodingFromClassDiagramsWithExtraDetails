Readme

Project name: Automatic Coding from Class Diagrams with Extra Details

Author: Liang Liu

Current Version: Concept Demo 1

1. Disclaim

This software is licensed under MIT license.

This software is only used to demonstrate my idea on creating computer programs in complete graphical user interface which using class diagrams of UML to represent the project organization and using flow chart to represent the method body. 

This software is not complete. I completed two parts: user interface and model structures. I did not connect these two parts. It is because I think there are better ways to do both parts. However, I believe my intention can be represented in current version.

2. Project Purpose

UML has been created for several decades, flow chart has been used much longer than UML. However, programmers still create computer programs in plain text environment. Modern IDEs give better and more convenient text interface. However, it is not visually direct. We cannot have runnable program from UML designs. That is why we can still hear UML is not convenient or we only draw concepts with UMLs. They are right in some sense. 

The aim of this project is to create a computer program that can create complete computer program from its design. That is the program accepts class diagrams + flow charts as inputs and gives actual computer programs as a translation of the input.

Note: the class diagrams and flow charts are not necessary the exact form in UML or original flow chart design, as the main target is to generate code through them. So only their concept will be used and their form are not considered as important. 

3. Software Worth Mention

One software is notable. It is Drakon. The details of it are on Wikipedia. The reason I put it here is because if the Russians can put OO concepts in it, there will be no this project. The other software I want to mention is jGrasp. This project is on the opposite direction of it. In my understanding, jGrasp generate class diagrams and flow charts from source files (and it has many other visual aids on text input), and this project attempts to generate source files from class diagrams and flow charts.

4. A Simple Description

In current version, Buttons are used to represent class diagrams (on main interface after create a new project) and flow chart nodes (in flow chart dialog interface, accessible from the button with name “create method body” in method creation dialog). 

A class diagram node can be created through right mouse button menu item "Add class object" at the tree view section on the left of the main interface, it can be modified through class modify dialog by click on it (it is a button control). 

Flow charts interface can be created in the process of creating a method. There are three flow chart node types: sequential, branch and loop. Two nodes are created at the beginning of the flow chart interface, they are start node, which marks the beginning of the flow chart, and the end node, which marks the end of the flow chart. Every flow chart nodes have unique labels, and can be connected to other nodes by choosing link to labels in node modification dialog (a click on the node). Each flow chart node have at least one link to label (except end node). Sequential and loop nodes have only one link to label, branch nodes have two link to labels (true branch and false branch, switch is not implemented). In this project, for, for each, while, and do-while are in the loop type. On their creation, there will be two nodes created on the interface. One for the loop condition declaration and one for marking its end (this is just like the Drakon). If, switch are considered as branch nodes. As sequential nodes, there will be only one flow chart nodes created. Flow chart nodes are created by click three buttons on the interface. Sequence button creates sequential nodes, branch button creates branch nodes and loop button create loop nodes.

Other functions like save and load is not implemented. Translate is implemented but not useable as no method body is savable from flow chart created. 

5. Working Environment

The GUI is created with JavaFX. The JavaSE version used is 8u66. 
