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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

/**
 * sitemap.xml file located online at a given address.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 *
 */
public final class SitemapXmlOnline implements SitemapXmlLocation {
    private String xmlAddress;
    private CloseableHttpClient httpClient;
    
    /**
     * Constructor.
     * @param address Url of the online sitemap.
     */
    public SitemapXmlOnline(String address) {
        this(HttpClientBuilder.create().build(), address);
    }
    
    /**
     * Constructor.
     * @param address Url of the online sitemap.
     * @param httpClient Given closeable http client.
     */
    public SitemapXmlOnline(CloseableHttpClient httpClient, String address) {
        this.xmlAddress = address;
        this.httpClient = httpClient;
    }
    
    @Override
    public InputStream getStream() throws IOException {
        try {
            CloseableHttpResponse response = httpClient.execute(new HttpGet(xmlAddress));
            try {
                return new ByteArrayInputStream(
                    IOUtils.toByteArray(response.getEntity().getContent())
                );
            } finally {
                response.close();
            }
        } catch (ClientProtocolException e) {
            throw e;
        } finally {
            httpClient.close();
        }
    }

}
