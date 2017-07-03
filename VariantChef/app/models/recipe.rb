class Recipe < ActiveRecord::Base
  extend FriendlyId

  belongs_to :user

  has_many :favorite_recipes, dependent: :destroy
  has_many :favorited_by, through: :favorite_recipes, source: :user

  has_many :recipe_assets, dependent: :destroy
  has_many :recipe_tags, dependent: :destroy

  has_many :recipe_comments, dependent: :destroy
  has_many :comments, through: :recipe_comments, source: :user

  auto_strip_attributes :title, :nullify => false
  auto_strip_attributes :short_description, :nullify => false

  accepts_nested_attributes_for :recipe_assets, :reject_if => lambda { |a| a[:content].blank? }, :allow_destroy => true

  friendly_id :title, use: [:slugged, :scoped], :scope => :user

  validates_length_of :title, :minimum => 5, :maximum => 25, :allow_blank => false
  validates_length_of :short_description, :minimum => 5, :maximum => 200, :allow_blank => false

  validates_uniqueness_of :slug, :scope => :user

  def to_param
    slug
  end

  # For now, take the top 10 newest comments for any particular recipe.
  def newest_recipe_comments
    recipe_comments.order("created_at desc").limit(10)
  end

  def self.search(search)
    if search
      where('lower(title) LIKE ?', "%#{search.downcase}%").order("created_at desc").limit(50)
    else
      order("created_at desc").limit(50)
    end
  end

  def favorited_by_users
    favorited_by.order("created_at desc").limit(10)
  end

end
