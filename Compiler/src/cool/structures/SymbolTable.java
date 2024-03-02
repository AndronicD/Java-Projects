package cool.structures;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cool.b.A;
import cool.structures.scope.ClassScope;
import cool.structures.scope.MethodScope;
import cool.structures.symbol.IdSymbol;
import org.antlr.v4.runtime.*;

import cool.compiler.Compiler;
import cool.parser.CoolParser;

public class SymbolTable {
    public static Scope globals;
    
    private static boolean semanticErrors;

    public static String objectType = "Object";
    public static String intType = "Int";
    public static String boolType = "Bool";
    public static String stringType = "String";
    public static String ioType = "IO";
    public static String self = "self";
    public static String selfType = "SELF_TYPE";

    public static ClassScope objectClassScope = new ClassScope("Object", null);
    public static ClassScope intClassScope = new ClassScope("Int", objectClassScope);
    public static ClassScope boolClassScope = new ClassScope("Bool", objectClassScope);
    public static ClassScope ioClassScope = new ClassScope("IO", objectClassScope);
    public static ClassScope stringClassScope = new ClassScope("String", objectClassScope);
    public static ClassScope selfTypeClassScope = new ClassScope("SELF_TYPE", objectClassScope);
    public static List<Symbol> notAllowedToBeInherited = new ArrayList<>();

    // Object methods
    public static MethodScope abortMethod = new MethodScope("abort", objectClassScope, objectType);
    public static MethodScope typeNameMethod = new MethodScope("type_name", objectClassScope, objectType);
    public static MethodScope copyMethod = new MethodScope("copy", objectClassScope, objectType);

    // IO symbols
    public static IdSymbol xStr = new IdSymbol("x", "String");
    public static IdSymbol xInt = new IdSymbol("x", "Int");

    // IO methods
    public static MethodScope outStringMethod = new MethodScope("out_string", ioClassScope, selfType);
    public static MethodScope outIntMethod = new MethodScope("out_int", ioClassScope, selfType);
    public static MethodScope inStringMethod = new MethodScope("in_string", ioClassScope, stringType);
    public static MethodScope inIntMethod = new MethodScope("in_int", ioClassScope, intType);

    // String symbols
    public static IdSymbol s = new IdSymbol("s", stringType);
    public static IdSymbol i = new IdSymbol("i", intType);
    public static IdSymbol l = new IdSymbol("l", intType);

    // String methods
    public static MethodScope concatMethod = new MethodScope("concat", stringClassScope, stringType);
    public static MethodScope lengthMethod = new MethodScope("length", stringClassScope, intType);
    public static MethodScope substrMethod = new MethodScope("substr", stringClassScope, stringType);

    public static void defineBasicClasses() {
        globals = new DefaultScope(null);
        semanticErrors = false;
        
        // TODO Populate global scope.
        globals.add(objectClassScope);
        globals.add(intClassScope);
        globals.add(boolClassScope);
        globals.add(ioClassScope);
        globals.add(selfTypeClassScope);
        globals.add(stringClassScope);

        notAllowedToBeInherited.add(intClassScope);
        notAllowedToBeInherited.add(boolClassScope);
        notAllowedToBeInherited.add(stringClassScope);
        notAllowedToBeInherited.add(selfTypeClassScope);

        objectClassScope.add(abortMethod);
        objectClassScope.add(typeNameMethod);
        objectClassScope.add(copyMethod);

        ioClassScope.add(outStringMethod);
        ioClassScope.add(outIntMethod);
        ioClassScope.add(inStringMethod);
        ioClassScope.add(inIntMethod);

        stringClassScope.add(concatMethod);
        stringClassScope.add(lengthMethod);
        stringClassScope.add(substrMethod);

        abortMethod.setReturnType(objectClassScope);
        abortMethod.setResolved(true);

        typeNameMethod.setReturnType(objectClassScope);
        typeNameMethod.setResolved(true);

        copyMethod.setReturnType(objectClassScope);
        copyMethod.setResolved(true);

        outStringMethod.setReturnType(selfTypeClassScope);
        outStringMethod.setResolved(true);

        outIntMethod.setReturnType(selfTypeClassScope);
        outIntMethod.setResolved(true);

        inStringMethod.setReturnType(stringClassScope);
        inStringMethod.setResolved(true);

        inIntMethod.setReturnType(intClassScope);
        inIntMethod.setResolved(true);

        concatMethod.setReturnType(stringClassScope);
        concatMethod.setResolved(true);

        lengthMethod.setReturnType(intClassScope);
        lengthMethod.setResolved(true);

        substrMethod.setReturnType(stringClassScope);
        substrMethod.setResolved(true);

        xStr.setClassScope(stringClassScope);
        xStr.setResolved(true);

        outStringMethod.add(xStr);

        xInt.setClassScope(intClassScope);
        xInt.setResolved(true);

        outIntMethod.add(xInt);

        s.setClassScope(stringClassScope);
        s.setResolved(true);

        concatMethod.add(s);

        i.setClassScope(intClassScope);
        i.setResolved(true);

        l.setClassScope(intClassScope);
        l.setResolved(true);

        lengthMethod.add(i);
        lengthMethod.add(l);
    }
    
    /**
     * Displays a semantic error message.
     * 
     * @param ctx Used to determine the enclosing class context of this error,
     *            which knows the file name in which the class was defined.
     * @param info Used for line and column information.
     * @param str The error message.
     */
    public static void error(ParserRuleContext ctx, Token info, String str) {
        while (! (ctx.getParent() instanceof CoolParser.ProgramContext))
            ctx = ctx.getParent();
        
        String message = "\"" + new File(Compiler.fileNames.get(ctx)).getName()
                + "\", line " + info.getLine()
                + ":" + (info.getCharPositionInLine() + 1)
                + ", Semantic error: " + str;
        
        System.err.println(message);
        
        semanticErrors = true;
    }
    
    public static void error(String str) {
        String message = "Semantic error: " + str;
        
        System.err.println(message);
        
        semanticErrors = true;
    }
    
    public static boolean hasSemanticErrors() {
        return semanticErrors;
    }
}
