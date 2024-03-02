package cool.compiler.classes;

import cool.compiler.ASTVisitor;
import cool.parser.CoolParser;
import org.antlr.v4.runtime.Token;

import java.util.List;

public class Dispatch extends ExpressionNode {
    String id;
    String type;
    ExpressionNode expressionNode;
    List<ExpressionNode> expressions;

    public Dispatch(CoolParser.DispatchContext context, List<ExpressionNode> expressions, ExpressionNode experssionNode, String type, String id, Token token){
        super(token, context);
        this.id = id;
        this.type = type;
        this.expressionNode = experssionNode;
        this.expressions = expressions;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
