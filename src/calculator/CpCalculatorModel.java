
package calculator;

/**
 * @author Callan Fox - Student Number 92016097
 */
public class CpCalculatorModel {
    private double primaryValue;
    private double newValue;
    private String operator;
    private String nextOperator;
    
    public boolean isPrimaryValueSet = false;
    public boolean isNewValueSet = false;
    private boolean isOperatorSet = false;
    
    public void setPrimaryValue(double value) {
        primaryValue = value;
    }
    
    public double getPrimaryValue() {
        return primaryValue;
    }
    
    public void setNewValue(double value) {
        newValue = value;
    }
    
    public double getNewValue() {
        return newValue;
    }
    
    public void setOperator(String op) {
        operator = op;
    }
    
    public String getOperator() {
        return operator;
    }
    
    public void setNextOperator(String op) {
        nextOperator = op;
    }
    
    public String getNextOperator() {
        return nextOperator;
    }
    
    public void setPrimaryValueSet(boolean value) {
        isPrimaryValueSet = value;
    }
    
    public boolean getPrimaryValueSet() {
        return isPrimaryValueSet;
    }
    
    public void setNewValueSet(boolean value) {
        isNewValueSet = value;
    }
    
    public boolean getNewValueSet() {
        return isNewValueSet;
    }
    
    public void setOperatorSet(boolean value) {
        isOperatorSet = value;
    }
    
    public boolean getOperatorSet() {
        return isOperatorSet;
    }
    
    public String CA() {
        primaryValue = 0.0;
        newValue = 0.0;
        operator = null;
        nextOperator = null;

        isPrimaryValueSet = false;
        isNewValueSet = false;
        isOperatorSet = false;
        return "0";
    }
    
    public String CE() {
        newValue = 0.0;
        isNewValueSet = false;
        return "0";
    }
    
    public double calculate() throws ArithmeticException {
        if (operator.equals("+")) {
            primaryValue += newValue;
            newValue = 0.0;
            isNewValueSet = false;
            operator = nextOperator;
            isOperatorSet = true;
        }
        else if (operator.equals("-")) {
            primaryValue -= newValue;
            newValue = 0.0;
            isNewValueSet = false;
            operator = nextOperator;
            isOperatorSet = true;
        }
        else if (operator.equals("*")) {
            primaryValue *= newValue;
            newValue = 0.0;
            isNewValueSet = false;
            operator = nextOperator;
            isOperatorSet = true;
        }
        else if (operator.equals("/")) {
            primaryValue /= newValue;
            if(primaryValue == Double.POSITIVE_INFINITY)  {
                throw new java.lang.ArithmeticException();
            }
            else {
                newValue = 0.0;
                isNewValueSet = false;
                operator = nextOperator;
                isOperatorSet = true;
            }
        }
        
        else if (operator.equals("^")) {
            primaryValue = exponent();
            newValue = 0.0;
            isNewValueSet = false;
            operator = nextOperator;
            isOperatorSet = true;
        }
        else if (operator.equals("=")) {
            isNewValueSet = false;
            isOperatorSet = false;
        }
        return primaryValue;
    }
    
    public double plusMinus(String value) {
        if(!isPrimaryValueSet) {
            primaryValue = Double.parseDouble(value);
            primaryValue -= (primaryValue * 2);
            return primaryValue;
        }
        else {
            newValue = Double.parseDouble(value);
            newValue -= (newValue * 2);
            return newValue;
        }
    }
    
    
    public double reciprocal (String value){
        primaryValue = Double.parseDouble(value);
        primaryValue = 1/primaryValue;
        if(primaryValue == Double.POSITIVE_INFINITY) {
            throw new java.lang.ArithmeticException();
        }
        return primaryValue;
    }
    
    public double exponent() {
        double initialValue = primaryValue; 
        if ( newValue > 0) {
            for(int i = 1; i < newValue; i++) {
                primaryValue *= initialValue;
            }
        }
        else if ( newValue < 0) {
            double value = 1;
            for(int i = 0; i > newValue; i--) {
                value /= initialValue;
            }
            primaryValue = value;
        }
        else {
            primaryValue = 1;
        }
        return primaryValue;
    }
    
    public double factorial(String value) throws NumberFormatException{
        primaryValue = Double.parseDouble(value);
        if(primaryValue != 0.0) {
            if(primaryValue < 0 || primaryValue > 20) {
                throw new java.lang.ArithmeticException();
            }
            int counter = Integer.parseInt(value) - 1;
            for(; counter > 0; counter--) {
                primaryValue *= counter;
            }
        }
        else {
            primaryValue = 1;
        }
        return primaryValue;
    }
    
    public double sin(String value, boolean isDegreesSelected) {
        primaryValue = Double.parseDouble(value);
        if (isDegreesSelected) {
            primaryValue = Math.sin(Math.toRadians(primaryValue));
        }
        else {
            primaryValue = Math.sin(primaryValue);
        }
        return primaryValue;
    }
    
    public double aSin(String value, boolean isDegreesSelected) {
        primaryValue = Double.parseDouble(value);
        if (isDegreesSelected) {
            primaryValue = Math.toDegrees(Math.asin(primaryValue));
        }
        else {
            primaryValue = Math.asin(primaryValue);
        }
        return primaryValue;
    }
    
    public double cos(String value, boolean isDegreesSelected) {
        primaryValue = Double.parseDouble(value);
        if (isDegreesSelected) {
            primaryValue = Math.cos(Math.toRadians(primaryValue));
        }
        else {
            primaryValue = Math.cos(primaryValue);
        }
        return primaryValue;
    }
    
    public double aCos(String value, boolean isDegreesSelected) {
        primaryValue = Double.parseDouble(value);
        if (isDegreesSelected) {
            primaryValue = Math.toDegrees(Math.acos(primaryValue));
        }
        else {
            primaryValue = Math.acos(primaryValue);
        }
        return primaryValue;
    }
    
    public double tan(String value, boolean isDegreesSelected) {
        primaryValue = Double.parseDouble(value);
        if (isDegreesSelected) {
            primaryValue = Math.tan(Math.toRadians(primaryValue));
        }
        else {
            primaryValue = Math.tan(primaryValue);
        }
        return primaryValue;
    }
    
    public double aTan(String value, boolean isDegreesSelected) {
        primaryValue = Double.parseDouble(value);
        if (isDegreesSelected) {
            primaryValue = Math.toDegrees(Math.atan(primaryValue));
        }
        else {
            primaryValue = Math.atan(primaryValue);
        }
        return primaryValue;
    }
}
