import java.io.*;

/**
 * This class shows how to declare a Generic class where the 
 * generic member is expected to extend an interface. Thus, if 
 * T is set to be a class that doesn't implement Serializable,
 * there will be a compile-time error.
 */
class SerializeEnforce<T extends Serializable> 
	implements Serializable {
	Integer val;
	T obj;
	
	public SerializeEnforce(T t, Integer i) {
		obj=t;
		val=i;
	}
	
	public static void main(String []args) {
		SerializeEnforce<String> val
			=new SerializeEnforce<String>(String.valueOf("hello"), 	
				Integer.valueOf(2));
	}
}