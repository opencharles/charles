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
import java.util.List;

import javax.json.Json;
import javax.json.JsonObject;

import org.junit.Ignore;
import org.junit.Test;

/**
 * Integration tests for {@link ElasticSearchRepository}
 * @author Mihai Andronache (amihaiemil@gmail.com)
 *
 */
public class ElasticSearchRepositoryITCase {
	/**
	 * {@link ElasticSearchRepository} can send the documents to index.
	 */
    @Test
    @Ignore//until setup of a test elasticsearch instance in the CI environment
	public void indexesDocuments() throws Exception {
		List<JsonObject> docs = new ArrayList<JsonObject>();
		docs.add(Json.createObjectBuilder().add("id", "1").add("name", "Mihai").build());
		docs.add(Json.createObjectBuilder().add("id", "2").add("name", "Emil").build());
		ElasticSearchIndex indexInfo = new ElasticSearchIndex("localhost", 9200, "test10", "doctype");
    	ElasticSearchRepository elasticRepo = new ElasticSearchRepository(
    		indexInfo,
    		new EsBulkContent(docs)
    	);
    	elasticRepo.export();
    }
}
