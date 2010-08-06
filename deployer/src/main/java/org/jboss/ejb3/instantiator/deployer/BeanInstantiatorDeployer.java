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

import org.jboss.deployers.spi.DeploymentException;
import org.jboss.deployers.spi.deployer.helpers.AbstractDeployer;
import org.jboss.deployers.structure.spi.DeploymentUnit;
import org.jboss.ejb3.instantiator.spi.AttachmentNames;
import org.jboss.ejb3.instantiator.spi.BeanInstantiator;
import org.jboss.logging.Logger;
import org.jboss.metadata.ejb.jboss.JBossMetaData;

/**
 * VDF Deployer to attach a {@link BeanInstantiator} implementation
 * to the current EJB3 {@link DeploymentUnit}
 *
 * @author <a href="mailto:andrew.rubinger@jboss.org">ALR</a>
 * @version $Revision: $
 */

public class BeanInstantiatorDeployer extends AbstractDeployer
{

   // ------------------------------------------------------------------------------||
   // Class Members ----------------------------------------------------------------||
   // ------------------------------------------------------------------------------||

   /**
    * Logger
    */
   private static final Logger log = Logger.getLogger(BeanInstantiatorDeployer.class);

   // ------------------------------------------------------------------------------||
   // Instance Members -------------------------------------------------------------||
   // ------------------------------------------------------------------------------||

   /**
    * {@link BeanInstantiator} implementation to attach to the {@link DeploymentUnit}
    * if it's an EJB3 deployment
    */
   private final BeanInstantiator beanInstantiator;

   // ------------------------------------------------------------------------------||
   // Constructor ------------------------------------------------------------------||
   // ------------------------------------------------------------------------------||

   public BeanInstantiatorDeployer(final BeanInstantiator beanInstantiator)
   {
      this.beanInstantiator = beanInstantiator;
   }

   // ------------------------------------------------------------------------------||
   // Required Implementations -----------------------------------------------------||
   // ------------------------------------------------------------------------------||

   /**
    * {@inheritDoc}
    * @see org.jboss.deployers.spi.deployer.Deployer#deploy(org.jboss.deployers.structure.spi.DeploymentUnit)
    */
   public void deploy(final DeploymentUnit unit) throws DeploymentException
   {
      // If not an EJB3 deployment, take no action
      if (!this.isEjb3Deployment(unit))
      {
         return;
      }

      // Ensure the instantiator was injected
      if (beanInstantiator == null)
      {
         throw new IllegalStateException("Bean instantiator implemenentation was not injected");
      }

      // Attach 
      unit.addAttachment(AttachmentNames.NAME_BEAN_INSTANCE_INSTANTIATOR, beanInstantiator);
      if (log.isTraceEnabled())
      {
         log.trace("Using bean instantiator " + beanInstantiator + " for " + unit);
      }
   }

   // ------------------------------------------------------------------------------||
   // Helper Methods ---------------------------------------------------------------||
   // ------------------------------------------------------------------------------||

   /*
    * These may be overridden for testing purposes
    */

   /**
    * Returns whether this is an EJB3 Deployment, determining if we should take action
    * @param unit
    * @return
    */
   boolean isEjb3Deployment(final DeploymentUnit unit)
   {
      // Obtain the Merged Metadata
      final JBossMetaData md = unit.getAttachment(JBossMetaData.class);

      // If metadata's not present as an attachment, return
      if (md == null)
      {
         return false;
      }

      // If this is not an EJB3 Deployment, return
      if (!md.isEJB3x())
      {
         return false;
      }

      // Meets conditions
      return true;
   }

}
