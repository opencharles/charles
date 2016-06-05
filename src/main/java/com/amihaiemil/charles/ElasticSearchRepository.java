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
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.json.Json;
import javax.json.JsonObject;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Elasticsearch repository. Documents are put into an elastic search index.
 * 
 * @author Mihai Andronache (amihaiemil@gmail.com)
 *
 */
public class ElasticSearchRepository implements Repository {
    private static final Logger LOG = LoggerFactory.getLogger(ElasticSearchRepository.class);    

	/**
	 * Index information.
	 */
	private ElasticSearchIndex indexInfo;

	/**
	 * HTTP client.
	 */
	private CloseableHttpClient httpClient;
	/**
	 * JSON docs to be indexed.
	 */
	private List<JsonObject> docs;

	public ElasticSearchRepository(ElasticSearchIndex indexInfo,
			List<JsonObject> docs) {
		this(indexInfo, docs, HttpClientBuilder.create().build());
	}

	public ElasticSearchRepository(ElasticSearchIndex indexInfo,
			List<JsonObject> docs, CloseableHttpClient httpClient) {
		this.indexInfo = indexInfo;
		this.docs = new ArrayList<JsonObject>();
		for (JsonObject doc : docs) {
			this.docs.add(doc);
		}
		this.httpClient = httpClient;
	}

	/**
	 * This will put all the specified JsonObject documents into the given
	 * elastic search index. If a document already exists, it will be updated
	 * (only if the id is specified). The indexing is done as bulk operation, to avoid
	 * many http requests (see https://www.elastic.co/guide/en/elasticsearch/reference/current/docs-bulk.html).<br>
	 * <br>
	 * <b>Note:</b> The "id" String attribute is searched in each json document
	 * and if found, it will be used for indexing. If not found, elasticsearch
	 * will generate one automatically.
	 */
	@Override
	public void export() throws DataExportException {
		LOG.info("Sending " + docs.size() + " to the elasticsearch index: " + indexInfo.toString());
		String uri = "";
		try {
            JsonObject jsonResponse = this.sendToIndex(
            						      this.makeBulkJsonStructure(docs),
            						      uri
            						  );
            if(jsonResponse.getBoolean("errors")) {
            	LOG.error("There were errors during indexing to " + indexInfo.toString());
            }
            LOG.info("Bulk indexing of the " + docs.size() + " documents, finished in " + jsonResponse.getString("took") + " miliseconds!");
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
			InputStream body = response.getEntity().getContent();
			return Json.createReader(body).readObject();
		} finally {
			IOUtils.closeQuietly(httpClient);
			IOUtils.closeQuietly(response);
		}
	}
	
	/**
	 * Pepare the json structure for bulk indexing.
	 * @param docs The json documents to be indexed.
	 * @return The json structure as a String.
	 */
	private String makeBulkJsonStructure(List<JsonObject> docs) {
		StringBuilder sb = new StringBuilder();
		for(JsonObject doc : docs) {
			String id = doc.getString("id", "");
			String action_and_meta_data;
			if(id.isEmpty()) {
			    action_and_meta_data = "{\"index\":{}}";
			} else {
				action_and_meta_data = "{\"index\":{\"_id\"=\"" + id + "\"}}";
			}
			sb = sb.append(action_and_meta_data).append("\n");
			sb = sb.append(doc.toString()).append("\n");
		}
		return sb.toString();
	}

}
