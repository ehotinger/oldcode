class ApplicationController < ActionController::Base
  # Prevent CSRF attacks by raising an exception.
  # For APIs, you may want to use :null_session instead.
  protect_from_forgery with: :exception

  before_action :configure_permitted_parameters, if: :devise_controller?

  protected

  def configure_permitted_parameters
    devise_parameter_sanitizer.for(:sign_up) { |u| u.permit(:first_name, :last_name, :user_name, :email, :password, :avatar) }
    devise_parameter_sanitizer.for(:sign_in) { |u| u.permit(:user_name, :email) }
    devise_parameter_sanitizer.for(:account_update) { |u| u.permit(:first_name, :last_name, :avatar, :password, :current_password, :password_confirmation) }
  end

  def render_404
    respond_to do |format|
      format.html { render :file => "#{Rails.root}/public/404", :layout => false, :status => :not_found }
      format.xml  { head :not_found }
      format.any  { head :not_found }
    end
  end
end