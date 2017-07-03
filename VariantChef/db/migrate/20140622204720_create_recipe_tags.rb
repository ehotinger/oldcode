class CreateRecipeTags < ActiveRecord::Migration
  def change
    create_table :recipe_tags do |t|

      t.integer :recipe_id, null: false
      t.string :title, null: false

      t.timestamps
    end

    # A recipe can only have one 
    add_index :recipe_tags, [:recipe_id, :title], unique: true
  end
end
