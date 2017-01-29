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
 * ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.amihaiemil.charles;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.mockito.Mockito;

/**
 * Unit tests for {@link LiveWebPage}
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 1.0.0
 */
public final class LiveWebPageTestCase {
    
    /**
     * LiveWebPage can retrieve the page's name when it has .html
     * extension.
     */
    @Test
    public void getsHtmlPageName() {
        WebDriver driver = Mockito.mock(WebDriver.class);
        Mockito.when(driver.getCurrentUrl())
            .thenReturn("amihaiemil.com/test/some_page.html");
        WebPage page = new LiveWebPage(driver);
        MatcherAssert.assertThat(
            page.getName(), Matchers.equalTo("some_page.html")
        );
    }
    
    /**
     * LiveWebPage can retrieve the page's name when it has more dots.
     * E.g. page.de.html
     * extension.
     */
    @Test
    public void getsPageNameWithMoreDots() {
        WebDriver driver = Mockito.mock(WebDriver.class);
        Mockito.when(driver.getCurrentUrl())
            .thenReturn("goo.gl/test/German_page.de.html");
        WebPage page = new LiveWebPage(driver);
        MatcherAssert.assertThat(
            page.getName(), Matchers.equalTo("German_page.de.html")
        );
    }

    
    /**
     * LiveWebPage can retrieve the page's name when it has .jsp
     * extension.
     */
    @Test
    public void getsJspPageName() {
        WebDriver driver = Mockito.mock(WebDriver.class);
        Mockito.when(driver.getCurrentUrl())
            .thenReturn("http://www.amihaiemil.com/test/page.jsp");
        WebPage page = new LiveWebPage(driver);
        MatcherAssert.assertThat(
            page.getName(), Matchers.equalTo("page.jsp")
        );
    }
    
    /**
     * LiveWebPage can retrieve the page's name when it doesn't
     * exist in the url (short url or index page).
     */
    @Test
    public void getsPageNameShortLink() {
        WebDriver driver = Mockito.mock(WebDriver.class);
        Mockito.when(driver.getCurrentUrl())
            .thenReturn("www.amihaiemil.com/today/test");
        WebPage page = new LiveWebPage(driver);
        MatcherAssert.assertThat(
            page.getName(), Matchers.equalTo("test")
        );
    }
}
