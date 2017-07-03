class CreateRecipeAssets < ActiveRecord::Migration
  def change
    create_table :recipe_assets do |t|
      t.integer :recipe_id
      t.timestamps
    end

    add_attachment :recipe_assets, :picture
  end
end