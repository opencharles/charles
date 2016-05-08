# charles
Smart web crawler.

[![Build Status](https://travis-ci.org/amihaiemil/charles.svg?branch=master)](https://travis-ci.org/amihaiemil/charles)

### v 1.0.0 (to be released)

A smart web crawler that fetches data from a website and stores it in some way (writes it in files on the disk or POSTs it to an http endpoint) . 

2 options for crawling: 

1) crawl the links from a ``sitemap.xml``

2) crawl the website as a graph starting from a given url (the index)

### Under the hood

Charles is powered by [Selenium WebDriver 2.41](http://www.seleniumhq.org/projects/webdriver/) and [PhantomJS](http://phantomjs.org/) through [GhostDriver](https://github.com/detro/ghostdriver). Crawling with other, graphical, drivers like ChromeDriver and FirefoxDriver will be implemented.

### How to contribute

1. Open an issue regarding an improvement you thought of, or a bug you noticed.
2. If the issue is confirmed, fork the repository, do the changes on a sepparate branch and make a Pull Request.
3. After review and acceptance, the PR is merged and closed.
4. You are automatically listed as a contributor on the project's site

Make sure the maven build

''$mvn clean install -Pitcases''

passes before making a PR. 

### Running integration tests: 

In order to run the integration tests you need to have PhantomJS installed on your machine and set the JVM system property ``phantomjsExec`` to point to that location. By default the exe is looked up at ``/usr/local/bin/phantomjs`` (linux), so if it's not found the tests won't work.
