package ast;

import parse.TokenType;

import java.util.ArrayList;
import java.util.List;

public class Relation extends Condition
{
    private Expr left;
    private TokenType rel;
    private Expr right;

    public Relation(Expr left, TokenType rel, Expr right)
    {
        this.left = left;
        this.rel = rel;
        this.right = right;
    }

    public Expr getLeft()
    {
        return left;
    }

    public TokenType getOperator()
    {
        return rel;
    }

    public Expr getRight()
    {
        return right;
    }

    public void changeLeft(Expr l)
    {
        left = l;
    }

    public void changeRel(TokenType newRel)
    {
        rel = newRel;
    }

    public void changeRight(Expr r)
    {
        right = r;
    }

    @Override
    public String toString()
    {
        return null;
    }

    @Override
    public Node clone()
    {
        Expr lClone = (Expr) left.clone();
        Expr rClone = (Expr) right.clone();
        Relation ret = new Relation(lClone, rel, rClone);
        lClone.setParent(ret);
        rClone.setParent(ret);
        return ret;
    }

    public List<Node> getChildren(){
        List<Node> list = new ArrayList<Node>();
        list.add(left);
        list.add(right);
        return list;
    }

    @Override
    public StringBuilder prettyPrint(StringBuilder sb) {
        sb.append(left.prettyPrint(sb));
        sb.append(" ");
        sb.append(rel.toString());
        sb.append(" ");
        sb.append(right.prettyPrint(sb));
        return sb;
    }

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
