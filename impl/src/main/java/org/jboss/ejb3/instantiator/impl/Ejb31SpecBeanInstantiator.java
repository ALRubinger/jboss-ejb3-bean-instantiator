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
package org.jboss.ejb3.instantiator.impl;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.jboss.ejb3.instantiator.spi.BeanInstantiationException;
import org.jboss.ejb3.instantiator.spi.BeanInstantiator;
import org.jboss.ejb3.instantiator.spi.InvalidConstructionParamsException;

/**
 * Implementation of {@link BeanInstantiator} strictly conforming to the
 * EJB 3.1 Specification.  Any non-0-length or non-null parameter arguments to 
 * {@link Ejb31SpecBeanInstantiator#create(Class, Object[])}
 * will result in {@link InvalidConstructionParamsException}
 *
 * @author <a href="mailto:andrew.rubinger@jboss.org">ALR</a>
 * @version $Revision: $
 */
public class Ejb31SpecBeanInstantiator implements BeanInstantiator
{

   //-------------------------------------------------------------------------------------||
   // Class Members ----------------------------------------------------------------------||
   //-------------------------------------------------------------------------------------||

   /**
    * Logger
    */
   private static final Logger log = Logger.getLogger(Ejb31SpecBeanInstantiator.class.getName());

   /**
    * Message used in reporting exceptions during instantiation
    */
   private static final String MSG_PREFIX_INSTANTIATION_EXCEPTION = "Could not create new instance with no arguments of: ";

   //-------------------------------------------------------------------------------------||
   // Required Implementations -----------------------------------------------------------||
   //-------------------------------------------------------------------------------------||

   /**
    * {@inheritDoc}
    * @see org.jboss.ejb3.instantiator.spi.BeanInstantiator#create(java.lang.Class, java.lang.Object[])
    */
   public <T> T create(final Class<T> implClass, final Object[] parameters) throws IllegalArgumentException,
         InvalidConstructionParamsException, BeanInstantiationException
   {
      // Precondition checks
      if (parameters != null && parameters.length > 0)
      {
         throw InvalidConstructionParamsException
               .newInstance("EJB Specification requires a no-argument constructor be invoked for bean instances on "
                     + implClass.getName());
      }

      // Instantiate
      final T obj;
      try
      {
         obj = implClass.newInstance();
      }
      catch (final InstantiationException e)
      {
         throw BeanInstantiationException.newInstance(MSG_PREFIX_INSTANTIATION_EXCEPTION + implClass, e);
      }
      catch (final IllegalAccessException e)
      {
         throw BeanInstantiationException.newInstance(MSG_PREFIX_INSTANTIATION_EXCEPTION + implClass, e);
      }

      // Log
      if (log.isLoggable(Level.FINER))
      {
         log.finer("Created: " + obj + "; instance of " + implClass.getName());
      }

      // Return
      return obj;
   }

}
