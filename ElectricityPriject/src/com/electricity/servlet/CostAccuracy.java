package com.electricity.servlet;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/*
    ���貿: �����������׼ȷ��

    projectFinancialStatement: ���̲������
    approvalEstimate: ��������
    costAccuracy: �����������׼ȷ��

    18��ָ�����׼ȷ��:
        ���� 85.57    �ױ� 82.25    ��Դ 82.25
        ���� 82.59    ���� 82.53    ���� 81.03
        ��� 84.47    ���� 84.27    ƽ��ɽ 82.25
        ��� 81.80    ����Ͽ 82.25  ���� 69.96
        ���� 83.13    ���� 82.25    ��� 82.38
        ֣�� 81.88    �ܿ� 82.25    פ��� 82.28
 */
public class CostAccuracy {
    private static double projectFinancialStatement;
    private static double approvalEstimate;
    private static double costAccuracy;
    private static int CostAccuracy;
    public String show(String[] strings,HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		
    	  Scanner sc = new Scanner(System.in);
          Map<String, Integer> zjl = new HashMap<>();     //�洢�������кͶ�Ӧ�����׼ȷ��
          String[] Locals = {"����" ,"�ױ�", "��Դ", "����", "����", "����",
                              "���", "����", "ƽ��ɽ", "���", "����Ͽ", "����",
                              "����", "����", "���", "֣��", "�ܿ�", "פ���"};
          DecimalFormat df = new DecimalFormat("0.0000");
         
     for (int i = 0; i <strings.length-1; i++) {
    	 Double double1=Double.parseDouble(strings[i]);
    	 costAccuracy = Double.parseDouble(df.format(double1));
         CostAccuracy = (int)(costAccuracy * 10000);
         zjl.put(Locals[i], CostAccuracy);
	}


          //����List����zjl�еļ�ֵ�Ը���valueֵ��С����
          List<Map.Entry<String, Integer>> entryList = new ArrayList<>(zjl.entrySet());
          Collections.sort(entryList, new Comparator<Map.Entry<String, Integer>>() {
              @Override
              public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                  //����valueֵ��С����
//                  return o1.getValue() - o2.getValue();

                  //����valueֵ�Ӵ�С
                  return o2.getValue() - o1.getValue();

                  //����valueֵ����compareTo()����Ĭ���Ǵ�С��������
//                  return o1.getValue().compareTo(o2.getValue());
              }
          });

          List<Integer> list = new ArrayList<>();
          int[] array = new int[entryList.size() + 1];
          array[0] = 1;
          int index = 1, k, up = 1;  //indexΪarray�����±�
          for (Map.Entry s : entryList) {
              Integer value = (Integer) s.getValue();
              list.add(value);
          }
//          System.out.println(list);
          if (list.get(0) == list.get(1)){
              k = 1;
          }else{
              k = 0;
          }

          //�������н�������
          for (int i = 1; i < list.size(); i++) {
              up++;
              if (list.get(i).equals(list.get(i-1))){
                  array[index++] = k;
              }else{
                  array[index++] = up;
                  k = up;
              }
          }

          //����index
          index = 0;
          int pingIndex = 0;

          //���������������
          for (Map.Entry s : entryList) {
              System.out.println("��" + array[index++] + "��: " + s.getKey() + "-->" + ((Integer)s.getValue() / 100.0) + "%");

              //��ȡƽ��ɽ������
              if ("ƽ��ɽ".equals(s.getKey())){
                  pingIndex = array[index - 1];
              }
          }
          System.out.println();
   
          System.out.println("�������������׼ȷ����: ");
          if (pingIndex > 0 && pingIndex <= 9) {
              System.out.println("ƽ��ɽ������Ϊ: " + pingIndex + ", ������Կ�ǰ, ��������!!!");
              String success="ƽ��ɽ������Ϊ: " + pingIndex + ", ������Կ�ǰ, ��������!!!";
              request.setAttribute("paiming",success);
              request.getRequestDispatcher("success.jsp").forward(request,response);
              return success;
          }else {
              System.out.println("ƽ��ɽ������Ϊ: " + pingIndex + ", ������Կ���, ԭ��Ϊ���̲��������ٻ�����������ࡣ");
              String falses="ƽ��ɽ������Ϊ: " + pingIndex + ", ������Կ���, ԭ��Ϊ���̲��������ٻ�����������ࡣ";
              request.setAttribute("paiming",falses);
              request.getRequestDispatcher("success.jsp").forward(request,response);
              return falses;
          }

      }
	
    
    public static void main(String[] args) {
    	CostAccuracy c=new CostAccuracy();
//    	c.show();
    }
}
