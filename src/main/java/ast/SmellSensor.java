package ast;

import java.util.List;

public class SmellSensor extends Sensor
{
    @Override
    public StringBuilder prettyPrint(StringBuilder sb) {
        sb.append("smell");
        return sb;
    }

    @Override
    public Node clone(){
        return new SmellSensor();
    }

    @Override
    public List<Node> getChildren() {
        return null;
    }

    @Override
    public void accept(Visitor v)
    {
        v.visit(this);
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
