package com.jaspersoft.studio.components.table;

import net.sf.jasperreports.engine.design.JRDesignDatasetRun;

import org.eclipse.swt.widgets.Composite;

import com.jaspersoft.studio.JSSCompoundCommand;
import com.jaspersoft.studio.components.table.model.MTable;
import com.jaspersoft.studio.components.table.model.column.command.RefreshColumnNamesCommand;
import com.jaspersoft.studio.components.table.model.table.command.SwitchDatasetCommand;
import com.jaspersoft.studio.model.dataset.descriptor.DatasetRunPropertyDescriptor;
import com.jaspersoft.studio.model.dataset.descriptor.SPDatasetRun;
import com.jaspersoft.studio.property.section.AbstractSection;
import com.jaspersoft.studio.property.section.widgets.ASPropertyWidget;

public class TableDatasetRunProperyDescriptor extends DatasetRunPropertyDescriptor {

	public TableDatasetRunProperyDescriptor(Object id, String displayName, boolean alldataset)  {
		super(id, displayName, alldataset);
	}

	@Override
	public ASPropertyWidget createWidget(Composite parent, AbstractSection section) {
		return new SPDatasetRun(parent, section, this, alldataset){


			@Override
			protected void changeProperty(AbstractSection section,  Object property, Object prop, Object value) {
				if (prop.equals(JRDesignDatasetRun.PROPERTY_DATASET_NAME)) {
					MTable table = (MTable)section.getElement();
					JSSCompoundCommand changeCommands = new JSSCompoundCommand(table);
					changeCommands.add(new RefreshColumnNamesCommand(table, false, true));
					changeCommands.add(new SwitchDatasetCommand(table,  (String) value));
					changeCommands.add(new RefreshColumnNamesCommand(table, true, false));
					section.getEditDomain().getCommandStack().execute(changeCommands);
				} else super.changeProperty(section, property, prop, value);
			}
			
		};
	}
	
}
