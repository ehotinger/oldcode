require 'spec_helper'

describe "Static Page Tests" do
  let(:base_title) { "VariantChef" }

  describe "Home Page" do
    before { visit root_path }

    it "Should have the brand on the home page" do
      expect(page).to have_title("#{base_title}")
    end

    it "Should not have a custom title" do
      expect(page).not_to have_title("| Home")
    end
  end

  describe "About Page" do
    it "Should have About as a piece of content" do
      visit about_path
      expect(page).to have_title("#{base_title} - About")
    end
  end

  describe "Home Page" do
    it "Should have Help as a piece of content" do
      visit help_path
      expect(page).to have_title("#{base_title} - Help")
    end
  end
end