package test;
import java.util.Date;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;


public class TestJob2 implements Job {
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException{
		//execute
		System.out.println("Testjob2");
	}


}
