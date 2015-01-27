package org.eclipse.babel.core.message.resource.ser;

public class DefaultPropertiesSerializerConfig implements IPropertiesSerializerConfig {

	@Override
	public boolean isUnicodeEscapeEnabled() {
		return true;
	}

	@Override
	public int getNewLineStyle() {
		return 0;
	}

	@Override
	public int getGroupSepBlankLineCount() {
		return 1;
	}

	@Override
	public boolean isShowSupportEnabled() {
		return true;
	}

	@Override
	public boolean isGroupKeysEnabled() {
		return true;
	}

	@Override
	public boolean isUnicodeEscapeUppercase() {
		return true;
	}

	@Override
	public int getWrapLineLength() {
		return 80;
	}

	@Override
	public boolean isWrapLinesEnabled() {
		return false;
	}

	@Override
	public boolean isWrapAlignEqualsEnabled() {
		return false;
	}

	@Override
	public int getWrapIndentLength() {
		return 80;
	}

	@Override
	public boolean isSpacesAroundEqualsEnabled() {
		return false;
	}

	@Override
	public boolean isNewLineNice() {
		return false;
	}

	@Override
	public int getGroupLevelDepth() {
		return 1;
	}

	@Override
	public String getGroupLevelSeparator() {
		return ".";
	}

	@Override
	public boolean isAlignEqualsEnabled() {
		return true;
	}

	@Override
	public boolean isGroupAlignEqualsEnabled() {
		return true;
	}

	@Override
	public boolean isKeySortingEnabled() {
		return true;
	}

}
