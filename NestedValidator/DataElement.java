
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author ali
 */
public class DataElement {

  enum errorTypes {

    RequiredButNotPresent,
    FormatMismatch,
    PatternMismatch,
    IncorrectNumberOfSubElements
  };

  // Complex type must have at least one element in its subElements list
  public enum elementTypes {

    Complex, Int, String
  };
  String elementName;
  elementTypes elementType;
  String elementRegex;
  boolean required;
  // for Complex type, determine how many subElements must be present
  int minNumberOfSubElements;
  public List<DataElement> subElements;

  public String getElementName() {
    return this.elementName;
  }

  public elementTypes getElementType() {
    return this.elementType;
  }

  public boolean getRequired() {
    return this.required;
  }

  public DataElement(String elemName, elementTypes type, String regex,
          boolean required, int minNumberOfSubElems) {

    this.elementName = elemName;
    this.elementType = type;
    this.elementRegex = regex;
    this.required = required;
    this.minNumberOfSubElements = minNumberOfSubElems;
    if (type.equals(elementTypes.Complex)) {
      this.subElements = new ArrayList<DataElement>();
    }
  }

  /**
   * returns true if this element was present in postVars
   * if true, then check listErrors for errors found during validate
   *
   * @param postVars
   * @param listErrors
   * @return
   */
  public boolean validate(Map<String, String> postVars, List<String> listErrors) {

    // check whether this request node is present in postVars
    String value = postVars.get(this.elementName);
    boolean present = (value != null);

    if (this.required == true && !present) {
      listErrors.add(errorTypes.RequiredButNotPresent + " "
              + this.elementName);
    }

    // traverse through until encounter a leaf node, then perform
    // node-specific checks
    if (this.elementType == elementType.Complex) {
      int count = 0;
      for (DataElement e : this.subElements) {
        if (e.validate(postVars, listErrors)) {
          ++count;
        }
      }

      // for complex elements, we want to ensure that the number found
      // is >= number of sub-elements expected
      if (count < this.minNumberOfSubElements) {
        listErrors.add(errorTypes.IncorrectNumberOfSubElements + " "
                + this.elementName + " Expected [" + this.minNumberOfSubElements
                + "] Got [" + count + "]");
      }
    } else if (present) {
      // if regex defined, do a match with it first
      if (this.elementRegex!=null) {
        Pattern p=Pattern.compile(this.elementRegex);
        Matcher m=p.matcher(value);
        if (m.matches()==false)
          listErrors.add(errorTypes.PatternMismatch + " " + this.elementName
                  + " Expected [" + this.elementRegex + "] Got ["
                  + value + "]");
      }

      // if type was int, try to convert value to number
      if (this.elementType.equals(elementType.Int)) {
        try {
          Integer i=Integer.valueOf(value);
        } catch (NumberFormatException e) {
          listErrors.add(errorTypes.FormatMismatch + " " + this.elementName
                  + " Expected [integer] Got [" + value + "]");
        }
      }
    }

    return present;
  }
}
