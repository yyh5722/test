package little.ant.pingtai.model;

import little.ant.pingtai.annotation.Table;

import org.apache.log4j.Logger;

@SuppressWarnings("unused")
@Table(tableName="pt_resources")
public class ResourcesModel extends BaseModel<ResourcesModel> {
	
	private static final long serialVersionUID = 2051998642258015518L;

	private static Logger log = Logger.getLogger(ResourcesModel.class);
	
	public static final ResourcesModel dao = new ResourcesModel();
	
}
