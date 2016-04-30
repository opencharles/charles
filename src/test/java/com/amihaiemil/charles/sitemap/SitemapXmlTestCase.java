package com.amihaiemil.charles.sitemap;

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
