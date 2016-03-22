package com.see.week3;

//import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
//import javax.swing.JSplitPane;
import javax.swing.JTextField;
//import java.awt.GridBagLayout;
import java.awt.GridLayout;
//import java.awt.GridBagConstraints;
//import java.awt.Insets;
//import java.awt.List;

//import com.jgoodies.forms.layout.FormLayout;
//import com.jgoodies.forms.layout.ColumnSpec;
//import com.jgoodies.forms.layout.RowSpec;
import javax.swing.JLabel;
//import net.miginfocom.swing.MigLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import java.awt.FlowLayout;
import javax.swing.SwingConstants;
import javax.swing.LayoutStyle.ComponentPlacement;
import org.eclipse.wb.swing.FocusTraversalOnArray;
import java.awt.Component;
import javax.swing.JScrollPane;
//import javax.swing.border.SoftBevelBorder;
//import javax.swing.border.BevelBorder;
import java.awt.Dimension;
import java.awt.Rectangle;
import javax.swing.border.MatteBorder;
import java.awt.Color;
//import javax.swing.border.TitledBorder;
//import javax.swing.event.DocumentEvent;
//import javax.swing.event.DocumentListener;
//import javax.swing.JScrollBar;
//import javax.swing.JSpinner;
//import javax.swing.JEditorPane;
//import javax.swing.JSlider;
import javax.swing.ScrollPaneConstants;
import javax.swing.JTabbedPane;
//import java.beans.PropertyChangeListener;
//import java.lang.reflect.Array;
import java.util.ArrayList;
//import java.beans.PropertyChangeEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.ComponentOrientation;
//import java.awt.event.InputMethodListener;
//import java.awt.event.InputMethodEvent;

