class RecipeAssetsController < ApplicationController
  before_action :authenticate_user!, only: [:pictures, :update, :destroy, :create]

  def pictures
    @user = current_user

    # If a user tries to add pictures to a recipe that isn't theirs, redirect them.
    redirect_to user_recipe_path(params[:user_id], params[:recipe_id]) unless @user.user_name == params[:user_id]

    @recipe = Recipe.where(:user_id => @user.id).where(:slug => params[:recipe_id]).first
    @recipe_asset = RecipeAsset.new
  end

  def create
    @user = current_user
    @recipe = Recipe.where(:user_id => @user.id).where(:slug => params[:recipe_id]).first
    @recipe_asset = RecipeAsset.new(:recipe_id => @recipe.id, :picture => recipe_asset_params[:picture])

    respond_to do |format|
      if @recipe_asset.save
        format.html { redirect_to user_recipe_path(@user.slug, @recipe.slug), notice: 'Successfully added picture.' }
      else
        format.html { redirect_to user_recipe_pictures_path(@user.slug, @recipe.slug), alert: 'Error: No picture provided.' }
      end
    end
  end

  def destroy
    @user = current_user
    @recipe_asset = RecipeAsset.find(params[:id])

    # If a user tries to delete pictures from a recipe that isn't theirs, redirect them.
    redirect_to user_recipe_path(params[:user_id], params[:recipe_id]) unless @user.user_name == params[:user_id]

    @recipe = Recipe.where(:user_id => @user.id).where(:slug => params[:recipe_id]).first

    @recipe_asset.destroy

    respond_to do |format|
      format.html { redirect_to user_recipe_pictures_path(@user.slug, @recipe.slug), notice: 'Successfully deleted picture from recipe' }
    end
  end

  private

  def recipe_asset_params
    params.fetch(:recipe_asset, {}).permit(:picture)
  end
end
