class CreateRecipeComments < ActiveRecord::Migration
  def change
    create_table :recipe_comments do |t|
      t.integer :recipe_id
      t.integer :user_id
      t.string :message, null: false

      t.timestamps
    end
  end
end