public class Main extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1585582583020842270L;
	private JPanel contentPane;
	private JTextField projectName;
	private JTextField projectLength;
	private JTextField discountRate;
	private JTextField discountRateTab2;
	private JTextField presentValueTab2;
	private JTextField futureValueTab2;
	private JTextField numOfYears;

	/**
	 * Launch the application.
	 */
	private static double IRR(float[] cashFlows)
	{
		final int MAX_ITER = 20;
		double EXCEL_EPSILON = 0.0000001;

		double x = 0.1;
		int iter = 0;
		while (iter++ < MAX_ITER) {

			final double x1 = 1.0 + x;
			double fx = 0.0;
			double dfx = 0.0;
			for (int i = 0; i < cashFlows.length; i++) {
				final double v = cashFlows[ i ];
				final double x1_i = Math.pow( x1, i );
				fx += v / x1_i;
				final double x1_i1 = x1_i * x1;
				dfx += -i * v / x1_i1;
			}
			final double new_x = x - fx / dfx;
			final double epsilon = Math.abs( new_x - x );

			if (epsilon <= EXCEL_EPSILON) {
				if (x == 0.0 && Math.abs( new_x ) <= EXCEL_EPSILON) {
					return 0.0;
				}
				else {
					return new_x*100;
				}
			}
			x = new_x;
		}
		return x;
	}

	private static String RiskAdjustedROI(float netPresentValue,float discountedCost){
		return String.valueOf((int) ((netPresentValue/discountedCost)*100));		
	}

	private static float FloatWithTwoNumberDecimal(float f){
		return (float) Math.round(f*100)/100;		
	}

	private float FV(float discountRate,float presentValue,int year){
		float fV=(float) (presentValue*Math.pow(1+discountRate,year));
		return FloatWithTwoNumberDecimal(fV);
	}
	private static String NPV(String _discountRate,float[] cashFlows){
		float npv=0;
		float discountRate=Float.valueOf(_discountRate);
		for(int i=0;i<cashFlows.length;i++){
			if(i==0){
				npv=cashFlows[0];
				continue;
			}
			npv+=FloatWithTwoNumberDecimal((float) (cashFlows[i]*FloatWithTwoNumberDecimal((float) (1/Math.pow(1+discountRate/100, i)))));			
		}
		return String.valueOf(FloatWithTwoNumberDecimal(npv));
	}

	private static float NPV(String _discountRate,float money,int year){
		float npv=0;
		float discountRate=Float.valueOf(_discountRate);
		//System.out.println(FloatWithTwoNumberDecimal(1.089f));

		npv+=FloatWithTwoNumberDecimal((float) (money*FloatWithTwoNumberDecimal((float) (1/Math.pow(1+discountRate/100, year)))));			

		return npv;		
	}
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main frame = new Main();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Main() {

		ArrayList<JLabel> cashFlowJLabels=new ArrayList<JLabel>();
		ArrayList<JLabel> totalJLabels=new ArrayList<JLabel>();
		ArrayList<JLabel> totalDiscountedJLabels=new ArrayList<JLabel>();
		ArrayList<JLabel> discountedBenefitJLabels=new ArrayList<JLabel>();
		ArrayList<JLabel> discountedCostJLabels=new ArrayList<JLabel>();
		ArrayList<JTextField> benefitJTextFields=new ArrayList<JTextField>();
		ArrayList<JTextField> costJTextFields=new ArrayList<JTextField>();
		setTitle("Solfware Engineering Economics");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(10, 10, 800, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		JScrollPane scrollPane = new JScrollPane();
		JLabel netPresentValue = new JLabel("");
		JLabel returnOfInvestment = new JLabel("");
		JLabel riskAdjustedROI = new JLabel("");
		JLabel internalRateOfReturn = new JLabel("");
		JLabel paybackPeriods = new JLabel("");
		presentValueTab2 = new JTextField();
		presentValueTab2.setHorizontalAlignment(SwingConstants.RIGHT);
		futureValueTab2 = new JTextField();
		futureValueTab2.setHorizontalAlignment(SwingConstants.RIGHT);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setAlignmentX(Component.LEFT_ALIGNMENT);
		tabbedPane.setAlignmentY(Component.TOP_ALIGNMENT);
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
				gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
						.addContainerGap()
						.addComponent(tabbedPane, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
				);
		gl_contentPane.setVerticalGroup(
				gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 551, Short.MAX_VALUE)
				);

		JPanel panel_4 = new JPanel();
		panel_4.setAlignmentY(Component.TOP_ALIGNMENT);
		panel_4.setAlignmentX(Component.LEFT_ALIGNMENT);
		tabbedPane.addTab("NPV+ROI+RaROI+IRR+PP", null, panel_4, null);

		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

		JLabel lblNewLabel = new JLabel("Project name:");
		lblNewLabel.setHorizontalAlignment(SwingConstants.LEFT);
		panel.add(lblNewLabel);

		projectName = new JTextField();
		projectName.setToolTipText("Name of your project.");
		panel.add(projectName);
		projectName.setColumns(10);

		JPanel panel_1 = new JPanel();
		panel_1.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

		JLabel lblLength = new JLabel("Length: ");
		lblLength.setHorizontalAlignment(SwingConstants.LEFT);
		panel_1.add(lblLength);

		projectLength = new JTextField();
		projectLength.setHorizontalAlignment(SwingConstants.RIGHT);
		projectLength.setToolTipText("Type length of your project in years.");
		//		projectLength.addFocusListener(new FocusAdapter() {
		//			@Override
		//			public void focusLost(FocusEvent arg0) {
		//			}
		//		});

		//		projectLength.addPropertyChangeListener(new PropertyChangeListener() {
		//			public void propertyChange(PropertyChangeEvent arg0) {
		//				int yearCount=Integer.valueOf(projectLength.getText());
		//				for(int i=0;i<yearCount;i++){
		//				scrollPane.add(new JLabel("Year "+i));
		//				}
		//				
		//			}
		//		});

		projectLength.setColumns(10);
		panel_1.add(projectLength);

		JLabel lblYears = new JLabel("Years.");
		panel_1.add(lblYears);

		JPanel panel_2 = new JPanel();
		panel_2.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

		JLabel lblDiscountRate = new JLabel("Discount rate:");
		lblDiscountRate.setHorizontalAlignment(SwingConstants.LEFT);
		panel_2.add(lblDiscountRate);

		discountRate = new JTextField();
		discountRate.setHorizontalAlignment(SwingConstants.RIGHT);
		discountRate.setToolTipText("Discount rate in percent. \r\nInput must be a number.");
		discountRate.setColumns(10);
		panel_2.add(discountRate);

		JLabel label_3 = new JLabel("%");
		panel_2.add(label_3);

		//		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setViewportBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		scrollPane.setBounds(new Rectangle(0, 0, 600, 200));
		scrollPane.setAlignmentY(Component.TOP_ALIGNMENT);
		scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
		scrollPane.setMinimumSize(new Dimension(600, 200));

		JPanel panel_3 = new JPanel();
		panel_3.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 10));

		JLabel lblNetPresentValue = new JLabel("Net present value:");
		lblNetPresentValue.setHorizontalAlignment(SwingConstants.LEFT);
		panel_3.add(lblNetPresentValue);

		JButton calcBtn = new JButton("Calculate!");
		calcBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		calcBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(projectLength.getText().length()<1) return;
				int yearCount=Integer.valueOf(projectLength.getText());
				//System.out.println(yearCount);
				float[] cashFlows=new float[yearCount];
				Boolean canCalcNPV=true;
				float totalBef=0f;
				float totalCost=0f;
				float totalDiscountBef=0;
				float totalDiscountCost=0;
				int paybackYear=-1;
				for(int i=0;i<yearCount;i++){
					String sBef=benefitJTextFields.get(i).getText();
					String sCost=costJTextFields.get(i).getText();
					if(sBef.length()<1||sCost.length()<1||Float.valueOf(sBef)==null||Float.valueOf(sCost)==null){
						canCalcNPV=false;
						continue;
					}
					else {
						totalBef+=Float.valueOf(sBef);
						totalCost+=Float.valueOf(sCost);
						float discountBef=NPV(discountRate.getText(),Float.valueOf(sBef),i);
						discountedBenefitJLabels.get(i).setText(String.valueOf(discountBef));
						totalDiscountBef+=discountBef;
						float discountCost=NPV(discountRate.getText(),Float.valueOf(sCost),i);
						discountedCostJLabels.get(i).setText(String.valueOf(discountCost));
						totalDiscountCost+=discountCost;
						if(totalDiscountBef-totalDiscountCost>0&&paybackYear<0) paybackYear=i;
						cashFlows[i]=Float.valueOf(sBef)-Float.valueOf(sCost);
						cashFlowJLabels.get(i).setText(String.valueOf(cashFlows[i]));
					}
				}

				totalJLabels.get(0).setText(String.valueOf(totalBef));
				totalJLabels.get(1).setText(String.valueOf(totalCost));
				totalJLabels.get(2).setText(String.valueOf(totalBef-totalCost));
				totalDiscountedJLabels.get(0).setText(String.valueOf(totalDiscountBef));
				totalDiscountedJLabels.get(1).setText(String.valueOf(totalDiscountCost));

				if(canCalcNPV) {
					netPresentValue.setText(NPV(discountRate.getText(),cashFlows));

					float discountedCost=0;
					for(int i=0;i<yearCount;i++){
						discountedCost+=Float.valueOf(discountedCostJLabels.get(i).getText());						
					}

					//returnOfInvestment.setText(ROI(Float.valueOf(NPV(discountRate.getText(),cashFlows)), discountedCost)+" %");
					returnOfInvestment.setText(String.valueOf(FloatWithTwoNumberDecimal((float) ((totalBef-totalCost)/totalCost)*100))+" %");
					riskAdjustedROI.setText(RiskAdjustedROI(Float.valueOf(NPV(discountRate.getText(),cashFlows)), discountedCost)+" %");
					internalRateOfReturn.setText(String.valueOf(FloatWithTwoNumberDecimal((float) IRR(cashFlows)))+" %");
					if(paybackYear>0) paybackPeriods.setText(paybackYear+" years");
					else
						paybackPeriods.setText("Project dont have a payback periods.");
				}
			}
		});

		calcBtn.setVerticalAlignment(SwingConstants.BOTTOM);
		calcBtn.setAlignmentX(0.5f);

		JPanel panel_6 = new JPanel();
		panel_6.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 10));

		JLabel lblReturnOfInvestmentroi = new JLabel("Return of Investment(ROI):");
		lblReturnOfInvestmentroi.setHorizontalAlignment(SwingConstants.LEFT);
		panel_6.add(lblReturnOfInvestmentroi);


		returnOfInvestment.setHorizontalAlignment(SwingConstants.LEFT);
		panel_6.add(returnOfInvestment);

		JPanel panel_7 = new JPanel();
		panel_7.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 10));

		JLabel lblReturnOfInvestmentrisk = new JLabel("Return of Investment(Risk adjusted ROI):");
		lblReturnOfInvestmentrisk.setHorizontalAlignment(SwingConstants.LEFT);
		panel_7.add(lblReturnOfInvestmentrisk);


		riskAdjustedROI.setHorizontalAlignment(SwingConstants.LEFT);
		panel_7.add(riskAdjustedROI);

		JPanel panel_8 = new JPanel();
		panel_8.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 10));

		JLabel lblInternalRateOf = new JLabel("Internal rate of return(IRR):");
		lblInternalRateOf.setHorizontalAlignment(SwingConstants.LEFT);
		panel_8.add(lblInternalRateOf);


		internalRateOfReturn.setHorizontalAlignment(SwingConstants.LEFT);
		panel_8.add(internalRateOfReturn);

		JPanel panel_9 = new JPanel();
		panel_9.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 10));

		JLabel lblPaybackPeriods = new JLabel("Payback periods:");
		lblPaybackPeriods.setHorizontalAlignment(SwingConstants.LEFT);
		panel_9.add(lblPaybackPeriods);

		//JLabel paybackPeriods = new JLabel("");
		paybackPeriods.setHorizontalAlignment(SwingConstants.LEFT);
		panel_9.add(paybackPeriods);
		GroupLayout gl_panel_4 = new GroupLayout(panel_4);
		gl_panel_4.setHorizontalGroup(
				gl_panel_4.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_4.createSequentialGroup()
						.addGap(5)
						.addGroup(gl_panel_4.createParallelGroup(Alignment.LEADING)
								.addComponent(panel_2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(panel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 731, GroupLayout.PREFERRED_SIZE))
						.addContainerGap(423, Short.MAX_VALUE))
				.addGroup(gl_panel_4.createSequentialGroup()
						.addContainerGap()
						.addComponent(panel_3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addContainerGap(1064, Short.MAX_VALUE))
				.addGroup(gl_panel_4.createSequentialGroup()
						.addContainerGap()
						.addComponent(panel_9, GroupLayout.PREFERRED_SIZE, 653, GroupLayout.PREFERRED_SIZE)
						.addContainerGap(516, Short.MAX_VALUE))
				.addGroup(gl_panel_4.createSequentialGroup()
						.addContainerGap()
						.addComponent(panel_8, GroupLayout.PREFERRED_SIZE, 653, GroupLayout.PREFERRED_SIZE)
						.addContainerGap(516, Short.MAX_VALUE))
				.addGroup(gl_panel_4.createSequentialGroup()
						.addGap(344)
						.addComponent(calcBtn)
						.addContainerGap(744, Short.MAX_VALUE))
				.addGroup(gl_panel_4.createSequentialGroup()
						.addContainerGap()
						.addGroup(gl_panel_4.createParallelGroup(Alignment.TRAILING, false)
								.addComponent(panel_6, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(panel_7, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 653, Short.MAX_VALUE))
						.addContainerGap(516, Short.MAX_VALUE))
				);
		gl_panel_4.setVerticalGroup(
				gl_panel_4.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_4.createSequentialGroup()
						.addGap(5)
						.addComponent(panel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addComponent(panel_2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 142, GroupLayout.PREFERRED_SIZE)
						.addGap(18)
						.addComponent(panel_3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(panel_6, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(panel_7, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(panel_8, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(panel_9, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addComponent(calcBtn, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addGap(17))
				);

		//JLabel netPresentValue = new JLabel("");
		netPresentValue.setHorizontalAlignment(SwingConstants.LEFT);
		panel_3.add(netPresentValue);
		gl_panel_4.setAutoCreateContainerGaps(true);
		panel_4.setLayout(gl_panel_4);
		panel_4.setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{panel, lblNewLabel, projectName, panel_1, lblLength, projectLength, lblYears, panel_2, lblDiscountRate, discountRate, label_3, scrollPane, panel_3, lblNetPresentValue}));

		JPanel panel_5 = new JPanel();
		tabbedPane.addTab("FW+PW", null, panel_5, null);
		panel_5.setLayout(null);

		JButton btnCalcFVPV = new JButton("PV<== FV");
		btnCalcFVPV.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				float tmpDiscountRate=Float.valueOf(discountRateTab2.getText());
				int years=Integer.valueOf(numOfYears.getText());
				float presentVal=Float.valueOf(presentValueTab2.getText());
				float futureVal=Float.valueOf(futureValueTab2.getText());
				if(futureVal>=-1)
				{
					presentVal=NPV(String.valueOf(tmpDiscountRate), futureVal, years);
					presentValueTab2.setText(String.valueOf(presentVal));
				}
				return;
			}

		}
				);
		btnCalcFVPV.setBounds(302, 245, 89, 23);
		panel_5.add(btnCalcFVPV);

		discountRateTab2 = new JTextField();
		discountRateTab2.setHorizontalAlignment(SwingConstants.RIGHT);
		discountRateTab2.setText("0");
		discountRateTab2.setBounds(305, 152, 86, 20);
		panel_5.add(discountRateTab2);
		discountRateTab2.setColumns(10);

		presentValueTab2.setText("0");
		futureValueTab2.setText("0");
		//		presentValueTab2.addInputMethodListener(new InputMethodListener() {
		//			public void caretPositionChanged(InputMethodEvent event) {
		//				futureValueTab2.setText("0");
		//			}
		//			public void inputMethodTextChanged(InputMethodEvent event) {
		//				futureValueTab2.setText("0");
		//			}
		//		});
		//		presentValueTab2.getDocument().addDocumentListener(new DocumentListener() {
		//			@Override
		//			public void insertUpdate(DocumentEvent e) {
		//				if(presentValueTab2.getText()!="0"&&presentValueTab2.getText().length()>0)
		//					futureValueTab2.setText("0");
		//			}           
		//			@Override
		//			public void removeUpdate(DocumentEvent e) {/* do nothing */
		//				if(presentValueTab2.getText()!="0"&&presentValueTab2.getText().length()>0)
		//					futureValueTab2.setText("0");
		//			}
		//			@Override
		//			public void changedUpdate(DocumentEvent e) { /* do nothing */ 
		//				if(presentValueTab2.getText()!="0"&&presentValueTab2.getText().length()>0)
		//					futureValueTab2.setText("0");
		//			}
		//		});

		presentValueTab2.setBounds(198, 225, 86, 20);
		panel_5.add(presentValueTab2);
		presentValueTab2.setColumns(10);


		//		futureValueTab2.addInputMethodListener(new InputMethodListener() {
		//			public void caretPositionChanged(InputMethodEvent arg0) {
		//
		//				presentValueTab2.setText("0");
		//			}
		//			public void inputMethodTextChanged(InputMethodEvent arg0) {
		//				presentValueTab2.setText("0");
		//			}
		//		});
		//		futureValueTab2.getDocument().addDocumentListener(new DocumentListener() {
		//			@Override
		//			public void insertUpdate(DocumentEvent e) {
		//				if(futureValueTab2.getText()!="0"&&futureValueTab2.getText().length()>0)
		//					presentValueTab2.setText("0");
		//			}           
		//			@Override
		//			public void removeUpdate(DocumentEvent e) {/* do nothing */
		//				if(futureValueTab2.getText()!="0"&&futureValueTab2.getText().length()>0)
		//					presentValueTab2.setText("0");
		//			}
		//			@Override
		//			public void changedUpdate(DocumentEvent e) { /* do nothing */ 
		//				if(futureValueTab2.getText()!="0"&&futureValueTab2.getText().length()>0)
		//					presentValueTab2.setText("0");
		//			}
		//		});

		futureValueTab2.setBounds(407, 225, 86, 20);
		panel_5.add(futureValueTab2);
		futureValueTab2.setColumns(10);

		JLabel lblNewLabel_1 = new JLabel("Present value:");
		lblNewLabel_1.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		lblNewLabel_1.setBounds(199, 200, 100, 20);
		panel_5.add(lblNewLabel_1);

		JLabel lblNewLabel_2 = new JLabel("Future value:");
		lblNewLabel_2.setBounds(406, 200, 100, 20);
		panel_5.add(lblNewLabel_2);

		JLabel lblDiscountRate_1 = new JLabel("Discount rate:");
		lblDiscountRate_1.setBounds(305, 127, 100, 20);
		panel_5.add(lblDiscountRate_1);

		numOfYears = new JTextField();
		numOfYears.setHorizontalAlignment(SwingConstants.RIGHT);
		numOfYears.setText("0");
		numOfYears.setColumns(10);
		numOfYears.setBounds(305, 103, 86, 20);
		panel_5.add(numOfYears);

		JLabel lblNumOfYears = new JLabel("Num of years:");
		lblNumOfYears.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		lblNumOfYears.setBounds(306, 75, 100, 20);
		panel_5.add(lblNumOfYears);

		JButton btnCalcPvFv = new JButton("PV ==>FV");
		btnCalcPvFv.setVerticalAlignment(SwingConstants.BOTTOM);
		btnCalcPvFv.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				float tmpDiscountRate=Float.valueOf(discountRateTab2.getText())/100;
				int years=Integer.valueOf(numOfYears.getText());
				float presentVal=Float.valueOf(presentValueTab2.getText());
				float futureVal=Float.valueOf(futureValueTab2.getText());
				if(presentVal>=-1)
				{
					futureVal=FloatWithTwoNumberDecimal(FV(tmpDiscountRate, presentVal, years));
					futureValueTab2.setText(String.valueOf(futureVal));
				}
			}
		});


		btnCalcPvFv.setBounds(302, 210, 89, 23);
		panel_5.add(btnCalcPvFv);
		contentPane.setLayout(gl_contentPane);
		contentPane.setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{panel, lblNewLabel, projectName, panel_1, lblLength, projectLength, lblYears, panel_2, lblDiscountRate, discountRate, label_3, scrollPane}));

		projectLength.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent arg0) {
				totalJLabels.clear();
				benefitJTextFields.clear();
				costJTextFields.clear();

				discountedBenefitJLabels.clear();
				discountedCostJLabels.clear();

				cashFlowJLabels.clear();
				if(projectLength.getText().length()<1) return;
				int yearCount=Integer.valueOf(projectLength.getText());
				System.out.println(yearCount);
				JPanel jp=new JPanel();
				jp.setLayout(new GridLayout(6, yearCount+2,20,10));
				for(int i=0;i<yearCount+1;i++){
					JLabel year=null;
					if(i!=0)
						year = new JLabel("Year: "+(i));
					else {
						year = new JLabel("   ");
					}
					//year.setMinimumSize();
					year.setHorizontalAlignment(SwingConstants.CENTER);
					jp.add(year);
				}

				jp.add(new JLabel("Total"));



				jp.add(new JLabel("Benefit($):"));

				for(int i=0;i<yearCount;i++){
					JTextField bef = new JTextField("0");
					//year.setMinimumSize();
					bef.setHorizontalAlignment(SwingConstants.RIGHT);
					benefitJTextFields.add(bef);
					jp.add(bef);
				}
				JLabel tmp=new JLabel("0");	
				totalJLabels.add(tmp);
				jp.add(tmp);

				jp.add(new JLabel("Discounted Benefit($):"));

				for(int i=0;i<yearCount;i++){
					JLabel bef = new JLabel("0");
					//year.setMinimumSize();
					bef.setHorizontalAlignment(SwingConstants.CENTER);
					discountedBenefitJLabels.add(bef);
					jp.add(bef);
				}

				tmp=new JLabel("0");	
				totalDiscountedJLabels.add(tmp);
				jp.add(tmp);

				jp.add(new JLabel("Cost($):"));
				for(int i=0;i<yearCount;i++){
					JTextField cost = new JTextField("0");
					//year.setMinimumSize();
					cost.setHorizontalAlignment(SwingConstants.RIGHT);
					costJTextFields.add(cost);
					jp.add(cost);
				}
				tmp= new JLabel("0");
				totalJLabels.add(tmp);
				jp.add(tmp);

				jp.add(new JLabel("Discounted Cost($):"));
				for(int i=0;i<yearCount;i++){
					JLabel dCost = new JLabel("0");
					//year.setMinimumSize();
					dCost.setHorizontalAlignment(SwingConstants.CENTER);
					discountedCostJLabels.add(dCost);
					jp.add(dCost);
				}

				tmp=new JLabel("0");	
				totalDiscountedJLabels.add(tmp);
				jp.add(tmp);

				jp.add(new JLabel("Cash Flow($):"));

				for(int i=0;i<yearCount;i++){
					JLabel cf = new JLabel("0");
					//year.setMinimumSize();
					cf.setHorizontalAlignment(SwingConstants.CENTER);
					cashFlowJLabels.add(cf);
					jp.add(cf);
				}
				tmp=new JLabel("0");
				totalJLabels.add(tmp);
				jp.add(tmp);
				scrollPane.setViewportView(jp);
			}
		});
	}
}
