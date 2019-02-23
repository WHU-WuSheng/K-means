import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Main {

	public static Double[] Calculation(ArrayList<Double[]> datas, ArrayList<Integer> s) // 聚类中心计算函数
	{
		Double[] z = { 0.0, 0.0, 0.0, 0.0, 0.0 };
		for (int i = 0; i < s.size(); i++) {
			z[0] += datas.get(s.get(i))[0];
			z[1] += datas.get(s.get(i))[1];
			z[2] += datas.get(s.get(i))[2];
			z[3] += datas.get(s.get(i))[3];
			z[4] += datas.get(s.get(i))[4];
		}
		z[0] = z[0] / s.size();
		z[1] = z[1] / s.size();
		z[2] = z[2] / s.size();
		z[3] = z[3] / s.size();
		z[4] = z[4] / s.size();
		return z;
	}

	public static void main(String[] args) {
		ArrayList<String[]> datas = new ArrayList<>();
		try {
			FileInputStream fis = new FileInputStream("data1.txt");
			InputStreamReader isr = new InputStreamReader(fis, "gbk");
			BufferedReader br = new BufferedReader(isr);
			String line = null;
			while ((line = br.readLine()) != null) {
				String[] data = line.split("\t");
				datas.add(data);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		// 以上读取数据共798条真实数据，然后对数据进行预处理，筛选初始六个簇类中心
		// 分别是 语文*30+文综*20+数学*20+英语*20+理综*10 第1,800名
		// 数学*30+理综*20+语文*20+英语*2+文综*10 第1,800名

		ArrayList<Double[]> new_datas = new ArrayList<>();
		double Lfirst = 0.0;
		double Lfirst_buf = 0.0;// 防止第一名重合或者第二名重合
		int Lfirst_rank = 0;
		int Lfirst_rank_buf = 0;
		double Llast = 9999.0;
		double Llast_buf = 9999.0;
		int Llast_rank = 0;
		int Llast_rank_buf = 0;
		double Wfirst = 0.0;
		double Wfirst_buf = 0.0;
		int Wfirst_rank = 0;
		int Wfirst_rank_buf = 0;
		double Wlast = 9999.0;
		double Wlast_buf = 9999.0;
		int Wlast_rank = 0;
		int Wlast_rank_buf = 0;

		for (int i = 0; i < datas.size(); i++) {
			Double[] new_data = new Double[5];
			new_data[0] = Double.valueOf(datas.get(i)[0]);// 语文成绩
			new_data[1] = Double.valueOf(datas.get(i)[1]);// 数学成绩
			new_data[2] = Double.valueOf(datas.get(i)[2]);// 英语成绩
			new_data[3] = (Double.valueOf(datas.get(i)[3]) + Double.valueOf(datas.get(i)[4])
					+ Double.valueOf(datas.get(i)[5])) / 3.0;// 理综成绩
			new_data[4] = (Double.valueOf(datas.get(i)[6]) + Double.valueOf(datas.get(i)[7])
					+ Double.valueOf(datas.get(i)[8])) / 3.0;// 文综成绩
			new_datas.add(new_data);

			if ((new_data[0] * 30 + new_data[4] * 30 + new_data[1] * 15 + new_data[2] * 15
					+ new_data[3] * 10) > Wfirst) {
				Wfirst = (new_data[0] * 30 + new_data[4] * 30 + new_data[1] * 15 + new_data[2] * 15 + new_data[3] * 10);
				Wfirst_rank = i;
			} else if ((new_data[0] * 30 + new_data[4] * 30 + new_data[1] * 15 + new_data[2] * 15
					+ new_data[3] * 10) > Wfirst_buf) {
				Wfirst_buf = (new_data[0] * 30 + new_data[4] * 30 + new_data[1] * 15 + new_data[2] * 15
						+ new_data[3] * 10);
				Wfirst_rank_buf = i;
			} // 文科1 2 名

			if ((new_data[0] * 30 + new_data[4] * 30 + new_data[1] * 15 + new_data[2] * 15
					+ new_data[3] * 10) < Wlast) {
				Wlast = (new_data[0] * 30 + new_data[4] * 30 + new_data[1] * 15 + new_data[2] * 15 + new_data[3] * 10);
				Wlast_rank = i;
			} else if ((new_data[0] * 30 + new_data[4] * 30 + new_data[1] * 15 + new_data[2] * 15
					+ new_data[3] * 10) < Wlast_buf) {
				Wlast_buf = (new_data[0] * 30 + new_data[4] * 30 + new_data[1] * 15 + new_data[2] * 15
						+ new_data[3] * 10);
				Wlast_rank_buf = i;
			}

			if ((new_data[1] * 30 + new_data[3] * 30 + new_data[0] * 15 + new_data[2] * 15
					+ new_data[4] * 10) > Lfirst) {
				Lfirst = (new_data[1] * 30 + new_data[3] * 30 + new_data[0] * 15 + new_data[2] * 15 + new_data[4] * 10);
				Lfirst_rank = i;
			} else if ((new_data[1] * 30 + new_data[3] * 30 + new_data[0] * 15 + new_data[2] * 15
					+ new_data[4] * 10) > Lfirst_buf) {
				Lfirst_buf = (new_data[1] * 30 + new_data[3] * 30 + new_data[0] * 15 + new_data[2] * 15
						+ new_data[4] * 10);
				Lfirst_rank_buf = i;
			}

			if ((new_data[1] * 30 + new_data[3] * 30 + new_data[0] * 15 + new_data[2] * 15
					+ new_data[4] * 10) < Llast) {
				Llast = (new_data[1] * 30 + new_data[3] * 30 + new_data[0] * 15 + new_data[2] * 15 + new_data[4] * 10);
				Llast_rank = i;
			} else if ((new_data[1] * 30 + new_data[3] * 30 + new_data[0] * 15 + new_data[2] * 15
					+ new_data[4] * 10) < Llast_buf) {
				Llast_buf = (new_data[1] * 30 + new_data[3] * 30 + new_data[0] * 15 + new_data[2] * 15
						+ new_data[4] * 10);
				Llast_rank_buf = i;
			}
		}

		// 下面防止两个第一名和倒数第一名重复人选
		if (Lfirst_rank == Wfirst_rank) {
			// 通过自身判断更偏重文还是理
			if (new_datas.get(Lfirst_rank)[0] > new_datas.get(Lfirst_rank)[1] * 0.9)// 更偏重文科
			{
				Lfirst_rank = Lfirst_rank_buf;
			} else {
				Wfirst_rank = Wfirst_rank_buf;
			}
		}
		if (Llast_rank == Wlast_rank) {
			if (new_datas.get(Llast_rank)[0] > new_datas.get(Llast_rank)[1] * 0.9)// 更偏重文科
			{
				Llast_rank = Llast_rank_buf;
			} else {
				Wlast_rank = Wlast_rank_buf;
			}
		} // 至此四个初始簇中心已经得到,创建x数组

		/*
		 * System.out.println(new_datas.get(Lfirst_rank)[0]+" "+new_datas.get(
		 * Lfirst_rank)[1]+" "+new_datas.get(Lfirst_rank)[2]);
		 * System.out.println(new_datas.get(Llast_rank)[0]+" "+new_datas.get(Llast_rank)
		 * [1]+" "+new_datas.get(Llast_rank)[2]);
		 * System.out.println(new_datas.get(Wfirst_rank)[0]+" "+new_datas.get(
		 * Wfirst_rank)[1]+" "+new_datas.get(Wfirst_rank)[2]);
		 * System.out.println(new_datas.get(Wlast_rank)[0]+" "+new_datas.get(Wlast_rank)
		 * [1]+" "+new_datas.get(Wlast_rank)[2]);
		 */

		ArrayList<Integer> s1 = new ArrayList<Integer>();
		ArrayList<Integer> s2 = new ArrayList<Integer>();
		ArrayList<Integer> s3 = new ArrayList<Integer>();
		ArrayList<Integer> s4 = new ArrayList<Integer>();
		Double[] z1 = new_datas.get(Lfirst_rank);
		z1[1] *= 1.2;
		z1[0] *= 0.9;
		Double[] z2 = new_datas.get(Wfirst_rank);
		z2[0] *= 1.2;
		z2[1] *= 0.9;
		Double[] z3 = new_datas.get(Llast_rank);
		z3[1] *= 1.2;
		z3[0] *= 0.9;
		Double[] z4 = new_datas.get(Wlast_rank);
		z4[0] *= 1.2;
		z4[1] *= 0.9;
		// 初始聚类中心和初始簇

		while (true) {
			double test1, test2, test3, test4;
			s1.clear();
			s2.clear();
			s3.clear();
			s4.clear();
			// 使用欧氏距离
			for (int i = 0; i < new_datas.size(); i++) {
				test1 = Math.sqrt(Math.pow(new_datas.get(i)[0] - z1[0], 2) + Math.pow(new_datas.get(i)[1] - z1[1], 2)
						+ Math.pow(new_datas.get(i)[2] - z1[2], 2) + Math.pow(new_datas.get(i)[3] - z1[3], 2)
						+ Math.pow(new_datas.get(i)[4] - z1[4], 2));
				test2 = Math.sqrt(Math.pow(new_datas.get(i)[0] - z2[0], 2) + Math.pow(new_datas.get(i)[1] - z2[1], 2)
						+ Math.pow(new_datas.get(i)[2] - z2[2], 2) + Math.pow(new_datas.get(i)[3] - z2[3], 2)
						+ Math.pow(new_datas.get(i)[4] - z2[4], 2));
				test3 = Math.sqrt(Math.pow(new_datas.get(i)[0] - z3[0], 2) + Math.pow(new_datas.get(i)[1] - z3[1], 2)
						+ Math.pow(new_datas.get(i)[2] - z3[2], 2) + Math.pow(new_datas.get(i)[3] - z3[3], 2)
						+ Math.pow(new_datas.get(i)[4] - z3[4], 2));
				test4 = Math.sqrt(Math.pow(new_datas.get(i)[0] - z4[0], 2) + Math.pow(new_datas.get(i)[1] - z4[1], 2)
						+ Math.pow(new_datas.get(i)[2] - z4[2], 2) + Math.pow(new_datas.get(i)[3] - z4[3], 2)
						+ Math.pow(new_datas.get(i)[4] - z4[4], 2));// 计算距离
				Double arr[] = new Double[4];
				arr[0] = test1;
				arr[1] = test2;
				arr[2] = test3;
				arr[3] = test4;
				Arrays.sort(arr);
				if (arr[0] == test1) {
					s1.add(i);
				} else if (arr[0] == test2) {
					s2.add(i);
				} else if (arr[0] == test3) {
					s3.add(i);
				} else if (arr[0] == test4) {
					s4.add(i);
				} // 遍历所有内容进行分组,记录次序
			}

			Double[] z1_buf = Calculation(new_datas, s1);
			Double[] z2_buf = Calculation(new_datas, s2);
			Double[] z3_buf = Calculation(new_datas, s3);
			Double[] z4_buf = Calculation(new_datas, s4);
			if (Arrays.equals(z1, z1_buf) && Arrays.equals(z2, z2_buf) && Arrays.equals(z3, z3_buf)
					&& Arrays.equals(z4, z4_buf))
				break;
			else {
				System.arraycopy(z1_buf, 0, z1, 0, 5);
				System.arraycopy(z2_buf, 0, z2, 0, 5);
				System.arraycopy(z3_buf, 0, z3, 0, 5);
				System.arraycopy(z4_buf, 0, z4, 0, 5);
			}
		}  //分析结束


		JFrame box = new JFrame("基于江苏某中学文理选择分析器");
		box.setBounds(150, 150, 950, 600);
		box.setLayout(null);

		JButton search = new JButton("集体分析");
		search.setBounds(200, 50, 100, 30);
		box.add(search);

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(100, 120, 160, 350);
		box.add(scrollPane_1);

		final JTextArea L_first = new JTextArea();
		L_first.setBounds(100, 120, 160, 350);
		box.add(L_first);
		scrollPane_1.setViewportView(L_first);

		JLabel lf = new JLabel("理科并具有优势");
		lf.setBounds(130, 95, 160, 15);
		box.add(lf);

		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(300, 120, 160, 350);
		box.add(scrollPane_2);

		JLabel wf = new JLabel("文科并具有优势");
		wf.setBounds(330, 95, 160, 15);
		box.add(wf);

		final JTextArea W_first = new JTextArea();
		W_first.setBounds(300, 120, 160, 350);
		box.add(W_first);
		scrollPane_2.setViewportView(W_first);

		JScrollPane scrollPane_3 = new JScrollPane();
		scrollPane_3.setBounds(500, 120, 160, 350);
		box.add(scrollPane_3);

		JLabel ll = new JLabel("理科但处于弱势");
		ll.setBounds(530, 95, 160, 15);
		box.add(ll);

		final JTextArea L_last = new JTextArea();
		L_last.setBounds(500, 120, 160, 350);
		box.add(L_last);
		scrollPane_3.setViewportView(L_last);

		JScrollPane scrollPane_4 = new JScrollPane();
		scrollPane_4.setBounds(700, 120, 160, 350);
		box.add(scrollPane_4);

		JLabel wl = new JLabel("文科但处于弱势");
		wl.setBounds(730, 95, 160, 15);
		box.add(wl);

		final JTextArea W_last = new JTextArea();
		W_last.setBounds(700, 120, 160, 350);
		box.add(W_last);
		scrollPane_4.setViewportView(W_last);

		search.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				L_first.setText("");
				for (int i = 0; i < s1.size(); i++) {
					L_first.append(datas.get(s1.get(i))[9] + "  " + datas.get(s1.get(i))[10] + "\n");
				}

				W_first.setText("");
				for (int i = 0; i < s2.size(); i++) {
					W_first.append(datas.get(s2.get(i))[9] + "  " + datas.get(s2.get(i))[10] + "\n");
				}

				L_last.setText("");
				for (int i = 0; i < s3.size(); i++) {
					L_last.append(datas.get(s3.get(i))[9] + "  " + datas.get(s3.get(i))[10] + "\n");
				}

				W_last.setText("");
				for (int i = 0; i < s4.size(); i++) {
					W_last.append(datas.get(s4.get(i))[9] + "  " + datas.get(s4.get(i))[10] + "\n");
				}

			}
		});

		JLabel qs = new JLabel("输入学号快速查询");
		qs.setBounds(330, 55, 160, 15);
		box.add(qs);
		
		JTextField numbers = new JTextField();
		numbers.setBounds(450, 50, 160, 30);
		box.add(numbers);

		JButton search1 = new JButton("查询");
		search1.setBounds(650, 50, 100, 30);
		box.add(search1);

		search1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				String number = numbers.getText();
				number=number.trim();
				ArrayList<ArrayList<Integer>> all = new ArrayList<>();
				all.add(s1);
				all.add(s2);
				all.add(s3);
				all.add(s4);

				for (int i = 0; i < all.size(); i++) {
					for (int j = 0; j < all.get(i).size(); j++)// s1
					{
						if (datas.get(all.get(i).get(j))[9].equals(number)) {
							Toolkit.getDefaultToolkit().beep();
							switch (i) {
							case 0:
								JOptionPane.showMessageDialog(null,
										datas.get(all.get(i).get(j))[10] + "同学更加适合学理科，并具有优势", null,
										JOptionPane.PLAIN_MESSAGE);
								break;
							case 1:
								JOptionPane.showMessageDialog(null,
										datas.get(all.get(i).get(j))[10] + "同学更加适合学文科，并具有优势", null,
										JOptionPane.PLAIN_MESSAGE);
								break;
							case 2:
								JOptionPane.showMessageDialog(null,
										datas.get(all.get(i).get(j))[10] + "同学更加适合学理科，但处于弱势", null,
										JOptionPane.PLAIN_MESSAGE);
								break;
							case 3:
								JOptionPane.showMessageDialog(null,
										datas.get(all.get(i).get(j))[10] + "同学更加适合学文科，但处于弱势", null,
										JOptionPane.PLAIN_MESSAGE);
								break;
							}
						}
					}
				}
			}

		});

		JLabel yw=new JLabel("语文");
		yw.setBounds(100, 500, 30, 15);
		box.add(yw);
		
		JTextField yw1=new JTextField("0");
		yw1.setBounds(130, 500, 40, 23);
		box.add(yw1);
		
		JLabel sx=new JLabel("数学");
		sx.setBounds(170, 500, 30, 15);
		box.add(sx);
		
		JTextField sx1=new JTextField("0");
		sx1.setBounds(200, 500, 40, 23); 
		box.add(sx1);
		
		JLabel yy=new JLabel("英语");
		yy.setBounds(240, 500, 30, 15);
		box.add(yy);
		
		JTextField yy1=new JTextField("0");
		yy1.setBounds(270, 500, 40, 23); 
		box.add(yy1); 
		
		JLabel wul=new JLabel("物理");
		wul.setBounds(310, 500, 30, 15);
		box.add(wul);
		
		JTextField wl1=new JTextField("0");
		wl1.setBounds(340, 500, 40, 23); 
		box.add(wl1);  
		
		JLabel hx=new JLabel("化学");
		hx.setBounds(380, 500, 30, 15);
		box.add(hx);
		
		JTextField hx1=new JTextField("0");
		hx1.setBounds(410, 500, 40, 23); 
		box.add(hx1);  
		
		JLabel sw=new JLabel("生物");
		sw.setBounds(450, 500, 30, 15);
		box.add(sw);
		
		JTextField sw1=new JTextField("0");
		sw1.setBounds(490, 500, 40, 23); 
		box.add(sw1); 
		
		JLabel zz=new JLabel("政治");
		zz.setBounds(530, 500, 30, 15); 
		box.add(zz);
		
		JTextField zz1=new JTextField("0");
		zz1.setBounds(560, 500, 40, 23); 
		box.add(zz1); 
		
		JLabel ls=new JLabel("历史");
		ls.setBounds(600, 500, 30, 15); 
		box.add(ls);
		
		JTextField ls1=new JTextField("0");
		ls1.setBounds(630, 500, 40, 23); 
		box.add(ls1);
		
		JLabel dl=new JLabel("地理");
		dl.setBounds(670, 500, 30, 15); 
		box.add(dl);
		
		JTextField dl1=new JTextField("0");
		dl1.setBounds(700, 500, 40, 23); 
		box.add(dl1);
		
		JButton analysis=new JButton("进行分析");
		analysis.setBounds(740, 495, 100, 30);
		box.add(analysis);
		
		analysis.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				double goal1=Double.valueOf(yw1.getText());
				double goal2=Double.valueOf(sx1.getText());
				double goal3=Double.valueOf(yy1.getText());
				double goal4=(Double.valueOf(wl1.getText())+Double.valueOf(hx1.getText())+Double.valueOf(sw1.getText()))/3.0;
				double goal5=(Double.valueOf(zz1.getText())+Double.valueOf(ls1.getText())+Double.valueOf(dl1.getText()))/3.0;
				
				Double arr[] = new Double[4];
				arr[0]=Math.sqrt(Math.pow(goal1 - z1[0], 2) + Math.pow(goal2 - z1[1], 2)
				+ Math.pow(goal3 - z1[2], 2) + Math.pow(goal4 - z1[3], 2)
				+ Math.pow(goal5 -z1[4], 2));
				arr[1]=Math.sqrt(Math.pow(goal1 - z2[0], 2) + Math.pow(goal2 - z2[1], 2)
				+ Math.pow(goal3 - z2[2], 2) + Math.pow(goal4 - z2[3], 2)
				+ Math.pow(goal5 -z2[4], 2));
				arr[2]=Math.sqrt(Math.pow(goal1 - z3[0], 2) + Math.pow(goal2 - z3[1], 2)
				+ Math.pow(goal3 - z3[2], 2) + Math.pow(goal4 - z3[3], 2)
				+ Math.pow(goal5 -z3[4], 2)); 
				arr[3]=Math.sqrt(Math.pow(goal1 - z4[0], 2) + Math.pow(goal2 - z4[1], 2)
				+ Math.pow(goal3 - z4[2], 2) + Math.pow(goal4 - z4[3], 2)
				+ Math.pow(goal5 -z4[4], 2));
				double test0=arr[0];
				double test1=arr[1];
				double test2=arr[2];
				double test3=arr[3]; 
				Arrays.sort(arr);
				//分析出最近的
				Toolkit.getDefaultToolkit().beep(); 
				if (arr[0] == test0) {
					JOptionPane.showMessageDialog(null, "同学更加适合学理科，并具有优势", null,
							JOptionPane.PLAIN_MESSAGE);
				} else if (arr[0] == test1) {
					JOptionPane.showMessageDialog(null, "同学更加适合学文，并具有优势", null,
							JOptionPane.PLAIN_MESSAGE);
				} else if (arr[0] == test2) {
					JOptionPane.showMessageDialog(null, "同学更加适合学理科但还需要努力", null,
							JOptionPane.PLAIN_MESSAGE);
				} else if (arr[0] == test3) {
					JOptionPane.showMessageDialog(null, "同学更加适合学文课但还需要努力", null,
							JOptionPane.PLAIN_MESSAGE); 
				}
			}
		});
		
		box.setVisible(true);
	}

}
