<?php 
	// may contain some special sophisticated logic
	// eg for analytics purpose, slow rollout of new version
	// etc. At the moment it simply returns current version
	// If you want to inform users about new version
	// simply return anything that is ALPHABETICALLY
	// higher than current version (it is logic inherited
	// from Jasper Studio).

	// Script is uploaded into directory
	//     /home/project-web/pdfreporterstudio/htdocs/
	//
	// using command 
	//     sftp YOUR_SF_ACCOUNT_NAME@web.sourceforge.net

	header('Content-Type: text/plain');
	echo "5.6.2.final\n";
?>
