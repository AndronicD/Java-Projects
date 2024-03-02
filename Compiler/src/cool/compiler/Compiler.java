package cool.compiler;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import cool.lexer.*;
import cool.parser.*;
import cool.parser.CoolParser;
import cool.parser.CoolParserBaseVisitor;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;


public class Compiler {
    // Annotates class nodes with the names of files where they are defined.
    public static ParseTreeProperty<String> fileNames = new ParseTreeProperty<>();

    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.err.println("No file(s) given");
            return;
        }
        
        CoolLexer lexer = null;
        CommonTokenStream tokenStream = null;
        CoolParser parser = null;
        ParserRuleContext globalTree = null;
        
        // True if any lexical or syntax errors occur.
        boolean lexicalSyntaxErrors = false;
        
        // Parse each input file and build one big parse tree out of
        // individual parse trees.
        for (var fileName : args) {
            var input = CharStreams.fromFileName(fileName);

            // Lexer
            if (lexer == null)
                lexer = new CoolLexer(input);
            else
                lexer.setInputStream(input);

            // Token stream
            if (tokenStream == null)
                tokenStream = new CommonTokenStream(lexer);
            else
                tokenStream.setTokenSource(lexer);


            // Test lexer only.
            tokenStream.fill();
            List<Token> tokens = tokenStream.getTokens();
            tokens.stream().forEach(token -> {
                var text = token.getText();
                var name = CoolLexer.VOCABULARY.getSymbolicName(token.getType());

                //System.out.println(text + " : " + name);
                //System.out.println(token);
            });

            // Parser
            if (parser == null)
                parser = new CoolParser(tokenStream);
            else
                parser.setTokenStream(tokenStream);

            // Customized error listener, for including file names in error
            // messages.
            var errorListener = new BaseErrorListener() {
                public boolean errors = false;

                @Override
                public void syntaxError(Recognizer<?, ?> recognizer,
                                        Object offendingSymbol,
                                        int line, int charPositionInLine,
                                        String msg,
                                        RecognitionException e) {
                    String newMsg = "\"" + new File(fileName).getName() + "\", line " +
                            line + ":" + (charPositionInLine + 1) + ", ";

                    Token token = (Token) offendingSymbol;
                    if (token.getType() == CoolLexer.ERROR)
                        newMsg += "Lexical error: " + token.getText();
                    else
                        newMsg += "Syntax error: " + msg;

                    System.err.println(newMsg);
                    errors = true;
                }
            };

            parser.removeErrorListeners();
            parser.addErrorListener(errorListener);

            // Actual parsing
            var tree = parser.program();
            if (globalTree == null)
                globalTree = tree;
            else
                // Add the current parse tree's children to the global tree.
                for (int i = 0; i < tree.getChildCount(); i++)
                    globalTree.addAnyChild(tree.getChild(i));

            // Annotate class nodes with file names, to be used later
            // in semantic error messages.
            for (int i = 0; i < tree.getChildCount(); i++) {
                var child = tree.getChild(i);
                // The only ParserRuleContext children of the program node
                // are class nodes.
                if (child instanceof ParserRuleContext)
                    fileNames.put(child, fileName);
            }

            // Record any lexical or syntax errors.
            lexicalSyntaxErrors |= errorListener.errors;

            // Stop before semantic analysis phase, in case errors occurred.
            if (lexicalSyntaxErrors) {
                System.err.println("Compilation halted");
                return;
            }

            var astConstructionVisitor = new CoolParserBaseVisitor<ASTNode>() {
                @Override
                public ASTNode visitProgram(CoolParser.ProgramContext ctx) {
                    return new ProgramNode(ctx.class_().stream().map(obj -> (ClassNode) visit(obj)).toList()) {
                    };
                }

                @Override
                public ASTNode visitClass(CoolParser.ClassContext ctx) {
                    String inherit = ctx.type == null ? null : ctx.type.getText() ;
                    return new ClassNode(ctx.ID(0).getText(),
                            ctx.parent.toString(),
                            inherit,
                            ctx.feature().stream().map(obj -> (FeatureNode) visit(obj)).toList()) {
                    };
                }

                @Override
                public ASTNode visitMethod(CoolParser.MethodContext ctx) {
                    List<FormalNode> list = null;
                    List<ExpressionNode> listExpr = new ArrayList<>();
                    ExpressionNode expr = (ExpressionNode) visit(ctx.expr());
                    listExpr.add(expr);
                    if(ctx.args != null){
                        list = ctx.args.stream().map(obj -> (FormalNode)visit(obj)).toList();
                    }
                    return new FeatureNode(ctx.ID(0).getText(),
                            ctx.type.getText(),
                            list,
                            listExpr, 0, 1) {
                    };
                }

                @Override
                public ASTNode visitAttribute(CoolParser.AttributeContext ctx) {
                    List<ExpressionNode> list = new ArrayList<>();
                    if(ctx.expr() != null) {
                        list.add((ExpressionNode) visit(ctx.expr()));
                    }
                    return new FeatureNode(ctx.ID(0).getText(),
                            ctx.type.getText(),
                            null,
                            list, 1, 0) {
                    };
                }

                @Override
                public ASTNode visitLocal(CoolParser.LocalContext ctx) {
                    return new LocalNode(ctx.ID(0).getText(),
                            ctx.type.getText(),
                            ctx.expr() == null ? null : (ExpressionNode) visit(ctx.expr())) {
                    };
                }

                @Override
                public ASTNode visitFormal(CoolParser.FormalContext ctx) {
                    return new FormalNode(ctx.ID(0).getText(), ctx.type.getText()) {
                    };
                }

                @Override
                public ASTNode visitDispatch(CoolParser.DispatchContext ctx) {
                    return new Dispatch(ctx.expr().stream().map(obj -> (ExpressionNode) visit(obj)).skip(1).toList(),
                            (ExpressionNode) visit(ctx.expr(0)),
                            ctx.type != null ? ctx.type.getText() : null,
                            ctx.id.getText(),
                            ctx.start);
                }

                @Override
                public ASTNode visitImplicit_dispatch(CoolParser.Implicit_dispatchContext ctx) {
                    return new ImplicitDispatch(ctx.expr().stream().map(obj -> (ExpressionNode) visit(obj)).toList(),
                            ctx.ID().getText(),
                            ctx.start);
                }

                @Override
                public ASTNode visitIf(CoolParser.IfContext ctx) {
                    return new If((ExpressionNode) visit(ctx.cond),
                            (ExpressionNode) visit(ctx.then),
                            (ExpressionNode) visit(ctx.else_),
                            ctx.start);
                }

                @Override
                public ASTNode visitWhile(CoolParser.WhileContext ctx) {
                    return new While((ExpressionNode) visit(ctx.cond),
                            (ExpressionNode) visit(ctx.body),
                            ctx.start);
                }

                @Override
                public ASTNode visitBlock(CoolParser.BlockContext ctx) {
                    return new Block(ctx.expr().stream().map(obj -> (ExpressionNode) visit(obj)).toList(),
                            ctx.start);
                }

                @Override
                public ASTNode visitLet(CoolParser.LetContext ctx) {
                    return new Let(ctx.local().stream().map(let -> (LocalNode) visit(let)).toList(),
                            (ExpressionNode) visit(ctx.body),
                            ctx.start);
                }

                @Override
                public ASTNode visitCase_block(CoolParser.Case_blockContext ctx) {
                    return new CaseBlock((ExpressionNode) visit(ctx.cond),
                            ctx.expr().stream().skip(1).map(obj -> (ExpressionNode) visit(obj)).toList(),
                            ctx.start);
                }

                @Override
                public ASTNode visitCase_expr(CoolParser.Case_exprContext ctx) {
                    return new CaseExpr((ExpressionNode) visit(ctx.expr()),
                            ctx.ID(0).getText(),
                            ctx.type.getText(),
                            ctx.start);
                }

                @Override
                public ASTNode visitNew(CoolParser.NewContext ctx) {
                    return new New(ctx.type.getText(),
                            ctx.start);
                }

                @Override
                public ASTNode visitIsvoid(CoolParser.IsvoidContext ctx) {
                    return new Unary((ExpressionNode) visit(ctx.expr()),
                            ctx.ISVOID().getText(),
                            ctx.start);
                }

                @Override
                public ASTNode visitMultdiv(CoolParser.MultdivContext ctx) {
                    return new Arithmetic((ExpressionNode) visit(ctx.leftExp),
                            (ExpressionNode) visit(ctx.rightExp),
                            ctx.op.getText(),
                            ctx.start);
                }

                @Override
                public ASTNode visitPlusminus(CoolParser.PlusminusContext ctx) {
                    return new Arithmetic((ExpressionNode) visit(ctx.leftExp),
                            (ExpressionNode) visit(ctx.rightExp),
                            ctx.op.getText(),
                            ctx.start);
                }

                @Override
                public ASTNode visitNegation(CoolParser.NegationContext ctx) {
                    return new Unary((ExpressionNode) visit(ctx.expr()),
                            ctx.TILDE().getText(),
                            ctx.start);
                }

                @Override
                public ASTNode visitEqual(CoolParser.EqualContext ctx) {
                    return new Relational((ExpressionNode) visit(ctx.leftExp),
                            (ExpressionNode) visit(ctx.rightExp),
                            ctx.EQUAL().getText(),
                            ctx.start);
                }

                @Override
                public ASTNode visitLess_than(CoolParser.Less_thanContext ctx) {
                    return new Relational((ExpressionNode) visit(ctx.leftExp),
                            (ExpressionNode) visit(ctx.rightExp),
                            ctx.LT().getText(),
                            ctx.start);
                }

                @Override
                public ASTNode visitLess_than_or_equal(CoolParser.Less_than_or_equalContext ctx) {
                    return new Relational((ExpressionNode) visit(ctx.leftExp),
                            (ExpressionNode) visit(ctx.rightExp),
                            ctx.LE().getText(),
                            ctx.start);
                }

                @Override
                public ASTNode visitNot(CoolParser.NotContext ctx) {
                    return new Unary((ExpressionNode) visit(ctx.expr()),
                            ctx.NOT().getText(),
                            ctx.start);
                }

                @Override
                public ASTNode visitAssignment(CoolParser.AssignmentContext ctx) {
                    return new Assignment((ExpressionNode) visit(ctx.expr()),
                            ctx.ID().getText(),
                            ctx.start);
                }

                @Override
                public ASTNode visitParentheses(CoolParser.ParenthesesContext ctx) {
                    return new Unary((ExpressionNode) visit(ctx.expr()),
                            null,
                            ctx.start);
                }

                @Override
                public ASTNode visitIdentifier(CoolParser.IdentifierContext ctx) {
                    return new Id(ctx.ID().getSymbol());
                }

                @Override
                public ASTNode visitInteger(CoolParser.IntegerContext ctx) {
                    return new Int(ctx.INT().getSymbol());
                }

                @Override
                public ASTNode visitSelf(CoolParser.SelfContext ctx) {
                    return new Self(ctx.SELF().getSymbol());
                }

                @Override
                public ASTNode visitTrue(CoolParser.TrueContext ctx) {
                    return new True(ctx.TRUE().getSymbol());
                }

                @Override
                public ASTNode visitFalse(CoolParser.FalseContext ctx) {
                    return new False(ctx.FALSE().getSymbol());
                }

                @Override
                public ASTNode visitString(CoolParser.StringContext ctx) {
                    return new StringToken(ctx.STRING().getSymbol());
                }
            };
            var ast = astConstructionVisitor.visit(tree);
            var printVisitor = new ASTVisitor<Void>() {
                int indent = 0;

                @Override
                public Void visit(Arithmetic arithmetic) {
                    printIndent(arithmetic.op);
                    indent++;
                    arithmetic.left.accept(this);
                    arithmetic.right.accept(this);
                    indent--;
                    return null;
                }

                @Override
                public Void visit(Assignment assignment) {
                    printIndent("<-");
                    indent++;
                    printIndent(assignment.id);
                    assignment.expressionNode.accept(this);
                    indent--;
                    return null;
                }

                @Override
                public Void visit(Block block) {
                    printIndent("block");
                    indent++;
                    for(ExpressionNode expr : block.expressionNodes){
                        expr.accept(this);
                    }
                    indent--;
                    return null;
                }

                @Override
                public Void visit(CaseBlock caseBlock) {
                    printIndent("case");
                    indent++;
                    caseBlock.condition.accept(this);
                    for(ExpressionNode expr : caseBlock.caseExpressions){
                        expr.accept(this);
                    }
                    indent--;
                    return null;
                }

                @Override
                public Void visit(CaseExpr caseExpr) {
                    printIndent("case branch");
                    indent++;
                    printIndent(caseExpr.id);
                    printIndent(caseExpr.type);
                    caseExpr.expressionNode.accept(this);
                    indent--;
                    return null;
                }

                @Override
                public Void visit(ClassNode classNode) {
                    printIndent("class");
                    indent++;
                    printIndent(classNode.getClassName());
                    if (classNode.getInheritClassName() != null) printIndent(classNode.getInheritClassName());
                    List<FeatureNode> featureNodes = classNode.getFeatures();
                    for(FeatureNode feature : featureNodes){
                        feature.accept(this);
                    }
                    indent--;
                    return null;
                }

                @Override
                public Void visit(Dispatch dispatch) {
                    printIndent(".");
                    indent++;
                    dispatch.expressionNode.accept(this);
                    String type = dispatch.type;
                    if (type != null)
                        printIndent(type);
                    printIndent(dispatch.id);
                    for(ExpressionNode expr : dispatch.expressions){
                        expr.accept(this);
                    }
                    indent--;
                    return null;
                }

                @Override
                public Void visit(ExpressionNode expressionNode) {
                    printIndent(expressionNode.token.getText());
                    return null;
                }

                @Override
                public Void visit(False falseToken) {
                    printIndent(falseToken.token.getText());
                    return null;
                }

                @Override
                public Void visit(FeatureNode featureNode) {
                    if(featureNode.getAttributeContext() == 1) {
                        printIndent("attribute");
                        indent++;
                        printIndent(featureNode.getFeatureId());
                        printIndent(featureNode.getFeatureType());
                        if (featureNode.getExpressions().size() > 0) {
                            featureNode.getExpressions().get(0).accept(this);
                        }
                        indent--;
                    }
                    else{
                        printIndent("method");
                        indent++;
                        printIndent(featureNode.getFeatureId());
                        if(featureNode.getFormalList() != null){
                            for(FormalNode formal : featureNode.getFormalList()){
                                formal.accept(this);
                            }
                        }
                        printIndent(featureNode.getFeatureType());
                        if (featureNode.getExpressions().size() > 0) {
                            featureNode.getExpressions().get(0).accept(this);
                        }
                        indent--;
                    }
                    return null;
                }

                @Override
                public Void visit(FormalNode formalNode) {
                    printIndent("formal");
                    indent++;
                    printIndent(formalNode.getId());
                    printIndent(formalNode.getType());
                    indent--;
                    return null;
                }

                @Override
                public Void visit(Id id) {
                    printIndent(id.token.getText());
                    return null;
                }

                @Override
                public Void visit(If ifToken) {
                    printIndent("if");
                    indent++;
                    ifToken.condition.accept(this);
                    ifToken.thenBranch.accept(this);
                    ifToken.elseBranch.accept(this);
                    indent--;
                    return null;
                }

                @Override
                public Void visit(ImplicitDispatch implicitDispatch) {
                    printIndent("implicit dispatch");
                    indent++;
                    printIndent(implicitDispatch.id);
                    for(ExpressionNode expr : implicitDispatch.expressions){
                        expr.accept(this);
                    }
                    indent--;
                    return null;
                }

                @Override
                public Void visit(Int intt) {
                    printIndent(intt.token.getText());
                    return null;
                }

                @Override
                public Void visit(Let let) {
                    printIndent("let");
                    indent++;
                    for(LocalNode local : let.letList){
                        local.accept(this);
                    }
                    let.expressionNode.accept(this);
                    indent--;
                    return null;
                }

                @Override
                public Void visit(LocalNode localNode) {
                    printIndent("local");
                    indent++;
                    printIndent(localNode.identifier);
                    printIndent(localNode.type);
                    if(localNode.expression != null) {
                        localNode.expression.accept(this);
                    }
                    indent--;
                    return null;
                }

                @Override
                public Void visit(New newToken) {
                    printIndent("new");
                    indent++;
                    printIndent(newToken.id);
                    indent--;
                    return null;
                }

                @Override
                public Void visit(ProgramNode programNode) {
                    printIndent("program");
                    List<ClassNode> classes = programNode.getClasses();
                    for(ClassNode classToken : classes){
                        indent++;
                        classToken.accept(this);
                        indent--;
                    }
                    return null;
                }

                @Override
                public Void visit(Relational relational) {
                    printIndent(relational.op);
                    indent++;
                    relational.left.accept(this);
                    relational.right.accept(this);
                    indent--;
                    return null;
                }

                @Override
                public Void visit(Self self) {
                    printIndent("self");
                    return null;
                }

                @Override
                public Void visit(StringToken stringToken) {
                    printIndent(stringToken.token.getText());
                    return null;
                }

                @Override
                public Void visit(True trueToken) {
                    printIndent(trueToken.token.getText());
                    return null;
                }

                @Override
                public Void visit(Unary unary) {
                    if(unary.op != null){
                        printIndent(unary.op);
                        indent++;
                        unary.expr.accept(this);
                        indent--;
                        return null;
                    }
                    unary.expr.accept(this);
                    return null;
                }

                @Override
                public Void visit(While whileToken) {
                    printIndent("while");
                    indent++;
                    whileToken.condition.accept(this);
                    whileToken.body.accept(this);
                    indent--;
                    return null;
                }

                void printIndent(String str) {
                    for (int i = 0; i < indent; i++)
                        System.out.print("  ");
                    System.out.println(str);
                }
            };
            ast.accept(printVisitor);
        }
    }
}
