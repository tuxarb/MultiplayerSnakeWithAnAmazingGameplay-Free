<p align="center"><img width="500" height="300" src="https://cloud.githubusercontent.com/assets/15519803/25315795/c3052110-2863-11e7-8bce-31c9f6d9a656.jpg"></p>

## *Многопользовательская змейка с серверной/клиентской частями на Java c использованием https://github.com/EsotericSoftware/kryonet либы для передачи объектов по сети, используя TCP/UDP.<br/>*
### Краткий обзор
Сервер часть использует 2 режима отображения:
* **Графический**
* **Консольный**

Благодаря этому сервер можно запустить на любой ОС, поддерживающей **Java**, независимо оттого, имеет ли она графическую поддержку или нет.<br/><br/>
Сервер поддерживает следующие команды:
* **speed {level} | level in [-9;9]**
* **score {id} {score}**
* **kamikaze {id}**
* **freeze {id}**
* **unfreeze {id}**
* **ban {ip}**
* **unban {ip}**
<br/>

**Примеры**
* speed 9
* speed -9
* score 2 100
* kamikaze 2
* ban 127.0.0.1

### Логирование
Программа использует **logback** либу для логирования. По умолчанию файл с логами (**snake_server.log**) сохраняется в корень проекта. Если вы используете консольный вариант сервера, то логи будут для Вас крайне полезны :)

### Как запустить
Для запуска требуется установленная **Java 8**. 
* Скачайте _https://github.com/FattyStump/MultiplayerSnakeWithAnAmazingGameplay-Free/blob/master/snake-server.jar_
* Скачайте _https://github.com/FattyStump/MultiplayerSnakeWithAnAmazingGameplay-Free/blob/master/snake-client.jar_
* Запустите сервер следующей командой из консоли:
  - Для графического режима:<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;**_java -jar ваш/путь/к/snake-server.jar_**
  - Для консольного режима:<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;**_java -jar -Dmode=console &nbsp;ваш/путь/к/snake-server.jar_**
* Запустите клиента следующей командой из консоли:<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;**_java -jar ваш/путь/к/snake-client.jar_**
* После запуска клиента укажите **ip** запущенного сервера (Если это один и тот же компьютер, то **localhost** или **127.0.0.1**). 
* Наслаждайтесь игрой! :)

#### Примечание
Запускать **ClientFrame** Вы можете сколько угодно раз. Каждый новый запуск будет создавать нового клиента (пользователя). Таким образом Вы можете играть вместе с друзьями, просто используя один из компьютеров в качестве сервера, но при условии, что Ваша сеть достаточно мощная.

### Как это выглядит
![snake](https://user-images.githubusercontent.com/15519803/57480835-7802ce00-72a9-11e9-8f5d-07b182135c52.png)

