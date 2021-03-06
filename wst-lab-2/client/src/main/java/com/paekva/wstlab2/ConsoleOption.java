package com.paekva.wstlab2;

import lombok.Getter;
import lombok.Setter;

public enum ConsoleOption {
    FIND_ALL("Вывести список всех студентов"),
    FIND_BY_FILTERS("Применить фильтры"),
    INSERT("Добавить информацию о студенте"),
    UPDATE("Обновить информацию о студенте"),
    DELETE("Удалить информацию о студенте"),
    QUIT("Выйти");

    @Getter
    @Setter
    private String help;

    ConsoleOption(String help) {
        this.help = help;
    }
}