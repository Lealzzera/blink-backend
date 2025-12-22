CLINIC ID VEM DO TOKEN

| Method | Old Path                                          | Path                                      | Updated |
|:------:|---------------------------------------------------|:------------------------------------------|:--------|
|  GET   |                                                   | /authenticatedUser/info                   |         |<-- mudar?                                            
|        |                                                   |                                           |         |
|  PUT   | /configurations/availability                      | /configuration/availability               | X       |
|  PUT   | /configurations/appointments                      | /configuration/appointments               | X       |
|  GET   | /configurations/availability/{clinicId}           | /configuration/availability               | X       | 
|  GET   | /configurations/appointments/{clinicId}           | /configuration/appointments               | X       |
|        |                                                   |                                           |         |
|  POST  | /configurations/availability/exception            | /configuration/availability/atypical      | X       |
|  GET   | /configurations/availability/{clinicId}/exception | /configuration/availability/atypical      | X       |
|  GET   | /configurations/availability/exception/{id}       | /configuration/availability/atypical/{id} |         |
|  PUT   |                                                   | /configuration/availability/atypical/{id} |         |
| DELETE | /configurations/availability/exception/{id}       | /configuration/availability/atypical/{id} | X       |
|        |                                                   |                                           |         |
|  GET   | /message/whats-app/{clinicId}/qr-code             | /chat/whats-app/qr-code                   | X       |
| DELETE | /message/whats-app/{clinicId}/disconnect          | /chat/whats-app/disconnect                | X       |
|  GET   | /message/whats-app/{clinicId}/status              | /chat/whats-app/status                    | X       |
|        |                                                   |                                           |         |
| POST*  |                                                   | /chat/whats-app/send-message              |         |<-- para o FE migrar para WS                          
| POST*  |                                                   | /chat/whats-app/web-hook/message-received |         |<-- *usados pelo N8N ou WAHA                          
|  GET   |                                                   | /chat/whats-app/overview                  |         |
|  GET   |                                                   | /chat/whats-app/{phoneNumber}/messages    |         |
|  PUT   |                                                   | /chat/whats-app/{phoneNumber}/ai-answer   |         |
|        |                                                   |                                           |         |
|  POST  | /appointments                                     | /appointments                             |         |
|  GET   | /appointments/availability/{clinicId}             | /appointments/availability/{clinicId}     |         |
|  GET   | /appointments/{appointmentId}/details             | --                                        |         |
|  PUT   | /appointments/{appointmentId}                     | /appointments/{appointmentId}             |         |
|  PUT   | /appointments/status                              | --                                        |         |
|        |                                                   |                                           |         |
|  PUT   |                                                   | /sales/status                             |         |
|  POST  |                                                   | /sales                                    |         |
|  GET   |                                                   | /sales/{saleId}/details                   |         |
|        |                                                   |                                           |         |
|  POST  |                                                   | /patient                                  |         |
|  GET   |                                                   | /patient/{id}                             |         |
|  GET   |                                                   | /patient/{clinicId}/phone-number/{phone}  |         |
|        |                                                   |                                           |         |
|  GET   |                                                   | /domain/whats-app-status                  |         |
|  GET   |                                                   | /domain/week-days                         |         |
|  GET   |                                                   | /domain/appointment-status                |         |
|        |                                                   |                                           |         |
|  GET   |                                                   | /dashboards/{clinicId}                    |         |
|        |                                                   |                                           |         |