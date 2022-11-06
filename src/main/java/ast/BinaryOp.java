package ast;

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
public class BinaryOp extends Expr {
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
    public Node clone(){
        return new BinaryOp((Expr) left.clone(), op, (Expr) right.clone());
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
