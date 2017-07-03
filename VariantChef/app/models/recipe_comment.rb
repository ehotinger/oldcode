class RecipeComment < ActiveRecord::Base
  belongs_to :user
  belongs_to :recipe

  validates :user_id, 	presence: true
  validates :recipe_id, presence: true
  validates_length_of :message, :minimum => 10, :maximum => 200, :allow_blank => false
end
