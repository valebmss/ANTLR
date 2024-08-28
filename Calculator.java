import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import java.util.HashMap;
import java.util.Map;

public class Calculator {
    public static class EvalVisitor extends CalculatorBaseVisitor<Integer> {
        Map<String, Integer> memory = new HashMap<>();

        @Override
        public Integer visitAssign(CalculatorParser.AssignContext ctx) {
            String id = ctx.ID().getText();
            int value = visit(ctx.expr());
            memory.put(id, value);
            return value;
        }

        @Override
        public Integer visitPrintExpr(CalculatorParser.PrintExprContext ctx) {
            Integer value = visit(ctx.expr());
            System.out.println("Resultado: " + value);
            return 0;
        }

        @Override
        public Integer visitInt(CalculatorParser.IntContext ctx) {
            return Integer.valueOf(ctx.INT().getText());
        }

        @Override
        public Integer visitId(CalculatorParser.IdContext ctx) {
            String id = ctx.ID().getText();
            if (memory.containsKey(id)) return memory.get(id);
            System.err.println("Error: Identificador no encontrado: " + id);
            return 0;
        }

        @Override
        public Integer visitMulDiv(CalculatorParser.MulDivContext ctx) {
            int left = visit(ctx.expr(0));
            int right = visit(ctx.expr(1));
            if (ctx.op.getText().equals("*")) return left * right;
            if (right == 0) {
                System.err.println("Error: División por cero.");
                return 0;
            }
            return left / right;
        }

        @Override
        public Integer visitAddSub(CalculatorParser.AddSubContext ctx) {
            int left = visit(ctx.expr(0));
            int right = visit(ctx.expr(1));
            if (ctx.op.getText().equals("+")) return left + right;
            return left - right;
        }

        @Override
        public Integer visitParens(CalculatorParser.ParensContext ctx) {
            return visit(ctx.expr());
        }
    }

    public static void main(String[] args) {
        System.out.println("Bienvenido a la Calculadora Simple");
        System.out.println("Introduce una expresión matemática y presiona Enter.");
        System.out.println("Ejemplo: 3 + 4 * 2");
        System.out.print("> ");

        try {
            // Configuración de ANTLR
            CharStream input = CharStreams.fromStream(System.in);
            CalculatorLexer lexer = new CalculatorLexer(input);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            CalculatorParser parser = new CalculatorParser(tokens);

            // Procesamiento del árbol de análisis
            ParseTree tree = parser.prog();
            EvalVisitor eval = new EvalVisitor();
            eval.visit(tree);
        } catch (Exception e) {
            System.err.println("Error al procesar la entrada: " + e.getMessage());
        }
    }
}
