class FavoriteRecipesController < ApplicationController
  before_action :authenticate_user!

  def create
    @recipe = Recipe.find(params[:favorite_recipe][:recipe_id])
    current_user.favorite!(@recipe)

    respond_to do |format|
      format.html { redirect_to :back }
      format.js
    end
  end

  def destroy
    @recipe = FavoriteRecipe.find(params[:id]).recipe
    current_user.unfavorite!(@recipe)

    respond_to do |format|
      format.html { redirect_to :back }
      format.js
    end
  end
end