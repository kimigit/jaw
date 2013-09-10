/* This class is used to change an object value inside a function
 * without it returning anything.
 * 
 * Example:
 * 
 * void doSomething(Changeable<String> s) {
 *   s.changeTo("bar");
 * }
 * 
 * Changeable<String> foo = new Changeable<String>("foo");
 * doSomething(foo);
 * 
 * -> Here foo.toString() equals "bar" 
 */

package jaw;

public class Changeable<T> {
	
	protected T value;
	
	public Changeable(T value) {
		this.value = value;
	}
	
	public String toString() {
		return value.toString();
	}
	
	public boolean equals(Object newObject) {
		return (newObject instanceof Changeable)
			?	this.value.equals(((Changeable<?>)newObject).value)
			: this.value.equals(newObject);
	}
  
	public void changeTo(T newValue) {
		this.value = newValue;
	}
}