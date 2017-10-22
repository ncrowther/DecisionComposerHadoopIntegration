import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

import odmengine.DecisionEngineRunner;
import odmengine.IRuleEngineRunner;
import odmengine.RestEngineRunner;

public class JsonTester {

	public static String clearTimestamp(long span) {
		long msec = span % 1000;
		span = (span - msec) / 1000;
		long sec = span % 60;
		long min = (span - sec) / 60;
		return "" + min + " mins :" + sec + " sec :" + msec + " msec";
	}

	public static void runRuleTest(IRuleEngineRunner de) throws Exception {

		//final String host = "localhost:9081";
		//final String host = "localhost:9090";
		
		//final String rulesetPath = "/MortgageSegmentation/1.0/RealTime/1.0";
		//final String rulesetPath = "/LoanValidationRuleApp/1.0/LoanValidation/1.0";
		//final String rulesetPath = "/CarRentalRuleApp/1.0/CarRental/1.0";
		final String rulesetPath = "/PassengerNameRecordCheckRuleApp/1.0/PassengerNameRecordCheck/1.0";
		final String host = "brsv2-b6dcdca2.ng.bluemix.net";
		final String resUser = "resAdmin";
		final String resPwd = "u7ralgw4gqwm";
		//final String resPwd = "resAdmin";
		
		//final String executionPwd = "resAdmin";
		final String executionPwd = "Basic cmVzQWRtaW46dTdyYWxndzRncXdt";

		final boolean https = true; // ****true for bluemix
		//final boolean https = false; // ****true for bluemix

	 	de.initialise(rulesetPath, host, resUser, resPwd, executionPwd, https);
			
		long executionStart = System.currentTimeMillis();
		final int nbRecords = 1000;

		String jsonData =readFile(".\\testpayloads\\pnr.json");
		//String jsonData =readFile(".\\testpayloads\\loanvalidationPayload.json");
		//String jsonData =readFile(".\\testpayloads\\carrental.json");
		
		for (int i = 0; i < nbRecords; i++) {
			
			String result = de.runRules(jsonData);
			
			writeFile(".\\testpayloads\\testresult.json", result);

			System.out.println("***RESULT: " + result);
		}

		long executionEnd = System.currentTimeMillis();

		long totalTime = executionEnd - executionStart;
		System.out.println("RULESET EXECUTION TOTAL TIME     "
				+ clearTimestamp(totalTime));
		System.out.println("AVERAGE PROCESSING     " + ((double) totalTime)
				/ nbRecords + " ms per record.");

		double tps = nbRecords / (double) (totalTime / 1000.0);
		System.out.println("TRANSACTION PER SEC    " + tps);
	}
	
  public static void writeFile(String filename, String content) throws IOException {
	  
		      File file = new File(filename);
		      
		      // creates the file
		      file.createNewFile();
		      
		      // creates a FileWriter Object
		      FileWriter writer = new FileWriter(file); 
		      
		      // Writes the content to the file
		      writer.write(content); 
		      writer.flush();
		      writer.close();

		      // Creates a FileReader Object
		      FileReader fr = new FileReader(file); 
		      char [] a = new char[50];
		      fr.read(a);   // reads the content to the array
		      
		      for(char c : a)
		         System.out.print(c);   // prints the characters one by one
		      fr.close();
		   }
  
  
	private static String readFile(String filename) {

		StringBuilder sb = new StringBuilder();
		File file = new File(filename);
		Scanner sc = null;
		try {
			sc = new Scanner(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		while (sc.hasNextLine()) {
			sb.append(sc.next());
		}

		sc.close();
		return sb.toString();

	}
  
  
	
	// ************************ MAIN ********************************
	
	public static void main(String[] args) throws Exception {

	    //IRuleEngineRunner restEngine = new RestEngineRunner();
		//JsonTester.runRuleTest(restEngine);
		
		IRuleEngineRunner embeddedEngine = new DecisionEngineRunner();
		JsonTester.runRuleTest(embeddedEngine);
	}

}
