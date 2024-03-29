package little.ant.weixin.service;

import little.ant.pingtai.common.SplitPage;
import little.ant.pingtai.service.BaseService;
import little.ant.weixin.lucene.DocKeyword;
import little.ant.weixin.model.KeywordModel;

import org.apache.log4j.Logger;

public class KeywordService extends BaseService {

	private static Logger log = Logger.getLogger(KeywordService.class);
	
	/**
	 * 保存
	 * @param keyword
	 * @return
	 */
	public String save(KeywordModel keyword){
		keyword.save();
		
		new DocKeyword().add(keyword); // 索引
		
		return keyword.getStr("ids");
	}

	/**
	 * 更新
	 * @param keyword
	 */
	public void update(KeywordModel keyword){
		keyword.update();
		new DocKeyword().update(keyword); // 索引
	}

	/**
	 * 删除
	 * @param keyword
	 */
	public void delete(String ids){
		KeywordModel.dao.deleteById(ids);
		new DocKeyword().delete(ids); // 索引
	}
	
	/**
	 * 分页
	 * @param splitPage
	 */
	public void list(SplitPage splitPage){
		log.debug("微信自动回复管理：分页处理");
		String select = " select * ";
		splitPageBase(splitPage, select, "weixin.keyword.splitPage");
	}
	
}
