Ajax.HOT = Class.create(Ajax.Request, {
  initialize: function($super, arg1) {
    options = arg1;
    url = "../process/AjaxProc.php";
    
    var onSuccess  = options.onSuccess  || { };
    var onFailure  = options.onFailure  || { };
    var onComplete = options.onComplete || { };
        
    options.parameters.r = Math.floor(Math.random() * 101);
    
    options.onComplete = (function(response, param) {
      clearTimeout(this.timerId);
      if(response.getHeader('Ajax-Complete') == 'yes')
        if (Object.isFunction(onSuccess))  onSuccess(response, param);
      
      else 
        {
        if (Object.isFunction(onFailure))  
          onFailure(response, param);
        Ajax.Responders.dispatch('onTimeout', this, response, response.headerJSON);
        }
      if (Object.isFunction(onComplete))  
        onComplete(response, param);
    }).bind(this);
    
    options.parameters = options.parameters  || { };
    options.requestHeaders = options.requestHeaders || { };
    
    $super(url, options);
  }
});