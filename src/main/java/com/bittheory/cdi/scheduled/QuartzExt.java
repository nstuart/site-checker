/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bittheory.cdi.scheduled;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.*;
import org.quartz.*;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author nstuart1
 */
public class QuartzExt implements Extension {

    private Scheduler scheduler;
    static BeanManager beanManager;
    private Logger log = LoggerFactory.getLogger(QuartzExt.class);

    public void createScheduler(@Observes BeforeBeanDiscovery bbd) throws SchedulerException {
        scheduler = StdSchedulerFactory.getDefaultScheduler();
        log.debug("Scheduler constructed.");
    }

    public void start(@Observes AfterDeploymentValidation adv, BeanManager beanManager) {
        this.beanManager = beanManager;
        try {
            scheduler.start();
            log.debug("Scheduler started.");
        } catch (SchedulerException ex) {
            throw new RuntimeException();
        }
    }

    public void schedule(@Observes ProcessAnnotatedType pat) {
        AnnotatedType t = pat.getAnnotatedType();
        Scheduled sc = t.getAnnotation(Scheduled.class);
        if (sc != null) {
            Class<Runnable> jobType = t.getJavaClass().asSubclass(Runnable.class); //make sure our jobs are Runnable.
            if (jobType == null) {
                log.error("Given class; {} does implement Runnable.", t.getJavaClass().getCanonicalName());
                return ;
            }
            JobDetail job = JobBuilder.newJob(CdiJob.class).
                    usingJobData(CdiJob.JOB_CLASS_NAME, jobType.getName()).
                    build();
            Trigger trig = TriggerBuilder.newTrigger().
                    withSchedule(CronScheduleBuilder.cronSchedule(sc.value())).
                    build();
            try {
                scheduler.scheduleJob(job, trig);
            } catch (SchedulerException se) {
                throw new RuntimeException(se);
            }
        }
    }
}
