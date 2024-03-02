package cool.compiler.classes;

import cool.compiler.ASTVisitor;
import cool.parser.CoolParser;
import org.antlr.v4.runtime.Token;

public class StringToken extends ExpressionNode {
    String value;
    public StringToken(CoolParser.StringContext context, Token token) {
        super(token, context);
        value = token.getText();
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
