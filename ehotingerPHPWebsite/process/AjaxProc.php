<?php
require_once "../header.php";

$fnName = "Ajax_" . preg_replace("/[^a-zA-Z0-9_]/", '_', $_REQUEST['requestId']);

if (is_callable($fnName)) {
  header('Ajax-Complete: yes');
  echo $fnName($_REQUEST);
  }
  
/**
 * Called whenever different tags or page numbers are clicked on
 * during the filtering process.  Recreates the posts.
 * 
 * @param array $projectIds
 *                         an array of json encoded project ids
 */
function Ajax_Posts($projectIds)
  {
  $matches = array();
  $json = $projectIds['projectIds'];
  preg_match_all('!\d+!', $json, $matches);
    
  foreach($matches as $val)
    {
    foreach($val as $post)
      {
      echo(CreatePostForProject($post));      
      }
    }
  }

?>
