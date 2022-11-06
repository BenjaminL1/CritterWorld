package parse;

import ast.*;
import ast.Number;
import cms.util.maybe.Maybe;
import exceptions.SyntaxError;

import javax.xml.transform.Source;
import java.io.Reader;

class ParserImpl implements Parser
{

    @Override
    public Program parse(Reader r) throws SyntaxError
    {
        return parseProgram(new Tokenizer(r));
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
    public static ProgramImpl parseProgram(Tokenizer t) throws SyntaxError
    {
//        System.out.println("parseProgram");
        ProgramImpl ret = new ProgramImpl();
        while(t.peek().getType() != TokenType.EOF)
        {
            Rule rule = parseRule(t);
            rule.addParentPointer(ret);
            ret.addRule(rule);
        }
        return ret;
    }

    public static Rule parseRule(Tokenizer t) throws SyntaxError
    {
//        System.out.println("parseRule");
        Condition condition = parseCondition(t);
        consume(t, TokenType.ARR);
        Command command = parseCommand(t);
        consume(t, TokenType.SEMICOLON);
        Rule ret = new Rule (condition, command);
        condition.addParentPointer(ret);
        command.addParentPointer(ret);
        return ret;
    }

    public static Command parseCommand(Tokenizer t) throws SyntaxError
    {
//        System.out.println("parseCommand");
        // TODO: fix for update-or-action
        Command command = new Command();
        while(t.peek().getType().category() == TokenCategory.MEMSUGAR || t.peek().getType() == TokenType.MEM
                || t.peek().getType().category() == TokenCategory.ACTION)
        {
            // TODO: fix mem
            if(t.peek().getType().category() == TokenCategory.MEMSUGAR || t.peek().getType() == TokenType.MEM)
            {
                Update update = parseUpdate(t);
                update.addParentPointer(command);
                command.add(update);
            }
            else if(t.peek().getType().category() == TokenCategory.ACTION)
            {
                Action action = parseAction(t);
                action.addParentPointer(command);
                command.add(action);
                break;
            }
        }
        return command;
    }

    // TODO: fix to account for memsugars
    public static Update parseUpdate(Tokenizer t) throws SyntaxError
    {
//        System.out.println("parseUpdate");
        Mem memNumber;
        if (t.peek().getType().category() == TokenCategory.MEMSUGAR)
        {
            memNumber = new Mem(t.next().getType());
        }
        else
        {
            consume(t, TokenType.MEM);
            consume(t, TokenType.LBRACKET);
            memNumber = new Mem(parseExpression(t));
            consume(t, TokenType.RBRACKET);
        }
        consume(t, TokenType.ASSIGN);
        Expr memValue = parseExpression(t);
        Update ret = new Update(memNumber, memValue);
        memNumber.addParentPointer(ret);
        memValue.addParentPointer(ret);
        return ret;
    }

    public static Action parseAction(Tokenizer t) throws SyntaxError
    {
//        System.out.println("parseAction");
        if(t.peek().getType().category() != TokenCategory.ACTION) throw new UnsupportedOperationException();
        TokenType actionName = t.next().getType();
        if(actionName == TokenType.SERVE)
        {
            consume(t, TokenType.LBRACE);
            Expr deezN = parseExpression(t);
            consume(t, TokenType.RBRACE);
            Action ret = new Action(actionName, deezN);
            deezN.addParentPointer(ret);
            return ret;
        }
        return new Action(actionName);
    }

    public static Condition parseCondition(Tokenizer t) throws SyntaxError
    {
//        System.out.println("parseCondition");
        Condition left = parseConjunction(t);
        while(t.peek().getType() == TokenType.OR)
        {
            t.next();
            Condition right = parseConjunction(t);
            Condition temp = new BinaryCondition(left, BinaryCondition.Operator.OR, right);
            left.addParentPointer(temp);
            right.addParentPointer(temp);
            left = temp;
        }
        return left;
    }

    public static Condition parseConjunction(Tokenizer t) throws SyntaxError
    {
//        System.out.println("parseConjunction");
        Condition left = parseRelation(t);
        while (t.peek().getType() == TokenType.AND)
        {
            t.next();
            Condition right = parseRelation(t);
            Condition temp = new BinaryCondition(left, BinaryCondition.Operator.AND, right);
            left.addParentPointer(temp);
            right.addParentPointer(temp);
            left = temp;
        }
        return left;
    }

    public static Condition parseRelation(Tokenizer t) throws SyntaxError
    {
//        System.out.println("parseRelation");
        if (t.peek().getType() == TokenType.LBRACE)
        {
            t.next();
            Condition ret = parseCondition(t);
            consume(t, TokenType.RBRACE);
            return ret;
        }
        else
        {
            Expr left = parseExpression(t);
            if(t.peek().getType().category() != TokenCategory.RELOP) throw new UnsupportedOperationException( );
            TokenType rel = t.next().getType();
            Expr right = parseExpression(t);
            Condition ret = new Relation(left, rel, right);
            left.addParentPointer(ret);
            right.addParentPointer(ret);
            return ret;
        }
    }

    public static Expr parseExpression(Tokenizer t) throws SyntaxError
    {
//        System.out.println("parseExpression");
        Expr left = parseTerm(t);
        while (t.peek().getType().category() == TokenCategory.ADDOP)
        {
            TokenType addOp = t.next().getType();
            Expr right = parseTerm(t);
            Expr temp = new BinaryOp(left, addOp, right);
            left.addParentPointer(temp);
            right.addParentPointer(temp);
            left = temp;
        }
        return left;
    }

    public static Expr parseTerm(Tokenizer t) throws SyntaxError
    {
//        System.out.println("parseTerm");
        Expr left = parseFactor(t);
        while(t.peek().getType().category() == TokenCategory.MULOP)
        {
            TokenType addOp = t.next().getType();
            Expr right = parseFactor(t);
            Expr temp = new BinaryOp(left, addOp, right);
            left.addParentPointer(temp);
            right.addParentPointer(temp);
            left = temp;
        }
        return left;
    }

    public static Expr parseFactor(Tokenizer t) throws SyntaxError
    {
//        System.out.println("parseFactor");
        System.out.println(t.peek());
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
            return new NegativeExpr(new Negative(), parseFactor(t));
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
//        System.out.println("parseSensor");
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
    public static void consume(Tokenizer t, TokenType tt) throws SyntaxError
    {
        Token temp = t.next();
        if(temp.getType() != tt)
        {
            throw new UnsupportedOperationException();
        }
    }
}
