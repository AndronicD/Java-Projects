package cool.compiler.classes;

import cool.compiler.ASTVisitor;
import cool.parser.CoolParser;
import org.antlr.v4.runtime.Token;

public class True extends ExpressionNode {
    public Boolean value = true;

    public True(CoolParser.TrueContext context, Token token) {
        super(token, context);
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
