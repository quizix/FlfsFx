package com.dxw.flfs.jobs;

import com.dxw.common.services.Service;
import com.dxw.common.services.ServiceException;
import com.dxw.common.services.Services;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * Created by zhang on 2016-05-31.
 */
public class JobManager implements Service {
    public JobManager()throws SchedulerException{

    }

    private boolean started = false;
    Scheduler scheduler;
    /**
     * 初始化系统Job
     * @throws SchedulerException
     */
    public void startAll() throws SchedulerException {

        if(started)
            return;
        started = true;

        scheduleSystemStatusPollJob(scheduler);
        scheduleProductionInstructionJob(scheduler);
        scheduleSetStyStatusJob(scheduler);
        scheduleRemindJob(scheduler);

        scheduler.start();
    }

    private void scheduleRemindJob(Scheduler s) throws SchedulerException{
        JobDetail job = newJob(RemindJob.class)
                .withIdentity("remindJob", "flfsGroup")
                .build();
        //使用cronSchedule， 0 0 8/24 * * ?，表示8点开始，每12个小时执行一次
        Trigger trigger = newTrigger()
                .withIdentity("remindTrigger", "flfsGroup")
                .startNow()
                .withSchedule(
                        cronSchedule("0 0 8/24 * * ?"))
                .build();
        s.scheduleJob(job, trigger);
    }

    private void scheduleSystemStatusPollJob(Scheduler s) throws SchedulerException {
        JobDetail job = newJob(PollSystemStatus.class)
                .withIdentity("pollSystemStatusJob", "flfsGroup")
                .build();

        Trigger trigger = newTrigger()
                .withIdentity("pollSystemStatusTrigger", "flfsGroup")
                .startNow()
                .withSchedule(
                        simpleSchedule()
                                .withIntervalInMinutes(1)
                                .repeatForever())
                .build();
        s.scheduleJob(job, trigger);
    }

    private void scheduleProductionInstructionJob(Scheduler s) throws SchedulerException {
        JobDetail job = newJob(SetProductionInstructionJob.class)
                .withIdentity("productionInstructionJob", "flfsGroup")
                .build();
        //使用cronSchedule， 0 0 6/12 * * ?，表示6点开始，每12个小时执行一次
        Trigger trigger = newTrigger()
                .withIdentity("productionInstructionTrigger", "flfsGroup")
                .startNow()
                .withSchedule(
                        cronSchedule("0 0 6/12 * * ?"))
                .build();
        s.scheduleJob(job, trigger);
    }

    private void scheduleSetStyStatusJob(Scheduler s) throws SchedulerException {
        JobDetail job = newJob(SetStyStatusJob.class)
                .withIdentity("setStyStatusJob", "flfsGroup")
                .build();

        Trigger trigger = newTrigger()
                .withIdentity("setStyStatusJobPollTrigger", "flfsGroup")
                .startNow()
                .withSchedule(
                        simpleSchedule()
                                .withIntervalInMinutes(1)
                                .repeatForever())
                .build();
        s.scheduleJob(job, trigger);
    }

    @Override
    public String getName() {
        return Services.JOB_MANAGER;
    }

    @Override
    public void init() throws ServiceException {
        try {
            scheduler = new StdSchedulerFactory().getScheduler();
        } catch (SchedulerException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public void destroy() throws ServiceException {
        if(scheduler != null){
            try {
                scheduler.shutdown();
            } catch (SchedulerException e) {
                e.printStackTrace();
            }
        }
    }
}
