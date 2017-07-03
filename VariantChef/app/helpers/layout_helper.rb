module LayoutHelper

  def bootstrap_version
    "3.2.0"
  end

  # Adds the mailto: to an email.
  def mail_to_format(email)
    "mailto:#{email}"
  end

  def get_shared_favorites_format(current_user, other_user)
    pluralize((current_user.favorite_recipes & other_user.favorite_recipes).count, 'favorite recipe')
  end

  def get_shared_following_format(current_user, other_user)
    pluralize((current_user.followed_users & other_user.followed_users).count, 'chef')
  end

  def get_shared_followers_format(current_user, other_user)
    pluralize((current_user.followers & other_user.followers).count, 'follower')
  end

  def get_total_groups()
    pluralize(Group.count, 'group')
  end

  def get_total_group_members()
    pluralize(Membership.count, 'chef')
  end

  def get_group_members(group)
    pluralize(group.memberships.count, 'chef')
  end

  def get_recipe_favorited_by_format(recipe)
    pluralize((recipe.favorited_by).count, 'chef')
  end

  def get_time_conversion(time)
    distance_of_time_in_words(Time.at(0), Time.at(time * 60))
  end

end