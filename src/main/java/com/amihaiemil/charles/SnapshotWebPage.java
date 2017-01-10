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
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package com.amihaiemil.charles;

import java.util.HashSet;
import java.util.Set;

/**
 * Crawled web page.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 1.0.0
 * @checkstyle HiddenField (160 lines)
 */
public final class SnapshotWebPage implements WebPage {

    /**
     * Name.
     */
    private String name;
    
    /**
     * Url.
     */
    private String url;
    
    /**
     * Title.
     */
    private String title;
    
    /**
     * Test content.
     */
    private String textContent;
    
    /**
     * Set of links on the page.
     */
    private Set<Link> links;

    /**
     * Default ctor.
     */
    public SnapshotWebPage() {
        this.url = "";
        this.title = "";
        this.textContent = "";
        this.links = new HashSet<Link>();
    }
    
    /**
     * Ctor.
     * @param livePage LivePage to take a snapshot of
     */
    public SnapshotWebPage(LivePage livePage) {
        this.name = livePage.getName();
        this.url = livePage.getUrl();
        this.title = livePage.getTitle();
        this.textContent = livePage.getTextContent();
        links = new HashSet<Link>();
        for(final Link link : livePage.getLinks()) {
            links.add(link);
        }
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(final String name) {
        this.name = name;
    }

    @Override
    public String getUrl() {
        return this.url;
    }
    
    @Override
    public void setUrl(final  String url) {
        this.url = url;        
    }
    
    @Override
    public String getTitle() {
        return this.title;
    }
    
    @Override
    public void setTitle(final  String title) {
        this.title = title;
    }
    
    @Override
    public String getTextContent() {
        return textContent;
    }
    
    @Override
    public void setTextContent(final  String textContent) {
        this.textContent = textContent;
    }
    
    @Override
    public Set<Link> getLinks() {
        return links;
    }
    @Override
    public void setLinks(final  Set<Link> links) {
        this.links = new HashSet<Link>();
        for(Link l : links) {
            this.links.add(l);
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        int urlhash = 0;
        if(this.url!= null) {
            urlhash = this.url.hashCode();
        }
        result = prime * result + urlhash;
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        SnapshotWebPage other = (SnapshotWebPage) obj;
        if (url == null) {
            if (other.url != null) {
                return false;
            }
        } else if (!this.url.equals(other.url)) {
            return false;
        }
        return true;
    }

}
