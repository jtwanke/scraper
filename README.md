# scraper
A website scraper for those curious about web scraping and web apps. 

About
-----
Scraper is an educational tool that can help beginners learn about web scraping. At its current state, Scraper takes in two inputs (search phrase, user location) and scrapes information, related to those inputs, from a list of target sites. 

The target sites that Scraper uses are currently Yelp and YellowPages, although more will likely be added in the future to showcase different website scraping techniques. 

### Scraping with JSoup

Scraper uses the open source library [JSoup](https://jsoup.org/) to collect and parse HTML content from the target sites. In order to do this, we provide JSoup's libraries with [css/jquery selectors](https://jsoup.org/cookbook/extracting-data/selector-syntax) that identify which information we are interested in. In the future, these selectors will be stored in a database so that updating the application doesn't require a re-build. 

Build
-----

Scraper is managed using [Maven](https://maven.apache.org/) so a typical build would look something like `mvn clean package` in the terminal. 

Exe
---

To run Scraper, you'll need an active installation of [Apache Tomcat](https://tomcat.apache.org/) on your machine. You can either deploy the prebuilt .war file in the scraper/target directory, or follow the instructions in the 'Build' section of this document and deploy your own build. 

If you're having trouble deploying the code, take a look at [Deployment with Tomcat](https://tomcat.apache.org/tomcat-7.0-doc/appdev/deployment.html#Deployment_With_Tomcat).

Usage
-----

Once you've successfully deployed your Scraper build, go to `localhost:8080/scraper/` (or the appropriate port for your Tomcat installation) and you'll find a form with two text fields, two checkboxes, and a button labeled "Go!"

The text fields are for the phrase and location to be used during the scrape of the target sites. The checkboxes can be used to specify which sites you would like to scrape. 

More To Come
------------

Scraper is a newborn, and will be improved as time goes on. You can look forward to the following features:

* Database functionality (saving selectors, queries, user accounts)
* Input validation (telling the user why their search is bad)
* Analytics (what can we learn from the scraper results?)

That being said, the above list is not exhaustive and is likely to change. If you have any suggestions feel free to email the author.
