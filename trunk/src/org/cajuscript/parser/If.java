/*
 * If.java
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
import org.cajuscript.Syntax;
import org.cajuscript.CajuScriptException;

/**
 * Script element of type if.
 * @author eduveks
 */
public class If extends Base {
    private Element condition = null;
    /**
     * Create new If.
     * @param line Line detail
     * @param syntax Syntax style
     */
    public If(LineDetail line, Syntax syntax) {
        super(line, syntax);
    }
    /**
     * Get condition.
     * @return Element of condition
     */
    public Element getCondition() {
        return condition;
    }
    /**
     * Set condition.
     * @param condition Element of condition
     */
    public void setCondition(Element condition) {
        this.condition = condition;
    }
    /**
     * Executed this element and all childs elements.
     * @param caju CajuScript instance
     * @return Value returned by execution
     * @throws org.cajuscript.CajuScriptException Errors ocurred on execution
     */
    @Override
    public Value execute(CajuScript caju, Context context) throws CajuScriptException {
        caju.setRunningLine(getLineDetail());
        Object conditionValue = condition.execute(caju, context).getValue();
        boolean finalValue = false;
        if (conditionValue instanceof Boolean) {
            finalValue = ((Boolean)conditionValue).booleanValue();
        } else if (conditionValue instanceof Integer) {
            if (((Integer)conditionValue).intValue() > 0) {
                finalValue = true;
            } else {
                finalValue = false;
            }
        } else {
            finalValue = Boolean.getBoolean(conditionValue.toString());
        }
        if (finalValue) {
            for (Element element : elements) {
                Value v = element.execute(caju, context);
                if (v != null && canElementReturn(element)) {
                    return v;
                }
            }
            Value v = caju.toValue(new Boolean(true), context, getSyntax());
            v.setFlag("if");
            return v;
        } else {
            return null;
        }
    }
    
    @Override
    protected void finalize() throws Throwable {
        condition = null;
    }
}