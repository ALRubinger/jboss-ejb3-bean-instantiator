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

import junit.framework.Assert;

import org.junit.Test;

/**
 * Test cases to ensure that the {@link InvalidConstructionParamsException}
 * factory method is working as contracted 
 *
 * @author <a href="mailto:andrew.rubinger@jboss.org">ALR</a>
 * @version $Revision: $
 */
public class InvalidConstructionParamsExceptionUnitTest
{

   //-------------------------------------------------------------------------------------||
   // Tests ------------------------------------------------------------------------------||
   //-------------------------------------------------------------------------------------||

   /**
    * Ensures an instance can be created when following the contract
    */
   @Test
   public void canCreateInstance()
   {
      final InvalidConstructionParamsException exception = InvalidConstructionParamsException.newInstance("Message");
      Assert.assertNotNull(exception);
   }

   /**
    * Ensures that an instance with blank message cannot be created
    */
   @Test(expected = IllegalArgumentException.class)
   public void blankMessageDisallowed()
   {
      InvalidConstructionParamsException.newInstance("");
   }

   /**
    * Ensures that an instance with null message cannot be created
    */
   @Test(expected = IllegalArgumentException.class)
   public void nullMessageDisallowed()
   {
      InvalidConstructionParamsException.newInstance(null);
   }

}
