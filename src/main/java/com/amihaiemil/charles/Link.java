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

/**
 * Represents an anchor on a WebPage.
 * 
 * @author Mihai Andronache (amihaiemil@gmail.com)
 *
 */
public final class Link {

    /**
     * Text of the anchor.
     */
    private String text;
    /**
     * Href attribute of the anchor.
     */
    private String href;

    public Link() {
        this("", "");
    }

    public Link(final String text, final String href) {
        this.text = text;
        this.href = href;
    }

    public String getText() {
        return text;
    }

    public void setText(final String text) {
        this.text = text;
    }

    public String getHref() {
        return href;
    }

    public void setHref(final String href) {
        this.href = href;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        if (this.href == null) {
            result = prime * result + 0;
        } else {
            if (this.href.contains("#")) {
                result = new Link(
                	         "", this.href.substring(0, this.href.indexOf("#"))
                	     ).hashCode();
            } else {
                if (this.href.endsWith("/")) {
                    result = prime
                            * result
                            + this.href.substring(0, this.href.length() - 1)
                                    .hashCode();
                } else {
                    result = prime * result + this.href.hashCode();
                }
            }
        }
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Link other = (Link) obj;
        if (this.href == null) {
            if (other.href != null)
                return false;
        } else {
            // get rid of any # fragment
            if (this.href.contains("#") && other.href.contains("#")) {
                return new Link("", this.href.substring(0, href.indexOf("#")))
                        .equals(new Link("", other.href.substring(0,
                                other.href.indexOf("#"))));
            } else if (this.href.contains("#")) {
                return new Link("", this.href.substring(0, href.indexOf("#")))
                        .equals(new Link("", other.href));
            } else if (other.href.contains("#")) {
                return new Link("", this.href).equals(new Link("", other.href
                        .substring(0, other.href.indexOf("#"))));
            }

            if (this.href.endsWith("/") && other.href.endsWith("/")) {
                return this.href.substring(0, this.href.length() - 1).equals(
                        other.href.substring(0, other.href.length() - 1));
            } else if (this.href.endsWith("/")) {
                return this.href.substring(0, href.length() - 1).equals(
                        other.href);
            } else if (other.href.endsWith("/")) {
                return this.href.equals(other.href.substring(0,
                        other.href.length() - 1));
            }
        }
        return this.href.equals(other.href);
    }

    public String toString() {
        return this.href;
    }

    /**
     * Checkes whether this link is valid (is not equivalent to its parent, has
     * the right format and points to a page on the same website).
     * 
     * @return ture if valid, false otherwise.
     */
    public boolean valid(final String parentLoc) {

        if (this.href != null && !this.href.startsWith("mailto")) {
            int slashIndex = parentLoc.indexOf("/", 8);// index of the first "/"
                                                        // after http:// or
                                                        // https://
            String domain = parentLoc;
            if (slashIndex != -1) {
                domain = parentLoc.substring(0, slashIndex);
            }
            if (this.href.startsWith(domain)) {
                return true;
            }
        }

        return false;
    }
}
