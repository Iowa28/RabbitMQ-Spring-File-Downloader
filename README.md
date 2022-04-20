# RabbitMQ-Spring-File-Downloader

- MQ Producer – получает ссылку на файл через get метод по http, отправляет ее в очередь
- MQ Consumer – достает ссылку из очереди, загружает файл в свою директорию