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
 */package com.amihaiemil.charles;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.amihaiemil.charles.sitemap.SitemapXmlOnDisk;

/**
 * Integration tests for {@link SitemapXmlCrawl}.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 *
 */
public class SitemapXmlCrawlITCase {
    
    private WebDriver driver;
    
    /**
     * A page's title can be retrieved.
     * @throws Exception - If something goes wrong.
     */
    @Test
    public void getsPageTitle() throws Exception {

        InMemoryRepository inmr = new InMemoryRepository();
        SitemapXmlCrawl sitemapXmlCrawl = new SitemapXmlCrawl(
            this.driver,
            new SitemapXmlOnDisk("src/test/resources/testsitemap.xml"),
            inmr
        );
        sitemapXmlCrawl.crawl();
        assertTrue(inmr.getCrawledPages().size() == 1);
        assertTrue(inmr.getCrawledPages().get(0).getTitle().equals("EvA project"));
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
