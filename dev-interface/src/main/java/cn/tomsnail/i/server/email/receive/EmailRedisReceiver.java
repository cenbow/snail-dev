package cn.tomsnail.i.server.email.receive;

import cn.tomsnail.jms.JmsType;
import cn.tomsnail.jms.spring.annotation.JmsConsumer;

/**
 *        email redis接收
 * @author yangsong
 * @version 0.0.1
 * @status 正常
 * @date 2016年8月20日 下午2:57:16
 * @see 
 */
@JmsConsumer(url="192.168.169.150",name="email",type=JmsType.REDIS)
public class EmailRedisReceiver extends EmailReciever  {


}
