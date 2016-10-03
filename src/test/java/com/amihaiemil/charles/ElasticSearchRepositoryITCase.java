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

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;

import org.apache.commons.io.IOUtils;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Integration tests for {@link ElasticSearchRepository}
 * @author Mihai Andronache (amihaiemil@gmail.com)
 *
 */
public class ElasticSearchRepositoryITCase {

	/**
	 * Test ES credentials.
	 */
	private static final String USER = "jeff";
	private static final String PASS = "s3cr3t";

	/**
	 * {@link ElasticSearchRepository} can send the documents to index.
	 */
    @Test
	public void indexesDocuments() throws Exception {
    	List<WebPage> pages = new ArrayList<WebPage>();
		pages.add(this.webPage("http://www.amihaiemil.com/index.html"));
		pages.add(this.webPage("http://eva.amihaiemil.com/index.html"));
    	
		String indexInfo = "http://localhost:9200/charlesit";
    	ElasticSearchRepository elasticRepo = new ElasticSearchRepository(indexInfo);
    	elasticRepo.export(pages);
    	
    	Thread.sleep(3000);//indexed docs don't become searchable instantly
    	
    	JsonObject resp = this.search("*:*", indexInfo + "/page", false);
    	JsonObject  hits = resp.getJsonObject("hits");
    	assertTrue(hits.getInt("total") == 2);
    	JsonArray results = hits.getJsonArray("hits");
    	assertTrue(hits.getJsonArray("hits").size() == 2);
    	boolean containsEva = false;
    	for(int i=0;i<results.size();i++) {
    		if(results.getJsonObject(i).getJsonObject("_source").getString("id").equals("http://eva.amihaiemil.com/index.html")) {
    			containsEva = true;
    			break;
    		}
    	}
    	assertTrue(containsEva);
    }

    /**
     * {@link ElasticSearchRepository} can send the documents to index 
     * while authenticated with basic auth.
	 */
    @Test
	public void indexesDocumentsWithAuth() throws Exception {
    	List<WebPage> pages = new ArrayList<WebPage>();
		pages.add(this.webPage("http://www.amihaiemil.com/index.html"));
		pages.add(this.webPage("http://eva.amihaiemil.com/index.html"));
    	
		String indexInfo = "http://"  + USER + ":" + PASS + "@localhost:8080/charlesitauth";
    	ElasticSearchRepository elasticRepo =
    	    new ElasticSearchRepository(indexInfo);
    	elasticRepo.export(pages);
    	
    	Thread.sleep(3000);//indexed docs don't become searchable instantly
    	
    	JsonObject resp = this.search("*:*", indexInfo + "/page", true);
    	JsonObject  hits = resp.getJsonObject("hits");
    	assertTrue(hits.getInt("total") == 2);
    	JsonArray results = hits.getJsonArray("hits");
    	assertTrue(hits.getJsonArray("hits").size() == 2);
    	boolean containsEva = false;
    	for(int i=0;i<results.size();i++) {
    		if(results.getJsonObject(i).getJsonObject("_source").getString("id").equals("http://eva.amihaiemil.com/index.html")) {
    			containsEva = true;
    			break;
    		}
    	}
    	assertTrue(containsEva);
    }
    
    /**
     * ElasticSearchRepository validateds the given url and throws
     * IllegalArgumentException if it's not valid.
     */
    @Test
    public void validatesIndexUrl() {
        ElasticSearchRepository esrepo = 
            new ElasticSearchRepository("http://localhost:9200/test");
        esrepo = 
            new ElasticSearchRepository("https://localhost:9200/test");
        esrepo = 
            new ElasticSearchRepository("http://www.estest.com/test");
        esrepo = 
            new ElasticSearchRepository("http://elastic-idx.ro:234/test_idx");
        esrepo = 
            new ElasticSearchRepository("http://es.id.com:8080/test1");
        esrepo = 
            new ElasticSearchRepository("https://es.id.prod:9200/test2");
        esrepo = 
            new ElasticSearchRepository("https://esrod:19200/test2");

        try {
            esrepo = 
                new ElasticSearchRepository("file://esrod/test2");
            Assert.fail("Expected IllegalArgumentException for invalid url!");
        } catch (IllegalArgumentException es) {
        	//ok
        }
        try {
            esrepo = 
                new ElasticSearchRepository("https://esrod:bla/test2");
            Assert.fail("Expected IllegalArgumentException for invalid url!");
        } catch (IllegalArgumentException es) {
        	//ok
        }
        try {
            esrepo = 
                new ElasticSearchRepository("https://esrod:123:233/test2");
            Assert.fail("Expected IllegalArgumentException for invalid url!");
        } catch (IllegalArgumentException es) {
        	//ok
        }
        try {
            esrepo = 
                new ElasticSearchRepository("https://esrod:19200");
            Assert.fail("Expected IllegalArgumentException for invalid url!");
        } catch (IllegalArgumentException es) {
        	//ok
        }
        try {
            esrepo = 
                new ElasticSearchRepository("esrod:19200/test2");
            Assert.fail("Expected IllegalArgumentException for invalid url!");
        } catch (IllegalArgumentException es) {
        	//ok
        }
        try {
            esrepo = 
                new ElasticSearchRepository("https://esrod:19200/test2/asd");
            Assert.fail("Expected IllegalArgumentException for invalid url!");
        } catch (IllegalArgumentException es) {
        	//ok
        }
        try {
            esrepo = 
                new ElasticSearchRepository("bla bla 123");
            Assert.fail("Expected IllegalArgumentException for invalid url!");
        } catch (IllegalArgumentException es) {
        	//ok
        }

    }
    
    /**
     * Search the elasticsearch index to check if the index was performed.
     * @param query search query.
     * @param indexInfo index url.
     * @param auth should it provide basic auth info?
     * @return JsonObject search results
     * @throws Exception If something goes wrong.
     */
    private JsonObject search(String query, String indexInfo, boolean auth) throws Exception {
    	HttpGet request = new HttpGet(indexInfo + "/_search?q=" + query);
		request.addHeader("content-type", "application/json");

		CloseableHttpResponse response = null;
		CloseableHttpClient httpClient;
		if(!auth) {
		    httpClient = HttpClientBuilder.create().build();
		} else {
		    CredentialsProvider credsProvider = new BasicCredentialsProvider();
	        credsProvider.setCredentials(
	            AuthScope.ANY,
	            new UsernamePasswordCredentials(USER, PASS)
	        );
	        httpClient = HttpClients.custom()
	            .setDefaultCredentialsProvider(credsProvider).build();
		}
		try {
			response = httpClient.execute(request);
			JsonObject jsonResp = Json.createReader(
								  	response.getEntity().getContent()
								  ).readObject();
			return jsonResp;
		} finally {
			IOUtils.closeQuietly(httpClient);
			IOUtils.closeQuietly(response);
		}
    }

    /**
	 * Returns a WebPage.
	 * @param url URL of the page.
	 * @return WebPage
	 */
	private WebPage webPage(String url) {
		WebPage page = new SnapshotWebPage();
		page.setUrl(url);
		page.setLinks(new LinkedHashSet<Link>());
		page.setName("indextest.html");
		page.setTitle("Intex Test | Title");
		page.setCategory("page");
		page.setTextContent("Test content of this awesome test page.");
		return page;
	}
}
