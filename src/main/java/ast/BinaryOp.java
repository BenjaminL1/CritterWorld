package ast;

import parse.TokenType;

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


    @Override
    public String toString() {
        return null;
    }

    @Override
    public boolean classInv() {
        return false;
    }
}
