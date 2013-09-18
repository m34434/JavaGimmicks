/**
 * Includes several helper classes for using CDI (Java's context and dependency injection API).
 * <p>
 * It mainly focuses on features enabling CDI in existing applications that are not built from scratch
 * around that API.
 * <p>
 * <b>Examples:</b>
 * <ul>
 * <li>Access the CDI context ({@link javax.enterprise.inject.spi.BeanManager}, lookups, etc.) via
 * 'plain old' Java (without using @{@link javax.inject.Inject})</li>
 * <li>Build CDI injections via fluent API</li>
 * <li>Bridge CDI with {@link net.sf.javagimmicks.lang.Factory} API from <i>GimmickUtils-core</i></li>
 * </ul>
 */
package net.sf.javagimmicks.cdi;

