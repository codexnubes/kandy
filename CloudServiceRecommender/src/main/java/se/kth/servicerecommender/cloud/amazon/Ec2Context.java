/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.servicerecommender.cloud.amazon;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Module;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import org.jclouds.Constants;
import static org.jclouds.Constants.PROPERTY_CONNECTION_TIMEOUT;
import org.jclouds.ContextBuilder;
import org.jclouds.aws.ec2.AWSEC2Api;
import org.jclouds.aws.ec2.compute.AWSEC2ComputeService;
import org.jclouds.aws.ec2.features.AWSKeyPairApi;
import org.jclouds.aws.ec2.features.SpotInstanceApi;
import static org.jclouds.aws.ec2.reference.AWSEC2Constants.PROPERTY_EC2_AMI_QUERY;
import static org.jclouds.aws.ec2.reference.AWSEC2Constants.PROPERTY_EC2_CC_AMI_QUERY;
import org.jclouds.compute.ComputeServiceContext;
import static org.jclouds.compute.config.ComputeServiceProperties.TIMEOUT_PORT_OPEN;
import static org.jclouds.compute.config.ComputeServiceProperties.TIMEOUT_SCRIPT_COMPLETE;
import org.jclouds.ec2.EC2Api;
import org.jclouds.ec2.features.SecurityGroupApi;
import org.jclouds.enterprise.config.EnterpriseConfigurationModule;
import org.jclouds.logging.slf4j.config.SLF4JLoggingModule;
import org.jclouds.sshj.config.SshjSshClientModule;
import se.kth.servicerecommender.cloud.common.Ec2Credentials;
import se.kth.servicerecommender.cloud.common.Settings;

/**
 *
 * @author kamal
 * @author Hossein - Add {@link #getSpotInstanceApi() }
 */
public class Ec2Context {

  private final Ec2Credentials credentials;
  private final AWSEC2ComputeService computeService;
  private final EC2Api ec2api;
  private final SecurityGroupApi securityGroupApi;
  private final AWSKeyPairApi keypairApi;
  private final SpotInstanceApi spotInstanceApi;
  private final AWSEC2Api aWSEC2Api;

  public Ec2Context(Ec2Credentials credentials) {
    this.credentials = credentials;
    Properties properties = new Properties();
    long scriptTimeout = TimeUnit.MILLISECONDS.convert(50, TimeUnit.MINUTES);
    properties.setProperty(TIMEOUT_SCRIPT_COMPLETE, scriptTimeout + "");
    properties.setProperty(TIMEOUT_PORT_OPEN, scriptTimeout + "");
    properties.setProperty(PROPERTY_CONNECTION_TIMEOUT, scriptTimeout + "");
    properties.setProperty(PROPERTY_EC2_AMI_QUERY, "owner-id=137112412989;state=available;image-type=machine");
    properties.setProperty(PROPERTY_EC2_CC_AMI_QUERY, "");
    properties.setProperty(Constants.PROPERTY_MAX_RETRIES, Settings.JCLOUDS_PROPERTY_MAX_RETRIES + "");
    properties.setProperty(Constants.PROPERTY_RETRY_DELAY_START, Settings.JCLOUDS_PROPERTY_RETRY_DELAY_START + "");

    Iterable<Module> modules = ImmutableSet.<Module>of(
        new SshjSshClientModule(),
        new SLF4JLoggingModule(),
        new EnterpriseConfigurationModule());

    ContextBuilder build = ContextBuilder.newBuilder("aws-ec2")
        .credentials(credentials.getAccessKey(), credentials.getSecretKey())
        .modules(modules)
        .overrides(properties);
    ComputeServiceContext context = build.buildView(ComputeServiceContext.class);
    this.computeService = (AWSEC2ComputeService) context.getComputeService();
    this.ec2api = computeService.getContext().unwrapApi(EC2Api.class);
    this.aWSEC2Api = computeService.getContext().unwrapApi(AWSEC2Api.class);
    this.securityGroupApi = ec2api.getSecurityGroupApi().get();
    this.keypairApi = (AWSKeyPairApi) ec2api.getKeyPairApi().get();
    this.spotInstanceApi = (SpotInstanceApi) aWSEC2Api.getSpotInstanceApi().get();
  }

  public Ec2Credentials getCredentials() {
    return credentials;
  }

  public AWSEC2ComputeService getComputeService() {
    return computeService;
  }

  public EC2Api getEc2api() {
    return ec2api;
  }

  public AWSKeyPairApi getKeypairApi() {
    return keypairApi;
  }

  public SecurityGroupApi getSecurityGroupApi() {
    return securityGroupApi;
  }

  public SpotInstanceApi getSpotInstanceApi() {
    return spotInstanceApi;
  }

  public AWSEC2Api getAWSEC2Api() {
    return aWSEC2Api;
  }
}
