package com.electricity.tool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.dbutils.QueryRunner;

import com.mchange.v2.c3p0.ComboPooledDataSource;


public class JDBCUtils {
	
	// ��ʼ��c3p0d
		private static DataSource dataSource = null;
		static {
			// �Զ�����srcĿ¼�����c3p0�������ļ�����c3p0-config.xml��
			dataSource = new ComboPooledDataSource();
		}
		public static QueryRunner getQueryRunner() {
			// ��һ��������QueryRunner���󣬴������ӳض���
			// �ڴ���QueryRunner�����ʱ������������ݶ���dataSource��
			// ��ô��ʹ��QueryRunner����ķ���ʱ�򣬾Ͳ���Ҫ�������Ӷ���
			QueryRunner query = new QueryRunner(dataSource);
			// �ڶ��������Զ�������Դ�л�ȡ����(���ùر�����)
			/*
			 * ����QueryRunner���󣬴������ӳض��� �ڴ���QueryRunner�����ʱ���������������Դ����
			 * ��ô��ʹ��QueryRunner���󷽷���ʱ�򣬾Ͳ���Ҫ�������Ӷ��� ���Զ�������Դ�л�ȡ����(���ùر�����)
			 */
			return query;
		}
	}
