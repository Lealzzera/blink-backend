| Method | Path                                              |
|:------:|:--------------------------------------------------|
|  GET   | /configurations/clinic-id                         |
|  PUT   | /configurations/availability                      |
|  PUT   | /configurations/appointments                      |
|  GET   | /configurations/availability/{clinicId}           |
|  GET   | /configurations/appointments/{clinicId}           |
|        |                                                   |
|  POST  | /configurations/availability/exception            |
|  GET   | /configurations/availability/{clinicId}/exception |
|  GET   | /configurations/availability/exception/{id}       |
| DELETE | /configurations/availability/exception/{id}       |
|        |                                                   |
|  POST  | /message/whats-app/{clinicId}/disconnect          |
|  POST  | /message/whats-app/send-message                   |
|  POST  | /message/whats-app/receive-message                |
|  GET   | /message/whats-app/{clinicId}/status              |
|  GET   | /message/whats-app/{clinicId}/qr-code             |
|        |                                                   |
|  PUT   | /chat/{clinicId}/ai-answer/{phoneNumber}          |
|  GET   | /chat/{clinicId}/overview                         |
|  GET   | /chat/{clinicId}/overview/{phoneNumber}           |
|        |                                                   |
|  PUT   | /appointments/{appointmentId}                     |
|  PUT   | /appointments/status                              |
|  POST  | /appointments                                     |
|  GET   | /appointments/{appointmentId}/details             |
|  GET   | /appointments/availability                        | <-- endpoint antigo, com clinic 1 chumbado
|  GET   | /appointments/availability/{clinicId}             |
|        |                                                   |
|  PUT   | /sales/status                                     |
|  POST  | /sales                                            |
|  GET   | /sales/{saleId}/details                           |
|        |                                                   |
|  POST  | /patient                                          |
|  GET   | /patient/{id}                                     |
|  GET   | /patient/{clinicId}/phone-number/{phone}          |
|        |                                                   |
|  GET   | /domain/whats-app-status                          |
|  GET   | /domain/week-days                                 |
|  GET   | /domain/appointment-status                        |
|        |                                                   |
|  GET   | /dashboards/{clinicId}                            |
|        |                                                   |