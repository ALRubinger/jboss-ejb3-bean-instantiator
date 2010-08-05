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

import junit.framework.TestCase;

import org.jboss.ejb3.instantiator.spi.BeanInstantiationException;
import org.jboss.ejb3.instantiator.spi.BeanInstantiator;
import org.jboss.ejb3.instantiator.spi.InvalidConstructionParamsException;
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases to ensure that the {@link Ejb31SpecBeanInstantiator}
 * is working as contracted 
 *
 * @author <a href="mailto:andrew.rubinger@jboss.org">ALR</a>
 * @version $Revision: $
 */
public class Ejb31SpecBeanInstantiatorUnitTest
{
   //-------------------------------------------------------------------------------------||
   // Class Members ----------------------------------------------------------------------||
   //-------------------------------------------------------------------------------------||

   /**
    * Empty String
    */
   private static final String EMPTY = "";

   //-------------------------------------------------------------------------------------||
   // Instance Members -------------------------------------------------------------------||
   //-------------------------------------------------------------------------------------||

   /**
    * {@link BeanInstantiator} instance under test
    */
   private BeanInstantiator instantiator;

   //-------------------------------------------------------------------------------------||
   // Lifecycle --------------------------------------------------------------------------||
   //-------------------------------------------------------------------------------------||

   /**
    * Creates the {@link BeanInstantiator} before each test run
    */
   @Before
   public void createInstantiator()
   {
      instantiator = new Ejb31SpecBeanInstantiator();
   }

   //-------------------------------------------------------------------------------------||
   // Tests ------------------------------------------------------------------------------||
   //-------------------------------------------------------------------------------------||

   /**
    * Ensures an instance can be created when using null params
    */
   @Test
   public void canCreateInstanceFromNullParams()
   {
      // Suppress the warning for unused assignment, this is in place to show type safety in creation
      @SuppressWarnings("unused")
      final Pojo pojo;
      try
      {
         pojo = instantiator.create(Pojo.class, null);
      }
      catch (final BeanInstantiationException e)
      {
         TestCase.fail("Should have created an instance, instead got " + e);
      }

   }

   /**
    * Ensures an instance can be created when using 0-length params
    */
   @Test
   public void canCreateInstanceFrom0LengthParams()
   {
      try
      {
         instantiator.create(Pojo.class, new Object[]
         {});
      }
      catch (final BeanInstantiationException e)
      {
         TestCase.fail("Should have created an instance, instead got " + e);
      }

   }

   /**
    * Ensures that passing in parameters results in {@link InvalidConstructionParamsException}
    */
   @Test(expected = InvalidConstructionParamsException.class)
   public void parametersDisallowed()
   {
      try
      {
         instantiator.create(Pojo.class, new Object[]
         {EMPTY});
      }
      catch (final BeanInstantiationException e)
      {
         TestCase.fail("Should have received " + InvalidConstructionParamsException.class.getName() + "; instead got "
               + e);
      }
   }

   //-------------------------------------------------------------------------------------||
   // Inner Classes ----------------------------------------------------------------------||
   //-------------------------------------------------------------------------------------||

   /**
    * Class used in testing
    */
   public static class Pojo
   {

   }
}
