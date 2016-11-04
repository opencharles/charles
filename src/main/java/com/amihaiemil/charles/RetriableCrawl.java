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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * WebCrawl, that retries a number of times if any runtime exception occurs in the process.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 1.0.0
 *
 */
public final class RetriableCrawl implements WebCrawl {
    private static final Logger LOG = LoggerFactory.getLogger(RetriableCrawl.class);

    /**
     * Number of retries.
     */
    private int retrials;

    /**
     * Web crawl.
     */
    private WebCrawl crawl;

    /**
     * Ctor.
     * @param crawl Initial web crawl.
     */
    public RetriableCrawl(WebCrawl crawl) {
        this(crawl, 3);
    }

    /**
     * Ctor.
     * @param crawl Initial crawl.
     * @param retrials How many times should it retry?
     */
    public RetriableCrawl(WebCrawl crawl, int retrials) {
        this.crawl = crawl;
        this.retrials = retrials;
    }

    @Override
    public void crawl() throws DataExportException {
        boolean success = false;
        RuntimeException exception = null;
        while (this.retrials > 0 && !success) {
            try {
                this.crawl.crawl();
                success = true;
            } catch (RuntimeException ex) {
                this.retrials--;
                LOG.error("Runtime exception occured while crawling!", ex);
                LOG.error("Retrying... number of retrials left: " + this.retrials);
                exception = ex;
            }
        }
        if(!success) {
            throw exception;
        }
    }

}
