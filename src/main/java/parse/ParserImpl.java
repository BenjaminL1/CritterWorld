package parse;

import ast.*;
import ast.Number;
import cms.util.maybe.Maybe;
import exceptions.SyntaxError;
import java.io.Reader;

class ParserImpl implements Parser {

    @Override
    public Program parse(Reader r) throws SyntaxError {
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
        while(t.peek().getType() != TokenType.EOF) {
            if(t.peek().getType() != TokenType.SEMICOLON){
                parseRule(t); //add this to programImpl class children
            }
            else consume(t, TokenType.SEMICOLON); //and add this to the programImpl class children
        }
        throw new UnsupportedOperationException();
    }

    public static Rule parseRule(Tokenizer t) throws SyntaxError {

        //create new rule node
        while(t.peek().getType() != TokenType.SEMICOLON){
            if(t.peek().getType() != TokenType.ARR){
                parseCondition(t);
            }
            else{
                //add t.next() to rule node children, t.next() should be "-->"
            }
        }
        //create a new
        throw new UnsupportedOperationException();
    }

    public static Condition parseCondition(Tokenizer t) throws SyntaxError {
        //create new condition orCondtion
        while(t.peek().getType() != TokenType.ARR){
            //create new condition andCondition
            while(t.peek().getType() != TokenType.OR){

                if(t.peek().getType() != TokenType.AND){
                    parseExpression(t); //add this to andCondition node
                }
                else; //add this.next() (should be "and") as child of andCondition
            }
            //put andCondition as child in orCondition
            //put this.next() (should be "or") as a child in orCondtion
        }
        throw new UnsupportedOperationException();
    }

    public static Expr parseExpression(Tokenizer t) throws SyntaxError {
        Expr ret = parseTerm(t);
        while (t.next().isAddOp())
        {
            if (t.peek().getType() == TokenType.PLUS)
            {
                t.next();
                ((Number) ret).add((Number) parseFactor(t));
            }
            else if (t.peek().getType() == TokenType.MINUS)
            {
                t.next();
                ((Number) ret).subtract((Number) parseFactor(t));
            }
        }
        return ret;
    }

    public static Expr parseTerm(Tokenizer t) throws SyntaxError
    {
        Expr ret = parseFactor(t);
        while (t.peek().isMulOp())
        {
            if (t.peek().getType() == TokenType.MUL)
            {
                t.next();
                ((Number) ret).multiply((Number) parseFactor(t));
            }
            else if (t.peek().getType() == TokenType.DIV)
            {
                t.next();
                ((Number) ret).divide((Number) parseFactor(t));
            }
            else if (t.peek().getType() == TokenType.MOD)
            {
                t.next();
                ((Number) ret).mod((Number) parseFactor(t));
            }
        }
        return ret;
    }

    public static Expr parseFactor(Tokenizer t) throws SyntaxError
    {
        if (t.peek().isNum())
        {
            return new Number(Integer.parseInt(t.next().toString()));
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
                // throw new ;
            }
        }
        else if (t.peek().isAddOp())
        {
            if (t.next().getType() != TokenType.MINUS)
            {
                // throw new ;
            }
            Expr factor = parseFactor(t);
            ((Number) factor).changeSign();
            return factor;
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

    // TODO
    // add more as necessary...

    /**
     * Consumes a token of the expected type.
     *
     * @throws SyntaxError if the wrong kind of token is encountered.
     */
    public static void consume(Tokenizer t, TokenType tt) throws SyntaxError {
        // TODO
        throw new UnsupportedOperationException();
    }
}
