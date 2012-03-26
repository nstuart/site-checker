/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bittheory.cdi.scheduled;

import java.util.Set;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 *
 * @author nstuart1
 */
public class CdiJob implements Job {

    public static final String JOB_CLASS_NAME = "CDI_JOB_CLASS_NAME";

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        JobDataMap jobData = jec.getJobDetail().getJobDataMap();
        String className = jobData.getString(JOB_CLASS_NAME);
        Class<Runnable> jobClass;
        try {
            jobClass = (Class<Runnable>) Class.forName(className).asSubclass(Runnable.class);
        } catch (ClassNotFoundException ex) {
            throw new JobExecutionException(ex);
        }

        BeanManager bm = QuartzExt.beanManager;
        Set<Bean<?>> beans = bm.getBeans(jobClass);
        Bean bean = bm.resolve(beans);
        CreationalContext ctx = bm.createCreationalContext(bean);
        Runnable job = (Runnable) bm.getReference(bean, jobClass, ctx);
        try {
            job.run();
        } finally {
            bean.destroy(job, ctx);
        }
    }
}
