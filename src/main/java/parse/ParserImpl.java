package parse;

import ast.Condition;
import ast.Expr;
import ast.Program;
import ast.ProgramImpl;
import ast.Rule;
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
     * @throws SyntaxError if there the input tokens have invalid syntax
     */
    public static ProgramImpl parseProgram(Tokenizer t) throws SyntaxError {
        while(t.peek().getType() != TokenType.EOF) {
            if(t.peek().getType() != TokenType.SEMICOLON){
                parseRule(t);
            }
            else consume(t, TokenType.SEMICOLON);
        }
        throw new UnsupportedOperationException();
    }

    public static Rule parseRule(Tokenizer t) throws SyntaxError {
        while(t.peek().getType() != TokenType.ARR){
            if(t.next().getType().category() = TokenType.CON)
        }
        throw new UnsupportedOperationException();
    }

    public static Condition parseCondition(Tokenizer t) throws SyntaxError {
        // TODO
        throw new UnsupportedOperationException();
    }

    public static Expr parseExpression(Tokenizer t) throws SyntaxError {
        // TODO
        throw new UnsupportedOperationException();
    }

    public static Expr parseTerm(Tokenizer t) throws SyntaxError {
        // TODO
        throw new UnsupportedOperationException();
    }

    public static Expr parseFactor(Tokenizer t) throws SyntaxError {
        // TODO
        throw new UnsupportedOperationException();
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
