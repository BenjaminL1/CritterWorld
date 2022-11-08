package parser;

import ast.*;
import cms.util.maybe.Maybe;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import exceptions.SyntaxError;
import org.junit.jupiter.api.Test;
import parse.Parser;
import parse.ParserFactory;

import static org.junit.jupiter.api.Assertions.*;

/** This class contains tests for the Critter parser. */
public class ParserTest
{
    /** Checks that a valid critter program is not {@code null} when parsed. */
    @Test
    public void testProgramIsNotNone()
    {
        InputStream in = ClassLoader.getSystemResourceAsStream("files/draw_critter_2.txt");
        Reader r = new BufferedReader(new InputStreamReader(in));
        Parser parser = ParserFactory.getParser();

        try
        {
            Program prog = parser.parse(r);
            System.out.println(prog.toString());
        }
        catch(SyntaxError e)
        {
            fail("A valid program should not have syntax errors");
        }
    }

    @Test
    public void testRemove()
    {
        Mutation mut = MutationFactory.getRemove();
        InputStream in = ClassLoader.getSystemResourceAsStream("files/draw_critter_2.txt");
        Reader r = new BufferedReader(new InputStreamReader(in));
        Parser parser = ParserFactory.getParser();
        try
        {
            Program prog = parser.parse(r);
            Node target = prog.nodeAt((int) (Math.random() * prog.size()));
            System.out.println();
            System.out.println(target.toString());
            System.out.println(target.getClass());
            System.out.println();
            System.out.println(mut.canApply(target));
            System.out.println();
            System.out.println(((AbstractNode) target).getParent());
            System.out.println(((AbstractNode) target).getParent().getClass());
            System.out.println();
            mut.apply(prog, target);
            System.out.println(prog.toString());
        }
        catch(SyntaxError e)
        {
            fail("A valid program should not have syntax errors");
        }
    }

}
