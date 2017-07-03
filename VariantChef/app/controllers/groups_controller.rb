class GroupsController < ApplicationController
  before_action :set_group, only: [:show, :edit, :add_user, :update, :destroy]
  before_action :authenticate_user!, only: [:edit, :add_user, :destroy]

  def index
    @groups = Group.limit(100)
  end

  def show
    @recipes = Recipe.joins("INNER JOIN memberships a ON recipes.user_id = a.user_id")
    .where("a.group_id = ?", @group.id)
    .order("recipes.created_at desc")
    .limit(50)
  end

  def new
    @group = Group.new
  end

  def edit
    redirect_to @group unless current_user == @group.admin

    # Make a blank membership that could be filled out if more users are to be added to the group
    @membership = @group.memberships.build
  end

  def create
    @group = Group.new(group_params)

    @membership = @group.memberships.build(:user_id => current_user.id, :is_admin => true)

    respond_to do |format|
      if @group.save
        format.html { redirect_to @group, notice: 'Group was successfully created!' }
      else
        format.html { render action: 'new' }
      end
    end
  end

  def destroy
    redirect_to @group unless current_user == @group.admin

    @group.destroy

    respond_to do |format|
      format.html { redirect_to groups_url }
    end
  end

  private
  def set_group
    @group = Group.friendly.find(params[:id])
  end

  def group_params
    params.require(:group).permit(:title, :user_id, :group_id, :description)
  end
end
