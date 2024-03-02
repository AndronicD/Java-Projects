package cool.compiler.classes;

import cool.compiler.ASTVisitor;
import cool.parser.CoolParser;
import org.antlr.v4.runtime.Token;

public class Int extends ExpressionNode {
    Integer value;
    public Int(CoolParser.IntegerContext context, Token token) {
        super(token, context);
        value = Integer.parseInt(token.getText());
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
