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

import java.util.ArrayList;
import java.util.List;

/**
* Crawled pages are "exported" in a List.
* Use this when you have a reasonable number of pages that you can afford to keep in memory
* and handle later. Use <b>getCrawledPages()</b> to fetch the crawled pages
* after calling WebCrawl.crawl().
* <br><br>
* E.g. This class is suitable for unit tests.
* 
* @author Mihai Andronache (amihaiemil@gmail.com)
*
*/
public final class InMemoryRepository implements Repository {

    /**
     * Holds all the crawled pages.
     */
    private final List<WebPage> pgs = new ArrayList<WebPage>();

    /**
     * Get all the pages from this Repository.
     * @return List of pages.
     */
    public List<WebPage> getCrawledPages() {
        return this.pgs;
    }

    @Override
    public void export(final List<WebPage> pages) throws DataExportException {
        for(final WebPage page : pages) {
            this.pgs.add(page);
        }
    }
}
