/*
 * CajuScriptEngine.java
 * 
 * This file is part of CajuScript.
 * 
 * CajuScript is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3, or (at your option) 
 * any later version.
 * 
 * CajuScript is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with CajuScript.  If not, see <http://www.gnu.org/licenses/>.
*/
package org.cajuscript;
import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptContext;
import javax.script.ScriptException;
import javax.script.ScriptEngineFactory;
import javax.script.SimpleScriptContext;
import java.io.Reader;
import java.util.List;
import java.util.Set;
import javax.script.Invocable;
import javax.script.SimpleBindings;
import java.util.List;
/**
 * <code>CajuScriptEngine</code> is the standard for execute scripts and 
 * transports of objects between Java and CajuScript.
 * <p><blockquote><pre>
 * javax.script.ScriptEngine cajuEngine = new org.cajuscript.CajuScriptEngine();
 * </pre></blockquote></p>
  * <p>Sample:</p>
 * <p><blockquote><pre>
 * try {
 *     javax.script.ScriptEngine caju = new org.cajuscript.CajuScriptEngine();
 *     String javaHello = "Java: Hello!";
 *     caju.put("javaHello", javaHello);
 *     String script = "$java.lang;";
 *     script += "System.out.println(javaHello);";
 *     script += "cajuHello = \"Caju: Hi!\";";
 *     caju.eval(script);
 *     System.out.println(caju.get("cajuHello"));
 * } catch (Exception e) {
 *     e.printStackTrace();
 * }
 * </pre></blockquote></p>
 * <p>To execute a file:</p>
 * <p><blockquote><pre>
 * try {
 *    javax.script.ScriptEngine cajuEngine = new org.cajuscript.CajuScriptEngine();
 *    java.io.FileInputStream fis = new java.io.FileInputStream(<b>"script.cj"</b>);
 *    java.io.Reader reader = new java.io.InputStreamReader(fis);
 *    cajuEngine.eval(reader);
 *    reader.close();
 *    fis.close();
 * } catch (Exception e) {
 *     e.printStackTrace();
 * }
 * </pre></blockquote></p>
 * <p><code>CajuScript</code> syntax:</p>
 * <p><blockquote><pre>
 * \ Imports
 * $java.lang
 * 
 * \ Defining a new variable
 * x = 0
 * 
 * \ LOOP
 * x &lt; 10 & x >= 0 &#64;
 *    System.out.println(x)
 *    x = x + 1
 * &#64;
 * 
 * \ IF
 * x &lt; 10 | x &gt; 0 ?
 *     System.out.println("X is less than 10!")
 * ? x &gt; 10 & x = 10 ?
 *     System.out.println("X is greater than 10!")
 * ??
 *     System.out.println("X = "+ x)
 * ?
 * 
 * \ FUNCTION
 * \ Allowed:
 * \ addWithX v1, v2 # ... #
 * \ addWithX(v1 v2) # ... #
 * addWithX(v1, v2) #
 *     \ "~" is the return
 *     ~ x + v1 + v2
 * #
 * 
 * x = addWithX(1, 2)
 * 
 * System.out.println("X = "+ x)
 * </pre></blockquote></p>
 * <p>Null value:</p>
 * <p><blockquote><pre>
 * \ $ is the null value
 * x = $
 * </pre></blockquote></p>
 * <p>Arithmetic Operators:</p>
 * <p><blockquote><pre>
 * \ + Addition
 * x = 0 + 1
 * x += 1
 * 
 * \ - Subtraction
 * x = 0 - 1
 * x -= 1
 * 
 * \ * Multiplication
 * x = 0 * 1
 * x *= 1
 * 
 * \ / Division
 * x = 0 / 1
 * x /= 1
 * 
 * \ % Modulus
 * x = 0 % 1
 * x %= 1
 * </pre></blockquote></p>
 * <p>Comparison Operators:</p>
 * <p><blockquote><pre>
 * \ = Equal to
 * (x = y)
 * 
 * \ ! Not equal to
 * (x ! y)
 * 
 * \ &lt; Less Than
 * (x &lt; y)
 * 
 * \ &gt; Greater Than
 * (x &gt; y)
 * 
 * \ &lt; Less Than or Equal To
 * (x &lt;= y)
 * 
 * \ &gt; Greater Than or Equal To
 * (x &gt;= y)
 * </pre></blockquote></p>
 * <p>Imports or include file:</p>
 * <p><blockquote><pre>
 * \ Import
 * $java.lang
 * 
 * \ Include file
 * $"script.cj"
 * </pre></blockquote></p>
 * @autor eduveks
 */
