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

import org.junit.Test;

import com.jcabi.http.mock.MkAnswer;
import com.jcabi.http.mock.MkContainer;
import com.jcabi.http.mock.MkGrizzlyContainer;

/**
 * Test cases for {@link ElasticSearchRepository}
 * @author Mihai Andronache (amihaiemil@gmail.com)
 *
 */
public class ElasticSearchRepositoryTestCase {
	
	
	/**
	 * {@link ElasticSearchRepository} can send the given list of json docs
	 * to the specified elastisearch index.
	 * @throws Exception - If something goes wrong.
	 */
	@Test
    public void indexesListOfDocuments() throws Exception {
		List<WebPage> pages = new ArrayList<WebPage>();
		pages.add(this.webPage("http://www.amihaiemil.com/index.html"));
		pages.add(this.webPage("http://eva.amihaiemil.com/index.html"));
    	
    	MkContainer server = new MkGrizzlyContainer()
            .next(new MkAnswer.Simple("{\"response\":\"ok\", \"errors\":false, \"took\":1}"))
            .next(new MkAnswer.Simple(200))
            .start(9201);
    	
    	ElasticSearchRepository elasticRepo = new ElasticSearchRepository(
            "http://localhost:9201/test5"
        );
    	try {
    	    elasticRepo.export(pages);
    	} finally {
    		server.close();
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
		page.setTextContent("Test content of this awesome test page.");
	    page.setCategory("page");
		return page;
	}

}
