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
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import model.ControlOnlyWorld;
import model.ReadOnlyCritter;
import model.ReadOnlyWorld;
import model.Tile;

import java.awt.*;
import java.util.Hashtable;
import java.util.List;

public class Main extends Application
{
    private Controller controller = ControllerFactory.getConsoleController();
    private StackPane world;
//    private Group world = new Group();
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

//        Group hexes = getHexGroup();
        BorderPane hexes = getHexGroup();
//        Group activePane = getActivePane();
        BorderPane activePane = getActivePane();

        world = new StackPane(hexes, activePane);

        return new ZoomableScrollPane(world, Math.max(numRows, numColumns));
    }

    public ZoomableScrollPane loadWorld(String filename) throws NoMaybeValue
    {
        controller.loadWorld(filename, false,false); // TODO change booleans maybe?

        numRows = controller.getNumRows();
        numColumns = controller.getNumColumns();

//        Group hexes = getHexGroup();
        BorderPane hexes = getHexGroup();
//        Group activePane = getActivePane();
        BorderPane activePane = getActivePane();

        world = new StackPane(hexes, activePane);

        return new ZoomableScrollPane(world, Math.max(numRows, numColumns));
    }

    public BorderPane getHexGroup()
    {
//        Group hexes = new Group();
//        Group hexes = world;
        BorderPane hexes = new BorderPane();

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

            if (numRows % 2 == 0)
            {
                blY = i % 2 == 0 ? BOTTOM_LEFT_Y_START : BOTTOM_LEFT_Y_START - HEIGHT / 2;
                brY = i % 2 == 0 ? BOTTOM_RIGHT_Y_START : BOTTOM_RIGHT_Y_START - HEIGHT / 2;
                crY = i % 2 == 0 ? CENTER_RIGHT_Y_START : CENTER_RIGHT_Y_START - HEIGHT / 2;
                trY = i % 2 == 0 ? TOP_RIGHT_Y_START : TOP_RIGHT_Y_START - HEIGHT / 2;
                tlY = i % 2 == 0 ? TOP_LEFT_Y_START : TOP_LEFT_Y_START - HEIGHT / 2;
                clY = i % 2 == 0 ? CENTER_LEFT_Y_START : CENTER_LEFT_Y_START - HEIGHT / 2;
            }
            else
            {
                blY = i % 2 == 0 ? BOTTOM_LEFT_Y_START : BOTTOM_LEFT_Y_START + HEIGHT / 2;
                brY = i % 2 == 0 ? BOTTOM_RIGHT_Y_START : BOTTOM_RIGHT_Y_START + HEIGHT / 2;
                crY = i % 2 == 0 ? CENTER_RIGHT_Y_START : CENTER_RIGHT_Y_START + HEIGHT / 2;
                trY = i % 2 == 0 ? TOP_RIGHT_Y_START : TOP_RIGHT_Y_START + HEIGHT / 2;
                tlY = i % 2 == 0 ? TOP_LEFT_Y_START : TOP_LEFT_Y_START + HEIGHT / 2;
                clY = i % 2 == 0 ? CENTER_LEFT_Y_START : CENTER_LEFT_Y_START + HEIGHT / 2;
            }

            int limit = i % 2 == 0 ? (int) Math.ceil(numRows / 2.0) : numRows / 2;

            for (int j = 0; j < limit; j++)
            {
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

    public BorderPane getActivePane() throws NoMaybeValue
    {
//        Group activePane = new Group();
//        Group activePane = world;

        BorderPane activePane = new BorderPane();

        double centerX = HEX_CENTER_X;

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
//                System.out.print(c + " " + (numRows - 1 - r));
//                System.out.print("     ");
//                System.out.println(centerX + " " + centerY);
//                System.out.println();
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
//                    body.setLayoutX(centerX);
//                    body.setLayoutY(centerY);
                    body.setLayoutX(centerX);
                    body.setLayoutY(centerY + 3);


                    Circle head = new Circle(4, Color.BLACK);
//                    head.setLayoutX(centerX);
//                    head.setLayoutY(centerY - 13);
                    head.setLayoutX(centerX);
                    head.setLayoutY(centerY - 8);

                    Line leg1 = new Line();
                    leg1.setStrokeWidth(1.5);
                    double startX1 = Math.cos((3 * Math.PI) / 4) * 10;
                    double startY1 = Math.sin((3 * Math.PI) / 4) * 10;
                    leg1.setStartX(centerX + startX1);
                    leg1.setEndX(centerX + startX1 * 1.2);
                    leg1.setStartY(centerY + 3 - startY1);
                    leg1.setEndY(centerY + 3 - startY1 * 1.2);

                    Line leg2 = new Line();
                    leg2.setStrokeWidth(1.5);
                    leg2.setStartX(centerX - 10);
                    leg2.setEndX(centerX - 10 - 2);
                    leg2.setStartY(centerY + 3);
                    leg2.setEndY(centerY + 3);

                    Line leg3 = new Line();
                    leg3.setStrokeWidth(1.5);
                    double startX3 = Math.cos((5 * Math.PI) / 4) * 10;
                    double startY3 = Math.sin((5 * Math.PI) / 4) * 10;
                    leg3.setStartX(centerX + startX3);
                    leg3.setEndX(centerX + startX3 * 1.2);
                    leg3.setStartY(centerY + 3 - startY3);
                    leg3.setEndY(centerY + 3 - startY3 * 1.2);

                    Line leg4 = new Line();
                    leg4.setStrokeWidth(1.5);
                    double startX4 = Math.cos(Math.PI / 4) * 10;
                    double startY4 = Math.sin(Math.PI / 4) * 10;
                    leg4.setStartX(centerX + startX4);
                    leg4.setEndX(centerX + startX4 * 1.2);
                    leg4.setStartY(centerY + 3 - startY4);
                    leg4.setEndY(centerY + 3 - startY4 * 1.2);

                    Line leg5 = new Line();
                    leg5.setStrokeWidth(1.5);
                    leg5.setStartX(centerX + 10);
                    leg5.setEndX(centerX + 10 + 2);
                    leg5.setStartY(centerY + 3);
                    leg5.setEndY(centerY + 3);

                    Line leg6 = new Line();
                    leg6.setStrokeWidth(1.5);
                    double startX6 = Math.cos((7 * Math.PI) / 4) * 10;
                    double startY6 = Math.sin((7 * Math.PI) / 4) * 10;
                    leg6.setStartX(centerX + startX6);
                    leg6.setEndX(centerX + startX6 * 1.2);
                    leg6.setStartY(centerY + 3 - startY6);
                    leg6.setEndY(centerY + 3 - startY6 * 1.2);

                    Group critterGroup = new Group();
                    critterGroup.getChildren().addAll(head, body, leg1, leg2, leg3, leg4, leg5, leg6);


                    Rotate rotate = new Rotate();

                    rotate.setPivotX(centerX);
                    rotate.setPivotY(centerY);

                    System.out.println(centerX + " " + centerY);
                    System.out.println();


                    if (dir == 1)
                    {
                        rotate.setAngle(60);
                    }
                    else if (dir == 2)
                    {
                        rotate.setAngle(120);
                    }
                    else if (dir == 3)
                    {
                        rotate.setAngle(180);
                    }
                    else if (dir == 4)
                    {
                        rotate.setAngle(240);
                    }
                    else if (dir == 5)
                    {
                        rotate.setAngle(300);
                    }

//                    body.getTransforms().add(rotate);
//                    head.getTransforms().add(rotate);
//                    leg1.getTransforms().add(rotate);
//                    leg2.getTransforms().add(rotate);
//                    leg3.getTransforms().add(rotate);
//                    leg4.getTransforms().add(rotate);
//                    leg5.getTransforms().add(rotate);
//                    leg6.getTransforms().add(rotate);

//                    activePane.getChildren().add(body);
//                    activePane.getChildren().add(head);
//                    activePane.getChildren().add(leg1);
//                    activePane.getChildren().add(leg2);
//                    activePane.getChildren().add(leg3);
//                    activePane.getChildren().add(leg4);
//                    activePane.getChildren().add(leg5);
//                    activePane.getChildren().add(leg6);
                    critterGroup.getTransforms().add(rotate);
                    activePane.getChildren().add(critterGroup);
                }
                // is rock
                else if (info == -1)
                {
                    Image rockImage = new Image("C:\\Users\\rhlin\\gitStuffs\\cornellEnterprise" +
                            "\\axz5-bbl35-rl659-critterworld\\src\\main\\java\\model\\sprites\\rock.png",
                            30, 30, true, false);
                    ImageView rock = new ImageView(rockImage);
                    double imageWidth = rockImage.getWidth();
                    double imageHeight = rockImage.getHeight();
                    rock.setLayoutX(centerX - imageWidth / 2);
                    rock.setLayoutY(centerY - imageHeight / 2);
                    activePane.getChildren().add(rock);
                }
                // is food
                else if (info < -1)
                {
                    Image foodImage = new Image("C:\\Users\\rhlin\\gitStuffs\\cornellEnterprise" +
                            "\\axz5-bbl35-rl659-critterworld\\src\\main\\java\\model\\sprites\\razzBerry.png",
                            23, 23, true, false);
                    ImageView food = new ImageView(foodImage);
                    double imageWidth = foodImage.getWidth();
                    double imageHeight = foodImage.getHeight();
                    food.setLayoutX(centerX - imageWidth / 2);
                    food.setLayoutY(centerY - imageHeight / 2);
                    activePane.getChildren().add(food);
                }
                // TODO REMOVE AFTER TESTING
                // FOR TESTING
                else
                {
//                    Image foodImage = new Image("C:\\Users\\rhlin\\gitStuffs\\cornellEnterprise" +
//                            "\\axz5-bbl35-rl659-critterworld\\src\\main\\java\\model\\sprites\\razzBerry.png",
//                            23, 23, true, false);
//                    ImageView food = new ImageView(foodImage);
//                    double imageWidth = foodImage.getWidth();
//                    double imageHeight = foodImage.getHeight();
//                    System.out.println(imageHeight);
//                    System.out.println(imageWidth);
//                    food.setLayoutX(centerX - imageWidth / 2);
//                    food.setLayoutY(centerY - imageHeight / 2);
//                    activePane.getChildren().add(food);
                }
                centerY += HEIGHT;
            }
        }

        return activePane;
    }
}
