package parser;

import ast.Program;
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
        StringBuilder sb = new StringBuilder();

        try
        {
            Program prog = parser.parse(r);
            prog.prettyPrint(sb);
            System.out.println(sb.toString());
        }
        catch(SyntaxError e)
        {
            fail("A valid program should not have syntax errors");
        }
    }

    // TODO continue adding tests

}
