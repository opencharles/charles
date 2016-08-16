package com.amihaiemil.charles;

import java.util.ArrayList;
import java.util.List;

/**
* Crawled pages are "exported" in a List.
* Use this when you have a reasonable number of pages that you can afford to keep in memory
* and handle later. Use <b>getCrawledPages()</b> to fetch the crawled pages
* after calling WebCrawl.crawl().
* <br><br>
* E.g. This class is suitable for unit tests.
* 
* @author Mihai Andronache (amihaiemil@gmail.com)
*
*/
public class InMemoryRepository implements Repository {

    /**
     * Holds all the crawled pages.
     */
    private List<WebPage> pages = new ArrayList<WebPage>();

    /**
     * Get all the pages from this Repository.
     * @return List of pages.
     */
    public List<WebPage> getCrawledPages() {
    	return this.pages;
    }

	@Override
	public void export(List<WebPage> pages) throws DataExportException {
		for(WebPage page : pages) {
			this.pages.add(page);
		}
	}
}
