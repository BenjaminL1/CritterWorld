package view;

import cms.util.maybe.NoMaybeValue;
import controller.Controller;
import controller.ControllerFactory;
import exceptions.SyntaxError;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Main extends Application implements Initializable
{
    private WorldGenerator worldGenerator = new WorldGenerator();
    private Controller controller = ControllerFactory.getViewController();
    private String workingDir = System.getProperty("user.dir");
    private HBox parent;
    private ZoomableScrollPane zoomWorld;
    private FileChooser fileChooser = new FileChooser();

    @FXML
    TextField rateText;
    @FXML
    Button step;
    @FXML
    ToggleButton advance;
    @FXML
    Button chooseWorld;
    @FXML
    Button chooseCritter;


    // TODO make two scheduled services, on for display and one for backend
    ScheduledService<Integer> svc = new ScheduledService<Integer>()
    {
        protected Task<Integer> createTask()
        {
            return new Task<>()
            {
                protected Integer call()
                {
                    controller.advanceTime(1);
                    Platform.runLater(() ->
                    {
                        updateActivePaneGUI();
                    });
//                    updateActivePaneGUI();
                    return 0;
                }
            };
        }
    };


    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));

        primaryStage.setTitle("Critter World");

        URL r = new File(workingDir + "\\src\\main\\java\\view\\window.fxml").toURI().toURL();
        FXMLLoader loader = new FXMLLoader(r);
        loader.setController(this);
        parent = loader.load();

        Image playButtonImage = new Image(workingDir + "\\src\\main\\java\\view\\sprites\\playButton.png",
                23, 23, true, false);
        Image fastForwardImage = new Image(workingDir + "\\src\\main\\java\\view\\sprites\\fastForward.png",
                23, 23, true, false);
        Image dragoniteCritterImage = new Image(workingDir + "\\src\\main\\java\\view\\sprites\\dragonite.png",
            30, 30, true, false);
        Image paldeaImage = new Image(workingDir + "\\src\\main\\java\\view\\sprites\\paldeaMap.jpg",
                35, 35, true, false);
        ImageView playButton = new ImageView(playButtonImage);
        ImageView fastForward = new ImageView(fastForwardImage);
        ImageView dragonite = new ImageView(dragoniteCritterImage);
        ImageView paldea = new ImageView(paldeaImage);
        step.setGraphic(playButton);
        advance.setGraphic(fastForward);
        chooseCritter.setGraphic(dragonite);
        chooseWorld.setGraphic(paldea);

        controller.loadWorld("src\\test\\resources\\A5files\\view_world.txt", false, false);
//        controller.loadWorld("src\\test\\resources\\A5files\\big_world.txt", false, false);

        zoomWorld = worldGenerator.loadWorld(controller.getNumRows(), controller.getNumColumns(), controller.getReadOnlyWorld());

        parent.getChildren().add(zoomWorld);
        HBox.setHgrow(zoomWorld, Priority.ALWAYS);

        Scene scene = new Scene(parent, 960, 540);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void updateWorldGUI()
    {
        try
        {
            zoomWorld = worldGenerator.loadWorld(controller.getNumRows(), controller.getNumColumns(), controller.getReadOnlyWorld());
        }
        catch (NoMaybeValue ex)
        {
            throw new RuntimeException(ex);
        }
        parent.getChildren().remove(1);
        parent.getChildren().add(zoomWorld);
        HBox.setHgrow(zoomWorld, Priority.ALWAYS);
    }

    public void updateActivePaneGUI()
    {
        try
        {
//            zoomWorld = worldGenerator.updateActivePane(controller.getReadOnlyWorld());
            StackPane world = worldGenerator.updateActivePane(controller.getReadOnlyWorld());
            zoomWorld.changeTarget(world);
        }
        catch (NoMaybeValue ex)
        {
            throw new RuntimeException(ex);
        }
        parent.getChildren().remove(1);
        parent.getChildren().add(zoomWorld);
        HBox.setHgrow(zoomWorld, Priority.ALWAYS);
    }

    public void initialize(final URL location, final ResourceBundle resources)
    {
//        step.setOnAction(e ->
//        {
//            controller.advanceTime(1);
//
//            Platform.runLater(() ->
//            {
//                updateActivePaneGUI();
//            });
//        });

        // step
        step.setOnAction(e ->
        {
            controller.advanceTime(1);
            updateActivePaneGUI();
        });

        // advance
        advance.setOnAction(e ->
        {
            String rateStr = rateText.getText();
            double rate;
            try
            {
                rate = Double.parseDouble(rateStr);
            }
            catch (NumberFormatException exception)
            {
                rate = 1.0;
                rateText.setText("1.0");
            }
            if (rate != 0.0)
            {
                if (advance.isSelected())
                {
                    svc.setPeriod(Duration.seconds(1.0 / rate));
                    svc.restart();
                }
                else
                {
                    svc.cancel();
                }
            }
        });

        // chooseWorld
        chooseWorld.setOnAction(e ->
        {
            File worldFile = fileChooser.showOpenDialog(new Stage());
            if (worldFile == null)
            {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setContentText("Would you like to create a default world?");

                alert.showAndWait().ifPresent(response ->
                {
                    if (response == ButtonType.OK)
                    {
                        controller.newWorld();
                        // TODO Platform.runLater?
                        updateWorldGUI();
                    }
                });
            }
            else
            {
                controller.loadWorld(worldFile.getAbsolutePath(), false, false);
                updateWorldGUI();
            }
        });

        // chooseCritter
        chooseCritter.setOnAction(e ->
        {
            File critterFile = fileChooser.showOpenDialog(new Stage());
            if (critterFile != null)
            {
                TextInputDialog td = new TextInputDialog("0");
                td.setHeaderText("How many critters would you like to place randomly into the world?");
                td.showAndWait().ifPresent(response ->
                {
                    try
                    {
                        int n = Integer.parseInt(response);
                        controller.loadCritters(critterFile.getAbsolutePath(), n);
                        updateActivePaneGUI();
                    }
                    catch (NumberFormatException exception)
                    {
                        // TODO change
                        System.out.println("please enter a valid integer");
                    }
                });
            }
        });
    }
}
