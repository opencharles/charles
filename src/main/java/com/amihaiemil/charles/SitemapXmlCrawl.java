/*
 Copyright (c) 2016, Mihai Emil Andronache
 All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright notice, this
 list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice,
 this list of conditions and the following disclaimer in the documentation
 and/or other materials provided with the distribution.
 * Neither the name of charles nor the names of its
 contributors may be used to endorse or promote products derived from
 this software without specific prior written permission.
 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

package com.amihaiemil.charles;

import com.amihaiemil.charles.sitemap.SitemapXml;
import com.amihaiemil.charles.sitemap.Url;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Crawl a website based on the given sitemap xml.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 */
public class SitemapXmlCrawl implements WebCrawl {
    private static final Logger LOG = LoggerFactory.getLogger(SitemapXmlCrawl.class);


    private WebDriver driver;
    private Set<Url> urlset;
    /**
     * Start a new sitemap.xml crawl using phantom js.
     * @param phantomJsExecPath Path to the phantomJS executable.
     * @param sitemapXmlPath Path to the sitemap.xml file.
     */
    public SitemapXmlCrawl(String phantomJsExecPath, String sitemapXmlPath) throws FileNotFoundException {
        DesiredCapabilities dc = new DesiredCapabilities();
        dc.setJavascriptEnabled(true);
        dc.setCapability(
            PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY,
            phantomJsExecPath
        );
        this.driver = new PhantomJSDriver(dc);
        try {
            this.urlset = new SitemapXml(sitemapXmlPath).read().getUrls();
        } catch (FileNotFoundException ex) {
            this.driver.quit();
            throw ex;
        }
    }

    /**
     * Start a new sitemap.xml crawl using the specified driver.
     * @param drv Specified driver (e.g. chrome, firefox etc).
     * @param sitemapXmlPath Path to the sitemap.xml file.
     */
    public SitemapXmlCrawl(WebDriver drv, String sitemapXmlPath) throws FileNotFoundException {
        this.driver = drv;
        this.urlset = new SitemapXml(sitemapXmlPath).read().getUrls();
    }

    public List<WebPage> crawl() {
        List<WebPage> pages = new ArrayList<WebPage>();
        LOG.info("Started crawling the sitemap.xml...");
        for(Url url : this.urlset) {
        	LOG.info("Crawling page " + url.getLoc() + "... ");
            pages.add(new LiveWebPage(this.driver, url).snapshot());
        	LOG.info("Done crawling page " + url.getLoc() + "!");
        }
        LOG.info("Finished crawling the sitemap.xml!");
        driver.quit();
        return pages;
    }

}
