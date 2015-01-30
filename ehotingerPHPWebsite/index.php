<?php
//--------------------------------------------------------------------
//                      BEGIN STANDARD HEADER
// ------------------------------------------------------------------>
require_once "header.php";
require_once "footer.php";

echo(GenerateHeader("Software Developer", 0, 'home'));
?>

<?php
// -------------------------------------------------------------------
//                      BEGIN UNIQUE PAGE CONTENT
// ------------------------------------------------------------------>

//    FILTERS
echo('<form action = "" id = "posts" method = "post">');

$currentTag = 0; // current tag is ALL by default

if (isset($_REQUEST['currentTag']))
  $currentTag = $_REQUEST['currentTag'];

echo GenerateFilters($currentTag);

echo ('<input id = "filter" name = "currentTag" type = "hidden" value = "' . $currentTag . '"/>');

echo('<script type="text/javascript">var currentTag = ' . $currentTag . '</script>');
echo('<script type = "text/javascript"> var currentPageNum = 1 </script>');
$numPages = 0;

$groupedPosts = ObtainAllPostsByTypeAndGroup($currentTag);

$currentPageNum = 1;

echo('<div id = "scripts">');
foreach($groupedPosts as $group){
  $numPages++;
  echo('<script type = "text/javascript">var page_' . $numPages . '=' . json_encode($group) . '</script>');
  }

echo ('</div>');

echo('<div id = "posters"></div>');
  
for($i = 1; $i < $numPages + 1; $i++){
  $html = CreateButton($i, false);
  
  if($i == $currentPageNum) 
      $html = CreateButton($i, true);
      
 echo($html); 
}

?>

<?php         
// --------------------------------------------------------------------
//                      BEGIN STANDARD FOOTER
// ------------------------------------------------------------------>
echo('</form>');
echo (GenerateFooter());
?>
<?php echo '<script src="javascript/prototype.js?v=' . VERSION . '"></script>' ?>
<?php echo '<script src="javascript/prototypeHot.js?v=' . VERSION . '"></script>' ?>
<?php echo '<script src="javascript/home.js?v=' . VERSION . '"></script>' ?>