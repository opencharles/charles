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
package com.amihaiemil.charles.sitemap;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.util.Set;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Unit tests for {@link SitemapXmlOnline}
 * @author Mihai Andronache (amihaiemil@gmail.com)
 *
 */
public class SitemapXmlOnlineTestCase {
    
    /**
     * Sitemap.xml can be downloaded and unmarshalled successfully.
     * @throws Exception - if something goes wrong.
     */
    @Test
    public void getsSitemap() throws Exception {
        String sitemapxml ="<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">\n" +
                "\n" +
                "   <url>\n" +
                "\n" +
                "      <loc>http://www.test.com/page.html</loc>\n" +
                "   </url>\n" +
                "\n" +
                "   <url>\n" +
                "\n" +
                "      <loc>http://www.test.com/page.html#fragment</loc>\n" +
                "   </url>\n" +
                "\n" +

                "</urlset> ";
        CloseableHttpClient httpClient = Mockito.mock(CloseableHttpClient.class);
        CloseableHttpResponse response = Mockito.mock(CloseableHttpResponse.class);
        HttpEntity entity = Mockito.mock(HttpEntity.class);
        Mockito.when(entity.getContent()).thenReturn(new ByteArrayInputStream(sitemapxml.getBytes()));        
        Mockito.when(response.getEntity()).thenReturn(entity);
        
        Mockito.when(httpClient.execute(Mockito.any(HttpGet.class))).thenReturn(response);

        SitemapXmlOnline online = new SitemapXmlOnline(httpClient, "http://testurl.com");
        Set<Url> urls = new SitemapXml(online.getStream()).read().getUrls();
        assertTrue(urls != null);
        assertTrue(urls.size() == 1);
    }
}
