package ast;

import cms.util.maybe.Maybe;

import java.util.ArrayList;
import java.util.List;

public class Swap extends AbstractMutation
{

    @Override
    public boolean equals(Mutation m)
    {
        return m instanceof Swap;
    }

    @Override
    public Maybe<Program> apply(Program program, Node node)
    {
        if (canApply(node))
        {
            node.accept(this);
            return Maybe.from(program);
        }
        return Maybe.none();
    }

    @Override
    public boolean canApply(Node n)
    {
        return false;
    }

    @Override
    public void visit(ProgramImpl node)
    {
        if (node.getChildren().size() >= 2)
        {
            List<Node> children = node.getChildren();
            int childPicker1 = (int) (Math.random() * children.size());
            int childPicker2 = (int) (Math.random() * children.size());
            while (childPicker1 == childPicker2)
            {
                childPicker2 = (int) (Math.random() * node.getChildren().size());
            }
            Node temp = node.getChildren().get(childPicker1);

        }
    }

    @Override
    public void visit(Rule node)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void visit(BinaryCondition node) {

    }

    @Override
    public void visit(Relation node) {

    }

    @Override
    public void visit(BinaryOp node) {

    }

    @Override
    public void visit(Number node) {

    }

    @Override
    public void visit(Mem node) {

    }

    @Override
    public void visit(NegativeExpr node) {

    }

    @Override
    public void visit(Negative node) {

    }

    @Override
    public void visit(NearbySensor node) {

    }

    @Override
    public void visit(AheadSensor node) {

    }

    @Override
    public void visit(RandomSensor node) {

    }

    @Override
    public void visit(SmellSensor node) {

    }

    @Override
    public void visit(Command node) {

    }

    @Override
    public void visit(Update node) {

    }

    @Override
    public void visit(Action node) {

    }
}
