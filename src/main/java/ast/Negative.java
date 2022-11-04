package ast;

public class Negative extends Expr
{

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
