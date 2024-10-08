package cool.structures.scope;

import cool.compiler.classes.ClassNode;
import cool.structures.Scope;
import cool.structures.Symbol;
import cool.structures.SymbolTable;

import java.util.LinkedHashMap;

public class MethodScope extends Symbol implements Scope {
    public boolean isResolved;
    public String methodType;
    public Scope scope;
    public Symbol methodReturnType;
    public LinkedHashMap<String, Symbol> parameters;

    public MethodScope(String name, Scope scope, String type) {
        super(name);
        this.methodType = type;
        this.scope = scope;
        parameters = new LinkedHashMap<>();
    }

    @Override
    public boolean add(Symbol sym) {
        return parameters.putIfAbsent(sym.getName(), sym) == null;
    }

    @Override
    public Symbol lookup(String str) {
        Symbol sym = parameters.get(str);
        return (sym != null) ? sym : (scope != null) ? scope.lookup(str) : null;
    }

    @Override
    public Scope getParent() {
        return scope;
    }

    @Override
    public boolean addClass(ClassNode classNode) {
        return false;
    }

    @Override
    public ClassNode getClassByName(String name) {
        return null;
    }

    public String getType() {
        return methodType;
    }

    public void setType(String type) {
        this.methodType = type;
    }

    public Scope getScope() {
        return scope;
    }

    public void setScope(Scope scope) {
        this.scope = scope;
    }

    public LinkedHashMap<String, Symbol> getParameters() {
        return parameters;
    }

    public void setResolved(boolean resolved) {
        isResolved = resolved;
    }

    public Symbol getReturnType() {
        return methodReturnType;
    }

    public void setReturnType(Symbol returnType) {
        this.methodReturnType = returnType;
    }
}
