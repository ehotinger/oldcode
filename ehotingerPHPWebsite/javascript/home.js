var request;
var currentPageNum;
var currentTag;
var posts;
var project_ids = [];
var activeBtns;
var inactiveBtns;

// When the document is initially loaded, mark the current page number.
document.observe("dom:loaded", function() {
  markPageNum(currentPageNum);
});

/**
 * Submit our posts form.
 */
function submitForm()
{
  var form = $('posts');
  form.submit();
}

/**
 * Mark the filter as having a certain value.
 */
function markForm(val)
  {
  var filter = $('filter');
  filter.value = val;
  }

/**
 * Mark the page number = to the given parameter.
 */
function markPageNum(num)
  {
  currentPageNum = num;
  project_ids = window["page_" + currentPageNum];
  runAjax();
  
  activeBtns = $$('.pageButtonGreyed');
  inactiveBtns = $$('.pageButton'); 
  
  activeBtns.each(function(element){
    return $(element).observe('click', function(){
      
      if(element.classNames('pageButtonGreyed'))
        {
        activeBtns.each(function(element){
        element.className = 'pageButton'; 
        });
          
        element.className = 'pageButtonGreyed';
        } 
    });
  });
  
  inactiveBtns.each(function(element){
    return $(element).observe('click', function(){
      
      if(element.classNames('pageButton'))
        {
        activeBtns.each(function(element){
        element.className = 'pageButton'; 
        });
          
        element.className = 'pageButtonGreyed';
        } 
    });
  });
  }

/**
 * Run our custom AJAX.
 */
function runAjax()
{
  new Ajax.HOT({
          method: 'get',
          parameters: {
            requestId:  'Posts',
            projectIds: JSON.stringify(project_ids)
          },
          onSuccess: function(response){
           $('posters').innerHTML = (response).responseText;
          },
          timeout: 3000 // 3 seconds
    });
}