package ast;

/** A representation of a binary Boolean condition: 'and' or 'or' */
public class BinaryCondition extends Condition
{
    private Condition l;
    private Operator op;
    private Condition r;

    /**
     * Create an AST representation of l op r.
     *
     * @param l
     * @param op
     * @param r
     */
    public BinaryCondition(Condition l, Operator op, Condition r)
    {
        this.l = l;
        this.op = op;
        this.r = r;

    }

    /** An enumeration of all possible binary condition operators. */
    public enum Operator
    {
        OR,
        AND;
    }

    public Condition getLeft()
    {
        return l;
    }

    public Operator getOp()
    {
        return op;
    }

    public Condition getRight()
    {
        return r;
    }

    @Override
    public String toString(){
        // TODO
        return null;
    }

    public boolean classInv() {
        // TODO
        return false;
    }
}
