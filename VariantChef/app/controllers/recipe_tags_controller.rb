class RecipeTagsController < ApplicationController
  before_action :authenticate_user!, only: [:tags, :update, :destroy]

  def tags
    @user = current_user

    # If a user tries to add tags to a recipe that isn't theirs, redirect them.
    redirect_to user_recipe_path(params[:user_id], params[:recipe_id]) unless @user.user_name == params[:user_id]

    @recipe = Recipe.where(:user_id => @user.id).where(:slug => params[:recipe_id]).first
    @recipe_tag = RecipeTag.new
  end

  def create
    @user = current_user
    @recipe = Recipe.where(:user_id => @user.id).where(:slug => params[:recipe_id]).first
    @recipe_tag = RecipeTag.new(:recipe_id => @recipe.id, :title => recipe_tag_params[:title])

    respond_to do |format|
      if @recipe_tag.save
        format.html { redirect_to user_recipe_tags_path(@user.user_name, @recipe.slug), notice: 'Successfully added tag.' }
      else
        format.html { redirect_to user_recipe_tags_path(@user.user_name, @recipe.slug), alert: "The tag's title must be at least 4 characters long and less than 15 characters." }
      end
    end
  end

  def destroy
    @user = current_user
    @recipe_tag = RecipeTag.find(params[:id])

    # If a user tries to delete pictures from a recipe that isn't theirs, redirect them.
    redirect_to user_recipe_path(params[:user_id], params[:recipe_id]) unless @user.user_name == params[:user_id]

    @recipe = Recipe.where(:user_id => @user.id).where(:slug => params[:recipe_id]).first

    @recipe_tag.destroy

    respond_to do |format|
      format.html { redirect_to user_recipe_tags_path(@user.slug, @recipe.slug), notice: 'Successfully removed tag from recipe' }
    end
  end

  def show
    @recipes = RecipeTag.search(params[:id])
  end

  private
  def recipe_tag_params
    params.require(:recipe_tag).permit(:user_id, :recipe_id, :title, :id)
  end
end
