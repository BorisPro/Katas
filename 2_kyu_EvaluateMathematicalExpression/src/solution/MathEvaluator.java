package solution;

import java.util.function.DoubleBinaryOperator;
import java.util.function.IntFunction;

public class MathEvaluator {
	
	// static final String STOPCHARS = "(*/";
	StringBuilder term;
	
	public double calculate(String expression) {
		expression = expression.replace(" ", "").replace("\\s", "").replace("\\s+", "");
		expression = expression.replace("--", "+").replace("+-", "-"); // remove whitespace.
		term = new StringBuilder(expression);
		executeAll('/', (a, b) -> a / b);
		executeAll('*', (a, b) -> a * b);
		executeAll('+', (a, b) -> a + b);
		executeAll('-', (a, b) -> a - b);
		
		return Double.valueOf(term.toString());
	}

	private void executeAll(char symbol, DoubleBinaryOperator operation) {
		while(containsOperator(symbol)) {
			for(int i = 0; i < term.length() - 1; i++)  {
				if (term.charAt(i) == symbol) {
					executeOperation(i, operation);
					break;
				}
			}
			String string = term.toString().replace("--", "+").replace("+-", "-");
			term = new StringBuilder(string);
		}
	}

	private boolean containsOperator(char symbol) {
		int indexOfOperator = term.indexOf(String.valueOf(symbol));
		return indexOfOperator != -1 && !isSign(indexOfOperator);
	}
	
	private double executeOperation(int index, DoubleBinaryOperator operation) {
		Integer indexRight = extractNumberIndex(index, x -> {return x + 1;});
		Integer indexLeft = extractNumberIndex(index, x -> {return x - 1;});
		
		String numberAsStringLeft = term.substring(indexLeft, index);
		String numberAsStringRight = term.substring(index + 1, indexRight + 1);
		
		Double leftNumber = Double.valueOf(numberAsStringLeft);
		Double rightNumber = Double.valueOf(numberAsStringRight);

		Double result = operation.applyAsDouble(leftNumber, rightNumber);
		term.replace(indexLeft, indexRight + 1, result.toString());
		
		return result;
	}

	private Integer extractNumberIndex(int startIndex, IntFunction<Integer> incrementor) {
		Integer endOfRightNumber = startIndex;
		do {
			endOfRightNumber = incrementor.apply(endOfRightNumber);
		} while ((indexBelongsToNumber(incrementor.apply(endOfRightNumber))));
		return endOfRightNumber;
	}

	private boolean indexBelongsToNumber(int startPositionOfFirstNumber) {
		if (startPositionOfFirstNumber == term.length() || startPositionOfFirstNumber < 0) {
			return false;
		} else {
			boolean isNumber = Character.isDigit(term.charAt(startPositionOfFirstNumber));
			boolean isDot = term.charAt(startPositionOfFirstNumber) == '.';
			return (isNumber || isDot || isSign(startPositionOfFirstNumber));
		} 
	}

	private boolean isSign(int startPositionOfFirstNumber) {
		boolean isMinus = term.charAt(startPositionOfFirstNumber) == '-';
		boolean result = false;
		if (isMinus) {
			if (startPositionOfFirstNumber == 0) {
				result = true;
			} else {
				char c = term.charAt(startPositionOfFirstNumber-1);
				result = "(*/+".contains(String.valueOf(c));
			}
		}
		return result;
	}
}
