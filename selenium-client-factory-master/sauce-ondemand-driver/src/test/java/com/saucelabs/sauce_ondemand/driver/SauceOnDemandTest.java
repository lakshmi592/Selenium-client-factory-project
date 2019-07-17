/*
 * The MIT License
 *
 * Copyright (c) 2010, InfraDNA, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.saucelabs.sauce_ondemand.driver;

import com.saucelabs.selenium.client.factory.SeleniumFactory;
import com.thoughtworks.selenium.Selenium;
import junit.framework.TestCase;
import org.apache.commons.io.IOUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.Augmenter;

import java.io.File;
import java.io.IOException;

/**
 * @author Kohsuke Kawaguchi
 */
public class SauceOnDemandTest extends TestCase {

    public void testSelenium() throws IOException, InterruptedException {
        Selenium s = SeleniumFactory.create("sauce-ondemand:?max-duration=30&os=Linux&browser=firefox&browser-version=3.", "http://www.google.com/");
        s.start();
        s.open("/");
        assertEquals("Google",s.getTitle());

        SauceOnDemandSelenium ss = (SauceOnDemandSelenium) s;
        assertNotNull(ss.getSessionIdValue());
        ss.jobPassed();
        s.stop();

        Thread.sleep(15000);

        System.out.println(ss.getSeleniumServerLogFile());
        System.out.println(ss.getVideo());
        IOUtils.copy(ss.getSeleniumServerLogFileInputStream(),System.out);
    }

    public void testWebDriver() throws IOException, InterruptedException {
        WebDriver s = SeleniumFactory.createWebDriver("sauce-ondemand:?max-duration=30&os=Linux&browser=firefox&browser-version=3.", "http://www.google.com/");        
        assertEquals("Google",s.getTitle());

        SauceOnDemandSelenium ss = (SauceOnDemandSelenium) s;
        assertNotNull(ss.getSessionIdValue());
        WebDriver augmentedDriver = new Augmenter().augment(s);
        File screenshot = ((TakesScreenshot)augmentedDriver).
                                    getScreenshotAs(OutputType.FILE);
        ss.jobPassed();
        s.quit();

        Thread.sleep(15000);

        System.out.println(ss.getSeleniumServerLogFile());
        System.out.println(ss.getVideo());
        IOUtils.copy(ss.getSeleniumServerLogFileInputStream(),System.out);
    }
}
