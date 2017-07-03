Rails.application.routes.draw do

  root "static_pages#home"

  devise_for :users, :path => '', :path_names => { :sign_in => "login", :sign_out => "logout", :sign_up => "register" }, :controllers => { :registrations => "registrations" }

  # Specific relationships
  resources :relationships, only: [:create, :destroy]
  resources :favorite_recipes, only: [:create, :destroy]
  resources :recipe_comments, only: [:create, :destroy]

  # Static Pages
  controller :static_pages do
    get 'site/help',  to: 'static_pages#help',  as: 'help'
    get 'site/about', to: 'static_pages#about', as: 'about'
    get 'site/terms', to: 'static_pages#terms', as: 'terms'
    get 'site/blog',  to: 'static_pages#blog',  as: 'blog'
    get 'site/random-recipe', to: 'static_pages#random', as: 'random-recipe'
    get 'site/home', to: 'static_pages#home', as: 'home'
    get 'site/groups', to: 'static_pages#groups', as: 'group-info'
  end

  resources :recipe_tags, :path => 'tags', :only => [:show]

  resources :groups do
    resources :memberships, :only => [:create, :destroy]
  end

  resources :users, :path => '', :only => [:show] do
    get :followers
    get :following
    get :favorites
  end

  resources :recipes, :only => [:new, :create] do
    post :fork
  end

  resources :users, :path => '', :only => [] do
    resources :recipes, :path => '', :only => [:show, :destroy, :update] do
      get 'pictures', to: 'recipe_assets#pictures', as: 'pictures'
      get 'content', to: 'recipecontents#content', as: 'content'
      get 'tags', to: 'recipe_tags#tags', as: 'tags'

      resources :recipecontents, :path => 'content', :only => [:create]
      resources :recipe_assets, :path => 'assets', :only => [:create, :destroy]
      resources :recipe_tags, :path => 'tags', :only => [:create, :destroy]
    end
  end
end
