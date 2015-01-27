-------------------------------------------------
Developing for Jaspersoft Studio in Eclipse 4.3.x
-------------------------------------------------
This folder is meant to contain all the necessary stuff to prepare an up-and-running
development environment where to build Jaspersoft Studio source code.
Here is a set of basic step you can take to have a fully working IDE.

1) 	Download an Eclipse Kepler (4.3.x) for RCP and RAP developers
2)	Install SVN plugin Subclipse version 1.8.x (http://subclipse.tigris.org/): choose by yourself to use SVNKit (Pure Java) or JavaHL (JNI).
3) 	Use the eclipse422.p2f file to have all the required features installed.
	You can do it by using the menu File->Import->Install->Install Software Items from File.
	Select the "eclipse43x.p2f" file and follow the steps.
4)	Next step after the restart is to import the needed SVN projects.
	To do this just select the menu File->Import->Team->Team Project Set and use the
	specified "jaspersoftStudioCE-plugin.psf" file.
5)	After import, just build and start coding!