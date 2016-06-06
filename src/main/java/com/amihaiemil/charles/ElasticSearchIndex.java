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

/**
 * Information about the elastic search index.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 *
 */
public class ElasticSearchIndex {
	/**
	 * Host name of the elasticsearch node.
	 */
	private String node; 
	
	/**
	 * Port.
	 */
	private int port;
	
	/**
	 * The index into which the data goes.
	 */
    private String indexName;
    /**
     * The logical type of the data within the index.
     */
    private String type;
    
    public ElasticSearchIndex() {
    	this("", 9200, "", "");
	}
    
    public ElasticSearchIndex(String node, int port, String indexName, String type) {
    	this.node = node;
    	this.port = port;
    	this.indexName = indexName;
    	this.type = type;
	}
    public String getNode() {
    	return node;
    }
    public int getPort() {
    	return port;
    }
	public String getIndexName() {
		return indexName;
	}
	public String getType() {
		return type;
	} 
    public String toString() {
    	return "http://" + this.node + ":" + this.port + "/" + this.indexName + "/" + this.type;
    }
}
