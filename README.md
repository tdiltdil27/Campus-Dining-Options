# Campus-Dining-Options

### Android Application

This application will display the operating hours of the dining options on the Rose-Hulman Institute of Technology campus as well as the daily menu for the cafeteria.

### Scraping Data

We wrote a python script that scrapes http://rose-hulman.cafebonappetit.com/ for our application data.

It can currently:
- get the avalable name and time for each meal
- get the daily menu items from each meal offered in all dining options
  across Rose-Hulmans campus
- each menu item includes its title, amount of calories and information
  on whether it is vegetarian, vegan or gluten free.

This data is then formatted into JSON to be read in by our android app.

We exposed a public API that serves this JSON data available at
https://campus-meal-scraper.herokuapp.com/locations/YYYY-MM-DD/

To get all meal information for the current date, replace
YYYY-MM-DD with the appropriate date. For example, to get meal menu
information for January 31st, 2017, go to
https://campus-meal-scraper.herokuapp.com/locations/2017-01-31/

We moved the scrapper to a separate repository to make it easy to deploy
to Heroku. You can find it at
https://github.com/sbrand83/campus-meal-scraper.
