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
 */package com.amihaiemil.charles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A crawl that switches to a new one if it fails with RuntimeException.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 1.0.0
 *
 */
public final class SwitchableCrawl implements WebCrawl {
    
    private static final Logger LOG = LoggerFactory.getLogger(SwitchableCrawl.class);

    /**
     * Initial crawl.
     */
    private WebCrawl initial;

    /**
     * Failsafe. Run in case the initial crawl fails.
     */
    private WebCrawl failsafe;

    /**
     * Ctor.
     * @param initial WebCrawl performed.
     * @param failsafe WebCrawl performed in case the initial one fails with RuntimeException.
     */
    public SwitchableCrawl(WebCrawl initial, WebCrawl failsafe) {
        this.initial = initial;
        this.failsafe = failsafe;
    }

    @Override
    public void crawl() throws DataExportException {
        try {
            this.initial.crawl();
        } catch (RuntimeException ex) {
            LOG.error("The initial crawl failed. Running the failsafe crawl...", ex);
            this.failsafe.crawl();
        }
    }
    
}
