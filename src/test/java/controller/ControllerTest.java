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
}
