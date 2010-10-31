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

import java.util.logging.Logger;

import org.jboss.deployers.spi.DeploymentException;
import org.jboss.deployers.spi.deployer.helpers.AbstractDeployer;
import org.jboss.deployers.structure.spi.DeploymentUnit;
import org.jboss.metadata.ejb.jboss.JBossEnterpriseBeanMetaData;
import org.jboss.metadata.ejb.jboss.JBossEnterpriseBeansMetaData;
import org.jboss.metadata.ejb.jboss.JBossMetaData;

/**
 * Mock implementation of a deployer to construct EJB3 metadata from dummy
 * deployments
 *
 * @author <a href="mailto:andrew.rubinger@jboss.org">ALR</a>
 * @version $Revision: $
 */
public class MockEjb3Deployer extends AbstractDeployer
{

   private static final Logger log = Logger.getLogger(MockEjb3Deployer.class.getName());

   public MockEjb3Deployer()
   {
      this.addOutput(JBossEnterpriseBeansMetaData.class);
   }

   /**
    * {@inheritDoc}
    * @see org.jboss.deployers.spi.deployer.Deployer#deploy(org.jboss.deployers.structure.spi.DeploymentUnit)
    */
   public void deploy(final DeploymentUnit unit) throws DeploymentException
   {
      // Create some mock EJB3 metadata and attach it
      final JBossEnterpriseBeansMetaData ejbs = new JBossEnterpriseBeansMetaData();
      final JBossMetaData jbmd = new JBossMetaData();
      jbmd.setVersion("3.0");
      ejbs.setEjbJarMetaData(jbmd);
      JBossEnterpriseBeanMetaData ejb = new JBossEnterpriseBeanMetaData()
      {

         /**
          * 
          */
         private static final long serialVersionUID = 1L;

         @Override
         protected String getDefaultInvokerName()
         {
            // TODO Auto-generated method stub
            return null;
         }

         @Override
         public String getDefaultConfigurationName()
         {
            // TODO Auto-generated method stub
            return null;
         }

         @Override
         public String determineJndiName()
         {
            // TODO Auto-generated method stub
            return null;
         }
      };
      ejb.setName("MockEJB");
      ejbs.add(ejb);
      unit.addAttachment(JBossEnterpriseBeansMetaData.class, ejbs);

      log.info("Added: " + ejbs);

   }

}
