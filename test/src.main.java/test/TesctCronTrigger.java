package test;


import java.text.ParseException;
import org.quartz.SimpleTrigger;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;


public class TesctCronTrigger {
	public SchedulerFactory schedFact;
	public Scheduler sched;
	
	public TesctCronTrigger() throws ParseException, java.text.ParseException {
		try {
			//schedule creating and start
			   schedFact = new StdSchedulerFactory();
			   sched = schedFact.getScheduler();
			   sched.start();
			
			   //Job1 creating


			   JobDetail job1 = new JobDetail("job1","group1",TestJob.class);
			   //Triggger1 creating
			   CronTrigger trigger1 = new CronTrigger("trigger1","group1","0 0/1 * 1/1 * ? *");
			   sched.scheduleJob(job1, trigger1);

			   //job2 creating and enroll
			 //  JobDetail job2 = new JobDetail("job2","group2",TestJob2.class);
			  // CronTrigger trigger2 = new CronTrigger("trigger2","group2","0 0/2 * 1/1 * ? *");
			   //sched.scheduleJob(job2, trigger2);
			    
		}catch(SchedulerException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws ParseException, java.text.ParseException {
		System.out.println("TestcronTrigger start");
		new TesctCronTrigger();

	}

}
