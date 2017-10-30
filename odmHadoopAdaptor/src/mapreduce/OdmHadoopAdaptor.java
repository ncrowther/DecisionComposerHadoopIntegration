package mapreduce;

import java.util.logging.Level;
import java.util.logging.Logger;


import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.util.*;

public class OdmHadoopAdaptor extends Configured implements Tool {

	private static final Logger LOG = Logger.getLogger(OdmHadoopAdaptor.class.getName());	
	
	public int run(String[] args) throws Exception {
		JobConf job = new JobConf(getConf(), OdmHadoopAdaptor.class);
		
		int argCounter = -1;
		
		if (args.length > ++argCounter) {
			LOG.log(Level.INFO, "input dir: " + args[argCounter]);
			FileInputFormat.setInputPaths(job, new Path(args[argCounter]));
		} else {
			LOG.log(Level.SEVERE, "No Input Directory supplied");
			System.exit(-1);
		}

		if (args.length > ++argCounter) {
			FileOutputFormat.setOutputPath(job, new Path(args[argCounter]));
			LOG.log(Level.INFO,"output dir: " + args[argCounter]);
		} else {
			LOG.log(Level.SEVERE,"No Output Directory supplied");
			System.exit(-1);
		}
		
		if (args.length > ++argCounter) {
			LOG.log(Level.INFO,"rulesetVersion: " + args[argCounter]);	
			job.set("rulesetVersion", args[argCounter]);
		} else {
			LOG.log(Level.SEVERE,"No Ruleset Version supplied");
			System.exit(-1);
		}			
		
		if (args.length > ++argCounter) {
			LOG.log(Level.INFO,"host: " + args[argCounter]);	
			job.set("host", args[argCounter]);
		} else {
			LOG.log(Level.SEVERE,"No Host supplied");
			System.exit(-1);
		}	
		
		if (args.length > ++argCounter) {
			LOG.log(Level.INFO,"Res User: " + args[argCounter]);	
			job.set("resUser", args[argCounter]);
		} else {
			LOG.log(Level.SEVERE,"No RES User supplied");
			System.exit(-1);			
		}		
		
		if (args.length > ++argCounter) {
			LOG.log(Level.INFO,"Res Password: ****");	
			job.set("resPwd", args[argCounter]);
		} else {
			LOG.log(Level.SEVERE,"No RES Password supplied");
			System.exit(-1);		
		}	
		
		if (args.length > ++argCounter) {
			LOG.log(Level.INFO,"Res Execution Password: ****");	
			job.set("executionPwd", args[argCounter]);
		} else {
			LOG.log(Level.SEVERE,"No RES Execution Password supplied");	
			System.exit(-1);
		}		
		
		if (args.length > ++argCounter) {
			LOG.log(Level.INFO,"BlueMixMode: " + args[argCounter]);	
			job.set("blueMixMode", args[argCounter]);
		} else {
			LOG.log(Level.SEVERE,"Bluemix Mode NOT supplied");
			System.exit(-1);		
		}	
		
		if (args.length > ++argCounter) {
			LOG.log(Level.INFO,"HTTPS: " + args[argCounter]);	
			job.set("https", args[argCounter]);
		}  else {
			LOG.log(Level.SEVERE,"HTTPS Mode NOT supplied");
			System.exit(-1);		
		}
		
		job.setJobName("OdmHadoopAdaptor. Blumix mode: " + job.get("blueMixMode"));

		job.setOutputKeyClass(LongWritable.class);
		job.setOutputValueClass(Text.class);
		
		job.setMapperClass(OdmEngineMap.class);
		// conf.setCombinerClass(Reduce.class);
		// conf.setReducerClass(Reduce.class);

		job.setInputFormat(TextInputFormat.class);
		job.setOutputFormat(TextOutputFormat.class);

		FileInputFormat.setInputPaths(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));

		JobClient.runJob(job);
		
		return 0;
	}

	public static void main(String[] args) throws Exception {
		int res = ToolRunner.run(new Configuration(), new OdmHadoopAdaptor(),
				args);
		System.exit(res);
	}
}
