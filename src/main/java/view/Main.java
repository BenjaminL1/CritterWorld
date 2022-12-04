package view;

import cms.util.maybe.NoMaybeValue;
import controller.Controller;
import controller.ControllerFactory;
import controller.ViewController;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import model.ReadOnlyCritter;
import model.ReadOnlyWorld;
import model.World;

import java.io.File;
import java.net.URL;
import java.util.Hashtable;
import java.util.ResourceBundle;
import java.util.Stack;

public class Main extends Application implements Initializable
{
    @FXML
    Button step;
//    private Controller controller = ControllerFactory.getConsoleController();
    private WorldGenerator worldGenerator = new WorldGenerator();
    private Controller controller = ControllerFactory.getViewController();
    private String workingDir = System.getProperty("user.dir");

    private HBox parent;
    private ZoomableScrollPane zoomWorld;

    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception
    {
//        System.out.println(this);
        //        parent = controller.loadGUI("src\\test\\resources\\A5files\\view_world.txt");
        primaryStage.setTitle("Critter World");

        URL r = new File(workingDir + "\\src\\main\\java\\view\\window.fxml").toURI().toURL();
//        parent = FXMLLoader.load(r);
        FXMLLoader loader = new FXMLLoader(r);
        loader.setController(this);
        parent = loader.load();

//        controller.loadWorld("src\\test\\resources\\A5files\\view_world.txt", false, false);
        controller.loadWorld("src\\test\\resources\\A5files\\big_world.txt", false, false);

        zoomWorld = worldGenerator.loadWorld(controller.getNumRows(), controller.getNumColumns(), controller.getReadOnlyWorld());

        parent.getChildren().add(zoomWorld);
        HBox.setHgrow(zoomWorld, Priority.ALWAYS);

        Scene scene = new Scene(parent, 960, 540);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void initialize(final URL location, final ResourceBundle resources)
    {
        step.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent e)
            {
                controller.advanceTime(1);
                try
                {
//                    zoomWorld = worldGenerator.step(controller.getReadOnlyWorld());
                    StackPane world = worldGenerator.step(controller.getReadOnlyWorld());
                    zoomWorld.changeTarget(world);
                } catch (NoMaybeValue ex) {
                    throw new RuntimeException(ex);
                }
                parent.getChildren().remove(1);
                parent.getChildren().add(zoomWorld);
                HBox.setHgrow(zoomWorld, Priority.ALWAYS);
            }
        });
    }
}
