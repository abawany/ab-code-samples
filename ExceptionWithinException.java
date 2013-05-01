import java.io.*;

class ExceptionWithinException {

  /**
   * I was mistaken in my answer - apparently within a method, it is not
   * possible for multiple handlers in a method to be invoked while handling
   * a given exception.
   * http://download.oracle.com/javase/tutorial/essential/exceptions/chained.html
   * 
   * @throws Exception
   */
  public static void mth() throws Exception {
    try {
			Integer x=new Integer("BOO");
		}
		catch (NumberFormatException e) {
			System.err.println("caught NFE");
			int i=1/0; // try for an arith exception
		}
		catch (ArithmeticException e) {
      // never gets invoked
			System.err.println("caught AE1");
		}
    finally {
      System.err.println("mth");
    }
  }

	public static void main(String []sArgs) {
		try {
      ExceptionWithinException.mth();
    }
    catch (ArithmeticException e) {
      System.err.println("caught AE2");
    }
    catch (Exception e) {
      System.err.println("caught E");
    }
    finally {
      System.err.println("main");
    }
	}
}