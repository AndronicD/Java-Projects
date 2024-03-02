package cool.compiler;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

public abstract class ASTNode {
    public final ParserRuleContext context;
    public final Token token;

    public ASTNode(Token token, ParserRuleContext context){
        this.context = context;
        this.token = token;
    }

    public ParserRuleContext getContext() {
        return context;
    }

    public Token getToken(){
        return token;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return null;
    }
}
