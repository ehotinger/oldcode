class StaticPagesController < ApplicationController
  def home
    @recipes = Recipe.search(params[:q])
  end

  def groups
    @groups = Group.all
  end

  def help
  end

  def about
  end

  def terms
  end

  def blog
  	redirect_to "https://medium.com/@ehotinger"
  end

  def random
    rand_id = rand(Recipe.count)
    random_recipe = Recipe.where("id >=?", rand_id).first

    if random_recipe
      redirect_to user_recipe_path(random_recipe.user.slug, random_recipe.slug)
    else
      render_404
    end
  end 
end