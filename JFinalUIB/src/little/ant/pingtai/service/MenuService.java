package little.ant.pingtai.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import little.ant.pingtai.model.MenuModel;
import little.ant.pingtai.tools.ToolSqlXml;

import org.apache.log4j.Logger;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;

public class MenuService extends BaseService {

	@SuppressWarnings("unused")
	private static Logger log = Logger.getLogger(MenuService.class);
	
	/**
	 * 获取子节点数据
	 * @param systemsIds
	 * @param parentIds
	 * @param i18n
	 * @return
	 */
	public String childNodeData(String systemsIds, String parentIds, String i18n){
		String names = "names" + i18n(i18n) + " as names";

		Map<String, Object> param = new HashMap<String, Object>();
		param.put("names", names);
		
		List<MenuModel> list = null;
		if(null != parentIds){
			String sql = ToolSqlXml.getSql("pingtai.menu.child", param);
			list = MenuModel.dao.find(sql, parentIds);
			
		}else{
			String sql = ToolSqlXml.getSql("pingtai.menu.root", param);
			list = MenuModel.dao.find(sql, systemsIds);
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		
		int size = list.size() - 1;
		for (MenuModel menu : list) {
			sb.append(" { ");
			sb.append(" id : '").append(menu.getStr("ids")).append("', ");
			sb.append(" name : '").append(menu.getStr("names")).append("', ");
			sb.append(" isParent : true, ");
			sb.append(" font : {'font-weight':'bold'}, ");
			sb.append(" icon : '").append("/jsFile/zTree/css/zTreeStyle/img/diy/").append(menu.getStr("images")).append("' ");
			sb.append(" }");
			if(list.indexOf(menu) < size){
				sb.append(", ");
			}
		}
		
		sb.append("]");
		
		return sb.toString();
	}
	
	/**
	 * 保存
	 * @param pIds
	 * @param names
	 * @param orderIds
	 * @param i18n
	 * @return
	 */
	@Before(Tx.class)
	public String save(String pIds, String names, int orderIds, String i18n){
		String namesColunm = "names" + i18n(i18n);
		
		MenuModel pMenu = MenuModel.dao.findById(pIds);
		pMenu.set("isparent", "true").update();
		
		String images = "";
		if(orderIds < 2 || orderIds > 9){
			orderIds = 2;
			images = "2.png";
		}else{
			images = orderIds + ".png";
		}

		MenuModel menu = new MenuModel();
		menu.set("isparent", "false");
		menu.set("parentmenuids", pIds);
		menu.set("orderids", orderIds);
		menu.set(namesColunm, names);
		menu.set("images", images);
		menu.set("systemsids", pMenu.getStr("systemsids"));
		menu.save();
		
		return menu.getStr("ids");
	}
	
	/**
	 * 更新
	 * @param ids
	 * @param pIds
	 * @param names
	 * @param principalIds
	 */
	public void update(String ids, String pIds, String names, String i18n){
		String namesColunm = "names" + i18n(i18n);
		
		MenuModel menu = MenuModel.dao.findById(ids);
		if(null != names && !names.isEmpty()){
			//更新模块名称
			menu.set(namesColunm, names).update();
			
		}else if(null != pIds && !pIds.isEmpty()){
			//更新上级模块
			menu.set("parentmenuids", pIds).update();
		}
	}
	
	/**
	 * 删除
	 * @param ids
	 * @return
	 */
	public boolean delete(String ids) {
		String sql = ToolSqlXml.getSql("pingtai.menu.childCount");
		Record record = Db.findFirst(sql, ids);
		Long counts = record.getNumber("counts").longValue();
	    if(counts > 1){
	    	return false;
	    }
	    MenuModel.dao.deleteById(ids);
	    return true;
	}
	
	/**
	 * 设置菜单功能
	 * @param roleIds
	 * @param moduleIds
	 * @param operatorIds
	 */
	public void setOperator(String menuIds, String operatorIds){
		MenuModel menu = MenuModel.dao.findById(menuIds);
		menu.set("operatorids", operatorIds).update();
	}
	
}
