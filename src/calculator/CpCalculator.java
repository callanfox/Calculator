
package calculator;

import java.awt.*;
import java.text.DecimalFormat;

/**
 * @author Callan Fox - Student Number 92016097
 */

public class CpCalculator extends javax.swing.JFrame {
    
    //Creates a model class to contain data and calculate input from our view-controller class.
    CpCalculatorModel calcModel = new CpCalculatorModel();
    
    //Array of all swing objects on the panBasic JPanel.
    Component[] arrayOfBasicComponents;
    
    //Array of all swing objects on the panExtended JPanel.
    Component[] arrayOfExtendedComponents;
    
    //Represents selected rounding precision.
    private String formatString = "#.##";
    
    //Formats all text directed to txtInput to selected rounding precision.
    private DecimalFormat rounder = new DecimalFormat(formatString);
    
    //Flag indicating whether or not a decimal point has already been input.
    private boolean isDecimalEntered = false;
    
    //Flag indicating whether or not an operator has been input.
    private boolean isOperatorEntered = false;
    
    //Flag indicating whether or not the Degrees option is selected. If not, then Radians is selected.
    private boolean isDegreesSelected = true;

    //Flag indicating whether or not the equals button was the last input.
    private boolean isEqualsSet = false;
    
    /**
     * Creates new form CpCalculator.
     */
    public CpCalculator() {
        initComponents();
        txtInput.setText(rounder.format(0.0));
        arrayOfBasicComponents  = panBasic.getComponents();
        arrayOfExtendedComponents = panExtended.getComponents();
        panExtended.setVisible(false);
        convertFont(Font.PLAIN);
        setSize(380, 400);
    }
    
    /*** 
     * Converts all components on basic and extended panels to font style specified in newWeight parameter.
     * @param newWeight 
     */
    public void convertFont(int newWeight) {
        Font oldFont = getContentPane().getFont();
        Font newFont = new Font(oldFont.getName(), newWeight, oldFont.getSize());
        for(Component comp : arrayOfBasicComponents) {
            comp.setFont(newFont);
        }
        for(Component comp : arrayOfExtendedComponents) {
            comp.setFont(newFont);
        }
        Font txtInputFont = new Font("Arial", newWeight, 28);
        txtInput.setFont(txtInputFont);
        Font lblCurrentOperatorFont = new Font("Arial", newWeight, 14);
        lblCurrentOperator.setFont(lblCurrentOperatorFont);
    }
    
    /***
     * Rounds value in CpCalculator txtInput field to selected rounding precision.
     */
    public void round() {
        rounder.applyPattern(formatString);
        if(!calcModel.getPrimaryValueSet())
        {
            calcModel.setPrimaryValue(Double.parseDouble(txtInput.getText()));
            calcModel.setPrimaryValueSet(true);
            isOperatorEntered = true;
            calcModel.setOperatorSet(true);
        }
        txtInput.setText(rounder.format(calcModel.getPrimaryValue()));
    }
       
    /***
     * Adds value of last numerical button pressed to the CpCalculator input text field.
     * @param valueEntered 
     */
    public void setNumber(int valueEntered) {
        String value = String.valueOf(valueEntered);
        if(isEqualsSet) {
            txtInput.setText(value);
            lblCurrentOperator.setText("");
            isEqualsSet = false;
            calcModel.isPrimaryValueSet = false;
            isOperatorEntered = false;
        }
        else if(txtInput.getText().equals("0") || isOperatorEntered) {
            txtInput.setText(value);
            isOperatorEntered = false;
        }   
        else {
            txtInput.setText(txtInput.getText() + value);
        }
        calcModel.setOperatorSet(false);
    }

    /***
     * Adds selected constant (either PI or E) to CpCalculator input text field.
     * @param value 
     */
    public void setConstant(double value) {
        if(isEqualsSet) {
            txtInput.setText(String.valueOf(rounder.format(value)));
            lblCurrentOperator.setText("");
        }
        else if(txtInput.getText().equals("0") || isOperatorEntered) {
            if(!calcModel.isPrimaryValueSet) {
                calcModel.setPrimaryValue(value);
                calcModel.setPrimaryValueSet(true);
                isOperatorEntered = true;
                calcModel.setOperatorSet(true);
            }
            else {
                calcModel.setNewValue(value);
                calcModel.setNewValueSet(true);
                isOperatorEntered = false;
                calcModel.setOperatorSet(false);
            }
            txtInput.setText(rounder.format(value));    
        } 
    }
    
