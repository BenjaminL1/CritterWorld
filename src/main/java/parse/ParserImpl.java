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

    public static Command parseCommand(Tokenizer t) throws SyntaxError {

        Command command = new Command();
        while(t.hasNext()){
            if(t.peek().getType().category() == TokenCategory.MEMSUGAR){
                command.getChildren().add(parseUpdate(t));
            }
            else if(t.peek().getType().category() == TokenCategory.ACTION){
                command.getChildren().add(parseAction(t));
            }
        }
        return command;
    }

    public static Update parseUpdate(Tokenizer t) throws SyntaxError {
        consume(t, TokenType.MEM);
        consume(t, TokenType.LBRACE);
        Expr memNumber = parseExpression(t);
        consume(t, TokenType.RBRACE);
        consume(t, TokenType.ASSIGN);
        Expr memValue = parseExpression(t);
        return new Update(memNumber, memValue);
    }

    public static Action parseAction(Tokenizer t) throws SyntaxError{
        if(t.peek().getType().category() != TokenCategory.ACTION) throw new UnsupportedOperationException();
        TokenType actionName = t.next().getType();
        if(actionName == TokenType.SERVE){
            consume(t, TokenType.LBRACE);
            Expr deezN = parseExpression(t);
            consume(t, TokenType.RBRACE);
            return new Action(actionName, deezN);
        }
        return new Action(actionName);
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
            return new Number(Integer.parseInt(t.next().toString()));
        }
        else if (t.peek().isMemSugar())
        {
            return new Mem(t.next().getType());
        }
        else if (t.peek().getType() == TokenType.MEM)
        {
            t.next();
            consume(t, TokenType.LBRACKET);
            Expr ret = new Mem(parseExpression(t));
            consume(t, TokenType.RBRACKET);
            return ret;
        }
        else if (t.peek().getType() == TokenType.LPAREN)
        {
            t.next();
            Expr ret = parseExpression(t);
            consume(t, TokenType.RPAREN);
            return ret;
        }
        else if (t.peek().getType() == TokenType.MINUS)
        {
            t.next();
            Expr ret = parseFactor(t);
            ret.changeSign();
            return ret;
        }
        else if (t.peek().isSensor())
        {
            return parseSensor(t);
        }
        else
        {
            throw new UnsupportedOperationException();
        }
    }

    public static Expr parseSensor(Tokenizer t) throws SyntaxError
    {
        if (t.peek().getType() == TokenType.NEARBY)
        {
            t.next();
            consume(t, TokenType.LBRACKET);
            Expr ret = new NearbySensor(parseExpression(t));
            consume(t, TokenType.RBRACKET);
            return ret;
        }
        else if (t.peek().getType() == TokenType.AHEAD)
        {
            t.next();
            consume(t, TokenType.LBRACKET);
            Expr ret = new AheadSensor(parseExpression(t));
            consume(t, TokenType.RBRACKET);
            return ret;
        }
        else if (t.peek().getType() == TokenType.RANDOM)
        {
            t.next();
            consume(t, TokenType.LBRACKET);
            Expr ret = new RandomSensor(parseExpression(t));
            consume(t, TokenType.RBRACKET);
            return ret;
        }
        else if (t.peek().getType() == TokenType.SMELL)
        {
            t.next();
            return new SmellSensor();
        }
        else
        {
            throw new UnsupportedOperationException();
        }
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
