class MembershipsController < ApplicationController
  before_action :authenticate_user!, only: [:create, :destroy]

  def create
    @group = Group.find(membership_params[:group_id])

    redirect_to edit_group_path(@group) unless current_user == @group.admin

    @user = User.find_by_email(membership_params[:user_id])

    @membership = Membership.new
    @membership.user = @user
    @membership.group = @group
    @membership.is_admin = false

    respond_to do |format|
      if !@user
        format.html { redirect_to edit_group_path(@group), alert: "Unable to find a user with the email #{membership_params[:user_id]}" }
      elsif @membership.save
        format.html { redirect_to edit_group_path(@group), notice: "#{@user.full_name} was successfully added to the group!" }
      else
        format.html { redirect_to edit_group_path(@group), alert: "Error: #{@user.full_name} is already in the group." }
      end
    end
  end

  def destroy
    @membership = Membership.find(params[:id])
    @user = @membership.user

    @group = @membership.group

    redirect_to group_path(@group) unless @group.admin == current_user

    respond_to do |format|
      if @membership.destroy
        format.html { redirect_to edit_group_path(@group), notice: "#{@user.full_name} was removed from the group" }
      else
        format.html { redirect_to edit_group_path(@group), notice: "Unable to remove #{@user.full_name} from the group" }
      end
    end
  end

  private
  def membership_params
    params.require(:membership).permit(:user_id, :group_id, :id)
  end
end
