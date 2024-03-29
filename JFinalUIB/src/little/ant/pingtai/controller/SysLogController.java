package little.ant.pingtai.controller;

import little.ant.pingtai.annotation.Controller;
import little.ant.pingtai.model.SyslogModel;
import little.ant.pingtai.service.SysLogService;

import org.apache.log4j.Logger;

@Controller(controllerKey = "/jf/sysLog")
public class SysLogController extends BaseController {

	@SuppressWarnings("unused")
	private static Logger log = Logger.getLogger(SysLogController.class);
	
	private SysLogService syslogService = new SysLogService();
	
	public void index() {
		defaultOrder("startdate", "desc");
		syslogService.list(splitPage);
		render("/pingtai/sysLog/list.html");
	}
	
	public void view() {
		setAttr("sysLog", syslogService.view(getPara()));
		render("/pingtai/sysLog/view.html");
	}
	
	public void delete() {
		SyslogModel.dao.deleteById(getPara());
		redirect("/jf/sysLog");
	}

}


