# encoding: UTF-8
# This file is auto-generated from the current state of the database. Instead
# of editing this file, please use the migrations feature of Active Record to
# incrementally modify your database, and then regenerate this schema definition.
#
# Note that this schema.rb definition is the authoritative source for your
# database schema. If you need to create the application database on another
# system, you should be using db:schema:load, not running all the migrations
# from scratch. The latter is a flawed and unsustainable approach (the more migrations
# you'll amass, the slower it'll run and the greater likelihood for issues).
#
# It's strongly recommended that you check this file into your version control system.

ActiveRecord::Schema.define(version: 20140707123417) do

  create_table "favorite_recipes", force: true do |t|
    t.integer  "recipe_id"
    t.integer  "user_id"
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  add_index "favorite_recipes", ["recipe_id", "user_id"], name: "index_favorite_recipes_on_recipe_id_and_user_id", unique: true, using: :btree
  add_index "favorite_recipes", ["recipe_id"], name: "index_favorite_recipes_on_recipe_id", using: :btree
  add_index "favorite_recipes", ["user_id"], name: "index_favorite_recipes_on_user_id", using: :btree

  create_table "groups", force: true do |t|
    t.string   "title",       null: false
    t.string   "description", null: false
    t.string   "slug",        null: false
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  add_index "groups", ["slug"], name: "index_groups_on_slug", unique: true, using: :btree

  create_table "memberships", force: true do |t|
    t.integer  "group_id",                   null: false
    t.integer  "user_id",                    null: false
    t.boolean  "is_admin",   default: false, null: false
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  add_index "memberships", ["group_id", "user_id"], name: "index_memberships_on_group_id_and_user_id", unique: true, using: :btree

  create_table "recipe_assets", force: true do |t|
    t.integer  "recipe_id"
    t.datetime "created_at"
    t.datetime "updated_at"
    t.string   "picture_file_name"
    t.string   "picture_content_type"
    t.integer  "picture_file_size"
    t.datetime "picture_updated_at"
  end

  create_table "recipe_comments", force: true do |t|
    t.integer  "recipe_id"
    t.integer  "user_id"
    t.string   "message",    null: false
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  create_table "recipe_tags", force: true do |t|
    t.integer  "recipe_id",  null: false
    t.string   "title",      null: false
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  add_index "recipe_tags", ["recipe_id", "title"], name: "index_recipe_tags_on_recipe_id_and_title", unique: true, using: :btree

  create_table "recipes", force: true do |t|
    t.integer  "user_id",           null: false
    t.string   "title",             null: false
    t.string   "short_description", null: false
    t.string   "slug",              null: false
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  add_index "recipes", ["user_id", "slug"], name: "index_recipes_on_user_id_and_slug", unique: true, using: :btree

  create_table "relationships", force: true do |t|
    t.integer  "follower_id"
    t.integer  "followed_id"
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  add_index "relationships", ["followed_id"], name: "index_relationships_on_followed_id", using: :btree
  add_index "relationships", ["follower_id", "followed_id"], name: "index_relationships_on_follower_id_and_followed_id", unique: true, using: :btree
  add_index "relationships", ["follower_id"], name: "index_relationships_on_follower_id", using: :btree

  create_table "users", force: true do |t|
    t.string   "user_name",                           null: false
    t.string   "first_name",                          null: false
    t.string   "last_name",                           null: false
    t.string   "slug",                                null: false
    t.string   "email",                  default: "", null: false
    t.string   "encrypted_password",     default: "", null: false
    t.string   "reset_password_token"
    t.datetime "reset_password_sent_at"
    t.datetime "remember_created_at"
    t.integer  "sign_in_count",          default: 0,  null: false
    t.datetime "current_sign_in_at"
    t.datetime "last_sign_in_at"
    t.string   "current_sign_in_ip"
    t.string   "last_sign_in_ip"
    t.datetime "created_at"
    t.datetime "updated_at"
    t.string   "avatar_file_name"
    t.string   "avatar_content_type"
    t.integer  "avatar_file_size"
    t.datetime "avatar_updated_at"
  end

  add_index "users", ["email"], name: "index_users_on_email", unique: true, using: :btree
  add_index "users", ["reset_password_token"], name: "index_users_on_reset_password_token", unique: true, using: :btree
  add_index "users", ["slug"], name: "index_users_on_slug", unique: true, using: :btree
  add_index "users", ["user_name"], name: "index_users_on_user_name", unique: true, using: :btree

end
