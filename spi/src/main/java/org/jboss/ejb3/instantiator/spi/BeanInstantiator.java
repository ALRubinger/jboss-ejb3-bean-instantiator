/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
  *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.ejb3.instantiator.spi;

/**
 * Contract of a component responsible for creation of EJB bean 
 * and instances.  Though the EJB specification dictates 
 * the use of a no-arg constructor, contextual EJBs defined by JSR-299 
 * provides for additional features such as injected constructor parameters, 
 * so alternate implementations of this type may be used by the 
 * EJB container.
 *
 * @author <a href="mailto:andrew.rubinger@jboss.org">ALR</a>
 * @version $Revision: $
 */
public interface BeanInstantiator
{
   //-------------------------------------------------------------------------------------||
   // Contracts --------------------------------------------------------------------------||
   //-------------------------------------------------------------------------------------||

   /**
    * Creates a new instance of the specified implementation {@link Class}
    * using the supplied parameters.  While the implementation class 
    * must be specified, <code>null</code> is a valid value for the parameters,
    * and will be treated as a 0-length array.
    * 
    * @throws IllegalArgumentException If the implementation class was not specified
    * or the implementation does not support parameters (while they've been supplied).
    * @throws InvalidConstructionParamsException If the implementation of this type does 
    * not support the parameters supplied.
    * @throws BeanInstantiationException If an unexpected error occurred in constructing the bean
    * instance
    */
   <T> T create(Class<T> implClass, Object[] parameters) throws IllegalArgumentException,
         InvalidConstructionParamsException, BeanInstantiationException;
}
