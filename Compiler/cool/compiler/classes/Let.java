package cool.compiler.classes;

import cool.compiler.ASTVisitor;
import cool.parser.CoolParser;
import org.antlr.v4.runtime.Token;

import java.util.List;

public class Let extends ExpressionNode {
    ExpressionNode expressionNode;
    List<LocalNode> letList;

    public Let(CoolParser.LetContext context, List<LocalNode> letList, ExpressionNode expressionNode, Token token){
        super(token, context);
        this.letList = letList;
        this.expressionNode = expressionNode;
    }

    public ExpressionNode getExpressionNode() {
        return expressionNode;
    }

    public List<LocalNode> getLetList() {
        return letList;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
