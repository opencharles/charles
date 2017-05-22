/**
 * Copyright (c) 2016-2017, Mihai Emil Andronache
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * * Neither the name of charles nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.amihaiemil.charles;

import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

/**
 * Integration tests for {@link LiveWebPage}
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * 
 */
public class LiveWebPageITCase {
    
    private WebDriver driver;
    
    /**
     * {@link LiveWebPage} can fetch all the links from a web page when it has a CNAME url defined.
     */
    @Test
    public void retrievesLinksFromPageCname() {
        this.driver.get("http://amihaiemil.github.io/page2/");
        LiveWebPage livePage = new LiveWebPage(this.driver);
        Set<Link> links = livePage.getLinks();
        assertTrue(links.size() > 0);
        assertTrue(
            "Expected link not on web page!", links.contains(
                new Link("What is HATEOAS?", "http://www.amihaiemil.com/2016/05/07/what-is-hateoas.html")
            )
        );
    }
    
    /**
     * {@link LiveWebPage} can fetch all the links from a web page.
     */
    @Test
    public void retrievesLinksFromPage() {
    	String address = "http://www.amihaiemil.com/page2/";
        this.driver.get(address);
        LiveWebPage livePage = new LiveWebPage(this.driver);
        Set<Link> links = livePage.getLinks();
        assertTrue(links.size() > 0);
        assertTrue(
            "Expected link not on web page!",
            links.contains(
                new Link("What is HATEOAS?", "http://www.amihaiemil.com/2016/05/07/what-is-hateoas.html")
            )
        );
        for(Link l : links) {
            assertTrue(l.getHref().startsWith("http://www.amihaiemil.com"));
        }
    }
    
    /**
     * {@link LiveWebPage} can return the visible text from the page.
     */
    @Test
    public void retrievesTextFromPage() {
    	this.driver.get("http://www.amihaiemil.com/2016/05/07/what-is-hateoas.html");
        LiveWebPage livePage = new LiveWebPage(this.driver);
        String textContent = livePage.getTextContent();
        assertTrue(textContent.contains("In his book Burke also describes HATEOAS"));
        assertTrue(textContent.contains("\"lastmodified\": \"15/03/2016\""));
        assertTrue(textContent.contains("JS client to work with it"));
    }
    
    /**
     * {@link LiveWebpage} can return a snapshot WebPage.
     */
    @Test
    public void snapshotsSelf() {
    	this.driver.get("http://www.amihaiemil.com/page2");
        LiveWebPage livePage = new LiveWebPage(this.driver);
        WebPage snapshot = livePage.snapshot();
        assertTrue(snapshot.getTitle().equals("amihaiemil.com | Programming blog"));
        assertTrue(snapshot.getLinks().size() > 0);
        assertTrue(
            "Expected link not on web page!",
            snapshot.getLinks().contains(
                new Link("What is HATEOAS?", "http://www.amihaiemil.com/2016/05/07/what-is-hateoas.html")
            )
        );
    }
    
    @Before
    public void initDriver() {
        this.driver = this.webDriver();
    }
    
    @After
    public void quitDriver() {
        this.driver.quit();
    }
    
    private WebDriver webDriver() {
    	final ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setBinary("/usr/bin/google-chrome");
        final DesiredCapabilities dc = new DesiredCapabilities();
        dc.setJavascriptEnabled(true);
        dc.setCapability(
            ChromeOptions.CAPABILITY, chromeOptions
        );
        System.setProperty("webdriver.chrome.driver", "/usr/local/bin/chromedriver");
        return new ChromeDriver(dc);

    }

}
