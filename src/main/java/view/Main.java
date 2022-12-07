package view;

import cms.util.maybe.NoMaybeValue;
import controller.Controller;
import controller.ControllerFactory;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class Main extends Application implements Initializable
{
    private WorldGenerator worldGenerator = new WorldGenerator();
    private Controller controller = ControllerFactory.getViewController();
    private String workingDir = System.getProperty("user.dir");
    private HBox parent;
    private ZoomPane zoomWorld;
    private FileChooser fileChooser = new FileChooser();
    private File critterFile;
    private String stepsText = "Steps: ";
    private String numCrittersText = "Alive Critters: ";
    private Stage critterLoadStage = new Stage();

    @FXML
    TextField rateText;
    @FXML
    Button step;
    @FXML
    ToggleButton advance;
    @FXML
    Text steps;
    @FXML
    Text numCritters;
    @FXML
    Button chooseWorld;
    @FXML
    Button chooseCritter;

    ScheduledService<Integer> controllerSVC = new ScheduledService<Integer>()
    {
        protected Task<Integer> createTask()
        {
            return new Task<>()
            {
                protected Integer call()
                {
                    controller.advanceTime(1);
                    return 0;
                }
            };
        }
    };

    ScheduledService<Integer> guiSVC = new ScheduledService<Integer>()
    {
        protected Task<Integer> createTask()
        {
            return new Task<>()
            {
                protected Integer call()
                {
                    Platform.runLater(() ->
                    {
                        updateActivePaneGUI();
                    });
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
        critterLoadStage.setTitle("Tile Selection");

        // load FXML file
        URL r = new File(workingDir + "\\src\\main\\java\\view\\window.fxml").toURI().toURL();
        FXMLLoader loader = new FXMLLoader(r);
        loader.setController(this);
        parent = loader.load();

        // put images on buttons
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

        // load world
//        controller.loadWorld("src\\test\\resources\\A5files\\view_world.txt", false, false);
        controller.newWorld();
        zoomWorld = worldGenerator.loadWorld(controller.getNumRows(), controller.getNumColumns(), controller.getReadOnlyWorld());
        parent.getChildren().add(zoomWorld);
        HBox.setHgrow(zoomWorld, Priority.ALWAYS);

        // set text for alive critters
        numCritters.setText(numCrittersText + controller.getNumberOfAliveCritters());

        // show window
        Scene scene = new Scene(parent, 960, 540);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void updateWorldGUI()
    {
        try
        {
            zoomWorld = worldGenerator.loadWorld(controller.getNumRows(), controller.getNumColumns(), controller.getReadOnlyWorld());
            parent.getChildren().remove(1);
            parent.getChildren().add(zoomWorld);
            HBox.setHgrow(zoomWorld, Priority.ALWAYS);
            steps.setText(stepsText + controller.getSteps());
            numCritters.setText(numCrittersText + controller.getNumberOfAliveCritters());
        }
        catch (NoMaybeValue ex)
        {
            throw new RuntimeException(ex);
        }
    }

    public void updateActivePaneGUI()
    {
        try
        {
//            StackPane world = worldGenerator.updateActivePane(controller.getReadOnlyWorld());
//            zoomWorld.changeTarget(world);

            BorderPane activePane = worldGenerator.updateActivePane(controller.getReadOnlyWorld());
            zoomWorld.changeTarget(activePane);
        }
        catch (NoMaybeValue ex)
        {
            throw new RuntimeException(ex);
        }
        parent.getChildren().remove(1);
        parent.getChildren().add(zoomWorld);
        HBox.setHgrow(zoomWorld, Priority.ALWAYS);

        steps.setText(stepsText + controller.getSteps());
        numCritters.setText(numCrittersText + controller.getNumberOfAliveCritters());
    }

    public void initialize(final URL location, final ResourceBundle resources)
    {
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
                    controllerSVC.setPeriod(Duration.seconds(1.0 / rate));

                    if (rate <= 30)
                    {
                        guiSVC.setPeriod(Duration.seconds(1.0 / rate));
                    }
                    else
                    {
                        guiSVC.setPeriod(Duration.seconds(1.0 / 30.0));
                    }
                    controllerSVC.restart();
                    guiSVC.restart();
                }
                else
                {
                    controllerSVC.cancel();
                    guiSVC.cancel();
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
            critterFile = fileChooser.showOpenDialog(new Stage());
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
//                        if (n == 1)
//                        {
//                            URL s = new File(workingDir + "\\src\\main\\java\\view\\addCritter.fxml").toURI().toURL();
//                            FXMLLoader addCritterLoader = new FXMLLoader(s);
//                            addCritterLoader.setController(this);
//                            VBox node = addCritterLoader.load();
//
//                            Scene scene = new Scene(node, 220, 186);
//                            critterLoadStage.setScene(scene);
//                            critterLoadStage.show();
//                        }
//                        else
//                        {
//                            controller.loadCritters(critterFile.getAbsolutePath(), n);
//                        }
                        updateActivePaneGUI();
                    }
                    catch (NumberFormatException exception)
                    {
                        // TODO change
                        System.out.println("please enter a valid integer");
                    }
//                    catch (IOException ex)
//                    {
//                        throw new RuntimeException(ex);
//                    }
                });
            }
        });

//        critterOK.setOnAction(event ->
//        {
//            int column = !colText.getText().equals("") ? Integer.parseInt(colText.getText()) :
//                    (int)(Math.random() * controller.getNumColumns());
//            int row = !rowText.getText().equals("") ? Integer.parseInt(rowText.getText()) :
//                    (int)(Math.random() * controller.getNumRows());
//            int dir = !dirText.getText().equals("") ? Integer.parseInt(dirText.getText()) :
//                    (int)(Math.random() * 6);
//            controller.addCritter(critterFile.getAbsolutePath(), row, column, dir);
//            critterLoadStage.hide();
//        });
//
//        critterCancel.setOnAction(event ->
//        {
//            critterLoadStage.hide();
//        });
    }
}
