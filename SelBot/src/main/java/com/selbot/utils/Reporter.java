package com.selbot.utils;

import java.io.IOException;
import java.util.Date;

import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.MediaEntityModelProvider;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;

import cucumber.api.testng.AbstractTestNGCucumberTests;

public abstract class Reporter extends AbstractTestNGCucumberTests{

	public static ExtentHtmlReporter reporter;
	public static ExtentReports extent;
	public static ExtentTest parentTest, test;

	public String testcaseName, testcaseDec, author, category;
	public static  String excelFileName;

	@BeforeSuite
	public void startReport() {
		reporter = new ExtentHtmlReporter("./reports/result.html");
		reporter.setAppendExisting(true); 		
		extent   = new ExtentReports();
		extent.attachReporter(reporter);

	}

	@BeforeClass
	public void reportEachTest() throws IOException {
		parentTest = extent.createTest(testcaseName, testcaseDec);

		parentTest.assignAuthor(author);
		parentTest.assignCategory(category);  
	}
	
	
	@BeforeMethod
	public void reportEachIteration() throws IOException {
		
		test = parentTest.createNode(testcaseName);

	}
	public abstract long takeSnap();

	public void reportStep(String desc, String status,boolean bSnap ) {
		MediaEntityModelProvider img = null;
		if(bSnap && !status.equalsIgnoreCase("INFO")){

			long snapNumber = 100000L;
			snapNumber = takeSnap();
			try {
				img = MediaEntityBuilder.createScreenCaptureFromPath
						("./../reports/images/"+snapNumber+".jpg").build();
			} catch (IOException e) {

			}
		}
		if(status.equalsIgnoreCase("pass")) {
			test.pass(desc, img);
		} else if(status.equalsIgnoreCase("fail")) {
			test.fail(desc, img); 
		} else if(status.equalsIgnoreCase("INFO")) {
			test.pass(desc); 
		}
	}
	public void reportStep(String desc, String status) {
		reportStep(desc, status, true);
	}


	@AfterSuite
	public void stopReport() {
		extent.flush();
	}
}














