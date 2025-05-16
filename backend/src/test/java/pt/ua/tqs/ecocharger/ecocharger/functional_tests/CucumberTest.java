package pt.ua.tqs.ecocharger.ecocharger.functional_tests;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

import io.cucumber.core.options.Constants;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("pt/ua/tqs/ecocharger/ecocharger/features")
@ConfigurationParameter(key = Constants.GLUE_PROPERTY_NAME, value = "pt.ua.tqs.ecocharger.ecocharger.functional_tests.steps")
public class CucumberTest {

}