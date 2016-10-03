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

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.json.Json;
import javax.json.JsonObject;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

/**
 * Unit tests for {@link EsBulkContent}
 * @author Mihai Andronache (amihaiemil@gmail.com)
 *
 */
public class EsBulkContentTestCase {

	/**
     * EsBulkIndex throws exception on empty docs list.
     */
	@Test(expected = IllegalArgumentException.class)
	public void exceptionOnEmptyList() {
		List<WebPage> docs = new ArrayList<WebPage>();
        new EsBulkContent(docs);
    }
	
	/**
     * EsBulkIndex throws exception on null docs list.
     */
	@Test(expected = IllegalArgumentException.class)
	public void exceptionOnNullList() {
        new EsBulkContent(null);
    }
	
	/**
	 * EsBulkContent can create the json bulk for Elastic search _bulk api.
	 * @throws Exception If something goes wrong.
	 */
	@Test
	public void structuresPagesCorrectly() throws Exception {
		List<WebPage> pages = new ArrayList<WebPage>();
		pages.add(this.mockWebPage("http://amihaiemil.com/page.html", "tech"));
		pages.add(this.mockWebPage("http://amihaiemil.com/stuff/page.html", "mischelaneous"));
		pages.add(this.mockWebPage("http://amihaiemil.com/stuff/more/page.html", "development"));

		String bulkStrucure = new EsBulkContent(pages).structure();
		
		String expected = new String(
		    IOUtils.toByteArray(
			    new FileInputStream(
				    new File("src/test/resources/bulkIndexStructure.txt")
				)	
			)
		);
		assertTrue(
			"The 2 structures are not the same! (did you forget to add a final newline (\\n)?",
		    expected.equals(bulkStrucure)
		);
	}
	
	/**
	 * Mock a WebPage for test.
	 * @param url
	 * @param category
	 * @return Webpage instance.
	 */
	private WebPage mockWebPage(String url, String category) {
		WebPage page = new SnapshotWebPage();
		page.setUrl(url);
		page.setCategory(category);

		page.setLinks(new HashSet<Link>());
		page.setTextContent("text content...");
		page.setName("page.html");
		page.setTitle("page | title");
		return page;
	}
	
}


