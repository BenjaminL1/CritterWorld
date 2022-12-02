package view;

import cms.util.maybe.Maybe;
import cms.util.maybe.NoMaybeValue;
import controller.Controller;
import controller.ControllerFactory;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import model.ControlOnlyWorld;
import model.ReadOnlyCritter;
import model.ReadOnlyWorld;
import model.Tile;
import java.util.Hashtable;
import java.util.List;

public class Main extends Application
{
    private Controller controller = ControllerFactory.getConsoleController();
    private StackPane world;
    private Hashtable<String, Color> speciesColor = new Hashtable<>();
    private int numRows;
    private int numColumns;

    private final double HEIGHT = 43.3013;

    private final double BOTTOM_LEFT_X_START = 17.5;
    private final double BOTTOM_LEFT_Y_START = 70.3013;
    private final double BOTTOM_RIGHT_X_START = 42.5;
    private final double BOTTOM_RIGHT_Y_START = 70.3013;
    private final double CENTER_RIGHT_X_START = 55;
    private final double CENTER_RIGHT_Y_START = 48.65065;
    private final double TOP_RIGHT_X_START = 42.5;
    private final double TOP_RIGHT_Y_START = 27;
    private final double TOP_LEFT_X_START = 17.5;
    private final double TOP_LEFT_Y_START = 27;
    private final double CENTER_LEFT_X_START = 5;
    private final double CENTER_LEFT_Y_START = 48.65065;

    private final double HEX_CENTER_X = (CENTER_LEFT_X_START + CENTER_RIGHT_X_START) / 2;
    private final double HEX_CENTER_Y = (CENTER_LEFT_Y_START + CENTER_RIGHT_Y_START) / 2;

    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        primaryStage.setTitle("CritterWorld");

        // use view controller to get info on world
        // -numRows and numColumns
        // -Tile[][] representation of world

//        ZoomableScrollPane zoomWorldPane = loadWorld();
        ZoomableScrollPane zoomWorldPane = loadWorld("src\\test\\resources\\A5files\\view_world.txt");
        Scene scene = new Scene(zoomWorldPane, 960, 540);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public ZoomableScrollPane loadWorld() throws NoMaybeValue
    {
        controller.newWorld();

        numRows = controller.getNumRows();
        numColumns = controller.getNumColumns();

        Group hexes = getHexGroup(); // replace with
//        BorderPane hexes = getHexGroup();
        Group activePane = getActivePane();
//        BorderPane activePane = getActivePane();

        world = new StackPane(hexes, activePane);

        return new ZoomableScrollPane(world, Math.max(numRows, numColumns));
    }

    public ZoomableScrollPane loadWorld(String filename) throws NoMaybeValue
    {
        controller.loadWorld(filename, false,false); // TODO change booleans maybe?

        numRows = controller.getNumRows();
        numColumns = controller.getNumColumns();

        Group hexes = getHexGroup(); // replace with
//        BorderPane hexes = getHexGroup();
        Group activePane = getActivePane();
//        BorderPane activePane = getActivePane();

        world = new StackPane(hexes, activePane);

        return new ZoomableScrollPane(world, Math.max(numRows, numColumns));
    }

    public Group getHexGroup()
    {
        Group hexes = new Group();
//        BorderPane hexes = new BorderPane();

        double blX = BOTTOM_LEFT_X_START;
//        double blY = 43.3013;
        double brX = BOTTOM_RIGHT_X_START;
//        double brY = 43.3013;
        double crX = CENTER_RIGHT_X_START;
//        double crY = 21.65065;
        double trX = TOP_RIGHT_X_START;
//        double trY = 0;
        double tlX = TOP_LEFT_X_START;
//        double tlY = 0;
        double clX = CENTER_LEFT_X_START;
//        double clY = 21.65065;
//        final double width = 50;

        for (int i = 0; i < numColumns; i++) {
            if (i > 0) {
                blX += 37.5;
                brX += 37.5;
                crX += 37.5;
                trX += 37.5;
                tlX += 37.5;
                clX += 37.5;
            }

            double blY;
            double brY;
            double crY;
            double trY;
            double tlY;
            double clY;

            if (numRows % 2 == 0) {
                blY = i % 2 == 0 ? BOTTOM_LEFT_Y_START : BOTTOM_LEFT_Y_START - HEIGHT / 2;
                brY = i % 2 == 0 ? BOTTOM_RIGHT_Y_START : BOTTOM_RIGHT_Y_START - HEIGHT / 2;
                crY = i % 2 == 0 ? CENTER_RIGHT_Y_START : CENTER_RIGHT_Y_START - HEIGHT / 2;
                trY = i % 2 == 0 ? TOP_RIGHT_Y_START : TOP_RIGHT_Y_START - HEIGHT / 2;
                tlY = i % 2 == 0 ? TOP_LEFT_Y_START : TOP_LEFT_Y_START - HEIGHT / 2;
                clY = i % 2 == 0 ? CENTER_LEFT_Y_START : CENTER_LEFT_Y_START - HEIGHT / 2;
            } else {
                blY = i % 2 == 0 ? BOTTOM_LEFT_Y_START : BOTTOM_LEFT_Y_START + HEIGHT / 2;
                brY = i % 2 == 0 ? BOTTOM_RIGHT_Y_START : BOTTOM_RIGHT_Y_START + HEIGHT / 2;
                crY = i % 2 == 0 ? CENTER_RIGHT_Y_START : CENTER_RIGHT_Y_START + HEIGHT / 2;
                trY = i % 2 == 0 ? TOP_RIGHT_Y_START : TOP_RIGHT_Y_START + HEIGHT / 2;
                tlY = i % 2 == 0 ? TOP_LEFT_Y_START : TOP_LEFT_Y_START + HEIGHT / 2;
                clY = i % 2 == 0 ? CENTER_LEFT_Y_START : CENTER_LEFT_Y_START + HEIGHT / 2;
            }

            int limit = i % 2 == 0 ? (int) Math.ceil(numRows / 2.0) : numRows / 2;

            for (int j = 0; j < limit; j++) {
                Polygon polygon = new Polygon(
                        blX, blY,  // bottom left
                        brX, brY,         // bottom right
                        crX, crY,          // center right
                        trX, trY,               // top right
                        tlX, tlY,               // top left
                        clX, clY);          // center left

                polygon.setStroke(Color.BLACK);
                polygon.setStrokeWidth(2);
                polygon.setFill(Color.TRANSPARENT);

                hexes.getChildren().add(polygon);

                blY += HEIGHT;
                brY += HEIGHT;
                crY += HEIGHT;
                trY += HEIGHT;
                tlY += HEIGHT;
                clY += HEIGHT;
            }
        }

        return hexes;
    }

