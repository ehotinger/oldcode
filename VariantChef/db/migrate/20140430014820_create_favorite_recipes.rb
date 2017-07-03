class CreateFavoriteRecipes < ActiveRecord::Migration
  def change
    create_table :favorite_recipes do |t|
      t.integer :recipe_id
      t.integer :user_id

      t.timestamps
    end

    add_index :favorite_recipes, :recipe_id
    add_index :favorite_recipes, :user_id
    add_index :favorite_recipes, [:recipe_id, :user_id], unique: true
  end
end
