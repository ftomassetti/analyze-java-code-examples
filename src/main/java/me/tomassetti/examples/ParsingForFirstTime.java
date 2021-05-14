package me.tomassetti.examples;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.Expression;

import java.util.stream.Collectors;

public class ParsingForFirstTime {

    public static void main(String[] args) {
        // Parse an expression
        Expression expressionNode = StaticJavaParser.parseExpression("1 + 2");

        // Parse a body declaration: it could be either a field or a method or an inner class
        BodyDeclaration<?> methodNode = StaticJavaParser.parseBodyDeclaration(
                "boolean invert(boolean aFlag) { return !aFlag; }");

        // Parse the code of an entire source file, a.k.a. a Compilation Unit
        CompilationUnit compilationUnitNode = StaticJavaParser.parse("class A { int aField; }");

        // If we know the expression is a binary expression we can cast it and access more
        // specific information like the element on the left and on the right and the operator
        BinaryExpr binaryExpr = (BinaryExpr)expressionNode;
        System.out.println(String.format("Binary expression: left=%s, right=%s, operator=%s",
                binaryExpr.getLeft(), binaryExpr.getRight(), binaryExpr.getOperator()));

        // Here we know we have a method declaration. We may want to figure out specific
        // things like the name or the return type of the method.
        // We transform the parameters to get only the names: we are not interested in
        // in printing the whole nodes corresponding to the parameters
        MethodDeclaration methodDeclaration = (MethodDeclaration)methodNode;
        System.out.println(String.format("Method declaration: modifiers=%s, name=%s, parameters=%s, returnType=%s",
                methodDeclaration.getModifiers(), methodDeclaration.getName(),
                methodDeclaration.getParameters().stream().map(p -> p.getName()).collect(Collectors.toList()),
                methodDeclaration.getType()));

        // We can navigate the compilation unit to extract a single class. In this case
        // the CompilationUnit contains only this class but in general it could contains
        // a package declaration, imports and several type declarations
        ClassOrInterfaceDeclaration classDeclaration = compilationUnitNode.getClassByName("A").get();
        System.out.println(String.format("Class declaration: name=%s, nMembers=%s",
                classDeclaration.getName(), classDeclaration.getMembers().size()));
    }
}
