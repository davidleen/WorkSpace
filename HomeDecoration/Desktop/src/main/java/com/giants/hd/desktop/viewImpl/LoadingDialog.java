package com.giants.hd.desktop.viewImpl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoadingDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String DEFAULT_MESSAGE="正在处理中...";


	JLabel messageLable;



	public static  void  init(Window toppestWindow)
	{
		loadingDialog=	new LoadingDialog(toppestWindow,null);
	}

	static LoadingDialog loadingDialog=null;



	public LoadingDialog(Window owner,
						 ActionListener cancleListener) {
		super(owner);
		iniDialog(DEFAULT_MESSAGE, cancleListener);


	}

	public LoadingDialog(Window owner,String message,
						 ActionListener cancleListener) {
		super(owner);
		iniDialog(message, cancleListener);
	}
	public LoadingDialog(Frame owner, String message,
			ActionListener cancleListener) {
		super(owner);
		iniDialog(message, cancleListener);

	}

	public LoadingDialog(Dialog owner, String message,
			ActionListener cancleListener) {
		super(owner);
		iniDialog(message, cancleListener);

	}

	private void iniDialog(String message, final ActionListener cancleListener) {
		if(message ==null)
		{message=DEFAULT_MESSAGE;}
		setModal(true);
		final JPanel mainPane = new JPanel(null);
		JProgressBar progressBar = new JProgressBar();
		messageLable = new JLabel("" + message);
		messageLable.setHorizontalAlignment(JLabel.CENTER);
		JButton btnCancel = new JButton("Cancel");
		progressBar.setIndeterminate(true);
		btnCancel.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {

				setVisible(false);
				dispose();
				cancleListener.actionPerformed(e);
			}
		});
		mainPane.setLayout(new BorderLayout());
		mainPane.add(progressBar,BorderLayout.NORTH);
		mainPane.add(messageLable,BorderLayout.CENTER);
		  mainPane.add(btnCancel,BorderLayout.SOUTH);
		  mainPane.setBackground(Color.white);
		getContentPane().add(mainPane);
		setUndecorated(true); // 除去title
		setResizable(true);
		setSize(500, 100);
		setLocationRelativeTo(getParent()); // 设置此窗口相对于指定组件的位�?
		 setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

	}

	public void setMessage(String message)
	{

		messageLable.setText(message);
	}

}
