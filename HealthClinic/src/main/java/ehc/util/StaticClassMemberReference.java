package ehc.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class StaticClassMemberReference {

	private String expression;

	private Method resolvedMethod;
	private Field resolvedField;

	public static class ExpressionException extends Exception {
		public ExpressionException(String message, Throwable cause) {
			super(message, cause);
		}
		public ExpressionException(String message) {
			super(message);
		}
	}

	public StaticClassMemberReference(String expr, Class<?> expectedClass) throws ExpressionException {
		this.expression = expr.trim();
		compile();
	}

	private void compile() throws ExpressionException {
		String ref = expression;

		int endOfClassName = ref.lastIndexOf('.');
		if (endOfClassName < 0)
			throw new ExpressionException("Invalid reference (missing '.'): " + ref);
		String className = ref.substring(0, endOfClassName);
		String staticMethodName = null;
		String globalInstanceVariableName = null;

		if (ref.endsWith("()")) {
			staticMethodName = ref.substring(endOfClassName + 1, ref.length() - 2);
		}
		else
			globalInstanceVariableName = ref.substring(endOfClassName + 1);

		try {
			Class<?> cl = resolveClassReference(className);
			if (staticMethodName != null)
				resolvedMethod = cl.getDeclaredMethod(staticMethodName, (Class<?>[]) null);
			else
				resolvedField = cl.getDeclaredField(globalInstanceVariableName);
		}
		catch (ClassNotFoundException e) {
			throw compilationError(e);
		}
		catch (SecurityException e) {
			throw compilationError(e);
		}
		catch (NoSuchMethodException e) {
			throw compilationError(e);
		}
		catch (NoSuchFieldException e) {
			throw compilationError(e);
		}
	}

	public Object evaluate() throws ExpressionException {
		try {
			if (resolvedMethod != null)
				return resolvedMethod.invoke((Object[]) null, (Object[]) null);
			else
				return resolvedField.get(null);
		}
		catch (IllegalArgumentException e) {
			throw evaluationError(e);
		}
		catch (IllegalAccessException e) {
			throw evaluationError(e);
		}
		catch (InvocationTargetException e) {
			throw evaluationError(e);
		}
	}

	private ExpressionException compilationError(Throwable e) {
		return new ExpressionException("Error compiling '" + expression + "': " + e.toString(), e);
	}

	private ExpressionException evaluationError(Throwable e) {
		return new ExpressionException("Error evaluating '" + expression + "': " + e.toString(), e);
	}

	protected Class<?> resolveClassReference(String className) throws ClassNotFoundException {
		return Class.forName(className);
	}

}
