class RecipesController < ApplicationController
  before_action :set_recipe, only: [:show, :edit, :destroy, :favorite]
  before_action :authenticate_user!, only: [:new, :destroy, :favorite, :fork]

  def show
    # Set the current user on the page,
    @user = User.friendly.find(params[:user_id])

    # The shown recipe content is the most recent
    @recipecontent = Recipecontent.where(:user_id => @user.id)
    .where(:recipe_id => @recipe.id).all.sort!{|a,b| b.created_at <=> a.created_at }[0]
  end

  def new
    @recipe = Recipe.new
  end

  def edit
  end

  def fork
    @old_recipe = Recipe.friendly.find(params[:recipe_id])
    @old_user = @old_recipe.user

    @new_recipe = @old_recipe.dup
    @new_user = current_user
    @new_recipe.user = @new_user

    # Update the new recipe's short description to give credit to the previous author.
    @new_recipe.short_description = "A variant of #{@old_recipe.user.full_name}'s original recipe."

    respond_to do |format|
      if @old_user == @new_user
        format.html { redirect_to user_recipe_path(@new_user.slug, @old_recipe), notice: "You can't fork your own recipe!"}

      elsif @new_recipe.save

        # Look up the old recipe's content in DynamoDB.
        # If there's content available, then clone it. Otherwise, do nothing.
        # It's possible that there is no content here, because the original user doesn't necessarily have to plug it in.
        @old_recipecontent = Recipecontent.where(:user_id => @old_user.id)
        .where(:recipe_id => @old_recipe.id).all.sort!{|a,b| b.created_at <=> a.created_at }[0]

        if @old_recipecontent

          @new_recipecontent = Recipecontent.build
          @new_recipecontent.user_id = @new_user.id
          @new_recipecontent.recipe_id = @new_recipe.id
          @new_recipecontent.cook_time = @old_recipecontent.cook_time
          @new_recipecontent.prep_time = @old_recipecontent.prep_time
          @new_recipecontent.directions = @old_recipecontent.directions
          @new_recipecontent.ingredients = @old_recipecontent.ingredients

          if @new_recipecontent.save
            format.html { redirect_to user_recipe_path(@new_user.slug, @new_recipe), notice: "Here's your forked recipe! Don't forget to upload pictures of your new dish!"}
          else
            format.html { redirect_to user_recipe_path(@old_user.slug, @old_recipe), alert: "Unexpected errors occurred. You can't seem to fork this recipe." }
          end
        end

        # Next, clone the tags associated with the previous recipe.
        begin
          @old_recipe.recipe_tags.each do |t|
            @new_recipe_tag = RecipeTag.new(:recipe_id => @new_recipe.id, :title => t.title)
            @new_recipe_tag.save
          end

        rescue
          format.html { redirect_to user_recipe_path(@old_user.slug, @old_recipe), alert: "Unexpected errors occurred when copying tags. You can't seem to fork this recipe." }
        end

        # At this point, we've gathered as much info possible about the recipe and we can assume that it's been cloned successfuly.
        format.html { redirect_to user_recipe_path(@new_user.slug, @new_recipe), notice: "Here's your forked recipe! Don't forget to upload pictures of your new dish!"}

      else
        format.html { redirect_to user_recipe_path(@old_user.slug, @old_recipe), alert: "Unexpected errors occurred. You can't seem to fork this recipe." }
      end
    end

  end

  def create
    @user = current_user
    @recipe = @user.recipes.build(recipe_params)

    respond_to do |format|
      if @recipe.save
        format.html { redirect_to user_recipe_path(@user.slug, @recipe), notice: 'Your recipe was created!' }
      else
        format.html { render action: 'new' }
      end
    end
  end

  def destroy
    @user = User.friendly.find(params[:user_id])

    render_404 unless @user == current_user

    @recipe.destroy

    respond_to do |format|
      format.html { redirect_to user_path(@user), notice: 'Recipe was successfully deleted.' }
    end
  end

  private
  def set_recipe
    begin
      @recipe = User.friendly.find(params[:user_id]).recipes.friendly.find(params[:id])
    rescue
      render_404
    end
  end

  def recipe_params
    params.require(:recipe).permit(:title, :short_description, :user_id, :id, :recipe_assets_attributes => [:id, :picture, :type])
  end
end
