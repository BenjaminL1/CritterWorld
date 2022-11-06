package ast;

import cms.util.maybe.Maybe;
import parse.TokenType;

import javax.naming.OperationNotSupportedException;

public class Remove extends AbstractMutation
{

    @Override
    public boolean equals(Mutation m)
    {
        return m instanceof Remove;
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
        if (n instanceof ProgramImpl || n instanceof Relation || n instanceof Number
                || n instanceof Mem || n instanceof Negative || n instanceof SmellSensor)
        {
            return false;
        }
        else if (n instanceof Rule)
        {
            return ((Rule) n).getParent().size() > 1;
        }
        else if (n instanceof Update || n instanceof Action)
        {
            Node parent = ((AbstractNode) n).getParent();
            return ((Command) parent).getChildren().size() > 1;
        }
        return true;
    }

    @Override
    public void visit(ProgramImpl node)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void visit(Rule node)
    {
        ProgramImpl parent = (ProgramImpl) node.getParent();
        if (parent.size() > 1)
        {
            parent.remove(node);
        }
    }

    @Override
    public void visit(BinaryCondition node)
    {
        Node parent = node.getParent();
        int childPicker = (int) (Math.random() * 2);
        if (parent instanceof Rule)
        {
            if (childPicker == 0)
            {
                ((Rule) parent).changeCondition(node.getLeft());
            }
            else
            {
                ((Rule) parent).changeCondition(node.getRight());
            }
        }
        if (parent instanceof BinaryCondition)
        {
            if (childPicker == 0)
            {
                if (node == ((BinaryCondition) parent).getLeft())
                {
                    ((BinaryCondition) parent).changeLeft(node.getLeft());
                }
                else
                {
                    ((BinaryCondition) parent).changeRight(node.getLeft());
                }
            }
            else
            {
                if (node == ((BinaryCondition) parent).getLeft())
                {
                    ((BinaryCondition) parent).changeLeft(node.getRight());
                }
                else
                {
                    ((BinaryCondition) parent).changeRight(node.getRight());
                }
            }
        }
    }

    @Override
    public void visit(Relation node)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void visit(BinaryOp node)
    {
        Node parent = node.getParent();
        int childPicker = (int) (Math.random() * 2);
        if (parent instanceof BinaryOp)
        {
            if (childPicker == 0)
            {
                if (node == ((BinaryOp) parent).getLeft())
                {
                    ((BinaryOp) parent).changeLeft(node.getLeft());
                }
                else
                {
                    ((BinaryOp) parent).changeRight(node.getLeft());
                }
            }
            else
            {
                if (node == ((BinaryOp) parent).getLeft())
                {
                    ((BinaryOp) parent).changeLeft(node.getRight());
                }
                else
                {
                    ((BinaryOp) parent).changeRight(node.getRight());
                }
            }
        }
        if (parent instanceof Relation)
        {
            if (childPicker == 0)
            {
                if (node == ((Relation) parent).getLeft())
                {
                    ((Relation) parent).changeLeft(node.getLeft());
                }
                else
                {
                    ((Relation) parent).changeRight(node.getLeft());
                }
            }
            else
            {
                if (node == ((Relation) parent).getLeft())
                {
                    ((Relation) parent).changeLeft(node.getRight());
                }
                else
                {
                    ((Relation) parent).changeRight(node.getRight());
                }
            }
        }
        else if (parent instanceof Update)
        {
            if (childPicker == 0)
            {
                ((Update) parent).changeExpr(node.getLeft());
            }
            else
            {
                ((Update) parent).changeExpr(node.getRight());
            }
        }
        else if (parent instanceof Action)
        {
            if (((Action) parent).getName() == TokenType.SERVE)
            {
                if (childPicker == 0)
                {
                    ((Action) parent).changeExpr(node.getLeft());
                }
                else
                {
                    ((Action) parent).changeExpr(node.getRight());
                }
            }
//            else
//            {
//                throw new UnsupportedOperationException();
//            }
        }
        else if (parent instanceof Mem)
        {
            if (childPicker == 0)
            {
                ((Mem) parent).changeExpr(node.getLeft());
            }
            else
            {
                ((Mem) parent).changeExpr(node.getRight());
            }
        }
        else if (parent instanceof Sensor)
        {
            if (parent instanceof NearbySensor)
            {
                if (childPicker == 0)
                {
                    ((NearbySensor) parent).changeExpr(node.getLeft());
                }
                else
                {
                    ((NearbySensor) parent).changeExpr(node.getRight());
                }
            }
            else if (parent instanceof AheadSensor)
            {
                if (childPicker == 0)
                {
                    ((AheadSensor) parent).changeExpr(node.getLeft());
                }
                else
                {
                    ((AheadSensor) parent).changeExpr(node.getRight());
                }
            }
            else if (parent instanceof RandomSensor)
            {
                if (childPicker == 0)
                {
                    ((RandomSensor) parent).changeExpr(node.getLeft());
                }
                else
                {
                    ((RandomSensor) parent).changeExpr(node.getRight());
                }
            }
        }
    }

