-------------------------------------------------
Developing for Jaspersoft Studio in Eclipse 3.8.2
-------------------------------------------------
This folder is meant to contain all the necessary stuff to prepare an up-and-running
development environment where to build Jaspersoft Studio source code.
Here is a set of basic step you can take to have a fully working IDE.

1) 	Download an Eclipse Juno SR2 SDK (3.8.2) from http://archive.eclipse.org/eclipse/downloads/drops/R-3.8.2-201301310800/
2)	Install SVN plugin Subclipse version 1.8.x (http://subclipse.tigris.org/): choose by yourself to use SVNKit (Pure Java) or JavaHL (JNI).
3) 	Use the eclipse382.p2f file to have all the required features installed.
	You can do it by using the menu File->Import->Install->Install Software Items from File.
	Select the "eclipse382.p2f" file and follow the steps.
	N.B: You should also import the deltapack for 3.8.2 in you current platform in order to be able to build the JSS product.
4)	Next step after the restart is to import the needed SVN projects.
	To do this just select the menu File->Import->Team->Team Project Set and use the
	specified "jaspersoftStudioCE-rcp.psf" or "jaspersoftStudioCE-plugin.psf" file.
5)	After import, just build and start coding!