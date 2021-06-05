# ЛР1 - ЛР6

## Темы
ЛР1 - ЛР3 -> Поиск, CRUD и обработка ошибок на основе SOAP - сервиса
ЛР4 - ЛР6 -> Поиск, CRUD и обработка ошибок на основе REST - сервиса

## Необходимое предусловие
В системе установлены и настроены docker, docker-compose, psql.

## Общие инструкции
- Склонировать репозиторий
- Импортировать в IDE нужную лабораторную работу
- Убедиться, что IDE поддерживает Lombok Plugin
- Выполнить ```mvn clean package```

## Инструкции к ЛР1, ЛР4
- Embedded режим
    - Запустить скрипт ```run.sh```
    - Убедиться, что контейнеры с Glassfish и PostgreSQL запущены 
    - Убедиться, что по адресу http://localhost:8080/ видна строка "Engine seems to be working"
    - Убедиться, что по адресу  https://localhost:4848/ открывается админка Glassfish
    - Запустить в IDE ```client/App.java``` и убедиться, что все корректно отрабатывает
    - Остановить выполнение клиентского приложения и скрипта
- Standalone режим
    - Запустить скрипт ```run_postgre_only.sh```
    - Убедиться, что контейнер PostgreSQL запущен
    - Запустить в IDE ```standalone/tests/AppLauncher.java``` и убедиться, что сервер запустился
    - Запустить в IDE ```client/App.java``` и убедиться, что все корректно отрабатывает
    - Остановить выполнение приложений и скриптов

## Инструкции к ЛР2, ЛР3, ЛР5, ЛР6
Для данных лабораторных работ интересен только standalone режим, так как в нем производились изменения.
Для проверки работы соответствующих заданий нужно использовать инструкции, указанные выше.

## Пример работы клиентского приложения

### Пример 1: Обработка удаления несуществующего пользователя
    Выберите один из пунктов:
    0. Вывести help
    1. Вывести список всех студентов
    2. Применить фильтры
    3. Добавить информацию о студенте
    4. Обновить информацию о студенте
    5. Удалить информацию о студенте
    6. Выйти
       1
       Student{id=1, email='first@mail.ru', group number='1a', is local=true, birthDate=1998-01-01T00:00:00+03:00}
       Student{id=2, email='second@mail.ru', group number='2a', is local=true, birthDate=1998-02-02T00:00:00+03:00}
       Student{id=3, email='third@mail.ru', group number='3a', is local=false, birthDate=1998-03-03T00:00:00+03:00}


    Выберите один из пунктов:
    0. Вывести help
    1. Вывести список всех студентов
    2. Применить фильтры
    3. Добавить информацию о студенте
    4. Обновить информацию о студенте
    5. Удалить информацию о студенте
    6. Выйти
       5
    
    Введите id:
    2323
    По данному id: 2323 никого не найдено
    Пожалуйста, попробуйте снова!

### Пример 2: Добавление нового пользователя

    Выберите один из пунктов:
    0. Вывести help
    1. Вывести список всех студентов
    2. Применить фильтры
    3. Добавить информацию о студенте
    4. Обновить информацию о студенте
    5. Удалить информацию о студенте
    6. Выйти
       3
       email:
       new@address.com
       password:
       12345
       group number:
       e3
       is local:
       true
       birthDate(yyyy-mm-dd):
       2021-01-01
       4
    
    Выберите один из пунктов:
    0. Вывести help
    1. Вывести список всех студентов
    2. Применить фильтры
    3. Добавить информацию о студенте
    4. Обновить информацию о студенте
    5. Удалить информацию о студенте
    6. Выйти
       1
       Student{id=2, email='second@mail.ru', group number='2a', is local=true, birthDate=1998-02-02T00:00:00+03:00}
       Student{id=3, email='third@mail.ru', group number='3a', is local=false, birthDate=1998-03-03T00:00:00+03:00}
       Student{id=4, email='new@address.com', group number='e3', is local=true, birthDate=2021-01-01T00:00:00+03:00}

### Пример 3: Изменение существующего пользователя
    Выберите один из пунктов:
    0. Вывести help
    1. Вывести список всех студентов
    2. Применить фильтры
    3. Добавить информацию о студенте
    4. Обновить информацию о студенте
    5. Удалить информацию о студенте
    6. Выйти
       4
    
    Введите id:
    4
    
    Чтобы не изменять значение поля, оставьте значение пустым
    email:
    updated@addres.ru
    password:
    
    group number:
    
    is local:
    false
    birthDate(yyyy-mm-dd):
    
    true
    
    Выберите один из пунктов:
    0. Вывести help
    1. Вывести список всех студентов
    2. Применить фильтры
    3. Добавить информацию о студенте
    4. Обновить информацию о студенте
    5. Удалить информацию о студенте
    6. Выйти
       1
       Student{id=2, email='second@mail.ru', group number='2a', is local=true, birthDate=1998-02-02T00:00:00+03:00}
       Student{id=3, email='third@mail.ru', group number='3a', is local=false, birthDate=1998-03-03T00:00:00+03:00}
       Student{id=4, email='updated@addres.ru', group number='e3', is local=false, birthDate=2021-01-01T00:00:00+03:00}



## Дополнительная информация: БД PostgreSQL
После того, как PostgreSQL запущен, содержимое таблицы можно посмотреть следующим образом:

    paekva@paekva-Vostro-3480:~/projects/wst-labs/wst-lab-3$ cat psql.sh 
    #!/bin/bash
    psql 'postgresql://admin:admin@localhost/db_lab1_wst'
    paekva@paekva-Vostro-3480:~/projects/wst-labs/wst-lab-3$ ./psql.sh
    psql (12.6 (Ubuntu 12.6-0ubuntu0.20.04.1), server 13.2 (Debian 13.2-1.pgdg100+1))
    WARNING: psql major version 12, server major version 13.
    Some psql features might not work.
    Type "help" for help.
    
    db_lab1_wst=> \dt
    List of relations
    Schema |   Name   | Type  | Owner
    --------+----------+-------+-------
    public | students | table | admin
    (1 row)
    
    db_lab1_wst=> select * from students;
    id |     email      | password |          name          | group_number | birth_date | is_local
    ----+----------------+----------+------------------------+--------------+------------+----------
    1 | first@mail.ru  | first    | Иванов Иван Иванович   | 1a           | 1998-01-01 | t
    2 | second@mail.ru | second   | Петров Петр Петрович   | 2a           | 1998-02-02 | t
    3 | third@mail.ru  | third    | Иванова Алина Ивановна | 3a           | 1998-03-03 | f
    (3 rows)
