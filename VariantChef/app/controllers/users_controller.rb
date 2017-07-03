class UsersController < ApplicationController
  before_action :set_user, only: [:show, :edit, :update, :following, :followers]
  before_action :authenticate_user!, only: [:edit, :update]

  def show
    render_404 unless @user
  end

  def following
    @user = User.find_by_user_name(params[:user_id])
  end

  def followers
    @user = User.find_by_user_name(params[:user_id])
  end

  def favorites
    @user = User.find_by_user_name(params[:user_id])
  end

  private
  def set_user
    @user = User.find_by_user_name(params[:id])
  end

  def user_params
    params.require(:user).permit(:user_name, :first_name, :last_name, :email, :avatar)
  end
end
