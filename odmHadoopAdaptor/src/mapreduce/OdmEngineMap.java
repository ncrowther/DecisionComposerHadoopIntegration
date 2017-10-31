package mapreduce;

import java.io.IOException;

import java.util.logging.Level;
import java.util.logging.Logger;

import odmengine.DecisionEngineRunner;
import odmengine.IRuleEngineRunner;
import odmengine.RestEngineRunner;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;

public class OdmEngineMap extends MapReduceBase implements
		Mapper<LongWritable, Text, LongWritable, Text> {

	private static final Logger LOG = Logger.getLogger(OdmEngineMap.class.getName());

	private static IRuleEngineRunner runner;
	private Text outputText = new Text();
	private String inputFile;
	private int outputLineCount = 0;

	/**
	 * Hadoop map method is invoked for each row of data. Each json object is 
	 *  sent to the rule engine, the rules are executed and the json result is written back.
	 */
	public void map(LongWritable key, Text value,
			OutputCollector<LongWritable, Text> output, Reporter reporter)
			throws IOException {

		String jsonData = value.toString();
		
		LOG.info("Processing Row: " + jsonData);

		executeRuleEngine(jsonData, output);
	}

	/**
	 * Execute the batch of csv rows against the rule engine
	 * 
	 * @param batch
	 *            the batch of csv rows
	 * @param output
	 *            the data sink
	 * @throws IOException
	 */
	private void executeRuleEngine(String jsonData,
			OutputCollector<LongWritable, Text> output) throws IOException {
		
		String response = runner.runRules(jsonData);

		if (response != null && response.length() > 0) {
			outputText.set(response);
			output.collect(new LongWritable(outputLineCount++), outputText);
		}

	}

	@Override
	public void configure(JobConf job) {

		inputFile = job.get("map.input.file");

		int fileNamePos = inputFile.lastIndexOf('/') + 1;
		inputFile = inputFile.substring(fileNamePos);
		LOG.info("inputFile: " + inputFile);

		String rulesetVersion = job.get("rulesetVersion");
		LOG.info("rulesetVersion: " + rulesetVersion);

		String host = job.get("host");
		LOG.info("Host: " + host);

		String resUser = job.get("resUser");
		LOG.info("ResUser: " + resUser);

		String resPwd = job.get("resPwd");
		LOG.info("ResPwd: " + resPwd);
		
		String executionPwd = job.get("executionPwd");
		LOG.info("ExecutionPwd: " + executionPwd);
		
		String blueMixMode = job.get("blueMixMode");
		LOG.info("BlueMixMode: " + blueMixMode);	
		
		if (blueMixMode.equalsIgnoreCase("true")){
			LOG.info("BlueMix Mode set ");	
			runner = new RestEngineRunner(); 
		} else {
			LOG.info("Enterprise Mode set ");	
			runner = new DecisionEngineRunner();
		}
		
		String https = job.get("https");
		LOG.info("Https: " + https);
		
		boolean httpsMode = false;
		if (https.equalsIgnoreCase("true")){
			LOG.info("Https enabled ");	
			httpsMode = true;
		} else {
			LOG.info("Http enabled ");	
			httpsMode = false;
		}		
		
		// Initialise ruleset and parameter signature using REST API
		try {
			runner.initialise(rulesetVersion, host, resUser, resPwd, executionPwd, httpsMode);
		} catch (Exception e) {
			LOG.log(Level.SEVERE, "Failed to initialise connection to rule service: " + e);
			e.printStackTrace();
			System.exit(-1);
		}

	}

}
