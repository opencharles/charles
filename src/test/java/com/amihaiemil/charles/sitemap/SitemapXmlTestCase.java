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
 */package com.amihaiemil.charles.sitemap;

import org.junit.Test;

import java.io.ByteArrayInputStream;

import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link SitemapXml}
 * @author Mihai Andronache (amihaiemil@gmail.com)
 */
public class SitemapXmlTestCase {

    /**
     * SitemapXml can unmarshal the urlset.
     */
    @Test
    public void unmarshallsUrlSet() {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "\n" +
                "<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">\n" +
                "\n" +
                "   <url>\n" +
                "\n" +
                "      <loc>http://www.test.com/</loc>\n" +
                "\n" +
                "      <lastmod>2005-01-01</lastmod>\n" +
                "\n" +
                "      <changefreq>monthly</changefreq>\n" +
                "\n" +
                "      <priority>0.8</priority>\n" +
                "\n" +
                "   </url>\n" +
                "\n" +
                "   <url>\n" +
                "\n" +
                "      <loc>http://www.test.com/</loc>\n" +
                "\n" +
                "      <lastmod>2005-01-03</lastmod>\n" +
                "\n" +
                "      <changefreq>monthly</changefreq>\n" +
                "\n" +
                "      <priority>0.8</priority>\n" +
                "\n" +
                "   </url>\n" +
                "\n" +

                "</urlset> ";
        SitemapXml sitemapXml = new SitemapXml(new ByteArrayInputStream(xml.getBytes()));
        UrlSet set = sitemapXml.read();
        assertTrue("Expected only 1 unique url in the set! Got: " + set.getUrls().size(),
                   set.getUrls().size() == 1);
    }

}
