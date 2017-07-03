class CreateGroups < ActiveRecord::Migration
  def change
    create_table :groups do |t|
      t.string :title,				null: false
      t.string :description,		null: false
      t.string :slug,				null: false

      t.timestamps
    end

    add_index :groups, :slug, unique: true
  end
end