    /***
     * Adds decimal place to CpCalculator input text field (only if no decimal place has already been entered).
     */
    public void setDecimal() {
        if(isEqualsSet) {
            txtInput.setText(".");
            lblCurrentOperator.setText("");
            isEqualsSet = false;
            calcModel.isPrimaryValueSet = false;
            isOperatorEntered = false;
        }
        else if(txtInput.getText().equals("0") || isOperatorEntered) {
           txtInput.setText(".");
           isDecimalEntered = true;
           isOperatorEntered = false;
        }
        else if(!isDecimalEntered) {
            txtInput.setText(txtInput.getText() + ".");
            isDecimalEntered = true;
        }
    }

    /***
     * Updates CpCalculatorModel values -  will invoke CpCalculatorModel calculate method given appropriate boolean values
     * and update CpCalculator input text field.
     * 
     * @param operator
     * @param txtInputValue 
     */
    public void modelUpdate(String operator, double txtInputValue) {
        //Sets appropriate values and view when last button pressed was '=' button.
        if(isEqualsSet) {
            calcModel.setPrimaryValue(txtInputValue);
            calcModel.setPrimaryValueSet(true);
            calcModel.setNewValue(0.0);
            calcModel.setNewValueSet(false);
            calcModel.setOperator(operator);
            calcModel.setOperatorSet(true);
            isOperatorEntered = true;
            isDecimalEntered = false;
            isEqualsSet = false;
            lblCurrentOperator.setText(rounder.format(calcModel.getPrimaryValue())  + " " + operator);  
        }
        //Sets appropriate values and view when initial value has yet to be set.
        else if(!calcModel.isPrimaryValueSet) {
            calcModel.setPrimaryValue(txtInputValue);
            calcModel.setOperator(operator);
            calcModel.setPrimaryValueSet(true);
            calcModel.setOperatorSet(true);
            isOperatorEntered = true;
            isDecimalEntered = false;
            lblCurrentOperator.setText(rounder.format(calcModel.getPrimaryValue())  + " " + operator);
        }
        //Sets appropriate values and view when user selects another operator when first value set.
        else if(calcModel.isPrimaryValueSet && calcModel.getOperatorSet() && !calcModel.isNewValueSet) {
            calcModel.setOperator(operator);
            lblCurrentOperator.setText(rounder.format(txtInputValue)  + " " + operator);
        }
        //Sets appropriate values and view when user selects a second value, with conditions set to perform a calculation.
        else if (calcModel.isPrimaryValueSet && !calcModel.getOperatorSet()) {
            calcModel.setNewValue(txtInputValue);
            calcModel.setNewValueSet(true);
            calcModel.setNextOperator(operator);
            try {
                lblCurrentOperator.setText(lblCurrentOperator.getText() + " " + rounder.format(txtInputValue) + " " + operator);
                txtInput.setText(String.valueOf(calcModel.calculate()));
            }
            catch (ArithmeticException ae) {
                msgErrorMessage.showMessageDialog(this, "ERROR: Divide by zero.", "ERROR", javax.swing.JOptionPane.ERROR_MESSAGE);
                calcModel.setPrimaryValue(0.0);
                calcModel.setNewValue(0.0);
                calcModel.setPrimaryValueSet(false);
                calcModel.setNewValueSet(false);
                txtInput.setText("0");
                lblCurrentOperator.setText("");
            }
            isOperatorEntered = true;
            isDecimalEntered = false;
        }
        //Sets view to the latest value of CpCalculatorModel's value1.
        txtInput.setText(rounder.format(calcModel.getPrimaryValue()));
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        grpAngle = new javax.swing.ButtonGroup();
        grpView = new javax.swing.ButtonGroup();
        grpFont = new javax.swing.ButtonGroup();
        grpRounding = new javax.swing.ButtonGroup();
        msgAboutMessage = new javax.swing.JOptionPane();
        msgHotKeyMessage = new javax.swing.JOptionPane();
        msgErrorMessage = new javax.swing.JOptionPane();
        panBasic = new javax.swing.JPanel();
        txtInput = new javax.swing.JTextField();
        btn1 = new javax.swing.JButton();
        btn2 = new javax.swing.JButton();
        btn3 = new javax.swing.JButton();
        btn4 = new javax.swing.JButton();
        btn5 = new javax.swing.JButton();
        btn6 = new javax.swing.JButton();
        btn9 = new javax.swing.JButton();
        btn8 = new javax.swing.JButton();
        btn7 = new javax.swing.JButton();
        btn0 = new javax.swing.JButton();
        btnDecimal = new javax.swing.JButton();
        btnEquals = new javax.swing.JButton();
        btnSubtract = new javax.swing.JButton();
        btnAdd = new javax.swing.JButton();
        btnDivide = new javax.swing.JButton();
        btnMultiply = new javax.swing.JButton();
        btnFactorial = new javax.swing.JButton();
        btnPlusMinus = new javax.swing.JButton();
        btnExponent = new javax.swing.JButton();
        btnReciprocal = new javax.swing.JButton();
        btnCA = new javax.swing.JButton();
        btnCE = new javax.swing.JButton();
        lblCurrentOperator = new javax.swing.JLabel();
        panExtended = new javax.swing.JPanel();
        btnAsin = new javax.swing.JButton();
        btnPi = new javax.swing.JButton();
        btnTan = new javax.swing.JButton();
        btnCos = new javax.swing.JButton();
        btnSin = new javax.swing.JButton();
        btnE = new javax.swing.JButton();
        btnAtan = new javax.swing.JButton();
        btnAcos = new javax.swing.JButton();
        radDegrees = new javax.swing.JRadioButton();
        radRadians = new javax.swing.JRadioButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        mnuView = new javax.swing.JMenu();
        mnuBasic = new javax.swing.JCheckBoxMenuItem();
        mnuExtended = new javax.swing.JCheckBoxMenuItem();
        mnuFont = new javax.swing.JMenu();
        mnuPlain = new javax.swing.JCheckBoxMenuItem();
        mnuBold = new javax.swing.JCheckBoxMenuItem();
        mnuRound = new javax.swing.JMenu();
        mnu0 = new javax.swing.JRadioButtonMenuItem();
        mnu1 = new javax.swing.JRadioButtonMenuItem();
        mnu2 = new javax.swing.JRadioButtonMenuItem();
        mnu3 = new javax.swing.JRadioButtonMenuItem();
        mnu4 = new javax.swing.JRadioButtonMenuItem();
        mnu5 = new javax.swing.JRadioButtonMenuItem();
        mnu6 = new javax.swing.JRadioButtonMenuItem();
        mnu7 = new javax.swing.JRadioButtonMenuItem();
        mnu8 = new javax.swing.JRadioButtonMenuItem();
        mnu9 = new javax.swing.JRadioButtonMenuItem();
        mnuHelp = new javax.swing.JMenu();
        mnuHotKeys = new javax.swing.JMenuItem();
        mnuAbout = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("CpCalculator");
        setBackground(new java.awt.Color(255, 255, 255));
        setResizable(false);
        getContentPane().setLayout(null);

        panBasic.setBackground(new java.awt.Color(255, 255, 255));

        txtInput.setEditable(false);
        txtInput.setBackground(new java.awt.Color(255, 255, 255));
        txtInput.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtInput.setBorder(null);

        btn1.setText("1");
        btn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn1ActionPerformed(evt);
            }
        });

        btn2.setText("2");
        btn2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn2ActionPerformed(evt);
            }
        });

        btn3.setText("3");
        btn3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn3ActionPerformed(evt);
            }
        });

        btn4.setText("4");
        btn4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn4ActionPerformed(evt);
            }
        });

        btn5.setText("5");
        btn5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn5ActionPerformed(evt);
            }
        });

        btn6.setText("6");
        btn6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn6ActionPerformed(evt);
            }
        });

        btn9.setText("9");
        btn9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn9ActionPerformed(evt);
            }
        });

        btn8.setText("8");
        btn8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn8ActionPerformed(evt);
            }
        });

        btn7.setText("7");
        btn7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn7ActionPerformed(evt);
            }
        });

        btn0.setText("0");
        btn0.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn0ActionPerformed(evt);
            }
        });

        btnDecimal.setText(".");
        btnDecimal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDecimalActionPerformed(evt);
            }
        });

        btnEquals.setText("=");
        btnEquals.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEqualsActionPerformed(evt);
            }
        });

        btnSubtract.setText("-");
        btnSubtract.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSubtractActionPerformed(evt);
            }
        });

        btnAdd.setText("+");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        btnDivide.setText("/");
        btnDivide.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDivideActionPerformed(evt);
            }
        });

        btnMultiply.setText("*");
        btnMultiply.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMultiplyActionPerformed(evt);
            }
        });

        btnFactorial.setText("x!");
        btnFactorial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFactorialActionPerformed(evt);
            }
        });

        btnPlusMinus.setText("+/-");
        btnPlusMinus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPlusMinusActionPerformed(evt);
            }
        });

        btnExponent.setText("x^n");
        btnExponent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExponentActionPerformed(evt);
            }
        });

        btnReciprocal.setText("1/x");
        btnReciprocal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReciprocalActionPerformed(evt);
            }
        });

        btnCA.setText("CA");
        btnCA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCAActionPerformed(evt);
            }
        });

        btnCE.setText("CE");
        btnCE.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCEActionPerformed(evt);
            }
        });

        lblCurrentOperator.setForeground(new java.awt.Color(153, 153, 153));
        lblCurrentOperator.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

        javax.swing.GroupLayout panBasicLayout = new javax.swing.GroupLayout(panBasic);
        panBasic.setLayout(panBasicLayout);
        panBasicLayout.setHorizontalGroup(
            panBasicLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panBasicLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panBasicLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panBasicLayout.createSequentialGroup()
                        .addGroup(panBasicLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panBasicLayout.createSequentialGroup()
                                .addComponent(btnCA, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnCE, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panBasicLayout.createSequentialGroup()
                                .addGroup(panBasicLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panBasicLayout.createSequentialGroup()
                                        .addComponent(btn4, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(btn5, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(btn6, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(panBasicLayout.createSequentialGroup()
                                        .addComponent(btn1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(btn2, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(btn3, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(panBasicLayout.createSequentialGroup()
                                        .addComponent(btn7, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(btn8, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(btn9, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(panBasicLayout.createSequentialGroup()
                                        .addComponent(btn0, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(btnDecimal, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(btnEquals, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(18, 18, 18)
                                .addGroup(panBasicLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnDivide, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnMultiply, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnSubtract, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(panBasicLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnReciprocal, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnPlusMinus, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnFactorial, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnExponent)))
                            .addComponent(txtInput, javax.swing.GroupLayout.Alignment.LEADING))
                        .addContainerGap(33, Short.MAX_VALUE))
                    .addGroup(panBasicLayout.createSequentialGroup()
                        .addComponent(lblCurrentOperator, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(42, 42, 42))))
        );

        panBasicLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btn0, btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btnAdd, btnDecimal, btnDivide, btnEquals, btnExponent, btnFactorial, btnMultiply, btnPlusMinus, btnReciprocal, btnSubtract});

        panBasicLayout.setVerticalGroup(
            panBasicLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panBasicLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblCurrentOperator, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtInput, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panBasicLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panBasicLayout.createSequentialGroup()
                        .addGroup(panBasicLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btn1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn2, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn3, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(15, 15, 15)
                        .addGroup(panBasicLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btn4, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn5, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn6, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(panBasicLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btn7, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn8, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn9, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(panBasicLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btn0, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnDecimal, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnEquals, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(panBasicLayout.createSequentialGroup()
                        .addComponent(btnMultiply, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(15, 15, 15)
                        .addComponent(btnDivide, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnSubtract, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panBasicLayout.createSequentialGroup()
                        .addComponent(btnReciprocal, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(15, 15, 15)
                        .addComponent(btnExponent, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnPlusMinus, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnFactorial, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(panBasicLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCA, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCE, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(58, Short.MAX_VALUE))
        );

        getContentPane().add(panBasic);
        panBasic.setBounds(0, 0, 380, 360);

        panExtended.setBackground(new java.awt.Color(255, 255, 255));

        btnAsin.setText("asin");
        btnAsin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAsinActionPerformed(evt);
            }
        });

        btnPi.setText("PI");
        btnPi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPiActionPerformed(evt);
            }
        });

        btnTan.setText("tan");
        btnTan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTanActionPerformed(evt);
            }
        });

        btnCos.setText("cos");
        btnCos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCosActionPerformed(evt);
            }
        });

        btnSin.setText("sin");
        btnSin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSinActionPerformed(evt);
            }
        });

        btnE.setText("E");
        btnE.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEActionPerformed(evt);
            }
        });

        btnAtan.setText("atan");
        btnAtan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAtanActionPerformed(evt);
            }
        });

        btnAcos.setText("acos");
        btnAcos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAcosActionPerformed(evt);
            }
        });

        radDegrees.setBackground(new java.awt.Color(255, 255, 255));
        grpAngle.add(radDegrees);
        radDegrees.setSelected(true);
        radDegrees.setText("Degrees");
        radDegrees.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                radDegreesItemStateChanged(evt);
            }
        });

        radRadians.setBackground(new java.awt.Color(255, 255, 255));
        grpAngle.add(radRadians);
        radRadians.setText("Radians");
        radRadians.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                radRadiansItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout panExtendedLayout = new javax.swing.GroupLayout(panExtended);
        panExtended.setLayout(panExtendedLayout);
        panExtendedLayout.setHorizontalGroup(
            panExtendedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panExtendedLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panExtendedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panExtendedLayout.createSequentialGroup()
                        .addGroup(panExtendedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnCos, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnSin, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnTan, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnPi, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(panExtendedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnAcos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnAsin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnAtan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(panExtendedLayout.createSequentialGroup()
                        .addComponent(radDegrees)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(radRadians)))
                .addContainerGap(40, Short.MAX_VALUE))
        );

        panExtendedLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnAcos, btnAsin, btnAtan, btnCos, btnE, btnPi, btnSin, btnTan});

        panExtendedLayout.setVerticalGroup(
            panExtendedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panExtendedLayout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(panExtendedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(radDegrees)
                    .addComponent(radRadians))
                .addGap(18, 18, 18)
                .addGroup(panExtendedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panExtendedLayout.createSequentialGroup()
                        .addComponent(btnSin, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(15, 15, 15)
                        .addComponent(btnCos, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnTan, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnPi, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panExtendedLayout.createSequentialGroup()
                        .addComponent(btnAsin, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(15, 15, 15)
                        .addComponent(btnAcos, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnAtan, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnE, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(116, Short.MAX_VALUE))
        );

        getContentPane().add(panExtended);
        panExtended.setBounds(380, 0, 180, 370);

        jMenuBar1.setBackground(new java.awt.Color(255, 255, 255));

        mnuView.setMnemonic('v');
        mnuView.setText("View");

        grpView.add(mnuBasic);
        mnuBasic.setMnemonic('b');
        mnuBasic.setSelected(true);
        mnuBasic.setText("Basic");
        mnuBasic.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                mnuBasicItemStateChanged(evt);
            }
        });
        mnuView.add(mnuBasic);

        grpView.add(mnuExtended);
        mnuExtended.setMnemonic('x');
        mnuExtended.setText("Extended");
        mnuExtended.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                mnuExtendedItemStateChanged(evt);
            }
        });
        mnuView.add(mnuExtended);

        jMenuBar1.add(mnuView);

        mnuFont.setMnemonic('f');
        mnuFont.setText("Font");

        grpFont.add(mnuPlain);
        mnuPlain.setMnemonic('p');
        mnuPlain.setSelected(true);
        mnuPlain.setText("Plain");
        mnuPlain.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                mnuPlainItemStateChanged(evt);
            }
        });
        mnuFont.add(mnuPlain);

        grpFont.add(mnuBold);
        mnuBold.setMnemonic('b');
        mnuBold.setText("Bold");
        mnuBold.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                mnuBoldItemStateChanged(evt);
            }
        });
        mnuFont.add(mnuBold);

        jMenuBar1.add(mnuFont);

        mnuRound.setMnemonic('r');
        mnuRound.setText("Round");

        grpRounding.add(mnu0);
        mnu0.setText("0");
        mnu0.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                mnu0ItemStateChanged(evt);
            }
        });
        mnuRound.add(mnu0);

        grpRounding.add(mnu1);
        mnu1.setText("1");
        mnu1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                mnu1ItemStateChanged(evt);
            }
        });
        mnuRound.add(mnu1);

        grpRounding.add(mnu2);
        mnu2.setSelected(true);
        mnu2.setText("2");
        mnu2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                mnu2ItemStateChanged(evt);
            }
        });
        mnuRound.add(mnu2);

        grpRounding.add(mnu3);
        mnu3.setText("3");
        mnu3.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                mnu3ItemStateChanged(evt);
            }
        });
        mnuRound.add(mnu3);

        grpRounding.add(mnu4);
        mnu4.setText("4");
        mnu4.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                mnu4ItemStateChanged(evt);
            }
        });
        mnuRound.add(mnu4);

        grpRounding.add(mnu5);
        mnu5.setText("5");
        mnu5.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                mnu5ItemStateChanged(evt);
            }
        });
        mnuRound.add(mnu5);

        grpRounding.add(mnu6);
        mnu6.setText("6");
        mnu6.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                mnu6ItemStateChanged(evt);
            }
        });
        mnuRound.add(mnu6);

        grpRounding.add(mnu7);
        mnu7.setText("7");
        mnu7.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                mnu7ItemStateChanged(evt);
            }
        });
        mnuRound.add(mnu7);

        grpRounding.add(mnu8);
        mnu8.setText("8");
        mnu8.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                mnu8ItemStateChanged(evt);
            }
        });
        mnuRound.add(mnu8);

        grpRounding.add(mnu9);
        mnu9.setText("9");
        mnu9.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                mnu9ItemStateChanged(evt);
            }
        });
        mnuRound.add(mnu9);

        jMenuBar1.add(mnuRound);

        mnuHelp.setMnemonic('h');
        mnuHelp.setText("Help");

        mnuHotKeys.setMnemonic('k');
        mnuHotKeys.setText("Hot Keys");
        mnuHotKeys.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuHotKeysActionPerformed(evt);
            }
        });
        mnuHelp.add(mnuHotKeys);

        mnuAbout.setMnemonic('a');
        mnuAbout.setText("About");
        mnuAbout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuAboutActionPerformed(evt);
            }
        });
        mnuHelp.add(mnuAbout);

        jMenuBar1.add(mnuHelp);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCAActionPerformed
        txtInput.setText(calcModel.CA());
        isDecimalEntered = false;
        isEqualsSet = false;
        lblCurrentOperator.setText("");
    }//GEN-LAST:event_btnCAActionPerformed

    private void btnCEActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCEActionPerformed
        txtInput.setText(calcModel.CE());
    }//GEN-LAST:event_btnCEActionPerformed

    private void btnAtanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAtanActionPerformed
        lblCurrentOperator.setText("atan(" + txtInput.getText() + ")");
        txtInput.setText(rounder.format(calcModel.aTan(txtInput.getText(), isDegreesSelected)));
        isEqualsSet = true;
    }//GEN-LAST:event_btnAtanActionPerformed

    private void mnuExtendedItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_mnuExtendedItemStateChanged
        if(mnuExtended.isSelected()) {
            setSize(565, 400);
            panExtended.setVisible(true);
        }
    }//GEN-LAST:event_mnuExtendedItemStateChanged

    private void mnuBasicItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_mnuBasicItemStateChanged
        if(mnuBasic.isSelected()) {
            setSize(380, 400);
            panExtended.setVisible(false);
        }
    }//GEN-LAST:event_mnuBasicItemStateChanged

