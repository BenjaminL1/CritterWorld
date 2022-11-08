package ast;

import java.util.ArrayList;
import java.util.List;

public class NegativeExpr extends Expr
{
    private Negative negativeNode;
    private Expr factor;

    public NegativeExpr(Negative negativeNode, Expr factor)
    {
        this.negativeNode = negativeNode;
        this.factor = factor;
    }

    @Override
    public Node clone(){
        Negative clonedNegativeNode =  (Negative) this.negativeNode.clone();
        Expr clonedFactor =  (Expr) this.factor.clone();
        NegativeExpr cloned = new NegativeExpr(clonedNegativeNode, clonedFactor);
        clonedNegativeNode.setParent(cloned);
        clonedFactor.setParent(cloned);
        return cloned;
    }

    @Override
    public List<Node> getChildren() {
        ArrayList<Node> children = new ArrayList<>();
        children.add(negativeNode);
        children.add(factor);
        return children;
    }

    @Override
    public StringBuilder prettyPrint(StringBuilder sb) {
        negativeNode.prettyPrint(sb);
        sb.append(" ");
        factor.prettyPrint(sb);
        return sb;
    }

    public Negative getLeft()
    {
        return negativeNode;
    }

    public Expr getRight()
    {
        return factor;
    }

    public void changeRight(Expr newFactor)
    {
        factor = newFactor;
    }

    @Override
    public void accept(Visitor v)
    {
        v.visit(this);
    }


    @Override
    public boolean classInv()
    {
        return false;
    }
}
