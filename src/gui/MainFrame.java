package gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.UIManager;

public class MainFrame extends JFrame implements ActionListener
{

	private DrawingPanel drawingPanel;
	private JButton clearButton;

	public MainFrame()
	{
		initSetting();
		initDrawingPanel();
		initClearButton();
	}

	public void initSetting()
	{
		this.setSize(GuiConfig.FRAME_DIMENSION.width, GuiConfig.FRAME_DIMENSION.height);
		this.setVisible(true);
		this.setLayout(null);
		this.setResizable(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBackground(Color.white);
	}

	public void initDrawingPanel()
	{
		drawingPanel = new DrawingPanel();
		getContentPane().add(drawingPanel);
		drawingPanel.setBackground(Color.WHITE);

	}

	public void initClearButton()
	{
		clearButton = new JButton("Çå³ý");
		clearButton.setBounds(0, 0, 100, 50);
		getContentPane().add(clearButton);
		clearButton.addActionListener(this);

	}

	@Override
	public void actionPerformed(ActionEvent event)
	{
		// TODO Auto-generated method stub
		if (event.getSource() == clearButton)
		{
			drawingPanel.clear();
		}

	}

	public static void main(String args[])
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e)
		{
		}

		MainFrame mainFrame = new MainFrame();

	}
}
