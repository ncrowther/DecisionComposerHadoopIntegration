package odmengine;

import java.util.Map;

public interface IRuleEngineRunner {

	/**
	 * Runs the engine with the rules
	 */
	public abstract void initialise(String rulesetVersion, String host,
			String user, String resPwd, String executionPwd, boolean https) throws Exception;

	/**
	 * Runs the engine with the provided ruleset archive
	 */
	public abstract String runRules(String jsonData);
	

}