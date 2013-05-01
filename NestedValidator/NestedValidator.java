
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ali
 */
public class NestedValidator {

  public NestedValidator() {
    //this.requestTemplateCache = new HashMap<String, DataElement>();
  }

  /**
   * Returns a list containing the errors found for the given request type
   *
   * @param postVars
   * @return
   */
  public List<String> validateRequest(String requestName, Map<String, String> postVars) {
    List<String> errorList=new ArrayList<String>();

    // use a factory-like method to get a request template that will look
    // through postVars to perform validation
    DataElement template=NestedValidator.buildRequestTemplate(requestName);
    template.validate(postVars, errorList);

    return errorList;
  }

  public static void main(String[] sArgs) {
    NestedValidator validator = new NestedValidator();

    Map<String, String> postVars = new HashMap<String, String>();
    // populate postVars with a good set
    NestedValidator.getGoodPostVars(postVars);
    List<String> listErr=validator.validateRequest("WorkoutStatsUpdateRequest",
            postVars);
    NestedValidator.printErrors("Good Request", listErr);
    
    // populate postVars with a bad set
    postVars.clear();
    NestedValidator.getBadPostVars(postVars);
    listErr=validator.validateRequest("WorkoutStatsUpdateRequest", postVars);
    NestedValidator.printErrors("Bad Request", listErr);
  }

  private static void printErrors(String title, List<String> listErr) {
    if (listErr.isEmpty())
      System.out.println("PASSED " + title);
    else {
      System.out.println("FAILED " + title + "\nErrors are as follows:");
      for (String msg: listErr)
        System.out.println(msg);
    }
  }

  private static void getGoodPostVars(Map<String, String> postVars) {
    postVars.put("UserId", "123");
    postVars.put("UserName", "homer");
    postVars.put("ExerciseType", "1");
    postVars.put("ExerciseTime", "10");
    postVars.put("Identifier", "2");
    postVars.put("WorkoutStats", "2");
    postVars.put("WorkoutStatsUpdateRequest", "2");
  }

  private static void getBadPostVars(Map<String, String> postVars) {
    postVars.put("UserId", "A1"); // non-numeric user id
    postVars.put("ExerciseType", "-2"); // invalid exercise type
    //postVars.put("ExerciseTime", "10");
    postVars.put("Identifier", "1");
    postVars.put("WorkoutStats", "2");
    postVars.put("WorkoutStatsUpdateRequest", "2");
  }

  //private Map<String, DataElement> requestTemplateCache;
  /**
   *
   * @param templateType
   * @return
   *
   * FIXME: naive operation: constructs the template from scratch each
   * time instead of caching and returning a copy to the caller
   */
  private static DataElement buildRequestTemplate(String templateType) {

    DataElement elem = null;
    /**
     * Example request template:
     * <WorkoutStatsUpdateRequest required=1>
     *  <Identifier type=complex required=1>
     *    <UserId type=int required=0/>
     *    <UserName type=String required=0/>
     *  </Identifier>
     *
     *  <WorkoutStats type=complex required=1>
     *    <ExerciseType type=int required=1/>
     *    <ExerciseTime type=int required=1/>
     *  </WorkoutStats>
     * </WorkoutStatsUpdateRequest>
     */
    if (templateType.equals("WorkoutStatsUpdateRequest")) {
      elem = new DataElement("WorkoutStatsUpdateRequest",
              DataElement.elementTypes.Complex, null, true, 2);
      // only one sub-element required (i.e. either userId or userName)
      {
        DataElement identifier = new DataElement("Identifier",
                DataElement.elementTypes.Complex, null, true, 1);
        {
          DataElement userId = new DataElement("UserId",
                  DataElement.elementTypes.Int, "[0-9]+", false, 0);
          DataElement userName = new DataElement("UserName",
                  DataElement.elementTypes.String, "[A-Za-z -]+", false, 0);
          identifier.subElements.add(userId);
          identifier.subElements.add(userName);
        }
        elem.subElements.add(identifier);
      }
      // both sub-elements are required (i.e. exerciseType and exerciseTime
      // must be provided)
      {
        DataElement workoutStat = new DataElement("WorkoutStats",
                DataElement.elementTypes.Complex, null, true, 2);
        {
          DataElement exerciseType=new DataElement("ExerciseType",
                  DataElement.elementTypes.Int, "[1-4]", true, 0);
          DataElement exerciseTime=new DataElement("ExerciseTime",
                  DataElement.elementTypes.Int, "[0-9]+", true, 0);
          workoutStat.subElements.add(exerciseType);
          workoutStat.subElements.add(exerciseTime);
        }
        elem.subElements.add(workoutStat);
      }
    } // for WorkoutStatsUpdateRequest

    return elem;
  }
}
