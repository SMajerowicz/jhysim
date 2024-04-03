package org.jhysim.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jhysim.gui.frame.JCancelFrameButton;
import org.jhysim.gui.frame.Killable;
import org.jhysim.plot.graphic.JShowProfilePanel;
import org.jhysim.pattern.CommandPattern;
import org.jhysim.pattern.ObserverPattern;
import org.jhysim.gui.slider.JShowSelectedProfilesSlider;

import org.jhysim.simulation.methods.NumericalSchema;

/**
 * To visualize all the profiles during a simulation
 * @author Sébastien Majerowicz
 */
public class JVisualizeProfilesFrame extends JFrame implements ActionListener, ChangeListener, ObserverPattern, Killable, WindowListener
{
	private static final long serialVersionUID = -7075548346812252369L;

	private static boolean isInstanced = false;

	private final static int PROFILES_PER_ROW = 2; 

	protected NumericalSchema schema = null;
	protected int nProfiles = 0;

	protected JShowProfilePanel[] profilePanels = null;
	private JPanel selectProfilePanel = null;
	private JShowSelectedProfilesSlider selectProfileSlider = null;

	private int nFrames = 0;

	private JCancelFrameButton closeButton = null;

/**
 * To retrieve the unique instance of this frame
 * @param schema NumericalSchema numerical schema
 * @return JVisualizeProfilesFrame or null
 */
	public final static JVisualizeProfilesFrame getInstance (NumericalSchema schema)
	{
		JVisualizeProfilesFrame frame = null;

		if (!JVisualizeProfilesFrame.isInstanced)
		{
			frame = new JVisualizeProfilesFrame(schema);
		}

		return frame;
	}

/**
 * Constructor
 * @param schema NumericalSchema numerical schema
 */
	protected JVisualizeProfilesFrame (NumericalSchema schema)
	{
		super();

		this.schema = schema;

		this.nProfiles = this.schema.getNProfiles();
		this.profilePanels = new JShowProfilePanel[this.nProfiles];

		GridBagLayout layout = new GridBagLayout();
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.HORIZONTAL;//for all components
		constraints.insets = new Insets(2,2,2,2);

		JPanel panel = new JPanel(layout);

		int iy = 0;
		double[] yext = null;
		for (int i = 0 ; i < this.nProfiles ; i++)
		{
			yext = this.schema.getYProfileExtrema(i);		

			this.profilePanels[i] = new JShowProfilePanel(350,250,25,Color.WHITE,this.schema.getParameter(0),this.schema.getProfileName(i),yext[0],yext[1]);
			constraints.gridx = i - iy*JVisualizeProfilesFrame.PROFILES_PER_ROW;
			constraints.gridy = iy;
			layout.setConstraints(this.profilePanels[i],constraints);
			panel.add(this.profilePanels[i]);
			if ((i != 0) && (i%JVisualizeProfilesFrame.PROFILES_PER_ROW == 0)) iy++;
		}

		this.selectProfileSlider = new JShowSelectedProfilesSlider(this,0,99,99);
		this.selectProfileSlider.setPaintLabels(true);
		this.selectProfileSlider.setPaintTicks(true);
		this.selectProfileSlider.setPaintTrack(true);
		this.selectProfileSlider.addChangeListener(this);

		this.selectProfilePanel = new JPanel(new GridLayout(1,1));//new FlowLayout(FlowLayout.CENTER));
		this.selectProfilePanel.add(this.selectProfileSlider);

		this.closeButton = new JCancelFrameButton(this,"Close",null,KeyEvent.VK_C);
		this.closeButton.addActionListener(this);

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		buttonPanel.add(this.closeButton);

		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(panel,BorderLayout.NORTH);
		this.getContentPane().add(buttonPanel,BorderLayout.SOUTH);

		this.addWindowListener(this);

		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		this.updateTitle();
		this.pack();
		this.setResizable(false);

		JVisualizeProfilesFrame.isInstanced = true;
	}

/**
 * To update the frame title
 */
	private void updateTitle ()
	{
		this.setTitle("Profile visualization ("+String.valueOf(this.nFrames)+")");
	}

/**
 * To update the frame title
 * @param index int
 */
	private void updateTitle (int index)
	{
		this.setTitle("Profile visualization ("+String.valueOf(index)+")");
	}

/**
 * To show the slider which can select a frame 
 */
	public final void showSelectProfileSlider ()
	{
		this.selectProfileSlider.setMinimum(0);
		this.selectProfileSlider.setMaximum(this.nFrames-1);
//determine minor and major tick values
		int tenPower = 1;
		while (tenPower < this.nFrames)
		{
			tenPower *= 10;
		}
		tenPower /= 10;
		this.selectProfileSlider.setMinorTickSpacing(tenPower/5);
		this.selectProfileSlider.setMajorTickSpacing(tenPower);
		this.selectProfileSlider.setValue(this.nFrames-1);
		this.getContentPane().add(this.selectProfilePanel,BorderLayout.CENTER);
		this.getContentPane().validate();
		this.getContentPane().repaint();
		this.pack();
	}

/**
 * To hide the slider which can select a frame 
 */
	public final void hideSelectProfileSlider ()
	{
		this.getContentPane().remove(this.selectProfilePanel);
		this.getContentPane().validate();
		this.getContentPane().repaint();
		this.pack();
	}

/**
 * To show (reload) the n-th frame
 * @throws IndexOutOfBoundsException in the case when index < 0 || index >= nFrames
 * @param index int frame index 
 */
	public void reloadFrame (int index) throws IndexOutOfBoundsException
	{
		if ((index >= 0) && (index < this.nFrames))
		{
			for (int i = 0 ; i < this.nProfiles ; i++)
			{
				this.profilePanels[i].setData(this.schema.getProfile(i,index));
				this.profilePanels[i].repaint();
			}

			this.updateTitle(index);
		}
		else
		{
			throw new IndexOutOfBoundsException();
		}
	}

/**
 * To retrieve the current number of displayed frames
 * @return int
 */
	public final int getNFrames ()
	{
		return this.nFrames;
	}

/**
 * To initialize the number of displayed frames
 */
	public final void initNFrames ()
	{
		if (this.schema != null)
		{
			this.nFrames = this.schema.getResults().getncols()/this.schema.getNProfiles();
		}
	}

/**
 * @see java.awt.event.WindowListener#windowOpened(java.awt.event.WindowEvent)
 */
	public void windowOpened(WindowEvent e)
	{}
/**
 * @see java.awt.event.WindowListener#windowClosing(java.awt.event.WindowEvent)
 */
	public void windowClosing(WindowEvent e)
	{
		this.kill();
	}
/**
 * @see java.awt.event.WindowListener#windowClosed(java.awt.event.WindowEvent)
 */
	public void windowClosed(WindowEvent e)
	{}
/**
 * @see java.awt.event.WindowListener#windowIconified(java.awt.event.WindowEvent)
 */
	public void windowIconified(WindowEvent e)
	{}
/**
 * @see java.awt.event.WindowListener#windowDeiconified(java.awt.event.WindowEvent)
 */
	public void windowDeiconified(WindowEvent e)
	{}
/**
 * @see java.awt.event.WindowListener#windowActivated(java.awt.event.WindowEvent)
 */
	public void windowActivated(WindowEvent e)
	{}
/**
 * @see java.awt.event.WindowListener#windowDeactivated(java.awt.event.WindowEvent)
 */
	public void windowDeactivated(WindowEvent e)
	{}

/**
 * @see org.application.gui.frame.Killable#kill()
 */
	public void kill()
	{
		JVisualizeProfilesFrame.isInstanced = false;
		this.dispose();
	}

/**
 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
 */
	public void actionPerformed(ActionEvent e)
	{
		CommandPattern command = (CommandPattern)e.getSource();
		command.execute();
	}

/**
 * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
 */
	public void stateChanged (ChangeEvent e)
	{
		CommandPattern command = (CommandPattern)e.getSource();
		command.execute();
	}

/**
 * @see org.application.pattern.ObserverPattern#updateObserver()
 */
	public void updateObserver()
	{
		for (int i = 0 ; i < this.nProfiles ; i++)
		{
			this.profilePanels[i].setData(this.schema.getProfile(i,this.nFrames));
			this.profilePanels[i].repaint();
		}

		this.nFrames++;
		this.updateTitle();
	}

/**
 * @see org.application.pattern.ObserverPattern#killObserver()
 */
	public void killObserver()
	{
		this.kill();
	}
}