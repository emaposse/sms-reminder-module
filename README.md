Module smsreminder desenvolvido por FGH
Este módulo tem como funcionalidades básicas notificar pacientes para a proxima consulta de levantamento

Este modulo é munida de uma classe que é executado pelo modulo de agendamentos de envios de SMS:
(org.openmrs.module.smsreminder.schedule.SendSmsReminderTask)
 e uma interface de envio Manual:
(Manual Send)

A implementação esta baseada no principio dos comandos RXTX para manipular as portas seriais do Computador,
 nas quais é colcado o modem SGM que desempnha o papel de GATEWAY de SMS.




