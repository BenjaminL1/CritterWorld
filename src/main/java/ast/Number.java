package ast;

public class Number extends Expr
{
    private int num;

    public Number(int num)
    {
        this.num = num;
    }

    public Node clone()
    {
        return new Number(this.getNum());
    }

    public int getNum()
    {
        return num;
    }

    public void changeNum(int newNum)
    {
        num = newNum;
    }

    @Override
    public void accept(Visitor v)
    {
        v.visit(this);
    }

    @Override
    public String toString()
    {
        return "" + num;
    }

    @Override
    public boolean classInv()
    {
        return false;
    }
}
