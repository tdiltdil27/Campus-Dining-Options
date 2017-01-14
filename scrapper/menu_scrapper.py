from bs4 import BeautifulSoup
import requests

menu_page = requests.get("http://rose-hulman.cafebonappetit.com/");
soup = BeautifulSoup(menu_page.content, 'html.parser')
print(soup.prettify())
