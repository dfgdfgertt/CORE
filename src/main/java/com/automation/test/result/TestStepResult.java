package com.automation.test.result;

import com.automation.test.annonations.Column;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.Map;

public class TestStepResult {
	@Column(name = "Description", width = 65)
	private String description;
	@Column(name = "Input", width = 35)
	private String input;
	@Column(name = "Expected", width = 35)
	private String expected;
	@Column(name = "Result", width = 10)
	private String result;
	@Column(name = "Actual", width = 150)
	private String actual;

	private String explanation;
	private String verifiableInstuction;


	public void setDescriptionComment(String comment) {	
		try {
			Field field = this.getClass().getDeclaredField("description");
			final Column col = field.getAnnotation(Column.class);
			changeAnnotationValue(col, "comment", comment);
		} catch (NoSuchFieldException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setInputComment(String comment) {	
		try {
			Field field = this.getClass().getDeclaredField("input");
			final Column col = field.getAnnotation(Column.class);
			changeAnnotationValue(col, "comment", comment);
		} catch (NoSuchFieldException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setExpectedComment(String comment) {	
		try {
			Field field = this.getClass().getDeclaredField("expected");
			final Column col = field.getAnnotation(Column.class);
			changeAnnotationValue(col, "comment", comment);
		} catch (NoSuchFieldException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setActualComment(String comment) {	
		try {
			Field field = this.getClass().getDeclaredField("actual");
			final Column col = field.getAnnotation(Column.class);
			changeAnnotationValue(col, "comment", comment);
		} catch (NoSuchFieldException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
	}

	public String getExpected() {
		return expected;
	}

	public void setExpected(String expected) {
		this.expected = expected;
	}

	public String getActual() {
		return actual;
	}

	public void setActual(String actual) {
		this.actual = actual;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getExplanation() { return this.explanation; }

	public void setExplanation(String explanation) { this.explanation = explanation; }

	public void setVerifiableInstruction(String verifiableInstruction){ this.verifiableInstuction = verifiableInstruction;}
	public String getVerifiableInstruction(){return this.verifiableInstuction;}


	/**
	 * Changes the annotation value for the given key of the given annotation to newValue and returns
	 * the previous value.
	 */
	@SuppressWarnings("unchecked")
	public static Object changeAnnotationValue(Annotation annotation, String key, Object newValue){
	    Object handler = Proxy.getInvocationHandler(annotation);
	    Field f;
	    try {
	        f = handler.getClass().getDeclaredField("memberValues");
	    } catch (NoSuchFieldException | SecurityException e) {
	        throw new IllegalStateException(e);
	    }
	    f.setAccessible(true);
	    Map<String, Object> memberValues;
	    try {
	        memberValues = (Map<String, Object>) f.get(handler);
	    } catch (IllegalArgumentException | IllegalAccessException e) {
	        throw new IllegalStateException(e);
	    }
	    Object oldValue = memberValues.get(key);
	    if (oldValue == null || oldValue.getClass() != newValue.getClass()) {
	        throw new IllegalArgumentException();
	    }
	    memberValues.put(key,newValue);
	    return oldValue;
	}
}