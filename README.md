<img src="http://www.amihaiemil.com/images/roundcharleslogo.PNG" align="left" height="100" width="100"/>
##charles

Smart web crawler.
[![Build Status](https://travis-ci.org/opencharles/charles.svg?branch=master)](https://travis-ci.org/opencharles/charles)
[![PDD status](http://www.0pdd.com/svg?name=opencharles/charles)](http://www.0pdd.com/p?name=opencharles/charles)
[![Coverage Status](https://coveralls.io/repos/github/opencharles/charles/badge.svg?branch=master&service=github)](https://coveralls.io/github/opencharles/charles?branch=master)

A smart web crawler that fetches data from a website and stores it in some way (writes it in files on the disk or POSTs it to an http endpoint etc) .

More options for crawling: 

1) crawl the links from a ``sitemap.xml``

2) crawl the website as a graph starting from a given url (the index)

3) crawl with retrial if any ``RuntimeException`` happens etc

**More details** in [this](http://www.amihaiemil.com/web/2016/12/05/project-charles.html) post.

### Maven dependency

Get it using Maven: 

```
<dependency>
    <groupId>com.amihaiemil.web</groupId>
    <artifactId>charles</artifactId>
    <version>1.1.1</version>
</dependency>
```

or take the <a href="https://oss.sonatype.org/service/local/repositories/releases/content/com/amihaiemil/web/charles/1.1.1/charles-1.1.1-jar-with-dependencies.jar">fat</a> jar.
### Under the hood

Charles is powered by [Selenium WebDriver](http://www.seleniumhq.org/projects/webdriver/).
Any WebDriver implementation can be used to build a ``WebCrawl``
Examples:
  - [PhantomJsDriver](https://github.com/detro/ghostdriver)
  - FirefoxDriver
  - ChromeDriver etc

Since it uses a web driver to render the pages, also any dynamic content will be crawled (e.g. content generated by javascript)

### How to contribute

Read this [post](http://www.amihaiemil.com/2016/12/30/becoming-a-contributor.html).

1. Open an issue regarding an improvement you thought of, or a bug you noticed.
2. If the issue is confirmed, fork the repository, do the changes on a sepparate branch and make a Pull Request.
3. After review and acceptance, the PR is merged and closed.
4. You are automatically listed as a contributor on the project's site

Make sure the maven build

``$mvn clean install -Pitcases``

passes before making a PR. 

### Running integration tests: 

In order to run the integration tests you need to have PhantomJS installed on your machine and set the JVM system property ``phantomjsExec`` to point to that location. By default the exe is looked up at ``/usr/local/bin/phantomjs`` (linux), so if it's not found the tests won't work.
