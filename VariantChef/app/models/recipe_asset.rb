class RecipeAsset < ActiveRecord::Base
  belongs_to :recipe

  has_attached_file :picture,
    :storage => :s3,
    :s3_credentials => {:bucket => ENV['S3_BUCKET_NAME'],
                        :access_key_id => ENV['AWS_ACCESS_KEY'],
                        :secret_access_key => ENV['AWS_SECRET_ACCESS_KEY']
                        },
    #:s3_permissions => :private,
  :styles => {
    :original => ['1920x1680>', :jpg],
    :small    => ['200x200#',   :jpg],
    :medium   => ['500x500',    :jpg],
    :large    => ['900x900>',   :jpg]
  },
    :convert_options => { :all => '-background white -flatten +matte' },
    #:s3_protocol => 'https',
    #:s3_host_name => 's3-us-east-1.amazonaws.com',
    :path => ':attachment/:id/:style.:extension'

  validates :picture, presence: true, allow_blank: false
  validates_attachment_content_type :picture, :content_type => /\Aimage\/.*\Z/
end