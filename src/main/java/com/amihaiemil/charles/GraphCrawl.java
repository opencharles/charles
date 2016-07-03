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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;

/**
 * Crawl the website as a graph (tree) starting from the index page.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 *
 */
public final class GraphCrawl implements WebCrawl {
	private WebDriver driver;
    private Link index;
    private IgnoredPatterns ignoredLinks;
    
    /**
     * Constructor.
     * @param idx The index page of the site.
     */
    public GraphCrawl(String idx, String phantomJsExecPath, IgnoredPatterns ignored) {
    	this.ignoredLinks = ignored;
    	this.index = new Link("index", idx);
    	DesiredCapabilities dc = new DesiredCapabilities();
        dc.setJavascriptEnabled(true);
        dc.setCapability(
            PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY,
            phantomJsExecPath
        );
        this.driver = new PhantomJSDriver(dc);
    }
    
    /**
     * Constructor.
     * @param idx The index page of the site.
     * @param drv {@link WebDriver} to use.
     */
    public GraphCrawl(String idx, WebDriver drv, IgnoredPatterns ignored) {
    	this.ignoredLinks = ignored;
    	this.index = new Link("index", idx);
    	this.driver = drv;
	}
    
	@Override
	public List<WebPage> crawl() {
		List<WebPage> pages = new ArrayList<WebPage>();
		if(this.ignoredLinks.contains(this.index.getHref())) {
			return pages;
		}
		List<Link> toCrawl = new ArrayList<Link>();
	    Set<Link> crawledLinks = new HashSet<Link>();
	    crawledLinks.add(this.index);
	    
		WebPage indexSnapshot =  new LiveWebPage(this.driver, this.index).snapshot();
		pages.add(indexSnapshot);
		toCrawl.addAll(indexSnapshot.getLinks());
		Link link = toCrawl.remove(0);
		while(toCrawl.size() > 0) {
			if(this.ignoredLinks.contains(link.getHref())) {
				link = toCrawl.remove(0);
				continue;
			}

			boolean notCrawledAlready = crawledLinks.add(link);
			if(notCrawledAlready) {
				WebPage snapshotCrawled = new LiveWebPage(this.driver, link).snapshot();
				pages.add(snapshotCrawled);
				toCrawl.addAll(snapshotCrawled.getLinks());
			}
			link = toCrawl.remove(0);
		}
		return pages;
	}

}
