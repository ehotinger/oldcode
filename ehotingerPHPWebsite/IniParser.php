<?php
/**
 * Simple Ini parser for a DB.
 */
class IniParser{
  
  /**
   * An array of settings.
   * 
   * @var array
   *            an array of settings
   */
  private $settings;
  
  /**
   * Parse the default file specified in this file when the object is created.
   */
  function __construct()
    {
    $file = 'db_settings9036.ini';
    $this->settings = parse_ini_file($file, true);
    }
  
  /**
   * Returns the settings of the IniParser.
   * 
   * @return array
   *               the settings of the IniParser
   */
  function getSettings()
    {
    return $this->settings;
    }
}
?>
