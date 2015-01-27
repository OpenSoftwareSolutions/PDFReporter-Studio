@rem ***************************************************************************
@rem  Jaspersoft Open Studio - Eclipse-based JasperReports Designer.
@rem  Copyright (C) 2005, 2010 Jaspersoft Corporation. All rights reserved.
@rem  http://www.jaspersoft.com
@rem  
@rem  Unless you have purchased a commercial license agreement from Jaspersoft,
@rem  the following license terms apply:
@rem  
@rem  This program is part of iReport.
@rem  
@rem  iReport is free software: you can redistribute it and/or modify
@rem  it under the terms of the GNU Affero General Public License as published by
@rem  the Free Software Foundation, either version 3 of the License, or
@rem  (at your option) any later version.
@rem  
@rem  iReport is distributed in the hope that it will be useful,
@rem  but WITHOUT ANY WARRANTY; without even the implied warranty of
@rem  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
@rem  GNU Affero General Public License for more details.
@rem  
@rem  You should have received a copy of the GNU Affero General Public License
@rem  along with iReport. If not, see <http://www.gnu.org/licenses/>.
@rem ***************************************************************************
@echo off
c:
cd C:\JasperSoft\workspace\jasperstudio\jni
rem set JAVA_HOME=e:\java\jdk
set "JAVA_HOME=C:\Program Files\Java\jdk1.6.0_12"
"%JAVA_HOME%\bin\javah" -jni -force -classpath ..\build\classes com.jaspersoft.studio.editor.java2d.Win32ImageRenderer
c:\Mingw\bin\gcc.exe "-I%JAVA_HOME%/include" "-I%JAVA_HOME%/include/win32" -shared Win32ImageRenderer.c -Wl,--add-stdcall-alias -o ../J2DWin32ImageRenderer.dll -lgdi32
pause