<?php
//--------------------------------------------------------------------
//                      BEGIN STANDARD HEADER
// ------------------------------------------------------------------>
require_once "../header.php";
require_once "../footer.php";
echo(GenerateHeader("About", 1, 'about'));
?>

<?php
// -------------------------------------------------------------------
//                      BEGIN UNIQUE PAGE CONTENT
// ------------------------------------------------------------------>
?>
<div id = "about">
    <h2>About me</h2>
        <ul>
            <div id ="leftInfo">
            <li>My name is Eric Hotinger; I'm a 20 year old aspiring software developer from the United States.</li><br/>
            <li>I grew up in a small town called Fairfield in the heart of Virginia and found myself at Virginia Tech in 2010.</li><br/>
            <li>Since then, I've learned a lot; some of the lessons might even be reflected in the code chunks and blog excerpts you can find on this site.</li><br/>
            <li>I'll be graduating in May of 2013 and although I'm not sure what the future holds for me yet, I'm excited.</li><br/>
            <li>If you're interested in learning more about me, feel free to get in touch with me or check out my <a href = "../blog/">blog</a>.</li><br/>
            <li>I've also started posting on <a href = "http://stackoverflow.com/users/1959230/eric-hotinger">Stack Overflow</a> recently, so perhaps you'll find some of my future comments insightful!</li>
            </div>
        </ul>
    
    <div id = "rightInfo">
        <img src = "../images/photo.jpg"/>
    </div>
</div>
        
<?php         
// --------------------------------------------------------------------
//                      BEGIN STANDARD FOOTER
// ------------------------------------------------------------------>
echo (GenerateFooter());
?>