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
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import javax.json.JsonObject;

import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcabi.http.Request;
import com.jcabi.http.Response;
import com.jcabi.http.request.ApacheRequest;
import com.jcabi.http.response.JsonResponse;
import com.jcabi.http.response.RestResponse;

/**
 * Elasticsearch repository.
 * Documents are put into an elastic search index making a HTTP POST to
 * the _bulk API.<br><br>
 * 
 * Use this class when you have your own ES instance setup.
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
     * Request made to ES.
     */
    private Request post;

	/**
	 * Ctor.
	 * @param index ES index address.
	 */
	public ElasticSearchRepository(String index) {
		this(index, null, null);
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
		if(username != null && password != null) {
			String wCredentials;
		    if(index.startsWith("http://")) {
		    	wCredentials = "http://" + username + ":" + password
		    	 + "@" + index.substring(7);
		    } else {
		    	wCredentials = "https://" + username + ":" + password
				    + "@" + index.substring(8);
		    }
		    this.post = new ApacheRequest(wCredentials + "/_bulk?pretty")
                .header("content-type", "application/json");
		} else {
			this.post = new ApacheRequest(index + "/_bulk?pretty")
            .header("content-type", "application/json");
		}
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
		try {
			LOG.info("Sending " + pages.size() + " to the elasticsearch index");
            JsonObject jsonResponse = this.sendToIndex(
            		                      new EsBulkContent(pages).structure()
            						  );
            if(jsonResponse.getBoolean("errors", Boolean.TRUE)) {
            	LOG.error(
            		"There were errors during indexing to " +
            		". Whole JSON response: " +
            		jsonResponse.toString()
            	);
            	throw new DataExportException("Errors when calling the _bulk api.");
            }
            LOG.info("Bulk indexing of the " + pages.size() + " documents, finished in " + jsonResponse.getInt("took") + " miliseconds!");
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
            throw new DataExportException(e.getMessage());
		}
	}

	/**
	 * POSTs the given json string to an elasticsearch index.
	 * @param jsonStructure Json structure to index
	 * @return JSON response body.
	 * @throws IOException if something goes wrong.
	 */
	private JsonObject sendToIndex(String jsonStructure)
			throws IOException {
		
		this.post = this.post
		    .method(Request.POST).body().set(jsonStructure).back();
		Response resp = post.fetch();
		int status = resp.as(RestResponse.class).status();
		JsonObject json = resp.as(JsonResponse.class).json().readObject();
		if(status != HttpStatus.SC_OK) {
		    LOG.warn(
			    "Http status response from elastic search index: " +
			    		status +
				". Whole JSON response: " + 
				json
			);
			if(status == HttpStatus.SC_INTERNAL_SERVER_ERROR) {
			    LOG.error(
				    "500 SERVER ERROR from elasticsearch /_bulk api. Whole JSON response " + 
					json.toString()
				);
				throw new IOException("500 SERVER ERROR from elasticsearch /_bulk api!");
			}
		}
		return json;
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
