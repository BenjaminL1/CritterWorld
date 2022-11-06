package ast;

import cms.util.maybe.Maybe;

public class Transform extends AbstractMutation
{

    @Override
    public boolean equals(Mutation m) {
        return false;
    }

    @Override
    public Maybe<Program> apply(Program program, Node node) {
        return null;
    }

    @Override
    public boolean canApply(Node n) {
        return false;
    }

    @Override
    public void visit(ProgramImpl node) {

    }

    @Override
    public void visit(Rule node) {

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
