class RecipecontentsController < ApplicationController
  #before_action :set_recipe, only: [:content]
  #before_action :set_recipe, only: [:show, :edit, :update, :destroy]

  before_filter :authenticate_user!,
    :only => [:create, :update, :destroy, :show, :new]

  def content
    @user = current_user

    # If a user tries to add pictures to a recipe that isn't theirs, redirect them.
    redirect_to user_recipe_path(params[:user_id], params[:recipe_id]) unless @user.user_name == params[:user_id]

    @recipe = Recipe.where(:user_id => @user.id).where(:slug => params[:recipe_id]).first

    @recipecontent = Recipecontent.build
  end

  def new
    @recipecontent = Recipecontent.build
  end

  # Verify that current_user_logged_in == user_id
  def create
    @user = current_user
    @recipe = Recipe.where(:user_id => @user.id).where(:slug => params[:recipe_id]).first
    @recipecontent = Recipecontent.build(recipecontent_params)

    respond_to do |format|
      if @recipecontent.save
        format.html { redirect_to user_recipe_path(@user.user_name, @recipe.slug), notice: 'Successfully added content.' }
      else
        format.html { redirect_to user_recipe_content_path(@user.user_name, @recipe.slug), alert: 'Failed to save content' }
      end
    end
  end

  def update
    @recipecontent = Recipecontent.new

    respond_to do |format|
      if @recipecontent.update_attributes(recipecontent_params)
        format.html { redirect_to @recipecontent, notice: 'Recipe Content was successfully updated.' }
        format.json { head :no_content }
      else
        format.html { render action: 'edit' }
        format.json { render json: @recipecontent.errors, status: :unprocessable_entity }
      end
    end
  end

  def destroy
    @recipecontent.delete() # or .destroy

    respond_to do |format|
      format.html { redirect_to recipes_url }
      format.json { head :no_content }
    end
  end

  private
  def recipecontent_params
    params.require(:recipecontent).permit(:user_id, :recipe_id, :ingredients, :directions, :prep_time, :cook_time, :forked_from)
  end
end