public class CajuScriptEngine implements ScriptEngine, Invocable {
    /**
     * Prefix of the variables created automaticaly to catch the values 
     * returned from functions.
     */
    public static final String CAJU_VARS_FUNC_RETURN = CajuScript.CAJU_VARS + "_func_return_";
    /**
     * Prefix of the variables created automaticaly to defined the values for 
     * parameters of functions.
     */
    public static final String CAJU_VARS_FUNC_PARAM = CajuScript.CAJU_VARS + "_func_param_";
    /**
     * Equals the ScriptContext.ENGINE_SCOPE
     */
    public static final int ENGINE_SCOPE = ScriptContext.ENGINE_SCOPE;
    /**
     * Equals the ScriptContext.GLOBAL_SCOPE
     */
    public static final int GLOBAL_SCOPE = ScriptContext.GLOBAL_SCOPE;
    private ScriptContext context;
    private InterfaceImplementor implementor;
    private CajuScript caju;
    /**
     * Create a new CajuScriptEngine.<br/>
     * <br/>
     * <code>
     * javax.script.ScriptEngine cajuEngine = new org.cajuscript.CajuScriptEngine();
     * </code>
     * @throws javax.script.ScriptException Exception on creating a new instance.
     */
    public CajuScriptEngine() throws ScriptException {
        try {
            caju = new CajuScript();
        } catch (Exception e) {
            throw new ScriptException(e);
        }
        context = new SimpleScriptContext();
        implementor = new InterfaceImplementor(this);
    }
    /**
     * Script on CajuScript to be executed.<br/>
     * <br/>
     * <code>
     *     javax.script.ScriptEngine cajuEngine = new org.cajuscript.CajuScriptEngine();<br/>
     *     cajuEngine.eval("java.lang.System.out.println('Hello CajuScript!');");<br/>
     * </code>
     * @param script Script on CajuScript to be executed.
     * @param context Define the context.
     * @return Script executed can return an object.
     * @throws javax.script.ScriptException Exception on execution.
     */
    public Object eval(String script, ScriptContext context) throws ScriptException {
        return runScript(script, context);
    }
    /**
     * Execute script in a Reader, can be files.<br/>
     * <br/>
     * <code>
     *    javax.script.ScriptEngine cajuEngine = new org.cajuscript.CajuScriptEngine();<br/>
     *    java.io.FileInputStream fis = new java.io.FileInputStream(<b>"script.cj"</b>);<br/>
     *    java.io.Reader reader = new java.io.InputStreamReader(fis);<br/>
     *    cajuEngine.eval(reader);<br/>
     *    reader.close();<br/>
     *    fis.close();<br/>
     * </code>
     * @param reader Input of the file with the script to be executed.
     * @param context Define the context.
     * @return Script executed can return an object.
     * @throws javax.script.ScriptException Exception on execution.
     */
    public Object eval(Reader reader, ScriptContext context) throws ScriptException {
        return runScript(readAll(reader), context);
    }
    /**
     * Execute script in an string.<br/>
     * <br/>
     * <code>
     *     javax.script.ScriptEngine cajuEngine = new org.cajuscript.CajuScriptEngine();<br/>
     *     cajuEngine.eval("java.lang.System.out.println('Hello CajuScript!');");
     * </code>
     * @param script Script to be executed.
     * @return Script executed can return an object.
     * @throws javax.script.ScriptException Exception on execution.
     */
    public Object eval(String script) throws ScriptException {
        return eval(script, context);
    }
    /**
     * Execute script in an file.<br/>
     * <br/>
     * <code>
     *    javax.script.ScriptEngine cajuEngine = new org.cajuscript.CajuScriptEngine();<br/>
     *    java.io.FileInputStream fis = new java.io.FileInputStream(<b>"script.cj"</b>);<br/>
     *    java.io.Reader reader = new java.io.InputStreamReader(fis);<br/>
     *    cajuEngine.eval(reader);<br/>
     *    reader.close();<br/>
     *    fis.close();<br/>
     * </code>
     * @param reader Input of the file with script to be executed.
     * @return Script can returned an object.
     * @throws javax.script.ScriptException Exception on execution.
     */
    public Object eval(Reader reader) throws ScriptException {
        return eval(reader, context);
    }
    /**
     * Script on CajuScript to be executed.<br/>
     * <br/>
     * <code>
     *     javax.script.ScriptEngine cajuEngine = new org.cajuscript.CajuScriptEngine();<br/>
     *     cajuEngine.eval("java.lang.System.out.println('Hello CajuScript!');");<br/>
     * </code>
     * @param script Script to be executed.
     * @param bindings Define the bindings.
     * @return Script can returned an object.
     * @throws javax.script.ScriptException Exception on execution.
     */
    public Object eval(String script, Bindings bindings) throws ScriptException {
        return runScript(script, bindings);
    }
    /**
     * Execute script in an file.<br/>
     * <br/>
     * <code>
     *    javax.script.ScriptEngine cajuEngine = new org.cajuscript.CajuScriptEngine();<br/>
     *    java.io.FileInputStream fis = new java.io.FileInputStream(<b>"script.cj"</b>);<br/>
     *    java.io.Reader reader = new java.io.InputStreamReader(fis);<br/>
     *    cajuEngine.eval(reader);<br/>
     *    reader.close();<br/>
     *    fis.close();<br/>
     * </code>
     * @param reader Input of the file with script to be executed.
     * @param bindings Define the bindings.
     * @return Script executed can return an object.
     * @throws javax.script.ScriptException Exception on execution.
     */
    public Object eval(Reader reader , Bindings bindings) throws ScriptException {
        return runScript(readAll(reader), bindings);
    }
    /**
     * Send object of Java to CajuScript.<br/>
     * <br/>
     * <code>
     *    javax.script.ScriptEngine cajuEngine = new new org.cajuscript.CajuScriptEngine();<br/>
     *    cajuEngine.put("test_put", new StringBuffer("Java object!"));<br/>
     *    cajuEngine.eval("test_put.append('\\nTest CajuScript append!')");<br/>
     *    cajuEngine.eval("java.lang.System.out.println(test_put.toString())");<br/>
     * </code>
     * @param key Name of the variable on CajuScript.
     * @param value Object to set the variable.
     */
    public void put(String key, Object value) {
        context.setAttribute(key, value, SimpleScriptContext.ENGINE_SCOPE);
    }
    /**
     * Catch objects of CajuScript to Java.<br/>
     * <br/>
     * <code>
     *    javax.script.ScriptEngine cajuEngine = new org.cajuscript.CajuScriptEngine();<br/>
     *    cajuEngine.eval("test_get = 'CajuScript object!'");<br/>
     *    String str = (String)cajuEngine.get("test_get");<br/>
     *    System.out.println(str);
     * </code>
     * @param key Name of the variable in CajuScript.
     * @return Object CajuScript in Java.
     */
    public Object get(String key) {
        Object result = context.getAttribute(key, SimpleScriptContext.ENGINE_SCOPE);
        if (result == null) {
            try {
                return caju.get(key);
            } catch (Exception e) {
                return null;
            }
        } else {
            return result;
        }
    }
    /**
     * Get Bindings
     * @param scope Scope
     * @return Binding
     */
    public Bindings getBindings(int scope) {
        return context.getBindings(scope);
    }
    /**
     * Set Binding
     * @param bindings Binding
     * @param scope Scope
     */
    public void setBindings(Bindings bindings, int scope) {
        context.setBindings(bindings, scope);
    }
    /**
     * Load a new Binding.
     * @return New Binding.
     */
    public Bindings createBindings() {
        return new SimpleBindings();
    }
    /**
     * The context.
     * @return Current context.
     */
    public ScriptContext getContext() {
        return context;
    }
    /**
     * Define the context.
     * @param context Context.
     */
    public void setContext(ScriptContext context) {
        this.context = context;
    }
    /**
     * Newly instance of the CajuScript Engine Factory.
     * @return Intanse of the CajuScript Engine Factory.
     */
    public ScriptEngineFactory getFactory() {
        return new CajuScriptEngineFactory();
    }
    /**
     * Invoke Java method on CajuScript.
     * @param thiz Object with the method to invoke.
     * @param name Name of the method to invoke.
     * @param args Parameters of the method.
     * @throws javax.script.ScriptException Exception on invoke.
     * @throws java.lang.NoSuchMethodException Exception if method was not found.
     * @return Object who was returned from the method.
     */
    public Object invokeMethod(Object thiz, String name, Object... args) throws ScriptException, NoSuchMethodException {
        String uuid = java.util.UUID.randomUUID().toString().replaceAll("\\-", "_");
        String funcObjectName = CajuScript.CAJU_VARS + "_" + uuid;
        try {
            caju.set(funcObjectName, thiz);
        } catch (Exception e) {
            throw new ScriptException(e);
        }
        Object returnObject = invokeFunction(funcObjectName + "." + name, args);
        return returnObject;
    }
    /**
     * Invoke a CajuScript function.
     * @param name Function name.
     * @param args Parameter of the function.
     * @throws javax.script.ScriptException Exception on invoke.
     * @throws java.lang.NoSuchMethodException Exception if function was not found.
     * @return Object who was returned from the function.
     */
    public Object invokeFunction(String name, Object... args) throws ScriptException, NoSuchMethodException {
        String cmd = "";
        String uuid = java.util.UUID.randomUUID().toString().replaceAll("\\-", "_");
        String funcReturnName = CAJU_VARS_FUNC_RETURN + uuid;
        cmd += funcReturnName + " = ";
        cmd += name + "(";
        if (args != null) {
            for (int i = 0; i < args.length; i++) {
                String funcParamName = CAJU_VARS_FUNC_PARAM + i +"_" + uuid;
                cmd += (i > 0 ? "," : "") + funcParamName;
                try {
                    caju.set(funcParamName, args[i]);
                } catch (Exception e) {
                    throw new ScriptException(e);
                }
            }
        }
        cmd += ");";
        eval(cmd);
        try {
            return caju.get(funcReturnName);
        } catch (Exception e) {
            throw new ScriptException(e);
        }
    }
    /**
     * Get interface
     * @param clasz Class
     * @return Interface.
     */
    public <T>T getInterface(Class<T> clasz) {
        try {
            return (T)implementor.getInterface(null, clasz);
        } catch(ScriptException e) {
            return null;
        }
    }
    /**
     * Get Interface.
     * @param thiz Object.
     * @param clasz Class.
     * @return Interface.
     */
    public <T>T getInterface(Object thiz, Class<T> clasz) {
        if(thiz == null) {
            throw new IllegalArgumentException("script object can not be null");
        }
        try {
             return (T)implementor.getInterface(thiz, clasz);
        } catch(ScriptException e) {
            return null;
        }
    }
    private void loadBindings(ScriptContext context) throws ScriptException {
        List<Integer> scopes = context.getScopes();
        for (Integer i : scopes) {
            loadBindings(context.getBindings(i));
        }
    }
    private void loadBindings(Bindings bindings) throws ScriptException {
        if (bindings == null) {
            return;
        }
        Set<String> keys = bindings.keySet();
        for (String key : keys) {
            try {
                caju.set(key, bindings.get(key));
            } catch (Exception e) {
                throw new ScriptException(e);
            }
        }
    }
    private void recoveryBindings(ScriptContext context) throws ScriptException {
        List<Integer> scopes = context.getScopes();
        for (Integer i : scopes) {
            recoveryBindings(context.getBindings(i));
        }
    }
    private void recoveryBindings(Bindings bindings) throws ScriptException {
        if (bindings == null) {
            return;
        }
        Set<String> keys = bindings.keySet();
        for (String key : keys) {
            try {
                bindings.put(key, caju.get(key));
            } catch (Exception e) {
                throw new ScriptException(e);
            }
        }
    }
    private Object runScript(String script) throws ScriptException {
        try {
            return caju.eval(script).getValue();
        } catch (Exception e) {
            throw new  ScriptException(e);
        }
    }
    private String readAll(Reader reader) throws ScriptException {
        int b;
        StringBuffer sb = new StringBuffer();
        try {
            while ((b = reader.read()) > -1) {
                sb.append((char)b);
            }
        } catch (Exception e) {
            throw new ScriptException(e);
        }
        return sb.toString();
    }
    private Object runScript(String script, ScriptContext context) throws ScriptException {
        loadBindings(context);
        Object obj = runScript(script);
        recoveryBindings(context);
        return obj;
    }
    private Object runScript(String script, Bindings bindings) throws ScriptException {
        loadBindings(bindings);
        Object obj = runScript(script);
        recoveryBindings(bindings);
        return obj;
    }
}
class InterfaceImplementor
{
    public class InterfaceImplementorInvocationHandler implements java.lang.reflect.InvocationHandler {
        public InterfaceImplementorInvocationHandler(Invocable engine, Object thiz) {
            this$0 = InterfaceImplementor.this;
            this.engine = engine;
            this.thiz = thiz;
        }
        public Object invoke(Object proxy, java.lang.reflect.Method method, Object args[]) throws Throwable {
            if (proxy != null) {
                proxy.toString();
            }
            args = convertArguments(method, args);
            Object result;
            if(thiz == null) {
                result = engine.invokeFunction(method.getName(), args);
            } else {
                result = engine.invokeMethod(thiz, method.getName(), args);
            }
            return convertResult(method, result);
        }
        private Invocable engine;
        private Object thiz;
        final InterfaceImplementor this$0;
    }
    public InterfaceImplementor(Invocable engine) {
        this.engine = engine;
    }
    public Object getInterface(Object thiz, Class iface) throws ScriptException {
        if(iface == null || !iface.isInterface()) {
            throw new IllegalArgumentException("interface Class expected");
        } else {
            return iface.cast(java.lang.reflect.Proxy.newProxyInstance(iface.getClassLoader(), new Class[] {iface}, new InterfaceImplementorInvocationHandler(engine, thiz)));
        }
    }
    protected Object convertResult(java.lang.reflect.Method method, Object res) throws ScriptException {
        if (method != null) {
            method.toString();
        }
        return res;
    }
    protected Object[] convertArguments(java.lang.reflect.Method method, Object args[]) throws ScriptException {
        if (method != null) {
            method.toString();
        }
        return args;
    }
    private Invocable engine;
}