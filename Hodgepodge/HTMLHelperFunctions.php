<?php
/**
 * This is a list of public HTML helpers.
 *  
 * @author Eric Hotinger
 */
  
/**
 * Generates a checkbox HTML element.
 *
 * @param  $settings   
 *                  HTML attributes; can also specify label => TRUE if you'd like
 *                  a label wrapped around the checkbox (you can then also
 *                  specify label_attr and label_pos ('after' or 'before')).
 * 
 * @return
 *                  an HTML checkbox
 */
function generateCheckbox($attr = array())
  {
  $html = '';
  $defaultSettings = array(
                            'checked'     => FALSE,
                            'label'       => FALSE,
                            'label_attr'  => array(),
                            'label_pos'   => 'after',
                            'name'        => '',
                            'type'        => 'checkbox',
                            'value'       => '');
  
  $attr = array_merge($defaultSettings, $attr);
  
  if ($attr['checked'])
    {
    $attr['checked'] = 'checked';
    }
    
  else
    {
    unset($attr['checked']);
    }

  $label       = $attr['label'];
  $label_attr  = generateAttributes($attr['label_attr']);
  $label_pos   = $attr['label_pos'];

  // Non-attributes.
  unset($attr['label'], $attr['label_attr'], $attr['label_pos']);

  $html = '<input' . generateAttributes($attr) . ' />';
  
  if ($label)
    {
    $after  = $label_pos === 'after' ? $label : '';
    $before = $label_pos === 'before' ? $label : '';

    $html = "<label{$label_attr}>{$before}{$html}{$after}</label>";
    }

  return $html;
  }
  
/**
 * Generates an HTML anchor.
 * 
 * @param  $html  Anchor contents.
 * @param  $href  URL.
 * @param  $attr  Optional HTML attributes.
 */
function generateAnchor($html, $href, $attr = array())
  {
  $attr['href'] = ! $href ? 'javascript:void(0);' : $href;
  return '<a' . generateAttributes($attr) . ">{$html}</a>";
  }
    
/**
 * Generate HTML attributes from an associated array.
 * 
 * @param array $attr
 *                    a list of attributes
 * 
 * @return string
 *                    a string concatenation of HTML attributes
 */
function generateAttributes($attr = array())
    {
    $html = '';

    foreach ($attr as $k => $v)
      {
      $v = str_replace('&', '&amp;', $v);
      $html.= " {$k}=\"{$v}\"";
      }

    return $html;
    }

/**
 * Generate a number of columns from an array of values.
 *
 * @param  $lines     Array of HTML strings that will be split up into columns.
 * @param  $num_cols  The target number of columns.
 */
function generateColumns($lines, $num_cols = 1)
  {
  $num_rows = ceil(count($lines) / $num_cols);
  $cols = array_fill(0, $num_cols, array());

  $i = 0;

  for ($j = 0; $j < $num_cols; $j++)
    {
    $cols[$j] = array_slice($lines, $i, $num_rows);
    $i += $num_rows;
    }

  $html = '';

  foreach ($cols as $i => $col)
    {
    $attr = ' class="column'.($i === 0 ? ' first' : '').'"';
    $html.= "<ul{$attr}><li>".implode('</li><li>', $col).'</li></ul>';
    }

  return $html;
  }
  
/**
 * Creates a ShowHide widget given an id, title, and content.
 * 
 * @param string $id the id of the ShowHide box.
 * @param string $title the title of the ShowHide box.
 * @param array $content the content of the ShowHide box.
 * @param boolean $hide determines whether or not to initially hide the ShowHide widget.
 * @param array $attr additional attributes.
 * @return string the HTML ShowHide div. 
 */
function generateShowHide($id, $title, $content, $hide = TRUE, $attr = array())
  {
  $tmp = new ShowHideWidget($id, $title, $content);
  $hide && $tmp->Hide();

  if ( ! isset($attr['class']))
    {
    $attr['class'] = '';
    }

  $attr['class'] .= 'showhide';

  return '<div ' . generateAttributes($attr) .'>' . $tmp->Draw() . '</div>';
  }
  
/**
 * Transposes an array to make it readily available
 * for use in the Simple Table creation process.
 * 
 * @param array $array 
 *                    the array to flipped around. 
 * 
 * @return array
 *                    a transposed array
 */
function FlipDiagonally($array)
  {
  $output = array();
  foreach ($array as $key => $subArray)
    {
    foreach ($subArray as $subKey => $subValue)
      {
      $output[$subKey][$key] = $subValue;
      }
    }
  
  return $output;
  }
  
/**
 * Creates a basic HTML images from a given source and specified attributes.
 * 
 * @param string $src the source of the image
 * @param array $attr an array of attributes
 * @return string a string of HTML to be used in the creation of an image
 */
function GenerateBasicHTMLImage($src, $attr = array())
  {
  $attr['src'] = $src;
  
  return '<img' . generateAttributes($attr) . ' />';
  }
  
?>
