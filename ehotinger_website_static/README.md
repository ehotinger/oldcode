ehotinger.com
=============
Hey, this is the second version of my site which is primarily static content.  I like music, videos, and *things*.  So I'll use these tools to share them!


==============

You can use the automation script to take any content inside of `new-content.html` and turn it into a new post for the website.  The HTML defined within the html file will be automatically copied into the inner HTML of the various pages.

**Here's a sample command:**

```
automation.py -n United States of Pop 2013 by DJ Earworm -t Music Remix Pop -c Music
```

```
-n specifies the title of the post
-t specifies a list of tags you want for the post
-c specifies which category this post should fall under "All"
```


Just be forewarned: you'll need to get soupy to run the Python script.  Don't be 'fraid though, just remember the sauce is boss.

```
$ pip install beautifulsoup4
```
