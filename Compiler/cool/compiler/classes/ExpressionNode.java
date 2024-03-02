package cool.compiler.classes;

import cool.compiler.ASTNode;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

public abstract class ExpressionNode extends ASTNode {
    public ExpressionNode(Token token, ParserRuleContext context) {
        super(token, context);
    }
}
