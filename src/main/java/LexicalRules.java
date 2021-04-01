import java.util.ArrayList;
import java.util.List;

public class LexicalRules {
    public static final List<String> LITERALS = new ArrayList<>();
    public static final List<String> TYPES = new ArrayList<>();
    public static final List<String> CONDITIONAL_OPERATORS = new ArrayList<>();
    public static final List<String> ARITHMETIC_OPERATORS = new ArrayList<>();
    public static final List<String> VARIABlES = new ArrayList<>();

    public static void init() {
        LITERALS.add("literal_integer");
        LITERALS.add("literal_double");
        LITERALS.add("literal_bool");
        LITERALS.add("literal_string");
        LITERALS.add("literal_import");

        TYPES.add("void");
        TYPES.add("integer");
        TYPES.add("double");
        TYPES.add("bool");
        TYPES.add("string");

        CONDITIONAL_OPERATORS.add("less/equal");
        CONDITIONAL_OPERATORS.add("more/equal");
        CONDITIONAL_OPERATORS.add("equal");
        CONDITIONAL_OPERATORS.add("not equal");
        CONDITIONAL_OPERATORS.add("less");
        CONDITIONAL_OPERATORS.add("more");

        ARITHMETIC_OPERATORS.add("add");
        ARITHMETIC_OPERATORS.add("subtract");
        ARITHMETIC_OPERATORS.add("multiply");
        ARITHMETIC_OPERATORS.add("divide");
        ARITHMETIC_OPERATORS.add("exponent");

        VARIABlES.add("identifier");
        VARIABlES.add("literal_integer");
        VARIABlES.add("literal_double");
        VARIABlES.add("literal_string");
    }
}
