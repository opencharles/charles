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

import javax.json.JsonObject;

/**
 * Index documents in bulk.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 *
 */
public class EsBulkContent {

	/**
	 * JSON docs to be indexed.
	 */
	private List<JsonObject> docs;
	
	public EsBulkContent(List<JsonObject> docs) {
		if(docs == null || docs.size() == 0) {
			throw new IllegalArgumentException("There must be at least 1 document!");
		}
		
		this.docs = new ArrayList<JsonObject>();
		for(JsonObject doc : docs) {
			this.docs.add(doc);
		}
	}
	
	/**
	 * Size of the documents list.
	 */
	public int size() {
		return this.docs.size();
	}
	
	/**
	 * Pepare the json structure for bulk indexing.
	 * @param docs The json documents to be indexed.
	 * @return The json structure as a String.
	 */
	public String structure() {
		StringBuilder sb = new StringBuilder();
		for(JsonObject doc : docs) {
			String id = doc.getString("id", "");
			String action_and_meta_data;
			if(id.isEmpty()) {
			    action_and_meta_data = "{\"index\":{}}";
			} else {
				action_and_meta_data = "{\"index\":{\"_id\":\"" + id + "\"}}";
			}
			sb = sb.append(action_and_meta_data).append("\n");
			sb = sb.append(doc.toString()).append("\n");
		}
		return sb.toString();
	}

}
