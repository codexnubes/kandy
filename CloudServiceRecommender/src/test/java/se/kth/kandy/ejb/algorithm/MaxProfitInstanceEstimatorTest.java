package se.kth.kandy.ejb.algorithm;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import org.apache.log4j.Logger;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import se.kth.kandy.cloud.common.exception.ServiceRecommanderException;
import se.kth.kandy.ejb.factory.EjbFactory;

/**
 * Integration test
 *
 * @author hossein
 */
public class MaxProfitInstanceEstimatorTest {

  private static final Logger logger = Logger.getLogger(MaxProfitInstanceEstimatorTest.class);
  private MaxProfitInstanceEstimator maxProfitInstanceEstimator = null;

  @BeforeClass
  public void setUpClass() {
    maxProfitInstanceEstimator = EjbFactory.getInstance().getMaxProfitInstanceEstimator();
  }

  //--------------------------------------------------------------------
  @Test
  public void testSportPrice() {
    Calendar cal = new GregorianCalendar(2016, Calendar.OCTOBER, 21, 8, 0, 0);
    maxProfitInstanceEstimator.getLinuxSpotPrice("r3.xlarge", "us-west-2c", cal.getTime());
    maxProfitInstanceEstimator.getLinuxSpotPrice("r3.xlarge", "us-west-2c", new Date());
  }

  //------------------------------------------------------------------
  @Test
  public void testEstimateSpotReliability() {
    float reliability = maxProfitInstanceEstimator.estimateSpotReliability("r3.xlarge", "us-west-2c",
        new BigDecimal(0.15).setScale(4, RoundingMode.HALF_UP), 108000000L, new Date());
    reliability = maxProfitInstanceEstimator.estimateSpotReliability("r3.xlarge", "us-west-2c",
        new BigDecimal(0.15).setScale(4, RoundingMode.HALF_UP), 108000000L, new Date());
  }

  @Test
  public void testEstimateSpotReliability1() {
    float reliability = maxProfitInstanceEstimator.estimateSpotReliability("d2.2xlarge", "us-east-1d",
        new BigDecimal(0.08).setScale(4, RoundingMode.HALF_UP), 229727000L, new Date());
  }

  //-------------------------------------------------------------------
  @Test
  public void testEstimateTerminationAverageRunTime() {
    double avgTime = maxProfitInstanceEstimator.estimateTerminationAverageRunTime("r3.xlarge", "us-west-2c",
        new BigDecimal(0.15).setScale(4, RoundingMode.HALF_UP), 108000000L, new Date());
  }

  //-------------------------------------------------------------------
  @Test
  public void testEstimateMinBid3() throws ServiceRecommanderException {
    BigDecimal bid = maxProfitInstanceEstimator.estimateMinBid("d2.8xlarge", "eu-central-1b", 229727000L, (float) 0.7,
        new Date());
  }

  @Test
  public void testEstimateMinBid2() throws ServiceRecommanderException {
    BigDecimal bid = maxProfitInstanceEstimator.estimateMinBid("d2.2xlarge", "us-east-1d", 229727000L, (float) 0.7,
        new Date());
  }

  @Test
  public void testEstimateMinBid1() throws ServiceRecommanderException {
    BigDecimal bid = maxProfitInstanceEstimator.estimateMinBid("r3.xlarge", "us-west-2c", 108000000L, (float) 0.7,
        new Date());
  }

  @Test
  public void testEstimateMinBid() throws ServiceRecommanderException {
    BigDecimal bid = maxProfitInstanceEstimator.estimateMinBid("g2.2xlarge", "us-west-1a", 43200000L, (float) 0.8,
        new Date());
  }

  //---------------------------------------------------------------------
  @Test
  public void testEstimateInstanceProfit() throws ServiceRecommanderException {
    BigDecimal cost = maxProfitInstanceEstimator.estimateInstanceProfit("r3.xlarge", "us-west-2c",
        new BigDecimal(0.15).setScale(4, RoundingMode.HALF_UP), 108000000L, new Date());
  }

  @Test
  public void testEstimateInstanceProfit1() throws ServiceRecommanderException {
    BigDecimal cost = maxProfitInstanceEstimator.estimateInstanceProfit("d2.2xlarge", "eu-central-1b",
        new BigDecimal(0.3).setScale(4, RoundingMode.HALF_UP), 229727000L, new Date());
  }

  //------------------------------------------------------------------------
  @Test
  void testFindAllInstancesZonesEstimatedProfit() throws Exception {
    List<Ec2Instance> instances = maxProfitInstanceEstimator.findAllInstancesZonesEstimatedProfit(229727000L,
        (float) 0.7, InstanceFilter.ECU.FIXED26, 2f, InstanceFilter.STORAGEGB.HDD12000, new Date());
  }

  @Test
  void testFindAllInstancesZonesEstimatedProfit1() throws Exception {
    List<Ec2Instance> instances = maxProfitInstanceEstimator.findAllInstancesZonesEstimatedProfit(229727000L,
        (float) 0.7, InstanceFilter.ECU.FIXED116, 244f, InstanceFilter.STORAGEGB.HDD12000, new Date());
  }

  @Test
  void testFindAllInstancesZonesEstimatedProfit2() throws Exception {
    List<Ec2Instance> instances = maxProfitInstanceEstimator.findAllInstancesZonesEstimatedProfit(229727000L,
        (float) 0.7, InstanceFilter.ECU.ALL, 0f, InstanceFilter.STORAGEGB.ALL, new Date());
  }

  //---------------------------------------------------------------------------
  @Test
  void testFindInstanceZonesEstimatedProfit() throws ServiceRecommanderException {
    List<Ec2Instance> instances = maxProfitInstanceEstimator.findInstanceZonesEstimatedProfit(229727000L, (float) 0.7,
        "c4.8xlarge", new Date());
  }

  @Test
  void testFindInstanceZonesEstimatedProfit1() throws ServiceRecommanderException { // jim used
    List<Ec2Instance> instances = maxProfitInstanceEstimator.findInstanceZonesEstimatedProfit(7200000L, (float) 0.95,
        "m3.xlarge", new Date());
  }
  //---------------------------------------------------------------------------
}
