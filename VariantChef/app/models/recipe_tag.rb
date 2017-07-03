class RecipeTag < ActiveRecord::Base
  belongs_to :recipe

  validates :recipe_id, presence: true
  validates :title, presence: true

  validates_length_of :title, :minimum => 4, :maximum => 15
  validates_uniqueness_of :title, :case_sensitive => false, :scope => :recipe


  def self.search(search)
    if search
      Recipe.joins(:recipe_tags).where('lower(recipe_tags.title) LIKE ?', "%#{search.downcase}%").limit(50)
    else
      Recipe.limit(50)
    end
  end
end