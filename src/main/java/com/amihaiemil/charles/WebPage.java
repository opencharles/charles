/*
 All rights reserved.
 Copyright (c) 2016, Mihai Emil Andronache

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

import java.util.Set;


/**
 * Interface for a web page.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 */
public interface WebPage {

	/**
	 * Get the filename of this webpage. E.g. index.html -> index
	 * @return String filename.
	 */
	String getName();
	
	/**
	 * Set the name of this webpage.
	 * @param name Given name.
	 */
    void setName(String name);
    
    /**
     * Page's url.
     * @return String url. e.g. http://charles.amihaiemil.com/index.html
     */
    String getUrl();
    
    /**
     * Set url.
     * @param url
     */
    void setUrl(String url);
    
    /**
     * Get the title of the page.
     * @return String title.
     */
    String getTitle();
    
    /**
     * Set the page title.
     * @param title
     */
    void setTitle(String title);
    
    /**
     * Get all the text content of the page.
     * @return
     */
    String getTextContent();
    
    /**
     * Set the text content.
     * @param textContent
     */
    void setTextContent(String textContent);

    /**
     * Get the page's category (text of an element with id = "pagectg")
     * @return
     */
    String getCategory();
    
    /**
     * Set the page's category.
     * @param category
     */
    void setCategory(String category);
    
    /**
     * Fetch all the anchors (links) from the page.
     * @return
     */
    Set<Link> getLinks();
    
    /**
     * Set the anchors on a page.
     * @param links
     */
    void setLinks(Set<Link> links);
}
