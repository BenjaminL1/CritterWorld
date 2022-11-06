package ast;

import parse.TokenType;

public class Mem extends Expr
{
    Expr e;

    public Mem(Expr e)
    {
        this.e = e;
    }

    public Mem(TokenType sugar)
    {
        if (sugar == TokenType.ABV_MEMSIZE)
        {
            e = new Number(0);
        }
        else if (sugar == TokenType.ABV_DEFENSE)
        {
            e = new Number(1);
        }
        else if (sugar == TokenType.ABV_OFFENSE)
        {
            e = new Number(2);
        }
        else if (sugar == TokenType.ABV_SIZE)
        {
            e = new Number(3);
        }
        else if (sugar == TokenType.ABV_ENERGY)
        {
            e = new Number(4);
        }
        else if (sugar == TokenType.ABV_PASS)
        {
            e = new Number(5);
        }
        // (sugar == TokenType.ABV_POSTURE)
        else
        {
            e = new Number(6);
        }
    }

    public enum Sugar
    {
        MEMSIZE,
        DEFENSE,
        OFFENSE,
        SIZE,
        ENERGY,
        PASS,
        POSTURE;
    }

    @Override
    public Node clone()
    {
        Mem cloned = new Mem((Expr)this.e.clone());
        return cloned;
    }


    public Expr getExpr()
    {
        return e;
    }

    public void changeExpr(Expr e)
    {
        this.e = e;
    }

    @Override
    public void accept(Visitor v)
    {
        v.visit(this);
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
