package me.tomassetti.examples;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.ExpressionStmt;

import java.io.File;
import java.io.FileNotFoundException;

import static java.io.File.separator;

public class ModifyingCode {

    /**
     * A utility method to print a CompilationUnit preceeded by a title
     */
    private static void printCompilationUnit(String title,
                                             CompilationUnit compilationUnit) {
        System.out.println(title);
        for (int i=0; i<title.length(); i++) {
            System.out.print("=");
        }
        System.out.println();
        System.out.println(compilationUnit);
    }

    public static void main(String[] args) throws FileNotFoundException {
        // The directory where we store the examples
        String pathToExamplesDir = "." + separator + "src"
                + separator + "main" + separator + "resources";
        // Parse the code of an entire source file, a.k.a. a Compilation Unit
        CompilationUnit compilationUnitNode = StaticJavaParser.parse(new File(pathToExamplesDir
                + separator + "ASimpleClass.java"));
        printCompilationUnit("My original class", compilationUnitNode);

        // Modifying the name of the class
        compilationUnitNode.getClassByName("ASimpleClass").get()
                .setName("MyRenamedClass");
        printCompilationUnit("Renamed class", compilationUnitNode);

        // Adding a method: we add a setter
        MethodDeclaration setter = compilationUnitNode
                .getClassByName("MyRenamedClass").get()
                .addMethod("setAField", Modifier.Keyword.PUBLIC);
        setter.addParameter("boolean", "aField");
        setter.getBody().get().getStatements().add(new ExpressionStmt(
                new AssignExpr(
                    new FieldAccessExpr(new ThisExpr(),"aField"),
                new NameExpr("aField"),
                AssignExpr.Operator.ASSIGN
        )));
        printCompilationUnit("With a setter", compilationUnitNode);
    }
}
