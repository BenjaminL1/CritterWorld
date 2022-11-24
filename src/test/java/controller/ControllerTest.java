package controller;

import controller.Controller;
import controller.ControllerFactory;
import org.junit.jupiter.api.Test;

public class ControllerTest
{
    @Test
    public void testLoadWorld()
    {
        Controller controller = ControllerFactory.getConsoleController();
        controller.loadWorld("src\\test\\resources\\A5files\\test_world.txt", true, false);
        controller.printWorld(System.out);
    }

    @Test
    public void testNewWorld()
    {
        Controller controller = ControllerFactory.getConsoleController();
        controller.newWorld();
        controller.loadCritters("src\\test\\resources\\A5files\\test_critter.txt", 10);
        controller.printWorld(System.out);
    }

    @Test
    public void testAdvanceTimeStep()
    {
        Controller controller = ControllerFactory.getConsoleController();
        controller.loadWorld("src\\test\\resources\\A5files\\test_world.txt", true, false);
//        controller.printWorld(System.out);
//        System.out.println();
        controller.advanceTime(5);
        controller.printWorld(System.out);
    }

    @Test
    public void testSpiralCritter()
    {
        Controller controller = ControllerFactory.getConsoleController();
//        controller.newWorld();
//        controller.loadCritters("src\\test\\resources\\A5files\\spiral_critter.txt", 1);
        controller.loadWorld("src\\test\\resources\\A5files\\spiral_world.txt", false, false);
        controller.printWorld(System.out);
        System.out.println();
        controller.advanceTime(300);
        System.out.println();
        controller.printWorld(System.out);
    }

    @Test
    public void testEatCritter(){
        Controller controller = ControllerFactory.getConsoleController();
        controller.loadWorld("src\\test\\resources\\A5files\\eat_world.txt", true, false);
        controller.printWorld(System.out);
        System.out.println();
        controller.advanceTime(100);
        System.out.println();
        controller.printWorld(System.out);
    }

    @Test
    public void testBudCritter(){
        Controller controller = ControllerFactory.getConsoleController();
        controller.loadWorld("src\\test\\resources\\A5files\\bud_world.txt", false, false);
        controller.printWorld(System.out);
        System.out.println();
        controller.advanceTime(10);
        System.out.println();
        controller.printWorld(System.out);
    }

    @Test
    public void testSmeller()
    {
        Controller controller = ControllerFactory.getConsoleController();
        controller.loadWorld("src\\test\\resources\\A5files\\smell_world.txt", false, false);
        controller.printWorld(System.out);
        System.out.println();
        controller.advanceTime(10);
        System.out.println();
        controller.printWorld(System.out);
    }
}
