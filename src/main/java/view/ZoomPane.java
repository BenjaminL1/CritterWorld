package view;

import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class ZoomPane extends ScrollPane
{
    private double scaleValue = 0.7;
    private double zoomIntensity = 0.02;
    private Node target;
    private Node zoomNode;
    private double minScaleValue;
    private double maxScaleValue = 9.8; // value determined thru testing

    // TODO change from type Node to type StackPane
    // Use StackPane.getScaleX() to check if scrolled too far out
    public ZoomPane(Node target)
    {
        super();
        this.target = target;
        this.minScaleValue = scaleValue;
        this.zoomNode = new Group(target);
        setContent(outerNode(zoomNode));

        setPannable(true);
        setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        setFitToHeight(true); //center
        setFitToWidth(true); //center
        updateScale();
//        System.out.println(minScaleValue);
    }

    public ZoomPane(Node target, int length)
    {
        super();
//        this.scaleValue = 20.0 / length;
//        this.scaleValue = 1 / Math.pow(Math.E, length * 0.017);
        this.scaleValue = 16.2 * Math.pow(length, -0.976);
        this.minScaleValue = scaleValue;
        this.target = target;
        this.zoomNode = new Group(target);
        setContent(outerNode(zoomNode));

        setPannable(true);
        setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        setFitToHeight(true); //center
        setFitToWidth(true); //center
        updateScale();
//        System.out.println(minScaleValue);
    }

    public double getMinScaleValue()
    {
        return minScaleValue;
    }

    public double getScaleValue()
    {
        return scaleValue;
    }

//    public void changeTarget(Node newTarget)
//    {
//        try
//        {
//            target = newTarget;
//            ((Group) zoomNode).getChildren().remove(0);
//            ((Group) zoomNode).getChildren().add(target);
//            setContent(outerNode(zoomNode));
//
//            setPannable(true);
//            setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
//            setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
//            setFitToHeight(true); //center
//            setFitToWidth(true); //center
//            updateScale();
//        }
//        catch (Exception e)
//        {
//
//        }
//    }

    public void changeTarget(Node newTarget)
    {
        try
        {
            ((StackPane) target).getChildren().remove(1);
            ((StackPane) target).getChildren().add(newTarget);
        }
        catch (Exception e)
        {

        }
    }

    private Node outerNode(Node node)
    {
        Node outerNode = centeredNode(node);
        outerNode.setOnScroll(e ->
        {
            e.consume();
            onScroll(e.getTextDeltaY(), new Point2D(e.getX(), e.getY()));
        });
        return outerNode;
    }

    private Node centeredNode(Node node)
    {
        VBox vBox = new VBox(node);
        vBox.setAlignment(Pos.CENTER);
        return vBox;
    }

    private void updateScale()
    {
        target.setScaleX(scaleValue);
        target.setScaleY(scaleValue);
    }

    private void onScroll(double wheelDelta, Point2D mousePoint)
    {
        double zoomFactor = Math.exp(wheelDelta * zoomIntensity);

//        System.out.println(scaleValue * zoomFactor);

//        scaleValue = scaleValue * zoomFactor;

        if (scaleValue * zoomFactor > minScaleValue && scaleValue * zoomFactor < maxScaleValue)
        {
            scaleValue = scaleValue * zoomFactor;

            Bounds innerBounds = zoomNode.getLayoutBounds();
            Bounds viewportBounds = getViewportBounds();

            // calculate pixel offsets from [0, 1] range
            double valX = this.getHvalue() * (innerBounds.getWidth() - viewportBounds.getWidth());
            double valY = this.getVvalue() * (innerBounds.getHeight() - viewportBounds.getHeight());

//            scaleValue = scaleValue * zoomFactor;
            updateScale();
            this.layout(); // refresh ScrollPane scroll positions & target bounds

            // convert target coordinates to zoomTarget coordinates
            Point2D posInZoomTarget = target.parentToLocal(zoomNode.parentToLocal(mousePoint));

            // calculate adjustment of scroll position (pixels)
            Point2D adjustment = target.getLocalToParentTransform().deltaTransform(posInZoomTarget.multiply(zoomFactor - 1));

            // convert back to [0, 1] range
            // (too large/small values are automatically corrected by ScrollPane)
            Bounds updatedInnerBounds = zoomNode.getBoundsInLocal();
            this.setHvalue((valX + adjustment.getX()) / (updatedInnerBounds.getWidth() - viewportBounds.getWidth()));
            this.setVvalue((valY + adjustment.getY()) / (updatedInnerBounds.getHeight() - viewportBounds.getHeight()));
        }
    }
}
