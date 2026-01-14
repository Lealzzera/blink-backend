# Blink Back-end

Aplicação de back end da blink
## Comandos docker

Construir a imagem docker: <br>
`docker build --build-arg JAR_FILE=build/libs/*.jar -t blink/be .`

Rodar a imagem docker: <br>
`docker run --name blink-be -p 3003:3003 blink/be`

Parametros para ambientes: <br>
--network `<docker_network>`<br>
-e `<environment_var>` <br>
-p `<external>:<internal>`
