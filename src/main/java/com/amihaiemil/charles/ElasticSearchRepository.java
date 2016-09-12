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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import javax.json.Json;
import javax.json.JsonObject;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpHost;
import org.apache.http.HttpStatus;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * Elasticsearch repository.
 * Documents are put into an elastic search index using the _bulk API.
 * 
 * @author Mihai Andronache (amihaiemil@gmail.com)
 *
 */
public final class ElasticSearchRepository implements Repository {
    private static final Logger LOG = LoggerFactory.getLogger(ElasticSearchRepository.class);    

    /**
     * Regex pattern to validate index url.
     */
    private static final String ES_INDEX_PATTERN = 
        "^(http:\\/\\/|https:\\/\\/)([a-zA-Z0-9._-]+)(:[0-9]{1,5})?\\/[a-zA-Z0-9-_.]+$";
    
	/**
	 * Index information.
	 */
	private String indexInfo;

	/**
	 * HTTP client.
	 */
	private CloseableHttpClient httpClient;

	/**
	 * Ctor.
	 * @param index ES index address.
	 */
	public ElasticSearchRepository(String index) {
		this(index, HttpClientBuilder.create().build());
	}

	/**
	 * Ctor.
	 * @param index ES index address.
	 * @param username Basic auth user.
	 * @param password Basic auth pass.
	 */
	public ElasticSearchRepository(
	    String index, String username, String password) {		
		if(!this.isIndexUrlValid(index)) {
	        throw new IllegalArgumentException(
	            "Wrong ES index url pattern! Expected "
		       + "(http|https)://domain[:port]/indexname"
	        );
		}
        int portColonIndex = index.indexOf(':', 7);
        int port = -1;
        if(portColonIndex != -1) {
        	port = Integer.valueOf(
        	    index.substring(
        	        portColonIndex+1,
        	        index.indexOf('/', portColonIndex)
                )
            );
        }
        String scheme = "http";
        String domain;
        int idxAfterScheme = 7;
        if(index.startsWith("https://")) {
            scheme = "https";
            idxAfterScheme = 8;
		}
        if(portColonIndex != -1) {
            domain = index.substring(idxAfterScheme, portColonIndex);
        } else {
        	domain = index.substring(idxAfterScheme, index.indexOf('/', 8));
        }


		HttpHost host = new HttpHost(domain, port, scheme);
        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(
            new AuthScope(host, null, host.getSchemeName()),
            new UsernamePasswordCredentials(username, password)
        );
        this.httpClient = HttpClients.custom()
                .setDefaultCredentialsProvider(credsProvider).build();
        this.indexInfo = index;
	}

	
	/**
	 * Ctor
	 * @param index Index address
	 * @param httpClient HTTP client.
	 */
	public ElasticSearchRepository(
	    String index,
		CloseableHttpClient httpClient
	) {
		if(!this.isIndexUrlValid(index)) {
	        throw new IllegalArgumentException(
	            "Wrong ES index url pattern! Expected "
		       + "(http|https)://domain[:port]/indexname"
	        );
		}
		this.indexInfo = index;
		this.httpClient = httpClient;
	}

    /**
     * This will put all the specified WebPages into the
     * elastic search index. If a document already exists, it will be updated
     * (only if the id is specified). The indexing is done as bulk operation, to avoid
     * many http requests.<br>
     * <br>
     * <b>Note:</b> The "id" String attribute is searched in each json document
     * and if found, it will be used for indexing. If not found, elasticsearch
     * will generate one automatically.
     * @param pages Crawled pages to be indexed
     * @see <a href="https://www.elastic.co/guide/en/elasticsearch/reference/current/docs-bulk.html">
     * _bulk API</a>
     */
	@Override
	public void export(List<WebPage> pages) throws DataExportException {
		String uri = indexInfo + "/_bulk?pretty"; 
		try {
			List<JsonObject> docs = new ArrayList<JsonObject>();
			for(WebPage page : pages){
				docs.add(this.preparePage(page));
			}
			LOG.info("Sending " + docs.size() + " to the elasticsearch index: " + indexInfo);
            JsonObject jsonResponse = this.sendToIndex(
            		                      new EsBulkContent(docs).structure(),
            						      uri
            						  );
            if(jsonResponse.getBoolean("errors", Boolean.TRUE)) {
            	LOG.error(
            		"There were errors during indexing to "
            		+ indexInfo +
            		". Whole JSON response: " +
            		jsonResponse.toString()
            	);
            }
            LOG.info("Bulk indexing of the " + docs.size() + " documents, finished in " + jsonResponse.getInt("took") + " miliseconds!");
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
            throw new DataExportException(e.getMessage());
		}
	}

	/**
	 * POSTs the given json string to an elasticsearch index.
	 * @param jsonStructure Json structure to index
	 * @param uri REST endpoint.
	 * @return JSON response body.
	 * @throws IOException if something goes wrong.
	 */
	private JsonObject sendToIndex(String jsonStructure, String uri)
			throws IOException {
		HttpPost request = new HttpPost(uri);
		request.addHeader("content-type", "application/json");
		request.setEntity(new StringEntity(jsonStructure, ContentType.APPLICATION_JSON));

		CloseableHttpResponse response = null;
		try {
			response = httpClient.execute(request);
			int statuscode = response.getStatusLine().getStatusCode();
			JsonObject jsonResp = Json.createReader(
								  	response.getEntity().getContent()
								  ).readObject();
			if(statuscode != HttpStatus.SC_OK) {
				LOG.warn(
					"Http status response from elastic search index: " +
					statuscode +
					". Whole JSON response: " + 
					jsonResp
				);
				if(statuscode == HttpStatus.SC_INTERNAL_SERVER_ERROR) {
					LOG.error(
						"500 SERVER ERROR from elasticsearch /_bulk api. Whole JSON response " + 
						jsonResp.toString()
					);
					throw new IOException("500 SERVER ERROR from elasticsearch /_bulk api!");
				}
			}
			return jsonResp;
		} finally {
			IOUtils.closeQuietly(httpClient);
			IOUtils.closeQuietly(response);
		}
	}

    /**
     * Converts the WebPage to a Json (with the URL is id) for the ES index.
     * @param page WebPage to index.
     * @return JSON which contains the id + json-formatted page
     * @throws IOException In case there are problems when parsing the webpage
     */
    private JsonObject preparePage(WebPage page) throws IOException {
        JsonWebPage jsonPage = new JsonWebPage(page);
        try {
        	JsonObject parsed = jsonPage.toJsonObject();
			return Json.createObjectBuilder()
			    .add("id", page.getUrl())
			    .add("category", parsed.getString("category"))
			    .add("page", parsed).build();
		} catch (JsonProcessingException e) {
			throw new IOException (e);
		}
	}

    /**
     * Checks if the index url is well formatted.
     * It has have the following format: 
     * http://domain[:port]/indexname or https://domain[:port]/indexnam
     * @param url Given url
     * @return true if valid, false if not
     */
    public boolean isIndexUrlValid(String url) {
    	try {
    		Pattern pattern = Pattern.compile(ES_INDEX_PATTERN);
    		Matcher matcher = pattern.matcher(url);
    		return matcher.matches();
    	} catch (PatternSyntaxException ex) {
    		return false;
    	}
    }

}
