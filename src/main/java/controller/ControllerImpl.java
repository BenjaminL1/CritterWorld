package controller;

import ast.Program;
import exceptions.SyntaxError;
import model.Constants;
import model.ControlOnlyWorld;
import model.ReadOnlyWorld;
import model.World;
import parse.Parser;
import parse.ParserFactory;

import java.io.*;

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
        return false;
    }

    @Override
    public boolean loadCritters(String filename, int n)
    {
        try
        {
            Reader r = new FileReader(filename);
            String species;
            Program ast;
            int[] mem = new int[Constants.MIN_MEMORY];
            int row;
            int column;
            int direction;
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
                    int num = Integer.parseInt(text.substring(text.length() - 1));
                    mem = num > 7 ? new int[num] : mem;
                    mem[0] = num;
                    mem[5] = 1;
                }
                else if (text.startsWith("defense: ") && line == 3)
                {
                    int num = Integer.parseInt(text.substring(text.length() - 1));
                    mem[1] = num;
                }
                else if (text.startsWith("offense: ") && line == 4)
                {
                    int num = Integer.parseInt(text.substring(text.length() - 1));
                    mem[2] = num;
                }
                else if (text.startsWith("size: ") && line == 5)
                {
                    int num = Integer.parseInt(text.substring(text.length() - 1));
                    mem[3] = num;
                }
                else if (text.startsWith("energy: ") && line == 6)
                {
                    int num = Integer.parseInt(text.substring(text.length() - 1));
                    mem[4] = num;
                }
                else if (text.startsWith("posture: ") && line == 7)
                {
                    int num = Integer.parseInt(text.substring(text.length() - 1));
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
            // TODO create critters;
        }
        catch (IOException e)
        {
            System.out.println("please enter a valid file");
        }
        catch (SyntaxError e)
        {
            System.out.println("a valid program should not have syntax errors");;
        }
        return false;
    }

    @Override
    public boolean advanceTime(int n) {
        return false;
    }

    @Override
    public void printWorld(PrintStream out)
    {

    }
}
