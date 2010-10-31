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
package org.jboss.ejb3.instantiator.deployer;

import org.jboss.beans.metadata.api.annotations.Inject;
import org.jboss.deployers.spi.DeploymentException;
import org.jboss.deployers.structure.spi.DeploymentUnit;
import org.jboss.ejb3.instantiator.spi.BeanInstantiator;

/**
 * Extension of the {@link SingletonBeanInstantiatorDeployer} used in testing.
 * Caches statically the last deployment received.  Not thread-safe.
 *
 * @author <a href="mailto:andrew.rubinger@jboss.org">ALR</a>
 * @version $Revision: $
 */
public class TestBeanInstantiatorDeployer extends SingletonBeanInstantiatorDeployer
{

   /**
    * The last deployment processed
    */
   static DeploymentUnit lastDeployment;

   public TestBeanInstantiatorDeployer(@Inject final BeanInstantiator beanInstantiator)
   {
      super(beanInstantiator);
   }

   /**
    * {@inheritDoc}
    * @see org.jboss.ejb3.instantiator.deployer.SingletonBeanInstantiatorDeployer#deploy(org.jboss.deployers.structure.spi.DeploymentUnit)
    */
   @Override
   public void deploy(final DeploymentUnit unit) throws DeploymentException
   {
      // Call business logic
      super.deploy(unit);

      // Cache the last deployment so the test can access it
      lastDeployment = unit;
   }

   @Override
   boolean isEjb3ModuleDeployment(final DeploymentUnit unit)
   {
      // Assume for testing we're an EJB3 deployment
      return true;
   }

}
