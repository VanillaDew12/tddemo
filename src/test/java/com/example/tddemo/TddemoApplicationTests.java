package com.example.tddemo;

import cn.hutool.core.io.FileUtil;
import com.grapetec.infra.client.component.InfraClient;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 基建层客户端 测试类
 *
 * @author mxy
 * @date 2024-04-10
 */
@Slf4j
@SpringBootTest
class TddemoApplicationTests {

	@Resource
	private InfraClient infraClient;

	/**
	 * 当前方法，可执行DDL和DML
	 */
	@Test
	void executeDefineSql() {
		String sql = "INSERT INTO t_app VALUES ('1', '凌云光报价应用', '凌云光技术股份有限公司', '1', NULL, NULL, NULL, '2024-04-10 16:40:57', " +
				"'2024-04-10 16:40:57');";
		boolean executeResult = infraClient.execDefineSql(sql);
		log.info("\r\n 执行DML的结果：{}", executeResult);
		assertTrue(executeResult);
	}

	/**
	 * 当前方法，可执行DDL和DML。增加了事务管理
	 */
	@Test
	void batchExecuteDefineSql() {
		List<String> sqlList = new ArrayList<>();
		String sql1 = "INSERT INTO t_app VALUES ('2', '机床应用', '北京格瑞普科技有限公司', '1', NULL, NULL, NULL, '2024-04-10 16:40:57', " +
				"'2024-04-10 16:40:57');";
		String sql2 = "INSERT INTO t_app VALUES ('3', '工件应用', '北京格瑞普科技有限公司', '1', NULL, NULL, NULL, '2024-04-10 16:40:57', " +
				"'2024-04-10 16:40:57');";
		String sql3 = "UPDATE t_app SET name = '刀具应用' WHERE id = 3 ";
		sqlList.add(sql1);
		sqlList.add(sql2);
		sqlList.add(sql3);

		boolean executeResult = infraClient.batchExecDefineSql(sqlList);
		log.info("\r\n 批量执行DML的结果：{}", executeResult);
		assertTrue(executeResult);
	}

//	/**
//	 * 当前方法，可执行DQL
//	 */
//	@Test
//	void executeQuerySql() {
//		String querySql = "select * from t_app";
//		JSONArray queryResult = infraClient.execQuerySql(querySql);
//		log.info("\r\n 执行查询的结果：{}", queryResult.toJSONString());
//		assertNotNull(queryResult);
//
//		querySql = "select * from t_app_user";
//		List<AppUser> queryResultList = infraClient.execQuerySql(querySql, AppUser.class);
//		for (AppUser user : queryResultList) {
//			log.info("\r\n 执行批量查询的结果：{}", user.getUsername());
//		}
//	}

	/**
	 * 文件上传
	 */
	@Test
	void uploadFile() throws IOException {
		String filePath = "/Users/xinyumao/Desktop/1.png";
		File file = new File(filePath);
		assertNotNull(file);
		String uploadResult = infraClient.uploadFile(file);
		log.info("\r\n 上传文件的相对路径：{}", uploadResult);

		MultipartFile multipartFile = new MockMultipartFile(file.getName(), file.getName(), null, FileUtil.getInputStream(file));
		String uploadResult1 = infraClient.uploadFile(multipartFile);
		log.info("\r\n 上传文件的相对路径：{}", uploadResult1);
	}

	/**
	 * 文件下载
	 */
	@Test
	void downloadFile() {
		String filePath = "/101/1012024040700001/1712742599598/1.png";
		ByteArrayResource resource = infraClient.downloadFile(filePath);
		assertNotNull(resource);
		FileUtil.writeBytes(resource.getByteArray(), "/Users/xinyumao/Desktop/2.png");
	}
}
