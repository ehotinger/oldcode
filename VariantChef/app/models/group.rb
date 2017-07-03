class Group < ActiveRecord::Base
  extend FriendlyId

  friendly_id :title, use: :slugged

  has_many :memberships, dependent: :destroy
  has_many :users, through: :memberships, source: :user

  validates_uniqueness_of :slug

  validates_length_of :title, :minimum => 5, :maximum => 25, :allow_blank => false
  validates_length_of :description, :minimum => 5, :maximum => 200, :allow_blank => false

  validates_associated :memberships

  def admin
  	User.joins("INNER JOIN memberships a ON users.id = a.user_id")
  	.where("a.is_admin = true")
  	.where("a.group_id = ?", self.id).first
  end

  def non_admin_users
  	User.joins("INNER JOIN memberships a ON users.id = a.user_id")
  	.where("a.is_admin = false")
  	.where("a.group_id =?", self.id).all
  end
end
