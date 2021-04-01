public class Variable {

    private String identifier;
    private String value;
    private Type type;
    private Purpose purpose;

    public Variable(String identifier, String value, Type type, Purpose purpose) {
        this.identifier = identifier;
        this.value = value;
        this.type = type;
        this.purpose = purpose;
    }

    public Variable() {

    }

    public String getValue() {
        return value;
    }

    public String getIdentifier() {
        return identifier;
    }

    public Type getType() {
        return type;
    }

    public Purpose getPurpose() {
        return purpose;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public void setPurpose(Purpose purpose) {
        this.purpose = purpose;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void modify(Variable other, String operation) {
        if (value.isEmpty() && type.equals(other.type)) {
            value = other.getValue();
            return;
        }

        switch (type) {
            case BOOL: break;
            case DOUBLE: {
                switch (operation) {
                    case "add": {
                        value = String.valueOf(Double.parseDouble(value) + Double.parseDouble(other.value));
                        break;
                    }
                    case "subtract": {
                        value = String.valueOf(Double.parseDouble(value) - Double.parseDouble(other.value));
                        break;
                    }
                    case "multiply": {
                        value = String.valueOf(Double.parseDouble(value) * Double.parseDouble(other.value));
                        break;
                    }
                    case "divide": {
                        value = String.valueOf(Double.parseDouble(value) / Double.parseDouble(other.value));
                        break;
                    }
                }
                break;
            }
            case INTEGER: {
                switch (operation) {
                    case "add": {
                        value = String.valueOf(Integer.parseInt(value) + Integer.parseInt(other.value));
                        break;
                    }
                    case "subtract": {
                        value = String.valueOf(Integer.parseInt(value) - Integer.parseInt(other.value));
                        break;
                    }
                    case "multiply": {
                        value = String.valueOf(Integer.parseInt(value) * Integer.parseInt(other.value));
                        break;
                    }
                    case "divide": {
                        value = String.valueOf(Integer.parseInt(value) / Integer.parseInt(other.value));
                        break;
                    }
                }
                break;
            }
            case STRING: {
                value += other.value;
                break;
            }
        }
    }

    public enum Purpose {
        VARIABLE,
        FUNCTION
    }

    public enum Type {
        STRING,
        INTEGER,
        DOUBLE,
        BOOL
    }
}
