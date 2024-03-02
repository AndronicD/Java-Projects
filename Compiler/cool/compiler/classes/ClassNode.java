package cool.compiler.classes;

import cool.compiler.ASTNode;
import cool.compiler.ASTVisitor;
import cool.parser.CoolParser;
import cool.structures.scope.ClassScope;
import org.antlr.v4.runtime.Token;

import java.util.List;

public abstract class ClassNode extends ASTNode {
    private Token className;
    private Token inheritClassName;
    private List<FeatureNode> features;

    private ClassScope classScope;

    public ClassNode(CoolParser.ClassContext context, Token className, Token inheritClassName, List<FeatureNode> features, Token token) {
        super(token, context);
        this.className = className;
        this.features = features;
        this.inheritClassName = inheritClassName;
    }

    public Token getClassName() {
        return className;
    }

    public List<FeatureNode> getFeatures() {
        return features;
    }

    public Token getInheritClassName() {return inheritClassName;}

    public ClassScope getClassScope() {return classScope;}

    public void setClassScope(ClassScope classScope) {this.classScope = classScope;}

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
