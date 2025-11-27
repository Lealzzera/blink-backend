CLINIC ID VEM DO TOKEN

| Method | Path                                      | Updated |
|:------:|:------------------------------------------|:--------|
|  GET   | /authenticatedUser/info                   |         |<-- mudar?                                            
|        |                                           |         |
|  PUT   | /configuration/availability               | X       |
|  PUT   | /configuration/appointments               | X       |
|  GET   | /configuration/availability               | X       |<-- usado para na tela de configs e na de agendamento 
|  GET   | /configuration/appointments               | X       |
|        |                                           |         |
|  POST  | /configuration/availability/atypical      | X       |
|  GET   | /configuration/availability/atypical      | X       |<-- consulta de antigos e futuros, ver como fazer
|  GET   | /configuration/availability/atypical/{id} |         |
|  PUT   | /configuration/availability/atypical/{id} |         |
| DELETE | /configuration/availability/atypical/{id} | X       |
|        |                                           |         |
|  POST  | /configuration/chat/auth                  |         |
| DELETE | /configuration/chat/auth                  |         |
|  GET   | /configuration/chat/status                |         |
|        |                                           |         |
| POST*  | /chat/whats-app/send-message              |         |<-- para o FE migrar para WS                          
| POST*  | /chat/whats-app/web-hook/message-received |         |<-- *usados pelo N8N ou WAHA                          
|  GET   | /chat/whats-app/overview                  |         |
|  GET   | /chat/whats-app/{phoneNumber}/messages    |         |
|  PUT   | /chat/whats-app/{phoneNumber}/ai-answer   |         |
|        |                                           |         |
|  PUT   | /appointments/{appointmentId}             |         |
|  PUT   | /appointments/status                      |         |
|  POST  | /appointments                             |         |
|  GET   | /appointments/{appointmentId}/details     |         |
|  GET   | /appointments/availability/{clinicId}     |         |
|        |                                           |         |
|  PUT   | /sales/status                             |         |
|  POST  | /sales                                    |         |
|  GET   | /sales/{saleId}/details                   |         |
|        |                                           |         |
|  POST  | /patient                                  |         |
|  GET   | /patient/{id}                             |         |
|  GET   | /patient/{clinicId}/phone-number/{phone}  |         |
|        |                                           |         |
|  GET   | /domain/whats-app-status                  |         |
|  GET   | /domain/week-days                         |         |
|  GET   | /domain/appointment-status                |         |
|        |                                           |         |
|  GET   | /dashboards/{clinicId}                    |         |
|        |                                           |         |