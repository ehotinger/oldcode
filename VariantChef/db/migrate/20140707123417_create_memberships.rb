class CreateMemberships < ActiveRecord::Migration
  def change
    create_table :memberships do |t|

      t.integer :group_id, 					null: false
      t.integer :user_id, 					null: false
      t.boolean :is_admin, default: false,	null: false

      t.timestamps
    end

    add_index :memberships, [:group_id, :user_id], unique: true
  end
end
