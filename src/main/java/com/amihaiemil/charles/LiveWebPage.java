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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.PageFactory;

/**
 * A web page that is currently being crawled.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 */
public final class LiveWebPage implements LivePage {
    
    /**
     * Selenium web driver.
     */
    private WebDriver driver;

    /**
     * Visible anchors.
     */
    @FindBys(@FindBy(tagName="a"))
    @CacheLookup
    private List<WebElement> anchors;

    /**
     * Text content from the page.
     */
    @FindBy(tagName="body")
    @CacheLookup
    private WebElement body;

    public LiveWebPage(WebDriver driver, Link l) {
        this(driver, l.getHref());
    }

    public LiveWebPage(WebDriver driver, String url) {
        this.driver = driver;
        this.driver.get(url);
        PageFactory.initElements(this.driver, this);
    }

    public String getName() {
        String url = this.getUrl();
        if(url.endsWith("/")) {
            url = url.substring(0, url.length()-1);
            return url.substring(url.lastIndexOf("/") + 1);
        }
        return url.substring(url.lastIndexOf("/") + 1, url.lastIndexOf("."));
    }

    public void setName(String name) {
        throw new UnsupportedOperationException("#setName");
    }
    
    public String getUrl() {
        return this.driver.getCurrentUrl();
    }
    public void setUrl(String url) {
        throw new UnsupportedOperationException("#setUrl");
    }
    
    public String getTitle() {
       return this.driver.getTitle();
    }
    public void setTitle(String title) {
        throw new UnsupportedOperationException("#setTitle");
    }

    public String getTextContent() {
        return this.body.getText();
    }
    
    public void setTextContent(String textContent) {
        throw new UnsupportedOperationException("#setTextContent");
    }

    public WebPage snapshot() {
        return new SnapshotWebPage(this);
    }

    public Set<Link> getLinks() {
        Set<Link> links = new HashSet<Link>();
        String currentLoc = this.getUrl();
        for(WebElement a : anchors) {
            if(a.isDisplayed()) {
                Link l = new Link(a.getText(), a.getAttribute("href"));
                if(l.valid(currentLoc)) {
                    links.add(l);
                }
            }
        }
        return links;
    }

    public void setLinks(Set<Link> links) {
        throw new UnsupportedOperationException("#setLinks");    
    }

}
