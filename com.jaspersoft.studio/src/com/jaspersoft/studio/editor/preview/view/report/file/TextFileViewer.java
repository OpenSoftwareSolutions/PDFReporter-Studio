package com.jaspersoft.studio.editor.preview.view.report.file;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import net.sf.jasperreports.eclipse.ui.util.UIUtils;

import org.apache.commons.io.IOUtils;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.editor.preview.view.APreview;
import com.jaspersoft.studio.editor.preview.view.report.IURLViewable;
import com.jaspersoft.studio.editor.preview.view.report.html.URLContributionItem;
import com.jaspersoft.studio.utils.Misc;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

public class TextFileViewer extends APreview implements IURLViewable {
	protected StyledText browser;

	public TextFileViewer(Composite parent, JasperReportsConfiguration jContext) {
		super(parent, jContext);
	}

	@Override
	protected Control createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		composite.setLayout(layout);
		try {
			browser = new StyledText(composite, SWT.WRAP | SWT.READ_ONLY);
			browser.setLayoutData(new GridData(GridData.FILL_BOTH));
		} catch (Error e) {
			e.printStackTrace();
		}
		return composite;
	}

	@Override
	public void contribute2ToolBar(IToolBarManager tmanager) {
		super.contribute2ToolBar(tmanager);
		urlBar = new URLContributionItem(Misc.nvl(url, " "));
		tmanager.add(urlBar);
		tmanager.add(new Action("", JaspersoftStudioPlugin.getInstance().getImageDescriptor(
				JaspersoftStudioPlugin.ICONS_RESOURCES_REFRESH_16_PNG)) {
			@Override
			public void run() {
				try {
					showData(TextFileViewer.this.url);
				} catch (MalformedURLException e) {
					UIUtils.showError(e);
				} catch (IOException e) {
					UIUtils.showError(e);
				}
			}
		});
	}

	protected String url;
	private URLContributionItem urlBar;

	public void setURL(String url, String urlcookie, String scookie) throws Exception {
		this.url = Misc.nvl(url);
		if (urlBar != null)
			urlBar.setUrl(url);
		if (browser != null)
			showData(url);
	}

	protected void showData(String url) throws IOException, MalformedURLException {
		InputStream in = new URL(url).openStream();
		try {
			browser.setText(IOUtils.toString(in));
		} finally {
			IOUtils.closeQuietly(in);
		}
	}

	public void setURL(String url) throws Exception {
		setURL(url, null, null);
	}
}
