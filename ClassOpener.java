/**
 * @author lorseau
 *
 * Interface to tell the object holder that a new object has been created
 */
public interface ClassOpener {
	
	/**
	 * What must the ClassOpener do once ClassLoadMenu has created a 
	 * new instance "object"of the previously given class
	 * @param object the new instance
	 * @param caller the caller of this function
	 */
	public void classOpened(Object object, Object caller);

}
