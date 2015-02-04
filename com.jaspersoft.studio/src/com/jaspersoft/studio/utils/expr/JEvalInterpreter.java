package com.jaspersoft.studio.utils.expr;

public class JEvalInterpreter extends AInterpreter {

	@Override
	protected Object eval(String expression) throws Exception {
		System.out.println("Called JEvalInterpreter.eval() with argument '" + expression + "'");
		return null;
	}

	@Override
	protected void set(String key, Object val) throws Exception {
		System.out.println("Called JEvalInterpreter.set() with argument '" +key + "'='" + val + "'");
	}

	@Override
	protected Object get(String key) throws Exception {
		System.out.println("Called JEvalInterpreter.get() with argument " + key);
		return null;
	}

}