private void mnuBoldItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_mnuBoldItemStateChanged
    convertFont(Font.BOLD);
}//GEN-LAST:event_mnuBoldItemStateChanged

private void mnuPlainItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_mnuPlainItemStateChanged
    convertFont(Font.PLAIN);
}//GEN-LAST:event_mnuPlainItemStateChanged

private void mnuAboutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuAboutActionPerformed
    msgAboutMessage.showMessageDialog(this, "CpCalculator\nVersion 1.2.3_456\nAugust 2015\n\nCreated by Callan Fox\nStudent No. 92016097\n\n", "Message", javax.swing.JOptionPane.INFORMATION_MESSAGE);
}//GEN-LAST:event_mnuAboutActionPerformed

private void mnuHotKeysActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuHotKeysActionPerformed
    msgAboutMessage.showMessageDialog(this, "+ - addition\n- - subtraction\n* - multiplication\n\\ - division\n\n s- sin\nS - arcsin\n c - cos\nC - arccos\nt - tan\nT - arctan\n\ni - inverse\nm - +/-\n^ - power\n! - factorial\n\nd - clear all(CA)\nD - clear entry(CE)\n\nDigits 0 - 9 can be entered from\n the keypad or the keyboard's\n digit keys.\n\n", "Hot Keys", javax.swing.JOptionPane.INFORMATION_MESSAGE);
}//GEN-LAST:event_mnuHotKeysActionPerformed

