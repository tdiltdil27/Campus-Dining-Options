from bs4 import BeautifulSoup
import requests
import dryscrape
from Item import Item

url = "http://rose-hulman.cafebonappetit.com/"

session = dryscrape.Session()
session.visit(url)
menu_page = session.body()

#menu_page = requests.get("http://rose-hulman.cafebonappetit.com/");
soup = BeautifulSoup(menu_page, 'html.parser')

scripts = soup.select('#panel-daypart-menu-1 script')
script = BeautifulSoup(str(scripts), 'html.parser').get_text()

start = script.find("Bamco.menu_items") + 19
end = script.find("Bamco.cor_icons")

data = script[start:end].strip()
print(data)
