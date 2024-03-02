package cool.compiler;

import cool.compiler.classes.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import cool.lexer.*;
import cool.parser.*;
import cool.structures.SymbolTable;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


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
//            tokenStream.fill();
//            List<Token> tokens = tokenStream.getTokens();
//            tokens.stream().forEach(token -> {
//                var text = token.getText();
//                var name = CoolLexer.VOCABULARY.getSymbolicName(token.getType());
//
//                //System.out.println(text + " : " + name);
//                //System.out.println(token);
//            });


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
            //lexicalSyntaxErrors |= errorListener.errors;

        }

            var astConstructionVisitor = new CoolParserBaseVisitor<ASTNode>() {
                @Override
                public ASTNode visitProgram(CoolParser.ProgramContext ctx) {
                    return new ProgramNode(ctx, ctx.class_().stream().map(obj -> (ClassNode) visit(obj)).toList(), ctx.start) {
                    };
                }

                @Override
                public ASTNode visitClass(CoolParser.ClassContext ctx) {
                    Token inherit = ctx.inherit == null ? null : ctx.inherit ;
                    return new ClassNode(ctx, ctx.type,
                            inherit,
                            ctx.feature().stream().map(obj -> (FeatureNode) visit(obj)).toList(), ctx.type) {
                    };
                }

                @Override
                public ASTNode visitMethod(CoolParser.MethodContext ctx) {
                    List<FormalNode> list = null;
                    List<ExpressionNode> listExpr = new ArrayList<>();
                    ExpressionNode expr = (ExpressionNode) visit(ctx.expr());
                    listExpr.add(expr);
                    if(ctx.formalParams != null){
                        list = ctx.formalParams.stream().map(obj -> (FormalNode)visit(obj)).toList();
                    }
                    return new FeatureNode(ctx, ctx.id,
                            ctx.type,
                            list,
                            listExpr, 0, 1, ctx.id) {
                    };
                }

                @Override
                public ASTNode visitAttribute(CoolParser.AttributeContext ctx) {
                    List<ExpressionNode> list = new ArrayList<>();
                    if(ctx.expr() != null) {
                        list.add((ExpressionNode) visit(ctx.expr()));
                    }
                    return new FeatureNode(ctx, ctx.id,
                            ctx.type,
                            null,
                            list, 1, 0, ctx.start) {
                    };
                }

                @Override
                public ASTNode visitLocal(CoolParser.LocalContext ctx) {
                    return new LocalNode(ctx, ctx.ID(0).getSymbol(),
                            ctx.type,
                            ctx.expr() == null ? null : (ExpressionNode) visit(ctx.expr()), ctx.start) {
                    };
                }

                @Override
                public ASTNode visitFormal(CoolParser.FormalContext ctx) {
                    return new FormalNode(ctx, ctx.id, ctx.type, ctx.start) {
                    };
                }

                @Override
                public ASTNode visitDispatch(CoolParser.DispatchContext ctx) {
                    return new Dispatch(ctx, ctx.expr().stream().map(obj -> (ExpressionNode) visit(obj)).skip(1).toList(),
                            (ExpressionNode) visit(ctx.expr(0)),
                            ctx.type != null ? ctx.type.getText() : null,
                            ctx.id.getText(),
                            ctx.start);
                }

                @Override
                public ASTNode visitImplicit_dispatch(CoolParser.Implicit_dispatchContext ctx) {
                    return new ImplicitDispatch(ctx, ctx.expr().stream().map(obj -> (ExpressionNode) visit(obj)).toList(),
                            ctx.ID().getText(),
                            ctx.start);
                }

                @Override
                public ASTNode visitIf(CoolParser.IfContext ctx) {
                    return new If(ctx, ctx.cond.start, (ExpressionNode) visit(ctx.cond),
                            (ExpressionNode) visit(ctx.then),
                            (ExpressionNode) visit(ctx.else_),
                            ctx.IF().getSymbol());
                }

                @Override
                public ASTNode visitWhile(CoolParser.WhileContext ctx) {
                    return new While(ctx, ctx.cond.start, (ExpressionNode) visit(ctx.cond),
                            (ExpressionNode) visit(ctx.body),
                            ctx.WHILE().getSymbol());
                }

                @Override
                public ASTNode visitBlock(CoolParser.BlockContext ctx) {
                    return new Block(ctx, ctx.expr().stream().map(obj -> (ExpressionNode) visit(obj)).toList(),
                            ctx.start);
                }

                @Override
                public ASTNode visitLet(CoolParser.LetContext ctx) {
                    return new Let(ctx, ctx.local().stream().map(let -> (LocalNode) visit(let)).toList(),
                            (ExpressionNode) visit(ctx.body),
                            ctx.LET().getSymbol());
                }

                @Override
                public ASTNode visitCase_block(CoolParser.Case_blockContext ctx) {
                    return new CaseBlock(ctx, (ExpressionNode) visit(ctx.cond),
                            ctx.expr().stream().skip(1).map(obj -> (ExpressionNode) visit(obj)).toList(),
                            ctx.CASE().getSymbol());
                }

                @Override
                public ASTNode visitCase_expr(CoolParser.Case_exprContext ctx) {
                    return new CaseExpr(ctx, (ExpressionNode) visit(ctx.expr()),
                            ctx.ID(0).getSymbol(),
                            ctx.type,
                            ctx.start);
                }

                @Override
                public ASTNode visitNew(CoolParser.NewContext ctx) {
                    return new New(ctx, ctx.type,
                            ctx.start);
                }

                @Override
                public ASTNode visitIsvoid(CoolParser.IsvoidContext ctx) {
                    return new Unary(ctx, (ExpressionNode) visit(ctx.expr()),
                            ctx.ISVOID().getSymbol(),
                            ctx.ISVOID().getSymbol());
                }

                @Override
                public ASTNode visitMultdiv(CoolParser.MultdivContext ctx) {
                    return new Arithmetic(ctx, (ExpressionNode) visit(ctx.leftExp),
                            (ExpressionNode) visit(ctx.rightExp),
                            ctx.op,
                            ctx.op.getText().equals("*") ? ctx.MULTIPLY().getSymbol() : ctx.DIVIDE().getSymbol());
                }

                @Override
                public ASTNode visitPlusminus(CoolParser.PlusminusContext ctx) {
                    return new Arithmetic(ctx, (ExpressionNode) visit(ctx.leftExp),
                            (ExpressionNode) visit(ctx.rightExp),
                            ctx.op,
                            ctx.op.getText().equals("+") ? ctx.PLUS().getSymbol() : ctx.MINUS().getSymbol());
                }

                @Override
                public ASTNode visitNegation(CoolParser.NegationContext ctx) {
                    return new Unary(ctx, (ExpressionNode) visit(ctx.expr()),
                            ctx.TILDA().getSymbol(),
                            ctx.TILDA().getSymbol());
                }

                @Override
                public ASTNode visitEqual(CoolParser.EqualContext ctx) {
                    return new Relational(ctx, (ExpressionNode) visit(ctx.leftExp),
                            (ExpressionNode) visit(ctx.rightExp),
                            ctx.EQ().getSymbol(),
                            ctx.EQ().getSymbol());
                }

                @Override
                public ASTNode visitLess_than(CoolParser.Less_thanContext ctx) {
                    return new Relational(ctx, (ExpressionNode) visit(ctx.leftExp),
                            (ExpressionNode) visit(ctx.rightExp),
                            ctx.LT().getSymbol(),
                            ctx.LT().getSymbol());
                }

                @Override
                public ASTNode visitLess_than_or_equal(CoolParser.Less_than_or_equalContext ctx) {
                    return new Relational(ctx, (ExpressionNode) visit(ctx.leftExp),
                            (ExpressionNode) visit(ctx.rightExp),
                            ctx.LE().getSymbol(),
                            ctx.LE().getSymbol());
                }

                @Override
                public ASTNode visitNot(CoolParser.NotContext ctx) {
                    return new Unary(ctx, (ExpressionNode) visit(ctx.expr()),
                            ctx.NOT().getSymbol(),
                            ctx.NOT().getSymbol());
                }

                @Override
                public ASTNode visitAssignment(CoolParser.AssignmentContext ctx) {
                    return new Assignment(ctx, (ExpressionNode) visit(ctx.expr()),
                            ctx.ID().getSymbol(),
                            ctx.ASSIGN().getSymbol());
                }

                @Override
                public ASTNode visitParentheses(CoolParser.ParenthesesContext ctx) {
                    return new Unary(ctx, (ExpressionNode) visit(ctx.expr()),
                            ctx.start,
                            ctx.start);
                }

                @Override
                public ASTNode visitIdentifier(CoolParser.IdentifierContext ctx) {
                    return new Id(ctx, ctx.ID().getSymbol());
                }

                @Override
                public ASTNode visitInteger(CoolParser.IntegerContext ctx) {
                    return new Int(ctx, ctx.INT().getSymbol());
                }

                @Override
                public ASTNode visitTrue(CoolParser.TrueContext ctx) {
                    return new True(ctx, ctx.TRUE().getSymbol());
                }

                @Override
                public ASTNode visitFalse(CoolParser.FalseContext ctx) {
                    return new False(ctx, ctx.FALSE().getSymbol());
                }

                @Override
                public ASTNode visitString(CoolParser.StringContext ctx) {
                    return new StringToken(ctx, ctx.STRING().getSymbol());
                }
            };

            var ast = astConstructionVisitor.visit(globalTree);

            // Populate global scope.
            SymbolTable.defineBasicClasses();

            //ast.accept(printVisitor);
            ast.accept(new DefinitionPassVisitor());
            ast.accept(new ResolutionPassVisitor());

            if (SymbolTable.hasSemanticErrors()) {
                System.err.println("Compilation halted");
                return;
            }
        }
    }
