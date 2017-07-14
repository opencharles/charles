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
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.amihaiemil.charles;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

/**
 * Integration tests for {@link GraphCrawl}
 * @author Mihai Andronache (amihaiemil@gmail.com)
 *
 */
public class GraphCrawlITCase {
    private WebDriver driver;
    
    /**
     * Crawls the site as a graph, visiting all the linked pages.
     * @throws Exception If something goes wrong.
     */
    @Test
    public void crawlsAllPages() throws Exception {
        InMemoryRepository inmr = new InMemoryRepository();
        GraphCrawl graph = new GraphCrawl("http://www.amihaiemil.com", this.driver, inmr);
        graph.crawl();
        Set<WebPage> uniquePages = new HashSet<WebPage>();
        for(WebPage p : inmr.getCrawledPages()) {
            assertTrue("Page crawled 2 times!", uniquePages.add(p));
        }
    }
    
    /**
     * Crawls the website as a graph.
     * The index page does not have any links to other pages on the site.
     * @throws Exception If something goes wrong.
     */
    @Test
    public void crawlsWithNoMoreLinks() throws Exception {
        InMemoryRepository inmr = new InMemoryRepository();
        GraphCrawl graph = new GraphCrawl("http://eva.amihaiemil.com/index.html", this.driver, inmr);
        graph.crawl();
        MatcherAssert.assertThat(
            inmr.getCrawledPages().size(), Matchers.is(1)
        );
        WebPage index = inmr.getCrawledPages().get(0);
        
        MatcherAssert.assertThat(
            index.getTextContent(),
            Matchers.containsString("changing and combining the existing ones")
        );        
        MatcherAssert.assertThat(
            index.getName(),
            Matchers.equalTo("index.html")
        );
        MatcherAssert.assertThat(
            index.getTitle(),
            Matchers.equalTo("EvA project")
        );
    }
    
    /**
     * Graph crawls all the links except the ignored ones.
     * @throws Exception If something goes wrong.
     */
    @Test
    public void crawlsAllPagesExceptIgnored() throws Exception {
        InMemoryRepository inmr = new InMemoryRepository();
        GraphCrawl graph = new GraphCrawl(
            "http://www.amihaiemil.com",
            this.driver,
            new IgnoredPatterns(Arrays.asList("http://www.amihaiemil.com/*/2016/04/*")),//ignore all pages (posts) from April 2016
            inmr
        );
        graph.crawl();
        for(WebPage p : inmr.getCrawledPages()) {
            assertTrue("Ignored page was crawled! " + p.getUrl(), !p.getUrl().contains("/2016/04/"));
        }
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
        chromeOptions.setBinary("/usr/bin/google-chrome-stable");
        chromeOptions.addArguments("--headless");
        chromeOptions.addArguments("--disable-gpu");
        final DesiredCapabilities dc = new DesiredCapabilities();
        dc.setJavascriptEnabled(true);
        dc.setCapability(
            ChromeOptions.CAPABILITY, chromeOptions
        );
        return new ChromeDriver(dc);
    }
}
