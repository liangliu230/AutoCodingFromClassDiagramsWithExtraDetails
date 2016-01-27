/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package accdedmilestone1;

import Controller.Controller;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Liu
 */
public class AutoCodingFromClassDiagramsWithExtraDetails extends Application
{
    
    @Override
    public void start(Stage primaryStage)
    {
        Controller controller = new Controller();
        
        Scene scene = controller.GetMainScene();
        
        primaryStage.setTitle("Mile Stone 1 -- Working Prototype");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        launch(args);
    }
    
}
