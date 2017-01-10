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
 * ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.amihaiemil.charles;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.WebDriver;

/**
 * Crawl the website as a graph (tree) starting from the index page.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 *
 */
public final class GraphCrawl extends AbstractWebCrawl {

    /**
     * Page to start the crawling from.
     */
    private Link index;

    /**
     * Constructor.
     * @param idx The index page of the site.
     * @param drv {@link WebDriver} to use.
     * @param repo Repository where the crawled pages are exported.
     */
    public GraphCrawl(String idx, WebDriver drv, Repository repo) {
        this(idx, drv, new IgnoredPatterns(), repo, 20);
    }

    /**
     * Constructor.
     * @param idx The index page of the site.
     * @param drv {@link WebDriver} to use.
     * @param repo Repository where the crawled pages are exported.
     * @param batchSize Size of the export batch.
     */
    public GraphCrawl(String idx, WebDriver drv, Repository repo, int batchSize) {
        this(idx, drv, new IgnoredPatterns(), repo, batchSize);
    }

    /**
     * Constructor.
     * @param idx The index page of the site.
     * @param drv {@link WebDriver} to use.
     * @param ignored Ignored pages patterns.
     * @param repo Repository where the crawled pages are exported.
     */
    public GraphCrawl(
        String idx, WebDriver drv,
        IgnoredPatterns ignored, Repository repo
    ) {
        this(idx, drv, ignored, repo, 20);
    }

    /**
     * Constructor.
     * @param idx The index page of the site.
     * @param drv {@link WebDriver} to use.
     * @param ignored Ignored pages patterns.
     * @param repo Repository where the crawled pages are exported.
     * @param batchSize Size of the export batch.
     */
    public GraphCrawl(
        String idx, WebDriver drv,
        IgnoredPatterns ignored, Repository repo, int batchSize
    ) {
        super(drv, ignored, repo, batchSize);
        this.index = new Link("index", idx);
    }

    @Override
    public void crawl() throws DataExportException {
        if(!this.ignoredPatterns().contains(this.index.getHref())) {
            List<WebPage> pages = new ArrayList<WebPage>();
            WebPage indexSnapshot =  new LiveWebPage(this.driver(), this.index).snapshot();
            pages.add(indexSnapshot);
            
            Set<Link> crawledLinks = new HashSet<Link>();
            crawledLinks.add(this.index);
        
            List<Link> toCrawl = new ArrayList<Link>();
            toCrawl.addAll(indexSnapshot.getLinks());
            
            this.checkBatchSize(pages);
            
            if(toCrawl.size() > 0) {
                Link link = toCrawl.remove(0);
                while(toCrawl.size() > 0) {
                    if(this.ignoredPatterns().contains(link.getHref())) {
                        link = toCrawl.remove(0);
                        continue;
                    }
                    boolean notCrawledAlready = crawledLinks.add(link);
                    if(notCrawledAlready) {
                        WebPage snapshotCrawled = new LiveWebPage(this.driver(), link).snapshot();
                        pages.add(snapshotCrawled);
                        this.checkBatchSize(pages);
                        toCrawl.addAll(snapshotCrawled.getLinks());   
                    }
                    link = toCrawl.remove(0);
                }
            }
            this.repo().export(pages);
            this.driver().quit();
        }
    }

    /**
     * Check if the batch size has been reached. If yes, export the pages and empty the
     * list for the next batch.
     * @param pages Pages crawled so far.
     * @throws DataExportException If something goes wrong during processing of crawled pages.
     */
    private void checkBatchSize(List<WebPage> pages) throws DataExportException {
        if(pages.size() == this.batchSize()) {
            this.repo().export(pages);
            pages.clear();
        }
    }
    
}
