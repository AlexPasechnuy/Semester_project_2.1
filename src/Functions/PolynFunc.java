package Functions;

import Exceptions.WrongFunctionFormatException;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class PolynFunc extends AbsFunc {
    String func;

    public PolynFunc(String func) {
        this.func = func;
    }

    public String getFunc(){return func;}

    public double solve(double x)throws WrongFunctionFormatException{
        ScriptEngineManager factory = new ScriptEngineManager();
        ScriptEngine engine = factory.getEngineByName("JavaScript");
        engine.put("x", x);
        Object result = null;
        try {
            result = engine.eval(func + ";");
        }catch(ScriptException ex)
        {
            throw new WrongFunctionFormatException();
        }
        return (double) result;
    }
}