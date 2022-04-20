package org.example;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class FilesController {
    private final static String PNG_ROUTING_KEY = "files.images.png";
    private final static String JPG_ROUTING_KEY = "files.images.jpg";
    private final static String PDF_ROUTING_KEY = "files.images.pdf";
    private final static String HTML_ROUTING_KEY = "files.images.html";

    private final RabbitTemplate template;

    @GetMapping("/files")
    public ResponseEntity<?> sendFile(@RequestParam("file") String fileName) {
        String currentRouting = fileName.substring(fileName.lastIndexOf("."));

        if (currentRouting.equals(".jpeg"))
            currentRouting = ".jpg";

        switch (currentRouting) {
            case ".jpg":
                currentRouting = JPG_ROUTING_KEY;
                break;
            case ".png":
                currentRouting = PNG_ROUTING_KEY;
                break;
            case ".pdf":
                currentRouting = PDF_ROUTING_KEY;
                break;
            default:
                currentRouting = HTML_ROUTING_KEY;
                break;

        }

        template.send("files_topic_exchange", currentRouting, new Message(fileName.getBytes()));
        return ResponseEntity.ok().build();
    }
}
