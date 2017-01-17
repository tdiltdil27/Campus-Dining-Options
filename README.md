# Campus-Dining-Options

### Android Application

This application will display the operating hours of the dining options on the Rose-Hulman Institute of Technology campus as well as the daily menu for the cafeteria.

### Scaping Data

We wrote a python script that scrapes http://rose-hulman.cafebonappetit.com/ for our application data.

It can currently:
- get the daily menu items from each meal offered in the union
  cafeteria
- each menu item includes its title, amount of calories and information
  on whether it is vegetarian, vegan or gluten free.

This data is then formatted into JSON to be read in by our android app.