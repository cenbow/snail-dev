package cn.tomsnail.i.server.sms.handler;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import cn.tomsnail.i.core.DataContentPckt;
import cn.tomsnail.i.server.email.filter.IEmailFilter;
import cn.tomsnail.i.server.email.send.IEmailSender;

/**
 *        
 * @author yangsong
 * @version 0.0.1
 * @status 正常
 * @date 2016年8月20日 下午3:07:04
 * @see 
 */
@Component
public class DefaultSmsHandler implements ISmsHandler{
	
	@Resource
	private List<ISmsHandler> emailHandlers;
	@Resource(name="beforeFilter")
	private IEmailFilter beforeFilter;
	@Resource(name="afterFilter")
	private IEmailFilter afterFilter;
	@Resource
	private IEmailSender emailSender;

	@Override
	public void handler(DataContentPckt dataContentPckt) {
		if(!before(dataContentPckt))return;
		if(emailHandlers!=null){
			for(ISmsHandler emailHandler:emailHandlers){
				emailHandler.handler(dataContentPckt);
			}
		}
		if(!after(dataContentPckt))return;
		emailSender.send(dataContentPckt);
	}
	
	private boolean before(DataContentPckt dataContentPckt){
		if(beforeFilter!=null){
			return beforeFilter.filter(dataContentPckt);
		}
		return true;
	}
	
	private boolean after(DataContentPckt dataContentPckt){
		if(afterFilter!=null){
			return afterFilter.filter(dataContentPckt);
		}
		return true;
	}

	public List<ISmsHandler> getEmailHandlers() {
		return emailHandlers;
	}

	public void setEmailHandlers(List<ISmsHandler> emailHandlers) {
		this.emailHandlers = emailHandlers;
	}

	public IEmailFilter getBeforeFilter() {
		return beforeFilter;
	}

	public void setBeforeFilter(IEmailFilter beforeFilter) {
		this.beforeFilter = beforeFilter;
	}

	public IEmailFilter getAfterFilter() {
		return afterFilter;
	}

	public void setAfterFilter(IEmailFilter afterFilter) {
		this.afterFilter = afterFilter;
	}

	public IEmailSender getEmailSender() {
		return emailSender;
	}

	public void setEmailSender(IEmailSender emailSender) {
		this.emailSender = emailSender;
	}
	
	
}
