from bs4 import BeautifulSoup
import requests
import dryscrape
import json
from Item import Item

url = "http://rose-hulman.cafebonappetit.com/"

session = dryscrape.Session()

print("Retrieving page...")
session.visit(url)


print("Scraping meal information...")
menu_page = session.body()
soup = BeautifulSoup(menu_page, 'html.parser')

breakfast = soup.select("#panel-daypart-menu-1 article .daypart-menu .column article")
lunch = soup.select("#panel-daypart-menu-2 article .daypart-menu .column article")
dinner = soup.select("#panel-daypart-menu-3 article .daypart-menu .column article")

meals = []

for meal in [breakfast, lunch, dinner]:
    meal_dict = {"hours": "", "items": []}
    for item in meal:
        item = BeautifulSoup(str(item), 'html.parser')
        item_name = item.span
        item_icons = item.select(".station-item-title .cor-icons")

        icons = BeautifulSoup(str(item_icons), 'html.parser')
        icon_titles = list(img.get('title') for img in icons.find_all('img'))

        calories = item.select('ul .nutrition span')
        calories = BeautifulSoup(str(calories), 'html.parser')
        calories = calories.get_text()

        item_name = item_name.get_text().strip()
        item = {"name": item_name, "icons": icon_titles, "calories": calories}
        meal_dict["items"].append(item)
    meals.append({"meal": meal_dict})

print(json.dumps(meals))


