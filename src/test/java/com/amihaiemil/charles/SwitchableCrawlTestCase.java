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

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.mockito.Mockito;

/**
 * Unit tests for {@link SwitchableCrawl}
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 1.0.0
 *
 */
public final class SwitchableCrawlTestCase {

    /**
     * {@link SwitchableCrawl} performs the initial crawl without problems.
     * @throws Exception If something goes wrong.
     */
    @Test
    public void initialCrawlWorksFine() throws Exception {
        WebCrawl initial = Mockito.mock(WebCrawl.class);
        WebCrawl failsafe = Mockito.mock(WebCrawl.class);
        Mockito.doNothing().when(initial).crawl();
        SwitchableCrawl sc = new SwitchableCrawl(initial, failsafe);
        sc.crawl();
        Mockito.verify(initial, Mockito.times(1)).crawl();
        Mockito.verify(failsafe, Mockito.times(0)).crawl();
    }

    /**
     * {@link SwitchableCrawl} performs the initial crawl, which throws DataExportException
     * @throws Exception If something goes wrong.
     */
    @Test
    public void initialCrawlThrowsDee() throws Exception {
        WebCrawl initial = Mockito.mock(WebCrawl.class);
        WebCrawl failsafe = Mockito.mock(WebCrawl.class);
        Mockito
            .doThrow(new DataExportException("Dee on initial crawl!"))
            .when(initial).crawl();
        SwitchableCrawl sc = new SwitchableCrawl(initial, failsafe);
        try {
            sc.crawl();
        } catch (DataExportException ex) {
            assertTrue(ex.getMessage().equals("Dee on initial crawl!"));
            Mockito.verify(initial, Mockito.times(1)).crawl();
            Mockito.verify(failsafe, Mockito.times(0)).crawl();
        }
    }

    /**
     * {@link SwitchableCrawl} performs the failsafe crawl without problems, after
     * the initial one failed
     * @throws Exception If something goes wrong.
     */
    @Test
    public void failsafeWorksFine() throws Exception {
        WebCrawl initial = Mockito.mock(WebCrawl.class);
        WebCrawl failsafe = Mockito.mock(WebCrawl.class);
        Mockito
            .doThrow(new RuntimeException("Fail initial crawl!"))
            .when(initial).crawl();
        Mockito.doNothing().when(failsafe).crawl();
        SwitchableCrawl sc = new SwitchableCrawl(initial, failsafe);
        sc.crawl();
        Mockito.verify(initial, Mockito.times(1)).crawl();
        Mockito.verify(failsafe, Mockito.times(1)).crawl();
    }

    /**
     * {@link SwitchableCrawl} throws RE after both crawls fail.
     * @throws Exception If something goes wrong.
     */
    @Test
    public void bothCrawlsFail() throws Exception {
        WebCrawl initial = Mockito.mock(WebCrawl.class);
        WebCrawl failsafe = Mockito.mock(WebCrawl.class);
        Mockito
            .doThrow(new RuntimeException("Fail initial crawl!"))
            .when(initial).crawl();
        Mockito
            .doThrow(new RuntimeException("Fail failsafe crawl!"))
            .when(failsafe).crawl();
        SwitchableCrawl sc = new SwitchableCrawl(initial, failsafe);
        try {
            sc.crawl();
        } catch (RuntimeException ex) {
            assertTrue(ex.getMessage().equals("Fail failsafe crawl!"));
            Mockito.verify(initial, Mockito.times(1)).crawl();
            Mockito.verify(failsafe, Mockito.times(1)).crawl();
        }
    }
}
