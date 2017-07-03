class CreateRecipes < ActiveRecord::Migration
  def change
    create_table :recipes do |t|

      t.integer :user_id, null: false

      t.string :title, 	              null: false
      t.string :short_description,    null: false
      t.string :slug, 	              null: false, unique: true
      t.timestamps
    end

    # A user can only have one recipe with a specific title.
    add_index :recipes, [:user_id, :slug], unique: true
  end
end
