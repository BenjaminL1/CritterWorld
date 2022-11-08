package ast;

import cms.util.maybe.Maybe;

import java.util.ArrayList;
import java.util.List;

/** A data structure representing a critter program. */
public class ProgramImpl extends AbstractNode implements Program
{
    private List<Node> rules;

    public ProgramImpl()
    {
        rules = new ArrayList<Node>();
    }

    @Override
    public Node clone()
    {
        ProgramImpl cloned = new ProgramImpl();
        for(Node rule : rules)
        {
            Rule clonedRule = (Rule) rule.clone();
            clonedRule.setParent(cloned);
            cloned.getChildren().add(clonedRule);
        }
        return cloned;
    }

    @Override
    public StringBuilder prettyPrint(StringBuilder sb)
    {
        for(Node child : getChildren())
        {
            child.prettyPrint(sb);
            sb.append(";");
            sb.append(System.lineSeparator());
        }
        return sb;
    }

    public void addRule(Rule rule)
    {
        rules.add(rule);
        rule.setParent(this);
    }

    public void replace(Rule rule, Rule newRule)
    {
        for (int i = 0; i < rules.size(); i++)
        {
            if (rules.get(i) == rule)
            {
                rules.set(i, newRule);
                rule.setParent(null);
                newRule.setParent(this);
                break;
            }
        }
    }

    @Override
    public void accept(Visitor v)
    {
        v.visit(this);
    }

    public void remove(Rule rule)
    {
        rules.remove(rule);
        rule.setParent(null);
    }

    public List<Node> getChildren()
    {
        return rules;
    }

    @Override
    public Program mutate()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Maybe<Program> mutate(int index, Mutation m)
    {
        return m.apply(this, this.nodeAt(index));
    }

    @Override
    public Maybe<Node> findNodeOfType(NodeCategory type)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public NodeCategory getCategory() {
        return NodeCategory.PROGRAM;
    }


    public boolean classInv() {
        // TODO
        return false;
    }
}
