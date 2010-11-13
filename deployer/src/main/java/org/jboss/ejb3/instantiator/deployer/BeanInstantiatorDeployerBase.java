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

import java.util.Iterator;

import org.jboss.beans.metadata.api.annotations.Inject;
import org.jboss.beans.metadata.plugins.builder.BeanMetaDataBuilderFactory;
import org.jboss.beans.metadata.spi.builder.BeanMetaDataBuilder;
import org.jboss.dependency.spi.ControllerContext;
import org.jboss.deployers.spi.DeploymentException;
import org.jboss.deployers.spi.deployer.helpers.AbstractDeployer;
import org.jboss.deployers.structure.spi.DeploymentUnit;
import org.jboss.ejb3.instantiator.spi.BeanInstantiator;
import org.jboss.ejb3.instantiator.spi.BeanInstantiatorRegistration;
import org.jboss.kernel.Kernel;
import org.jboss.logging.Logger;
import org.jboss.metadata.ejb.jboss.JBossEnterpriseBeanMetaData;
import org.jboss.metadata.ejb.jboss.JBossEnterpriseBeansMetaData;
import org.jboss.metadata.ejb.jboss.JBossMetaData;
import org.jboss.reloaded.naming.deployers.javaee.JavaEEComponentInformer;

/**
 * VDF Deployer base to attach a {@link BeanInstantiator} implementation
 * to the current EJB3 {@link DeploymentUnit}
 *
 * @author <a href="mailto:andrew.rubinger@jboss.org">ALR</a>
 * @version $Revision: $
 */
public abstract class BeanInstantiatorDeployerBase extends AbstractDeployer
{

   // ------------------------------------------------------------------------------||
   // Class Members ----------------------------------------------------------------||
   // ------------------------------------------------------------------------------||

   /**
    * Logger
    */
   private static final Logger log = Logger.getLogger(BeanInstantiatorDeployerBase.class);

   // ------------------------------------------------------------------------------||
   // Instance Members -------------------------------------------------------------||
   // ------------------------------------------------------------------------------||

   /**
    * The kernel used to install {@link BeanInstantiator} implementations
    */
   private Kernel kernel;

   /**
    * Used to get hold of the app/module/component names out of a deployment unit
    */
   private JavaEEComponentInformer javaEEComponentInformer;

   // ------------------------------------------------------------------------------||
   // Constructor ------------------------------------------------------------------||
   // ------------------------------------------------------------------------------||

   /**
    * Create a new deployer instance, setting the input to {@link JBossMetaData}
    * and the output as {@link BeanInstantiator}
    */
   public BeanInstantiatorDeployerBase()
   {
      this.addInput(JBossMetaData.class);
      this.addOutput(BeanInstantiator.class);
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
      if (!this.isEjb3ModuleDeployment(unit))
      {
         return;
      }

      // Get the EJB Metadata
      final JBossEnterpriseBeansMetaData ejbs = this.getEjbModuleMetadata(unit);
      assert ejbs != null : "No EJBs found in this deployment, the deployer should have skipped this operation earlier";
      final Iterator<JBossEnterpriseBeanMetaData> it = ejbs.iterator();
      while (it.hasNext())
      {
         // Get the EJB
         final JBossEnterpriseBeanMetaData ejb = it.next();
         // Get an instantiator to use for the EJB
         final BeanInstantiator instantiator = this.getBeanInstantiator(ejb);
         // Ensure the instantiator was supplied
         if (instantiator == null)
         {
            throw new IllegalStateException("Bean instantiator implemenentation was not supplied");
         }
         // Construct a name
         final String mcBindName = this.getRegistrationNameFromDeploymentUnit(unit, ejb);
         final BeanMetaDataBuilder bmdb = BeanMetaDataBuilderFactory.createBuilder(mcBindName,
               BeanInstantiator.class.getName());
         this.processMetadata(bmdb, unit, ejb);
         try
         {
            kernel.getController().install(bmdb.getBeanMetaData(), instantiator);
         }
         catch (final Throwable e)
         {
            throw new DeploymentException("Could not install bean instantiator", e);
         }
         log.info("Installed " + instantiator + " into MC at " + mcBindName);
      }
   }

