<?php
/**
 * Returns a list of all project ids based on an associated type, or returns
 * all project ids if 'all' is selected.  Orders by date descending (most recent first).
 * 
 * $tagId = 0 => all posts
 * 
 * @param int $tagId
 *                    a tag ID for a post
 * @return array
 *                    an array of project ids based on an associated type
 */
function ObtainAllPostsByTypeAndGroup($tagId = 0)
  {
  $projIds = array();
  
  if ($tagId == 0)
    $sql =         'SELECT PROJECT_ID ' .
                   '  FROM PROJECTS ' .
                   ' GROUP BY PROJECT_ID ' .
                   ' ORDER BY max(PROJECT_TIMESTAMP) DESC ';
  else
    $sql =         'SELECT p.PROJECT_ID ' .
                   '  FROM PROJECT_TAGS pt, PROJECTS p ' .
                   ' WHERE pt.TAG_ID = ' . $tagId .
                   '   AND pt.PROJECT_ID = p.PROJECT_ID ' .
                   ' GROUP BY p.PROJECT_ID ' .
                   ' ORDER BY max(p.PROJECT_TIMESTAMP) DESC ';

  $result = QueryDB($sql);
  
  while($result != null && $row = $result->fetch_array(MYSQLI_ASSOC))
    $projIds[] = $row['PROJECT_ID'];
  
  return array_chunk($projIds, 5);
  }

/**
 * Returns a list of all the tags in the database.
 * 
 * @return array
 *                an array of all of the tags in the database.
 */
function GetAllTags($activeTag = 'All')
  {
  $ret = array();
  $sql = 'SELECT TAG_DESC, TAG_ID ' .
         '  FROM TAGS ' .
         ' WHERE IS_ACTIVE = 1 ' .
         'ORDER BY TAG_DESC ASC';

  $result = QueryDB($sql);
  
  while($result != null && $row = $result->fetch_array(MYSQLI_ASSOC))
    {
    if ($activeTag == $row['TAG_ID']) 
      $ret[] = 
                '<span class = "tag activeFilter" ' . 
                '      title = "Click to filter posts by ' . $row['TAG_DESC'] . '.' .
                '"   onclick = "markForm(' . $row['TAG_ID'] . '); submitForm();"' . '>' . $row['TAG_DESC'] .
                '</span>';
    
    else
      $ret[] = 
                '<span class = "tag inactiveFilter" ' . 
                '      title = "Click to filter posts by ' . $row['TAG_DESC'] . '.' .
                '"   onclick = "markForm(' . $row['TAG_ID'] . '); submitForm();"' . '>' . $row['TAG_DESC'] .
                '</span>';
    }
  
  return $ret;
  }
  
/**
 * Returns a list of all the Project IDs which are associated
 * with a specified tag id.  If the tag id is 'All' it returns all of the
 * project IDs in the database.
 * 
 * @param int $tagId
 *                   a tag id
 * @return array
 *                   an array of project ids based on tag
 */
function GetProjectsBasedOnTag($tagId = 'All')
  {
  $ret = array();
  $sql = '';
  
  if($tagId == 'All')
    $sql = 'SELECT PROJECT_ID ' .
           '  FROM PROJECTS ';
  else
    $sql = 'SELECT PROJECT_ID ' .
           '  FROM PROJECT_TAGS ' .
           ' WHERE TAG_ID = ' . $tagId;
  
  $result = QueryDB($sql);
  
  while($result != null && $row = $result->fetch_array(MYSQLI_ASSOC))
    $ret[] = $row['PROJECT_ID'];
  
  return $ret;
  }

/**
 * Returns a list of project information based on a given project ID.
 * 
 * @param int $projId
 *                    a project ID
 * @return array
 *                    an array of project information based on the given
 *                    project id
 */
function GetProjectInfoBasedOnId($projId)
  {
  $ret = '';
  
  $sql = 'SELECT * ' .
         '  FROM PROJECTS ' .
         ' WHERE PROJECT_ID = ' . $projId;
  
  $result = QueryDB($sql);
  
  while($result != null && $row = $result->fetch_array(MYSQLI_ASSOC))
    {
    $info = array();
    $info['name'] = $row['PROJECT_NAME'];
    $info['desc'] = $row['PROJECT_DESC'];
    $info['id'] = $row['PROJECT_ID'];
    $info['timestamp'] = $row['PROJECT_TIMESTAMP'];
    $ret = $info;
    }
  
  return $ret;
  }
  
/**
 * Returns a list of all the tags associated with a given project id.
 * 
 * @param int $projId
 *                    a project id
 */
function GetTagsForProject($projId)
  {
  $ret = array();
  $sql = 'SELECT t.TAG_DESC, t.TAG_ID ' .
         '  FROM PROJECT_TAGS pt, TAGS t ' .
         ' WHERE pt.TAG_ID = t.TAG_ID ' .
         '   AND pt.PROJECT_ID = ' . $projId . 
         ' ORDER BY t.TAG_DESC ASC ';

  $result = QueryDB($sql);
  
  while($result != null && $row = $result->fetch_array(MYSQLI_ASSOC))
    {
    $ret[] = '<span class = "tag" ' . 
                '      title = "Click to filter posts by ' . $row['TAG_DESC'] . '.' .
                '"   onclick = "markForm(' . $row['TAG_ID'] . '); submitForm();"' . '>' . $row['TAG_DESC'] .
                '</span>';
    }
  
  return $ret;
  }

?>
