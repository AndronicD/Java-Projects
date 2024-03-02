package cool.structures.scope;

import cool.compiler.classes.ClassNode;
import cool.structures.Scope;
import cool.structures.Symbol;
import cool.structures.symbol.IdSymbol;
import java.util.LinkedHashMap;

public class ClassScope extends Symbol implements Scope {
    public Scope scope;
    public LinkedHashMap<String, IdSymbol> attributes;
    public LinkedHashMap<String, MethodScope> methods;

    public ClassScope(String name, Scope scope) {
        super(name);
        this.scope = scope;
        attributes = new LinkedHashMap<>();
        methods = new LinkedHashMap<>();
    }

    @Override
    public boolean add(Symbol sym) {
        boolean isIdSymbol = (sym instanceof IdSymbol) && attributes.putIfAbsent(sym.getName(), (IdSymbol) sym) == null;
        boolean isMethodSymbol = (sym instanceof MethodScope) && methods.putIfAbsent(sym.getName(), (MethodScope) sym) == null;
        return isIdSymbol || isMethodSymbol;
    }

    @Override
    public Symbol lookup(String str) {
        Symbol sym = attributes.get(str);
        return (sym != null) ? sym : (scope != null) ? scope.lookup(str) : null;
    }

    public MethodScope lookupMethod(String str){
        Symbol sym = methods.get(str);
        return (sym != null) ? (MethodScope) sym : (scope != null) ? (MethodScope) scope.lookup(str) : null;
    }

    @Override
    public Scope getParent() {
        return scope;
    }

    public String getInheritName(){
        if(scope instanceof ClassScope){
            return ((ClassScope) scope).getName();
        }
        else{
            return null;
        }
    }

    @Override
    public boolean addClass(ClassNode classNode) {
        return false;
    }

    @Override
    public ClassNode getClassByName(String name) {
        return null;
    }
}
