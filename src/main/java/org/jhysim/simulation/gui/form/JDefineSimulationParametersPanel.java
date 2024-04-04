package org.jhysim.simulation.gui.form;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import javax.swing.border.TitledBorder;

import org.jhysim.pattern.CommandPattern;

import org.jhysim.plot.graphic.JDefineProfilePanel;

import org.jhysim.gui.button.JChangeSimulationProfileComboBox;

/**
 * To initialize all the parameters for a given simulation
 * @author Sébastien Majerowicz
 */
public class JDefineSimulationParametersPanel extends JPanel implements ActionListener
{
	private static final long serialVersionUID = 5322556755071753824L;

	private JChangeSimulationProfileComboBox changeProfileComboBox = null;
	private JPanel selectedProfilePanel = null;

	private JDefineProfilePanel[] profilePanels = null;
	private JTextField[] doubleTextFields = null;

	private final static int FIELDS_PER_ROW = 4;

/**
 * Constructor
 * @param profiles String[] name of the needed initial profiles
 * @param yexts Arraylist list of two-element array defining the y extrema of each profile
 * @param paramdesc String[] name of the needed initial variable description
 */
	public JDefineSimulationParametersPanel (String[] profiles, ArrayList<double[]> yexts, String[] doubles, String[] paramdesc)
	{
		super(true);

		DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>();
		this.profilePanels = new JDefineProfilePanel[profiles.length];
		String title = null;
		double[] yext = null;
		for (int i = 0 ; i < profiles.length ; i++)
		{
			model.addElement(profiles[i]);
			title = profiles[i];
			yext = (double[])yexts.get(i);
			this.profilePanels[i] = new JDefineProfilePanel(400,300,25,Color.WHITE,yext[0],yext[1],title);
		}

		this.changeProfileComboBox = new JChangeSimulationProfileComboBox(this,model);
		this.changeProfileComboBox.setSelectedIndex(0);
		this.changeProfileComboBox.addActionListener(this);

		this.selectedProfilePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		this.selectedProfilePanel.add(this.profilePanels[0]);

		JPanel changePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		changePanel.add(new JLabel("Selected profile",JLabel.LEFT));
		changePanel.add(this.changeProfileComboBox);

		JPanel profilePanel = new JPanel(new BorderLayout());
		profilePanel.add(changePanel,BorderLayout.NORTH);
		profilePanel.add(this.selectedProfilePanel,BorderLayout.CENTER);

		this.doubleTextFields = new JTextField[doubles.length];

		JLabel[] doubleLabels = new JLabel[doubles.length];
		for (int i = 0 ; i < doubles.length ; i++)
		{
			this.doubleTextFields[i] = new JTextField(5);
			this.doubleTextFields[i].setHorizontalAlignment(JTextField.RIGHT);
			doubleLabels[i] = new JLabel(doubles[i],JLabel.LEFT);
		}

		GridBagLayout layout = new GridBagLayout();
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.HORIZONTAL;//for all components
		constraints.insets = new Insets(2,2,2,2);

		JPanel doublePanel = new JPanel(layout);
		int iy = 0;
		for (int i = 0 ; i < 2*this.doubleTextFields.length ; i = i+2)
		{
			constraints.gridx = i - iy*JDefineSimulationParametersPanel.FIELDS_PER_ROW;
			constraints.gridy = iy;
			layout.setConstraints(doubleLabels[i/2],constraints);
			doublePanel.add(doubleLabels[i/2]);
			constraints.gridx = i - iy*JDefineSimulationParametersPanel.FIELDS_PER_ROW + 1;
			constraints.gridy = iy;
			layout.setConstraints(this.doubleTextFields[i/2],constraints);
			doublePanel.add(this.doubleTextFields[i/2]);
			if ((i != 0) && ((i+2)%JDefineSimulationParametersPanel.FIELDS_PER_ROW == 0)) iy++;
		}

		doublePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK,1),"Constants",TitledBorder.RIGHT,TitledBorder.TOP));

		this.setLayout(new BorderLayout());
		this.add(profilePanel,BorderLayout.CENTER);
		this.add(doublePanel,BorderLayout.SOUTH);
	}

/**
 * To change the selected simulation panel
 */
	public final void changeSelectedSimulationPanel ()
	{
		this.selectedProfilePanel.removeAll();

		int index = this.changeProfileComboBox.getSelectedIndex();

		this.selectedProfilePanel.add(this.profilePanels[index],BorderLayout.CENTER);

		this.selectedProfilePanel.validate();
		this.selectedProfilePanel.repaint();
	}

/**
 * To initialize a profile in a selected panel
 * @param index int index
 * @param profile double[]
 * @throws IndexOutOfBoundsException
 */
	public final void setProfile (int index, double[] profile) throws IndexOutOfBoundsException
	{
		this.profilePanels[index].setProfile(profile);
	}

/**
 * To initialize the text in a selected textfield
 * @param index int
 * @param text String
 * @throws IndexOutOfBoundsException
 */
	public final void setDoubleText (int index, String text) throws IndexOutOfBoundsException
	{
		this.doubleTextFields[index].setText(text);
	}

/**
 * To initialize the y-axis extrema
 * @param index int
 * @param ymin double
 * @param ymax double
 * @throws IndexOutfOfBOundsException
 */
	public final void setYExtrema (int index, double ymin, double ymax) throws IndexOutOfBoundsException
	{
		this.profilePanels[index].setYmin(ymin);
		this.profilePanels[index].setYmax(ymax);
	}

/**
 * To retrieve all the profiles
 * @param dx double spatial resolution
 * @return ArrayList list of double array
 * @throws NumberFormatException the spatial resolution must be between 0.0 and 1.0 strictly
 */
	public final ArrayList<double[]> getProfiles (double dx) throws NumberFormatException
	{
		if ((dx <= 0.0) || (dx >= 1.0)) throw new NumberFormatException("The spatial resolution must be between 0.0 and 1.0 strictly");

		ArrayList<double[]> profiles = new ArrayList<double[]>(this.profilePanels.length);

		for (int i = 0 ; i < this.profilePanels.length ; i++)
		{
			profiles.add(this.profilePanels[i].getProfile(dx));
		}

		return profiles;
	}

/**
 * To retrieve all the double texts
 * @return String[]
 */
	public final String[] getDoubles ()
	{
		String[] strings = new String[this.doubleTextFields.length];

		for (int i = 0 ; i < this.doubleTextFields.length ; i++)
		{
			strings[i] = this.doubleTextFields[i].getText();
		}

		return strings;
	}

/**
 * To retrieve the number of profiles
 * @return int
 */
	public final int getNProfiles ()
	{
		return this.profilePanels.length;
	}
/**
 * To retrieve the number of double text fields
 * @return int
 */
	public final int getNDoubles ()
	{
		return this.doubleTextFields.length;
	}

/**
 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
 */
	public void actionPerformed(ActionEvent e)
	{
		CommandPattern command = (CommandPattern)e.getSource();
		command.execute();
	}
}