private void btn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn1ActionPerformed
    setNumber(1);
}//GEN-LAST:event_btn1ActionPerformed

private void btn2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn2ActionPerformed
    setNumber(2);
}//GEN-LAST:event_btn2ActionPerformed

private void btn3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn3ActionPerformed
    setNumber(3);
}//GEN-LAST:event_btn3ActionPerformed

private void btn4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn4ActionPerformed
    setNumber(4);
}//GEN-LAST:event_btn4ActionPerformed

private void btn5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn5ActionPerformed
    setNumber(5);
}//GEN-LAST:event_btn5ActionPerformed

private void btn6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn6ActionPerformed
    setNumber(6);
}//GEN-LAST:event_btn6ActionPerformed

private void btn7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn7ActionPerformed
    setNumber(7);
}//GEN-LAST:event_btn7ActionPerformed

private void btn8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn8ActionPerformed
    setNumber(8);
}//GEN-LAST:event_btn8ActionPerformed

private void btn9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn9ActionPerformed
    setNumber(9);
}//GEN-LAST:event_btn9ActionPerformed

private void btn0ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn0ActionPerformed
    setNumber(0);
}//GEN-LAST:event_btn0ActionPerformed

private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
    modelUpdate("+", Double.parseDouble(txtInput.getText()));
}//GEN-LAST:event_btnAddActionPerformed

