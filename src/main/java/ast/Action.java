package ast;

import parse.TokenType;

import java.util.ArrayList;
import java.util.List;

public class Action extends AbstractNode
{
    TokenType name;
    Expr value;

    public Action(TokenType name)
    {
        this.name = name;
    }

    public Action(TokenType name, Expr value){
        this.name = name;
        this.value = value;
    }

    public List<Node> getChildren()
    {
        List<Node> list = new ArrayList<Node>();
        list.add(value);
        return list;
    }

    public TokenType getName()
    {
        return name;
    }

    public Expr getExpr()
    {
        return value;
    }

    public void changeExpr(Expr e)
    {
        value = e;
    }

    @Override
    public String toString() {
        return null;
    }

    @Override
    public NodeCategory getCategory() {
        return null;
    }

    @Override
    public boolean classInv() {
        return false;
    }

    public void accept(Visitor v)
    {
        v.visit(this);
    }
}
