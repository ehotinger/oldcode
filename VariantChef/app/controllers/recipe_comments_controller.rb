class RecipeCommentsController < ApplicationController
  before_action :authenticate_user!

  def create
    @recipe_comment = RecipeComment.new(recipe_comment_params)
    @recipe = Recipe.friendly.find(recipe_comment_params[:recipe_id])

    respond_to do |format|
      if current_user.comment(@recipe_comment).save
        format.html { redirect_to user_recipe_path(@recipe.user.slug, @recipe.slug), notice: 'Successfully added comment.' }
      else
        format.html { redirect_to user_recipe_path(@recipe.user.slug, @recipe.slug), alert: 'Failed to add comment. Must be at least 10 characters long.' }
      end
    end
  end

  private
  def recipe_comment_params
    params.require(:recipe_comment).permit(:user_id, :recipe_id, :message)
  end
end