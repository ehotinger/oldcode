#VariantChef

It’s a social website like Github and Pinterest, but for chefs and cooks to share, discover, and store recipes.

You can create recipes, attach pictures of your fantastical food creations, and much more.

Other users can “fork” your recipes into their own variations, adding a different ingredient or technique to make it their own!

##Installation:

```
git clone https://github.com/ehotinger/VariantChef.git
bundle update
bundle install
rake db:create db:migrate
rails s
```

You will also have to manage several environment variables. 

Depending on your operating system, this could vary.

Assuming Ubuntu, you'll want to `export key="value"` an `AWS_ACCESS_KEY_ID`, an `AWS_SECRET_ACCESS_KEY`, and a `S3_BUCKET_NAME`

These are the bare minimum connection points necessary to not only run the application locally, but deploy to production as well.

####Ruby version:

Developed, tested, and deployed in 1.9.3, but anything >= should be fine.

####Managing the Database:

All you have to do is `rake db:create db:migrate` and you're golden.

By default, the development database will login as root without a password. If your mysql root has a password, set it to `NULL` or change the database's config.

You should be able to use `mysql -u root` or `rails dbconsole` to login and then type `use variantchef_development;` to switch on the development database. From there, you can use `show tables;` to browse around the various tables and collect insights from them.


####How to run the test suite:

```
bundle exec rspec spec/
```

####Deployment instructions:

Using Elastic Beanstalk, it should be straightforward:

```
eb init
git aws.push
eb start
```

Then you can update the corresponding environment variables above within your EB instance.