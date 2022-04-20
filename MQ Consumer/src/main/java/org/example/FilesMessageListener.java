package org.example;

import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.net.URLConnection;

@Component
public class FilesMessageListener {

    @RabbitListener(queues = "#{queue.name}", containerFactory = "containerFactory")
    public void onMessage(Message message) {
        try {
            String fileUrl = new String(message.getBody());
            URL url = new URL(fileUrl);
			String fileName = url.getFile();
			try {
                    BufferedImage image = ImageIO.read(url);
                    File output = new File("images/" + UUID.randomUUID() +
                            fileName.substring(fileName.lastIndexOf(".")));
                    ImageIO.write(image, "jpg", output);

                    channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
                } catch (IOException e) {
                    channel.basicReject(message.getEnvelope().getDeliveryTag(), false);
                }
        } catch (Exception e) {
            throw new AmqpRejectAndDontRequeueException(e);
        }
    }
	
}
