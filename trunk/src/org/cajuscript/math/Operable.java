package org.cajuscript.math;

/**
 * To do calcs with classes, like:
 * <pre>
 * package cajuscript_test;
 * public class Vector2D implements org.cajuscript.math.Operable<Vector2D> {
 *     private int x, y = 0;
 *     public Vector2D(int x, int y) { this.x = x; this.y = y; }
 *     public int getX() { return x; }
 *     public void setX(int x) { this.x = x; }
 *     public int getY() { return y; }
 *     public void setY(int y) { this.y = y; }
 *     public Vector2D plus(Vector2D other) { return new Vector2D(x + other.x, y + other.y); }
 *     public Vector2D subtract(Vector2D other) { return new Vector2D(x - other.x, y - other.y); }
 *     public Vector2D multiply(Vector2D other) { return new Vector2D(x * other.x, y * other.y); }
 *     public Vector2D divide(Vector2D other) { return new Vector2D(x / other.x, y / other.y); }
 *     public Vector2D module(Vector2D other) { return new Vector2D(x % other.x, y % other.y); }
 * }
 * </pre>
 * In CajuScript you can use this:
 * <pre>
 * printVector2D action, vector #
 *     System.out.println(action + ' x = ' + vector.getX())
 *     System.out.println(action + ' y = ' + vector.getY())
 * #
 * a = cajuscript_test.Vector2D(1, 2)
 * b = cajuscript_test.Vector2D(2, 3)
 * c = a + b
 * printVector2D('plus', c)
 * c = a - b
 * printVector2D('subtract', c)
 * c = a * b
 * printVector2D('multiply', c)
 * c = a / b;" +
 * printVector2D('divide', c)
 * c = a % b
 * printVector2D('module', c)
 * </pre>
 * @author mark.vscs
 */
public interface Operable<T> {
    /**
     * Plus.
     * @param other Another value to be calculated.
     * @return New value calculated.
     */
    public T plus(T other);
    
    /**
     * Subtract.
     * @param other Another value to be calculated.
     * @return New value calculated.
     */
    public T subtract(T other);
    
    /**
     * Multiply.
     * @param other Another value to be calculated.
     * @return New value calculated.
     */
    public T multiply(T other);
    
    /**
     * Divide.
     * @param other Another value to be calculated.
     * @return New value calculated.
     */
    public T divide(T other);
    
    /**
     * Module.
     * @param other Another value to be calculated.
     * @return New value calculated.
     */
    public T module(T other);
}