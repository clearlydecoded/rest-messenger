package com.clearlydecoded.commander.discovery;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * {@link DiscoverCommandHandler} annotation is a marker annotation designed to mark an
 * implementation of {@link com.clearlydecoded.commander.CommandHandler} as a handler that can be
 * then automatically discovered and registered with some
 * {@link com.clearlydecoded.commander.CommandHandlerRegistry} implementation.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface DiscoverCommandHandler {

}