   /**
    *
    */
   @Override
   public void undeploy(final DeploymentUnit unit)
   {
      // If not an EJB3 deployment, take no action
      if (!this.isEjb3ModuleDeployment(unit))
      {
         return;
      }

      final JBossEnterpriseBeansMetaData ejbs = this.getEjbModuleMetadata(unit);
      assert ejbs != null : "No EJBs found in this deployment, the deployer should have skipped this operation earlier";
      final Iterator<JBossEnterpriseBeanMetaData> it = ejbs.iterator();
      while (it.hasNext())
      {
         // Get the EJB
         final JBossEnterpriseBeanMetaData ejb = it.next();
         final String mcBindName = this.getRegistrationNameFromDeploymentUnit(unit, ejb);
         ControllerContext context = kernel.getController().uninstall(mcBindName);
         log.info("Uninstalled " + context.getTarget() + " from MC at " + mcBindName);
      }
   }

   /**
    * Callback allowing a subclass to customize the bean metadata for the instantiator
    * (e.g. inject properties, register callbacks, etc.)
    *
    * @param beanMetaDataBuilder the metadata builder for the instantiator bean
    * @param unit the deployment unit
    * @param ejb the ejb metadata for the instantiator
    */
   protected void processMetadata(BeanMetaDataBuilder beanMetaDataBuilder, DeploymentUnit unit,
         JBossEnterpriseBeanMetaData ejb)
   {
      // empty default implementation
   }

   /**
    * Obtains the {@link BeanInstantiator} implementation to use for the 
    * specified {@link JBossEnterpriseBeanMetaData}
    *  
    * @param ebmd
    * @return
    */
   protected abstract BeanInstantiator getBeanInstantiator(JBossEnterpriseBeanMetaData ebmd);

   // ------------------------------------------------------------------------------||
   // Helper Methods ---------------------------------------------------------------||
   // ------------------------------------------------------------------------------||

   /**
    * Obtains the {@link JBossEnterpriseBeansMetaData} associated with the deployment
    * unit, else null
    */
   private JBossEnterpriseBeansMetaData getEjbModuleMetadata(final DeploymentUnit unit)
   {
      assert unit != null : "Deployment Unit must be specified";
      // Obtain the Metadata
      final JBossMetaData md = unit.getAttachment(JBossMetaData.class);
      final JBossEnterpriseBeansMetaData ejbs;
      if (md != null)
      {
         ejbs = md.getEnterpriseBeans();
      }
      else
      {
         return null;
      }
      return ejbs;
   }

   /*
    * May be overridden for testing purposes
    */

   /**
    * Returns whether this is an EJB3 Deployment, determining if we should take action
    * @param unit
    * @return
    */
   boolean isEjb3ModuleDeployment(final DeploymentUnit unit)
   {
      // Obtain the Merged Metadata
      final JBossEnterpriseBeansMetaData ejbs = this.getEjbModuleMetadata(unit);

      // If metadata's not present as an attachment, return
      if (ejbs == null)
      {
         return false;
      }

      // If this is not an EJB3 Deployment, return
      if (!ejbs.getEjbJarMetaData().isEJB3x())
      {
         return false;
      }

      // Meets conditions
      return true;
   }

   @Inject
   public void setKernel(final Kernel kernel)
   {
      this.kernel = kernel;
   }

   @Inject
   public void setJavaEEComponentInformer(final JavaEEComponentInformer informer)
   {
      this.javaEEComponentInformer = informer;
   }

   private String getRegistrationNameFromDeploymentUnit(final DeploymentUnit unit, JBossEnterpriseBeanMetaData ejb)
   {
      assert unit != null : "unit must be specified";
      final String appName = this.javaEEComponentInformer.getApplicationName(unit);
      final String moduleName = this.javaEEComponentInformer.getModuleName(unit);
      final String registrationName = BeanInstantiatorRegistration.getInstantiatorRegistrationName(appName, moduleName,
            ejb.getName());
      return registrationName;
   }

}