    @Override
    public void visit(Number node)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void visit(Mem node)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void visit(NegativeExpr node)
    {
        Node parent = node.getParent();
        if (parent instanceof BinaryOp)
        {
            if (node == ((BinaryOp) parent).getLeft())
            {
                ((BinaryOp) parent).changeLeft(node.getRight());
            }
            else
            {
                ((BinaryOp) parent).changeRight(node.getRight());
            }
        }
        if (parent instanceof Relation)
        {
            if (node == ((Relation) parent).getLeft())
            {
                ((Relation) parent).changeLeft(node.getRight());
            }
            else
            {
                ((Relation) parent).changeRight(node.getRight());
            }
        }
        else if (parent instanceof Update)
        {
            ((Update) parent).changeExpr(node.getRight());
        }
        else if (parent instanceof Action)
        {
            if (((Action) parent).getName() == TokenType.SERVE)
            {
                ((Action) parent).changeExpr(node.getRight());
            }
            else
            {
                throw new UnsupportedOperationException();
            }
        }
        else if (parent instanceof Mem)
        {
            ((Mem) parent).changeExpr(node.getRight());
        }
        else if (parent instanceof Sensor)
        {
            if (parent instanceof NearbySensor)
            {
                ((NearbySensor) parent).changeExpr(node.getRight());
            }
            else if (parent instanceof AheadSensor)
            {
                ((AheadSensor) parent).changeExpr(node.getRight());
            }
            else if (parent instanceof RandomSensor)
            {
                ((RandomSensor) parent).changeExpr(node.getRight());
            }
        }
    }

    @Override
    public void visit(Negative node)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void visit(NearbySensor node)
    {
        Node parent = node.getParent();
        if (parent instanceof BinaryOp)
        {
            if (node == ((BinaryOp) parent).getLeft())
            {
                ((BinaryOp) parent).changeLeft(node.getExpr());
            }
            else
            {
                ((BinaryOp) parent).changeRight(node.getExpr());
            }
        }
        if (parent instanceof Relation)
        {
            if (node == ((Relation) parent).getLeft())
            {
                ((Relation) parent).changeLeft(node.getExpr());
            }
            else
            {
                ((Relation) parent).changeRight(node.getExpr());
            }
        }
        else if (parent instanceof Update)
        {
            ((Update) parent).changeExpr(node.getExpr());
        }
        else if (parent instanceof Action)
        {
            if (((Action) parent).getName() == TokenType.SERVE)
            {
                ((Action) parent).changeExpr(node.getExpr());
            }
            else
            {
                throw new UnsupportedOperationException();
            }
        }
        else if (parent instanceof Mem)
        {
            ((Mem) parent).changeExpr(node.getExpr());
        }
        else if (parent instanceof Sensor)
        {
            if (parent instanceof NearbySensor)
            {
                ((NearbySensor) parent).changeExpr(node.getExpr());
            }
            else if (parent instanceof AheadSensor)
            {
                ((AheadSensor) parent).changeExpr(node.getExpr());
            }
            else if (parent instanceof RandomSensor)
            {
                ((RandomSensor) parent).changeExpr(node.getExpr());
            }
        }
    }

