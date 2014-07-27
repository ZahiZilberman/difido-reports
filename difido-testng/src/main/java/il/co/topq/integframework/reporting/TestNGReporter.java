package il.co.topq.integframework.reporting;

import il.co.topq.difido.client.DifidoClient;
import il.co.topq.report.Configuration;
import il.co.topq.report.Configuration.ConfigProps;

import org.testng.ITestContext;
import org.testng.TestListenerAdapter;

public class TestNGReporter extends TestListenerAdapter {

	protected final DifidoClient client = DifidoClient.build(Configuration.INSTANCE.read(ConfigProps.BASE_URI));
	int execution;

	@Override
	public void onStart(ITestContext testContext) {
		super.onStart(testContext);
		execution = client.addExecution();
	}

	@Override
	public void onFinish(ITestContext testContext) {
		client.endExecution(execution);
		super.onFinish(testContext);
	}

}
