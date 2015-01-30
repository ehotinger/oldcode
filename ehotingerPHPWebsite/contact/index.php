<?php
//--------------------------------------------------------------------
//                      BEGIN STANDARD HEADER
// ------------------------------------------------------------------>
require_once "../header.php";
require_once "../footer.php";
echo(GenerateHeader("Contact", 1, 'contact'));
?>

<?php
// -------------------------------------------------------------------
//                      BEGIN UNIQUE PAGE CONTENT
// ------------------------------------------------------------------>
?>
<div id = "contact">
    <h2>Want to get in touch?</h2>
    <ul>
        <li>Send me an e-mail: <a href="mailto:erichotinger@ehotinger.com">erichotinger@ehotinger.com</a></li><br/>
        <li>Add me on Facebook: <a href="http://www.facebook.com/lREMl">http://www.facebook.com/lREMl</a></li><br/>
        <li>Follow me on Twitter: <a href="https://twitter.com/ehotinger">https://twitter.com/ehotinger</a></li>
    </ul>
</div>
        
<?php         
// --------------------------------------------------------------------
//                      BEGIN STANDARD FOOTER
// ------------------------------------------------------------------>
echo (GenerateFooter());
?>