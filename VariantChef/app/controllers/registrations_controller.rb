class RegistrationsController < Devise::RegistrationsController
	def update
	    # For Rails 4
	    account_update_params = devise_parameter_sanitizer.sanitize(:account_update)

	    # required for settings form to submit when password is left blank
	    if account_update_params[:password].blank?
	      account_update_params.delete("password")
	      account_update_params.delete("password_confirmation")
	    end

	    @user = User.find(current_user.id)
	    if @user.update_with_password(account_update_params)
	      set_flash_message :notice, :updated

	      # Sign in the user bypassing validation in case his password changed
	      sign_in @user, :bypass => true
	      redirect_to @user
	    else
	      render "edit"
	    end
	  end

  protected

  def after_sign_up_path_for(resource)
    root_path
  end
end
