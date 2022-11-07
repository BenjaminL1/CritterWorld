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
        for(Node child : getChildren()){
            sb.append(child.prettyPrint(sb));
            sb.append(";");
            sb.append(System.lineSeparator());
        }
        return sb;
    }

    public void addRule(Rule rule)
    {
        rules.add(rule);
    }

    public void replace(Rule rule, Rule newRule)
    {
        for (int i = 0; i < rules.size(); i++)
        {
            if (rules.get(i) == rule)
            {
                rules.set(i, newRule);
                break;
            }
        }
    }

    @Override
    public void accept(Visitor v)
    {
        v.visit(this);
    }

    public int size()
    {
        return rules.size();
    }

    public void remove(Rule rule)
    {
        rules.remove(rule);
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
    public Maybe<Program> mutate(int index, Mutation m) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Maybe<Node> findNodeOfType(NodeCategory type) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public NodeCategory getCategory() {
        return NodeCategory.PROGRAM;
    }

    @Override
    public String toString(){
        // TODO Auto-generated method stub
        return null;
    }

    public boolean classInv() {
        // TODO
        return false;
    }
}
