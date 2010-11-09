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
 * Utility class for registration of the {@link BeanInstantiator}
 * instances
 * 
 * @author <a href="mailto:andrew.rubinger@jboss.org">ALR</a>
 */
public class BeanInstantiatorRegistration
{

   /**
    * Namespace udner which we'll install the BI
    */
   public static final String NAMESPACE_PREFIX = "org.jboss.ejb.bean.instantiator/";

   /**
    * No instances
    */
   private BeanInstantiatorRegistration()
   {
      throw new UnsupportedOperationException("No instances permitted");
   }

   /**
    * Obtains the expected name under which the {@link BeanInstantiator} is to be registered
    * for the EJB of specified name within the specified {@link DeploymentUnit}.
    * @param unit
    * @param ejb
    * @return
    */
   public static String getInstantiatorRegistrationName(final String appName, final String moduleName,
         final String ejbName)
   {
      // Precondition checks
      if (moduleName == null || moduleName.length() == 0)
      {
         throw new IllegalArgumentException("module name must be specified");
      }
      if (ejbName == null || ejbName.length() == 0)
      {
         throw new IllegalArgumentException("ejb-name must be specified");
      }

      final StringBuilder sb = new StringBuilder();
      sb.append(NAMESPACE_PREFIX);
      final char delimiter = '/';
      if (appName != null)
      {
         sb.append(appName);
         sb.append(delimiter);
      }
      sb.append(moduleName);
      sb.append(delimiter);
      sb.append(ejbName);
      return sb.toString();
   }
}