    public Group getActivePane() throws NoMaybeValue
    {
        Group activePane = new Group();
//        BorderPane activePane = new BorderPane();

        double centerX = HEX_CENTER_X;
        System.out.println();

        for (int c = 0; c < numColumns; c++)
        {
            centerX = c > 0 ? centerX + 37.5 : centerX;
            double centerY;
            if (numRows % 2 == 0)
            {
                centerY = c % 2 == 0 ? HEX_CENTER_Y : HEX_CENTER_Y - HEIGHT / 2;
            }
            else
            {
                centerY = c % 2 == 0 ? HEX_CENTER_Y : HEX_CENTER_Y + HEIGHT / 2;
            }

            int start;
            if (numRows % 2 == 0)
            {
                start = c % 2 == 1 ? 0 : 1;
            }
            else
            {
                start = c % 2 == 0 ? 0 : 1;
            }
            for (int r = start; r < numRows; r += 2)
            {
                System.out.print(c + " " + (numRows - 1 - r));
                System.out.print("     ");
                System.out.println(centerX + " " + centerY);
                System.out.println();
                ReadOnlyWorld readWorld = controller.getReadOnlyWorld();
                int info = readWorld.getTerrainInfo(c, numRows - 1 - r);
                // is critter
                if (info > 0)
                {
                    ReadOnlyCritter critter = readWorld.getReadOnlyCritter(c, numRows - 1 - r).get();
                    int dir = critter.getDirection();
                    String species = critter.getSpecies();
                    Color color = speciesColor.get(species);
                    if (color == null)
                    {
                        color = Color.color(Math.random(), Math.random(), Math.random());
                        speciesColor.put(species, color);
                    }
                    // TODO draw critter with species color and rotate to correct direction
                    Circle body = new Circle(10, color);
                    body.setLayoutX(centerX);
                    body.setLayoutY(centerY);
//                    body.setLayoutX(centerX + 12);
//                    body.setLayoutY(centerY + 12);
                    Circle head = new Circle(4, Color.BLACK);
                    head.setLayoutX(centerX);
                    head.setLayoutY(centerY - 13);
//                    head.setLayoutX(centerX + 12);
//                    head.setLayoutY(centerY - 2);
                    activePane.getChildren().add(body);
                    activePane.getChildren().add(head);
                }
                // is rock
                else if (info == -1)
                {
                    Image rockImage = new Image("C:\\Users\\rhlin\\gitStuffs\\cornellEnterprise" +
                            "\\axz5-bbl35-rl659-critterworld\\src\\main\\java\\model\\sprites\\rock.png",
                            30, 30, true, false);
                    ImageView rock = new ImageView(rockImage);
                    rock.setLayoutX(centerX);
                    rock.setLayoutY(centerY);
                    activePane.getChildren().add(rock);
                }
                // is food
                else if (info < -1)
                {
                    Image foodImage = new Image("C:\\Users\\rhlin\\gitStuffs\\cornellEnterprise" +
                            "\\axz5-bbl35-rl659-critterworld\\src\\main\\java\\model\\sprites\\razzBerry.png",
                            23, 23, true, false);
                    ImageView food = new ImageView(foodImage);
                    food.setLayoutX(centerX);
                    food.setLayoutY(centerY);
                    activePane.getChildren().add(food);
                }
                // TODO REMOVE AFTER TESTING
                // FOR TESTING
                else
                {
                    Image foodImage = new Image("C:\\Users\\rhlin\\gitStuffs\\cornellEnterprise" +
                            "\\axz5-bbl35-rl659-critterworld\\src\\main\\java\\model\\sprites\\razzBerry.png",
                            23, 23, true, false);
                    ImageView food = new ImageView(foodImage);
                    food.setLayoutX(centerX);
                    food.setLayoutY(centerY);
                    activePane.getChildren().add(food);
                }
                centerY += HEIGHT;
            }
        }

        return activePane;
    }
}
