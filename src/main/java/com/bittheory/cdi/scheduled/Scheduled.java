/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bittheory.cdi.scheduled;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author nstuart1
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Scheduled {
    String value(); //Schedule expressed as a cron expression.
}
