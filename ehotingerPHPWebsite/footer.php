<?php

/**
 * Generates the standard footer of the website, to be used for every page.
 * 
 * @return string 
 *                A string of HTML that generates the footer
 */
function GenerateFooter()
    {
    $footer = 
                        "<div class=\"footer\"></div>
                            <div id=\"copyrightFooter\">Copyright &copy; 2013 | Eric Hotinger
                             <span class = \"fltrt\">
                                 <a href = \"http://www.facebook.com/lREMl\">facebook</a> | 
                                 <a href = \"https://twitter.com/ehotinger\">twitter</a> |
                                 <a href = \"http://www.ehotinger.com/contact/\">contact</a>
                             </span>
                         </div>
                         </div>
                     </div>
                 </body>
             </html>";

    return $footer;
    }
?>