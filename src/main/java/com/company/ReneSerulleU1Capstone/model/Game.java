package com.company.ReneSerulleU1Capstone.model;

import com.company.ReneSerulleU1Capstone.servicelayer.ItemType;
import com.fasterxml.jackson.annotation.JsonTypeName;

import javax.validation.constraints.*;
import java.util.Objects;

@JsonTypeName(ItemType.game)
public class Game extends Item {

    @Digits(integer = 11, fraction = 0, message = "Invalid gameId. Must be a whole number up to 11 digits long.")
    private Long gameId;

    @NotBlank(message = "Invalid Title: cannot be empty or blank.")
    @Size(max = 50, message = "Invalid Title: must not be longer than 50 characters.")
    private String title;

    @NotBlank(message = "Invalid esrbRating: cannot be empty or blank.")
    @Size(max = 50, message = "Invalid esrbRating: must not be longer than 50 characters.")
    private String esrbRating;

    @NotBlank(message = "Invalid description: cannot be empty or blank.")
    @Size(max = 255, message = "Invalid description: must not be longer than 255 characters.")
    private String description;

    @NotBlank(message = "Invalid studio: cannot be empty or blank.")
    @Size(max = 50, message = "Invalid studio: must not be longer than 50 characters.")
    private String studio;

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEsrbRating() {
        return esrbRating;
    }

    public void setEsrbRating(String esrbRating) {
        this.esrbRating = esrbRating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStudio() {
        return studio;
    }

    public void setStudio(String studio) {
        this.studio = studio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Game)) return false;
        Game game = (Game) o;
        return Objects.equals(getGameId(), game.getGameId()) &&
                getTitle().equals(game.getTitle()) &&
                getEsrbRating().equals(game.getEsrbRating()) &&
                getDescription().equals(game.getDescription()) &&
                getPrice().equals(game.getPrice()) &&
                getStudio().equals(game.getStudio()) &&
                Objects.equals(getQuantity(), game.getQuantity());
    }

    @Override
    public int hashCode() {
        return Objects
                .hash(getGameId(), getTitle(), getEsrbRating(), getDescription(), getPrice(), getStudio(),
                        getQuantity());
    }
}
