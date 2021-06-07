package com.paekva.wstlab7.cli;

import lombok.Getter;
import lombok.Setter;

public enum StudentCommand {
    FIND_ALL("Вывести список всех студентов"),
    FIND_BY_FILTERS("Применить фильтры"),
    INSERT("Добавить информацию о студенте"),
    UPDATE("Обновить информацию о студенте"),
    DELETE("Удалить информацию о студенте"),
    QUIT("Выйти");

    @Getter
    @Setter
    private String help;

    StudentCommand(String help) {
        this.help = help;
    }
}