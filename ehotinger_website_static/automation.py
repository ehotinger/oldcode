import argparse
import shutil
import re
import os
from bs4 import BeautifulSoup

parser = argparse.ArgumentParser(description='Automatically spit out static content for ehotinger')
parser.add_argument('-n','--new', help='The title of a new Post', nargs='+', required=True)
parser.add_argument('-t','--tags', help='The tags for a new Post', nargs='+', required=True)
parser.add_argument('-c','--category', help='The category this post should fall under "All"', required=True)

args = parser.parse_args()

originalTitle = ' '.join(str(x) for x in args.new)
category = args.category

slugTitle = originalTitle.encode('ascii', 'ignore').lower()
slugTitle = re.sub(r'[^a-z0-9]+', '-', slugTitle).strip('-')
slugTitle = re.sub(r'[-]+', '-', slugTitle)


if os.path.exists(slugTitle):
	print 'A directory named ' + slugTitle + ' already exists'

else:
	print 'Making a new directory named "' + slugTitle + '"'
 	os.makedirs(slugTitle)
	shutil.copyfile('template.html', slugTitle + '/template.html')
	soup = BeautifulSoup(open(slugTitle + '/template.html'))

	newContent = BeautifulSoup(open('new-content.html'))

	# Insert the meta tags based on the original title we passed in.
	for meta in soup.findAll('meta'):
		if meta.get('name') == 'description' or meta.get('name') == 'keywords':
			meta['content'] = originalTitle

	# Replace the title tag with the original title we passed in.
	soup.find('title').string.replaceWith(originalTitle + ' | Eric Hotinger')

	tagsList = soup.find('ul', class_='tags')

	for tag in args.tags:
		if tag.lower() == 'chill':
			tagTitle = 'Chill Music'
		elif tag.lower() == 'remix':
			tagTitle = 'Remixes'
		elif tag.lower() == 'electronic':
			tagTitle = 'Electronic Music'
		else:
			tagTitle = tag

		a = soup.new_tag("a", href='../'+ tag.lower(), title='View all ' + tagTitle)
		a.string = tag
		a['class'] = 'tag'
		li = soup.new_tag("li")

		li.append(a)
		tagsList.append(li)

		# Add the new post to the tags' hierarchies.
		tagSoup = BeautifulSoup(open(tag.lower() + '/index.html'))

		tagUlSoup = tagSoup.findAll('ul')[1]

		a = tagSoup.new_tag("a", href='../' + slugTitle)
		a.string = originalTitle

		li = tagSoup.new_tag("li")
		li.append(a)

		tagUlSoup.insert(0, li)

		with open(tag.lower() + '/temp.html', 'w') as file:
			file.write(tagSoup.prettify("utf-8"))
			os.remove(tag.lower() + '/index.html')
		os.rename(tag.lower() + '/temp.html', tag.lower() + '/index.html')
		# TODO

	postContent = soup.find('div', class_='post-content')

	postContent.replaceWith(newContent)
	postContent.prettify(formatter="html")

	# Clean up the old template file and write the new one as "index.html" in the
	# new hierarchy			
	with open(slugTitle + "/index.html", "wb") as file:
		file.write(soup.prettify("utf-8"))
		os.remove(slugTitle + '/template.html')

	# Add the new post to 'All'
	allSoup = BeautifulSoup(open('all/index.html'))

	if category.lower() == 'music':
		allUlSoup = allSoup.findAll('ul')[1]
	elif category.lower() == 'blog':
		allUlSoup = allSoup.findAll('ul')[2]
	else:
		allUlSoup = allSoup.findAll('ul')[3]
	
	a = allSoup.new_tag("a", href='../' + slugTitle)
	a.string = originalTitle

	li = allSoup.new_tag("li")
	li.append(a)

	allUlSoup.insert(0, li)

	with open('all/temp.html', 'w') as file:
		file.write(allSoup.prettify("utf-8"))
		os.remove('all/index.html')
	os.rename('all/temp.html', 'all/index.html')


# Sample input:
# automation.py -n The Winners -t Music Chill -c Music