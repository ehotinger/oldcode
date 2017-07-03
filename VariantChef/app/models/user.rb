class User < ActiveRecord::Base
  extend FriendlyId

  # SLUG
  friendly_id :user_name, use: :slugged

  # DEVISE
  devise :database_authenticatable, :registerable,
    :recoverable, :rememberable, :trackable, :validatable

  # RECIPES
  has_many :recipes, dependent: :destroy

  has_many :favorite_recipes, dependent: :destroy
  has_many :favorites, through: :favorite_recipes, source: :recipe

  # COMMENTS
  has_many :recipe_comments, dependent: :destroy
  has_many :comments, through: :recipe_comments, source: :recipe

  # GROUPS
  has_many :memberships, dependent: :destroy
  has_many :groups, through: :memberships, source: :group

  # FOLLOWERS
  has_many :relationships, foreign_key: "follower_id", dependent: :destroy
  has_many :followed_users, through: :relationships, source: :followed

  has_many :reverse_relationships, foreign_key: "followed_id",
    class_name:  "Relationship",
    dependent:   :destroy

  has_many :followers, through: :reverse_relationships, source: :follower

  # AVATAR 
  has_attached_file :avatar,
    :storage => :s3,
    :s3_credentials => {:bucket => ENV['S3_BUCKET_NAME'],
                        :access_key_id => ENV['AWS_ACCESS_KEY'],
                        :secret_access_key => ENV['AWS_SECRET_ACCESS_KEY']
                        },
  :styles => {
    :small    => ['128x128#',   :jpg]
  },
    :convert_options => { :all => '-background white -flatten +matte' },
    :path => ':attachment/:id/:style.:extension'

  # Validate the attached image is image/jpg, image/png, etc
  validates_attachment_content_type :avatar, :content_type => /\Aimage\/.*\Z/

  # VALIDATION
  auto_strip_attributes :user_name, :email, :nullify => false

  validates :user_name, :uniqueness => { :case_sensitive => false }, presence: true
  validates_length_of :user_name, :minimum => 4, :maximum => 25, :allow_blank => false

  validates :first_name, presence: true
  validates :last_name, presence: true

  validates_length_of :last_name, :minimum => 2, :maximum => 25, :allow_blank => false
  validates_length_of :first_name, :minimum => 2, :maximum => 25, :allow_blank => false

  def full_name
    "#{first_name} #{last_name}"
  end

  def membership_for_group(group)
    Membership.where(user_id: self.id, group_id: group.id).first
  end

  def following?(person)
    relationships.find_by(followed_id: person.id)
  end

  def follow!(person)
    relationships.create!(followed_id: person.id)
  end

  def unfollow!(person)
    relationships.find_by(followed_id: person.id).destroy
  end

  def favorited?(recipe)
    favorite_recipes.find_by(recipe_id: recipe.id)
  end

  def favorites
    favorite_recipes.joins("LEFT OUTER JOIN favorite_recipes a ON favorite_recipes.recipe_id = a.recipe_id")
        .select("favorite_recipes.*, COUNT(a.recipe_id) AS num_favorites").group("a.recipe_id").order("num_favorites desc")
  end

  def favorite!(recipe)
    favorite_recipes.create!(recipe_id: recipe.id, user_id: self.id)
  end

  def comment(recipe_comment)
    recipe_comments.create(recipe_id: recipe_comment.recipe_id, user_id: self.id, message: recipe_comment.message)
  end

  def unfavorite!(recipe)
    favorite_recipes.find_by(recipe_id: recipe.id).destroy
  end

  def popular_recipes
    recipes.joins("LEFT OUTER JOIN favorite_recipes ON recipes.id = favorite_recipes.recipe_id")
        .select("recipes.*, COUNT(recipe_id) AS num_favorites").group("recipes.id").order("num_favorites desc")
  end
end