    @Override
    public void visit(AheadSensor node)
    {
        Node parent = node.getParent();
        if (parent instanceof BinaryOp)
        {
            if (node == ((BinaryOp) parent).getLeft())
            {
                ((BinaryOp) parent).changeLeft(node.getExpr());
            }
            else
            {
                ((BinaryOp) parent).changeRight(node.getExpr());
            }
        }
        if (parent instanceof Relation)
        {
            if (node == ((Relation) parent).getLeft())
            {
                ((Relation) parent).changeLeft(node.getExpr());
            }
            else
            {
                ((Relation) parent).changeRight(node.getExpr());
            }
        }
        else if (parent instanceof Update)
        {
            ((Update) parent).changeExpr(node.getExpr());
        }
        else if (parent instanceof Action)
        {
            if (((Action) parent).getName() == TokenType.SERVE)
            {
                ((Action) parent).changeExpr(node.getExpr());
            }
            else
            {
                throw new UnsupportedOperationException();
            }
        }
        else if (parent instanceof Mem)
        {
            ((Mem) parent).changeExpr(node.getExpr());
        }
        else if (parent instanceof Sensor)
        {
            if (parent instanceof NearbySensor)
            {
                ((NearbySensor) parent).changeExpr(node.getExpr());
            }
            else if (parent instanceof AheadSensor)
            {
                ((AheadSensor) parent).changeExpr(node.getExpr());
            }
            else if (parent instanceof RandomSensor)
            {
                ((RandomSensor) parent).changeExpr(node.getExpr());
            }
        }
    }

    @Override
    public void visit(RandomSensor node)
    {
        Node parent = node.getParent();
        if (parent instanceof BinaryOp)
        {
            if (node == ((BinaryOp) parent).getLeft())
            {
                ((BinaryOp) parent).changeLeft(node.getExpr());
            }
            else
            {
                ((BinaryOp) parent).changeRight(node.getExpr());
            }
        }
        if (parent instanceof Relation)
        {
            if (node == ((Relation) parent).getLeft())
            {
                ((Relation) parent).changeLeft(node.getExpr());
            }
            else
            {
                ((Relation) parent).changeRight(node.getExpr());
            }
        }
        else if (parent instanceof Update)
        {
            ((Update) parent).changeExpr(node.getExpr());
        }
        else if (parent instanceof Action)
        {
            if (((Action) parent).getName() == TokenType.SERVE)
            {
                ((Action) parent).changeExpr(node.getExpr());
            }
            else
            {
                throw new UnsupportedOperationException();
            }
        }
        else if (parent instanceof Mem)
        {
            ((Mem) parent).changeExpr(node.getExpr());
        }
        else if (parent instanceof Sensor)
        {
            if (parent instanceof NearbySensor)
            {
                ((NearbySensor) parent).changeExpr(node.getExpr());
            }
            else if (parent instanceof AheadSensor)
            {
                ((AheadSensor) parent).changeExpr(node.getExpr());
            }
            else if (parent instanceof RandomSensor)
            {
                ((RandomSensor) parent).changeExpr(node.getExpr());
            }
        }
    }

    @Override
    public void visit(SmellSensor node)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void visit(Command node)
    {
        Node parent = node.getParent();
        int childPicker = (int) (Math.random() * node.getChildren().size());
        Command child = new Command();
        child.add(node.getChildren().get(childPicker));
        ((Rule) parent).changeCommand(child);
    }

    @Override
    public void visit(Update node)
    {
        Node parent = node.getParent();
        if (((Command) parent).getChildren().size() > 1)
        {
            ((Command) parent).remove(node);
        }
    }

    @Override
    public void visit(Action node)
    {
        Node parent = node.getParent();
        if (((Command) parent).getChildren().size() > 1)
        {
            ((Command) parent).remove(node);
        }
    }
}
