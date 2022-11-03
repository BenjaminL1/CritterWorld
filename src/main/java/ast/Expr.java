package ast;

/** A critter program expression that has an integer value. */
public abstract class Expr extends AbstractNode
{
    private boolean isNegative;

    public Expr()
    {
        isNegative = false;
    }

    public boolean getIsNegative()
    {
        return isNegative;
    }
    public void changeSign()
    {
        isNegative = isNegative ? false : true;
    }
    @Override
    public NodeCategory getCategory()
    {
        return NodeCategory.EXPRESSION;
    }
}
