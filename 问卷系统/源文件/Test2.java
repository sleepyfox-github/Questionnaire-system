import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.NumberFormat;
import java.util.*;
import javax.swing.*;
 
@SuppressWarnings("serial")
public class Test2 extends JFrame implements ActionListener{
  
 private JButton start,commit,back,next;
 private JRadioButton aButton,bButton,cButton,dButton;
 private ButtonGroup buttonGroup;
 private JLabel label,clock;
 private JTextArea jTextArea;
 private JPanel panel,panel2,panel3;
 Testquestion t1;
 Testquestion[] questions;
 int examtime;
 int p=0;//设置题目数指针  
 int topicnum=10;
 int right,error;       //答对和答错
 ClockDispaly mt;       //倒计时模块
  
 public Test2(){
  
 this.setTitle("问卷系统");     //设置标题
 this.setSize(440,320);      //设置窗口大小
 this.setLocationRelativeTo(null);    //设置显示位置居中
 this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  //设置关闭时关闭
  
 panel = new JPanel();      //初始化面板
 panel2 = new JPanel();
 panel3 = new JPanel();
 label = new JLabel("总考试时间:10分钟 ");    //初始化并命名标签
 clock = new JLabel();
 jTextArea = new JTextArea(10,35);    //初始化文本区域
 jTextArea.setEditable(false);     //设置文本不可修改
  
 aButton = new JRadioButton("A");    //初始化单选按钮
 bButton = new JRadioButton("B");
 cButton = new JRadioButton("C");
 dButton = new JRadioButton("D");
 buttonGroup = new ButtonGroup();     //初始化选项组
  
 start = new JButton("开始考试");     //初始化按键
 back = new JButton("上一题");
 next = new JButton("下一题");
 commit = new JButton("提交考试");
  
 aButton.addActionListener(this);    //单选按钮添加监听事件
 bButton.addActionListener(this);
 cButton.addActionListener(this);
 dButton.addActionListener(this);
  
 start.addActionListener(this);    //按钮添加监听事件
 back.addActionListener(this);
 next.addActionListener(this);
 commit.addActionListener(this);
  
  
 buttonGroup.add(aButton);     //把单选按钮放到选项组
 buttonGroup.add(bButton);
 buttonGroup.add(cButton);
 buttonGroup.add(dButton);
  
 panel.add(label);      //把标签放入面板panel
 panel.add(clock);
 panel.add(start);      //把按键放入面板panel
 panel2.add(jTextArea);     //把文本区域放入面板panel2
 panel3.add(aButton);      //把单选按钮放入面板panel3
 panel3.add(bButton);
 panel3.add(cButton);
 panel3.add(dButton);
 panel3.add(back);      //把按键放入面板panel3
 panel3.add(next);
 panel3.add(commit); 
  
 this.add(panel,BorderLayout.NORTH);    //设置面板panel放在上面
 this.add(panel2,BorderLayout.CENTER);    //设置面板panel2放在中间
 this.add(panel3, BorderLayout.SOUTH);    //设置面板panel放在下面
  
 this.setVisible(true);     //设置窗口可见
  
 mt = new ClockDispaly(clock, 10);    //调用并设置倒计时的时间
 }
  
 public void createExam() {//创建考试模块
 Vector<Testquestion> qList=null;//创建一个向量列表，用于动态增加试题
 Testquestion t;
 String questionText="";
 String standardKey;
 String s;
 try {
 FileReader fr=new FileReader("题目.txt"); 
 BufferedReader br = new BufferedReader(fr); //可以每次读一行 
 qList=new Vector<Testquestion>();
 while((s=br.readLine())!=null){//读取试题
 if (s.equals("*****")){
  questionText="";//准备接收一个题目的内容
  s = br.readLine();//获取试题内容的首行
   
 }
 if (s.equals("$$$$$")){//准备读取试题的答案
  s = br.readLine(); //获取试题的答案
  standardKey = s; //把试题答案赋值给正确答案 
  t = new Testquestion(questionText,standardKey); //把试题和答案赋值给t
  qList.add(t);     //把试题和答案赋值给列表
 }
 questionText=questionText+s+'\n';
  
 }
 br.close();//关闭缓冲流
 fr.close();//关闭文件流
  
 } 
 catch (IOException e) { 
 e.printStackTrace(); //打印异常信息
 }
 topicnum=qList.size();  //统计试题数量
 questions=new Testquestion[topicnum];
 for (int i=0;i<qList.size();i++) //读取试题
 questions[i]=qList.get(i);
  
 }
  
