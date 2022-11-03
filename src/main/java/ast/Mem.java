package ast;

import parse.TokenType;

public class Mem extends Expr
{
    Expr e;
    TokenType sugar;

    public Mem(Expr e)
    {
        this.e = e;
    }

    public Mem(TokenType sugar)
    {
        this.sugar = sugar;
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
    public String toString() {
        return null;
    }

    @Override
    public boolean classInv() {
        return false;
    }
}
