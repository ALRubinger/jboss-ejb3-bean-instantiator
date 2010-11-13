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

import org.jboss.deployers.structure.spi.DeploymentUnit;
import org.jboss.reloaded.naming.deployers.javaee.JavaEEComponentInformer;

/**
 * @author Jaikiran Pai
 * @author <a href="mailto:andrew.rubinger@jboss.org">ALR</a>
 */
public class MockJavaEEComponentInformer implements JavaEEComponentInformer
{

   /**
    * {@inheritDoc}
    * @see org.jboss.reloaded.naming.deployers.javaee.JavaEEComponentInformer#getComponentName(org.jboss.deployers.structure.spi.DeploymentUnit)
    */
   @Override
   public String getComponentName(DeploymentUnit unit)
   {
      return null;
   }

   /**
    * {@inheritDoc}
    * @see org.jboss.reloaded.naming.deployers.javaee.JavaEEComponentInformer#isJavaEEComponent(org.jboss.deployers.structure.spi.DeploymentUnit)
    */
   @Override
   public boolean isJavaEEComponent(DeploymentUnit unit)
   {
      return false;
   }

   /**
    * {@inheritDoc}
    * @see org.jboss.reloaded.naming.deployers.javaee.JavaEEModuleInformer#getApplicationName(org.jboss.deployers.structure.spi.DeploymentUnit)
    */
   @Override
   public String getApplicationName(DeploymentUnit deploymentUnit)
   {
      final DeploymentUnit parent = deploymentUnit.getParent();
      return parent != null ? parent.getName() : null;
   }

   /**
    * {@inheritDoc}
    * @see org.jboss.reloaded.naming.deployers.javaee.JavaEEModuleInformer#getModuleName(org.jboss.deployers.structure.spi.DeploymentUnit)
    */
   @Override
   public String getModuleName(DeploymentUnit deploymentUnit)
   {
      return deploymentUnit.getSimpleName();
   }

   /**
    * {@inheritDoc}
    * @see org.jboss.reloaded.naming.deployers.javaee.JavaEEModuleInformer#getModuleType(org.jboss.deployers.structure.spi.DeploymentUnit)
    */
   @Override
   public ModuleType getModuleType(DeploymentUnit deploymentUnit)
   {
      return null;
   }

   /**
    * {@inheritDoc}
    * @see org.jboss.reloaded.naming.deployers.javaee.DeploymentUnitInformer#getRequiredAttachments()
    */
   @Override
   public String[] getRequiredAttachments()
   {
      return null;
   }

}
