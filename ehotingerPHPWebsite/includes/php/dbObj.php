<?php

/**
 * A crude, but simple way to query the DB.  Returns the resource obtained from
 * the given MySQL statement.
 * 
 * @param string $sql
 *                    a string of sql
 * 
 * @return the resource obtained from the given MySQL statement
 */
function QueryDB($sql)
  {
  $iniParser = new IniParser();
  $settings = $iniParser->getSettings();

  $schema = $settings['database']['schema'];
  $host = $settings['database']['host'];
  $password = $settings['database']['password'];
  $username = $settings['database']['username'];
  
  $link = mysqli_connect($host, $username, $password, $schema);
  $sql = mysqli_real_escape_string($link, $sql);
  $result = mysqli_query($link, $sql);
  return $result;
  }
?>
