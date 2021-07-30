package com.example.responsefromiseries.rest;

import com.ibm.jms.JMSTextMessage;
import com.ibm.mq.jms.*;
import com.ibm.msg.client.wmq.WMQConstants;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.jms.*;

@org.springframework.web.bind.annotation.RestController
public class RestController {

    @PostMapping("/prueba/send-response-message")
    public void sendMessage(@RequestBody String message) throws JMSException {
        MQQueueConnectionFactory cf = new MQQueueConnectionFactory();
        cf.setHostName("127.0.0.1");
        cf.setPort(1414);
        cf.setTransportType(1);
        cf.setQueueManager("QM1");
        cf.setChannel("DEV.ADMIN.SVRCONN");

        MQQueueConnection connection = (MQQueueConnection) cf.createQueueConnection("admin", "passw0rd");
        MQQueueSession session = (MQQueueSession) connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);

        MQQueue queue = (MQQueue) session.createQueue("queueResponseISeriesAuthLegalPerson");
        queue.setPersistence(1);
        queue.setBooleanProperty(WMQConstants.WMQ_MQMD_WRITE_ENABLED, true);
        MQQueueSender sender = (MQQueueSender) session.createSender(queue);

        String messageId = getCorrelationId(session, connection);

        JMSTextMessage jmsMessage = (JMSTextMessage) session.createTextMessage(message);
        jmsMessage.setJMSCorrelationID(messageId);
        sender.send(jmsMessage);
    }

    private String getCorrelationId(MQQueueSession session, MQQueueConnection conn) throws JMSException {
        MQQueue queue = (MQQueue) session.createQueue("queueSendAuthLegalPerson");
        MessageConsumer consumer = session.createConsumer(queue);
        conn.start();
        Message receivedMsg;

        try {
            while ((receivedMsg = consumer.receiveNoWait()) != null) {
                if (receivedMsg != null) {
//                    MQQueue mq = (MQQueue) receivedMsg.getJMSReplyTo();
                    return receivedMsg.getJMSMessageID();
                }
            }
        } catch (JMSException jmse2) {
            jmse2.printStackTrace();

        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        return null;
    }
}
