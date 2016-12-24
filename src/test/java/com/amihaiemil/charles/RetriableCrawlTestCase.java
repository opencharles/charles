/**
 * Copyright (c) 2016, Mihai Emil Andronache
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

import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Unit tests for {@link RetriableCrawl}
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 1.0.0
 *
 */
public final class RetriableCrawlTestCase {

    /**
     * RetriableCrawl works from the first trial.
     * @throws Exception If something goes wrong.
     */
    @Test
    public void crawlSuccessfulFromFirstTrial() throws Exception {
        WebCrawl mc = Mockito.mock(WebCrawl.class);
        Mockito.doNothing().when(mc).crawl();
        RetriableCrawl rc = new RetriableCrawl(mc);
        rc.crawl();
        Mockito.verify(mc, Mockito.times(1)).crawl();
    }

    /**
     * RetriableCrawl works from the third trial.
     * @throws Exception If something goes wrong.
     */
    @Test
    public void crawlSuccessfulFromThirdTrial() throws Exception {
        WebCrawl mc = Mockito.mock(WebCrawl.class);
        Mockito
            .doThrow(new RuntimeException("Test runtime exception 1"))
            .doThrow(new RuntimeException("Test runtime exception 2"))
            .doNothing()
            .when(mc).crawl();
        RetriableCrawl rc = new RetriableCrawl(mc, 5);
        rc.crawl();
        Mockito.verify(mc, Mockito.times(3)).crawl();
    }
    
    /**
     * RetriableCrawl works only at last trial
     * @throws Exception If something goes wrong.
     */
    @Test
    public void crawlSuccessfulAtLastTrial() throws Exception {
        WebCrawl mc = Mockito.mock(WebCrawl.class);
        Mockito
            .doThrow(new RuntimeException("Test runtime exception 1"))
            .doThrow(new RuntimeException("Test runtime exception 2"))
            .doThrow(new RuntimeException("Test runtime exception 3"))
            .doThrow(new RuntimeException("Test runtime exception 4"))
            .doNothing()
            .when(mc).crawl();
        RetriableCrawl rc = new RetriableCrawl(mc, 5);
        rc.crawl();
        Mockito.verify(mc, Mockito.times(5)).crawl();
    }
    
    /**
     * RetriableCrawl fails with RE the first time, then throws DataExportException
     * the second time (thus not retrying the 3rd time) 
     * @throws Exception If something goes wrong.
     */
    @Test
    public void crawlThrowsDeeSecondTime() throws Exception {
        WebCrawl mc = Mockito.mock(WebCrawl.class);
        Mockito
            .doThrow(new RuntimeException("Test runtime exception 1"))
            .doThrow(new DataExportException("Test dee exception 2"))
            .doNothing()
            .when(mc).crawl();
        RetriableCrawl rc = new RetriableCrawl(mc);
        try {
            rc.crawl();
        } catch (DataExportException ex) {
            assertTrue(ex.getMessage().equals("Test dee exception 2"));
            Mockito.verify(mc, Mockito.times(2)).crawl();
        }
    }

    /**
     * RetriableCrawl fails after all retrials
     * @throws Exception If something goes wrong.
     */
    @Test
    public void crawlFailsAfterAllRetrials() throws Exception {
        WebCrawl mc = Mockito.mock(WebCrawl.class);
        Mockito
            .doThrow(new RuntimeException("Fail all retrials!"))
            .when(mc).crawl();
        RetriableCrawl rc = new RetriableCrawl(mc);
        try {
            rc.crawl();
        } catch (RuntimeException ex) {
            assertTrue(ex.getMessage().equals("Fail all retrials!"));
            Mockito.verify(mc, Mockito.times(3)).crawl();
        }
    }
}
