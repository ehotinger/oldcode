 /**
  * Some resizing for the logo on the top of each page.
  */
 jQuery.noConflict(); // Avoid Prototype $ issues
 
 // Resizing for Logo
 jQuery(window).resize(function() {
  if (jQuery(window).width() < 675) {
    jQuery(".center").attr("width", "62");
    jQuery("#introduction").attr("style", "display: none;");
    jQuery(".center").attr("height", "61");
    jQuery(".center").attr("src", "/images/hot_logo_small.png");
  }
 else {
    jQuery(".center").attr("width", "241");
    jQuery(".center").attr("height", "66");
    jQuery("#introduction").attr("style", "display: block;");
    jQuery(".center").attr("src", "/images/hot_logo.png");
 }
 
 // Resizing for About page
 if(jQuery(window).width() < 800){
       jQuery("#rightInfo").attr("style", "display: none;");
       jQuery("#leftInfo").width("100%");
 }
 else{
    jQuery("#rightInfo").attr("style", "display: block;");
    jQuery("#leftInfo").width("40%");
 }
});