private void btnSubtractActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSubtractActionPerformed
    modelUpdate("-", Double.parseDouble(txtInput.getText()));
}//GEN-LAST:event_btnSubtractActionPerformed

private void btnMultiplyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMultiplyActionPerformed
    modelUpdate("*", Double.parseDouble(txtInput.getText()));
}//GEN-LAST:event_btnMultiplyActionPerformed

private void btnDivideActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDivideActionPerformed
    modelUpdate("/", Double.parseDouble(txtInput.getText()));    
}//GEN-LAST:event_btnDivideActionPerformed

private void btnDecimalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDecimalActionPerformed
    setDecimal();
}//GEN-LAST:event_btnDecimalActionPerformed

private void btnEqualsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEqualsActionPerformed
    modelUpdate("=", Double.parseDouble(txtInput.getText()));
    isEqualsSet = true;
}//GEN-LAST:event_btnEqualsActionPerformed

private void btnReciprocalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReciprocalActionPerformed
    try {
        lblCurrentOperator.setText("1/" + txtInput.getText());
        txtInput.setText(rounder.format(calcModel.reciprocal(txtInput.getText())));
        isEqualsSet = true;
    }
    catch (ArithmeticException ae) {
        msgErrorMessage.showMessageDialog(this, "ERROR: Inverse of zero is infinity.", "ERROR", javax.swing.JOptionPane.ERROR_MESSAGE);
        calcModel.setPrimaryValue(0.0);
        calcModel.setNewValue(0.0);
        calcModel.setPrimaryValueSet(false);
        calcModel.setNewValueSet(false);
        txtInput.setText("0");
        lblCurrentOperator.setText("");
    }
}//GEN-LAST:event_btnReciprocalActionPerformed

    private void btnExponentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExponentActionPerformed
        modelUpdate("^", Double.parseDouble(txtInput.getText()));
    }//GEN-LAST:event_btnExponentActionPerformed

    private void btnPlusMinusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPlusMinusActionPerformed
        txtInput.setText(rounder.format(calcModel.plusMinus(txtInput.getText())));
    }//GEN-LAST:event_btnPlusMinusActionPerformed

    private void btnFactorialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFactorialActionPerformed
        try {
            lblCurrentOperator.setText(txtInput.getText() + "!");
            txtInput.setText(rounder.format(calcModel.factorial(txtInput.getText())));
            isEqualsSet = true;
        }
        catch (NumberFormatException nfe) {
            msgErrorMessage.showMessageDialog(this, "ERROR: Must be a positive integer between 0 and 20.", "ERROR", javax.swing.JOptionPane.ERROR_MESSAGE);
            calcModel.setPrimaryValue(0.0);
            calcModel.setNewValue(0.0);
            calcModel.setPrimaryValueSet(false);
            calcModel.setNewValueSet(false);
            txtInput.setText("0");
            lblCurrentOperator.setText("");
        }
        catch (ArithmeticException ae) {
            msgErrorMessage.showMessageDialog(this, "ERROR: Must be a positive integer between 0 and 20.", "ERROR", javax.swing.JOptionPane.ERROR_MESSAGE);
            calcModel.setPrimaryValue(0.0);
            calcModel.setNewValue(0.0);
            calcModel.setPrimaryValueSet(false);
            calcModel.setNewValueSet(false);
            txtInput.setText("0");
            lblCurrentOperator.setText("");
        }
    }//GEN-LAST:event_btnFactorialActionPerformed

    private void btnEActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEActionPerformed
        setConstant(Math.E);
    }//GEN-LAST:event_btnEActionPerformed

    private void btnPiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPiActionPerformed
        setConstant(Math.PI);
    }//GEN-LAST:event_btnPiActionPerformed

    private void btnSinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSinActionPerformed
        lblCurrentOperator.setText("sin(" + txtInput.getText() + ")");
        txtInput.setText(rounder.format(calcModel.sin(txtInput.getText(), isDegreesSelected)));
        isEqualsSet = true;
    }//GEN-LAST:event_btnSinActionPerformed

    private void radDegreesItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_radDegreesItemStateChanged
        isDegreesSelected = true;
    }//GEN-LAST:event_radDegreesItemStateChanged

    private void radRadiansItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_radRadiansItemStateChanged
        isDegreesSelected = false;
    }//GEN-LAST:event_radRadiansItemStateChanged

    private void btnAsinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAsinActionPerformed
        lblCurrentOperator.setText("asin(" + txtInput.getText() + ")");
        txtInput.setText(rounder.format(calcModel.aSin(txtInput.getText(), isDegreesSelected)));
        isEqualsSet = true;
    }//GEN-LAST:event_btnAsinActionPerformed

    private void btnCosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCosActionPerformed
        lblCurrentOperator.setText("cos(" + txtInput.getText() + ")");
        txtInput.setText(rounder.format(calcModel.cos(txtInput.getText(), isDegreesSelected)));
        isEqualsSet = true;
    }//GEN-LAST:event_btnCosActionPerformed

    private void btnAcosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAcosActionPerformed
        lblCurrentOperator.setText("acos(" + txtInput.getText() + ")");
        txtInput.setText(rounder.format(calcModel.aCos(txtInput.getText(), isDegreesSelected)));
        isEqualsSet = true;
    }//GEN-LAST:event_btnAcosActionPerformed

    private void btnTanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTanActionPerformed
        lblCurrentOperator.setText("tan(" + txtInput.getText() + ")");
        txtInput.setText(rounder.format(calcModel.tan(txtInput.getText(), isDegreesSelected)));
        isEqualsSet = true;
    }//GEN-LAST:event_btnTanActionPerformed

    private void mnu2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_mnu2ItemStateChanged
        formatString = "#.##";
        round();
    }//GEN-LAST:event_mnu2ItemStateChanged

    private void mnu1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_mnu1ItemStateChanged
        formatString = "#.#";
        round();
    }//GEN-LAST:event_mnu1ItemStateChanged

    private void mnu0ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_mnu0ItemStateChanged
        formatString = "#";
        round();
    }//GEN-LAST:event_mnu0ItemStateChanged

    private void mnu3ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_mnu3ItemStateChanged
        formatString = "#.###";
        round();
    }//GEN-LAST:event_mnu3ItemStateChanged

    private void mnu4ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_mnu4ItemStateChanged
        formatString = "#.####";
        round();
    }//GEN-LAST:event_mnu4ItemStateChanged

    private void mnu5ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_mnu5ItemStateChanged
        formatString = "#.#####";
        round();
    }//GEN-LAST:event_mnu5ItemStateChanged

    private void mnu6ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_mnu6ItemStateChanged
        formatString = "#.######";
        round();
    }//GEN-LAST:event_mnu6ItemStateChanged

    private void mnu7ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_mnu7ItemStateChanged
        formatString = "#.#######";
        round();
    }//GEN-LAST:event_mnu7ItemStateChanged

    private void mnu8ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_mnu8ItemStateChanged
        formatString = "#.########";
        round();
    }//GEN-LAST:event_mnu8ItemStateChanged

    private void mnu9ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_mnu9ItemStateChanged
        formatString = "#.#########";
        round();
    }//GEN-LAST:event_mnu9ItemStateChanged

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(CpCalculator.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CpCalculator.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CpCalculator.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CpCalculator.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new CpCalculator().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn0;
    private javax.swing.JButton btn1;
    private javax.swing.JButton btn2;
    private javax.swing.JButton btn3;
    private javax.swing.JButton btn4;
    private javax.swing.JButton btn5;
    private javax.swing.JButton btn6;
    private javax.swing.JButton btn7;
    private javax.swing.JButton btn8;
    private javax.swing.JButton btn9;
    private javax.swing.JButton btnAcos;
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnAsin;
    private javax.swing.JButton btnAtan;
    private javax.swing.JButton btnCA;
    private javax.swing.JButton btnCE;
    private javax.swing.JButton btnCos;
    private javax.swing.JButton btnDecimal;
    private javax.swing.JButton btnDivide;
    private javax.swing.JButton btnE;
    private javax.swing.JButton btnEquals;
    private javax.swing.JButton btnExponent;
    private javax.swing.JButton btnFactorial;
    private javax.swing.JButton btnMultiply;
    private javax.swing.JButton btnPi;
    private javax.swing.JButton btnPlusMinus;
    private javax.swing.JButton btnReciprocal;
    private javax.swing.JButton btnSin;
    private javax.swing.JButton btnSubtract;
    private javax.swing.JButton btnTan;
    private javax.swing.ButtonGroup grpAngle;
    private javax.swing.ButtonGroup grpFont;
    private javax.swing.ButtonGroup grpRounding;
    private javax.swing.ButtonGroup grpView;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JLabel lblCurrentOperator;
    private javax.swing.JRadioButtonMenuItem mnu0;
    private javax.swing.JRadioButtonMenuItem mnu1;
    private javax.swing.JRadioButtonMenuItem mnu2;
    private javax.swing.JRadioButtonMenuItem mnu3;
    private javax.swing.JRadioButtonMenuItem mnu4;
    private javax.swing.JRadioButtonMenuItem mnu5;
    private javax.swing.JRadioButtonMenuItem mnu6;
    private javax.swing.JRadioButtonMenuItem mnu7;
    private javax.swing.JRadioButtonMenuItem mnu8;
    private javax.swing.JRadioButtonMenuItem mnu9;
    private javax.swing.JMenuItem mnuAbout;
    private javax.swing.JCheckBoxMenuItem mnuBasic;
    private javax.swing.JCheckBoxMenuItem mnuBold;
    private javax.swing.JCheckBoxMenuItem mnuExtended;
    private javax.swing.JMenu mnuFont;
    private javax.swing.JMenu mnuHelp;
    private javax.swing.JMenuItem mnuHotKeys;
    private javax.swing.JCheckBoxMenuItem mnuPlain;
    private javax.swing.JMenu mnuRound;
    private javax.swing.JMenu mnuView;
    private javax.swing.JOptionPane msgAboutMessage;
    private javax.swing.JOptionPane msgErrorMessage;
    private javax.swing.JOptionPane msgHotKeyMessage;
    private javax.swing.JPanel panBasic;
    private javax.swing.JPanel panExtended;
    private javax.swing.JRadioButton radDegrees;
    private javax.swing.JRadioButton radRadians;
    private javax.swing.JTextField txtInput;
    // End of variables declaration//GEN-END:variables
}
