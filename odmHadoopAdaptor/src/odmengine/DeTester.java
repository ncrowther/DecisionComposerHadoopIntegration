	package odmengine;

	import java.util.HashMap;
	import java.util.Map;

	public class DeTester {

		public static String clearTimestamp(long span) {
			long msec = span % 1000;
			span = (span - msec) / 1000;
			long sec = span % 60;
			long min = (span - sec) / 60;
			return "" + min + " mins :" + sec + " sec :" + msec + " msec";
		}

		public static void runRuleTest(IRuleEngineRunner de) throws Exception {
			
			final String host = "brsv2-7a461af4.ng.bluemix.net"; //"localhost:9080";  
			final String rulesetPath = "/PNRCheckRuleApp/1.0/PNRCheckRuleset/1.0"; //"/validatePnrApp/1.0/validatePnr/1.0";
			final String resUser = "resAdmin";
			final String resPwd =  "1hv0ftlm0j9ys"; //"resAdmin"; 							
			
			final String executionPwd = "resAdmin";
			final boolean https = false; // ****true for bluemix

			de.initialise(rulesetPath, host, resUser, resPwd, executionPwd, https);

			long executionStart = System.currentTimeMillis();

			final int nbRecords = 1;

			for (int i = 0; i < nbRecords; i++) {

				Map<String, Object> params = new HashMap<String, Object>();

				params.put("PassportNumber", "9087089348");
				params.put("CustomerName", "Fred Flintstone");
				params.put("DateOfBirth", "1992-02-23");
				params.put("FlightNumber", "BA5654");
				params.put("FlightDate", "2012-12-31");
				params.put("Route", "BOCJFK");
						
				de.runRules(params);
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

	}
