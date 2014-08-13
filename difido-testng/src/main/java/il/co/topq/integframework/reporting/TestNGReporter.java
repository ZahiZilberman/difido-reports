package il.co.topq.integframework.reporting;

import static il.co.topq.difido.model.Enums.Status.*;
import il.co.topq.difido.client.DifidoClient;
import il.co.topq.difido.model.execution.MachineNode;
import il.co.topq.difido.model.execution.ScenarioNode;
import il.co.topq.difido.model.execution.TestNode;
import il.co.topq.difido.model.test.TestDetails;
import il.co.topq.report.Configuration;
import il.co.topq.report.Configuration.ConfigProps;

import java.util.List;

import org.testng.IReporter;
import org.testng.ISuite;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.internal.IResultListener2;
import org.testng.xml.XmlSuite;

public class TestNGReporter extends org.testng.Reporter implements IReporter, IResultListener2 {

	protected final DifidoClient client = DifidoClient.build(Configuration.INSTANCE.read(ConfigProps.BASE_URI));
	protected int executionId;
	protected int machineId;
	protected int scenarioId = -1;
	protected int testId;

	public TestNGReporter() {
		executionId = client.addExecution();
		machineId = client.addMachine(executionId, new MachineNode(System.getProperty("user.name")));

	}

	@Override
	public void onStart(ITestContext testContext) {
		if (scenarioId == -1) {
			scenarioId = client.addRootScenario(executionId, machineId, new ScenarioNode(testContext.getSuite().getName()));
		} else {
			client.addSubScenario(executionId, machineId, scenarioId, new ScenarioNode(testContext.getSuite().getName()));
		}

	}

	@Override
	public void onFinish(ITestContext testContext) {
		client.endExecution(executionId);
	}

	@Override
	public void onTestStart(ITestResult result) {
		TestNode node = new TestNode();
		node.setStatus(in_progress);
		testId = client.addTest(executionId, machineId, scenarioId, node);
		TestDetails details = new TestDetails(result.getName());
		details.setDescription(result.getTestName());
		client.addTestDetails(executionId, machineId, scenarioId, testId, details);
	}

	@Override
	public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTestSuccess(ITestResult result) {
		client.getTest(executionId, machineId, scenarioId, testId).setStatus(success);
	}

	@Override
	public void onTestFailure(ITestResult result) {
		client.getTest(executionId, machineId, scenarioId, testId).setStatus(failure);

	}

	@Override
	public void onTestSkipped(ITestResult result) {
		client.getTest(executionId, machineId, scenarioId, testId).setStatus(skipped);

	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		client.getTest(executionId, machineId, scenarioId, testId).setStatus(warning);
	}

	@Override
	public void onConfigurationSuccess(ITestResult itr) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onConfigurationFailure(ITestResult itr) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onConfigurationSkip(ITestResult itr) {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeConfiguration(ITestResult tr) {
		// TODO Auto-generated method stub

	}

}
