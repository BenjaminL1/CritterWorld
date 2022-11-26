package view;

import controller.Controller;
import controller.ControllerFactory;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;

public class Main extends Application
{
    Controller controller = ControllerFactory.getConsoleController();

    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        primaryStage.setTitle("CritterWorld");

        Group world = new Group();

//        Polygon polygon = new Polygon(
//                12.5, 43.3013,         // bottom left
//                37.5, 43.3013,         // bottom right
//                50, 21.65065,          // center right
//                37.5, 0,               // top right
//                12.5, 0,               // top left
//                0, 21.65065);          // center left

//        polygon.setStroke(Color.BLACK);
//        polygon.setStrokeWidth(3);
//        polygon.setFill(Color.TRANSPARENT);

//        Polygon polygon = new Polygon(
//                17.5, 48.3013,  // bottom left
//                44.5, 48.3013,         // bottom right
//                55, 26.65065,          // center right
//                44.5, 5,               // top right
//                17.5, 5,               // top left
//                5, 26.65065);          // center left

//        double blX = 12.5;
////        double blY = 43.3013;
//        double brX = 37.5;
////        double brY = 43.3013;
//        double crX = 50;
////        double crY = 21.65065;
//        double trX = 37.5;
////        double trY = 0;
//        double tlX = 12.5;
////        double tlY = 0;
//        double clX = 0;
////        double clY = 21.65065;

        double blX = 17.5;
//        double blY = 43.3013;
        double brX = 44.5;
//        double brY = 43.3013;
        double crX = 55;
//        double crY = 21.65065;
        double trX = 44.5;
//        double trY = 0;
        double tlX = 17.5;
//        double tlY = 0;
        double clX = 5;
//        double clY = 21.65065;

        final double height = 43.3013;
//        final double width = 50;

        int numRows = 9;
        int numColumns = 7;

        for (int i = 0; i < numColumns; i++)
        {
            if (i > 0)
            {
                blX += 37.5 + 1;
                brX += 37.5 + 1;
                crX += 37.5 + 1;
                trX += 37.5 + 1;
                tlX += 37.5 + 1;
                clX += 37.5 + 1;
            }

            double blY;
            double brY;
            double crY;
            double trY;
            double tlY;
            double clY;

            if (numRows % 2 == 0)
            {
                blY = i % 2 == 0 ? 70.3013 : 70.3013 - height / 2;
                brY = i % 2 == 0 ? 70.3013 : 70.3013 - height / 2;
                crY = i % 2 == 0 ? 48.65065 : 48.65065 - height / 2;
                trY = i % 2 == 0 ? 27 : 27 - height / 2;
                tlY = i % 2 == 0 ? 27 : 27 - height / 2;
                clY = i % 2 == 0 ? 48.65065 : 48.65065 - height / 2;
            }
            else
            {
                blY = i % 2 == 0 ? 70.3013 : 70.3013 + height / 2;
                brY = i % 2 == 0 ? 70.3013 : 70.3013 + height / 2;
                crY = i % 2 == 0 ? 48.65065 : 48.65065 + height / 2;
                trY = i % 2 == 0 ? 27 : 27 + height / 2;
                tlY = i % 2 == 0 ? 27 : 27 + height / 2;
                clY = i % 2 == 0 ? 48.65065 : 48.65065 + height / 2;
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
                polygon.setStrokeWidth(3);
                polygon.setFill(Color.TRANSPARENT);

                world.getChildren().add(polygon);

                blY += height;
                brY += height;
                crY += height;
                trY += height;
                tlY += height;
                clY += height;
            }
        }


//        Polygon cut = new Polygon(
//                13.5, 41.56946,  // left bottom
//                37.5 - 1, 41.56946,         // right bottom
//                50 - 2, 21.65065,          // right center
//                37.5 - 1, 1.73204,               // right top
//                13.5, 1.73204,               // left top
//                0 + 2, 21.65065);          // left center



//        Shape hexagon = Shape.subtract(polygon, cut);

//        world.getChildren().add(polygon);

        ZoomableScrollPane pane = new ZoomableScrollPane(world);
        Scene scene = new Scene(pane, 700, 500);

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
