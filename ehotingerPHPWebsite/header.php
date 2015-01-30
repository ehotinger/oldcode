<?php
require_once 'includes/php/dbObj.php';
require_once 'includes/php/DefinedConstants.php';
require_once 'includes/php/ProjectDB.php';
require_once 'includes/php/HTMLGeneration.php';
require_once 'IniParser.php';



/**
 * Generates a simple header HTML to be used at the top of every single page.
 * 
 * The header includes the window title ($winTitle) and uses an offset ($offset)
 * to determine the path to the css file.
 * 
 * @param string $winTitle
 *                         The name of the Window
 * @param boolean $offset
 *                         True or false if there should be an offset
 *                         for the path.
 * @return string
 *                         HTML string
 */
function GenerateHeader($winTitle, $offset, $greyedButton = '')
    { 
    $path = "";
    
    for($i = 0; $i < $offset; $i++)
            $path .= "../";
        
    $fileName = $path . 'css/styles.css';
    
    $scripts = '<script src="' . $path . 'javascript/jquery-1.9.1.js?v=' . VERSION . '"></script>';
    $scripts .= '<script src="' . $path . 'javascript/resize.js?v=' . VERSION . '"></script>';
    
    
    $css = '<link rel="stylesheet" href="' . $path . 'css/styles.css?v=' . filectime($fileName) . '" type="text/css"/>';
    $css .= '<link rel="stylesheet" href="' . $path . 'css/skeleton.css?v=' . filectime($fileName) . '" type="text/css"/>';
    $css .= '<link rel="stylesheet" href="' . $path . 'css/layout.css?v=' . filectime($fileName) . '" type="text/css"/>';
    
    $header = 
                "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">
                 <html xmlns=\"http://www.w3.org/1999/xhtml\"> 
                 <head>
                    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1, maximum-scale=1\">
                    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">
                    <title>Eric Hotinger | " . $winTitle . "</title>" . $css .
                    "<link rel=\"icon\" 
                          type=\"image/png\" 
                          href=\"http://ehotinger.com/favicon.png\" />
                </head>
                <body>
                    <div class=\"header\"></div>
                    <div class=\"container\">
                      <div id=\"content\">
                          <div id = \"LogoHeader\"><a href = \"http://www.ehotinger.com/\"><img class = \"center\"
                               src = \"" . $path . "images/hot_logo.png\" width = \"241\" 
                               height = \"66\" alt = \"Eric Hotinger\" /></a></div>
                          <div id = \"introduction\">
                            <p>Hi, I'm Eric Hotinger!
                            <br />
                            Welcome to my personal website, where I occasionally post cool stuff.<br />
                            </p>
                          </div>
                          <hr />
                          <div id = \"navMenu\"> ";
            
                          ($greyedButton == 'home')
                                  ? $header .= "<a href = \"http://www.ehotinger.com\">
                                               <span id = \"simpleButtonGreyed\"
                                                     title = \"Home\">HOME</span></a> "
    
                                  : $header .= "<a href=\"http://www.ehotinger.com\">
                                        <span id=\"simpleButton\"
                                              title=\"Home\">HOME</span></a> ";
                          
                          ($greyedButton == 'about')
                                  ? $header .= "<a href = \"/about/\">
                                               <span id = \"simpleButtonGreyed\"
                                                     title = \"About\">ABOUT</span></a> "
                          
                                  : $header .= " <a href=\"/about/\">
                                                 <span id=\"simpleButton\"
                                                       title=\"About\">ABOUT</span></a> ";
                          
                          $header .= 
                           "<a href=\"/blog/\">
                                  <span id=\"simpleButton\"
                                        title=\"Blog\">BLOG</span></a> ";
                          
                          ($greyedButton == 'contact')
                                  ? $header .= "<a href = \"/contact/\">
                                               <span id = \"simpleButtonGreyed\"
                                                     title = \"Contact\">CONTACT</span></a> "
                          
                                  : $header .= " <a href=\"/contact/\">
                                                 <span id=\"simpleButton\"
                                                       title=\"Contact\">CONTACT</span></a> ";
                          
                          $header .= '</div>';
                          $header .= $scripts;
    return $header;
    }
?>
