package com.github.bringking.maven.requirejs.sm;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.logging.Log;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

public class SkipTester {
	
	Skipper skipper;
	
	@Before
	public void setup() {
		skipper = new Skipper(Mockito.mock(Log.class));
	}
	
	@Test
	public void basicTest() {
		try {
			skipper.shouldSkip(new File("src/test/resources/testcase space/app.build.js")); 
			boolean result = skipper.shouldSkip(new File("src/test/resources/testcase space/app.build.js"));
			assertTrue(result);
		} catch (JsonSyntaxException | JsonIOException | IOException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	@Test
	public void shouldNotSkip() {
		try {
			skipper.shouldSkip(new File("src/test/resources/testcase space/app.build.js"));
			
			File main = new File("src/test/resources/testcase space/js/main.js");
			main.setLastModified(System.currentTimeMillis() + 100000);
			
			boolean result = skipper.shouldSkip(new File("src/test/resources/testcase space/app.build.js"));
			assertFalse("Shouldn't skip, but wants to!" , result);
		} catch (IOException e) {
			fail();
		}
	}
	
	@Test
	public void missingCache() {
		File cacheFile = new File("src/test/resources/testcase space/missing.sm");
		try {
			boolean result = skipper.shouldSkip(new File("src/test/resources/testcase space/app_missing.build.js"));
			assertFalse(result);
			assertTrue(cacheFile.exists());
		} catch (JsonSyntaxException | JsonIOException | IOException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		try {
			FileUtils.forceDelete(cacheFile);
		} catch (IOException e) {
		}
	}
}
