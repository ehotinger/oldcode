class Recipecontent
  include Dynamoid::Document

  # Related to the recipe
  field :recipe_id, :integer

  # Creator of the recipe
  field :user_id, :integer
  
  field :ingredients
  field :directions

  field :cook_time, :integer
  field :prep_time, :integer

  validates_presence_of :user_id
  validates_presence_of :recipe_id

end