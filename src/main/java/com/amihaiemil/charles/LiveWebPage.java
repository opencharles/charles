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

import com.amihaiemil.charles.sitemap.Url;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

/**
 * A web page that is currently being crawled.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 */
public class LiveWebPage implements WebPage, LivePage {
    private WebDriver driver;
    private Url url;
    public LiveWebPage(WebDriver driver, Url url) {
        this.driver = driver;
        this.url = url;
        driver.get(url.getLoc());
        PageFactory.initElements(this.driver, this);
    }
    public Url getUrl() {
        return this.url;
    }
    public void setUrl(Url url) {
    	this.url = url;
	}
    
    public String getTitle() {
       return this.driver.getTitle();
    }
	public void setTitle(String title) {
		throw new UnsupportedOperationException("#setTitle");
	}

    public String getTextContent() {
        return "";
    }
    
    public void setTextContent(String textContent) {
    	throw new UnsupportedOperationException("#setTextContent");
	}

    public WebPage snapshot() {
        return new SnapshotWebPage(this);
    }
}
