package cool.compiler.classes;

import cool.compiler.ASTNode;
import cool.compiler.ASTVisitor;
import cool.parser.CoolParser;
import org.antlr.v4.runtime.Token;

import java.util.List;

public abstract class ProgramNode extends ASTNode {
    private final List<ClassNode> classes;

    public ProgramNode(CoolParser.ProgramContext context, List<ClassNode> classes, Token token) {
        super(token, context);
        this.classes = classes;
    }

    public List<ClassNode> getClasses() {
        return classes;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
