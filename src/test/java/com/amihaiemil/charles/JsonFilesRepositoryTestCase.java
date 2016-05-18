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

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import org.mockito.Mockito;
import com.amihaiemil.charles.sitemap.Url;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Unit tests for {@link JsonFilesRepository}
 * @author Mihai Andronache (amihaiemil@gmail.com)
 */
public class JsonFilesRepositoryTestCase {
	
    @Test
    public void exportsPagesToFiles() throws Exception {
    	SnapshotWebPage page = new SnapshotWebPage(Mockito.mock(LiveWebPage.class));
    	page.setTextContent("text on page");
    	page.setTitle("Title | Page");
    	
    	Url url = new Url();
    	url.setLoc("http://amihaiemil.com");
    	url.setChangefreq("monthly");
    	url.setLastmod("15/03/1994");
    	url.setPriority("0.8");
    	
    	page.setUrl(url);
    	
    	File jsonFile = new File("src/test/resources/testpageExport.json");
    	if(!jsonFile.exists()) {
    		jsonFile.createNewFile();
    	}
    	
    	Map<SnapshotWebPage, File> pages = new HashMap<SnapshotWebPage, File>();
    	pages.put(page, jsonFile);
    	Repository testRepo = new JsonFilesRepository(pages);
    	testRepo.export();
    	SnapshotWebPage readPage = (new ObjectMapper()).readValue(jsonFile, SnapshotWebPage.class);
    	
    	assertTrue(readPage.getTitle().equals(page.getTitle()));
    	assertTrue(readPage.getTextContent().equals(page.getTextContent()));
    	assertTrue(readPage.getUrl().equals(page.getUrl()));
    	
    }
}
