package ast;

public class Number extends Expr
{
    private int num;

    public Number(int num)
    {
        this.num = num;
    }

    public int getNum()
    {
        return num;
    }

    public void changeNum(int newNum)
    {
        num = newNum;
    }

    public void changeSign()
    {
        num *= -1;
    }

    public void add(Number term)
    {
        num += term.getNum();
    }

    public void subtract(Number term)
    {
        num -= term.getNum();
    }


    public void multiply(Number factor)
    {
        num *= factor.getNum();
    }

    public void divide(Number factor)
    {
        num /= factor.getNum();
    }

    public void mod(Number factor)
    {
        num %= factor.getNum();
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
