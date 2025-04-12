package com.kodla.coz.model.dtos;

import java.util.List;

/**
 * Algoritma sorularını filtrelemek için kullanılan Dto'dur
 */
// FIXME: daha iyi bir filtreleme yöntemi gerekiyor
public class FilterQueryDto {
    public FilterQueryDto(){

    }

    public FilterQueryDto(List<String> solved, List<String> difficulty) {
        this.solved = solved;
        this.difficulty = difficulty;
    }
    private List<String> solved;
    private List<String> difficulty;

    public List<String> getSolved() {
        return solved;
    }

    public void setSolved(List<String> solved) {
        this.solved = solved;
    }

    public List<String> getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(List<String> difficulty) {
        this.difficulty = difficulty;
    }
}
