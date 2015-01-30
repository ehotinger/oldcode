<?php

/**
 * Creates HTML that represents a "button" given a page number to put on the button
 * and whether or not the button should be "grey," meaning that it's active.
 * 
 * @param int $pageNum
 *                        the page number that will appear on the button
 * 
 * @param boolean $greyed
 *                        true or false, if the button should be greyed out
 * 
 * @return string
 *                        HTML representing a button based on a page number
 */
function CreateButton($pageNum = 1, $greyed = false)
  {
  $html = '';
  
  ($greyed) 
      ? $html .= '<span class = "pageButtonGreyed" ' . 
                 '      title = "Go to page ' . $pageNum .
                 '"   onclick = "markPageNum(' . $pageNum . '); return false;">' . $pageNum .
                 '</span>'
          
      : $html .= '<span class = "pageButton" ' . 
                 '      title = "Go to page ' . $pageNum . 
                 '"   onclick = "markPageNum(' . $pageNum . '); return false;">' . $pageNum .
                 '</span>';
  
  return $html;
  }

/**
 * Generates an HTML post based on a given project id.
 * 
 * @param int $projId
 *                    a project ID
 * @return string
 *                    an HTML post
 */
function CreatePostForProject($projId)
  {
  $html = '';
  
  $info = GetProjectInfoBasedOnId($projId);
  $tags = GetTagsForProject($projId);
  $tagsImploded = implode($tags, ', ');
  
  if($info != array())
    {
    $html .= '<div id = "post" name = "' . $info['name'] . '">' . 
               '<span class = "name">' . $info['name'] . '</span>' .
               '<span class = "tags"><p>' . $tagsImploded . ' | ' . date('M j Y g:i A', strtotime($info['timestamp']))
               . '</p></span>' .
               '<span class = "desc"><p>' . $info['desc'] . '</p></span>' .
             '</div>';
    }
  
  return $html;
  }
  
/**
 * Generates a list of all of the active tags in the DB and encases them in a div.
 * 
 * @return string
 *                a div of all the active tags in the DB
 */
function GenerateFilters($currentTag)
  {
  return '<div id = "filters">' . implode(GetAllTags($currentTag), '') . '</div>';
  }

?>
