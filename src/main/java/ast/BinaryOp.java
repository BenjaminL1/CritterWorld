package ast;

import parse.TokenCategory;
import parse.TokenType;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Binary Operation
 * @author Richard Lin
 * @author Benjamin Li
 * @author Allison Zheng
 * @version
 * Class invariant: The global variable op must be of TokenCategory ADDOP or MULOP
 */
public class BinaryOp extends Expr
{
    private Expr left;
    private TokenType op;
    private Expr right;

    public BinaryOp(Expr left, TokenType op, Expr right)
    {
        this.left = left;
        this.op = op;
        this.right = right;
    }

    @Override
    public Node clone()
    {
        Expr clonedLeft = (Expr) left.clone();
        Expr clonedRight = (Expr) right.clone();
        BinaryOp cloned =  new BinaryOp(clonedLeft, op, clonedRight);
        clonedLeft.setParent(cloned);
        clonedRight.setParent(cloned);
        return cloned;
    }

    @Override
    public StringBuilder prettyPrint(StringBuilder sb) {
        if(this.getParent() instanceof BinaryOp)
        {
            BinaryOp temp = (BinaryOp) this.getParent();
            if(temp.getOp().equals(TokenCategory.MULOP) && this.op.equals(TokenCategory.ADDOP)){
                sb.append("{");
                sb.append(getLeft().prettyPrint(sb));
                sb.append(" ");
                sb.append(op.toString());
                sb.append(" ");
                sb.append(getRight().prettyPrint(sb));
                sb.append("}");
                return sb;
            }
        }
        sb.append(getLeft().prettyPrint(sb));
        sb.append(" ");
        sb.append(op.toString());
        sb.append(" ");
        sb.append(getRight().prettyPrint(sb));
        return sb;
    }

    public Expr getLeft()
    {
        return left;
    }

    public TokenType getOp()
    {
        return op;
    }

    public Expr getRight()
    {
        return right;
    }

    public void changeLeft(Expr l)
    {
        left = l;
    }

    public void changeOp(TokenType newOp)
    {
        op = newOp;
    }

    public void changeRight(Expr r)
    {
        right = r;
    }

    public void accept(Visitor v)
    {
        v.visit(this);
    }

    public List<Node> getChildren(){
        List<Node> list = new ArrayList<Node>();
        list.add(left);
        list.add(right);
        return list;
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
