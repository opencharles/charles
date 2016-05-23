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

package com.amihaiemil.charles.sitemap;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * Url from sitemap.xml
 * @author Mihai Andronache (amihaiemil@gmail.com)
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Url {
	/**
	 * Default ctor.
	 */
	public Url() {
		this("", "", "", "");
	}
	/**
	 * Constructor which only takes the location.
	 * @param loc Address of the page:<br> e.g. http://www.amihaiemil.com
	 */
	public Url(String loc) {
		this(loc, "", "", "");
	}
	/**
	 * Constructor.
	 * @param loc Address of the page.
	 * @param changeFreq Change frequency.
	 * @param lastmod Last modified.
	 * @param priority Priority.
	 * @see http://www.sitemaps.org/protocol.html
	 */
	public Url(String loc, String changeFreq, String lastmod, String priority) {
		this.loc = loc;
		this.changefreq = changeFreq;
		this.lastmod = lastmod;
		this.priority = priority;
	}
    /**
     * Address.<b>Required.</b>
     */
    @XmlElement(name="loc")
    private String loc;
    /**
     * Last modified date yyyy-mm-dd. <b>Optional.</b>
     */
    @XmlElement(name="lastmod")
    private String lastmod;
    /**
     * Change frequency. <b>Optional.</b>
     */
    @XmlElement(name="changefreq")
    private String changefreq;
    /**
     * The priority of this URL relative to other URLs on the site.</br>
     * Valid values range from 0.0 to 1.0.</br>
     * <b>optional</b>
     */
    @XmlElement(name="priority")
    private String priority;

    public String getLoc() {
        return loc;
    }
    public void setLoc(String loc) {
        this.loc = loc;
    }
    public String getPriority() {
        return priority;
    }
    public void setPriority(String priority) {
        this.priority = priority;
    }
    public String getChangefreq() {
        return changefreq;
    }
    public void setChangefreq(String changefreq) {
        this.changefreq = changefreq;
    }
    public String getLastmod() {
        return lastmod;
    }
    public void setLastmod(String lastmod) {
        this.lastmod = lastmod;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Url url = (Url) o;
        return loc.equals(url.loc);
    }

    @Override
    public int hashCode() {
        return loc.hashCode();
    }
}
