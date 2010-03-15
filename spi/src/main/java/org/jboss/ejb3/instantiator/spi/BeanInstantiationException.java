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
 * Thrown when the implementation of {@link BeanInstantiator#create(Class, Object[])}
 * encounters an error in creating a new bean instance; the underlying cause
 * is guaranteed to be available from @link {@link BeanInstantiationException}{@link #getCause()}.
 * Provided as not all {@link BeanInstantiator} implementations will use a JDK
 * backend, so provides safe translation for instance from {@link InstantiationException}.
 *
 * @author <a href="mailto:andrew.rubinger@jboss.org">ALR</a>
 * @version $Revision: $
 */
public class BeanInstantiationException extends Exception
{

   //-------------------------------------------------------------------------------------||
   // Class Members ----------------------------------------------------------------------||
   //-------------------------------------------------------------------------------------||

   /**
    * serialVersionUID
    */
   private static final long serialVersionUID = 1L;

   //-------------------------------------------------------------------------------------||
   // Constructors -----------------------------------------------------------------------||
   //-------------------------------------------------------------------------------------||

   /**
    * Creates a new {@link BeanInstantiationException} with the 
    * specified message and cause.  Private so we can perform precondition checks; 
    * callers should use {@link BeanInstantiationException#newInstance(String,Throwable)}.
    */
   private BeanInstantiationException(final String message, final Throwable cause)
   {
      super(message, cause);
   }

   //-------------------------------------------------------------------------------------||
   // Factory Methods -------------------------------------------------------------------||
   //-------------------------------------------------------------------------------------||

   /**
    * Creates a new {@link BeanInstantiationException} with the 
    * specified message and cause.  Both arguments required.
    * 
    * @throws IllegalArgumentException If the message or cause is not specified
    */
   public static BeanInstantiationException newInstance(final String message, final Throwable cause)
         throws IllegalArgumentException
   {
      // Precondition checks
      if (message == null || message.length() == 0)
      {
         throw new IllegalArgumentException("Message must be specified");
      }
      if (cause == null)
      {
         throw new IllegalArgumentException("Cause must be specified");
      }

      // Return new instance.
      return new BeanInstantiationException(message, cause);
   }

}
