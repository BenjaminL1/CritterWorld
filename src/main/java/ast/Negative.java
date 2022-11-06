package ast;

public class Negative extends Expr
{

    @Override
    public void accept(Visitor v)
    {
        v.visit(this);
    }

    @Override
    public String toString()
    {
        return "-";
    }

    @Override
    public boolean classInv()
    {
        return false;
    }
}
