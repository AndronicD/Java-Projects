package cool.compiler.classes;

import cool.compiler.ASTVisitor;
import cool.parser.CoolParser;
import org.antlr.v4.runtime.Token;

import java.util.List;

public class ImplicitDispatch extends ExpressionNode {
    String id;
    List<ExpressionNode> expressions;

    public ImplicitDispatch(CoolParser.Implicit_dispatchContext context, List<ExpressionNode> expressions, String id, Token token){
        super(token, context);
        this.id = id;
        this.expressions = expressions;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
