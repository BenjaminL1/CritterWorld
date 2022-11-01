package parse;

import ast.*;
import ast.Number;
import cms.util.maybe.Maybe;
import exceptions.SyntaxError;
import java.io.Reader;

class ParserImpl implements Parser {

    @Override
    public Program parse(Reader r) throws SyntaxError
    {
        // TODO
        throw new UnsupportedOperationException();
    }

    /**
     * Parses a program from the stream of tokens provided by the Tokenizer,
     * consuming tokens representing the program. All following methods with a
     * name "parseX" have the same spec except that they parse syntactic form
     * X.
     *
     * @return the created AST
     * @throws SyntaxError if the input tokens have invalid syntax
     */
    public static ProgramImpl parseProgram(Tokenizer t) throws SyntaxError {
        //create programImpl class
        ProgramImpl ret = new ProgramImpl();
        while(t.peek().getType() != TokenType.EOF)
        {
            if(t.peek().getType() != TokenType.SEMICOLON)
            {
                ret.addRule(parseRule(t)); //add this to programImpl class children
            }
            else consume(t, TokenType.SEMICOLON); //and add this to the programImpl class children
        }
        throw new UnsupportedOperationException();
    }

    public static Rule parseRule(Tokenizer t) throws SyntaxError
    {
        Condition condition = parseCondition(t);
        consume(t, TokenType.ARR);
        Command command= parseCommand(t);
        return new Rule (condition, command);
    }

    public static Command parseCommand(Tokenizer t)
    {

    }

    public static Condition parseCondition(Tokenizer t) throws SyntaxError
    {
        Condition left = parseConjunction(t);
        while(t.peek().getType() == TokenType.OR)
        {
            t.next();
            left = new BinaryCondition(left, BinaryCondition.Operator.OR, parseConjunction(t));
        }
        return left;
    }

    public static Condition parseConjunction(Tokenizer t) throws SyntaxError
    {
        Condition left = parseRelation(t);
        while (t.peek().getType() == TokenType.AND)
        {
            t.next();
            left = new BinaryCondition(left, BinaryCondition.Operator.AND, parseConjunction(t));
        }
        return left;
    }

    public static Condition parseRelation(Tokenizer t) throws SyntaxError
    {
        if (t.peek().getType() == TokenType.LBRACE)
        {
            t.next();
            Condition ret = parseCondition(t);
            consume(t, TokenType.LBRACE);
            return ret;
        }
        else
        {
            Expr left = parseExpression(t);
            if(t.peek().getType().category() != TokenCategory.RELOP) throw new UnsupportedOperationException( );
            TokenType rel = t.next().getType();
            return new Relation(left, rel, parseExpression(t));
        }
    }

    public static Expr parseExpression(Tokenizer t) throws SyntaxError
    {
        Expr left = parseTerm(t);
        while (t.peek().getType().category() == TokenCategory.ADDOP)
        {
            left = new BinaryOp(left, t.next().getType(), parseTerm(t));
        }
        return left;
    }

    public static Expr parseTerm(Tokenizer t) throws SyntaxError
    {
        Expr left = parseFactor(t);
        while(t.peek().getType().category() == TokenCategory.MULOP)
        {
            left = new BinaryOp(left, t.next().getType(), parseFactor(t));
        }
        return left;
    }

    public static Expr parseFactor(Tokenizer t) throws SyntaxError
    {
        if (t.peek().isNum())
        {

        }
        else if (t.peek().isMemSugar())
        {

        }
        else if (t.peek().getType() == TokenType.MEM)
        {

        }
        else if (t.peek().getType() == TokenType.LPAREN)
        {
            t.next();
            Expr ret = parseExpression(t);
            if (t.next().getType() == TokenType.RPAREN)
            {
                return ret;
            }
            else
            {
                throw new UnsupportedOperationException();
            }
        }
        else if (t.peek().isAddOp())
        {

        }
        else if (t.peek().isSensor())
        {
            if (t.next().getType() == TokenType.NEARBY)
            {

            }
            else if (t.next().getType() == TokenType.AHEAD)
            {

            }
            else if (t.next().getType() == TokenType.RANDOM)
            {

            }
        }
    }

    public static Sensor parseSensor(Tokenizer t) throws SyntaxError{
        Token curr = t.next();
        if(!curr.isSensor()) throw new UnsupportedOperationException;

        if(curr.getType() == TokenType.)

    }

    // TODO
    // add more as necessary...

    /**
     * Consumes a token of the expected type.
     *
     * @throws SyntaxError if the wrong kind of token is encountered.
     */
    public static void consume(Tokenizer t, TokenType tt) throws SyntaxError {
        Token temp = t.next();
        if(temp.getType() != tt) {
            throw new UnsupportedOperationException();
        }
    }
}
