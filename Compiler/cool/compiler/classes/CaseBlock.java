package cool.compiler.classes;

import cool.compiler.ASTVisitor;
import cool.parser.CoolParser;
import org.antlr.v4.runtime.Token;

import java.util.List;

public class CaseBlock extends ExpressionNode {
    ExpressionNode condition;
    List<ExpressionNode> caseExpressions;

    public CaseBlock(CoolParser.Case_blockContext context, ExpressionNode condition, List<ExpressionNode> caseExpressions, Token token){
        super(token, context);
        this.condition = condition;
        this.caseExpressions = caseExpressions;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public ExpressionNode getCondition() {
        return condition;
    }

    public List<ExpressionNode> getCaseExpressions() {
        return caseExpressions;
    }
}
