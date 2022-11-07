package ast;

import java.util.ArrayList;
import java.util.List;

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

    public void changeLeft(Condition left)
    {
        l = left;
    }

    public void changeOp(Operator newOp)
    {
        op = newOp;
    }

    public void changeRight(Condition right)
    {
        r = right;
    }

    @Override
    public void accept(Visitor v)
    {
        v.visit(this);
    }

    public List<Node> getChildren(){
        List<Node> list = new ArrayList<Node>();
        list.add(l);
        list.add(r);
        return list;
    }

    @Override
    public Node clone()
    {
        Condition clonedLeft = (Condition) this.l.clone();
        Condition clonedRight = (Condition)this.r.clone();
        BinaryCondition cloned = new BinaryCondition(clonedLeft, this.op, clonedRight);
        clonedLeft.setParent(cloned);
        clonedRight.setParent(cloned);
        return cloned;
    }

    @Override
    public StringBuilder prettyPrint(StringBuilder sb)
    {
        if(this.getParent() instanceof BinaryCondition)
        {
            BinaryCondition temp = (BinaryCondition) this.getParent();
            if(temp.getOp() == Operator.AND && this.op == Operator.OR){
                sb.append("{");
                getLeft().prettyPrint(sb);
                sb.append(" ");
                sb.append(op.toString());
                sb.append(" ");
                getRight().prettyPrint(sb);
                sb.append("}");
                return sb;
            }
        }
        getLeft().prettyPrint(sb);
        sb.append(" ");
        sb.append(op.toString());
        sb.append(" ");
        getRight().prettyPrint(sb);
        return sb;
    }

    @Override
    public String toString()
    {
        return this.prettyPrint(new StringBuilder()).toString();
    }

    public boolean classInv()
    {
        // TODO
        return false;
    }
}
