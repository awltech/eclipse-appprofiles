package com.worldline.appprofiles.wizard.ui.project.pages;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import com.worldline.appprofiles.wizard.ui.ApplicationWizardPageStatus;
import com.worldline.appprofiles.wizard.ui.FormDataBuilder;
import com.worldline.appprofiles.wizard.ui.ValidatingApplicationWizardPage;
import com.worldline.appprofiles.wizard.ui.project.ApplicationWizardOutput;
import com.worldline.appprofiles.wizard.ui.project.pages.tools.ChoiceConfigCheckStateListener;
import com.worldline.appprofiles.wizard.ui.project.pages.tools.ChoiceConfigCheckStateProvider;
import com.worldline.appprofiles.wizard.ui.project.pages.tools.ChoiceConfigContentProvider;
import com.worldline.appprofiles.wizard.ui.project.pages.tools.ChoiceConfigLabelProvider;
import com.worldline.appprofiles.wizard.ui.project.pages.tools.TableResizeControlListener;
import com.worldline.appprofiles.wizard.ui.project.pages.tools.TableSelectionDisableListener;
import com.worldline.appprofiles.wizard.ui.project.pages.tools.ValueConfigCellEditor;
import com.worldline.appprofiles.wizard.ui.project.pages.tools.ValueConfigCellModifier;
import com.worldline.appprofiles.wizard.ui.project.pages.tools.ValueConfigContentProvider;
import com.worldline.appprofiles.wizard.ui.project.pages.tools.ValueConfigLabelProvider;

public class ApplicationConfigureProfilePage extends ValidatingApplicationWizardPage {

	private static final String VALUE_KEY = "value";
	private static final String MESSAGE_KEY = "message";
	private ApplicationWizardOutput applicationWizardOutput;
	private Label descriptionLabel;
	private CheckboxTableViewer optionalElementsTableViewer;
	private TableViewer valueElementsTableViewer;
	private ValueConfigCellEditor cellEditor;

	public ApplicationConfigureProfilePage(ApplicationWizardOutput applicationWizardOutput) {
		super(ApplicationConfigureProfilePageMessages.PAGE_NAME.value());
		this.applicationWizardOutput = applicationWizardOutput;
		setTitle(ApplicationConfigureProfilePageMessages.PAGE_TITLE.value());
		setDescription(ApplicationConfigureProfilePageMessages.PAGE_DESC.value());
	}

	public void createControl(Composite parent) {
		Composite background = new Composite(parent, SWT.NONE);
		background.setLayout(new FormLayout());
		
		this.descriptionLabel = new Label(background, SWT.NONE);
		new FormDataBuilder().left().top(0, 9).right().apply(descriptionLabel);

		Composite intermediate = new Composite(background, SWT.NONE);
		intermediate.setLayout(new FormLayout());
		new FormDataBuilder().top(this.descriptionLabel,9).left().right().bottom().apply(intermediate);
		
		// Create a CheckBoxList viewer for element that are optional
		Table optionalElementsTable = new Table(intermediate, SWT.BORDER | SWT.CHECK | SWT.V_SCROLL | SWT.H_SCROLL);
		optionalElementsTable.setHeaderVisible(true);
		optionalElementsTable.setLinesVisible(true);
		TableColumn optionalElementsTableColumn = new TableColumn(optionalElementsTable, SWT.NONE);
		optionalElementsTableColumn.setText(ApplicationConfigureProfilePageMessages.PAGE_LABEL_OPTENTRIES_LIST.value());
		optionalElementsTable.addControlListener(new TableResizeControlListener(optionalElementsTable,
				optionalElementsTableColumn));
		new FormDataBuilder().left().top().right().bottom(50).apply(optionalElementsTable);
		TableSelectionDisableListener.disableSelection(optionalElementsTable);

		this.optionalElementsTableViewer = new CheckboxTableViewer(optionalElementsTable);
		optionalElementsTableViewer
				.setCheckStateProvider(new ChoiceConfigCheckStateProvider(applicationWizardOutput));
		optionalElementsTableViewer.setLabelProvider(new ChoiceConfigLabelProvider());
		optionalElementsTableViewer.setContentProvider(new ChoiceConfigContentProvider());
		optionalElementsTableViewer
				.addCheckStateListener(new ChoiceConfigCheckStateListener(applicationWizardOutput));

		// Create a TableViewer for value elements
		Table valueElementsTable = new Table(intermediate, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL | SWT.FULL_SELECTION);
		valueElementsTable.setHeaderVisible(true);
		valueElementsTable.setLinesVisible(true);
		TableSelectionDisableListener.disableSelection(valueElementsTable);
		TableColumn valueElementsTableMessageColumn = new TableColumn(valueElementsTable, SWT.NONE, 0);
		valueElementsTableMessageColumn.setText(ApplicationConfigureProfilePageMessages.PAGE_LABEL_VALENTRIES_LIST.value());
		TableColumn valueElementsTableValueColumn = new TableColumn(valueElementsTable, SWT.NONE, 1);
		valueElementsTableValueColumn.setText(ApplicationConfigureProfilePageMessages.PAGE_LABEL_VALENTRIES_VALLABEL.value());
		valueElementsTableValueColumn.setWidth(120);
		valueElementsTable.addControlListener(new TableResizeControlListener(valueElementsTable,
				valueElementsTableMessageColumn));

		this.valueElementsTableViewer = new TableViewer(valueElementsTable);
		this.valueElementsTableViewer.setContentProvider(new ValueConfigContentProvider());
		this.valueElementsTableViewer.setLabelProvider(new ValueConfigLabelProvider(applicationWizardOutput));
		this.valueElementsTableViewer.setColumnProperties(new String[] { MESSAGE_KEY, VALUE_KEY });
		new FormDataBuilder().left().top(optionalElementsTable).right().bottom().apply(valueElementsTable);

		CellEditor[] cellEditors = new CellEditor[2];
		this.cellEditor = new ValueConfigCellEditor(valueElementsTable) {

			@Override
			public void runValidationCallback() {
				validate();
			}
			
		};
		ValueConfigCellModifier valueConfigCellModifier = new ValueConfigCellModifier(applicationWizardOutput,
				valueElementsTableViewer);
		valueConfigCellModifier.setCellEditor(cellEditor);
		cellEditors[1] = cellEditor;
		this.valueElementsTableViewer.setCellEditors(cellEditors);
		this.valueElementsTableViewer.setCellModifier(valueConfigCellModifier);

		setControl(background);
		validate();
	}

	@Override
	public void setVisible(boolean visible) {
		this.descriptionLabel.setText(ApplicationConfigureProfilePageMessages.PAGE_LABEL_SELECTEDPROFILE.value(this.applicationWizardOutput
				.getSelectedApplicationProfile().getName()));
		this.optionalElementsTableViewer.setInput(this.applicationWizardOutput);
		this.valueElementsTableViewer.setInput(this.applicationWizardOutput);
		super.setVisible(visible);
		optionalElementsTableViewer.getTable().setFocus();
	}

	@Override
	protected ApplicationWizardPageStatus isPageValid() {
		if (this.cellEditor.getErrorMessage() != null) {
			ApplicationWizardPageStatus.error(this.cellEditor.getErrorMessage());
		}
		return ApplicationWizardPageStatus.ok();
	}
}
