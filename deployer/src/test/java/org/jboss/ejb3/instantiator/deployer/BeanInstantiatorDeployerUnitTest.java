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

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.List;

import junit.framework.TestCase;

import org.jboss.bootstrap.api.descriptor.BootstrapDescriptor;
import org.jboss.bootstrap.api.mc.server.MCServer;
import org.jboss.bootstrap.api.mc.server.MCServerFactory;
import org.jboss.deployers.client.spi.main.MainDeployer;
import org.jboss.deployers.spi.DeploymentException;
import org.jboss.deployers.structure.spi.DeploymentUnit;
import org.jboss.deployers.vfs.spi.client.VFSDeployment;
import org.jboss.deployers.vfs.spi.client.VFSDeploymentFactory;
import org.jboss.ejb3.instantiator.spi.AttachmentNames;
import org.jboss.ejb3.instantiator.spi.BeanInstantiator;
import org.jboss.kernel.spi.dependency.KernelController;
import org.jboss.logging.Logger;
import org.jboss.reloaded.api.ReloadedDescriptors;
import org.jboss.vfs.VFS;
import org.jboss.vfs.VirtualFile;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Ensures the {@link BeanInstantiatorDeployer} is working as expected
 * by attaching a {@link BeanInstantiator} implementation to the incoming
 * {@link DeploymentUnit}.
 *
 * @author <a href="mailto:andrew.rubinger@jboss.org">ALR</a>
 * @version $Revision: $
 */
public class BeanInstantiatorDeployerUnitTest
{

   //-------------------------------------------------------------------------------------||
   // Class Members ----------------------------------------------------------------------||
   //-------------------------------------------------------------------------------------||

   /**
    * Logger
    */
   private static final Logger log = Logger.getLogger(BeanInstantiatorDeployerUnitTest.class);

   /**
    * The Server
    */
   private static MCServer server;

   /**
    * Name of the system property signaling JBossXB to ignore order
    */
   private static final String NAME_SYSPROP_JBOSSXB_IGNORE_ORDER = "xb.builder.useUnorderedSequence";

   /**
    * Value to set for JBossXB ordering
    */
   private static final String VALUE_SYSPROP_JBOSSXB_IGNORE_ORDER = "true";

   /**
    * MC bean name of the {@link MainDeployer}
    */
   protected static final String NAME_MC_MAIN_DEPLOYER = "MainDeployer";

   //-------------------------------------------------------------------------------------||
   // Instance Members -------------------------------------------------------------------||
   //-------------------------------------------------------------------------------------||

   /**
    * {@link MainDeployer} implementation
    */
   private MainDeployer deployer;

   /**
    * Dummy deployment to run through the {@link BeanInstantiatorDeployer}
    */
   private VFSDeployment dummyDeployment;

   //-------------------------------------------------------------------------------------||
   // Lifecycle --------------------------------------------------------------------------||
   //-------------------------------------------------------------------------------------||

   /**
    * Setup JBossXB
    * TODO @see comments below so that this step is not necessary
    */
   @BeforeClass
   public static void setupJBossXb()
   {
      AccessController.doPrivileged(new PrivilegedAction<Void>()
      {
         public Void run()
         {
            // Must use unordered sequence else JBossXB will explode
            //TODO Define a proper vfs.xml which is properly ordered
            System.setProperty(NAME_SYSPROP_JBOSSXB_IGNORE_ORDER, VALUE_SYSPROP_JBOSSXB_IGNORE_ORDER);
            return null;
         }
      });
   }

   /**
    * Creates the server
    */
   @BeforeClass
   public static void createAndConfigureServer()
   {
      // Create a server
      final MCServer mcServer = MCServerFactory.createServer();

      // Add the required bootstrap descriptors
      final List<BootstrapDescriptor> descriptors = mcServer.getConfiguration().getBootstrapDescriptors();
      descriptors.add(ReloadedDescriptors.getClassLoadingDescriptor());
      descriptors.add(ReloadedDescriptors.getVdfDescriptor());

      log.info("Using bootstrap descriptors:" + descriptors);

      // Set
      server = mcServer;
   }

   /**
    * Starts the server before each test
    * @throws Exception
    */
   @Before
   public void startServerAndDeploy() throws Exception
   {
      // Start
      server.start();

      // Get the KernelController
      final KernelController controller = server.getKernel().getController();

      // Get the MainDeployer (should have been installed via the lifecycle)
      final MainDeployer mainDeployer = (MainDeployer) controller.getInstalledContext(NAME_MC_MAIN_DEPLOYER)
            .getTarget();
      TestCase.assertNotNull(MainDeployer.class.getName() + " instance was not installed into MC", mainDeployer);
      this.deployer = mainDeployer;
      
      // Deploy the test Bean Instantiator Impl and deployer
      final VirtualFile testInstantiatorFile = VFS.getChild(this.getClass().getClassLoader()
            .getResource("ejb3-instantiator-test-deployer-jboss-beans.xml").toURI());
      deployer.deploy(VFSDeploymentFactory.getInstance().createVFSDeployment(testInstantiatorFile));

      // Deploy a dummy
      final String deploymentName = "dummyDeployment";
      final File deploymentFile = new File(this.getClass().getClassLoader().getResource(deploymentName).toURI());
      TestCase.assertTrue(deploymentFile.exists());
      final VirtualFile deploymentVf = VFS.getChild(this.getClass().getClassLoader().getResource(deploymentName)
            .toURI());
      final VFSDeployment dummyDeployment = VFSDeploymentFactory.getInstance().createVFSDeployment(deploymentVf);
      deployer.addDeployment(dummyDeployment);
      this.dummyDeployment = dummyDeployment;

      // Run the pending deployments
      deployer.process();
      deployer.checkComplete();
   }

   /**
    * Undeploys the dummy deployment
    * @throws Exception
    */
   @After
   public void undeploy() throws Exception
   {
      deployer.removeDeployment(dummyDeployment);
      deployer.process();
      deployer.checkComplete();
   }

   //-------------------------------------------------------------------------------------||
   // Tests ------------------------------------------------------------------------------||
   //-------------------------------------------------------------------------------------||

   /**
    * Ensures the {@link BeanInstantiatorDeployer} attaches the {@link BeanInstantiator} implementation
    * to the deployment unit
    */
   @Test
   public void beanInstantiatorDeployerCreatesAttachments() throws IOException, URISyntaxException, DeploymentException
   {
      // Get the last deployment
      final DeploymentUnit unit = TestBeanInstantiatorDeployer.lastDeployment;
      TestCase.assertNotNull("Could not obtain cached deployment unit to test", unit);

      // Ensure the attachment is in place
      final BeanInstantiator instantiator = unit.getAttachment(AttachmentNames.NAME_BEAN_INSTANCE_INSTANTIATOR,
            BeanInstantiator.class);
      TestCase.assertNotNull(instantiator);

   }

}