 public void setSelected(String s) {//设置单选按钮不重复模块
 if (s.equals("A")) buttonGroup.setSelected(aButton.getModel(), true);
 if (s.equals("B")) buttonGroup.setSelected(bButton.getModel(), true);
 if (s.equals("C")) buttonGroup.setSelected(cButton.getModel(), true);
 if (s.equals("D")) buttonGroup.setSelected(dButton.getModel(), true);
 if (s.equals("")) buttonGroup.clearSelection();
 }
  
 public void showQuestion() {//设置试题模块
 jTextArea.setText("");
 jTextArea.append(questions[p].getQuestionText());//在文本区域显示试题
 setSelected(questions[p].getSelectKey());
 }
  
 public void showScore() {//设置成绩模块
 right=0;error=0;
 for (int i = 0; i < topicnum; i++) {
 if (questions[i].check()) {//判断答案的正确与错误
 right++;
 }else {
 error++;
 }
 }
 int score = (int)(right*100/topicnum);  //设置分数
 JOptionPane.showMessageDialog(null, "答对"+right+"题，答错"+error+"题，分数为"+score);
 }
  
 
 @Override
 public void actionPerformed(ActionEvent e) {//动作监听事件
  
 if (e.getSource()==start) {//开始开始按键实现
 createExam();  //调用createExam模块
 p=0;   //题目序号
 showQuestion(); //调用showQuestion模块
 start.setEnabled(false);//设置按钮不可点击
 mt.start();  //考试时间倒计时启动
 }
 if (e.getSource()==back) {//上一题的按键实现
 p--;
 if (p==-1) {
 JOptionPane.showMessageDialog(null, "已经是第一题");
 p++;
 }
 showQuestion();
 }
 if (e.getSource()==next) {//下一题的按键实现
 p++;
 if (p==topicnum) {
 JOptionPane.showMessageDialog(null, "已经是最后一题");
 p--;
 }
 showQuestion();
 }
 if (e.getSource()==commit) {//提交试卷的按键实现
 showScore();
 commit.setEnabled(false);
 System.exit(0);  //退出
 }
  
 if(e.getSource()==aButton) questions[p].setSelectKey("A");
 if(e.getSource()==bButton) questions[p].setSelectKey("B");
 if(e.getSource()==cButton) questions[p].setSelectKey("C");
 if(e.getSource()==dButton) questions[p].setSelectKey("D");
  
 }
  
 public static void main(String[] args) {
 new Test2();
 }
}
 
class ClockDispaly extends Thread{//设置Thread考试倒计时模块
  
 private JLabel lefttimer;
 private int testtime;
  
 public ClockDispaly(JLabel lt,int time) {
 lefttimer = lt;
 testtime = time * 60;
 }
 public void run(){
 NumberFormat numberFormat = NumberFormat.getInstance();//控制时间的显示格式
 numberFormat.setMinimumIntegerDigits(2);//设置数值的整数部分允许的最小位数
 int h,m,s;//定义时分秒
 while (testtime >= 0) {
 h = testtime / 3600;
 m = testtime % 3600 / 60;
 s = testtime % 60;
 StringBuffer stringBuffer = new StringBuffer("");
 //增加到lefttimer标签
 stringBuffer.append("考试剩余时间为："+numberFormat.format(h)+":"+numberFormat.format(m)+":"+numberFormat.format(s));
 lefttimer.setText(stringBuffer.toString());
 try {
 Thread.sleep(1000);//延时一秒
 } catch (Exception e) {
 //ignore error
 }
 testtime = testtime - 1; 
 }
 if (testtime <= 0) {
 JOptionPane.showMessageDialog(null, "考试结束");
 System.exit(0);
 }
 }
}