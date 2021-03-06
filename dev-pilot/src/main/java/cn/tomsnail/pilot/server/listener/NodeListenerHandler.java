package cn.tomsnail.pilot.server.listener;

import java.util.List;

import cn.tomsnail.pilot.server.IServer;
import cn.tomsnail.pilot.server.NotifType;

/**
 *        
 * @author yangsong
 * @version 0.0.1
 * @status 正常
 * @date 2016年7月6日 下午12:06:09
 * @see 
 */
public class NodeListenerHandler extends  AListenerHandler{

	public NodeListenerHandler(IServer server) {
		super(server);
	}

	@Override
	public int  handler0(String parentPath, List<String> currentChilds) {
		if(currentChilds==null) return NotifType.NOTHING;
		return NotifType.NODE_CHANGE;
	}

	@Override
	public void close() {
		
	}

}
