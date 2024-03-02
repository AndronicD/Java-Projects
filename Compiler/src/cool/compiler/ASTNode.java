package cool.compiler;

import com.sun.nio.file.ExtendedOpenOption;
import org.antlr.v4.runtime.Token;

import java.util.List;

public abstract class ASTNode {
    public <T> T accept(ASTVisitor<T> visitor) {
        return null;
    }
}

abstract class ProgramNode extends ASTNode {
    private List<ClassNode> classes;

    public ProgramNode(List<ClassNode> classes) {
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

abstract class ClassNode extends ASTNode {
    private String className;
    private String inheritClassName;
    private String parentClassName;
    private List<FeatureNode> features;

    public ClassNode(String className, String parentClassName, String inheritClassName, List<FeatureNode> features) {
        this.className = className;
        this.features = features;
        this.inheritClassName = inheritClassName;
        this.parentClassName = parentClassName;
    }

    public String getClassName() {
        return className;
    }

    public List<FeatureNode> getFeatures() {
        return features;
    }

    public String getInheritClassName() {return inheritClassName;}

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

abstract class FeatureNode extends ASTNode {
    private String featureId;
    private String featureType;
    private List<FormalNode> formalList;
    private List<ExpressionNode> expressions;
    private int attributeContext;
    private int methodContext;

    public int getAttributeContext() {
        return attributeContext;
    }

    public int getMethodContext() {
        return methodContext;
    }

    // Constructor for method feature
    public FeatureNode(String featureId,
                       String featureType,
                       List<FormalNode> formalListNode,
                       List<ExpressionNode> expressions,
                       int attributeContext,
                       int methodContext) {
        this.featureId = featureId;
        this.featureType = featureType;
        this.formalList = formalListNode;
        this.expressions = expressions;
        this.attributeContext = attributeContext;
        this.methodContext = methodContext;
    }

    public String getFeatureId() {
        return featureId;
    }

    public String getFeatureType() {
        return featureType;
    }

    public List<ExpressionNode> getExpressions() {
        return expressions;
    }

    public List<FormalNode> getFormalList() {
        return formalList;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class LocalNode extends ASTNode {
    public String identifier;
    public String type;
    public ExpressionNode expression;

    public LocalNode(String identifier, String type, ExpressionNode expression) {
        this.identifier = identifier;
        this.type = type;
        this.expression = expression;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

abstract class FormalNode extends ASTNode {
    private String id;
    private String type;

    public FormalNode(String id, String type) {
        this.id = id;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

abstract class ExpressionNode extends ASTNode {
    Token token;

    ExpressionNode(Token token) {
        this.token = token;
    }
}

class Dispatch extends ExpressionNode{
    String id;
    String type;
    ExpressionNode expressionNode;
    List<ExpressionNode> expressions;

    Dispatch(List<ExpressionNode> expressions, ExpressionNode experssionNode, String type, String id,Token token){
        super(token);
        this.id = id;
        this.type = type;
        this.expressionNode = experssionNode;
        this.expressions = expressions;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class ImplicitDispatch extends ExpressionNode{
    String id;
    List<ExpressionNode> expressions;

    ImplicitDispatch(List<ExpressionNode> expressions, String id, Token token){
        super(token);
        this.id = id;
        this.expressions = expressions;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class If extends ExpressionNode{
    ExpressionNode condition;
    ExpressionNode thenBranch;
    ExpressionNode elseBranch;

    If(ExpressionNode condition, ExpressionNode thenBranch, ExpressionNode elseBranch, Token token){
        super(token);
        this.condition = condition;
        this.thenBranch = thenBranch;
        this.elseBranch = elseBranch;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class While extends ExpressionNode{
    ExpressionNode condition;
    ExpressionNode body;

    While(ExpressionNode condition, ExpressionNode body, Token token){
        super(token);
        this.condition = condition;
        this.body = body;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class Block extends ExpressionNode{
    List<ExpressionNode> expressionNodes;

    Block(List<ExpressionNode> exprListNode, Token token){
        super(token);
        this.expressionNodes = exprListNode;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class Let extends ExpressionNode {
    ExpressionNode expressionNode;
    List<LocalNode> letList;

    Let(List<LocalNode> letList, ExpressionNode expressionNode, Token token){
        super(token);
        this.letList = letList;
        this.expressionNode = expressionNode;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class CaseBlock extends ExpressionNode{
    ExpressionNode condition;
    List<ExpressionNode> caseExpressions;

    CaseBlock(ExpressionNode condition, List<ExpressionNode> caseExpressions, Token token){
        super(token);
        this.condition = condition;
        this.caseExpressions = caseExpressions;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class CaseExpr extends ExpressionNode{
    String id;
    String type;
    ExpressionNode expressionNode;

    CaseExpr(ExpressionNode expressionNode, String id, String type, Token token){
        super(token);
        this.id = id;
        this.type = type;
        this.expressionNode = expressionNode;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class New extends ExpressionNode {
    String id;
    New(String id, Token token) {
        super(token);
        this.id = id;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class Relational extends ExpressionNode {
    ExpressionNode left;
    ExpressionNode right;
    String op;

    Relational(ExpressionNode left, ExpressionNode right, String op, Token token) {
        super(token);
        this.op = op;
        this.left = left;
        this.right = right;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class Arithmetic extends ExpressionNode {  // plus, minus, devide, multiply
    ExpressionNode left;
    ExpressionNode right;
    String op;

    Arithmetic(ExpressionNode left, ExpressionNode right, String op, Token token) {
        super(token);
        this.op = op;
        this.left = left;
        this.right = right;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class Unary extends ExpressionNode { //tilde, not, isvoid, paranteze, assignment_in_block
    ExpressionNode expr;
    String op;

    Unary(ExpressionNode expr, String op,Token token) {
        super(token);
        this.op = op;
        this.expr = expr;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class Assignment extends ExpressionNode{
    String id;
    ExpressionNode expressionNode;

    Assignment(ExpressionNode expressionNode, String id, Token token){
        super(token);
        this.id = id;
        this.expressionNode = expressionNode;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class Id extends ExpressionNode {
    Id(Token token) {
        super(token);
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class Int extends ExpressionNode {
    Integer value;
    Int(Token token) {
        super(token);
        value = Integer.parseInt(token.getText());
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class StringToken extends ExpressionNode {
    String value;
    StringToken(Token token) {
        super(token);
        value = token.getText();
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class True extends ExpressionNode {
    public Boolean value = true;

    True(Token token) {
        super(token);
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class Self extends ExpressionNode {
    Self(Token token) {
        super(token);
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class False extends ExpressionNode {
    public Boolean value = false;

    False(Token token) {
        super(token);
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}