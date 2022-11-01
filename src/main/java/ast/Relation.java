package ast;

import parse.TokenType;

public class Relation extends Condition
{
    private Expr left;
    private TokenType rel;
    private Expr right;

    public Relation(Expr left, TokenType rel, Expr right)
    {
        this.left = left;
        this.rel = rel;
        this.right = right;
    }

    public Expr getLeft()
    {
        return left;
    }

    public TokenType getOperator()
    {
        return rel;
    }

    public Expr getRight()
    {
        return right;
    }

    @Override
    public String toString()
    {
        return null;
    }

    @Override
    public boolean classInv()
    {
        return false;
    }

}
