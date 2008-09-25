/*
 * Variable.java
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

package org.cajuscript.parser;

import org.cajuscript.CajuScript;
import org.cajuscript.Context;
import org.cajuscript.Value;
import org.cajuscript.SyntaxPosition;
import org.cajuscript.Syntax;
import org.cajuscript.CajuScriptException;

/**
 * Script element of type variable.
 * @author eduveks
 */
public class Variable extends Base {
    private String key = "";
    private Element value = null;
    
    /**
     * Create new Variable.
     * @param line Line detail
     */
    public Variable(LineDetail line) {
        super(line);
    }
    
    /**
     * Get variable key.
     * @return Key
     */
    public String getKey() {
        return key;
    }
    
    /**
     * Set variable key.
     * @param key Key
     */
    public void setKey(String key) {
        this.key = key;
    }
    
    /**
     * Get variable value.
     * @return Element of value
     */
    public Element getValue() {
        return value;
    }
    
    /**
     * Set variable value.
     * @param value Element of value
     */
    public void setValue(Element value) {
        this.value = value;
    }
    
    /**
     * Executed this element.
     * @param caju CajuScript
     * @param context Context
     * @param syntax Syntax
     * @return Value returned by execution
     * @throws org.cajuscript.CajuScriptException Errors ocurred on execution
     */
    @Override
    public Value execute(CajuScript caju, Context context, Syntax syntax) throws CajuScriptException {
        caju.setRunningLine(getLineDetail());
        Value v = value.execute(caju, context, syntax);
        if (!key.equals("")) {
            SyntaxPosition syntaxPosition = syntax.matcherPosition(key, syntax.getRootContext());
            if (syntaxPosition.getStart() == 0) {
                caju.setVar(key.substring(syntaxPosition.getEnd()), v);
            } else {
                context.setVar(key, v);
            }
        }
        return v;
    }
}
