package ast;

public class NegativeExpr extends Expr
{
    private Negative negativeNode;
    private Expr factor;

    public NegativeExpr(Negative negativeNode, Expr factor)
    {
        this.negativeNode = negativeNode;
        this.factor = factor;
    }

    public Negative getLeft()
    {
        return negativeNode;
    }

    public Expr getRight()
    {
        return factor;
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
