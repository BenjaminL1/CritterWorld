package controller;

import ast.Program;
import exceptions.SyntaxError;
import model.Constants;
import model.ControlOnlyWorld;
import model.ReadOnlyWorld;
import model.World;
import parse.Parser;
import parse.ParserFactory;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.File;
import java.io.Reader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class ControllerImpl implements Controller
{
    private ControlOnlyWorld controlWorld;
    private ReadOnlyWorld readOnlyWorld;

    @Override
    public ReadOnlyWorld getReadOnlyWorld()
    {
        return readOnlyWorld;
    }

    @Override
    public void newWorld()
    {
        World world = new World();
        controlWorld = world;
        readOnlyWorld = world;
    }

    @Override
    public boolean loadWorld(String filename, boolean enableManna, boolean enableForcedMutation)
    {
        File worldFile = new File(filename);
        try
        {
            Scanner sc = new Scanner(worldFile);
            while (sc.hasNext())
            {
                String object = sc.next();
                if (object.equals("rock"))
                {
                    int column = sc.nextInt();
                    int row = sc.nextInt();
                    controlWorld.addRock(row, column);
                }
                else if (object.equals("food"))
                {
                    int column = sc.nextInt();
                    int row = sc.nextInt();
                    int amount = sc.nextInt();
                    controlWorld.addFood(row, column, amount);
                }
                else if (object.equals("critter"))
                {
                    String critterFile = sc.next();
                    int column = sc.nextInt();
                    int row = sc.nextInt();
                    int direction = sc.nextInt();
                    Object[] critterInfo = parseCritterFile(critterFile);
                    controlWorld.addCritter((String) critterInfo[0], (int[]) critterInfo[1], (Program) critterInfo[2],
                            row, column, direction);
                }
            }
            return true;
        }
        catch (FileNotFoundException e)
        {
            System.out.println("please input a valid world file");
        }
        return false;
    }

    @Override
    public boolean loadCritters(String filename, int n)
    {
        Object[] critterInfo = parseCritterFile(filename);
        if (critterInfo != null)
        {
            for (int i = 0; i < n; i++)
            {
                controlWorld.addCritter((String) critterInfo[0], (int[]) critterInfo[1], (Program) critterInfo[2]);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean advanceTime(int n)
    {
        return false;
    }

    @Override
    public void printWorld(PrintStream out)
    {

    }

    public Object[] parseCritterFile(String filename)
    {
        try
        {
            Reader r = new FileReader(filename);
            String species = "";
            Program ast;
            int[] mem = new int[Constants.MIN_MEMORY];
//            int row;
//            int column;
//            int direction;
            int line = 1;
            String text = "";
            while (line <= 7)
            {
                text = "";
                int i;
                while ((char) (i = r.read()) != '\n')
                {
                    text += (char) i;
                }
                if (text.startsWith("species: ") && line == 1)
                {
                    species = text.substring(9);
                }
                else if (text.startsWith("memsize: ") && line == 2)
                {
                    int num = Integer.parseInt(text.substring(9));
                    mem = num > 7 ? new int[num] : mem;
                    mem[0] = num;
                    mem[5] = 1;
                }
                else if (text.startsWith("defense: ") && line == 3)
                {
                    int num = Integer.parseInt(text.substring(9));
                    mem[1] = num;
                }
                else if (text.startsWith("offense: ") && line == 4)
                {
                    int num = Integer.parseInt(text.substring(9));
                    mem[2] = num;
                }
                else if (text.startsWith("size: ") && line == 5)
                {
                    int num = Integer.parseInt(text.substring(6));
                    mem[3] = num;
                }
                else if (text.startsWith("energy: ") && line == 6)
                {
                    int num = Integer.parseInt(text.substring(8));
                    mem[4] = num;
                }
                else if (text.startsWith("posture: ") && line == 7)
                {
                    int num = Integer.parseInt(text.substring(9));
                    mem[6] = num;
                }
                else
                {
                    throw new IOException();
                }
                r.read();
                line++;
            }
            Parser p = ParserFactory.getParser();
            ast = p.parse(r);
            return new Object[]{species, mem, ast};
        }
        catch (IOException e)
        {
            System.out.println("please input a valid critter file");
        }
        catch (SyntaxError e)
        {
            System.out.println("a valid program should not have syntax errors");;
        }
        return null;
    }
}
