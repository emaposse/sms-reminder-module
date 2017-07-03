Module smsreminder desenvolvido por FGH
Este módulo tem como funcionalidades básicas notificar pacientes para a proxima consulta de levantamento

Este modulo é munida de uma classe que é executado pelo modulo de agendamentos de envios de SMS:
(org.openmrs.module.smsreminder.schedule.SendSmsReminderTask)
 e uma interface de envio Manual:
(Manual Send)

A implementação esta baseada no principio dos comandos RXTX para manipular as portas seriais do Computador,
 nas quais é colcado o modem SGM que desempnha o papel de GATEWAY de SMS.


Para usar os comandos RXTX é necessário que a tua JVM tenha o puder de se connectar com as portas seriais(HW), pelo que precisa
baixar e instalar os seguintes ficheiros:
rxtxSerial.dll - para windows-Copiar para:<JAVA_HOME>\jre\bin
librxtxSerial.so - para Linux-Copiar para:<JAVA_HOME>/jre/lib/i386/
Poderá baixar os ficheiros acima no Link:
http://mfizz.com/oss/rxtx-for-java

Com mais detalhes abaixo:

Windows
----------------------------------------------------

Choose your binary build - x64 or x86 (based on which version of
the JVM you are installing to)

NOTE: You MUST match your architecture.  You can't install the i386
version on a 64-bit version of the JDK and vice-versa.

For a JDK installation:

Copy RXTXcomm.jar ---> <JAVA_HOME>\jre\lib\ext
Copy rxtxSerial.dll ---> <JAVA_HOME>\jre\bin
Copy rxtxParallel.dll ---> <JAVA_HOME>\jre\bin

Linux
----------------------------------------------------

Choose your binary build - x86_64 or i386 (based on which version of
the JVM you are installing to)

NOTE: You MUST match your architecture.  You can't install the i386
version on a 64-bit version of the JDK and vice-versa.

For a JDK installation on architecture=i386

Copy RXTXcomm.jar ---> <JAVA_HOME>/jre/lib/ext
Copy librxtxSerial.so ---> <JAVA_HOME>/jre/lib/i386/
Copy librxtxParallel.so ---> <JAVA_HOME>/jre/lib/i386/

NOTE: For a JDK installation on architecture=x86_64, just change the
i386 to x86_64 above.
